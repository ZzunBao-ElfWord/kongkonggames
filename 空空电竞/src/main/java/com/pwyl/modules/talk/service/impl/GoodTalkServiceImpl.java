package com.pwyl.modules.talk.service.impl;

import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.app.service.UserService;
import com.pwyl.modules.message.entity.MessageInfo;
import com.pwyl.modules.message.service.MessageService;
import com.pwyl.modules.talk.dao.GoodTalkRepository;
import com.pwyl.modules.talk.dao.TalkRepository;
import com.pwyl.modules.talk.entity.GoodTalk;
import com.pwyl.modules.talk.entity.Talk;
import com.pwyl.modules.talk.service.GoodTalkService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 点赞
 */
@Service
@Slf4j
public class GoodTalkServiceImpl implements GoodTalkService {

    @Autowired
    private GoodTalkRepository goodTalkRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private TalkRepository talkRepository;
    private ReentrantReadWriteLock reentrantReadWriteLock=new ReentrantReadWriteLock(true);

    @Override
    public GoodTalk saveGoodTalk(GoodTalk goodTalk){
        reentrantReadWriteLock.writeLock().lock();
        try {
            GoodTalk goodTalk1 = goodTalkRepository.selectGoodTalkCountByTalkIdAndUserId(goodTalk.getTalkId(), goodTalk.getUserId());
            if(goodTalk1!=null){
                goodTalkRepository.deleteById(goodTalk1.getGoodTalkId());
                return null;
            }else{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                goodTalk.setCreateTime(sdf.format(new Date()));

                Talk talk = talkRepository.findById(goodTalk.getTalkId()).orElse(null);
                UserEntity userEntity = userService.selectUserById(talk.getUserId());
                UserEntity user = userService.selectUserById(goodTalk.getUserId());
                if(userEntity!=null && StringUtils.isNotEmpty(userEntity.getClientid())){
                    userService.pushToSingle("动态点赞",user.getUserName()+"点赞了您的动态",userEntity.getClientid());
                }
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setState(String.valueOf(5));
                messageInfo.setContent(user.getUserName()+"点赞了您的动态");
                messageInfo.setTitle("动态点赞");
                messageInfo.setUserName(userEntity.getUserName());
                messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
                messageInfo.setIsSee("0");
                messageService.saveBody(messageInfo);


                return goodTalkRepository.save(goodTalk);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("点赞异常："+e.getMessage(),e);
        }finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return null;
    }

    @Override
    public void deleteGoodTalkById(Long id){
        goodTalkRepository.deleteById(id);
    }







}
