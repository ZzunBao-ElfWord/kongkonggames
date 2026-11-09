package com.pwyl.modules.pay.controller.app;


import com.pwyl.common.utils.PageUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.annotation.Login;
import com.pwyl.modules.app.service.UserMoneyDetailsService;
import com.pwyl.modules.pay.service.CashOutService;
import com.pwyl.modules.pay.service.PayDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fang
 * @date 2020/5/15
 */
@Slf4j
@RestController
@Api(value = "提现", tags = {"提现"})
@RequestMapping(value = "/app/cash")
public class AppCashController {

    /** 提现记录 */
    @Autowired
    private CashOutService cashOutService;
    @Autowired
    private PayDetailsService payDetailsService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;


    @Login
    @GetMapping(value = "/cashMoney")
    @ApiOperation("发起提现")
    public Result cashMoney(@RequestAttribute("userId") Long userId, Double money,Integer classify)
    {
        return cashOutService.cashMoney(userId,money,classify);
    }

    @Login
    @RequestMapping(value = "/selectUserRechargeByUserId", method = RequestMethod.GET)
    @ApiOperation("查询某个用户充值信息列表")
    @ResponseBody
    public Result selectUserRechargeByUserId(int page, int limit, String startTime, String endTime, @RequestAttribute("userId") Long userId, Integer state){
        return Result.success().put("data",payDetailsService.selectPayDetails(page,limit,startTime,endTime,userId,state));
    }

    @Login
    @RequestMapping(value = "/selectPayDetails", method = RequestMethod.GET)
    @ApiOperation("查询提现记录列表")
    @ResponseBody
    public Result selectHelpProfit(Integer page, Integer limit, String zhifubaoName, String zhifubao, @RequestAttribute("userId") Long userId, Integer classify, Integer state, Integer cashType, String startTime, String endTime) {
        PageUtils pageUtils = cashOutService.selectCashOutList(page, limit, zhifubaoName, zhifubao, String.valueOf(userId), classify, state, cashType,startTime,endTime);
        return Result.success().put("data", pageUtils);
    }

    @Login
    @ApiOperation("钱包明细")
    @GetMapping("/queryUserMoneyDetails")
    public Result queryUserMoneyDetails(Integer page, Integer limit, @RequestAttribute("userId") Long userId, Integer classify, Integer type) {
        return userMoneyDetailsService.queryUserMoneyDetails(page, limit, userId,classify,type);
    }


}