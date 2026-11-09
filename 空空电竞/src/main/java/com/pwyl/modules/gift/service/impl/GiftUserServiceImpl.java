package com.pwyl.modules.gift.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwyl.common.utils.PageUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.app.entity.UserMoney;
import com.pwyl.modules.app.entity.UserMoneyDetails;
import com.pwyl.modules.app.service.UserMoneyDetailsService;
import com.pwyl.modules.app.service.UserMoneyService;
import com.pwyl.modules.app.service.UserService;
import com.pwyl.modules.common.service.CommonInfoService;
import com.pwyl.modules.consortia.entity.Consortia;
import com.pwyl.modules.consortia.service.ConsortiaService;
import com.pwyl.modules.gift.dao.GiftUserDao;
import com.pwyl.modules.gift.entity.Gift;
import com.pwyl.modules.gift.entity.GiftUser;
import com.pwyl.modules.gift.service.GiftService;
import com.pwyl.modules.gift.service.GiftUserService;
import com.pwyl.modules.room.entity.RoomUser;
import com.pwyl.modules.room.service.RoomUserService;
import com.pwyl.modules.taking.entity.OrderTakingReward;
import com.pwyl.modules.taking.service.OrderTakingRewardService;
import com.pwyl.modules.talk.dao.TalkRewardRepository;
import com.pwyl.modules.talk.entity.TalkReward;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class GiftUserServiceImpl extends ServiceImpl<GiftUserDao, GiftUser> implements GiftUserService {

    @Autowired
    private GiftService giftService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private TalkRewardRepository talkRewardRepository;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private OrderTakingRewardService orderTakingRewardService;
    @Autowired
    private ConsortiaService consortiaService;
    @Autowired
    private RoomUserService roomUserService;

    private ReentrantReadWriteLock reentrantReadWriteLock=new ReentrantReadWriteLock(true);

    @Override
    public Result selectGiftUser(Integer page, Integer limit, Long userId,Long sendUserId,String userName,
                                 String phone,String sendUserName,String sendPhone,String giftName,Long roomId){
        Page<GiftUser> pages=new Page<>(page,limit);
        IPage<GiftUser> giftUserIPage = baseMapper.selectGiftUserList(pages, userId, sendUserId, userName,
                phone, sendUserName, sendPhone, giftName,roomId);
        return Result.success().put("data",new PageUtils(giftUserIPage));
    }

    @Override
    public Result sendGift(GiftUser giftUser){
        reentrantReadWriteLock.writeLock().lock();
        try{
            Gift gift = giftService.getById(giftUser.getGiftId());
            UserMoney userMoney = userMoneyService.selectUserMoneyByUserId(giftUser.getSendUserId());
            if(userMoney.getMoney().doubleValue()<gift.getGiftMoney().doubleValue()){
                return Result.error("金额不足，请充值！");
            }
            UserEntity userEntity = userService.selectUserById(giftUser.getUserId());
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(new Date());
            userMoneyService.updateMoney(2,giftUser.getSendUserId(),gift.getGiftMoney().doubleValue());
            UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
            userMoneyDetails.setUserId(giftUser.getSendUserId());
            userMoneyDetails.setTitle("[赠送礼物]"+gift.getGiftName());
            userMoneyDetails.setContent("赠送用户："+userEntity.getUserName()+"，"+gift.getGiftName()+" 礼物");
            userMoneyDetails.setType(2);
            userMoneyDetails.setMoney(gift.getGiftMoney());
            userMoneyDetails.setCreateTime(time);
            userMoneyDetailsService.save(userMoneyDetails);

            String value = commonInfoService.findOne(202).getValue();
            BigDecimal money=gift.getGiftMoney().subtract(gift.getGiftMoney().multiply(new BigDecimal(value)));
            money=money.setScale(2,BigDecimal.ROUND_DOWN);
            userEntity = userService.selectUserById(giftUser.getSendUserId());
            userMoneyService.updateEarningsMoney(1,giftUser.getUserId(),money.doubleValue());
            userMoneyDetails=new UserMoneyDetails();
            userMoneyDetails.setUserId(giftUser.getUserId());
            userMoneyDetails.setTitle("[收到礼物]"+gift.getGiftName());
            userMoneyDetails.setContent("收到用户："+userEntity.getUserName()+"，送来的 "+gift.getGiftName()+" 礼物");
            userMoneyDetails.setType(1);
            userMoneyDetails.setClassify(10);
            userMoneyDetails.setMoney(money);
            userMoneyDetails.setCreateTime(time);
            userMoneyDetailsService.save(userMoneyDetails);

            giftUser.setGiftImg(gift.getGiftImg());
            giftUser.setGiftMoney(gift.getGiftMoney());
            giftUser.setGiftName(gift.getGiftName());
            giftUser.setCreateTime(time);
            giftUser.setUserMoney(money);

            //同步之前的礼物
            if(giftUser.getTalkId()!=null){
                TalkReward talkReward=new TalkReward();
                talkReward.setTalkId(giftUser.getTalkId());
                talkReward.setUserId(giftUser.getSendUserId());
                talkReward.setMoney(money);
                talkReward.setCreateTime(sdf.format(new Date()));
                talkRewardRepository.save(talkReward);
            }
            if(giftUser.getOrderTakingId()!=null){
                OrderTakingReward orderTakingReward=new OrderTakingReward();
                orderTakingReward.setCreateTime(sdf.format(new Date()));
                orderTakingReward.setMoney(money);
                orderTakingReward.setOrderTakingId(giftUser.getOrderTakingId());
                orderTakingReward.setUserId(giftUser.getUserId());
                orderTakingRewardService.save(orderTakingReward);
            }
            UserEntity peiUser = userService.selectUserById(giftUser.getUserId());
            if(peiUser!=null){
                //判断陪玩是否有公会
                if(peiUser.getConsortiaId()!=null){
                    Consortia consortia = consortiaService.getById(peiUser.getConsortiaId());
                    if(consortia!=null){
                        UserEntity consortiaUser = userService.selectUserById(consortia.getUserId());
                        if(consortiaUser!=null && consortiaUser.getConsortiaGiftRate()!=null && consortiaUser.getConsortiaGiftRate().doubleValue()>0){
                            BigDecimal consortiaMoney = gift.getGiftMoney().multiply(consortiaUser.getConsortiaGiftRate()).setScale(2, BigDecimal.ROUND_DOWN);
                            giftUser.setConsortiaUserId(consortiaUser.getUserId());
                            giftUser.setConsortiaRate(consortiaMoney);
                            userMoneyService.updateEarningsMoney(1, consortiaUser.getUserId(), consortiaMoney.doubleValue());
                            userMoneyDetails = new UserMoneyDetails();
                            userMoneyDetails.setUserId(consortiaUser.getUserId());
                            userMoneyDetails.setByUserId(peiUser.getUserId());
                            userMoneyDetails.setTitle("陪陪收到礼物：" + gift.getGiftName());
                            userMoneyDetails.setContent("公会陪陪："+peiUser.getUserName()+"，收到用户："+userEntity.getUserName()+"，送来的 "+gift.getGiftName()+" 礼物");
                            userMoneyDetails.setType(1);
                            userMoneyDetails.setClassify(6);
                            userMoneyDetails.setMoney(consortiaMoney);
                            userMoneyDetails.setCreateTime(time);
                            userMoneyDetailsService.save(userMoneyDetails);
                        }
                    }
                }
            }
            BigDecimal pingMoney=giftUser.getGiftMoney().subtract(giftUser.getUserMoney());
            if(giftUser.getConsortiaRate()!=null){
                pingMoney=pingMoney.subtract(giftUser.getConsortiaRate());
            }
            giftUser.setPingMoney(pingMoney);
            baseMapper.insert(giftUser);
            if(giftUser.getRoomUserId()!=null){
                RoomUser roomUser = roomUserService.getById(giftUser.getRoomUserId());
                if(roomUser!=null){
                    if(roomUser.getGiftCount()==null){
                        roomUser.setGiftCount(1);
                    }else{
                        roomUser.setGiftCount(roomUser.getGiftCount()+1);
                    }
                    roomUserService.updateById(roomUser);
                }
            }


            return Result.success();

        }catch (Exception e){
            e.printStackTrace();
            log.error("赠送礼物异常："+e.getMessage(),e);
        }finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return Result.error("系统繁忙，请稍后再试！");
    }





}
