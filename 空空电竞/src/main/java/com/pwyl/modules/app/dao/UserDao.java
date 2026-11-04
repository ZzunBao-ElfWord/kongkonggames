package com.pwyl.modules.app.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwyl.modules.app.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 用户
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {


    IPage<UserEntity> selectUserPage(Page<UserEntity> page,@Param("search") String search,@Param("sex") Integer sex,@Param("platform") String platform, @Param("sysPhone")String sysPhone,@Param("status") Integer status,@Param("member")Integer member,@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("userName")String userName,@Param("inviterCode")String inviterCode,@Param("isAuthentication") Integer isAuthentication);

    int queryInviterCount(@Param("inviterCode") String inviterCode);

    int queryUserCount(@Param("type") int type, @Param("date") String date);

    int queryUserByPhoneCount(@Param("type") int type, @Param("date") String date);

    Double queryPayMoney(@Param("type") int type, @Param("date") String date);

    IPage<Map<String, Object>> queryCourseOrder(Page iPage, @Param("type") int type, @Param("date") String date);

    int userMessage(String date, int type);

    int insertUser(UserEntity userEntity);

    IPage<Map<String, Object>> takingOrdersMessage(Page<Map<String, Object>> iPage, @Param("type") Long type, @Param("date") String date);

    UserEntity selectUserByUserId(@Param("userId") Long userId,@Param("byUserId") Long byUserId);

    int updateUserConsortiaIsNullByUserId(Long userId);

    int updateUserConsortiaIsNullByConsortiaId(Long consortiaId);

    int updateUserClientIdIsNull(String clientid);

    IPage<UserEntity> selectUserByOrdersCount(Page<UserEntity> page);

    int userPlatform(String platform,String date,Integer type);

}
