package com.pwyl.modules.coupon.service;

import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.coupon.utils.Result;
import com.pwyl.modules.coupon.entity.SelfCouponUser;

import java.math.BigDecimal;

public interface SelfCouponUserService {

    Result getNewUserCoupon(UserEntity userEntity);

    //列表
    Result findAll(Integer page, Integer size, String couponName,String nickName);

    //用户端优惠券列表
    Result userList(Integer page, Integer size, Long userId, String goodsId, Integer type, BigDecimal money);

    Result useList(Long userId, String goodsId);

    //查询
    Result findOne(Long id);

    //删除
    Result delete(Long id);

    //添加
    Result saveBody(SelfCouponUser entity);

    //修改
    Result updateBody(SelfCouponUser entity);

    Result sendUserCoupon(String userIds,String couponIds,Integer type);

}
