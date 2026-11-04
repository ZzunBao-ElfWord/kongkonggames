package com.pwyl.modules.talk.dao;


import com.pwyl.modules.talk.entity.TalkReward;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface TalkRewardRepository extends JpaRepository<TalkReward, Long> {

    @Query(value = "select t.reward_id as rewardId,u.user_name as nickName,u.avatar as imageUrl," +
            "u.user_id as userId,t.money,t.create_time as createTime,ta.content,ta.img " +
            "from talk_reward t " +
            "left join tb_user u on u.user_id=t.user_id " +
            "left join talk ta on ta.talk_id=t.talk_id " +
            "where t.talk_id=:talkId order by t.money desc",
            countQuery = "select count(*) from talk_reward t where t.talk_id=:talkId order by t.money desc"
            ,nativeQuery = true)
    Page<Map<String,Object>> selectTalkRewardPage(Pageable pageable, Long talkId);


}
