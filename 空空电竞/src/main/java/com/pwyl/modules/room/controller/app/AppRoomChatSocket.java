package com.pwyl.modules.room.controller.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.app.service.UserService;
import com.pwyl.modules.chat.entity.ChatContent;
import com.pwyl.modules.chat.service.ChatContentService;
import com.pwyl.modules.room.entity.Room;
import com.pwyl.modules.room.entity.RoomChat;
import com.pwyl.modules.room.entity.RoomUser;
import com.pwyl.modules.room.service.RoomChatService;
import com.pwyl.modules.room.service.RoomService;
import com.pwyl.modules.room.service.RoomUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
@Component
@ServerEndpoint("/roomChatSocket/{userId}/{roomId}")
public class AppRoomChatSocket {//用户聊天

    /**
     * 在线人数
     */
    public static int onlineNumber = 0;
    /**
     * 以用户的id为key，WebSocket为对象保存起来
     */
    private static ConcurrentHashMap<String, ConcurrentHashMap<String,AppRoomChatSocket>> clients = new ConcurrentHashMap<String, ConcurrentHashMap<String,AppRoomChatSocket>>();
    private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 会话
     */
    private Session session;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 房间id
     */
    private String roomId;

    // 这里使用静态，让 service 属于类
    private static RoomChatService roomChatService;
    private static RoomService roomService;
    private static RoomUserService roomUserService;
    private static UserService userService;

    // 注入的时候，给类的 service 注入
    @Autowired
    public void setWxChatContentService(RoomChatService roomChatService,RoomService roomService,UserService userService,RoomUserService roomUserService) {
        AppRoomChatSocket.roomChatService = roomChatService;
        AppRoomChatSocket.roomService = roomService;
        AppRoomChatSocket.roomUserService = roomUserService;
        AppRoomChatSocket.userService = userService;
    }



