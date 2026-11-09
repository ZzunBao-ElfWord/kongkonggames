package com.pwyl.modules.app.controller;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.service.UserBrowseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
@RestController
@AllArgsConstructor
@RequestMapping("/userBrowse")
@Api(value = "访客|浏览", tags = {"访客|浏览"})
public class UserBrowseController {
    private UserBrowseService userBrowseService;

    @ApiOperation("查询我的访客")
    @RequestMapping(value = "/myVisitor", method = RequestMethod.GET)
    public Result selectMyVisitor(@ApiParam("用户id") Long userId, @ApiParam("页") Long page, @ApiParam("行") Long limit) {
        return userBrowseService.selectMyVisitor(userId, page, limit);
    }

    @RequestMapping(value = "/myBrowse", method = RequestMethod.GET)
    @ApiOperation("浏览足迹")
    public Result selectMyBrowse(Long userId, Long page, Long limit) {
        return userBrowseService.selectMyBrowse(userId, page, limit);
    }

    @ApiOperation("删除我的访客")
    @RequestMapping(value = "/deleteMyVisitor", method = RequestMethod.POST)
    public Result deleteMyVisitor(Long id) {

        return userBrowseService.deleteMyVisitor(id);
    }
    @ApiOperation(("删除足迹"))
    @RequestMapping(value = "/deleteMyBrowse", method = RequestMethod.POST)
    public Result deleteMyBrowse(Long id) {

        return userBrowseService.deleteMyBrowse(id);
    }



}
