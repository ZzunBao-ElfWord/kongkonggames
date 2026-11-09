package com.pwyl.modules.taking.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pwyl.modules.taking.entity.CommentFabulous;
import com.pwyl.modules.taking.entity.TakingCommnt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface OrderTakingCommentDao extends BaseMapper<TakingCommnt> {
    IPage<Map<String, Object>> selectOrderTakingComment(IPage page, @Param("takingId") Long id,@Param("userName") String userName,@Param("phone") String phone,@Param("content") String content,@Param("userId") Long userId);

    IPage<Map<String, Object>> selectCommentByOrderTakingUserId(IPage page, @Param("userId") Long userId);

    IPage<Map<String, Object>> selectCommentByUserId(IPage page, @Param("userId") Long userId);

    int selectCommentNumber(@Param("id") Long id);

    CommentFabulous selectGoodsNum(@Param("commentId") Long commentId, @Param("userId") Long userId);

    int deleteGoodsNum(@Param("id") Long id);

    int insertGoodsNum(@Param("commentId") Long commentId, @Param("userId") Long userId);

    Double selectAvgScore(@Param("orderTakingId") Long orderTakingId);

    Double selectAvgScoreByUserId(@Param("userId") Long userId);

}
