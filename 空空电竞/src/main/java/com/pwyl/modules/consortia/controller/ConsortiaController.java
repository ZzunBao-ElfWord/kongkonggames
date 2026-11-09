package com.pwyl.modules.consortia.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pwyl.common.utils.DateUtils;
import com.pwyl.common.utils.Result;
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
@RequestMapping("/consortia")
@Api(value = "公会", tags = {"公会"})
public class ConsortiaController {

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


    @PostMapping("/insertConsortia")
    @ApiOperation("创建公会")
    public Result insertConsortia(@RequestBody Consortia consortia){
        return consortiaService.insertConsortia(consortia);
    }

    @PostMapping("/updateConsortia")
    @ApiOperation("修改公会")
    public Result updateConsortia(@RequestBody Consortia consortia){
        return consortiaService.updateConsortia(consortia);
    }

    @PostMapping("/deleteConsortia")
    @ApiOperation("删除公会")
    public Result deleteConsortia(Long consortiaId){
        return consortiaService.deleteConsortia(consortiaId);
    }

    @GetMapping("/selectConsortiaById")
    @ApiOperation("查询公会")
    public Result selectConsortiaById(Long consortiaId){
        return Result.success().put("data",consortiaService.getById(consortiaId));
    }

    @GetMapping("/selectConsortiaList")
    @ApiOperation("查询公会列表")
    public Result selectConsortiaList(Integer page,Integer limit,String consortiaName,String userName,String phone){
        return consortiaService.selectConsortiaList(page, limit, consortiaName, userName,phone);
    }

    @GetMapping("/selectConsortiaPersonList")
    @ApiOperation("查询公会成员")
    public Result selectConsortiaPersonList(Integer page,Integer limit,String userName,String phone,Long consortiaId){
        return userService.selectConsortiaPersonList(page, limit, userName, phone,consortiaId);
    }

    @PostMapping("/insertConsortiaApply")
    @ApiOperation("公会申请")
    public Result insertConsortiaApply(@RequestBody ConsortiaApply consortiaApply){
        return consortiaApplyService.insertConsortiaApply(consortiaApply);
    }

    @PostMapping("/auditConsortiaApply")
    @ApiOperation("审核公会申请")
    public Result auditConsortiaApply(String ids,Integer status,String content){
        return consortiaApplyService.auditConsortiaApply(ids, status, content);
    }

    @GetMapping("/selectConsortiaApplyList")
    @ApiOperation("入会申请列表")
    public Result selectConsortiaApplyList(Integer page,Integer limit,Integer status,Long consortiaId,Integer classify){
        return consortiaApplyService.selectConsortiaApplyList(page, limit, status, consortiaId,classify);
    }

    @GetMapping("/selectConsortiaApplyByUserId")
    @ApiOperation("查看自己的入会申请列表")
    public Result selectConsortiaApplyByUserId(Integer page,Integer limit,Integer status,Long userId){
        return consortiaApplyService.selectConsortiaApplyByUserId(page, limit, status, userId);
    }

    @GetMapping("/selectConsortiaPersonMoneyList")
    @ApiOperation("公会人员收益列表")
    public Result selectConsortiaPersonMoneyList(Integer page,Integer limit,Long consortiaId){
        Consortia consortia = consortiaService.getById(consortiaId);
        return userMoneyDetailsService.selectConsortiaPersonMoney(page,limit,consortiaId,consortia.getUserId());
    }


    @GetMapping("/selectConsortiaPersonMoneyDetails")
    @ApiOperation("公会人员收益明细")
    public Result selectConsortiaPersonMoneyDetails(Integer page,Integer limit,Long userId,Long consortiaId,Integer classify){
        Consortia consortia = consortiaService.getById(consortiaId);
        return userMoneyDetailsService.selectUserMoneyDetailsUserIdAndByUserId(page,limit,consortia.getUserId(),userId,classify);
    }

    @PostMapping("/outConsortiaPerson")
    @ApiOperation("踢出公会")
    public Result outConsortiaPerson(Long userId){
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

    @GetMapping("/selectConsortiaData")
    @ApiOperation("公会邀请人数和收益")
    public Result selectConsortiaData(Long consortiaId){
        Consortia consortia = consortiaService.getById(consortiaId);
        //公会总收益
        Double sumMoney = userMoneyDetailsService.selectSumMoneyByClassifyAndUserId(consortia.getUserId(), null,null,null);
        //公会邀请人数
        int count = userService.count(new QueryWrapper<UserEntity>().eq("consortia_id", consortiaId));
        Map<String,Object> result=new HashMap<>();
        result.put("sumMoney",sumMoney);
        result.put("count",count);
        return Result.success().put("data",result);
    }

    @GetMapping("/selectConsortiaMoney")
    @ApiOperation("查询公会收益")
    public Result selectConsortiaMoney(Long consortiaId,String time,String flag){
        Consortia consortia = consortiaService.getById(consortiaId);
        //公会总收益
        Double sumMoney = userMoneyDetailsService.selectSumMoneyByClassifyAndUserId(consortia.getUserId(), null,time,flag);
        //订单收益
        Double ordersMoney = userMoneyDetailsService.selectSumMoneyByClassifyAndUserId(consortia.getUserId(), 5,time,flag);
        //礼物收益
        Double giftMoney = userMoneyDetailsService.selectSumMoneyByClassifyAndUserId(consortia.getUserId(), 6,time,flag);
        Map<String,Double> result=new HashMap<>();
        result.put("sumMoney",sumMoney);
        result.put("ordersMoney",ordersMoney);
        result.put("giftMoney",giftMoney);
        return Result.success().put("data",result);
    }

    @PostMapping("/updateUserConsortia")
    public Result updateUserConsortia(Long userId,String consortiaNo){
        Consortia consortia = consortiaService.selectConsortiaByNo(consortiaNo);
        if(consortia==null){
            return Result.error("公会不存在，请检查公会编号！");
        }
        UserEntity userEntity = userService.selectUserById(userId);
        userEntity.setConsortiaId(consortia.getConsortiaId());
        userService.updateById(userEntity);
        return Result.success();
    }




}
