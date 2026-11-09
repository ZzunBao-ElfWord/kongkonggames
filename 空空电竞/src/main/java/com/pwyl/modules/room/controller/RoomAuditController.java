package com.pwyl.modules.room.controller;


import com.pwyl.common.utils.Result;
import com.pwyl.modules.room.service.RoomAuditService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fang
 * @date 2023/12/01
 */
@Slf4j
@RestController
@Api(value = "语音房内容审核", tags = {"语音房内容审核"})
@RequestMapping(value = "/roomAudit")
public class RoomAuditController {

    @Autowired
    private RoomAuditService roomAuditService;

    @GetMapping("/selectRoomAuditList")
    @ApiOperation("查询内容审核列表")
    public Result selectRoomAuditList(Integer page,Integer limit,Long userId,Long roomUserId, String roomName, String roomNo,
                                      String roomUserName,String roomPhone, String userName,String phone,Integer status){
        return roomAuditService.selectRoomAuditList(page, limit, userId, roomUserId, roomName, roomNo, roomUserName, roomPhone, userName, phone,status);
    }

    @PostMapping("/auditRoomAudit")
    @ApiOperation("审核")
    public Result auditRoomAudit(String ids,Integer status,Integer auditType,String auditContent){
        return roomAuditService.auditRoomAudit(ids, status, auditType, auditContent);
    }


}