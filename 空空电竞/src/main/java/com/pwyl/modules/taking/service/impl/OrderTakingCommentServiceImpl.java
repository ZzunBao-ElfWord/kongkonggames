package com.pwyl.modules.taking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwyl.common.utils.DateUtils;
import com.pwyl.common.utils.PageUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.entity.Risk;
import com.pwyl.modules.app.service.RiskService;
import com.pwyl.modules.orders.dao.OrdersDao;
import com.pwyl.modules.orders.entity.Orders;
import com.pwyl.modules.taking.dao.OrderTakingCommentDao;
import com.pwyl.modules.taking.dao.OrderTakingDao;
import com.pwyl.modules.taking.entity.CommentFabulous;
import com.pwyl.modules.taking.entity.OrderTaking;
import com.pwyl.modules.taking.entity.TakingCommnt;
import com.pwyl.modules.taking.response.TakingCommentResponse;
import com.pwyl.modules.taking.response.TakingDetailsResponse;
import com.pwyl.modules.taking.service.OrderTakingCommentService;
import com.pwyl.modules.utils.SenInfoCheckUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class OrderTakingCommentServiceImpl extends ServiceImpl<OrderTakingCommentDao, TakingCommnt> implements OrderTakingCommentService {

    @Autowired
    private OrderTakingCommentDao orderTakingCommentDao;
    @Autowired
    private OrderTakingDao orderTakingDao;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private RiskService riskService;

    @Override
    public Result selectOrderTakingComment(Integer page, Integer limit, Long id,String userName,String phone,String content,Long userId) {
        IPage<TakingDetailsResponse> page1 = new Page<>(page, limit);
        IPage iPage = orderTakingCommentDao.selectOrderTakingComment(page1, id,userName,phone,content,userId);
        return Result.success().put("data", new PageUtils(iPage));
    }


    @Override
    public Result selectCommentByOrderTakingUserId(Integer page, Integer limit, Long userId) {
        IPage<TakingDetailsResponse> page1 = new Page<>(page, limit);
        IPage iPage = orderTakingCommentDao.selectCommentByOrderTakingUserId(page1, userId);
        return Result.success().put("data", new PageUtils(iPage));
    }

    @Override
    public Result selectCommentByUserId(Integer page, Integer limit, Long userId) {
        IPage<TakingDetailsResponse> page1 = new Page<>(page, limit);
        IPage iPage = orderTakingCommentDao.selectCommentByUserId(page1, userId);
        return Result.success().put("data", new PageUtils(iPage));
    }

    @Override
    public Result updateGoodsNum(Long commentId, Long userId) {
        //判断自己是否点过赞
        CommentFabulous commentFabulous = orderTakingCommentDao.selectGoodsNum(commentId, userId);
        if (commentFabulous != null) {
            //有赞则取消点赞
            int i = orderTakingCommentDao.deleteGoodsNum(commentFabulous.getId());
            if (i > 0) {
                return Result.success("取消点赞成功！");
            } else {
                return Result.error("取消点赞失败！");
            }
        } else {
            //无赞 点赞
            int i = orderTakingCommentDao.insertGoodsNum(commentId, userId);
            if (i > 0) {
                return Result.success("点赞成功！");
            } else {
                return Result.error("点赞失败！");
            }
        }
    }

    @Override
    public Result addGoodsNum(Long id, Long userId, String content, Integer score,Long ordersId) {
        int i = selectTakingCommentCount(id, userId);
        if(i>0){
            return Result.error("您已经评价过了！");
        }
        if(!SenInfoCheckUtil.contentFilter(content)){
            Risk risk=new Risk();
            risk.setUserId(userId);
            risk.setContent("系统检测触发关键词："+content);
            risk.setRiskType(4);
            risk.setCreateTime(DateUtils.format(new Date()));
            riskService.save(risk);
            return Result.error("内容不符合要求，请检查后重新提交！");
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //创建
        TakingCommnt takingCommnt = new TakingCommnt();
        takingCommnt.setContent(content);
        takingCommnt.setOrderTakingId(id);
        takingCommnt.setUserId(userId);
        takingCommnt.setCreateTime(simpleDateFormat.format(new Date()));
        takingCommnt.setScore(score);
        takingCommnt.setOrdersId(ordersId);
        baseMapper.insert(takingCommnt);
        OrderTaking orderTaking = new OrderTaking();
        orderTaking.setId(id);
        orderTaking.setOrderScore(baseMapper.selectAvgScore(id));
        orderTakingDao.updateById(orderTaking);
        return Result.success();
    }

    @Override
    public int selectTakingCommentCount(Long ordersId,Long userId){
        return baseMapper.selectCount(new QueryWrapper<TakingCommnt>().eq("orders_id",ordersId).eq("user_id",userId));
    }

    @Override
    public Result queryMyComment(Long userId) {

        return null;
    }
}
