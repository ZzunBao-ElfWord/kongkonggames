package com.pwyl.modules.message.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pwyl.common.utils.PageUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.annotation.Login;
import com.pwyl.modules.message.entity.MessageInfo;
import com.pwyl.modules.message.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fang
 * @date 2020/7/13
 */
@RestController
@Api(value = "消息管理", tags = {"消息管理"})
@RequestMapping(value = "/app/message")
public class AppMessageController {

    @Autowired
    private MessageService messageService;

    @Login
    @RequestMapping(value = "/selectMessageByUserId", method = RequestMethod.GET)
    @ApiOperation("查询用户消息")
    @ResponseBody
    public Result selectUserRecharge(int page, int limit,@RequestAttribute("userId") Long userId,Integer state,Integer platform,Integer status){
        Map<String,Object> map=new HashMap<>();
        map.put("page",page);
        map.put("limit",limit);
        map.put("userId",userId);
        map.put("state",state);
        map.put("platform",platform);
        map.put("status",status);
        PageUtils pageUtils = messageService.selectMessageList(map);
        messageService.updateSendState(userId,state);
        return Result.success().put("data",pageUtils);
    }

    @Login
    @RequestMapping(value = "/selectMessageByUserIdLimit1", method = RequestMethod.GET)
    @ApiOperation("查询用户消息")
    @ResponseBody
    public Result selectMessageByUserIdLimit1(@RequestAttribute("userId") Long userId){
        Map<String,Object> map=new HashMap<>();
        map.put("page",1);
        map.put("limit",1);
        map.put("userId",userId);
        return Result.success().put("data",messageService.selectMessageList(map));
    }

    @Login
    @GetMapping("/selectMessageById")
    @ApiOperation("根据id查详情")
    public Result selectMessageById(Long id){
        return Result.success().put("data",messageService.getById(id));
    }


    @Login
    @PostMapping("/insertMessage")
    @ApiOperation("添加投诉")
    public Result insertMessage(@RequestBody MessageInfo messageInfo){
        if(!"100".equals(messageInfo.getState())){
            messageInfo.setIsSee("2");
        }
        messageService.saveBody(messageInfo);
        return Result.success();
    }

    @Login
    @PostMapping("/updateMessage")
    @ApiOperation("修改")
    public Result updateMessage(@RequestBody MessageInfo messageInfo){
        messageService.update(messageInfo);
        return Result.success();
    }

    @Login
    @PostMapping("/deleteMessage")
    @ApiOperation("删除")
    public Result deleteMessage(Long id){
        messageService.delete(id);
        return Result.success();
    }


}