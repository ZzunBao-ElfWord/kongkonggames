package com.pwyl.modules.app.controller.app;

import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.annotation.Login;
import com.pwyl.modules.app.entity.UserCertification;
import com.pwyl.modules.app.service.UserCertificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@AllArgsConstructor
@RequestMapping("/app/userCertification")
@Api("app实 名 认 证")
public class AppUserCertificationController {

    private UserCertificationService userCertificationService;

    /**
     * 实名认证
     **/
    @RequestMapping("/insert")
    @Login
    @ApiOperation("实名认证")
    public Result insert(@RequestAttribute Long userId, UserCertification userCertification) {
        userCertification.setUserId(userId);
        return userCertificationService.insert(userCertification);

    }


    @Login
    @RequestMapping("/updateUserCertification")
    @ApiOperation("实名认证")
    public Result updateUserCertification(@RequestAttribute Long userId,@RequestBody UserCertification userCertification) {
        userCertification.setUserId(userId);
        return userCertificationService.updateUserCertification(userCertification);

    }

    /**
     * 是否进行实名
     */
    @RequestMapping("/isInsert")
    @Login
    @ApiOperation("是否进行实名")
    public Result isInsert(@RequestAttribute Long userId) {
        return userCertificationService.isInsert(userId);
    }

    /**
     * 查询自己的实名信息
     */
    @RequestMapping("/queryInsert")
    @Login
    @ApiOperation("查询自己的实名信息")
    public Result queryInsert(@RequestAttribute Long userId) {

        return userCertificationService.queryInsert(userId);
    }
}
