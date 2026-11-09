package com.pwyl.modules.room.service;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.room.entity.RoomAudit;

public interface RoomAuditService extends IService<RoomAudit> {

    String notifyRoomAudit(JSONObject jsonObject);

    Result selectRoomAuditList(Integer page,Integer limit,Long userId,Long roomUserId, String roomName, String roomNo,
                               String roomUserName,String roomPhone, String userName,String phone,Integer status);

    Result auditRoomAudit(String ids,Integer status,Integer auditType,String auditContent);

}
