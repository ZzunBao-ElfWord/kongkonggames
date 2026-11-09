package com.pwyl.modules.coupon.service;

import com.aliyuncs.CommonRequest;
import com.pwyl.modules.app.annotation.LoginUser;
import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.app.service.UserService;
import com.pwyl.modules.common.service.CommonInfoService;
import com.pwyl.modules.coupon.utils.DateUtil;
import com.pwyl.modules.coupon.utils.DateUtils;
import com.pwyl.modules.coupon.utils.Result;
import com.pwyl.modules.coupon.utils.ResultUtil;
import com.pwyl.modules.coupon.entity.SelfCoupon;
import com.pwyl.modules.coupon.entity.SelfCouponIssue;
import com.pwyl.modules.coupon.entity.SelfCouponUser;
import com.pwyl.modules.coupon.respository.SelfCouponIssueJpaRepository;
import com.pwyl.modules.coupon.respository.SelfCouponJpaRepository;
import com.pwyl.modules.coupon.respository.SelfCouponUserJpaRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SelfCouponUserServiceImpl implements SelfCouponUserService {
    @Autowired
    private SelfCouponUserJpaRepository jpaRepository;
    @Autowired
    private SelfCouponIssueJpaRepository couponIssueJpaRepository;
    @Autowired
    private SelfCouponJpaRepository couponJpaRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CommonInfoService commentRepository;

    @Override
    public Result getNewUserCoupon(UserEntity userEntity){
        String value = commentRepository.findOne(818).getValue();
        List<SelfCoupon> couponList=new ArrayList<>();
        for(String id:value.split(",")){
            Long couponId=Long.parseLong(id);
            SelfCoupon selfCoupon = couponJpaRepository.findById(couponId).orElse(null);
            if(selfCoupon!=null){
                SelfCouponUser selfCouponUser=new SelfCouponUser();
                selfCouponUser.setStatus(1);
                String time = DateUtil.createTime();
                selfCouponUser.setCreateTime(time);
                Integer days = selfCoupon.getValidDay();
                selfCouponUser.setFailureTime(DateUtil.addDays(time, days));
                selfCouponUser.setCouponId(selfCoupon.getCouponId());
                selfCouponUser.setCouponName(selfCoupon.getCouponName());
                selfCouponUser.setLessMoney(selfCoupon.getLessMoney());
                selfCouponUser.setMinMoney(selfCoupon.getMinMoney());
                selfCouponUser.setNickName(userEntity.getUserName());
                selfCouponUser.setType(selfCoupon.getType());
                selfCouponUser.setUserId(userEntity.getUserId());
                jpaRepository.save(selfCouponUser);
                couponList.add(selfCoupon);
            }
        }
        userEntity.setIsNewUser(2);
        userService.updateById(userEntity);
        return ResultUtil.success(couponList);
    }

    @Override
    public Result findAll(Integer page, Integer size, String couponName,String nickName) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<SelfCouponUser> queryCondition = new Specification<SelfCouponUser>() {
            @Override
            public Predicate toPredicate(Root<SelfCouponUser> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (StringUtils.isNotEmpty(couponName)) {
                    predicateList.add(criteriaBuilder.equal(root.get("couponName"), couponName));
                }
                if (StringUtils.isNotEmpty(nickName)) {
                    predicateList.add(criteriaBuilder.equal(root.get("nickName"), nickName));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        Page<SelfCouponUser> all = jpaRepository.findAll(queryCondition, pageable);
        return ResultUtil.success(all);
    }

    /**
     * 用户优惠券列表
     */
    @Override
    public Result userList(Integer page, Integer size, Long userId, String goodsId, Integer type, BigDecimal money) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(new Sort.Order(Sort.Direction.DESC, "createTime")));
        Specification<SelfCouponUser> queryCondition = new Specification<SelfCouponUser>() {
            @Override
            public Predicate toPredicate(Root<SelfCouponUser> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(criteriaBuilder.equal(root.get("userId"), userId));
                if (StringUtils.isNotEmpty(goodsId)) {
                    predicateList.add(criteriaBuilder.like(root.get("goodsIds"), "%"+goodsId+"%"));
                }
                if (type!=null && type != 0) {
                    predicateList.add(criteriaBuilder.equal(root.get("type"), type));
                }
                if(money!=null){
                    predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("minMoney"),money));
                }
                predicateList.add(criteriaBuilder.equal(root.get("status"), 1)); //未使用
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        Page<SelfCouponUser> all = jpaRepository.findAll(queryCondition, pageable);
        return ResultUtil.success(all);
    }

    /**
     * 用户优惠券使用列表
     */
    @Override
    public Result useList(Long userId, String goodsId) {
        Specification<SelfCouponUser> queryCondition = new Specification<SelfCouponUser>() {
            @Override
            public Predicate toPredicate(Root<SelfCouponUser> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(criteriaBuilder.equal(root.get("userId"), userId));
                if (StringUtils.isNotEmpty(goodsId)) {
                    predicateList.add(criteriaBuilder.like(root.get("goodsIds"), "%"+goodsId+"%"));
                }
                predicateList.add(criteriaBuilder.equal(root.get("status"), 1)); //未使用
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        List<SelfCouponUser> list = jpaRepository.findAll(queryCondition);
        return ResultUtil.success(list);
    }

    @Async
    @Override
    public Result sendUserCoupon(String userIds,String couponIds,Integer type) {
        if(type==1){
            for(String sCouponId:couponIds.split(",")){
                Long couponId=Long.parseLong(sCouponId);
                SelfCoupon selfCoupon = couponJpaRepository.findById(couponId).orElse(null);
                for(String sUserId:userIds.split(",")){
                    Long userId=Long.parseLong(sUserId);
                    SelfCouponUser selfCouponUser=new SelfCouponUser();
                    selfCouponUser.setStatus(1);
                    String time = DateUtil.createTime();
                    selfCouponUser.setCreateTime(time);
                    Integer days = selfCoupon.getValidDay();
                    selfCouponUser.setFailureTime(DateUtil.addDays(time, days));
                    selfCouponUser.setCouponId(selfCoupon.getCouponId());
                    selfCouponUser.setCouponName(selfCoupon.getCouponName());
                    selfCouponUser.setLessMoney(selfCoupon.getLessMoney());
                    selfCouponUser.setMinMoney(selfCoupon.getMinMoney());
                    UserEntity userEntity = userService.selectUserById(userId);
                    selfCouponUser.setNickName(userEntity.getUserName());
                    selfCouponUser.setType(selfCoupon.getType());
                    selfCouponUser.setUserId(userId);
                    jpaRepository.save(selfCouponUser);
                }
            }
        }else{
            send(couponIds);
        }
        return ResultUtil.success();
    }


    public void send(String couponIds){
        List<UserEntity> list = userService.list();
        for(UserEntity user:list){
            Long userId=user.getUserId();
            for(String sCouponId:couponIds.split(",")){
                Long couponId=Long.parseLong(sCouponId);
                SelfCoupon selfCoupon = couponJpaRepository.findById(couponId).orElse(null);
                SelfCouponUser selfCouponUser=new SelfCouponUser();
                selfCouponUser.setStatus(1);
                String time = DateUtil.createTime();
                selfCouponUser.setCreateTime(time);
                Integer days = selfCoupon.getValidDay();
                selfCouponUser.setFailureTime(DateUtil.addDays(time, days));
                selfCouponUser.setCouponId(selfCoupon.getCouponId());
                selfCouponUser.setCouponName(selfCoupon.getCouponName());
                selfCouponUser.setLessMoney(selfCoupon.getLessMoney());
                selfCouponUser.setMinMoney(selfCoupon.getMinMoney());
                UserEntity userEntity = userService.selectUserById(userId);
                selfCouponUser.setNickName(userEntity.getUserName());
                selfCouponUser.setType(selfCoupon.getType());
                selfCouponUser.setUserId(userId);
                jpaRepository.save(selfCouponUser);
            }
        }
    }

    /**
     * 领取优惠券
     * @param entity
     * @return
     */
    @Override
    public Result saveBody(SelfCouponUser entity) {
        SelfCouponIssue couponIssue = couponIssueJpaRepository.findById(entity.getCouponIssueId()).orElse(null);
        if (couponIssue.getRemainNumber() < 1){
            return ResultUtil.error(-1, "优惠券领取完了");
        }
        String time = DateUtil.createTime();
        entity.setCreateTime(time); //创建时间
        entity.setStatus(1); //状态（1未使用 2已使用 3已过期）
        //失效时间
        SelfCoupon coupon = couponJpaRepository.findById(entity.getCouponId()).orElse(null);
        Integer days = coupon.getValidDay();
        entity.setFailureTime(DateUtil.addDays(time, days));
        entity.setGoodsIds(coupon.getGoodsIds()); //商品id
        //优惠券剩余数量-1
        couponIssue.setRemainNumber(couponIssue.getRemainNumber()-1);
        return ResultUtil.success(jpaRepository.save(entity));
    }

    @Override
    public Result updateBody(SelfCouponUser entity) {
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
