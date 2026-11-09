package com.pwyl.modules.room.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwyl.modules.room.entity.RoomChat;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author fang
 * @date 2023/12/01
 */
@Mapper
public interface RoomChatDao extends BaseMapper<RoomChat> {

    IPage<RoomChat> selectRoomChatList(Page<RoomChat> page,Long roomId,String content,Long userId);


}
