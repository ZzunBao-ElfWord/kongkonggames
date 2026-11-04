package com.pwyl.modules.room.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwyl.common.utils.PageUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.room.dao.RoomChatDao;
import com.pwyl.modules.room.entity.RoomChat;
import com.pwyl.modules.room.service.RoomChatService;
import org.springframework.stereotype.Service;

/**
 * 房间聊天记录
 */
@Service
public class RoomChatServiceImpl extends ServiceImpl<RoomChatDao, RoomChat> implements RoomChatService {

    @Override
    public Result selectRoomChatList(Integer page, Integer limit, Long roomId, String content, Long userId){
        return Result.success().put("data",new PageUtils(baseMapper.selectRoomChatList(new Page<>(page,limit),roomId,content,userId)));
    }

}
