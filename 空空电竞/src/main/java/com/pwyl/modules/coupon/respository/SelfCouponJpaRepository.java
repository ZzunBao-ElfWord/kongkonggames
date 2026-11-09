package com.pwyl.modules.coupon.respository;


import com.pwyl.modules.coupon.entity.SelfCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelfCouponJpaRepository extends JpaRepository<SelfCoupon, Long> {

    //分页查询
    Page<SelfCoupon> findAll(Pageable pageable);

    //条件查询
    Page<SelfCoupon> findAll(Specification<SelfCoupon> specification, Pageable pageable);

}
