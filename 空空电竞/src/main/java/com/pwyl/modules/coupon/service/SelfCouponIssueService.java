package com.pwyl.modules.coupon.service;


import com.pwyl.modules.coupon.entity.SelfCouponIssue;
import com.pwyl.modules.coupon.utils.Result;

public interface SelfCouponIssueService {
    //列表
    Result findAll(Integer page, Integer size);

    //用户端
    Result useList(Long goodsId, Long userId);

    //查询
    Result findOne(Long id);

    //删除
    Result delete(Long id);

    //添加
    Result saveBody(SelfCouponIssue entity);

    //修改
    Result updateBody(SelfCouponIssue entity);

}
