package com.pwyl.modules.talk.dao;


import com.pwyl.modules.talk.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "select c.content,ifnull(u.user_name,'神秘用户') as nickName,u.avatar as imageUrl,c.create_time as createTime,c.comment_id as commentId  " +
            "from comment c " +
            "left join tb_user u on u.user_id=c.user_id " +
            "where parent_id=0 and  if(:userId!=0,c.user_id=:userId,1=1) " +
            "and if(:talkId!=0,c.talk_id=:talkId,1=1) order by c.create_time desc ",
            countQuery = "select count(*) from comment c " +
                    "left join tb_user u on u.user_id=c.user_id " +
                    "where parent_id=0 and  if(:userId!=0,c.user_id=:userId,1=1) " +
                    "and if(:talkId!=0,c.talk_id=:talkId,1=1) order by c.create_time desc ",
            nativeQuery = true)
    Page<Map<String,Object>> selectCommentPage(Pageable pageable, Long userId, Long talkId);


    @Query(value = "select c.content,ifnull(u1.user_name,'神秘用户') as nickName,u1.avatar as imageUrl,ifnull(u2.user_name,'神秘用户') as byNickName,u2.avatar as byImageUrl,c.create_time as createTime,c.comment_id as commentId  " +
            "from comment c " +
            "left join tb_user u1 on u.user_id=c.user_id " +
            "left join tb_user u2 on u.user_id=c.by_user_id " +
            "where parent_id=:parentId and  if(:userId!=0,c.user_id=:userId,1=1) " +
            "and if(:talkId!=0,c.talk_id=:talkId,1=1) order by c.create_time desc ",
            countQuery = "select count(*) from comment c " +
                    "left join tb_user u on u.user_id=c.user_id " +
                    "where parent_id=:parentId and  if(:userId!=0,c.user_id=:userId,1=1) " +
                    "and if(:talkId!=0,c.talk_id=:talkId,1=1) order by c.create_time desc ",
            nativeQuery = true)
    Page<Map<String,Object>> selectCommentParentIdPage(Pageable pageable, Long userId, Long talkId, Long parentId);


}
