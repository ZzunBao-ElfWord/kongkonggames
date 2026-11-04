package com.pwyl.modules.app.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.entity.UserCertification;
import com.pwyl.modules.app.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserCertificationDao extends BaseMapper<UserCertification> {

    IPage<Map<String, Object>> queryCertification(IPage iPage, @Param("status") String status, @Param("name") String name);

    IPage<Map<String, Object>> queryUserCertification(IPage iPage, @Param("name") String name,@Param("phone")String phone);
}
