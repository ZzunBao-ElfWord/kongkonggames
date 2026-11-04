package com.pwyl.modules.pay.apple.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.pwyl.modules.app.dao.UserVipDao;
import com.pwyl.modules.app.dao.VipDetailsDao;
import com.pwyl.modules.app.entity.UserVip;
import com.pwyl.modules.app.entity.VipDetails;
import com.pwyl.modules.pay.apple.config.AppleIapProperties;
import com.pwyl.modules.pay.apple.dao.AppleIapOrderDao;
import com.pwyl.modules.pay.apple.dto.AppleIapVerifyRequest;
import com.pwyl.modules.pay.apple.dto.Java11VerifyRequest;
import com.pwyl.modules.pay.apple.dto.Java11VerifyResponse;
import com.pwyl.modules.pay.apple.entity.AppleIapOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 苹果内购核心业务流程：预占 -> 调 Java11(官方 app-store-server-library)验票 ->
 * 本地事务发放柚币/会员 -> ack Java11 -> 返回 App 可 finish。
 *
 * 幂等设计：
 * - apple_iap_order.transaction_id 唯一索引预占，并发/重复请求只有一个进入验证；
 * - 状态机单向流转，所有状态更新均带原状态条件；
 * - 柚币流水 transaction_id 唯一索引 + 会员行锁，资产侧二次兜底；
 * - 已成功订单重复请求直接返回成功，App 可安全 finish。
 *
 * 注意：所有 HTTP 调用均在数据库事务之外，事务只包裹本地资产发放(见 AppleIapGrantService)。
 */
@Slf4j
@Service
public class AppleIapService {

    private static final String API_TYPE_COIN = "COIN";
    private static final String API_TYPE_MEMBERSHIP = "MEMBERSHIP";

    private final AppleIapProperties properties;
    private final Java11IapClient iapClient;
    private final AppleIapOrderDao orderDao;
    private final AppleIapGrantService grantService;
    private final UserVipDao userVipDao;
    private final VipDetailsDao vipDetailsDao;

    public AppleIapService(AppleIapProperties properties,
                           Java11IapClient iapClient,
                           AppleIapOrderDao orderDao,
                           AppleIapGrantService grantService,
                           UserVipDao userVipDao,
                           VipDetailsDao vipDetailsDao) {
        this.properties = properties;
        this.iapClient = iapClient;
        this.orderDao = orderDao;
        this.grantService = grantService;
        this.userVipDao = userVipDao;
        this.vipDetailsDao = vipDetailsDao;
    }

    // ====================================================================
    // 对外入口
    // ====================================================================

    public Map<String, Object> verifyAndGrant(Long userId, AppleIapVerifyRequest request) {
        return process(userId, request, AppleIapOrder.BIZ_COIN, API_TYPE_COIN);
    }

    public Map<String, Object> verifyMembershipAndGrant(Long userId, AppleIapVerifyRequest request) {
        return process(userId, request, AppleIapOrder.BIZ_MEMBERSHIP, API_TYPE_MEMBERSHIP);
    }

    // ====================================================================
    // 主流程
    // ====================================================================

