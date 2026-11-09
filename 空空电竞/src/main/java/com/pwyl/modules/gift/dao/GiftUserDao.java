package com.pwyl.modules.gift.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwyl.modules.gift.entity.GiftUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface GiftUserDao extends BaseMapper<GiftUser> {

    IPage<GiftUser> selectGiftUserList(Page<GiftUser> page,Long userId,Long sendUserId,
                                       String userName,String phone,String sendUserName,
                                       String sendPhone,String giftName,Long roomId);

    List<Map<String,Object>> selectNewGiftUser();

    Double selectSumMoneyByTime(@Param("time")String time, @Param("flag")Integer flag);

    BigDecimal selectOrdersMoneyByTime(Integer type, Integer flag, String time);

}
