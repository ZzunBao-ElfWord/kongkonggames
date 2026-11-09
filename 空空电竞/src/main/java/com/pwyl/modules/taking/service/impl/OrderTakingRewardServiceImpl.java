package com.pwyl.modules.taking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import com.pwyl.modules.taking.dao.GameDao;
import com.pwyl.modules.taking.dao.OrderTakingRewardDao;
import com.pwyl.modules.taking.entity.Game;
import com.pwyl.modules.taking.entity.OrderTaking;
import com.pwyl.modules.taking.entity.OrderTakingReward;
import com.pwyl.modules.taking.service.GameService;
import com.pwyl.modules.taking.service.OrderTakingRewardService;
import com.pwyl.modules.taking.service.OrderTakingService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OrderTakingRewardServiceImpl extends ServiceImpl<OrderTakingRewardDao, OrderTakingReward> implements OrderTakingRewardService {

    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private OrderTakingService orderTakingService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private CommonInfoService commonInfoService;


    @Override
    public Result rewardMoney(Long userId, BigDecimal money, Long orderTakingId){
        String min = commonInfoService.findOne(203).getValue();
        if(Double.parseDouble(min)>money.doubleValue()){
            return Result.error("打赏金额太少！");
        }
        String max = commonInfoService.findOne(204).getValue();
        if(money.doubleValue()>Double.parseDouble(max)){
            return Result.error("打赏金额太多！");
        }
        UserMoney userMoney = userMoneyService.selectUserMoneyByUserId(userId);
        if(money.doubleValue()>userMoney.getMoney().doubleValue()){
            return Result.error("金币不足！");
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(new Date());

        OrderTaking orderTaking = orderTakingService.getById(orderTakingId);

        OrderTakingReward orderTakingReward=new OrderTakingReward();
        orderTakingReward.setCreateTime(format);
        orderTakingReward.setMoney(money);
        orderTakingReward.setOrderTakingId(orderTakingId);
        orderTakingReward.setUserId(userId);
        baseMapper.insert(orderTakingReward);

        UserEntity userEntity = userService.selectUserById(orderTaking.getUserId());
        UserEntity sendUserEntity = userService.selectUserById(userId);
        userMoneyService.updateMoney(2,userId,money.doubleValue());
        UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
        userMoneyDetails.setMoney(money);
        userMoneyDetails.setUserId(userId);
        userMoneyDetails.setContent("打赏用户："+userEntity.getUserName());
        userMoneyDetails.setTitle("打赏");
        userMoneyDetails.setType(2);

        userMoneyDetails.setCreateTime(format);
        userMoneyDetailsService.save(userMoneyDetails);

        String value = commonInfoService.findOne(202).getValue();
        BigDecimal multiply = money.multiply(new BigDecimal(value));
        BigDecimal subtract = money.subtract(multiply);
        userMoneyService.updateEarningsMoney(1,userEntity.getUserId(),subtract.doubleValue());
        userMoneyDetails=new UserMoneyDetails();
        userMoneyDetails.setMoney(subtract);
        userMoneyDetails.setUserId(userEntity.getUserId());
        userMoneyDetails.setContent("接受用户 "+sendUserEntity.getUserName()+" 到打赏，赏金："+subtract);
        userMoneyDetails.setTitle("赏金");
        userMoneyDetails.setType(1);
        userMoneyDetails.setClassify(10);
        userMoneyDetails.setCreateTime(format);
        userMoneyDetailsService.save(userMoneyDetails);
        return Result.success();
    }

    @Override
    public Result selectOrderTakingRewardList(Integer page,Integer limit,Long userId,Long orderTakingId){
        Page<Map<String,Object>> pages=new Page<>(page,limit);
        return Result.success().put("data",new PageUtils(baseMapper.selectOrderTakingRewardList(pages,userId,orderTakingId)));
    }


}
