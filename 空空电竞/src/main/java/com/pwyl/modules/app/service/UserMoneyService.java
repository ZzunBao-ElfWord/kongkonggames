package com.pwyl.modules.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pwyl.common.utils.PageUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.entity.UserMoney;
import com.pwyl.modules.app.entity.UserMoneyDetails;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.math.BigDecimal;

public interface UserMoneyService extends IService<UserMoney> {

    UserMoney selectUserMoneyByUserId(Long userId);

    void updateMoney(int i, Long userId, double money);

    void updateEarningsMoney(int i, Long userId, double money);

    Double selectMyProfit(Long userId);

    Result payTakingOrder(Long userId, Long orderId);

    Result completePaidVipOrder(Long orderId);

    PageUtils balanceDetailed(@RequestAttribute Long userId, Page<UserMoneyDetails> ipage,Integer classify);

    Result profitDetailed(@RequestAttribute Long userId, IPage ipage);

    Result inviteMoneyConvertMoney(Long userId, Long payClassifyId);

}