    private Map<String, Object> process(Long userId, AppleIapVerifyRequest request,
                                        int expectedBizType, String expectedApiType) {
        validateRequest(userId, request);
        String now = IapTimeUtils.now();

        // 1. 查询或预占订单
        AppleIapOrder order = findByTransactionId(request.getTransactionId());
        boolean createdByCurrentRequest = false;
        if (order == null) {
            AppleIapOrder initOrder = buildInitOrder(userId, request, expectedBizType, now);
            if (orderDao.insertIgnore(initOrder) == 0) {
                order = findByTransactionId(request.getTransactionId());
                if (order == null) {
                    throw AppleIapException.retry("Apple 交易入账冲突，请稍后重试");
                }
            } else {
                createdByCurrentRequest = true;
                order = findByTransactionId(request.getTransactionId());
                if (order == null) {
                    throw AppleIapException.retry("Apple 交易已创建但暂时不可见，请稍后重试");
                }
            }
        }

        // 2. 已有订单：校验归属与业务类型，按状态分流
        if (!userId.equals(order.getUserId())) {
            throw AppleIapException.permanent("该 Apple 交易已绑定其他账户");
        }
        if (order.getBizType() != null && order.getBizType() != expectedBizType) {
            throw AppleIapException.permanent("该 Apple 交易商品与当前接口类型不匹配");
        }

        Integer status = order.getStatus() == null ? AppleIapOrder.STATUS_INIT : order.getStatus();
        switch (status) {
            case AppleIapOrder.STATUS_VERIFY_FAILED:
                throw AppleIapException.permanent(
                        order.getVerifyFailReason() == null ? "Apple 交易验证未通过" : order.getVerifyFailReason());
            case AppleIapOrder.STATUS_REFUNDED:
                throw AppleIapException.permanent("该 Apple 交易已退款或撤销");
            case AppleIapOrder.STATUS_ACK_SUCCESS:
                return buildResult(order, true);
            case AppleIapOrder.STATUS_DELIVERED:
                // 已发货但 ack 未完成，补 ack 后返回成功(资产已到账，App 可 finish)
                order = doAck(order);
                return buildResult(order, true);
            case AppleIapOrder.STATUS_PENDING_DELIVER:
                // 验证成功但发货中断，重新验票后补发
                return redeliver(order);
            case AppleIapOrder.STATUS_INIT:
            case AppleIapOrder.STATUS_VERIFYING:
            default:
                if (!createdByCurrentRequest && !isTimedOut(order)) {
                    throw AppleIapException.processing("订单处理中，请稍后重试");
                }
                // 超时则接管，继续向下重新验证
                break;
        }

        // 3. 抢占验证权：0/1 -> 1(验证中)，同时补存凭证
        UpdateWrapper<AppleIapOrder> verifying = new UpdateWrapper<AppleIapOrder>()
                .eq("id", order.getId())
                .in("status", AppleIapOrder.STATUS_INIT, AppleIapOrder.STATUS_VERIFYING)
                .set("status", AppleIapOrder.STATUS_VERIFYING)
                .set("update_time", now)
                .set(request.getReceiptData() != null, "receipt_data", request.getReceiptData())
                .set(request.getSignedTransactionInfo() != null, "signed_transaction", request.getSignedTransactionInfo());
        if (orderDao.update(null, verifying) == 0) {
            AppleIapOrder latest = orderDao.selectById(order.getId());
            if (latest != null && latest.getStatus() != null
                    && latest.getStatus() >= AppleIapOrder.STATUS_DELIVERED) {
                latest = doAck(latest);
                return buildResult(latest, true);
            }
            throw AppleIapException.processing("订单正在处理中，请稍后重试");
        }
        order = orderDao.selectById(order.getId());

        // 4. 调用 Java11 验票并做业务校验(HTTP 在事务外)
        Java11VerifyResponse verified;
        try {
            verified = verifyAndValidate(order, request, expectedApiType);
        } catch (AppleIapException e) {
            if (e.getCode() == AppleIapException.CODE_PERMANENT) {
                markFailed(order.getId(), e.getMessage());
            } else {
                touchVerifying(order.getId(), e.getMessage());
            }
            throw e;
        }

        // 5. 验证通过：1(验证中) -> 3(待发货)，写入苹果交易信息
        String verifyTime = IapTimeUtils.now();
        UpdateWrapper<AppleIapOrder> pending = new UpdateWrapper<AppleIapOrder>()
                .eq("id", order.getId())
                .eq("status", AppleIapOrder.STATUS_VERIFYING)
                .set("status", AppleIapOrder.STATUS_PENDING_DELIVER)
                .set("original_transaction_id", verified.getOriginalTransactionId())
                .set("product_id", verified.getProductId())
                .set("environment", verified.getEnvironment())
                .set("grant_value", verified.getGrantValue())
                .set("purchase_time", IapTimeUtils.formatInstant(verified.getPurchaseDate()))
                .set("expires_time", IapTimeUtils.formatInstant(verified.getExpiresDate()))
                .set("raw_response", JSON.toJSONString(verified))
                .set("verify_time", verifyTime)
                .set("verify_fail_reason", null)
                .set("update_time", verifyTime);
        if (request.getReceiptData() != null) {
            pending.set("receipt_data", request.getReceiptData());
        }
        if (request.getSignedTransactionInfo() != null) {
            pending.set("signed_transaction", request.getSignedTransactionInfo());
        }
        if (orderDao.update(null, pending) == 0) {
            AppleIapOrder latest = orderDao.selectById(order.getId());
            if (latest == null) {
                throw AppleIapException.retry("订单状态异常，请稍后重试");
            }
            if (latest.getStatus() >= AppleIapOrder.STATUS_DELIVERED) {
                latest = doAck(latest);
                return buildResult(latest, true);
            }
            if (latest.getStatus() == AppleIapOrder.STATUS_PENDING_DELIVER) {
                return redeliver(latest);
            }
            throw AppleIapException.processing("订单验证结果处理中，请稍后重试");
        }

        // 6. 本地事务发货 + ack
        order = orderDao.selectById(order.getId());
        order = deliverAndAck(order, verified);
        return buildResult(order, false);
    }

