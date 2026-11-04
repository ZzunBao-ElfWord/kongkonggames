package com.pwyl.modules.coupon.controller;

import com.pwyl.modules.coupon.utils.Result;
import com.pwyl.modules.coupon.entity.SelfCouponUser;
import com.pwyl.modules.coupon.service.SelfCouponUserService;
import com.pwyl.modules.coupon.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@Api(value="自营商城优惠券领取",tags={"自营商城优惠券领取"})
@RequestMapping(value = "/selfCouponUser")
public class SelfCouponUserController {
    @Autowired
    private SelfCouponUserService service;

    @PostMapping("/sendUserCoupon")
    @ApiOperation("赠送用户优惠券")
    public Result sendUserCoupon(String userIds,String couponIds,Integer type){
        service.sendUserCoupon(userIds, couponIds,type);
        return ResultUtil.success();
    }

    
    @GetMapping("/list")
    @ApiOperation("列表")
    public Result findAll(Integer page, Integer size, String couponName,String nickName) {
        return service.findAll(page, size, couponName,nickName);
    }

    
    @GetMapping("/userList")
    @ApiOperation("用户端用户优惠券列表")
    public Result userList(Integer page, Integer size, Long userId, @RequestParam(required = false) String goodsId, Integer type, BigDecimal money) {
        return service.userList(page, size, userId, goodsId, type,money);
    }
    
    @GetMapping("/useList")
    @ApiOperation("优惠券使用列表")
    public Result useList(Long userId, @RequestParam(required = false) String goodsId) {
        return service.useList(userId, goodsId);
    }


    
    @GetMapping("/find")
    @ApiOperation("查询")
    public Result findOne(Long id) {
        return service.findOne(id);
    }

    
    @PostMapping("/save")
    @ApiOperation("添加")
    public Result saveBody(@RequestBody SelfCouponUser entity) {
        return service.saveBody(entity);
    }




    
    @PostMapping("/update")
    @ApiOperation("修改")
    public Result updateBody(@RequestBody SelfCouponUser entity) {
        return service.updateBody(entity);
    }


    
    @GetMapping("/delete")
    @ApiOperation("删除")
    public Result delete(Long id) {
        return service.delete(id);
    }

}
