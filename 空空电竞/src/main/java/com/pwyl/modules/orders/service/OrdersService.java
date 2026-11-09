package com.pwyl.modules.orders.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pwyl.common.utils.PageUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.message.entity.MessageInfo;
import com.pwyl.modules.orders.entity.Orders;
import com.pwyl.modules.utils.excel.ExcelData;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;


public interface OrdersService extends IService<Orders> {

    int selectMyOrdersCount(Long userId, String time);

    int selectTakeOrdersCount( Long userId, String time);




    /**
     * 生成订单
     *
     * @param userId
     * @param id
     * @param type
     * @param orderNumber
     * @param remarks
     * @return
     */
    Result generateOrder(Long userId, Long id, Long type, Long orderNumber, String remarks,Long couponId,Long fastOrderId,Long fastOrderTakeId);

    /**
     * 查看我的订单
     *
     * @param userId
     * @return
     */
    Result selectMyOrder(Long userId, IPage iPage, String status);

    Result selectMyOrdersCount(Long userId);

    Result selectTeamOrdersList(Integer page, Integer limit, Long userId,Integer type, Integer status);

    Result selectTeamUserList(Integer page, Integer limit, String invitationCode,Integer type,Long userId);


    Double selectOrdersMoneyCountByUserId(Long userId,Integer type);

    Integer selectUserCountByInvitationCode(String invitationCode,Integer type);

    /**
     * 生成充值订单
     *
     * @param userId
     * @param money
     * @return
     */
    Result investOrder(Long userId, Double money, Integer classify);

    /**
     * 取消订单
     *
     * @param id
     * @param status
     * @return
     */
    Result cancelOrder(Long id, String status);

    /**
     * 删除订单
     *
     * @param id
     * @param
     * @return
     */
    Result deleteOrder(Long id);

    /**
     * \
     * 查询订单详情
     *
     * @param id
     * @return
     */
    Result queryOrders(Long id);

    Result insertOrders(Orders orders);

    /**
     * 查看所有订单
     */
    Result queryOrdersAll(Long page, Long limit,Long type,String orderName, String name, Long status,Long userId,Integer orderType,Integer ordersType,String startTime,String endTime,String ordersNo);

    ExcelData ordersListExcel(Long type,String orderName, String name, Long status, Long userId, Integer orderType,Integer ordersType, String startTime, String endTime,String ordersNo);

    Result selectMyTakeOrders(Integer page,Integer limit,Long userId,Integer status);

    Result payMoney(Long ordersId);

    PageUtils incomeAnalysisOrders(Integer page, Integer limit, String time, Integer flag);

    PageUtils selectTakingUserList(Integer page, Integer limit, String userName, String phone);

    PageUtils selectUserMoneyOrdersList(Integer page, Integer limit, String userName, String phone);

    Integer selectOrdersCountByState(Integer state,Integer orderType,Integer flag,String time);

    PageUtils selectGiveRewardList(Integer page, Integer limit, String userName, String phone);

    BigDecimal selectOrdersMoneyByTime(Integer type, Integer flag, String time);


}
