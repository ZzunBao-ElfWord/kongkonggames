package com.pwyl.modules.talk.dao;

import com.pwyl.modules.talk.entity.Talk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Repository
public interface TalkRepository extends JpaRepository<Talk, Long> {

    @Query(value = "select t.talk_id as talkId,t.status,u.user_name as nickName,u.avatar as imageUrl," +
            "t.create_time as createTime,t.content,t.img,t.share_num as shareNum,t.user_id as userId," +
            "(select count(*) from comment c where c.talk_id=t.talk_id) as commentCount," +
            "(select count(*) from talk_reward c where c.talk_id=t.talk_id and c.user_id=:byUserId) as rewardByUserCount," +
            "(select count(*) from talk_reward c where c.talk_id=t.talk_id) as rewardCount," +
            "(select count(*) from comment c where c.talk_id=t.talk_id and c.user_id=:byUserId) as commentByUserCount," +
            "(select count(*) from good_talk c where c.talk_id=t.talk_id and c.user_id=:byUserId) as goodTalkByUserCount, " +
            "(select count(*) from good_talk c where c.talk_id=t.talk_id) as goodTalkCount " +
            "from talk t " +
            "left join tb_user u on u.user_id=t.user_id " +
            "where u.user_id is not null and  if(:followUserId!=0,u.user_id in (select by_user_id from attention where user_id=:followUserId),1=1) " +
            "and if(:userId!=0,t.user_id=:userId,1=1) and t.status=:status order by t.create_time desc ",
            countQuery = "select count(*) from talk t " +
                    "left join tb_user u on u.user_id=t.user_id " +
                    "where u.user_id is not null and  if(:followUserId!=0,u.user_id in (select by_user_id from attention where user_id=:followUserId),1=1) " +
                    "and if(:userId!=0,t.user_id=:userId,1=1) and t.status=:status  order by t.create_time desc ",nativeQuery = true)
    Page<Map<String,Object>> selectTalkPage(Pageable pageable, @Param("status") Integer status, @Param("userId") Long userId, @Param("byUserId") Long byUserId, @Param("followUserId") Long followUserId);

    @Query(value = "select t.talk_id as talkId,t.status,u.user_name as nickName,u.avatar as imageUrl,t.audit_content as auditContent," +
            "t.create_time as createTime,t.content,t.img,t.video_url as videoUrl,t.share_num as shareNum,t.user_id as userId," +
            "(select count(*) from comment c where c.talk_id=t.talk_id) as commentCount," +
            "(select count(*) from talk_reward c where c.talk_id=t.talk_id and c.user_id=:byUserId) as rewardByUserCount," +
            "(select count(*) from talk_reward c where c.talk_id=t.talk_id) as rewardCount," +
            "(select count(*) from comment c where c.talk_id=t.talk_id and c.user_id=:byUserId) as commentByUserCount," +
            "(select count(*) from good_talk c where c.talk_id=t.talk_id and c.user_id=:byUserId) as goodTalkByUserCount, " +
            "(select count(*) from good_talk c where c.talk_id=t.talk_id) as goodTalkCount " +
            "from talk t " +
            "left join tb_user u on u.user_id=t.user_id  " +
            "where if(:talkId!=0,t.talk_id=:talkId,1=1) " +
            "and if(:userId!=0,t.user_id=:userId,1=1) and if(:status!=0,t.status=:status,1=1) order by t.create_time desc ",
            nativeQuery = true)
    Map<String,Object> selectTalkByTalkId(@Param("talkId") Long talkId, @Param("status") Integer status, @Param("userId") Long userId, @Param("byUserId") Long byUserId);



    @Transactional
    @Modifying
    @Query(value = "update talk set share_num=share_num+1 where talk_id=:talkId",nativeQuery = true)
    int updateAddShareNum(@Param("talkId") Long talkId);


    @Transactional
    @Modifying
    @Query(value = "update talk set status=3 where user_id=:userId",nativeQuery = true)
    int updateTalkStatusByUserId(@Param("userId") Long userId);

}
