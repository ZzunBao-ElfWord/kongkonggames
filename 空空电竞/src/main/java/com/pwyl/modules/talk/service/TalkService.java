package com.pwyl.modules.talk.service;



import com.pwyl.modules.talk.entity.Talk;
import com.pwyl.modules.talk.utils.Result;

import java.util.Map;

public interface TalkService {

    Talk saveTalk(Talk talk);

    Talk updateTalk(Talk talk);

    Result updateTalkStatus(Long talkId);

    Result updateTalkStatus(Long talkId,Integer status,String auditContent);

    Result deleteTalk(Long talkId);

    void deleteTalkById(Long id);

    Map<String,Object> selectTalkPage(Integer page, Integer size, Integer status, Long userId, Long byUserId,
                                      Long followUserId,Integer isDelete,String nickName,String content,Integer videoOnly);

    Map<String,Object> selectTalkByTalkId(Long talkId, Integer status, Long userId, Long byUserId);

    Talk selectById(Long talk);

    int addShare(Long talkId);

}
