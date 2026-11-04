package com.pwyl.modules.app.controller;


import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.app.service.RiskService;
import com.pwyl.modules.app.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * APP登录授权
 *
 */
@RestController
@RequestMapping("/risk")
@Api(value = "风险管理", tags = {"风险管理"})
public class RiskController {

    @Autowired
    private RiskService riskService;
    @Autowired
    private UserService userService;


    @GetMapping("/selectRiskList")
    @ApiOperation("查询风险列表")
    public Result selectRiskList(Integer page,Integer limit,String userName,String phone,Integer riskType){
        return riskService.selectRiskList(page, limit, userName, phone, riskType);
    }

    @GetMapping("/selectRiskGroupList")
    @ApiOperation("查询风险列表分组")
    public Result selectRiskGroupList(Integer page,Integer limit, String userName, String phone, Integer riskSort){
        return riskService.selectRiskGroupList(page, limit, userName, phone, riskSort);
    }

    @PostMapping("/updateRiskUser")
    @ApiOperation("标记为风险用户或取消标记")
    public Result updateRiskUser(Long userId){
        UserEntity user = userService.getById(userId);
        if(user.getRisk()!=null && user.getRisk()==1){
            user.setRisk(2);
        }else{
            user.setRisk(1);
        }
        userService.updateById(user);
        return Result.success();
    }


}
