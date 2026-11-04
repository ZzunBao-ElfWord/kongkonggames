package com.pwyl.modules.chat.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwyl.common.utils.PageUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.chat.dao.ChatContentDao;
import com.pwyl.modules.chat.entity.ChatContent;
import com.pwyl.modules.chat.service.ChatContentService;
import com.pwyl.modules.common.dao.CommonInfoDao;
import com.pwyl.modules.common.entity.CommonInfo;
import com.pwyl.modules.common.service.CommonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class ChatContentServiceImpl extends ServiceImpl<ChatContentDao, ChatContent> implements ChatContentService {


    @Override
    public PageUtils selectChatContentPage(Integer page, Integer limit, Long chatConversationId,String content,String userName,String phone) {
        return new PageUtils(baseMapper.selectChatContentPage(new Page<>(page,limit),chatConversationId,content,userName,phone));
    }

    @Override
    public int updateChatContentStatusByUserIdAndChatId(Long userId,Long chatConversationId){
        return baseMapper.updateChatContentStatusByUserIdAndChatId(userId, chatConversationId);
    }

    @Override
    public int selectChatCount(Long userId) {
        return baseMapper.selectChatCount(userId);
    }

}