    // ====================================================================
    // 验票与校验
    // ====================================================================

    private Java11VerifyResponse verifyAndValidate(AppleIapOrder order, AppleIapVerifyRequest request,
                                                   String expectedApiType) {
        Java11VerifyRequest verifyRequest = new Java11VerifyRequest();
        verifyRequest.setUserId(order.getUserId());
        verifyRequest.setProductId(order.getProductId());
        verifyRequest.setTransactionId(order.getTransactionId());
        verifyRequest.setReceiptData(
                request.getReceiptData() != null ? request.getReceiptData() : order.getReceiptData());
        verifyRequest.setSignedTransactionInfo(
                request.getSignedTransactionInfo() != null
                        ? request.getSignedTransactionInfo() : order.getSignedTransaction());

        Java11VerifyResponse verified = iapClient.verify(verifyRequest);

        // 商品类型必须与接口匹配，防止用柚币单调会员接口或反之
        if (verified.getProductType() == null
                || !expectedApiType.equalsIgnoreCase(verified.getProductType())) {
            throw AppleIapException.permanent("Apple 交易类型与接口不匹配");
        }
        // 交易 id 必须一致(Java11 已校验，这里双保险)
        if (verified.getTransactionId() == null
                || !verified.getTransactionId().equals(order.getTransactionId())) {
            throw AppleIapException.permanent("Apple 返回的交易标识不一致");
        }
        // 生产环境拒绝沙盒交易
        if (!properties.isAllowSandbox() && verified.getEnvironment() != null
                && "Sandbox".equalsIgnoreCase(verified.getEnvironment())) {
            throw AppleIapException.permanent("沙盒交易不能在正式环境发放");
        }
        return verified;
    }

    // ====================================================================
    // 发货与 ack
    // ====================================================================

    /**
     * 待发货订单补发：重新验票后走发货+ack。
     */
    private Map<String, Object> redeliver(AppleIapOrder order) {
        AppleIapVerifyRequest request = new AppleIapVerifyRequest();
        request.setProductId(order.getProductId());
        request.setTransactionId(order.getTransactionId());
        request.setReceiptData(order.getReceiptData());
        request.setSignedTransactionInfo(order.getSignedTransaction());
        String apiType = AppleIapOrder.BIZ_MEMBERSHIP == order.getBizType() ? API_TYPE_MEMBERSHIP : API_TYPE_COIN;
        Java11VerifyResponse verified = verifyAndValidate(order, request, apiType);
        AppleIapOrder latest = deliverAndAck(order, verified);
        return buildResult(latest, true);
    }

    /**
     * 事务发货(幂等)，发货成功后调用 ack。ack 失败不影响资产，仅标记由定时任务重试。
     */
    private AppleIapOrder deliverAndAck(AppleIapOrder order, Java11VerifyResponse verified) {
        AppleIapOrder delivered = grantService.grant(order, verified);
        if (delivered.getStatus() == null) {
            return delivered;
        }
        if (delivered.getStatus() == AppleIapOrder.STATUS_ACK_SUCCESS) {
            return delivered;
        }
        if (delivered.getStatus() == AppleIapOrder.STATUS_DELIVERED) {
            return doAck(delivered);
        }
        // 仍停留在待发货/验证中，说明本次发货条件未达成，返回让客户端重试
        throw AppleIapException.processing("发货处理中，请稍后重试");
    }

    /**
     * 调用 Java11 ack。任何失败都不抛给前端，资产已到账即视为成功。
     */
    private AppleIapOrder doAck(AppleIapOrder order) {
        String now = IapTimeUtils.now();
        String businessOrderNo = order.getBusinessOrderNo();
        if (businessOrderNo == null || businessOrderNo.trim().isEmpty()) {
            businessOrderNo = "APPLE_IAP_" + order.getId();
        }
        try {
            iapClient.acknowledgeGrant(order.getTransactionId(), order.getUserId(), businessOrderNo);
            UpdateWrapper<AppleIapOrder> ok = new UpdateWrapper<AppleIapOrder>()
                    .eq("id", order.getId())
                    .eq("status", AppleIapOrder.STATUS_DELIVERED)
                    .set("status", AppleIapOrder.STATUS_ACK_SUCCESS)
                    .set("ack_status", AppleIapOrder.ACK_SUCCESS)
                    .set("ack_time", now)
                    .set("business_order_no", businessOrderNo)
                    .set("update_time", now);
            orderDao.update(null, ok);
        } catch (AppleIapException e) {
            log.warn("Apple IAP ack 异步待重试 orderId={}, txnId={}, reason={}",
                    order.getId(), order.getTransactionId(), e.getMessage());
            int retryCount = order.getAckRetryCount() == null ? 0 : order.getAckRetryCount();
            UpdateWrapper<AppleIapOrder> fail = new UpdateWrapper<AppleIapOrder>()
                    .eq("id", order.getId())
                    .set("ack_status", AppleIapOrder.ACK_RETRY)
                    .set("ack_retry_count", retryCount + 1)
                    .set("business_order_no", businessOrderNo)
                    .set("update_time", now);
            orderDao.update(null, fail);
        }
        return orderDao.selectById(order.getId());
    }

