package com.pwyl.modules.message.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwyl.common.utils.PageUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.message.entity.MessageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.util.Map;

public interface MessageService extends IService<MessageInfo> {

    PageUtils selectMessageList(Map<String,Object> params);

    int saveBody(MessageInfo messageInfo);

    int update(MessageInfo messageInfo);

    int delete(Long id);

    MessageInfo selectMessageById(Long id);

    int updateSendState(Long userId,Integer state);

    Result auditMessage(Long messageId,Integer status,String auditContent);

}
