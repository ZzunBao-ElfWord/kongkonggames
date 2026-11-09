package com.pwyl.modules.taking.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwyl.modules.taking.entity.Game;
import com.pwyl.modules.taking.entity.OrderTakingReward;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface OrderTakingRewardDao extends BaseMapper<OrderTakingReward> {

    IPage<Map<String,Object>> selectOrderTakingRewardList(Page<Map<String,Object>> page,@Param("userId") Long userId,@Param("orderTakingId") Long orderTakingId);


}
