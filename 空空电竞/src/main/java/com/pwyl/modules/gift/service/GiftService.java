package com.pwyl.modules.gift.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.gift.entity.Gift;

import java.util.List;

public interface GiftService extends IService<Gift> {

    List<Gift> selectGiftList(Integer status, String giftName);

    Result selectGiftPage(Integer page,Integer limit,Integer status,String giftName);

}
