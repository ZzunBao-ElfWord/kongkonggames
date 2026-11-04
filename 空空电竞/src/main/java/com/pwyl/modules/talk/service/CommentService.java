package com.pwyl.modules.talk.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pwyl.modules.talk.entity.Comment;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface CommentService {

    Page<Map<String,Object>> selectCommentPage(Integer page, Integer size, Long userId, Long talkId);

    IPage<Map<String,Object>> selectCommentPage(Integer page, Integer limit, Long userId, Long talkId, String userName, String phone, String content);

    Page<Map<String,Object>> selectCommentParentIdPage(Integer page, Integer size, Long userId, Long talkId, Long parentId);

    Comment saveComment(Comment comment);

    void deleteComment(Long id);

}
