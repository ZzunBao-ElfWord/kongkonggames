package com.pwyl.modules.coupon.controller.app;

import com.pwyl.modules.coupon.entity.SelfCouponIssue;
import com.pwyl.modules.coupon.service.SelfCouponIssueService;
import com.pwyl.modules.coupon.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value="自营商城优惠券发布",tags={"自营商城优惠券发布"})
@RequestMapping(value = "/app/selfCouponIssue")
public class AppSelfCouponIssueController {
    @Autowired
    private SelfCouponIssueService service;

    
    @GetMapping("/list")
    @ApiOperation("后台列表")
    public Result findAll(Integer page, Integer size) {
        return service.findAll(page, size);
    }

    
    @GetMapping("/useList")
    @ApiOperation("用户端领券列表")
    public Result useList(@RequestParam(required = false) Long goodsId, @RequestParam(required = false) Long userId) {
        return service.useList(goodsId, userId);
    }

    
    @GetMapping("/find")
    @ApiOperation("查询")
    public Result findOne(Long id) {
        return service.findOne(id);
    }

    
    @PostMapping("/save")
    @ApiOperation("添加")
    public Result saveBody(@RequestBody SelfCouponIssue entity) {
        return service.saveBody(entity);
    }

    
    @PostMapping("/update")
    @ApiOperation("修改")
    public Result updateBody(@RequestBody SelfCouponIssue entity) {
        return service.updateBody(entity);
    }

    
    @GetMapping("/delete")
    @ApiOperation("删除")
    public Result delete(Long id) {
        return service.delete(id);
    }

}
