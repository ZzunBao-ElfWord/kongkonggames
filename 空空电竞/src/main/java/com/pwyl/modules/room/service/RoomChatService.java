package com.pwyl.modules.room.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.room.entity.RoomChat;

public interface RoomChatService extends IService<RoomChat> {

    Result selectRoomChatList(Integer page, Integer limit, Long roomId, String content, Long userId);

}
