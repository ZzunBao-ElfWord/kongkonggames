package com.pwyl.modules.app.controller.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.annotation.Login;
import com.pwyl.modules.app.entity.UserMoneyDetails;
import com.pwyl.modules.app.service.UserMoneyDetailsService;
import com.pwyl.modules.app.service.UserMoneyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/app/userMoney")
@Api("app 钱包 ")
public class AppUserMoneyController {

    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;

    @Login
    @GetMapping("/selectConsortiaPersonMoneyDetails")
    @ApiOperation("团队收益明细")
    public Result selectConsortiaPersonMoneyDetails(Integer page, Integer limit, Long byUserId, @RequestAttribute Long userId, Integer classify) {
        return userMoneyDetailsService.selectUserMoneyDetailsUserIdAndByUserId(page, limit, userId, byUserId, classify);
    }


    @GetMapping("/selectMyMoney")
    @Login
    @ApiOperation("我的钱包余额")
    public Result selectMyMoney(@RequestAttribute Long userId) {
        return Result.success().put("data", userMoneyService.selectUserMoneyByUserId(userId));
    }

    /**
     * 我的收益
     */
    @GetMapping("/selectMyProfit")
    @Login
    @ApiOperation("我的收益")
    public Result selectMyProfit(@RequestAttribute Long userId) {
        return Result.success().put("data", userMoneyService.selectMyProfit(userId));
    }

    /**
     * 付款接单
     */
    @GetMapping("/payTakingOrder")
    @Login
    @ApiOperation("付款接单或会员")
    public Result payTakingOrder(@RequestAttribute Long userId, Long orderId) {
        return Result.success().put("data", userMoneyService.payTakingOrder(userId, orderId));
    }



    /**
     * 余额明细
     */
    @GetMapping("/balanceDetailed")
    @Login
    @ApiOperation("余额明细")
    public Result balanceDetailed(@RequestAttribute Long userId, Long page, Long limit,Integer classify) {
        Page<UserMoneyDetails> pages = new Page<>(page, limit);
        return Result.success().put("data", userMoneyService.balanceDetailed(userId, pages,classify));
    }


    /**
     * 收益明细
     */
    @GetMapping("/profitDetailed")
    @Login
    @ApiOperation("收益明细")
    public Result profitDetailed(@RequestAttribute Long userId, Long page, Long limit) {
        if (page != null || limit != null) {
            return Result.error("分页条件为空");
        } else {
            IPage ipage = new Page(page, limit);
            return Result.success().put("data", userMoneyService.profitDetailed(userId, ipage));
        }
    }

    @Login
    @PostMapping("/inviteMoneyConvertMoney")
    @ApiOperation("收益转余额")
    public Result inviteMoneyConvertMoney(@RequestAttribute Long userId,Long payClassifyId){
        return userMoneyService.inviteMoneyConvertMoney(userId, payClassifyId);
    }


}
