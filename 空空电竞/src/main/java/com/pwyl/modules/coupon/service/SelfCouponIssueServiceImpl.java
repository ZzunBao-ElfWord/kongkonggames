package com.pwyl.modules.coupon.service;

import com.pwyl.modules.chats.utils.DateUtil;
import com.pwyl.modules.coupon.utils.Result;
import com.pwyl.modules.coupon.utils.ResultUtil;
import com.pwyl.modules.coupon.entity.SelfCoupon;
import com.pwyl.modules.coupon.entity.SelfCouponIssue;
import com.pwyl.modules.coupon.entity.SelfCouponUser;
import com.pwyl.modules.coupon.respository.SelfCouponIssueJpaRepository;
import com.pwyl.modules.coupon.respository.SelfCouponJpaRepository;
import com.pwyl.modules.coupon.respository.SelfCouponUserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class SelfCouponIssueServiceImpl implements SelfCouponIssueService {
    @Autowired
    private SelfCouponIssueJpaRepository jpaRepository;
    @Autowired
    private SelfCouponUserJpaRepository couponUserJpaRepository;
    @Autowired
    private SelfCouponJpaRepository couponJpaRepository;

    @Override
    public Result findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResultUtil.success(jpaRepository.findAll(pageable));
    }

    /**
     * 用户可领取的优惠券列表
     * @param goodsId
     * @param userId
     * @return
     */
    @Override
    public Result useList(Long goodsId, Long userId) {
        Specification<SelfCouponIssue> queryCondition = new Specification<SelfCouponIssue>() {
            @Override
            public Predicate toPredicate(Root<SelfCouponIssue> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (goodsId != null) {
                    predicateList.add(criteriaBuilder.like(root.get("goodsIds"), "%"+goodsId+"%"));
                }
                String nowTime = DateUtil.createTime();
                predicateList.add(criteriaBuilder.lessThan(root.get("startTime"), nowTime));
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("endTime"), nowTime));
                predicateList.add(criteriaBuilder.equal(root.get("status"), 1));
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        List<SelfCouponIssue> list = jpaRepository.findAll(queryCondition);
        List<SelfCouponUser> couponUserList = couponUserJpaRepository.findAllByUserId(userId); //用户领券信息
        List<SelfCoupon> couponList = couponJpaRepository.findAll(); //优惠券信息：金额
        //筛选可领取的券
        for (SelfCouponIssue c : list) {
            //1.剩余数量
            if (c.getRemainNumber() == 0){ //剩余数量为空
                list.remove(c);
                break;
            }
            for (SelfCoupon s : couponList) {
                if (c.getCouponId().equals(s.getCouponId())){
                    c.setCoupon(s);
                    break;
                }
            }
            //2.领取限制
            c.setGetStatus(1); //领取状态（1可领取 2已领取）
            for (SelfCouponUser u : couponUserList) {
                if (u.getCouponIssueId()!=null){
                    if (u.getCouponIssueId().equals(c.getCouponIssueId()) && c.getIsLimit() == 1){ //判断是否已领取且券为限领
                        c.setGetStatus(2);
                        break;
                    }
                }
            }
        }
        return ResultUtil.success(list);
    }

    @Override
    public Result saveBody(SelfCouponIssue entity) {
        entity.setCreateTime(DateUtil.createTime());
        return ResultUtil.success(jpaRepository.save(entity));
    }

    @Override
    public Result updateBody(SelfCouponIssue entity) {
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
}
