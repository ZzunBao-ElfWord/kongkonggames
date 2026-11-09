package com.pwyl.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.dao.UserMoneyDetailsDao;
import com.pwyl.modules.app.entity.UserMoneyDetails;
import com.pwyl.modules.app.service.UserMoneyDetailsService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserMoneyDetailsServiceImpl extends ServiceImpl<UserMoneyDetailsDao, UserMoneyDetails> implements UserMoneyDetailsService {

    @Override
    public Result queryUserMoneyDetails(Integer page, Integer limit, Long userId,Integer classify,Integer type) {
        IPage<UserMoneyDetails> page1 = new Page(page, limit);
        QueryWrapper<UserMoneyDetails> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        if(classify!=null){
            if(classify==10){
                queryWrapper.in("classify", 1,10,5,6);
            }else{
                queryWrapper.eq("classify", classify);
            }
        }else{
            queryWrapper.isNull("classify");
        }
        if(type!=null){
            queryWrapper.eq("type", type);
        }
        queryWrapper.orderByDesc("create_time");
        return Result.success().put("data", baseMapper.selectPage(page1, queryWrapper));
    }

    @Override
    public Double monthIncome(String date, Long userId) {
        return baseMapper.monthIncome(date,userId);
    }


    @Override
    public Double selectSumMoneyByClassifyAndUserId(Long userId,Integer classify,String time,String flag){
        return baseMapper.selectSumMoneyByClassifyAndUserId(userId, classify,time,flag);
    }

    @Override
    public Result selectConsortiaPersonMoney(Integer page,Integer limit, Long consortiaId, Long userId){
        return Result.success().put("data",baseMapper.selectConsortiaPersonMoney(new Page<>(page,limit),consortiaId,userId));
    }

    @Override
    public Result selectUserMoneyDetailsUserIdAndByUserId(Integer page,Integer limit,Long userId,Long byUserId,Integer classify){
        return Result.success().put("data",baseMapper.selectUserMoneyDetailsUserIdAndByUserId(new Page<>(page,limit),userId,byUserId,classify));
    }

    @Override
    public BigDecimal selectRefundMoney(Integer flag, String time){
        return baseMapper.selectRefundMoney(flag, time);
    }


}
