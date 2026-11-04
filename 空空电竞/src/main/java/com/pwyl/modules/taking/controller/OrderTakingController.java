package com.pwyl.modules.taking.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.annotation.Login;
import com.pwyl.modules.gift.dao.GiftUserDao;
import com.pwyl.modules.orders.dao.OrdersDao;
import com.pwyl.modules.orders.service.OrdersService;
import com.pwyl.modules.taking.dao.OrderTakingDao;
import com.pwyl.modules.taking.entity.OrderTaking;
import com.pwyl.modules.taking.response.OrderTakingResponse;
import com.pwyl.modules.taking.service.OrderTakingRewardService;
import com.pwyl.modules.taking.service.OrderTakingService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/orderTaking")
public class OrderTakingController {

    @Autowired
    private OrderTakingService orderTakingService;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private OrderTakingDao orderTakingDao;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrderTakingRewardService orderTakingRewardService;
    @Autowired
    private GiftUserDao giftUserDao;


    /**
     * 发布接单
     */
    @ApiOperation("发布接单")
    @GetMapping("/insertOrderTaking")
    public Result insertOrderTaking(OrderTaking orderTaking) {
        return orderTakingService.insertOrderTaking(orderTaking);
    }

    /**
     * 删除订单
     */
    @ApiOperation("删除接单")
    @GetMapping("/deleteOrderTaking")
    public Result deleteOrderTaking(Long id) {
        return orderTakingService.deleteOrderTaking(id);
    }

    /**
     * 查询所有接单
     */
    @ApiOperation("查询所有接单")
    @GetMapping("/queryAllOrderTaking")
    public Result queryAllOrderTaking(@ApiParam("页") Integer page, @ApiParam("条") Integer limit, Long gameId, Long status, String userName,Long userId,Integer classify,String longitude,String latitude) {
        return orderTakingService.queryAllOrderTaking(page,limit, gameId, status, userName,userId,classify,longitude,latitude);
    }

    /**
     * 审核接单
     */
    @ApiOperation("审核接单")
    @GetMapping("/auditorOrderTaking")
    public Result auditorOrderTaking(Long id, Integer status,String content) {
        return orderTakingService.auditorOrderTaking(id, status,content);
    }

    /**
     * 修改
     */
    @ApiOperation("修改接单")
    @GetMapping("/updateTakingOrder")
    public Result updateTakingOrder(OrderTaking orderTaking) {
        return orderTakingService.updateTakingOrder(orderTaking);
    }

    @ApiOperation("修改接单")
    @GetMapping("/updateTakingOrders")
    public Result updateTakingOrders(OrderTaking orderTaking) {
        return orderTakingService.updateTakingOrders(orderTaking);
    }

    @PostMapping("/updateOrderTakingStatus/{id}")
    @ApiOperation("修改状态")
    public Result updateOrderTakingStatus(@PathVariable Long id){
        OrderTaking byId = orderTakingService.getById(id);
        if(byId.getStatus()==0){
            byId.setStatus(2);
        }else{
            byId.setStatus(0);
        }
        orderTakingService.updateById(byId);
        return Result.success();
    }

    @PostMapping("/updateOrderTakingRecommend/{id}")
    @ApiOperation("修改状态")
    public Result updateOrderTakingRecommend(@PathVariable Long id){
        OrderTaking byId = orderTakingService.getById(id);
        if("0".equals(byId.getIsRecommend())){
            byId.setIsRecommend("1");
        }else{
            byId.setIsRecommend("0");
        }
        orderTakingService.updateById(byId);
        return Result.success();
    }

    @ApiOperation("订单统计")
    @GetMapping("/taskAnalysis")
    public Result taskAnalysis(String time,Integer flag){
        /**
         * 总金额 多少笔 待服务 服务中 已完成 已退款
         * 服务订单 约玩订单统计多少笔
         */
        //服务订单  总金额  数量  待服务 服务中  已完成  已退款
        //约玩订单  总金额  数量  待服务 服务中  已完成  已退款
        //status 1待服务2已完成3已退款4进行中
        //orderType 1服务 2约玩
        Double serviceMoney = ordersDao.sumOrdersByCreateTime(time, flag,0,1);
        Integer serviceCount = ordersDao.countOrdersByCreateTime(time, flag,0,1);
        Integer serviceDaiFuWuCount = ordersDao.countOrdersByCreateTime(time, flag,1,1);
        Integer serviceYiWanChengCount = ordersDao.countOrdersByCreateTime(time, flag,2,1);
        Integer serviceYiTuiKuanCount = ordersDao.countOrdersByCreateTime(time, flag,3,1);
        Integer serviceJinXingZhongCount = ordersDao.countOrdersByCreateTime(time, flag,4,1);

        Double fastMoney = ordersDao.sumOrdersByCreateTime(time, flag,0,2);
        Integer fastCount = ordersDao.countOrdersByCreateTime(time, flag,0,2);
        Integer fastDaiFuWuCount = ordersDao.countOrdersByCreateTime(time, flag,1,2);
        Integer fastYiWanChengCount = ordersDao.countOrdersByCreateTime(time, flag,2,2);
        Integer fastYiTuiKuanCount = ordersDao.countOrdersByCreateTime(time, flag,3,2);
        Integer fastJinXingZhongCount = ordersDao.countOrdersByCreateTime(time, flag,4,2);

        //打赏统计
        Double giftMoney = giftUserDao.selectSumMoneyByTime(time, flag);


        Map<String,Object> map=new HashMap<>();
        map.put("serviceMoney",serviceMoney);map.put("serviceCount",serviceCount);
        map.put("serviceDaiFuWuCount",serviceDaiFuWuCount);map.put("serviceYiWanChengCount",serviceYiWanChengCount);
        map.put("serviceYiTuiKuanCount",serviceYiTuiKuanCount);map.put("serviceJinXingZhongCount",serviceJinXingZhongCount);
        map.put("fastMoney",fastMoney);map.put("fastCount",fastCount);
        map.put("fastDaiFuWuCount",fastDaiFuWuCount);map.put("fastYiWanChengCount",fastYiWanChengCount);
        map.put("fastYiTuiKuanCount",fastYiTuiKuanCount);map.put("fastJinXingZhongCount",fastJinXingZhongCount);
        map.put("giftMoney",giftMoney);
        return Result.success().put("data",map);
    }

    @ApiOperation("接单收入分析")
    @GetMapping("/incomeAnalysis")
    public Result incomeAnalysis(String time,Integer flag,int page,int limit){
        return Result.success().put("data",ordersService.incomeAnalysisOrders(page,limit,time,flag));
    }

    @ApiOperation("查询打赏列表")
    @PostMapping("/selectOrderTakingRewardList")
    public Result selectOrderTakingRewardList(Integer page,Integer limit,Long userId,Long orderTakingId){
        return orderTakingRewardService.selectOrderTakingRewardList(page, limit, userId, orderTakingId);
    }


}
