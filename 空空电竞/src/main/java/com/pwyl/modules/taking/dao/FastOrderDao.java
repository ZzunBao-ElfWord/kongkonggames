package com.pwyl.modules.taking.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwyl.modules.taking.entity.FastOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FastOrderDao extends BaseMapper<FastOrder> {

    IPage<FastOrder> selectFastOrderPage(Page<FastOrder> page,Long userId,Integer status,String userName,String phone,Integer templateType);

    IPage<FastOrder> selectFastOrderTake(Page<FastOrder> page,Long userId,Integer templateType);

    IPage<FastOrder> selectFastOrderTakeListByUserId(Page<FastOrder> page,Long userId,Integer status);

    int selectFastOrderCountByUserIdAndTime(Long userId);

    List<FastOrder> selectOverTimeFastOrder();

}
