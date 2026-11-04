package com.pwyl.modules.room.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.room.entity.RoomUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author fang
 * @date 2023/12/01
 */
@Mapper
public interface RoomUserDao extends BaseMapper<RoomUser> {

    IPage<RoomUser> selectRoomUserList(Page<RoomUser> page,Long userId,String userName,String phone,Long roomId,
                                       String roomName,String roomUserName,Integer isSeat,Integer status);

    List<UserEntity> selectUserByRoomUserId(Long roomId);

}
