package com.pwyl.modules.talk.service;


import com.pwyl.modules.talk.entity.GoodTalk;

public interface GoodTalkService {

    GoodTalk saveGoodTalk(GoodTalk goodTalk);

    void deleteGoodTalkById(Long id);


}
