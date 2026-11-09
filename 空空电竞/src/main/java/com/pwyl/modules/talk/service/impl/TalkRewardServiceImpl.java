package com.pwyl.modules.talk.service.impl;

import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.app.entity.UserMoney;
import com.pwyl.modules.app.entity.UserMoneyDetails;
import com.pwyl.modules.app.service.UserMoneyDetailsService;
import com.pwyl.modules.app.service.UserMoneyService;
import com.pwyl.modules.app.service.UserService;
import com.pwyl.modules.common.service.CommonInfoService;
import com.pwyl.modules.message.entity.MessageInfo;
import com.pwyl.modules.message.service.MessageService;
import com.pwyl.modules.talk.dao.TalkRepository;
import com.pwyl.modules.talk.dao.TalkRewardRepository;
import com.pwyl.modules.talk.entity.Talk;
import com.pwyl.modules.talk.entity.TalkReward;
import com.pwyl.modules.talk.service.TalkRewardService;
import com.pwyl.modules.talk.utils.Result;
import com.pwyl.modules.talk.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 打赏
 */
@Service
@Slf4j
public class TalkRewardServiceImpl implements TalkRewardService {

    @Autowired
    private TalkRewardRepository talkRewardRepository;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private TalkRepository talkRepository;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    private ReentrantReadWriteLock reentrantReadWriteLock=new ReentrantReadWriteLock(true);

    @Override
    public Result insertTalkReward(TalkReward talkReward){
        reentrantReadWriteLock.writeLock().lock();
        try{
            if(talkReward.getMoney().doubleValue()<=0){
                return ResultUtil.error(-200,"请输入正确的打赏金额！");
            }
            Talk talk = talkRepository.findById(talkReward.getTalkId()).orElse(null);
            if(talk==null){
                return ResultUtil.error(-200,"动态不存在，请刷新后重试！");
            }
            if(talk.getUserId().equals(talkReward.getUserId())){
                return ResultUtil.error(-200,"不可以打赏自己的动态！");
            }

            UserMoney userMoney = userMoneyService.selectUserMoneyByUserId(talkReward.getUserId());
            if(userMoney.getMoney().doubleValue()>= talkReward.getMoney().doubleValue()){
                //扣除用户的积分
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                userMoneyService.updateMoney(2,talkReward.getUserId(),talkReward.getMoney().doubleValue());
                UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
                userMoneyDetails.setMoney(talkReward.getMoney());
                userMoneyDetails.setUserId(talkReward.getUserId());
                userMoneyDetails.setContent("打赏动态，消耗："+talkReward.getMoney());
                userMoneyDetails.setTitle("打赏动态");
                userMoneyDetails.setType(2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
                userMoneyDetailsService.save(userMoneyDetails);

                UserEntity userEntity = userService.selectUserById(talkReward.getUserId());

                userMoneyService.updateEarningsMoney(1,talk.getUserId(),talkReward.getMoney().doubleValue());
                userMoneyDetails=new UserMoneyDetails();
                userMoneyDetails.setMoney(talkReward.getMoney());
                userMoneyDetails.setUserId(talk.getUserId());
                userMoneyDetails.setContent(userEntity.getUserName()+" 打赏了您的动态，获得赏金积分："+talkReward.getMoney());
                userMoneyDetails.setTitle("打赏赏金");
                userMoneyDetails.setType(1);
                userMoneyDetails.setClassify(10);
                userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
                userMoneyDetailsService.save(userMoneyDetails);

                MessageInfo messageInfo=new MessageInfo();
                messageInfo.setContent(userEntity.getUserName()+" 打赏了您的动态，获得赏金积分："+talkReward.getMoney());
                userEntity = userService.selectUserById(talk.getUserId());
                messageInfo.setState(String.valueOf(5));
                messageInfo.setTitle("打赏通知");
                messageInfo.setUserName(userEntity.getUserName());
                messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
                messageService.saveBody(messageInfo);
                if (userEntity.getClientid() != null) {
                    userService.pushToSingle("打赏通知", userEntity.getUserName()+" 打赏了您的动态，获得赏金积分："+talkReward.getMoney(), userEntity.getClientid());
                }
                talkReward.setCreateTime(sdf.format(new Date()));
                talkRewardRepository.save(talkReward);
                return ResultUtil.success();
            }else{
                return ResultUtil.error(-200,"积分不足！");
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("打赏动态出错:"+e.getMessage(),e);
        }finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return ResultUtil.error(500,"系统繁忙，请稍后再试");
    }

    @Override
    public void deleteTalkReward(Long rewardId){
        talkRewardRepository.deleteById(rewardId);
    }

    @Override
    public Page<Map<String,Object>> selectTalkRewardPage(Integer page,Integer size,Long talkId){
        Pageable pageable=PageRequest.of(page,size);
        return talkRewardRepository.selectTalkRewardPage(pageable,talkId);
    }




}
