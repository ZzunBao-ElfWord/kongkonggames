package com.pwyl.modules.talk.service;


import com.pwyl.modules.talk.entity.TalkReward;
import com.pwyl.modules.talk.utils.Result;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface TalkRewardService {

    void deleteTalkReward(Long rewardId);

    Result insertTalkReward(TalkReward talkReward);

    Page<Map<String,Object>> selectTalkRewardPage(Integer page, Integer size, Long talkId);

}
