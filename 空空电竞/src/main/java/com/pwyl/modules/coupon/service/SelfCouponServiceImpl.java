package com.pwyl.modules.coupon.service;

import com.pwyl.modules.chats.utils.DateUtil;
import com.pwyl.modules.coupon.respository.SelfCouponUserJpaRepository;
import com.pwyl.modules.coupon.utils.Result;
import com.pwyl.modules.coupon.utils.ResultUtil;
import com.pwyl.modules.coupon.entity.SelfCoupon;
import com.pwyl.modules.coupon.respository.SelfCouponJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SelfCouponServiceImpl implements SelfCouponService {
    @Autowired
    private SelfCouponJpaRepository jpaRepository;
    @Autowired
    private SelfCouponUserJpaRepository selfCouponUserJpaRepository;

    @Override
    public Result findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(new Sort.Order(Sort.Direction.DESC, "sort")));
        return ResultUtil.success(jpaRepository.findAll(pageable));
    }


    @Override
    public Result saveBody(SelfCoupon entity) {
        entity.setCreateTime(DateUtil.createTime());
        return ResultUtil.success(jpaRepository.save(entity));
    }

    @Override
    public Result updateBody(SelfCoupon entity) {
        return ResultUtil.success(jpaRepository.save(entity));
    }

    @Override
    public Result findOne(Long id) {
        return ResultUtil.success(jpaRepository.findById(id).orElse(null));
    }

    @Override
    public Result delete(Long id) {
        jpaRepository.deleteById(id);
        return ResultUtil.success();
    }

    @Scheduled(cron = "0 */1 * * * ?")
    public void updateEndCoupon(){
        selfCouponUserJpaRepository.updateEndCoupon();
    }

}