    // ====================================================================
    // 定时兜底任务入口
    // ====================================================================

    /**
     * 扫描卡在待验证/验证中的超时订单，重新验证发放。
     */
    public void retryVerifyingOrders() {
        String deadline = timeoutDeadline();
        List<AppleIapOrder> orders = orderDao.selectList(new QueryWrapper<AppleIapOrder>()
                .in("status", AppleIapOrder.STATUS_INIT, AppleIapOrder.STATUS_VERIFYING)
                .le("update_time", deadline)
                .orderByAsc("id")
                .last("limit " + Math.max(1, properties.getRetryBatchSize())));
        for (AppleIapOrder order : orders) {
            try {
                AppleIapVerifyRequest request = new AppleIapVerifyRequest();
                request.setProductId(order.getProductId());
                request.setTransactionId(order.getTransactionId());
                request.setReceiptData(order.getReceiptData());
                request.setSignedTransactionInfo(order.getSignedTransaction());
                int bizType = order.getBizType() == null ? AppleIapOrder.BIZ_COIN : order.getBizType();
                String apiType = bizType == AppleIapOrder.BIZ_MEMBERSHIP ? API_TYPE_MEMBERSHIP : API_TYPE_COIN;
                process(order.getUserId(), request, bizType, apiType);
            } catch (AppleIapException e) {
                log.warn("定时重试验证未完成 orderId={}, txnId={}, code={}, msg={}",
                        order.getId(), order.getTransactionId(), e.getCode(), e.getMessage());
            } catch (Exception e) {
                log.error("定时重试验证异常 orderId=" + order.getId()
                        + ", txnId=" + order.getTransactionId(), e);
            }
        }
    }

    /**
     * 扫描验证通过但发货中断的订单，重新验票并补发。
     */
    public void retryPendingDeliverOrders() {
        List<AppleIapOrder> orders = orderDao.selectList(new QueryWrapper<AppleIapOrder>()
                .eq("status", AppleIapOrder.STATUS_PENDING_DELIVER)
                .orderByAsc("id")
                .last("limit " + Math.max(1, properties.getRetryBatchSize())));
        for (AppleIapOrder order : orders) {
            try {
                redeliver(order);
            } catch (AppleIapException e) {
                log.warn("定时补发未完成 orderId={}, txnId={}, code={}, msg={}",
                        order.getId(), order.getTransactionId(), e.getCode(), e.getMessage());
            } catch (Exception e) {
                log.error("定时补发异常 orderId=" + order.getId()
                        + ", txnId=" + order.getTransactionId(), e);
            }
        }
    }

    /**
     * 扫描已发货待 ack 的订单，重试 ack。
     */
    public void retryPendingAckOrders() {
        List<AppleIapOrder> orders = orderDao.selectList(new QueryWrapper<AppleIapOrder>()
                .eq("status", AppleIapOrder.STATUS_DELIVERED)
                .in("ack_status", AppleIapOrder.ACK_PENDING, AppleIapOrder.ACK_RETRY)
                .lt("ack_retry_count", Math.max(1, properties.getAckMaxRetry()))
                .orderByAsc("id")
                .last("limit " + Math.max(1, properties.getRetryBatchSize())));
        for (AppleIapOrder order : orders) {
            try {
                AppleIapOrder latest = doAck(order);
                if (latest.getStatus() != null
                        && latest.getStatus() == AppleIapOrder.STATUS_ACK_SUCCESS) {
                    log.info("定时 ack 成功 orderId={}, txnId={}", order.getId(), order.getTransactionId());
                }
            } catch (Exception e) {
                log.error("定时 ack 异常 orderId=" + order.getId()
                        + ", txnId=" + order.getTransactionId(), e);
            }
        }
    }

    // ====================================================================
    // 私有辅助方法
    // ====================================================================

