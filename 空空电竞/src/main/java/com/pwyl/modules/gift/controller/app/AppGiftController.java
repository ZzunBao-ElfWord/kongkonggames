package com.pwyl.modules.gift.controller.app;

import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.annotation.Login;
import com.pwyl.modules.app.annotation.LoginUser;
import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.gift.entity.GiftUser;
import com.pwyl.modules.gift.service.GiftService;
import com.pwyl.modules.gift.service.GiftUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/gift")
@Api(value = "礼物", tags = {"礼物"})
public class AppGiftController {

    @Autowired
    private GiftService giftService;
    @Autowired
    private GiftUserService giftUserService;

    @GetMapping("/selectGiftList")
    @ApiOperation("查询礼物列表")
    public Result selectGiftList(Integer status,String giftName){
        return Result.success().put("data",giftService.selectGiftList(status, giftName));
    }

    @Login
    @GetMapping("/selectGiftUser")
    @ApiOperation("查询用户收到礼物列表")
    public Result selectGiftUser(Integer page, Integer limit, @LoginUser UserEntity user){
        return giftUserService.selectGiftUser(page,limit,user.getUserId(),null,null,
                null,null,null,null,null);
    }

    @Login
    @GetMapping("/selectGiftSendUser")
    @ApiOperation("查询用户发出的礼物列表")
    public Result selectGiftSendUser(Integer page, Integer limit, @LoginUser UserEntity user){
        return giftUserService.selectGiftUser(page,limit,null,user.getUserId(),null,
                null,null,null,null,null);
    }

    @Login
    @GetMapping("/selectGiftRoomList")
    @ApiOperation("查询用房间礼物列表")
    public Result selectGiftRoomList(Integer page, Integer limit, Long roomId){
        return giftUserService.selectGiftUser(page,limit,null,null,null,
                null,null,null,null,roomId);
    }


    @Login
    @PostMapping("/sendGift")
    @ApiOperation("赠送礼物")
    public Result sendGift(@RequestAttribute Long userId, @RequestBody GiftUser giftUser){
        giftUser.setSendUserId(userId);
        return giftUserService.sendGift(giftUser);
    }



}
