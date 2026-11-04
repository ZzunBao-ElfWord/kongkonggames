package com.pwyl.modules.room.controller.app;


import com.alibaba.fastjson.JSONObject;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.room.service.RoomAuditService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author fang
 * @date 2023/12/01
 */
@Slf4j
@RestController
@Api(value = "语音房内容审核", tags = {"语音房内容审核"})
@RequestMapping(value = "/app/roomAudit")
public class AppRoomAuditController {

    @Autowired
    private RoomAuditService roomAuditService;

    @PostMapping("/notifyRoomAudit")
    @ApiOperation("审核内容回调")
    public String notifyRoomAudit(@RequestBody JSONObject jsonObject){
        return roomAuditService.notifyRoomAudit(jsonObject);
    }

    @GetMapping("/selectRoomAuditList")
    @ApiOperation("查询内容审核列表")
    public Result selectRoomAuditList(Integer page,Integer limit,Long userId,Long roomUserId, String roomName, String roomNo,
                                      String roomUserName,String roomPhone, String userName,String phone,Integer status){
        return roomAuditService.selectRoomAuditList(page, limit, userId, roomUserId, roomName, roomNo, roomUserName, roomPhone, userName, phone,status);
    }

}