    /**
     * 建立连接
     *
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("userId") String userId,@PathParam("roomId") String roomId, Session session)
    {
        onlineNumber++;
        log.error("现在来连接的客户id："+userId);
        this.userId = userId;
        this.roomId = roomId;
        this.session = session;
        log.error("有新连接加入！ 当前在线人数" + onlineNumber);
        try {
            //把自己的信息加入到map当中去
            ConcurrentHashMap<String,AppRoomChatSocket> appChatSocket = clients.get(roomId);
            if(appChatSocket==null){
                appChatSocket=new ConcurrentHashMap<>();
                appChatSocket.put(userId,this);
                clients.put(roomId,appChatSocket);
            }else{
                appChatSocket.put(userId,this);
            }
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
        ConcurrentHashMap<String,AppRoomChatSocket> appChatSocket = clients.get(roomId);
        if(appChatSocket!=null){
            appChatSocket.remove(userId);
        }
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
            if("HeartBeat".equals(message)){
                Map<String, AppRoomChatSocket> stringAppRoomChatSocketMap = clients.get(roomId);
                AppRoomChatSocket appRoomChatSocket = stringAppRoomChatSocketMap.get(this.userId);
                synchronized(appRoomChatSocket.session){
                    appRoomChatSocket.session.getBasicRemote().sendText(message);
                }
            }else{
                JSONObject jsonObject = JSON.parseObject(message);
                String type = jsonObject.getString("type");

                if("1".equals(type)){
                    String textMessage = jsonObject.getString("content");
                    String messageType = jsonObject.getString("messageType");
                    String voiceLength = jsonObject.getString("voiceLength");
                    //将聊天记录保存包数据库中
                    RoomChat roomChat=new RoomChat();
                    roomChat.setRoomId(Long.parseLong(this.roomId));
                    roomChat.setUserId(Long.parseLong(this.userId));
                    roomChat.setVoiceLength(voiceLength);
                    roomChat.setContent(textMessage);
                    roomChat.setMessageType(messageType);
                    roomChat.setCreateTime(sdf.format(new Date()));
                    roomChatService.save(roomChat);
                    Map<String, AppRoomChatSocket> stringAppRoomChatSocketMap = clients.get(roomId);
                    for (AppRoomChatSocket item : stringAppRoomChatSocketMap.values()) {
                        try {
                            item.session.getAsyncRemote().sendText(message);
                        }catch (Exception e){
                            e.printStackTrace();
                            log.error("推送异常："+e.getMessage(),e);
                        }
                    }
                }else if("2".equals(type)){
                    //用户给房主发送请求上麦信息
                    String num = jsonObject.getString("num");
                    Room room = roomService.getById(Long.parseLong(this.roomId));
                    Long roomUserId = room.getUserId();
                    JSONObject jsonObject1=new JSONObject();
                    jsonObject1.put("type","2");
                    jsonObject1.put("num",num);
                    jsonObject1.put("user",userService.selectUserById(Long.parseLong(this.userId)));
                    Map<String, AppRoomChatSocket> stringAppRoomChatSocketMap = clients.get(roomId);
                    AppRoomChatSocket appRoomChatSocket = stringAppRoomChatSocketMap.get(String.valueOf(roomUserId));
                    if(appRoomChatSocket!=null){
                        appRoomChatSocket.session.getAsyncRemote().sendText(jsonObject1.toJSONString());
                    }
                }else if("3".equals(type)){
                    //房主审核用户的请求上麦信息
                    Map<String, AppRoomChatSocket> stringAppRoomChatSocketMaps = clients.get(roomId);
                    synchronized (stringAppRoomChatSocketMaps){
                        String status = jsonObject.getString("status");
                        if("1".equals(status)){
                            String num = jsonObject.getString("num");
                            String seatUserId = jsonObject.getString("userId");
                            try {
                                //判断这个麦克风上是否有用户 如果有 则直接拒绝
                                RoomUser roomUser1 = roomUserService.selectRoomUserBySeatNoAndRoomId(Long.parseLong(this.roomId), num);
                                if(roomUser1!=null){
                                    for (int i=1;i<8;i++){
                                        roomUser1 = roomUserService.selectRoomUserBySeatNoAndRoomId(Long.parseLong(this.roomId), String.valueOf(i));
                                        if(roomUser1==null){
                                            num=String.valueOf(i);
                                            break;
                                        }
                                    }
                            /*JSONObject jsonObject1=new JSONObject();
                            jsonObject1.put("type","5");
                            Map<String, AppRoomChatSocket> stringAppRoomChatSocketMap = clients.get(roomId);
                            AppRoomChatSocket appRoomChatSocket = stringAppRoomChatSocketMap.get(String.valueOf(seatUserId));
                            if(appRoomChatSocket!=null){
                                appRoomChatSocket.session.getAsyncRemote().sendText(jsonObject1.toJSONString());
                            }*/
                                }
                                if(roomUser1==null){
                                    RoomUser roomUser = roomUserService.selectRoomUserByUserIdAndRoomId(Long.parseLong(this.roomId), Long.parseLong(seatUserId));
                                    roomUser.setIsSeat(1);
                                    roomUser.setSeatNo(Integer.parseInt(num));
                                    roomUserService.updateById(roomUser);
                                    JSONObject jsonObject1=new JSONObject();
                                    jsonObject1.put("type","4");
                                    jsonObject1.put("num",num);
                                    jsonObject1.put("user",userService.selectUserById(Long.parseLong(seatUserId)));
                                    Map<String, AppRoomChatSocket> stringAppRoomChatSocketMap = clients.get(roomId);
                                    for (AppRoomChatSocket item : stringAppRoomChatSocketMap.values()) {
                                        try {
                                            item.session.getAsyncRemote().sendText(jsonObject1.toJSONString());
                                        }catch (Exception e){
                                            e.printStackTrace();
                                            log.error("推送异常："+e.getMessage(),e);
                                        }
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                log.error("申请上麦异常："+e.getMessage(),e);
                            }

                        }else{
                            JSONObject jsonObject1=new JSONObject();
                            jsonObject1.put("type","5");
                            String seatUserId = jsonObject.getString("userId");
                            Map<String, AppRoomChatSocket> stringAppRoomChatSocketMap = clients.get(roomId);
                            AppRoomChatSocket appRoomChatSocket = stringAppRoomChatSocketMap.get(String.valueOf(seatUserId));
                            if(appRoomChatSocket!=null){
                                appRoomChatSocket.session.getAsyncRemote().sendText(jsonObject1.toJSONString());
                            }
                        }
                    }

                }else if("6".equals(type)){
                    //下麦
                    String seatUserId = jsonObject.getString("userId");
                    RoomUser roomUser = roomUserService.selectRoomUserByUserIdAndRoomId(Long.parseLong(this.roomId), Long.parseLong(seatUserId));
                    Integer seatNo = roomUser.getSeatNo();
                    roomUser.setIsSeat(0);
                    roomUser.setSeatNo(0);
                    roomUserService.updateById(roomUser);
                    JSONObject jsonObject1=new JSONObject();
                    jsonObject1.put("type","7");
                    jsonObject1.put("num",seatNo);
                    jsonObject1.put("user",userService.selectUserById(Long.parseLong(seatUserId)));
                    Map<String, AppRoomChatSocket> stringAppRoomChatSocketMap = clients.get(roomId);
                    for (AppRoomChatSocket item : stringAppRoomChatSocketMap.values()) {
                        try {
                            item.session.getAsyncRemote().sendText(jsonObject1.toJSONString());
                        }catch (Exception e){
                            e.printStackTrace();
                            log.error("推送异常："+e.getMessage(),e);
                        }
                    }
                }else if("8".equals(type)){
                    //下麦
                    String seatUserId = jsonObject.getString("userId");
                    RoomUser roomUser = roomUserService.selectRoomUserByUserIdAndRoomId(Long.parseLong(this.roomId), Long.parseLong(seatUserId));
                    Integer seatNo = roomUser.getSeatNo();
                    roomUser.setIsSeat(0);
                    roomUser.setSeatNo(0);
                    roomUserService.updateById(roomUser);
                    JSONObject jsonObject1=new JSONObject();
                    jsonObject1.put("type","9");
                    jsonObject1.put("num",seatNo);
                    jsonObject1.put("user",userService.selectUserById(Long.parseLong(seatUserId)));
                    Map<String, AppRoomChatSocket> stringAppRoomChatSocketMap = clients.get(roomId);
                    for (AppRoomChatSocket item : stringAppRoomChatSocketMap.values()) {
                        try {
                            item.session.getAsyncRemote().sendText(jsonObject1.toJSONString());
                        }catch (Exception e){
                            e.printStackTrace();
                            log.error("推送异常："+e.getMessage(),e);
                        }
                    }
                }else if("10".equals(type)){
                    String num = jsonObject.getString("num");
                    String seatUserId = jsonObject.getString("userId");
                    JSONObject jsonObject1=new JSONObject();
                    jsonObject1.put("type","11");
                    jsonObject1.put("num",num);
                    Map<String, AppRoomChatSocket> stringAppRoomChatSocketMap = clients.get(roomId);
                    AppRoomChatSocket appRoomChatSocket = stringAppRoomChatSocketMap.get(String.valueOf(seatUserId));
                    if(appRoomChatSocket!=null){
                        appRoomChatSocket.session.getAsyncRemote().sendText(jsonObject1.toJSONString());
                    }
                }else if("12".equals(type)){
                    //用户同意或拒绝房主的的上麦请求信息
                    Map<String, AppRoomChatSocket> stringAppRoomChatSocketMaps = clients.get(roomId);
                    synchronized (stringAppRoomChatSocketMaps){
                        String status = jsonObject.getString("status");
                        if("1".equals(status)){
                            String num = jsonObject.getString("num");
                            try {
                                RoomUser roomUser1 = roomUserService.selectRoomUserBySeatNoAndRoomId(Long.parseLong(this.roomId), num);
                                if(roomUser1!=null){
                                    for (int i=1;i<8;i++){
                                        roomUser1 = roomUserService.selectRoomUserBySeatNoAndRoomId(Long.parseLong(this.roomId), String.valueOf(i));
                                        if(roomUser1==null){
                                            num=String.valueOf(i);
                                            break;
                                        }
                                    }
                            /*JSONObject jsonObject1=new JSONObject();
                            jsonObject1.put("type","13");
                            jsonObject1.put("user",userService.selectUserById(Long.parseLong(this.userId)));
                            Room room = roomService.getById(Long.parseLong(this.roomId));
                            Map<String, AppRoomChatSocket> stringAppRoomChatSocketMap = clients.get(roomId);
                            AppRoomChatSocket appRoomChatSocket = stringAppRoomChatSocketMap.get(String.valueOf(room.getUserId()));
                            if(appRoomChatSocket!=null){
                                appRoomChatSocket.session.getAsyncRemote().sendText(jsonObject1.toJSONString());
                            }*/
                                }
                                if(roomUser1==null){
                                    RoomUser roomUser = roomUserService.selectRoomUserByUserIdAndRoomId(Long.parseLong(this.roomId), Long.parseLong(this.userId));
                                    roomUser.setIsSeat(1);
                                    roomUser.setSeatNo(Integer.parseInt(num));
                                    roomUserService.updateById(roomUser);
                                    JSONObject jsonObject1=new JSONObject();
                                    jsonObject1.put("type","4");
                                    jsonObject1.put("num",num);
                                    jsonObject1.put("user",userService.selectUserById(Long.parseLong(this.userId)));
                                    Map<String, AppRoomChatSocket> stringAppRoomChatSocketMap = clients.get(roomId);
                                    for (AppRoomChatSocket item : stringAppRoomChatSocketMap.values()) {
                                        try {
                                            item.session.getAsyncRemote().sendText(jsonObject1.toJSONString());
                                        }catch (Exception e){
                                            e.printStackTrace();
                                            log.error("推送异常："+e.getMessage(),e);
                                        }
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                log.error("上麦异常："+e.getMessage(),e);
                            }



                        }else{
                            JSONObject jsonObject1=new JSONObject();
                            jsonObject1.put("type","13");
                            jsonObject1.put("user",userService.selectUserById(Long.parseLong(this.userId)));
                            Room room = roomService.getById(Long.parseLong(this.roomId));
                            Map<String, AppRoomChatSocket> stringAppRoomChatSocketMap = clients.get(roomId);
                            AppRoomChatSocket appRoomChatSocket = stringAppRoomChatSocketMap.get(String.valueOf(room.getUserId()));
                            if(appRoomChatSocket!=null){
                                appRoomChatSocket.session.getAsyncRemote().sendText(jsonObject1.toJSONString());
                            }
                        }
                    }

                }else if("14".equals(type)){
                    JSONObject jsonObject1=new JSONObject();
                    jsonObject1.put("type","15");
                    jsonObject1.put("user",userService.selectUserById(Long.parseLong(this.userId)));
                    Map<String, AppRoomChatSocket> stringAppRoomChatSocketMap = clients.get(roomId);
                    for (AppRoomChatSocket item : stringAppRoomChatSocketMap.values()) {
                        try {
                            item.session.getAsyncRemote().sendText(jsonObject1.toJSONString());
                        }catch (Exception e){
                            e.printStackTrace();
                            log.error("推送异常："+e.getMessage(),e);
                        }
                    }
                }else if("16".equals(type)){
                    JSONObject jsonObject1=new JSONObject();
                    jsonObject1.put("type","17");
                    Map<String, AppRoomChatSocket> stringAppRoomChatSocketMap = clients.get(roomId);
                    for (AppRoomChatSocket item : stringAppRoomChatSocketMap.values()) {
                        try {
                            item.session.getAsyncRemote().sendText(jsonObject1.toJSONString());
                        }catch (Exception e){
                            e.printStackTrace();
                            log.error("推送异常："+e.getMessage(),e);
                        }
                    }
                }else if("18".equals(type)){
                    JSONObject jsonObject1=new JSONObject();
                    jsonObject1.put("type","19");
                    Map<String, AppRoomChatSocket> stringAppRoomChatSocketMap = clients.get(roomId);
                    for (AppRoomChatSocket item : stringAppRoomChatSocketMap.values()) {
                        try {
                            item.session.getAsyncRemote().sendText(jsonObject1.toJSONString());
                        }catch (Exception e){
                            e.printStackTrace();
                            log.error("推送异常："+e.getMessage(),e);
                        }
                    }
                }else if("20".equals(type)){
                    //房主发起踢人请求
                    String userId = jsonObject.getString("userId");
                    RoomUser roomUser = roomUserService.selectRoomUserByUserIdAndRoomId(Long.parseLong(this.roomId), Long.parseLong(userId));
                    if(roomUser!=null){
                        roomUser.setStatus(3);
                        roomUser.setIsSeat(0);
                        roomUser.setSeatNo(0);
                        roomUserService.updateById(roomUser);
                    }
                    JSONObject jsonObject1=new JSONObject();
                    //推送踢人消息给所有人
                    jsonObject1.put("type","21");
                    jsonObject1.put("user",userService.selectUserById(Long.parseLong(userId)));
                    Map<String, AppRoomChatSocket> stringAppRoomChatSocketMap = clients.get(roomId);
                    for (AppRoomChatSocket item : stringAppRoomChatSocketMap.values()) {
                        try {
                            item.session.getAsyncRemote().sendText(jsonObject1.toJSONString());
                        }catch (Exception e){
                            e.printStackTrace();
                            log.error("推送异常："+e.getMessage(),e);
                        }
                    }
                }
            }

        }
        catch (Exception e){
            log.error("发生了错误了"+e.getMessage(),e);
        }
    }


    /*public void sendMessageTo(String message, String ToUserName) throws IOException {
        for (AppRoomChatSocket item : clients.values()) {
            if (item.userId.equals(ToUserName) ) {
                item.session.getAsyncRemote().sendText(message);
                System.err.println(this.userId+"发送成功："+ToUserName);
                break;
            }
        }
    }

    public void sendMessageAll(String message,String FromUserName) throws IOException {
        for (AppRoomChatSocket item : clients.values()) {
            item.session.getAsyncRemote().sendText(message);
        }
    }*/

    public static synchronized int getOnlineCount() {
        return onlineNumber;
    }

}
