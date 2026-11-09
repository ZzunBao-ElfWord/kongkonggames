package com.pwyl.modules.app.controller;

import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.service.UserMoneyDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Details")
@AllArgsConstructor
@Api("钱包明细")
public class UserMoneyDetailsController {
    private UserMoneyDetailsService userMoneyDetailsService;

    @ApiOperation("钱包明细")
    @GetMapping("/queryUserMoneyDetails")
    public Result queryUserMoneyDetails(Integer page, Integer limit, Long userId,Integer classify,Integer type) {

        return userMoneyDetailsService.queryUserMoneyDetails(page, limit, userId,classify,type);
    }



}
