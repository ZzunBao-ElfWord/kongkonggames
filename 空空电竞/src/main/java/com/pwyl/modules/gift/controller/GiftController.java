package com.pwyl.modules.gift.controller;

import com.pwyl.common.utils.Result;
import com.pwyl.modules.gift.entity.Gift;
import com.pwyl.modules.gift.entity.GiftUser;
import com.pwyl.modules.gift.service.GiftService;
import com.pwyl.modules.gift.service.GiftUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/gift")
@Api(value = "礼物", tags = {"礼物"})
public class GiftController {

    @Autowired
    private GiftService giftService;
    @Autowired
    private GiftUserService giftUserService;

    @PostMapping("/insertGift")
    @ApiOperation("添加礼物")
    public Result insertGift(@RequestBody Gift gift){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        gift.setCreateTime(simpleDateFormat.format(new Date()));
        giftService.save(gift);
        return Result.success();
    }

    @PostMapping("/updateGift")
    @ApiOperation("修改礼物")
    public Result updateGift(@RequestBody Gift gift){
        giftService.updateById(gift);
        return Result.success();
    }

    @PostMapping("/deleteGift")
    @ApiOperation("删除礼物")
    public Result deleteGift(Long giftId){
        giftService.removeById(giftId);
        return Result.success();
    }

    @GetMapping("/selectGiftList")
    @ApiOperation("查询礼物列表")
    public Result selectGiftList(Integer status,String giftName){
        return Result.success().put("data",giftService.selectGiftList(status, giftName));
    }

    @GetMapping("/selectGiftPage")
    @ApiOperation("查询礼物列表分页")
    public Result selectGiftPage(Integer page,Integer limit,Integer status,String giftName){
        return giftService.selectGiftPage(page, limit, status, giftName);
    }

    @GetMapping("/selectGiftUser")
    @ApiOperation("查询用户礼物列表")
    public Result selectGiftUser(Integer page, Integer limit, Long userId,Long sendUserId,String userName,
                                 String phone,String sendUserName,String sendPhone,String giftName,Long roomId){
        return giftUserService.selectGiftUser(page, limit, userId, sendUserId, userName, phone, sendUserName, sendPhone, giftName,roomId);
    }


    @PostMapping("/sendGift")
    @ApiOperation("赠送礼物")
    public Result sendGift(@RequestBody GiftUser giftUser){
        return giftUserService.sendGift(giftUser);
    }

    @GetMapping("/selectGiftRoomList")
    @ApiOperation("查询用房间礼物列表")
    public Result selectGiftRoomList(Integer page, Integer limit, Long roomId){
        return giftUserService.selectGiftUser(page,limit,null,null,null,
                null,null,null,null,roomId);
    }





}
