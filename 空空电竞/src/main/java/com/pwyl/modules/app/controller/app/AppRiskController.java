package com.pwyl.modules.app.controller.app;


import com.pwyl.common.utils.DateUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.annotation.Login;
import com.pwyl.modules.app.entity.Risk;
import com.pwyl.modules.app.service.RiskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController
@RequestMapping("/app/risk")
@Api(value = "风险管理", tags = {"风险管理"})
public class AppRiskController {

    @Autowired
    private RiskService riskService;

    @Login
    @PostMapping("/insertRisk")
    @ApiOperation("触发风险")
    public Result insertRisk(@RequestAttribute Long userId,Integer riskType, String content,Long byUserId){
        Risk risk=new Risk();
        if(byUserId!=null){
            risk.setUserId(byUserId);
        }else{
            risk.setUserId(userId);
        }
        if(riskType==6){
            risk.setContent("用户被举报："+content);
        }else{
            risk.setContent("系统检测触发关键词："+content);
        }
        risk.setRiskType(riskType);
        risk.setCreateTime(DateUtils.format(new Date()));
        riskService.save(risk);
        return Result.success();
    }








}
