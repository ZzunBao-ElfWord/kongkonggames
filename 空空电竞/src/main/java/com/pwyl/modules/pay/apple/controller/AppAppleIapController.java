package com.pwyl.modules.pay.apple.controller;

import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.annotation.Login;
import com.pwyl.modules.pay.apple.dto.AppleIapVerifyRequest;
import com.pwyl.modules.pay.apple.service.AppleIapException;
import com.pwyl.modules.pay.apple.service.AppleIapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * App 端苹果内购验证接口。
 *
 * 返回码约定(供 DCloud 客户端决定是否 finishTransaction)：
 * - 0           成功(含重复交易幂等成功)，客户端必须调用 finishTransaction；
 * - 202         订单处理中，客户端稍后轮询，禁止 finish；
 * - 4002        永久失败(交易无效/伪造/退款/绑定他人)，客户端可 finish 丢弃该交易；
 * - 5001        临时失败(验证服务/网络/发货异常)，客户端重试，禁止 finish。
 */
@Slf4j
@RestController
@RequestMapping("/app/appleIap")
public class AppAppleIapController {

    private final AppleIapService appleIapService;

    public AppAppleIapController(AppleIapService appleIapService) {
        this.appleIapService = appleIapService;
    }

    /**
     * 柚币(消耗型)内购验证并发币。
     */
    @Login
    @PostMapping("/verify")
    public Result verify(@RequestAttribute("userId") Long userId,
                         @RequestBody AppleIapVerifyRequest request) {
        try {
            return Result.success().put("data", appleIapService.verifyAndGrant(userId, request));
        } catch (AppleIapException e) {
            log.warn("Apple IAP 柚币验证未完成 userId={}, code={}, msg={}",
                    userId, e.getCode(), e.getMessage());
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("Apple IAP 柚币验证系统异常 userId=" + userId, e);
            return Result.error(AppleIapException.CODE_RETRY, "Apple 订单处理异常，请稍后重试");
        }
    }

    /**
     * 会员(非续期)内购验证并开通/叠加会员时长。
     */
    @Login
    @PostMapping("/verifyMembership")
    public Result verifyMembership(@RequestAttribute("userId") Long userId,
                                   @RequestBody AppleIapVerifyRequest request) {
        try {
            return Result.success().put("data", appleIapService.verifyMembershipAndGrant(userId, request));
        } catch (AppleIapException e) {
            log.warn("Apple IAP 会员验证未完成 userId={}, code={}, msg={}",
                    userId, e.getCode(), e.getMessage());
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("Apple IAP 会员验证系统异常 userId=" + userId, e);
            return Result.error(AppleIapException.CODE_RETRY, "Apple 订单处理异常，请稍后重试");
        }
    }
}
