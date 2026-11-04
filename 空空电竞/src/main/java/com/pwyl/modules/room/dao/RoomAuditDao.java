package com.pwyl.modules.room.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwyl.modules.room.entity.RoomAudit;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author fang
 * @date 2023/12/01
 */
@Mapper
public interface RoomAuditDao extends BaseMapper<RoomAudit> {

    IPage<RoomAudit> selectRoomAuditList(Page<RoomAudit> page,Long userId,Long roomUserId, String roomName, String roomNo,
                                         String roomUserName,String roomPhone, String userName,String phone,Integer status);


}
