package com.pwyl.modules.invite.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwyl.modules.invite.entity.InviteMoney;

/**
 * 邀请收益
 *
 */
public interface InviteMoneyService extends IService<InviteMoney> {

    InviteMoney selectInviteMoneyByUserId(Long userId);

    int updateInviteMoneySum(Double money,Long userId);

    int updateInviteMoneyCashOut(Double money,Long userId);


}
