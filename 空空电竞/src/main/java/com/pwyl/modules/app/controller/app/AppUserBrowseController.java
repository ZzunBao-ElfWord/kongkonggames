package com.pwyl.modules.app.controller.app;

import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.annotation.Login;
import com.pwyl.modules.app.entity.UserBrowse;
import com.pwyl.modules.app.service.UserBrowseService;
import com.pwyl.modules.app.service.UserFollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/app/userBrowse")
@Api(value = "app 访问 | 浏览 ", tags = {"app 访问 | 浏览 "})
public class AppUserBrowseController {
    private UserBrowseService userBrowseService;

    @ApiOperation("查询我的访客")
    @Login
    @RequestMapping("/myVisitor")
    public Result selectMyVisitor(@RequestAttribute Long userId, Long page, Long limit) {
        return userBrowseService.selectMyVisitor(userId, page, limit);
    }

    @Login
    @RequestMapping("/myBrowse")
    @ApiOperation("浏览足迹")
    public Result selectMyBrowse(@RequestAttribute Long userId, Long page, Long limit) {
        return userBrowseService.selectMyBrowse(userId, page, limit);
    }

    @Login
    @RequestMapping("/selectAmount")
    @ApiOperation("粉丝量 关注量 访客量 足迹量")
    public Result selectAmount(@RequestAttribute Long userId) {
        return userBrowseService.selectAmount(userId);
    }

    @ApiOperation("删除我的访客")
    @RequestMapping(value = "/deleteMyVisitor", method = RequestMethod.POST)
    public Result deleteMyVisitor(Long id) {
        return userBrowseService.deleteMyVisitor(id);
    }

    @ApiOperation(("删除我的足迹"))
    @RequestMapping(value = "/deleteMyBrowse", method = RequestMethod.POST)
    public Result deleteMyBrowse(Long id) {

        return userBrowseService.deleteMyBrowse(id);
    }


}
