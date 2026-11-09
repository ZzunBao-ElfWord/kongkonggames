package com.pwyl.modules.talk.dao;


import com.pwyl.modules.talk.entity.GoodTalk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodTalkRepository extends JpaRepository<GoodTalk, Long> {

    @Query(value = "select * from good_talk where talk_id=:talkId and user_id=:userId",nativeQuery = true)
    GoodTalk selectGoodTalkCountByTalkIdAndUserId(@Param("talkId") Long talkId, @Param("userId") Long userId);


}
