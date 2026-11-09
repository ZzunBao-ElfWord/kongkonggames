package com.pwyl.modules.app.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwyl.modules.app.entity.UserVip;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserVipDao extends BaseMapper<UserVip> {

    int updateUserVip();

    /**
     * 苹果内购并发发放专用：幂等初始化会员行(依赖 user_id 唯一索引)。
     */
    int insertIgnoreVip(UserVip userVip);

    /**
     * 苹果内购并发发放专用：行锁锁定会员记录，保证叠加会员时长不丢失更新。
     */
    UserVip selectForUpdate(@Param("userId") Long userId);

}
