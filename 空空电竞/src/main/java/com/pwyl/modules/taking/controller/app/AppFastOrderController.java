package com.pwyl.modules.taking.controller.app;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.annotation.Login;
import com.pwyl.modules.taking.entity.FastOrder;
import com.pwyl.modules.taking.entity.FastOrderTake;
import com.pwyl.modules.taking.service.FastOrderService;
import com.pwyl.modules.taking.service.FastOrderTakeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/fastOrder")
@Api("快速约玩")
public class AppFastOrderController {

    @Autowired
    private FastOrderService fastOrderService;
    @Autowired
    private FastOrderTakeService fastOrderTakeService;

    @Login
    @GetMapping("/selectMyFastCount")
    @ApiOperation("查询我今日发布的快速约玩数量")
    public Result selectMyFastCount(@RequestAttribute("userId") Long userId){
        return fastOrderService.selectMyFastCount(userId);
    }

    @Login
    @PostMapping("/insertFastOrder")
    @ApiOperation("发起快速约玩")
    public Result insertFastOrder(@RequestAttribute("userId") Long userId,@RequestBody FastOrder fastOrder){
        fastOrder.setUserId(userId);
        return fastOrderService.insertFastOrder(fastOrder);
    }

    @Login
    @GetMapping("/selectFastOrderDetails")
    @ApiOperation("查询快速约玩详情")
    public Result selectFastOrderDetails(Long fastOrderId){
        return fastOrderService.selectFastOrderDetails(fastOrderId);
    }


    @Login
    @GetMapping("/selectFastOrderPage")
    @ApiOperation("查询我发布的快速约玩")
    public Result selectFastOrderPage(Integer page,Integer limit,@RequestAttribute("userId") Long userId,Integer status,String userName,String phone,Integer templateType){
        return fastOrderService.selectFastOrderPage(page, limit, userId, status, userName, phone,templateType);
    }

    @Login
    @PostMapping("/cancelFastOrder")
    @ApiOperation("取消约玩")
    public Result cancelFastOrder(Long fastOrderId){
        return fastOrderService.cancelFastOrder(fastOrderId);
    }


    @Login
    @GetMapping("/selectFastOrder")
    @ApiOperation("查询我的最近一次的约玩")
    public Result selectFastOrder(@RequestAttribute("userId") Long userId){
        return Result.success().put("data",fastOrderService.selectFastOrder(userId));
    }

    @Login
    @GetMapping("/selectFastOrderTake")
    @ApiOperation("接单池")
    public Result selectFastOrderTake(Integer page,Integer limit,@RequestAttribute("userId") Long userId,Integer templateType){
        return fastOrderService.selectFastOrderTake(page,limit,userId,templateType);
    }

    
    @Login
    @GetMapping("/selectFastOrderTakeListByUserId")
    @ApiOperation("查询我的抢单")
    public Result selectFastOrderTakeListByUserId(Integer page,Integer limit,@RequestAttribute("userId") Long userId,Integer status){
        return fastOrderService.selectFastOrderTakeListByUserId(page, limit, userId, status);
    }


    @Login
    @PostMapping("/insertFastOrderTake")
    @ApiOperation("发起抢单")
    public Result cancelFastOrderTake(@RequestBody FastOrderTake fastOrderTake, @RequestAttribute("userId") Long userId){
        fastOrderTake.setUserId(userId);
        return fastOrderTakeService.insertFastOrderTake(fastOrderTake);
    }

    @Login
    @PostMapping("/cancelFastOrderTake")
    @ApiOperation("取消抢单")
    public Result cancelFastOrderTake(Long fastOrderTakeId){
        return fastOrderTakeService.cancelFastOrderTake(fastOrderTakeId);
    }

    @Login
    @GetMapping("/selectFastOrderTakeList")
    @ApiOperation("查询我发布的快速约玩抢单记录")
    public Result selectFastOrderTakeList(Long fastOrderId){
        return fastOrderTakeService.selectFastOrderTakeList(fastOrderId);
    }

    @Login
    @GetMapping("/selectFastOrderTakeLists")
    @ApiOperation("查询我发布的快速约玩抢单记录")
    public Result selectFastOrderTakeLists(Long fastOrderId){
        return fastOrderTakeService.selectFastOrderTakeLists(fastOrderId);
    }

    @GetMapping("/selectFastOrderTakePage")
    @ApiOperation("搜索接口")
    public Result selectFastOrderTakePage(Integer page,Integer limit,FastOrder fastOrder){
        return fastOrderTakeService.selectFastOrderTakePage(page,limit,fastOrder);
    }


}
