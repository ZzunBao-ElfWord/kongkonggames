package com.pwyl.modules.room.controller.app;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pwyl.common.utils.DateUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.annotation.Login;
import com.pwyl.modules.app.entity.UserEntity;
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
@RequestMapping(value = "/app/room")
public class AppRoomController {

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

    @Login
    @GetMapping("/selectSign")
    @ApiOperation("生成权限")
    public Result selectSign(@RequestAttribute Long userId){
        String sdkappid = commonInfoService.findOne(814).getValue();
        String key = commonInfoService.findOne(813).getValue();
        String s = TLSSigAPIv2.genUserSig(String.valueOf(userId), 86400, Long.parseLong(sdkappid), key);
        return Result.success().put("data",s);
    }

    @Login
    @PostMapping("/insertRoom")
    @ApiOperation("创建语音房")
    public Result insertRoom(@RequestBody Room room,@RequestAttribute Long userId){
        room.setRoomNo(roomService.setRoomNo());
        room.setStatus(1);
        room.setUserId(userId);
        room.setCreateTime(DateUtils.format(new Date()));
        roomService.save(room);
        UserEntity userEntity = userService.selectUserById(userId);
        userEntity.setIsRoom(1);
        userEntity.setRoomId(room.getRoomId());
        userEntity.setRoomNo(room.getRoomNo());
        userService.updateById(userEntity);
        return Result.success().put("data",room);
    }

    @Login
    @PostMapping("/updateRoom")
    @ApiOperation("修改语音房")
    public Result updateRoom(@RequestBody Room room){
        room.setCreateTime(DateUtils.format(new Date()));
        roomService.updateById(room);
        return Result.success();
    }

    @Login
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

    @Login
    @GetMapping("/selectRoomDetails")
    @ApiOperation("查询语音房详情")
    public Result selectRoomDetails(Long roomId,@RequestAttribute Long userId){
        return roomService.selectRoomDetails(roomId, userId);
    }

    @Login
    @PostMapping("/insertRoomUser")
    @ApiOperation("加入语音房")
    public Result insertRoomUser(@RequestBody RoomUser roomUser,@RequestAttribute Long userId){
        Room room = roomService.getById(roomUser.getRoomId());
        RoomUser roomUser1 = roomUserService.selectRoomUserByUserIdAndRoomId(roomUser.getRoomId(), userId);
        if(roomUser1!=null){
            if(roomUser1.getStatus()==3){
                return Result.success().put("data",roomUser1);
            }
            roomUser1.setStatus(1);
            if(room.getUserId().equals(userId)){
                roomUser1.setIsSeat(1);
                roomUser1.setSeatNo(0);
            }
            roomUserService.updateById(roomUser1);
        }else{
            roomUser.setCreateTime(DateUtils.format(new Date()));
            roomUser.setUserId(userId);
            roomUser.setStatus(1);
            if(room.getUserId().equals(userId)){
                roomUser.setIsSeat(1);
                roomUser.setSeatNo(0);
            }
            roomUserService.save(roomUser);
        }
        if(room.getUserId().equals(userId)){
            room.setStatus(1);
            roomService.updateById(room);
        }
        if(roomUser1!=null){
            return Result.success().put("data",roomUser1);
        }
        return Result.success().put("data",roomUser);
    }

    @Login
    @PostMapping("/updateRoomUser")
    @ApiOperation("修改语音房人信息")
    public Result updateRoomUser(@RequestBody RoomUser roomUser,@RequestAttribute Long userId){
        RoomUser roomUser1 = null;
        if(roomUser.getRoomUserId()==null){
            roomUser1 = roomUserService.selectRoomUserByUserIdAndRoomId(roomUser.getRoomId(),userId);
            roomUser.setRoomUserId(roomUser1.getRoomUserId());
        }else{
            roomUser1 = roomUserService.getById(roomUser.getRoomUserId());
        }
        if(roomUser.getStatus()!=null && roomUser.getStatus()==2){
            roomUser.setIsSeat(0);
            roomUser.setSeatNo(0);
        }

        if(roomUser.getStatus()!=null && roomUser.getStatus()==2){
            Room room = roomService.getById(roomUser1.getRoomId());
            if(room.getUserId().equals(roomUser1.getUserId())){
                room.setStatus(2);
                roomService.updateById(room);
                userService.update(null, Wrappers.<UserEntity>lambdaUpdate()
                        .set(UserEntity::getRoomId,null)
                        .set(UserEntity::getIsRoom,0)
                        .set(UserEntity::getRoomNo,null)
                        .eq(UserEntity::getUserId,room.getUserId()));
            }
        }
        if(roomUser1.getStatus()==3){
            roomUser.setStatus(3);
        }
        roomUserService.updateById(roomUser);
        return Result.success();
    }

    @Login
    @PostMapping("/deleteRoomUser")
    @ApiOperation("删除语音房加入信息")
    public Result deleteRoomUser(Long roomUserId){
        roomUserService.removeById(roomUserId);
        return Result.success();
    }

    @Login
    @GetMapping("/selectRoomUserList")
    @ApiOperation("查询语音房人员列表")
    public Result selectRoomUserList(Integer page,Integer limit,Long userId,String userName,String phone,Long roomId,
                                     String roomName,String roomNo,Integer isSeat,Integer status){
        return roomUserService.selectRoomUserList(page, limit, userId, userName, phone, roomId, roomName, roomNo,isSeat,status);
    }

    @Login
    @GetMapping("/selectCreateRoomUser")
    @ApiOperation("查询语音房人员列表")
    public Result selectCreateRoomUser(Long roomId){
        Room room = roomService.getById(roomId);
        return Result.success().put("data",userService.getById(room.getUserId()));
    }


    @Login
    @GetMapping("/selectRoomChatList")
    @ApiOperation("查询聊天记录")
    public Result selectRoomChatList(Integer page,Integer limit,Long roomId,String content,Long userId){
        return roomChatService.selectRoomChatList(page, limit, roomId, content, userId);
    }

    @Login
    @GetMapping("/roomRemoveUser")
    @ApiOperation("移除房间用户")
    public Result roomRemoveUser(String roomNo,Long userId){
        return roomService.roomRemoveUser(roomNo, userId);
    }

    @Login
    @GetMapping("/dismissRoom")
    @ApiOperation("解散房间")
    public Result dismissRoom(String roomNo){
        return roomService.dismissRoom(roomNo);
    }



}