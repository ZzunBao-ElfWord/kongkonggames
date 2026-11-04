package com.pwyl.modules.chat.controller;

import com.pwyl.common.utils.Result;
import com.pwyl.modules.chat.service.ChatContentService;
import com.pwyl.modules.chat.service.ChatConversationService;
import com.pwyl.modules.chats.service.ChatsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "聊天", tags = {"聊天"})
@RequestMapping(value = "/chat")
public class ChatController {

    @Autowired
    private ChatContentService chatContentService;
    @Autowired
    private ChatConversationService chatConversationService;

    @GetMapping("/selectChatConversationPage")
    @ApiOperation("获取聊天会话")
    public Result selectChatConversationPage(Integer page, Integer limit, Long userId, String nickName,String content){
        return Result.success().put("data",chatConversationService.selectChatConversationPage(page, limit, userId,nickName,content));
    }


    @GetMapping("/selectChatContent")
    @ApiOperation("获取聊天记录")
    public Result selectChatContent(Integer page,Integer limit,Long chatConversationId,String content,String userName,String phone){
        return Result.success().put("data",chatContentService.selectChatContentPage(page, limit, chatConversationId,content,userName,phone));
    }


    @PostMapping("/deleteChatContentById")
    @ApiOperation("删除某一句聊天记录")
    public Result deleteChatContentById(Long chatContentId){
        chatContentService.removeById(chatContentId);
        return Result.success();
    }





}
