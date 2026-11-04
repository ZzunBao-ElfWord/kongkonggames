package com.pwyl.modules.app.controller.app;

import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.annotation.Login;
import com.pwyl.modules.app.entity.UserFollow;
import com.pwyl.modules.app.service.UserFollowService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/app/userFollow")
@Api("app 关注")
public class AppUserFollowController {
    private UserFollowService userFollowService;

    /**
     * 关注 / 取消关注
     **/

    @RequestMapping("/insert")
    @Login
    public Result insert(@RequestAttribute Long userId, @RequestParam Long followUserId) {
        return userFollowService.insert(userId, followUserId);
    }

    /**
     * 查看我的关注
     */
    @RequestMapping("/selectMyFollow")
    @Login
    public Result selectMyFollow(@RequestAttribute Long userId,Long page,Long limit) {
        return userFollowService.selectMyFollow(userId,page,limit);
    }
    /**
     * 查看我的粉丝
     */
    @RequestMapping("/selectFans")
    @Login
    public Result selectFans(@RequestAttribute Long userId,Long page,Long limit) {
        return userFollowService.selectFans(userId,page,limit);
    }
    /**
     * 查看我是否关注
     */
    @RequestMapping("/selectFollowUser")
    @Login
    public Result selectFollowUser(@RequestAttribute Long userId,@RequestParam Long followUserId) {
        return userFollowService.selectFollowUser(userId,followUserId);
    }

}
