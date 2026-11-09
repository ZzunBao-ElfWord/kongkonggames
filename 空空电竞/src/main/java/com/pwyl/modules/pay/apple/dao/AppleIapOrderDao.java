package com.pwyl.modules.pay.apple.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwyl.modules.pay.apple.entity.AppleIapOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AppleIapOrderDao extends BaseMapper<AppleIapOrder> {
    /**
     * 以 transaction_id 唯一索引做幂等预占，并发下只有一条插入成功。
     */
    int insertIgnore(AppleIapOrder order);
}
