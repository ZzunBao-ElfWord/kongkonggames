package com.pwyl.modules.app.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.app.entity.UserMoneyDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface UserMoneyDetailsDao extends BaseMapper<UserMoneyDetails> {

    Double monthIncome(@Param("date") String date,@Param("userId") Long userId);

    Double selectMyProfit(Long userId);

    Double selectSumMoneyByClassifyAndUserId(Long userId,Integer classify,String time,String flag);

    IPage<UserEntity> selectConsortiaPersonMoney(Page<UserEntity> page, Long consortiaId, Long userId);

    IPage<UserMoneyDetails> selectUserMoneyDetailsUserIdAndByUserId(Page<UserMoneyDetails> page,Long userId,Long byUserId,Integer classify);

    BigDecimal selectRefundMoney(Integer flag, String time);

}
