package com.pwyl.modules.room.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwyl.modules.room.entity.Room;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author fang
 * @date 2023/12/01
 */
@Mapper
public interface RoomDao extends BaseMapper<Room> {


    IPage<Room> selectRoomList(Page<Room> page, Long userId, String roomNo, String roomName,
                               String userName, Integer status,Long gameId,Integer roomType,String search);


}
