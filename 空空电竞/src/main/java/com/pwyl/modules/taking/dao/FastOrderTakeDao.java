package com.pwyl.modules.taking.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwyl.modules.taking.entity.FastOrder;
import com.pwyl.modules.taking.entity.FastOrderTake;
import com.pwyl.modules.taking.entity.OrderTaking;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FastOrderTakeDao extends BaseMapper<FastOrderTake> {

    List<OrderTaking> selectFastOrderTakeList(FastOrder fastOrder);

    IPage<OrderTaking> selectFastOrderTakePage(Page<OrderTaking> page,FastOrder fastOrder);

    int updateFastOrderTake(Long fastOrderId);
}
