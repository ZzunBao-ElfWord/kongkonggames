package com.pwyl.modules.chat.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwyl.modules.chat.entity.ChatContent;
import com.pwyl.modules.chat.entity.ChatConversation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;


@Mapper
public interface ChatConversationDao extends BaseMapper<ChatConversation> {

    IPage<Map<String,Object>> selectChatConversationPage(Page<Map<String,Object>> page,@Param("userId") Long userId,@Param("nickName") String nickName,@Param("content") String content);

    int insertChatConversation(ChatConversation chatConversation);

}