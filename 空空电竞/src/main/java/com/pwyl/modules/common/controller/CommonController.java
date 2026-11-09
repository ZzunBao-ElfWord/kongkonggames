package com.pwyl.modules.common.controller;

import com.pwyl.common.utils.Result;
import com.pwyl.modules.common.entity.CommonInfo;
import com.pwyl.modules.common.service.CommonInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "通用配置管理", tags = {"通用配置管理"})
@RequestMapping(value = "/common")
public class CommonController {
    @Autowired
    private CommonInfoService commonService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation("管理平台通用配置详情")
    @ResponseBody
    public Result getCommon(@PathVariable Integer id) {
        return Result.success().put("data",commonService.findOne(id));
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation("管理平台添加通用配置")
    @ResponseBody
    public Result addCommon(@RequestBody CommonInfo app) {

        return commonService.update(app);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ApiOperation("管理平台删除通用配置")
    public Result deleteCommon(@PathVariable int id) {
        return commonService.delete(id);
    }

    @RequestMapping(value = "/type/{type}", method = RequestMethod.GET)
    @ApiOperation("用户端根据type获取对象")
    @ResponseBody
    public Result getCommonList(@PathVariable Integer type) {
        return commonService.findByType(type);
    }


    @RequestMapping(value = "/type/condition/{condition}", method = RequestMethod.GET)
    @ApiOperation("根据condition去查询  xitong  xitongs  shouye")
    @ResponseBody
    public Result findByTypeAndCondition(@PathVariable String condition) {
        return commonService.findByTypeAndCondition(condition);
    }




}