    private void validateRequest(Long userId, AppleIapVerifyRequest request) {
        if (!properties.isEnabled()) {
            throw AppleIapException.permanent("Apple 内购服务尚未启用");
        }
        if (userId == null) {
            throw AppleIapException.permanent("登录状态已失效，请重新登录");
        }
        if (request == null) {
            throw AppleIapException.permanent("Apple 交易信息不完整");
        }
        if (isBlank(request.getProductId())) {
            throw AppleIapException.permanent("缺少 Apple 商品标识");
        }
        if (isBlank(request.getTransactionId())) {
            throw AppleIapException.permanent("缺少 Apple 交易标识");
        }
    }

    private AppleIapOrder buildInitOrder(Long userId, AppleIapVerifyRequest request, int bizType, String now) {
        AppleIapOrder order = new AppleIapOrder();
        order.setUserId(userId);
        order.setTransactionId(request.getTransactionId().trim());
        order.setProductId(request.getProductId().trim());
        order.setBizType(bizType);
        order.setCoinAmount(BigDecimal.ZERO);
        order.setStatus(AppleIapOrder.STATUS_INIT);
        order.setAckStatus(AppleIapOrder.ACK_PENDING);
        order.setAckRetryCount(0);
        order.setGrantValue(0);
        order.setReceiptData(request.getReceiptData());
        order.setSignedTransaction(request.getSignedTransactionInfo());
        order.setCreateTime(now);
        order.setUpdateTime(now);
        return order;
    }

    private AppleIapOrder findByTransactionId(String transactionId) {
        return orderDao.selectOne(
                new QueryWrapper<AppleIapOrder>().eq("transaction_id", transactionId));
    }

    private boolean isTimedOut(AppleIapOrder order) {
        String base = order.getUpdateTime() != null ? order.getUpdateTime() : order.getCreateTime();
        if (base == null) {
            return true;
        }
        try {
            java.util.Date update = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(base);
            long elapsed = System.currentTimeMillis() - update.getTime();
            return elapsed > properties.getOrderTimeoutSeconds() * 1000L;
        } catch (Exception e) {
            return true;
        }
    }

    private String timeoutDeadline() {
        int seconds = properties.getOrderTimeoutSeconds() > 0 ? properties.getOrderTimeoutSeconds() : 90;
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.add(java.util.Calendar.SECOND, -seconds);
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
    }

    private void markFailed(Long orderId, String reason) {
        String safeReason = reason == null ? "Apple 交易验证未通过"
                : (reason.length() > 480 ? reason.substring(0, 480) : reason);
        UpdateWrapper<AppleIapOrder> failed = new UpdateWrapper<AppleIapOrder>()
                .eq("id", orderId)
                .in("status", AppleIapOrder.STATUS_INIT, AppleIapOrder.STATUS_VERIFYING)
                .set("status", AppleIapOrder.STATUS_VERIFY_FAILED)
                .set("verify_fail_reason", safeReason)
                .set("update_time", IapTimeUtils.now());
        orderDao.update(null, failed);
    }

    private void touchVerifying(Long orderId, String reason) {
        UpdateWrapper<AppleIapOrder> verifying = new UpdateWrapper<AppleIapOrder>()
                .eq("id", orderId)
                .set("status", AppleIapOrder.STATUS_VERIFYING)
                .set("verify_fail_reason", reason)
                .set("update_time", IapTimeUtils.now());
        orderDao.update(null, verifying);
    }

    private Map<String, Object> buildResult(AppleIapOrder order, boolean alreadyProcessed) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("transactionId", order.getTransactionId());
        result.put("productId", order.getProductId());
        result.put("environment", order.getEnvironment());
        result.put("alreadyProcessed", alreadyProcessed);

        if (AppleIapOrder.BIZ_MEMBERSHIP == order.getBizType()) {
            // 实时查询会员状态返回，避免用户多笔购买后订单记录的到期时间不是最新
            UserVip userVip = userVipDao.selectOne(
                    new QueryWrapper<UserVip>().eq("user_id", order.getUserId()));
            String endTime = userVip == null ? order.getVipEndTime() : userVip.getEndTime();
            Integer vipNameType = userVip == null ? order.getVipNameType() : userVip.getVipNameType();
            String vipName = null;
            if (vipNameType != null) {
                VipDetails vipDetails = vipDetailsDao.selectOne(
                        new QueryWrapper<VipDetails>().eq("vip_name_type", vipNameType));
                if (vipDetails != null) {
                    vipName = vipDetails.getVipName();
                }
            }
            result.put("vipNameType", vipNameType);
            result.put("vipName", vipName);
            result.put("endTime", endTime);
        } else {
            result.put("coinAmount", order.getCoinAmount());
        }
        return result;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
