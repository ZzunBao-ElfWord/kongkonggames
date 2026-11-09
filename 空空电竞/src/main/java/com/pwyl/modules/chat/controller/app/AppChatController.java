package com.pwyl.modules.chat.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.annotation.Login;
import com.pwyl.modules.chat.entity.ChatConversation;
import com.pwyl.modules.chat.service.ChatContentService;
import com.pwyl.modules.chat.service.ChatConversationService;
import com.pwyl.modules.message.entity.MessageInfo;
import com.pwyl.modules.message.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Api(value = "聊天", tags = {"聊天"})
@RequestMapping(value = "/app/chat")
public class AppChatController {

    @Autowired
    private ChatContentService chatContentService;
    @Autowired
    private ChatConversationService chatConversationService;
    @Autowired
    private MessageService messageService;

    @Login
    @GetMapping("/selectChatConversationPage")
    @ApiOperation("获取聊天会话")
    public Result selectChatConversationPage(Integer page,Integer limit,@RequestAttribute("userId") Long userId){
        return Result.success().put("data",chatConversationService.selectChatConversationPage(page, limit, userId,null,null));
    }

    @Login
    @PostMapping("/insertChatConversation")
    @ApiOperation("发起聊天")
    public Result insertChatConversation(@RequestBody ChatConversation chatConversation){
        return chatConversationService.insertChatConversations(chatConversation);
    }

    @Login
    @GetMapping("/selectChatContent")
    @ApiOperation("获取聊天记录")
    public Result selectChatContent(Integer page,Integer limit,Long chatConversationId,@RequestAttribute("userId") Long userId,String content){
        //清空未读消息
        chatContentService.updateChatContentStatusByUserIdAndChatId(userId, chatConversationId);
        ChatConversation byId = chatConversationService.getById(chatConversationId);
        if(byId!=null){
            boolean flag=false;
            if(String.valueOf(byId.getIsWxMsg()).equals(String.valueOf(userId))){
                byId.setIsWxMsg(0);
                flag=true;
            }
            if(String.valueOf(byId.getIsSendMsg()).equals(String.valueOf(userId))){
                byId.setIsSendMsg(0);
                flag=true;
            }
            if(flag){
                chatConversationService.updateById(byId);
            }
        }
        return Result.success().put("data",chatContentService.selectChatContentPage(page, limit, chatConversationId,content,null,null));
    }

    @Login
    @GetMapping("/selectChatCount")
    @ApiOperation("获取未读消息数量")
    public Result selectChatCount(@RequestAttribute("userId") Long userId){
        int chatCount = chatContentService.selectChatCount(userId);
        int messageCount = messageService.count(new QueryWrapper<MessageInfo>().eq("is_see", 0).and(Wrapper -> Wrapper.eq("state", "4").or().eq("state ","5")).eq("user_id", userId));
        int systemMessageCount = messageService.count(new QueryWrapper<MessageInfo>().eq("is_see", 0).eq("state",6).eq("user_id", userId));
        int yueMessageCount = messageService.count(new QueryWrapper<MessageInfo>().eq("is_see", 0).eq("state",7).eq("user_id", userId));
        Map<String,Object> result=new HashMap<>();
        result.put("chatCount",chatCount);
        result.put("messageCount",messageCount);
        result.put("systemMessageCount",systemMessageCount);
        result.put("yueMessageCount",yueMessageCount);
        return Result.success().put("data",result);
    }
}