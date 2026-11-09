package com.pwyl.modules.common.service.impl;

import com.alipay.api.domain.Coupon;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.common.dao.CommonInfoDao;
import com.pwyl.modules.common.entity.CommonInfo;
import com.pwyl.modules.common.service.CommonInfoService;
import com.pwyl.modules.coupon.entity.SelfCoupon;
import com.pwyl.modules.coupon.respository.SelfCouponJpaRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author fang
 * @date 2020/7/8
 */
@Service
public class CommonInfoServiceImpl extends ServiceImpl<CommonInfoDao, CommonInfo> implements CommonInfoService {


    private final CommonInfoDao commonInfoDao;
    @Autowired
    private SelfCouponJpaRepository couponJpaRepository;

    @Autowired
    public CommonInfoServiceImpl(CommonInfoDao commonInfoDao) {
        this.commonInfoDao = commonInfoDao;
    }

    @Override
    public Result update(CommonInfo commonInfo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        commonInfo.setCreateAt(sdf.format(now));
        commonInfoDao.updateById(commonInfo);
        return Result.success();
    }


    @Override
    public CommonInfo findOne(int id) {
        return commonInfoDao.findOne(id);
    }

    @Override
    public Result delete(long id) {
        commonInfoDao.deleteById(id);
        return Result.success();
    }


    @Override
    public Result updateBody(CommonInfo commonInfo) {
        commonInfoDao.updateById(commonInfo);
        return Result.success();
    }

    @Override
    public Result findByType(Integer type) {
        CommonInfo one = commonInfoDao.findOne(type);
        if("818".equals(String.valueOf(type))){
            if(StringUtils.isNotEmpty(one.getValue())){
                List<SelfCoupon> couponList=new ArrayList<>();
                for (String id:one.getValue().split(",")){
                    SelfCoupon selfCoupon = couponJpaRepository.findById(Long.parseLong(id)).orElse(null);
                    if(selfCoupon!=null){
                        couponList.add(selfCoupon);
                    }
                }
                one.setCouponList(couponList);
            }
        }
        return Result.success().put("data",one);
    }

    @Override
    public Result findByTypeAndCondition(String condition) {
        List<CommonInfo> commonInfoList = commonInfoDao.findByCondition(condition);
        for (CommonInfo commonInfo:commonInfoList){
            if("818".equals(String.valueOf(commonInfo.getType()))){
                if(StringUtils.isNotEmpty(commonInfo.getValue())){
                    List<SelfCoupon> couponList=new ArrayList<>();
                    for (String id:commonInfo.getValue().split(",")){
                        SelfCoupon selfCoupon = couponJpaRepository.findById(Long.parseLong(id)).orElse(null);
                        if(selfCoupon!=null){
                            couponList.add(selfCoupon);
                        }
                    }
                    commonInfo.setCouponList(couponList);
                }
            }
        }
        return Result.success().put("data",commonInfoList);
    }


}