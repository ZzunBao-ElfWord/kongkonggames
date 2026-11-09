package com.pwyl.modules.taking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.taking.entity.OrderTakingReward;

import java.math.BigDecimal;

public interface OrderTakingRewardService extends IService<OrderTakingReward> {

    Result rewardMoney(Long userId, BigDecimal money, Long orderTakingId);

    Result selectOrderTakingRewardList(Integer page,Integer limit,Long userId,Long orderTakingId);

}
