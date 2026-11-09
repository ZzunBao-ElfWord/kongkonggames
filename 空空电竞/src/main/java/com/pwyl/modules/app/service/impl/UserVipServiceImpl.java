package com.pwyl.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.dao.UserVipDao;
import com.pwyl.modules.app.entity.UserVip;
import com.pwyl.modules.app.service.UserVipService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class UserVipServiceImpl extends ServiceImpl<UserVipDao, UserVip> implements UserVipService {

    @Override
    public UserVip selectUserVipByUserId(Long userId) {
        QueryWrapper<UserVip> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public Result isUserVip(Long userId) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //查询用户是否是会员
        QueryWrapper<UserVip> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        UserVip userVip = baseMapper.selectOne(queryWrapper);
        if (userVip != null) {
            //获取用户到期时间
            Date date = null;
            try {
                date = simpleDateFormat.parse(userVip.getEndTime());
            } catch (Exception e) {
                e.getMessage();
            }
            if (date.getTime() >= System.currentTimeMillis()) {
                userVip.setIsVip(1);
                baseMapper.updateById(userVip);
                return Result.success().put("data", true);
            } else {
                userVip.setIsVip(2);
                baseMapper.updateById(userVip);
                return Result.success().put("data", false);
            }
        } else {
            return Result.success().put("data", false);
        }
    }

    @Scheduled(fixedDelay = 60 * 1000)
    public void closeUserVip(){
        baseMapper.updateUserVip();
    }



}
