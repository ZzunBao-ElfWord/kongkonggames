package com.pwyl.modules.invite.controller.app;


import com.pwyl.common.utils.PageUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.annotation.Login;
import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.app.service.UserService;
import com.pwyl.modules.common.entity.CommonInfo;
import com.pwyl.modules.common.service.CommonInfoService;
import com.pwyl.modules.invite.entity.InviteMoney;
import com.pwyl.modules.invite.service.InviteMoneyService;
import com.pwyl.modules.invite.service.InviteService;
import com.pwyl.modules.utils.SenInfoCheckUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fang
 * @date 2020/7/9
 */
@Slf4j
@RestController
@Api(value = "邀请收益", tags = {"邀请收益"})
@RequestMapping(value = "/app/invite")
public class AppInviteController {

    @Autowired
    private InviteService inviteService;
    @Autowired
    private UserService userService;
    @Autowired
    private InviteMoneyService inviteMoneyService;
    @Autowired
    private CommonInfoService commonInfoService;

    @RequestMapping(value = "/selectInviteCount", method = RequestMethod.GET)
    @ApiOperation("查看我邀请的人员数量")
    @ResponseBody
    public Result selectInviteCount(Integer state,Long userId){
        return Result.success().put("data",inviteService.selectInviteCount(state,userId));
    }
    @Login
    @RequestMapping(value = "/selectInviteAndPoster", method = RequestMethod.GET)
    @ApiOperation("查看我的邀请码和海报二维码")
    @ResponseBody
    public Result selectInviteAndPoster(@RequestAttribute Long userId){
        UserEntity userEntity = userService.queryByUserId(userId);
        CommonInfo one = commonInfoService.findOne(19);
        Map<String,Object> map=new HashMap<>();
        map.put("url",one.getValue());
        map.put("user",userEntity);
        return Result.success().put("data",map);
    }

    @Login
    @RequestMapping(value = "/selectInviteMoney", method = RequestMethod.GET)
    @ApiOperation("我的收益")
    @ResponseBody
    public Result selectInviteMoney(@RequestAttribute("userId") Long userId){
        Double inviteMoney = inviteService.selectInviteSum(-1,userId);
        Integer inviteCount = inviteService.selectInviteCount(-1, userId);
        Map<String,Object> result=new HashMap<>();
        result.put("inviteMoney",inviteMoney);
        result.put("inviteCount",inviteCount);
        return Result.success().put("data",result);
    }

    @GetMapping("/mpCreateQr")
    @ApiOperation("小程序推广二维码")
    public void mpCreateQr(@RequestParam String invitationCode, HttpServletResponse response) {
        SenInfoCheckUtil.getPoster(invitationCode,response);
    }


    @Login
    @RequestMapping(value = "/selectInviteByUserIdLists", method = RequestMethod.GET)
    @ApiOperation("查看我邀请的人员列表(查看所有邀请列表)")
    @ResponseBody
    public Result selectInviteByUserIdLists(int page,int limit,@RequestAttribute("userId") Long userId){
        PageUtils pageUtils = inviteService.selectInviteUser(page, limit, userId,null);
        return Result.success().put("data",pageUtils);
    }

}