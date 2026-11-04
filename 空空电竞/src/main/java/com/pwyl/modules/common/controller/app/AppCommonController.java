package com.pwyl.modules.common.controller.app;

import com.pwyl.common.utils.Result;
import com.pwyl.modules.common.service.CommonInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "通用配置管理", tags = {"通用配置管理"})
@RequestMapping(value = "/app/common")
public class AppCommonController {
    @Autowired
    private CommonInfoService commonService;

  


    @RequestMapping(value = "/type/{type}", method = RequestMethod.GET)
    @ApiOperation("用户端根据type获取对象")
    @ResponseBody
    public Result getCommonList(@PathVariable Integer type) {
        return commonService.findByType(type);
    }





}
