package com.pwyl.modules.consortia.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pwyl.common.utils.DateUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.annotation.Login;
import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.app.service.UserMoneyDetailsService;
import com.pwyl.modules.app.service.UserService;
import com.pwyl.modules.consortia.entity.Consortia;
import com.pwyl.modules.consortia.entity.ConsortiaApply;
import com.pwyl.modules.consortia.service.ConsortiaApplyService;
import com.pwyl.modules.consortia.service.ConsortiaService;
import com.pwyl.modules.message.entity.MessageInfo;
import com.pwyl.modules.message.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/app/consortia")
@Api(value = "公会", tags = {"公会"})
public class AppConsortiaController {

    @Autowired
    private ConsortiaService consortiaService;
    @Autowired
    private ConsortiaApplyService consortiaApplyService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private MessageService messageService;

    @Login
    @GetMapping("/selectConsortiaById")
    @ApiOperation("查询公会")
    public Result selectConsortiaById(Long consortiaId) {
        return Result.success().put("data", consortiaService.getById(consortiaId));
    }


    @Login
    @GetMapping("/selectConsortiaPersonList")
    @ApiOperation("查询公会成员")
    public Result selectConsortiaPersonList(Integer page, Integer limit, String userName, String phone, Long consortiaId) {
        return userService.selectConsortiaPersonList(page, limit, userName, phone, consortiaId);
    }

    @Login
    @PostMapping("/insertConsortiaApply")
    @ApiOperation("公会申请")
    public Result insertConsortiaApply(@RequestBody ConsortiaApply consortiaApply) {
        return consortiaApplyService.insertConsortiaApply(consortiaApply);
    }

    @Login
    @GetMapping("/selectOldConsortiaApplyByUserId")
    @ApiOperation("查看正在申请中的公会")
    public Result selectOldConsortiaApplyByUserId(@RequestAttribute("userId") Long userId, Integer classify) {
        return consortiaApplyService.selectOldConsortiaApplyByUserId(userId, classify);
    }


    @Login
    @PostMapping("/auditConsortiaApply")
    @ApiOperation("审核公会申请")
    public Result auditConsortiaApply(String ids, Integer status, String content) {
        return consortiaApplyService.auditConsortiaApply(ids, status, content);
    }

    @Login
    @GetMapping("/selectConsortiaApplyList")
    @ApiOperation("入会申请列表")
    public Result selectConsortiaApplyList(Integer page, Integer limit, Integer status, Long consortiaId, Integer classify) {
        return consortiaApplyService.selectConsortiaApplyList(page, limit, status, consortiaId, classify);
    }

    @Login
    @GetMapping("/selectConsortiaApplyByUserId")
    @ApiOperation("查看自己的入会申请列表")
    public Result selectConsortiaApplyByUserId(Integer page, Integer limit, Integer status,@RequestAttribute Long userId) {
        return consortiaApplyService.selectConsortiaApplyByUserId(page, limit, status, userId);
    }

    @Login
    @GetMapping("/selectConsortiaMoney")
    @ApiOperation("查询公会收益")
    public Result selectConsortiaMoney(Long consortiaId) {
        Consortia consortia = consortiaService.getById(consortiaId);
        //公会总收益
        Double sumMoney = userMoneyDetailsService.selectSumMoneyByClassifyAndUserId(consortia.getUserId(), null,null,null);
        //订单收益
        Double ordersMoney = userMoneyDetailsService.selectSumMoneyByClassifyAndUserId(consortia.getUserId(), 5,null,null);
        //礼物收益
        Double giftMoney = userMoneyDetailsService.selectSumMoneyByClassifyAndUserId(consortia.getUserId(), 6,null,null);
        Map<String, Double> result = new HashMap<>();
        result.put("sumMoney", sumMoney);
        result.put("ordersMoney", ordersMoney);
        result.put("giftMoney", giftMoney);
        return Result.success().put("data", result);
    }

    @Login
    @GetMapping("/selectConsortiaData")
    @ApiOperation("公会邀请人数和收益")
    public Result selectConsortiaData(Long consortiaId) {
        Consortia consortia = consortiaService.getById(consortiaId);
        //公会总收益
        Double sumMoney = userMoneyDetailsService.selectSumMoneyByClassifyAndUserId(consortia.getUserId(), null,null,null);
        //公会邀请人数
        int count = userService.count(new QueryWrapper<UserEntity>().eq("consortia_id", consortiaId));
        Map<String, Object> result = new HashMap<>();
        result.put("sumMoney", sumMoney);
        result.put("count", count);
        return Result.success().put("data", result);
    }

    @Login
    @GetMapping("/selectConsortiaPersonMoneyList")
    @ApiOperation("公会人员收益列表")
    public Result selectConsortiaPersonMoneyList(Integer page, Integer limit, Long consortiaId) {
        Consortia consortia = consortiaService.getById(consortiaId);
        return userMoneyDetailsService.selectConsortiaPersonMoney(page, limit, consortiaId, consortia.getUserId());
    }

    @Login
    @GetMapping("/selectConsortiaPersonMoneyDetails")
    @ApiOperation("公会人员收益明细")
    public Result selectConsortiaPersonMoneyDetails(Integer page, Integer limit, Long userId, Long consortiaId, Integer classify) {
        Consortia consortia = consortiaService.getById(consortiaId);
        return userMoneyDetailsService.selectUserMoneyDetailsUserIdAndByUserId(page, limit, consortia.getUserId(), userId, classify);
    }

    @Login
    @PostMapping("/outConsortiaPerson")
    @ApiOperation("踢出公会")
    public Result outConsortiaPerson(@RequestAttribute("userId") Long consortiaIdUserId, Long userId) {
        UserEntity userEntity1 = userService.selectUserById(userId);
        Consortia consortia = consortiaService.getById(userEntity1.getConsortiaId());
        if(!consortia.getUserId().equals(consortiaIdUserId)){
            return Result.error("您不是会长，不支持本次操作！");
        }
        userService.updateUserConsortiaIsNullByUserId(userId);
        UserEntity userEntity = userService.selectUserById(userId);
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setContent("您已经被移除公会！");
        messageInfo.setTitle("退出公会通知");
        messageInfo.setState(String.valueOf(5));
        messageInfo.setUserName(userEntity.getUserName());
        messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
        messageInfo.setCreateAt(DateUtils.format(new Date()));
        messageInfo.setIsSee("0");
        messageService.saveBody(messageInfo);
        return Result.success();
    }



}
