package com.pwyl.modules.pay.apple.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pwyl.modules.pay.apple.config.AppleIapProperties;
import com.pwyl.modules.pay.apple.dto.Java11AckRequest;
import com.pwyl.modules.pay.apple.dto.Java11VerifyRequest;
import com.pwyl.modules.pay.apple.dto.Java11VerifyResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * Java11 苹果验证服务客户端。
 * 只负责 HTTP 通信与错误码分类，不处理业务发放。
 * 永久失败(400/404/409/422)：交易无效、伪造、退款、绑定他人、商品不匹配，不再重试。
 * 临时失败(401/429/5xx/网络超时)：保留订单，由客户端重试或定时任务兜底。
 */
@Slf4j
@Component
public class Java11IapClient {

    private static final String VERIFY_PATH = "/internal/v1/apple/transactions/verify";
    private static final String ACK_PATH_PREFIX = "/internal/v1/apple/transactions/";
    private static final String ACK_PATH_SUFFIX = "/ack";

    private final AppleIapProperties properties;
    private final RestTemplate restTemplate;

    public Java11IapClient(AppleIapProperties properties) {
        this.properties = properties;
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(properties.getConnectTimeout() > 0 ? properties.getConnectTimeout() : 5000);
        factory.setReadTimeout(properties.getReadTimeout() > 0 ? properties.getReadTimeout() : 10000);
        this.restTemplate = new RestTemplate(factory);
    }

    /**
     * 请求 Java11 验证交易。
     */
    public Java11VerifyResponse verify(Java11VerifyRequest request) {
        String url = trimBaseUrl() + VERIFY_PATH;
        HttpHeaders headers = buildHeaders();
        String body = JSON.toJSONString(request);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    url, new HttpEntity<String>(body, headers), String.class);
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw AppleIapException.retry("Apple 验证服务返回异常状态：" + response.getStatusCodeValue());
            }
            Java11VerifyResponse result = JSON.parseObject(response.getBody(), Java11VerifyResponse.class);
            if (result == null || !result.isValid()) {
                throw AppleIapException.permanent("Apple 交易验证未通过");
            }
            return result;
        } catch (AppleIapException e) {
            throw e;
        } catch (HttpStatusCodeException e) {
            throw mapHttpError("验证", e);
        } catch (ResourceAccessException e) {
            log.error("调用 Apple 验证服务网络异常: {}", e.getMessage());
            throw AppleIapException.retry("Apple 验证服务连接超时，请稍后重试", e);
        } catch (Exception e) {
            log.error("调用 Apple 验证服务返回解析失败", e);
            throw AppleIapException.retry("Apple 验证服务返回异常，请稍后重试", e);
        }
    }

    /**
     * 通知 Java11 业务侧已完成发放。Java11 侧 ack 本身幂等，重复调用返回成功。
     */
    public void acknowledgeGrant(String transactionId, Long userId, String businessOrderNo) {
        String url = trimBaseUrl() + ACK_PATH_PREFIX + transactionId + ACK_PATH_SUFFIX;
        Java11AckRequest ackRequest = new Java11AckRequest();
        ackRequest.setUserId(userId);
        ackRequest.setBusinessOrderNo(businessOrderNo);
        HttpHeaders headers = buildHeaders();
        String body = JSON.toJSONString(ackRequest);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    url, new HttpEntity<String>(body, headers), String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw AppleIapException.retry("Apple ack 返回异常状态：" + response.getStatusCodeValue());
            }
        } catch (AppleIapException e) {
            throw e;
        } catch (HttpStatusCodeException e) {
            int status = e.getStatusCode().value();
            // 401 多为内部令牌配置错误，429/5xx 为临时故障，均交给定时任务重试并告警
            String responseBody = e.getResponseBodyAsString();
            log.warn("Apple ack 失败 httpStatus={}, body={}", status, responseBody);
            throw AppleIapException.retry("Apple ack 失败，等待异步重试");
        } catch (ResourceAccessException e) {
            log.warn("Apple ack 网络异常: {}", e.getMessage());
            throw AppleIapException.retry("Apple ack 网络异常，等待异步重试", e);
        } catch (Exception e) {
            log.error("Apple ack 调用异常", e);
            throw AppleIapException.retry("Apple ack 异常，等待异步重试", e);
        }
    }

    private AppleIapException mapHttpError(String action, HttpStatusCodeException e) {
        int httpStatus = e.getStatusCode().value();
        String responseBody = e.getResponseBodyAsString();
        String remoteCode = null;
        String remoteMessage = null;
        try {
            JSONObject errorJson = JSON.parseObject(responseBody);
            if (errorJson != null) {
                remoteCode = errorJson.getString("code");
                remoteMessage = errorJson.getString("message");
            }
        } catch (Exception ignored) {
            // 错误体非 JSON 时按状态码处理
        }
        log.warn("Apple {}失败 httpStatus={}, remoteCode={}, body={}", action, httpStatus, remoteCode, responseBody);

        // 400 参数/票据问题、404 交易不存在、409 交易归属冲突、422 验签/商品/退款等业务拒绝：永久失败
        if (httpStatus == 400 || httpStatus == 404 || httpStatus == 409 || httpStatus == 422) {
            String message = describePermanentError(remoteCode, remoteMessage);
            return AppleIapException.permanent(message);
        }
        // 401/403 令牌问题、429 限流、5xx 服务异常：可重试
        return AppleIapException.retry("Apple 验证服务暂时不可用，请稍后重试(" + httpStatus + ")");
    }

    private String describePermanentError(String remoteCode, String remoteMessage) {
        if (remoteCode == null) {
            return "Apple 交易验证未通过";
        }
        switch (remoteCode) {
            case "TRANSACTION_NOT_FOUND":
                return "Apple 交易不存在或环境不匹配";
            case "TRANSACTION_OWNED_BY_ANOTHER_USER":
                return "该 Apple 交易已绑定其他账户";
            case "TRANSACTION_PRODUCT_CONFLICT":
                return "该 Apple 交易与商品信息不匹配";
            case "BUNDLE_ID_MISMATCH":
                return "Apple 交易不属于当前应用";
            case "PRODUCT_ID_MISMATCH":
            case "UNKNOWN_PRODUCT":
                return "Apple 商品信息不匹配";
            case "TRANSACTION_ID_MISMATCH":
                return "Apple 交易标识不一致";
            case "TRANSACTION_REVOKED":
                return "该 Apple 交易已被撤销或退款";
            case "SUBSCRIPTION_EXPIRED":
                return "该 Apple 会员订阅已过期";
            case "INVALID_APPLE_TRANSACTION":
            case "INVALID_APPLE_SIGNATURE":
            case "INVALID_RECEIPT":
                return "Apple 交易票据无效";
            case "MISSING_APPLE_PROOF":
            case "MISSING_TRANSACTION_ID":
                return "Apple 交易凭证不完整";
            default:
                return remoteMessage != null ? ("Apple 交易验证未通过：" + remoteMessage) : "Apple 交易验证未通过";
        }
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (properties.getInternalToken() != null && !properties.getInternalToken().trim().isEmpty()) {
            headers.add("X-Internal-Token", properties.getInternalToken().trim());
        }
        return headers;
    }

    private String trimBaseUrl() {
        String baseUrl = properties.getBaseUrl();
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            throw AppleIapException.retry("服务端未配置 Apple 验证服务地址");
        }
        while (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        return baseUrl;
    }
}
