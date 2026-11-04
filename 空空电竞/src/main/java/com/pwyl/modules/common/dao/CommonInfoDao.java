package com.pwyl.modules.common.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwyl.modules.common.entity.CommonInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author fang
 * @date 2020/7/8
 */
@Mapper
public interface CommonInfoDao extends BaseMapper<CommonInfo> {

    List<CommonInfo> findByCondition(@Param("condition") String condition);

    CommonInfo findOne(@Param("type") int type);

    IPage<Map<String,Object>> selectTalkPage(Page<Map<String,Object>> page, @Param("status") Integer status, @Param("userId") Long userId,
                                             @Param("byUserId") Long byUserId, @Param("followUserId") Long followUserId,
                                             @Param("isDelete") Integer isDelete,@Param("nickName") String nickName,@Param("content") String content,
                                             @Param("videoOnly") Integer videoOnly);

    IPage<Map<String,Object>> selectCommentPage(Page<Map<String,Object>> page, Long userId, Long talkId,String userName,String phone,String content);

}
