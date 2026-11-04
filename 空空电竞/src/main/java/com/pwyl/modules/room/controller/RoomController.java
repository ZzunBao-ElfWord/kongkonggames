package com.pwyl.modules.room.controller;


import com.pwyl.common.utils.DateUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.service.UserService;
import com.pwyl.modules.common.service.CommonInfoService;
import com.pwyl.modules.room.entity.Room;
import com.pwyl.modules.room.entity.RoomUser;
import com.pwyl.modules.room.service.RoomChatService;
import com.pwyl.modules.room.service.RoomService;
import com.pwyl.modules.room.service.RoomUserService;
import com.pwyl.modules.utils.TLSSigAPIv2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author fang
 * @date 2023/12/01
 */
@Slf4j
@RestController
@Api(value = "语音房", tags = {"语音房"})
@RequestMapping(value = "/room")
public class RoomController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomChatService roomChatService;
    @Autowired
    private RoomUserService roomUserService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private UserService userService;

    @GetMapping("/selectSign")
    @ApiOperation("生成权限")
    public Result selectSign(Long userId){
        String sdkappid = commonInfoService.findOne(814).getValue();
        String key = commonInfoService.findOne(813).getValue();
        String s = TLSSigAPIv2.genUserSig(String.valueOf(userId), 86400, Long.parseLong(sdkappid), key);
        return Result.success().put("data",s);
    }


    @PostMapping("/insertRoom")
    @ApiOperation("创建语音房")
    public Result insertRoom(@RequestBody Room room){
        room.setStatus(1);
        room.setCreateTime(DateUtils.format(new Date()));
        roomService.save(room);
        return Result.success();
    }

    @PostMapping("/updateRoom")
    @ApiOperation("修改语音房")
    public Result updateRoom(@RequestBody Room room){
        room.setCreateTime(DateUtils.format(new Date()));
        roomService.updateById(room);
        return Result.success();
    }

    @PostMapping("/deleteRoom")
    @ApiOperation("删除语音房")
    public Result deleteRoom(Long roomId){
        roomService.removeById(roomId);
        return Result.success();
    }

    @GetMapping("/selectRoomList")
    @ApiOperation("查询语音房")
    public Result selectRoomList(Integer page,Integer limit,Long userId,String roomNo,String roomName,String userName,
                                 Integer status,Long gameId,Integer roomType,String search){
        return roomService.selectRoomList(page, limit, userId, roomNo, roomName, userName, status,gameId,roomType,search);
    }

    @GetMapping("/selectCreateRoomUser")
    @ApiOperation("查询语音房人员列表")
    public Result selectCreateRoomUser(Long roomId){
        Room room = roomService.getById(roomId);
        return Result.success().put("data",userService.getById(room.getUserId()));
    }

    @GetMapping("/selectRoomDetails")
    @ApiOperation("查询语音房详情")
    public Result selectRoomDetails(Long roomId){
        return roomService.selectRoomDetails(roomId, null);
    }

    @GetMapping("/insertRoomUser")
    @ApiOperation("加入语音房")
    public Result insertRoomUser(@RequestBody RoomUser roomUser){
        roomUser.setCreateTime(DateUtils.format(new Date()));
        roomUserService.save(roomUser);
        return Result.success();
    }

    @PostMapping("/updateRoomUser")
    @ApiOperation("修改语音房人信息")
    public Result updateRoomUser(@RequestBody RoomUser roomUser){
        roomUserService.updateById(roomUser);
        return Result.success();
    }

    @PostMapping("/deleteRoomUser")
    @ApiOperation("删除语音房加入信息")
    public Result deleteRoomUser(Long roomUserId){
        roomUserService.removeById(roomUserId);
        return Result.success();
    }

    @GetMapping("/selectRoomUserList")
    @ApiOperation("查询语音房人员列表")
    public Result selectRoomUserList(Integer page,Integer limit,Long userId,String userName,String phone,Long roomId,
                                     String roomName,String roomNo,Integer isSeat,Integer status){
        return roomUserService.selectRoomUserList(page, limit, userId, userName, phone, roomId, roomName, roomNo,isSeat,status);
    }

    @GetMapping("/selectRoomChatList")
    @ApiOperation("查询聊天记录")
    public Result selectRoomChatList(Integer page,Integer limit,Long roomId,String content,Long userId){
        return roomChatService.selectRoomChatList(page, limit, roomId, content, userId);
    }

    @GetMapping("/roomRemoveUser")
    @ApiOperation("移除房间用户")
    public Result roomRemoveUser(String roomNo,Long userId){
        return roomService.roomRemoveUser(roomNo, userId);
    }

    @GetMapping("/dismissRoom")
    @ApiOperation("解散房间")
    public Result dismissRoom(String roomNo){
        return roomService.dismissRoom(roomNo);
    }


}