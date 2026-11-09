package com.pwyl.modules.taking.controller.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.annotation.Login;
import com.pwyl.modules.taking.entity.OrderTaking;
import com.pwyl.modules.taking.response.OrderTakingResponse;
import com.pwyl.modules.taking.service.OrderTakingRewardService;
import com.pwyl.modules.taking.service.OrderTakingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@AllArgsConstructor
@Api("app接单详情")
@RequestMapping("/app/orderTaking")
public class AppOrderTakingController {

    private OrderTakingService orderTakingService;
    private OrderTakingRewardService orderTakingRewardService;

    /**
     * 按照指定条件查询接单
     *
     * @param bannerid
     * @return
     */
    @CrossOrigin
    @Login
    @ApiOperation("按照指定条件查询接单")
    @GetMapping("/queryTaking")
    public Result queryTaking(@ApiParam("城市")String city,@ApiParam("精度") String longitude, @ApiParam("维度") String latitude, @RequestAttribute Long userId, @ApiParam("模糊") String like, @ApiParam("条件") String condition, @ApiParam("bannerid") Long bannerid, @ApiParam("推荐") Long isRecommend, @ApiParam("游戏id") Long id, @ApiParam("页") Long page, @ApiParam("条") Long limit, @ApiParam("性别") Long sex, @ApiParam("排序") String by,Integer classify) {
        Page<OrderTaking> iPage = new Page<>(page, limit);
        return orderTakingService.queryTaking(city,longitude, latitude, userId, like, condition, bannerid, isRecommend, id, iPage, sex, by,classify);
    }

    @CrossOrigin
    @ApiOperation("按照指定条件查询接单")
    @GetMapping("/queryTakings")
    public Result queryTakings(@ApiParam("城市")String city,@ApiParam("精度") String longitude, @ApiParam("维度") String latitude, Long userId, @ApiParam("模糊") String like, @ApiParam("条件") String condition, @ApiParam("bannerid") Long bannerid, @ApiParam("推荐") Long isRecommend, @ApiParam("游戏id") Long id, @ApiParam("页") Long page, @ApiParam("条") Long limit, @ApiParam("性别") Long sex, @ApiParam("排序") String by,Integer classify) {
        Page<OrderTaking> iPage = new Page<>(page, limit);
        return orderTakingService.queryTaking(city,longitude, latitude, userId, like, condition, bannerid, isRecommend, id, iPage, sex, by,classify);
    }



    /**
     * 查询低价系列的接单
     */
    @ApiOperation("查询低价系列的接单")
    @GetMapping("/queryLowTaking")
    public Result queryLowTaking(Integer page, Integer limit,@ApiParam("精度") String longitude, @ApiParam("维度") String latitude) {
        return orderTakingService.queryLowTaking(page,limit,longitude,latitude);
    }

    /**
     * 查询接单详情
     */
    @ApiOperation("查询接单详情")
    @CrossOrigin
    @GetMapping("/queryTakingDetails")
    @Login
    public Result queryTakingDetails(@RequestParam Long id, @RequestAttribute Long userId,String longitude,String latitude) {
        return orderTakingService.queryTakingDetails(id, userId,longitude,latitude);
    }

    @ApiOperation("查询接单详情(不需要token)")
    @CrossOrigin
    @GetMapping("/queryTakingDetailss")
    public Result queryTakingDetailss(@RequestParam Long id, String longitude,String latitude) {
        return orderTakingService.queryTakingDetails(id, null,longitude,latitude);
    }

    /**
     * 发布接单
     */
    @ApiOperation("发布接单")
    @GetMapping("/insertOrderTaking")
    @Login
    public Result insertOrderTaking(OrderTaking orderTaking,@RequestAttribute Long userId) {
        orderTaking.setUserId(userId);
        return orderTakingService.insertOrderTaking(orderTaking);
    }

    /**
     * 查看我的发布
     */
    @Login
    @ApiOperation("查看我的发布")
    @GetMapping("/selectMyRelease")
    public Result selectMyRelease(@RequestAttribute Long userId, Long page, Long limit, @ApiParam("状态") String status) {
        return orderTakingService.selectMyRelease(userId, page, limit, status);
    }


    @ApiOperation("查看其他用户发布")
    @GetMapping("/selectUserOrderTaking")
    public Result selectUserOrderTaking(Long userId, Long page, Long limit) {
        return orderTakingService.selectMyRelease(userId, page, limit, "0");
    }

    /**
     * 修改发布状态
     */
    @Login
    @ApiOperation("修改发布状态")
    @GetMapping("/updateTakingStatus")
    public Result updateTakingStatus(Long id, @ApiParam("状态") Integer status,String content) {
        return orderTakingService.updateTakingStatus(id, status,content);
    }

    /**
     * 删除发布接单
     */
    @ApiOperation("删除发布接单")
    @GetMapping("/deleteTaking")
    public Result deleteTaking(Long id) {

        return orderTakingService.deleteTaking(id);
    }

    /**
     * 查询接单想详情
     */
    @ApiOperation("查询接单详情")
    @GetMapping("/queryTakingOrder")
    public Result queryTakingOrder(Long id,Long userId) {

        return orderTakingService.queryTakingOrder(id,userId);
    }

    /**
     * 重新编辑
     */
    @ApiOperation("重新编辑")
    @GetMapping("/updateTakingOrder")
    @Login
    public Result updateTakingOrder(OrderTaking orderTaking,@RequestAttribute Long userId) {
        orderTaking.setUserId(userId);
        return orderTakingService.updateTakingOrder(orderTaking);
    }



    /**
     * 查看我的接单
     */
    @ApiOperation("查看我的接单")
    @GetMapping("/queryMyTakingOrder")
    @Login
    public Result queryMyTakingOrder(@RequestAttribute Long userId, Long page, Long limit,Long status) {
        return orderTakingService.queryMyTakingOrder(userId,page,limit,status);
    }


    @ApiOperation("打赏")
    @PostMapping("/rewardMoney")
    @Login
    public Result rewardMoney(@RequestAttribute Long userId,BigDecimal money,Long orderTakingId){
        return orderTakingRewardService.rewardMoney(userId,money,orderTakingId);
    }

    @ApiOperation("查询打赏列表")
    @PostMapping("/selectOrderTakingRewardList")
    @Login
    public Result selectOrderTakingRewardList(Integer page,Integer limit,Long userId,Long orderTakingId){
        return orderTakingRewardService.selectOrderTakingRewardList(page, limit, userId, orderTakingId);
    }




}
