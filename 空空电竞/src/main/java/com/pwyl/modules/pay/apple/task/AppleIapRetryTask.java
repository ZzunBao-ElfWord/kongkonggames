package com.pwyl.modules.pay.apple.task;

import com.pwyl.modules.pay.apple.config.AppleIapProperties;
import com.pwyl.modules.pay.apple.service.AppleIapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 苹果内购订单兜底定时任务。
 * 处理因 App 杀进程、网络抖动、服务重启等原因停留在中间状态的订单：
 * 1. 待验证/验证中超时 -> 重新调用 Java11 验证并发放；
 * 2. 待发货 -> 重新验票并补发柚币/会员；
 * 3. 已发货待 ack -> 重试通知 Java11。
 * 单条订单失败不影响其他订单，所有方法均为幂等执行。
 */
@Slf4j
@Component
public class AppleIapRetryTask {

    private final AppleIapProperties properties;
    private final AppleIapService appleIapService;

    public AppleIapRetryTask(AppleIapProperties properties, AppleIapService appleIapService) {
        this.properties = properties;
        this.appleIapService = appleIapService;
    }

    /**
     * 每 60 秒扫描一次卡在验证环节且超时的订单。
     */
    @Scheduled(fixedDelay = 60_000L, initialDelay = 30_000L)
    public void retryVerifying() {
        if (!properties.isEnabled()) {
            return;
        }
        try {
            appleIapService.retryVerifyingOrders();
        } catch (Exception e) {
            log.error("Apple IAP 定时重试验证任务执行异常", e);
        }
    }

    /**
     * 每 2 分钟扫描一次验证通过但发货中断的订单。
     */
    @Scheduled(fixedDelay = 120_000L, initialDelay = 60_000L)
    public void retryPendingDeliver() {
        if (!properties.isEnabled()) {
            return;
        }
        try {
            appleIapService.retryPendingDeliverOrders();
        } catch (Exception e) {
            log.error("Apple IAP 定时补发任务执行异常", e);
        }
    }

    /**
     * 每 5 分钟重试一次已发货但 ack 未完成的订单。
     */
    @Scheduled(fixedDelay = 300_000L, initialDelay = 120_000L)
    public void retryPendingAck() {
        if (!properties.isEnabled()) {
            return;
        }
        try {
            appleIapService.retryPendingAckOrders();
        } catch (Exception e) {
            log.error("Apple IAP 定时 ack 任务执行异常", e);
        }
    }
}
