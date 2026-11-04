package com.pwyl.modules.gift.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.gift.entity.GiftUser;

public interface GiftUserService extends IService<GiftUser> {

    Result selectGiftUser(Integer page, Integer limit, Long userId,Long sendUserId,String userName,
                          String phone,String sendUserName,String sendPhone,String giftName,Long roomId);

    Result sendGift(GiftUser giftUser);
}
