package com.pwyl.modules.taking.controller;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.taking.entity.FastOrder;
import com.pwyl.modules.taking.service.FastOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fastOrder")
@Api("快速约玩")
public class FastOrderController {

    @Autowired
    private FastOrderService fastOrderService;

    @PostMapping("/insertFastOrder")
    @ApiOperation("发起快速约玩")
    public Result insertFastOrder(@RequestBody FastOrder fastOrder){
        return fastOrderService.insertFastOrder(fastOrder);
    }

    @GetMapping("/selectFastOrderPage")
    @ApiOperation("查询我发布的快速约玩")
    public Result selectFastOrderPage(Integer page,Integer limit,Long userId,Integer status,String userName,String phone,Integer templateType){
        return fastOrderService.selectFastOrderPage(page, limit, userId, status, userName, phone,templateType);
    }

    @PostMapping("/cancelFastOrder")
    @ApiOperation("取消约玩")
    public Result cancelFastOrder(Long fastOrderId){
        return fastOrderService.cancelFastOrder(fastOrderId);
    }

    @GetMapping("/selectFastOrderTakeListByUserId")
    @ApiOperation("查询我的抢单")
    public Result selectFastOrderTakeListByUserId(Integer page,Integer limit,Long userId,Integer status){
        return fastOrderService.selectFastOrderTakeListByUserId(page, limit, userId, status);
    }





}
