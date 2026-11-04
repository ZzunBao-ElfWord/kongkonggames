package com.pwyl.modules.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwyl.common.utils.PageUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.chat.entity.ChatContent;
import com.pwyl.modules.chat.entity.ChatConversation;


public interface ChatConversationService extends IService<ChatConversation> {

    PageUtils selectChatConversationPage(Integer page, Integer limit, Long userId,String nickName,String content);

    int insertChatConversation(ChatConversation chatConversation);

    ChatConversation selectChatConversation(Long userId,Long focusedUserId);

    Result insertChatConversations(ChatConversation chatConversation);

}