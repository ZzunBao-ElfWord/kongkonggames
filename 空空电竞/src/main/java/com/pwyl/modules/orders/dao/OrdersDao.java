package com.pwyl.modules.orders.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.orders.entity.Orders;
import com.pwyl.modules.orders.response.MyOrderResponse;
import com.pwyl.modules.orders.response.OrderAllResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrdersDao extends BaseMapper<Orders> {

    IPage<MyOrderResponse> selectMyOrder(IPage iPage, @Param("userId") Long userId, @Param("status") String status);

    /**
     * 查看所有订单
     */
    IPage<OrderAllResponse> queryOrdersAll(Page<Orders> iPage, @Param("type") Long type,@Param("orderName") String orderName,  @Param("name") String name, @Param("status") Long status,@Param("userId") Long userId,@Param("orderType") Integer orderType,@Param("ordersType") Integer ordersType,@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("ordersNo") String ordersNo);

    List<OrderAllResponse> queryOrdersAllExcel(@Param("type") Long type, @Param("orderName") String orderName,  @Param("name") String name, @Param("status") Long status, @Param("userId") Long userId, @Param("orderType") Integer orderType,@Param("ordersType") Integer ordersType,@Param("startTime") String startTime, @Param("endTime") String endTime,@Param("ordersNo") String ordersNo);

    int selectOrdersCountByUserId(@Param("userId") Long userId);

    IPage<Map<String,Object>> selectMyTakeOrders(Page<Map<String,Object>> page,@Param("userId") Long userId,@Param("status") Integer status);

    IPage<Map<String,Object>> selectTeamOrdersList(Page<Map<String,Object>> page,@Param("userId") Long userId,@Param("type") Integer type,@Param("status") Integer status);

    IPage<Map<String,Object>> selectTeamUserList(Page<Map<String,Object>> page,@Param("invitationCode") String invitationCode,@Param("type") Integer type,@Param("userId") Long userId);

    int selectMyOrdersCount(@Param("userId") Long userId,@Param("time") String time);

    int selectTakeOrdersCount(@Param("userId") Long userId,@Param("time") String time);

    Integer countOrdersByCreateTime(@Param("time")String time,@Param("flag")Integer flag,@Param("status")Integer status,@Param("orderType") Integer orderType);

    Double sumOrdersByCreateTime(@Param("time")String time,@Param("flag")Integer flag,@Param("status")Integer status,@Param("orderType") Integer orderType);

    IPage<Map<String,Object>> incomeAnalysisOrders(Page<Map<String,Object>> page,@Param("time")String time,@Param("flag")Integer flag);

    Double selectOrdersMoneyCountByUserId(Long userId,Integer type);

    Integer selectUserCountByInvitationCode(String invitationCode,Integer type);

    IPage<Map<String,Object>> selectTeamUserOrderMoneyByTime(Page<Map<String,Object>> page,@Param("time")String time,@Param("flag")Integer flag);

    IPage<Map<String,Object>> selectConsortiaUserOrderMoneyByTime(Page<Map<String,Object>> page,@Param("time")String time,@Param("flag")Integer flag);

    IPage<Map<String,Object>> selectTakingUserOrderMoneyByTime(Page<Map<String,Object>> page,@Param("time")String time,@Param("flag")Integer flag);

    IPage<Map<String,Object>> selectGiftUserOrderMoneyByTime(Page<Map<String,Object>> page,@Param("time")String time,@Param("flag")Integer flag);

    IPage<Map<String,Object>> selectUserMoneyByTime(Page<Map<String,Object>> page,@Param("time")String time,@Param("flag")Integer flag);

    IPage<Map<String,Object>> selectPayMoneyByTime(Page<Map<String,Object>> page,@Param("time")String time,@Param("flag")Integer flag);

    IPage<Map<String,Object>> selectMoneyByTime(Page<Map<String,Object>> page,@Param("time")String time,@Param("flag")Integer flag);

    IPage<Map<String,Object>> selectPayOrdersMoneyByTime(Page<Map<String,Object>> page,@Param("time")String time,@Param("flag")Integer flag);

    IPage<Map<String,Object>> selectTakingUserList(Page<Map<String,Object>> page,@Param("userName")String userName,@Param("phone")String phone);

    IPage<Map<String,Object>> selectUserMoneyOrdersList(Page<Map<String,Object>> page,@Param("userName")String userName,@Param("phone")String phone);

    IPage<Map<String,Object>> selectGiveRewardList(Page<Map<String,Object>> page,@Param("userName")String userName,@Param("phone")String phone);

    Integer selectOrdersCountByState(Integer state,Integer orderType,Integer flag,String time);

    BigDecimal selectOrdersMoneyByTime(Integer type, Integer flag, String time);


}
