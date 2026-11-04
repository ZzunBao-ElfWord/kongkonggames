package com.pwyl.modules.taking.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.taking.entity.OrderTaking;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

public interface OrderTakingService extends IService<OrderTaking> {
    /**
     *
     */
    Result queryTaking(String city, String longitude, String latitude, Long userId, String like, String condition, Long bannerid, Long isRecommend, Long id, Page<OrderTaking> iPage, Long sex, String by, Integer classify);

    /**
     * 查询乐享低价系列
     *
     * @return
     */
    Result queryLowTaking(Integer page,Integer limit,String longitude,String latitude);


    /**
     * 接单详情
     */
    Result queryTakingDetails(Long id, Long userId,String longitude,String latitude);

    /**
     * 发布接单
     */
    Result insertOrderTaking(OrderTaking orderTaking);

    /**
     * 查看我的发布
     *
     * @return
     */
    Result selectMyRelease(Long userId, Long page, Long limit, String status);

    /**
     * 删除接单
     */
    Result deleteOrderTaking(Long id);

    /**
     * 查询所有接单
     */
    Result queryAllOrderTaking(Integer page,Integer limit, Long gameId, Long status, String userName, Long userId,Integer classify,String longitude,String latitude);

    /**
     * 审核接单
     */
    Result auditorOrderTaking(Long id, Integer status,String content);

    /**
     * 修改发布订单
     */
    Result updateTakingStatus(Long id, Integer status,String content);

    /**
     * 删除发布接单
     */
    Result deleteTaking(Long id);

    /**
     * 查询接单详情
     */
    Result queryTakingOrder(Long id,Long userId);

    /**
     * 修改接单详情
     */
    Result updateTakingOrder(OrderTaking orderTaking);

    Result updateTakingOrders(OrderTaking orderTaking);

    /**
     * 查看我的接单
     */
    Result queryMyTakingOrder(Long userId,Long page, Long limit,Long status);
}
