package com.pwyl.modules.chat.controller;

import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.app.service.UserService;
import com.pwyl.modules.chat.dao.ChatContentDao;
import com.pwyl.modules.chat.dao.ChatConversationDao;
import com.pwyl.modules.chat.entity.ChatConversation;
import com.pwyl.modules.chat.service.ChatContentService;
import com.pwyl.modules.common.service.CommonInfoService;
import com.pwyl.modules.utils.SenInfoCheckUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class Timer {

    @Autowired
    private UserService userService;
    @Autowired
    private ChatContentDao chatContentDao;
    @Autowired
    private ChatConversationDao chatConversationDao;
    @Autowired
    private CommonInfoService commonInfoService;


    @Scheduled(cron="0 */1 * * * ?")
    public void sendMsg(){
        //获取超过一分钟未读的消息 进行小程序推送
        List<Map<String, Object>> maps = chatContentDao.selectChatContentCountByWx(1,0);
        String value = commonInfoService.findOne(240).getValue();
        for(Map<String, Object> map:maps){
            String chatConversationId = String.valueOf(map.get("chatConversationId"));
            String userId = String.valueOf(map.get("userId"));
            String ccUserId = String.valueOf(map.get("ccUserId"));
            String ccFocusedUserId = String.valueOf(map.get("ccFocusedUserId"));
            String counts = String.valueOf(map.get("counts"));
            if(userId.equals(ccUserId)){
                userId=ccFocusedUserId;
            }else{
                userId=ccUserId;
            }
            if(StringUtils.isNotEmpty(userId) && !"null".equals(userId)){
                UserEntity userEntity = userService.selectUserById(Long.parseLong(userId));
                if(userEntity!=null && (userEntity.getIsSendMsg()==null || userEntity.getIsSendMsg()==1)){
                    if(StringUtils.isNotEmpty(chatConversationId) && !"null".equals(chatConversationId)){
                        ChatConversation chatConversation = chatConversationDao.selectById(Long.parseLong(chatConversationId));
                        if(chatConversation!=null){
                            chatConversation.setIsWxMsg(Integer.parseInt(userId));
                            chatConversationDao.updateById(chatConversation);
                        }
                    }
                    List<String> msgList=new ArrayList<>();
                    msgList.add("未读消息通知");
                    msgList.add("您有"+counts+"条未读消息，赶快上线查看吧！");
                    SenInfoCheckUtil.sendMsg(userEntity.getOpenId(),value,msgList,3);
                }
            }
        }
        String msg = commonInfoService.findOne(241).getValue();
        String count = commonInfoService.findOne(242).getValue();
        List<Map<String, Object>> mapList = chatContentDao.selectChatContentCountByDx(Integer.parseInt(msg),Integer.parseInt(count));
        for(Map<String, Object> map:mapList){
            String chatConversationId = String.valueOf(map.get("chatConversationId"));
            String userId = String.valueOf(map.get("userId"));
            String ccUserId = String.valueOf(map.get("ccUserId"));
            String ccFocusedUserId = String.valueOf(map.get("ccFocusedUserId"));
            String counts = String.valueOf(map.get("counts"));
            if(userId.equals(ccUserId)){
                userId=ccFocusedUserId;
            }else{
                userId=ccUserId;
            }
            if(StringUtils.isNotEmpty(userId) && !"null".equals(userId)){
                UserEntity userEntity = userService.selectUserById(Long.parseLong(userId));
                if(userEntity!=null && (userEntity.getIsSendMsg()==null || userEntity.getIsSendMsg()==1)){
                    if(StringUtils.isNotEmpty(chatConversationId) && !"null".equals(chatConversationId)){
                        ChatConversation chatConversation = chatConversationDao.selectById(Long.parseLong(chatConversationId));
                        if(chatConversation!=null){
                            chatConversation.setIsSendMsg(Integer.parseInt(userId));
                            chatConversationDao.updateById(chatConversation);
                        }
                    }
                    userService.sendMsg(userEntity.getPhone(),"dx",counts);
                }
            }
        }
    }



}
