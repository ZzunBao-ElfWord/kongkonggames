package com.pwyl.modules.app.controller;

import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.annotation.Login;
import com.pwyl.modules.app.service.UserFollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/userFollow")
@Api(value = "关注|粉丝", tags = {"关注|粉丝"})
public class UserFollowController {
    private UserFollowService userFollowService;


    /**
     * 查看我的关注
     */
    @GetMapping("/selectMyFollow")
    @ApiOperation("查看我的关注")
    public Result selectMyFollow(Long userId, Long page, Long limit) {
        return userFollowService.selectMyFollow(userId, page, limit);
    }

    /**
     * 查看我的粉丝
     */
    @GetMapping("/selectFans")
    @ApiOperation("查看我的粉丝")
    public Result selectFans(Long userId, Long page, Long limit) {
        return userFollowService.selectFans(userId, page, limit);
    }

    /**
     * 关注 / 取消关注
     **/

    @PostMapping("/insert")
    @ApiOperation("关注/取消关注")
    public Result insert(Long userId,  Long followUserId) {
        return userFollowService.insert(userId, followUserId);
    }

}
