package com.pwyl.modules.talk.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.app.service.UserService;
import com.pwyl.modules.common.dao.CommonInfoDao;
import com.pwyl.modules.message.entity.MessageInfo;
import com.pwyl.modules.message.service.MessageService;
import com.pwyl.modules.talk.dao.CommentRepository;
import com.pwyl.modules.talk.dao.TalkRepository;
import com.pwyl.modules.talk.entity.Comment;
import com.pwyl.modules.talk.entity.Talk;
import com.pwyl.modules.talk.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 点赞
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TalkRepository talkRepository;
    @Autowired
    private MessageService messageService;
    @Autowired
    private CommonInfoDao commonInfoDao;


    @Override
    public Page<Map<String,Object>> selectCommentPage(Integer page,Integer size,Long userId,Long talkId){
        Pageable pageable= PageRequest.of(page,size);
        return commentRepository.selectCommentPage(pageable,userId==null?0L:userId,talkId==null?0L:talkId);
    }

    @Override
    public IPage<Map<String,Object>> selectCommentPage(Integer page, Integer limit, Long userId, Long talkId, String userName, String phone, String content){
        return commonInfoDao.selectCommentPage(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, limit),userId==null?0L:userId,talkId==null?0L:talkId,userName,phone,content);
    }

    @Override
    public Page<Map<String,Object>> selectCommentParentIdPage(Integer page,Integer size,Long userId,Long talkId,Long parentId){
        Pageable pageable= PageRequest.of(page,size);
        return commentRepository.selectCommentParentIdPage(pageable,userId==null?0L:userId,talkId==null?0L:talkId,parentId);
    }




    @Override
    public Comment saveComment(Comment comment){
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setState(String.valueOf(5));
        if(comment.getParentId()==null){
            comment.setParentId(0);
            Talk talk = talkRepository.findById(comment.getTalkId()).orElse(null);
            UserEntity userEntity = userService.selectUserById(talk.getUserId());
            UserEntity user = userService.selectUserById(comment.getUserId());
            if(userEntity!=null && StringUtils.isNotEmpty(userEntity.getClientid())){
                userService.pushToSingle("新的评论",user.getUserName()+":"+comment.getContent(),userEntity.getClientid());
            }
            messageInfo.setContent(user.getUserName()+":"+comment.getContent());
            messageInfo.setTitle("新的评论");
            messageInfo.setUserName(userEntity.getUserName());
            messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
        }else{
            UserEntity userEntity = userService.selectUserById(comment.getByUserId());
            UserEntity user = userService.selectUserById(comment.getUserId());
            if(userEntity!=null && StringUtils.isNotEmpty(userEntity.getClientid())){
                userService.pushToSingle("评论回复",user.getUserName()+":"+comment.getContent(),userEntity.getClientid());
            }
            messageInfo.setContent(user.getUserName()+":"+comment.getContent());
            messageInfo.setTitle("评论回复");
            messageInfo.setUserName(userEntity.getUserName());
            messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
        }
        messageService.saveBody(messageInfo);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        comment.setCreateTime(sdf.format(new Date()));
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long id){
        commentRepository.deleteById(id);
    }





}
