package com.pwyl.modules.taking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.entity.App;
import com.pwyl.modules.taking.entity.TakingCommnt;
import org.springframework.web.bind.annotation.RequestAttribute;

public interface OrderTakingCommentService extends IService<TakingCommnt> {
    /**
     * 查看接单的评论
     */
    Result selectOrderTakingComment(Integer page, Integer limit, Long id,String userName,String phone,String content,Long userId);

    Result selectCommentByOrderTakingUserId(Integer page, Integer limit, Long userId);

    Result selectCommentByUserId(Integer page, Integer limit, Long userId);

    /**
     * 点赞
     *
     * @param commentId
     * @param userId
     * @return
     */
    Result updateGoodsNum(Long commentId, Long userId);

    /**
     * 添加评论
     *
     * @param id
     * @param userId
     * @param content
     */
    Result addGoodsNum(Long id, Long userId, String content, Integer score,Long ordersId);

    /**
     * 我的评论
     *
     * @param userId
     * @return
     */
    Result queryMyComment(Long userId);

    int selectTakingCommentCount(Long orderTakingId,Long userId);
}
