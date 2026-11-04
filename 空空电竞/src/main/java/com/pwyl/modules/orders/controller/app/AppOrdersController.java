package com.pwyl.modules.orders.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.annotation.Login;
import com.pwyl.modules.app.annotation.LoginUser;
import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.orders.entity.Orders;
import com.pwyl.modules.orders.response.MyOrderResponse;
import com.pwyl.modules.orders.service.OrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/app/orders")
@Api(value = "app订单信息", tags = {"app订单信息"})
public class AppOrdersController {

    private OrdersService ordersService;

    @Login
    @ApiOperation("生成订单")
    @GetMapping("/generateOrder")
    public Result generateOrder(@RequestAttribute Long userId, @ApiParam("接单/会员id") Long id, @ApiParam("订单类型") Long type, @ApiParam("订单数量") Long orderNumber, @ApiParam("备注") String remarks,Long couponId,Long fastOrderId,Long fastOrderTakeId) {
        return ordersService.generateOrder(userId, id, type, orderNumber, remarks,couponId,fastOrderId,fastOrderTakeId);
    }

    /**
     * 查看我的订单
     */
    @Login
    @ApiOperation("查看我的订单")
    @GetMapping("/selectMyOrder")
    public Result selectMyOrder(@RequestAttribute Long userId, Long page, Long limit, String status) {
        if (page == null || limit == null) {
            return Result.error("分页的条件为空");
        } else {
            IPage<MyOrderResponse> iPage = new Page(page, limit);
            return ordersService.selectMyOrder(userId, iPage, status);
        }
    }

    @Login
    @ApiOperation("查询订单不同状态的数量")
    @GetMapping("/selectMyOrdersCount")
    public Result selectMyOrdersCount(@RequestAttribute Long userId){
        return ordersService.selectMyOrdersCount(userId);
    }



    /**
     * 生成充值订单
     */
    @Login
    @ApiOperation("生成充值订单")
    @GetMapping("/investOrder")
    public Result investOrder(@RequestAttribute Long userId, @RequestParam Double money, @RequestParam Integer classify) {

        return ordersService.investOrder(userId, money, classify);
    }

    /**
     * 修改订单状态
     */
    @Login
    @ApiOperation("修改订单状态")
    @GetMapping("/cancelOrder")
    public Result cancelOrder(Long id, String status) {
        return ordersService.cancelOrder(id, status);
    }

    /**
     * 假删除订单
     */
    @Login
    @ApiOperation("假删除订单")
    @GetMapping("/deleteOrder")
    public Result deleteOrder(Long id) {

        return ordersService.deleteOrder(id);
    }

    /**
     * 查看订单详情
     */
    @Login
    @ApiOperation("查看订单详情")
    @GetMapping("/queryOrders")
    public Result queryOrders(Long id) {
        return ordersService.queryOrders(id);
    }


    @Login
    @GetMapping("/selectMyTakeOrders")
    @ApiOperation("查询我的接单")
    public Result selectMyTakeOrders(Integer page,Integer limit,@RequestAttribute Long userId,Integer status){
        return ordersService.selectMyTakeOrders(page,limit,userId,status);
    }

    @Login
    @PostMapping("/payMoney")
    @ApiOperation("零钱去支付")
    public Result payMoney(Long ordersId){
        return ordersService.payMoney(ordersId);
    }


    @Login
    @GetMapping("/selectTeamOrdersList")
    @ApiOperation("获取团队订单")
    public Result selectTeamOrdersList(@RequestAttribute Long userId, Integer page, Integer limit, Integer type, Integer status){
        return ordersService.selectTeamOrdersList(page,limit,userId,type,status);
    }

    @Login
    @GetMapping("/selectTeamUserList")
    @ApiOperation("获取团队列表")
    public Result selectTeamUserList(@LoginUser UserEntity user, Integer page, Integer limit, Integer type){
        return ordersService.selectTeamUserList(page,limit,user.getInvitationCode(),type,user.getUserId());
    }

    @Login
    @GetMapping("/selectTeamStatistics")
    @ApiOperation("团队统计")
    public Result selectTeamStatistics(@LoginUser UserEntity user){
        Double oneTeamMoney = ordersService.selectOrdersMoneyCountByUserId(user.getUserId(), 1);
        Integer oneTeamCount = ordersService.selectUserCountByInvitationCode(user.getInvitationCode(), 1);
        Double twoTeamMoney = ordersService.selectOrdersMoneyCountByUserId(user.getUserId(), 2);
        Integer twoTeamCount = ordersService.selectUserCountByInvitationCode(user.getInvitationCode(), 2);
        Integer teamCount=oneTeamCount+twoTeamCount;
        BigDecimal teamMoney=BigDecimal.valueOf(oneTeamMoney).add(BigDecimal.valueOf(twoTeamMoney));
        Map<String,Object> result=new HashMap<>();
        result.put("teamMoney",teamMoney);
        result.put("teamCount",teamCount);
        result.put("oneTeamMoney",oneTeamMoney);
        result.put("oneTeamCount",oneTeamCount);
        result.put("twoTeamMoney",twoTeamMoney);
        result.put("twoTeamCount",twoTeamCount);
        return Result.success().put("data",result);
    }

    @GetMapping("/selectTakingUserOrdersList")
    @ApiOperation("陪玩收益排行榜")
    public Result selectTakingUserList(Integer page,Integer limit,String userName,String phone){
        return Result.success().put("data",ordersService.selectTakingUserList(page, limit, userName, phone));
    }

    @GetMapping("/selectUserMoneyOrdersList")
    @ApiOperation("用户支付排行榜")
    public Result selectUserMoneyOrdersList(Integer page,Integer limit,String userName,String phone){
        return Result.success().put("data",ordersService.selectUserMoneyOrdersList(page, limit, userName, phone));
    }

    @GetMapping("/selectGiveRewardList")
    @ApiOperation("被打赏人排行榜")
    public Result selectGiveRewardList(Integer page,Integer limit,String userName,String phone){
        return Result.success().put("data",ordersService.selectGiveRewardList(page, limit, userName, phone));
    }

}
