package com.pwyl.modules.chat.controller.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.app.service.UserService;
import com.pwyl.modules.chat.entity.ChatContent;
import com.pwyl.modules.chat.service.ChatContentService;
import com.pwyl.modules.utils.SenInfoCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ServerEndpoint("/chatSocket/{userId}")
public class AppChatSocket {//用户聊天

    /**
     * 在线人数
     */
    public static int onlineNumber = 0;
    /**
     * 以用户的id为key，WebSocket为对象保存起来
     */
    private static Map<String, AppChatSocket> clients = new ConcurrentHashMap<String, AppChatSocket>();
    private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 会话
     */
    private Session session;
    /**
     * 用户id
     */
    private String userId;

    // 这里使用静态，让 service 属于类
    private static ChatContentService chatContentService;
    private static UserService userService;

    // 注入的时候，给类的 service 注入
    @Autowired
    public void setWxChatContentService(ChatContentService chatContentService,UserService userService) {
        AppChatSocket.chatContentService = chatContentService;
        AppChatSocket.userService = userService;
    }



    /**
     * 建立连接
     *
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session)
    {
        onlineNumber++;
        log.error("现在来连接的客户id："+userId);
        this.userId = userId;
        this.session = session;
        log.error("有新连接加入！ 当前在线人数" + onlineNumber);
        try {
            //把自己的信息加入到map当中去
            AppChatSocket appChatSocket = clients.get(userId);
            if(appChatSocket!=null){
                clients.remove(userId);
            }
            clients.put(userId, this);

            /*sendMessageTo("恭喜你连接成功！",wxUserId);*/
        }
        catch (Exception e){
            log.error(userId+"上线的时候通知所有人发生了错误");
        }

    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("服务端发生了错误"+error.getMessage(),error);
        //error.printStackTrace();
    }
    /**
     * 连接关闭
     */
    @OnClose
    public void onClose()
    {
        onlineNumber--;
        //webSockets.remove(this);
        clients.remove(userId);
        log.error("有连接关闭！ 当前在线人数" + onlineNumber);
    }

    /**
     * 收到客户端的消息
     *
     * @param message 消息
     * @param session 会话
     */
    @OnMessage
    public void onMessage(String message, Session session)
    {
        try {
            JSONObject jsonObject = JSON.parseObject(message);
            String textMessage = jsonObject.getString("content");
            String messageType = jsonObject.getString("messageType");

            /*if("1".equals(messageType)){
                if(!SenInfoCheckUtil.contentFilter(textMessage)){
                    textMessage="内容违规";
                }
            }else if("2".equals(messageType)){
                if(!SenInfoCheckUtil.imgFilter(SenInfoCheckUtil.createFileItem(textMessage,null))){
                    textMessage="图片违规";
                    messageType="1";
                }
            }*/
            String width = jsonObject.getString("width");
            String height = jsonObject.getString("height");
            String voiceLength = jsonObject.getString("voiceLength");
            String userId = jsonObject.getString("userId");
            String chatConversationId = jsonObject.getString("chatConversationId");
            chatContentService.updateChatContentStatusByUserIdAndChatId(Long.parseLong(this.userId), Long.parseLong(chatConversationId));
            //将聊天记录保存包数据库中
            ChatContent wxChatContent=new ChatContent();
            wxChatContent.setChatConversationId(Long.parseLong(chatConversationId));
            wxChatContent.setUserId(Long.parseLong(this.userId));
            wxChatContent.setWidth(width);
            wxChatContent.setHeight(height);
            wxChatContent.setVoiceLength(voiceLength);
            wxChatContent.setContent(textMessage);
            wxChatContent.setMessageType(messageType);
            wxChatContent.setCreateTime(sdf.format(new Date()));
            //判断对方是否在线
            AppChatSocket chatSocket = clients.get(userId);
            if (chatSocket!=null) {
                wxChatContent.setStatus(1);
                chatSocket.session.getAsyncRemote().sendText(message);
            }else{
                wxChatContent.setStatus(0);
            }
            chatContentService.save(wxChatContent);
            UserEntity userEntity = userService.selectUserById(Long.parseLong(userId));
            if(userEntity!=null && StringUtils.isNotBlank(userEntity.getClientid())){
                UserEntity user = userService.selectUserById(Long.parseLong(this.userId));
                if("2".equals(messageType)){
                    textMessage="[图片]";
                }else if("3".equals(messageType)){
                    textMessage="[语音]";
                }
                userService.pushToSingle("新消息提醒",user.getUserName()+":"+textMessage,userEntity.getClientid());
            }
            /*AppChatSocket chatSocket = clients.get(userId);
            if (chatSocket!=null) {
                chatSocket.session.getAsyncRemote().sendText(message);
            }*/
        }
        catch (Exception e){
            log.error("发生了错误了"+e.getMessage(),e);
        }

    }


    public void sendMessageTo(String message, String ToUserName) throws IOException {
        for (AppChatSocket item : clients.values()) {
            if (item.userId.equals(ToUserName) ) {
                item.session.getAsyncRemote().sendText(message);
                System.err.println(this.userId+"发送成功："+ToUserName);
                break;
            }
        }
    }

    public void sendMessageAll(String message,String FromUserName) throws IOException {
        for (AppChatSocket item : clients.values()) {
            item.session.getAsyncRemote().sendText(message);
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineNumber;
    }

}
