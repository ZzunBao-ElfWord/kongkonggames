package com.pwyl.modules.chats.controller.app;


import com.pwyl.modules.chats.entity.Chats;
import com.pwyl.modules.chats.service.ChatsContentService;
import com.pwyl.modules.chats.service.ChatsService;
import com.pwyl.modules.chats.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value="聊天会话",tags={"聊天会话"})
@RequestMapping(value = "/app/chats")
public class AppChatsController {

    @Autowired
    private ChatsService service;
    @Autowired
    private ChatsContentService chatsContentService;

    @PostMapping("/save")
    @ApiOperation("用户端发起聊天")
    public Result saveBody(@RequestBody Chats entity) {
        return service.saveBody(entity);
    }

    @GetMapping("/list")
    @ApiOperation("用户端聊天内容列表")
    public Result findAll(Long chatId) {
        return chatsContentService.findAll(chatId);
    }
    
    @GetMapping("/userCount")
    @ApiOperation("用户端未读消息")
    public Result userCount(@ApiParam("店铺id(总后台商户传0)") @RequestParam Long userId) {
        return service.userCount(userId);
    }


}
