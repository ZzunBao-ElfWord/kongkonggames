package com.pwyl.modules.pay.apple.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 苹果内购 Java8 业务后端配置。
 * 商品与发放值统一在 Java11 验证服务 ProductCatalog 中维护，Java8 不重复配置商品数量，
 * 一切以 Java11 验签后返回的 productType / grantValue 为准。
 */
@Data
@Component
@ConfigurationProperties(prefix = "apple.iap")
public class AppleIapProperties {
    /** 总开关 */
    private boolean enabled = true;
    /** Java11 苹果验证服务地址，例如 http://127.0.0.1:8091 */
    private String baseUrl = "http://127.0.0.1:8091";
    /** 调用 Java11 /internal 接口的内部令牌，请与 Java11 的 IAP_INTERNAL_TOKEN 保持一致 */
    private String internalToken;
    /** 连接超时(毫秒) */
    private int connectTimeout = 5000;
    /** 读取超时(毫秒) */
    private int readTimeout = 10000;
    /** 是否允许沙盒交易发放正式资产：本地/TestFlight 测试期 true，App Store 正式上线必须 false */
    private boolean allowSandbox = true;
    /** 订单处于待验证/验证中状态超过该秒数后，允许请求接管或定时任务重试 */
    private int orderTimeoutSeconds = 90;
    /** ack 最大重试次数，超过后仅记录日志(资产已到账，不影响用户)，定时任务仍会继续尝试 */
    private int ackMaxRetry = 50;
    /** 定时任务每批处理订单数量 */
    private int retryBatchSize = 20;
}
