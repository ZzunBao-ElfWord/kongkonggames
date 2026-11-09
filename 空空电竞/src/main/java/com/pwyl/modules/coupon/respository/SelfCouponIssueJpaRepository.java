package com.pwyl.modules.coupon.respository;


import com.pwyl.modules.coupon.entity.SelfCouponIssue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelfCouponIssueJpaRepository extends JpaRepository<SelfCouponIssue, Long> {

    //分页查询
    Page<SelfCouponIssue> findAll(Pageable pageable);

    //条件查询
    Page<SelfCouponIssue> findAll(Specification<SelfCouponIssue> specification, Pageable pageable);

    //领券列表
    List<SelfCouponIssue> findAll(Specification<SelfCouponIssue> specification);

}
