package com.pwyl.modules.chats.service;


import com.pwyl.modules.chats.entity.Chats;
import com.pwyl.modules.chats.utils.Result;

public interface ChatsService {

    /**
     * 商家端未读消息
     * @param storeId
     * @return
     */
    Result count(Long storeId);
    Result userCount(Long count);

    /**
     * 商家端会话列表
     * @param storeId
     * @return
     */
    Result findAll(Long storeId, String userName);

    /**
     * 用户端会话列表
     * @param userId
     * @return
     */
    Result userList(Long userId);

    //查询
    Result findOne(Long id);

    //删除
    Result delete(Long id);

    //添加
    Result saveBody(Chats entity);

    //修改
    Result updateBody(Chats entity);

}
