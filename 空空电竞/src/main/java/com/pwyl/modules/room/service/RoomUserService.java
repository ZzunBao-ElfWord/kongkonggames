package com.pwyl.modules.room.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.room.entity.RoomUser;

import java.util.List;

public interface RoomUserService extends IService<RoomUser> {


    Result selectRoomUserList(Integer page, Integer limit, Long userId, String userName, String phone, Long roomId,
                              String roomName, String roomNo,Integer isSeat,Integer status);

    RoomUser selectRoomUserByUserIdAndRoomId(Long roomId,Long userId);

    RoomUser selectRoomUserBySeatNoAndRoomId(Long roomId,String seatNo);

    List<UserEntity> selectUserByRoomUserId(Long roomId);

}
