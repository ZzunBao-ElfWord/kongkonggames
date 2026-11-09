package com.pwyl.modules.room.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.room.entity.Room;

public interface RoomService extends IService<Room> {

    Result selectRoomList(Integer page, Integer limit, Long userId, String roomNo, String roomName, String userName,
                          Integer status,Long gameId,Integer roomType,String search);

    Result selectRoomDetails(Long roomId,Long userId);

    Room selectRoomByRoomNo(String roomNo);

    String setRoomNo();

    Result roomRemoveUser(String roomNo,Long userId);

    Result dismissRoom(String roomNo);

}
