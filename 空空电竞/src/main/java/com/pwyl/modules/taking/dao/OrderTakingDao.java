package com.pwyl.modules.taking.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwyl.modules.taking.entity.OrderTaking;
import com.pwyl.modules.taking.response.OrderTakingResponse;
import com.pwyl.modules.taking.response.TakingDetailsResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderTakingDao extends BaseMapper<OrderTaking> {

    IPage<OrderTaking> queryTaking(Page<OrderTaking> page,@Param("city") String city, @Param("longitude") String longitude, @Param("latitude") String latitude, @Param("like") String like, @Param("condition") Integer condition, @Param("bannerid") Long bannerid, @Param("isRecommend") Long isRecommend, @Param("id") Long id, @Param("sex") Long sex, @Param("by") String by,Integer classify);

    List<OrderTaking> queryLowTaking(String longitude,String latitude);

    IPage<OrderTaking> queryLowTakings(Page<OrderTaking> pages,String longitude,String latitude);

    OrderTaking queryTakingDetails(@Param("id") Long id,String longitude,String latitude);

    IPage<OrderTaking> selectMyRelease(Page<OrderTaking> page, @Param("userId") Long userId, @Param("status") String status);

    IPage<OrderTaking> queryAllOrderTaking(Page<OrderTaking> page, @Param("gameId") Long gameId, @Param("status") Long status, @Param("userName") String userName, @Param("userId") Long userId,@Param("classify") Integer classify,String longitude,String latitude);

    Integer countGoodsByCreateTime(@Param("time")String time,@Param("flag")Integer flag);

    Double sumGoodsByCreateTime(@Param("time")String time,@Param("flag")Integer flag);

    int updateTakingStatusByUserId(@Param("userId") Long userId);


}
