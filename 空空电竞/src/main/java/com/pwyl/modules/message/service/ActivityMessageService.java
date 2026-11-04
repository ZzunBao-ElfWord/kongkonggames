package com.pwyl.modules.message.service;


import com.pwyl.common.utils.PageUtils;
import com.pwyl.modules.message.entity.ActivityMessageInfo;

import java.util.List;

public interface ActivityMessageService {

    int saveBody(ActivityMessageInfo messageInfo);

    List<ActivityMessageInfo> findAll();

    ActivityMessageInfo findOne(long id);

    ActivityMessageInfo selectById(long id);

    int delete(long id);

    PageUtils find(String state, int page,int limit);

    int updateBody(ActivityMessageInfo userInfo);

    PageUtils findType(Integer type, int page,int limit);

    int updateState(String state, Long id);

    int updateSendState(String state, Long id);

    PageUtils findTypeByUserId( String type,String userId, int page,int limit);


}
