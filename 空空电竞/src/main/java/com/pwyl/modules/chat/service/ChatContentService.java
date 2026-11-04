package com.pwyl.modules.chat.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwyl.common.utils.PageUtils;
import com.pwyl.modules.chat.entity.ChatContent;
import org.apache.ibatis.annotations.Param;

public interface ChatContentService  extends IService<ChatContent> {

    PageUtils selectChatContentPage(Integer page, Integer limit, Long chatConversationId,String content,String userName,String phone);

    int updateChatContentStatusByUserIdAndChatId(Long userId,Long chatConversationId);

    int selectChatCount(Long userId);

}