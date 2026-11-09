package com.pwyl.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwyl.common.utils.PageUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.dao.UserBrowseDao;
import com.pwyl.modules.app.dao.UserFollowDao;
import com.pwyl.modules.app.dao.UserVisitorDao;
import com.pwyl.modules.app.entity.UserBrowse;
import com.pwyl.modules.app.entity.UserFollow;
import com.pwyl.modules.app.entity.UserVisitor;
import com.pwyl.modules.app.response.UserFollowResponse;
import com.pwyl.modules.app.service.UserBrowseService;
import com.pwyl.modules.taking.dao.GameDao;
import com.pwyl.modules.taking.dao.OrderTakingDao;
import com.pwyl.modules.taking.entity.Game;
import com.pwyl.modules.taking.entity.OrderTaking;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class UserBrowseServiceImpl extends ServiceImpl<UserBrowseDao, UserBrowse> implements UserBrowseService {
    private UserFollowDao userFollowDao;
    private OrderTakingDao orderTakingDao;
    private UserVisitorDao userVisitorDao;
    private GameDao gameDao;
    private UserBrowseDao userBrowseDao;

    @Override
    public Result selectMyVisitor(Long userId, Long page, Long limit) {
        //查询我的访客
        IPage<UserFollowResponse> iPage = new Page(page, limit);
        return Result.success().put("data", new PageUtils(userVisitorDao.selectMyVisitor(iPage, userId)));
    }

    @Override
    public Result selectMyBrowse(Long userId, Long page, Long limit) {
        //查询我的浏览
        IPage<UserFollowResponse> iPage = new Page(page, limit);
        IPage iPage1 = baseMapper.selectMyBrowse(iPage, userId);
        List<UserFollowResponse> lists = iPage1.getRecords();
        for (UserFollowResponse userFollowResponse : lists) {
            if (userFollowResponse != null) {
                OrderTaking orderTaking = orderTakingDao.selectById(userFollowResponse.getTakingId());
                if(orderTaking!=null){
                    Game game = gameDao.selectById(orderTaking.getGameId());
                    if(game!=null){
                        orderTaking.setGame(game);
                    }
                    userFollowResponse.setOrderTaking(orderTaking);
                }
            }
        }
        return Result.success().put("data", new PageUtils(iPage1));
    }

    @Override
    public Result selectAmount(Long userId) {
        Map<String, Object> map = new HashMap<>();
        baseMapper.selectMyVisitor1(userId);
        map.put("fans", userFollowDao.selectFans1(userId).size());
        map.put("follow", userFollowDao.selectMyFollow1(userId).size());
        map.put("visitor", userVisitorDao.selectMyVisitor1(userId).size());
        map.put("browse", baseMapper.selectMyBrowse1(userId).size());
        return Result.success().put("data", map);
    }

    @Override
    public Result addAmount(Long userId, Long byBrowseId, Long takingId) {

        UserBrowse userBrowse = baseMapper.selectOne(new QueryWrapper<UserBrowse>().eq("user_id", userId).eq("by_browse_id", byBrowseId).eq("taking_id", takingId));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (userBrowse != null) {
            userBrowse.setUpdateTime(simpleDateFormat.format(new Date()));
            baseMapper.updateById(userBrowse);
        } else {
            UserBrowse userBrowse1 = new UserBrowse();
            userBrowse1.setUserId(userId);
            userBrowse1.setByBrowseId(byBrowseId);
            userBrowse1.setTakingId(takingId);
            userBrowse1.setUpdateTime(simpleDateFormat.format(new Date()));
            baseMapper.insert(userBrowse1);
        }
        return Result.success();
    }

    @Override
    public Result addVisitor(Long userId, Long byBrowseId) {
        if (userId.equals(byBrowseId)) {
            return Result.success();
        } else if (byBrowseId == null) {
            return Result.success();
        } else {
            UserVisitor userVisitor = userVisitorDao.selectOne(new QueryWrapper<UserVisitor>().eq("user_id", userId).eq("by_user_id", byBrowseId));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (userVisitor != null) {
                userVisitor.setUpdateTime(simpleDateFormat.format(new Date()));
                userVisitorDao.updateById(userVisitor);
            } else {
                UserVisitor userVisitor1 = new UserVisitor();
                userVisitor1.setUserId(userId);
                userVisitor1.setByUserId(byBrowseId);
                userVisitor1.setUpdateTime(simpleDateFormat.format(new Date()));
                userVisitorDao.insert(userVisitor1);
            }
            return Result.success();
        }
    }

    @Override
    public Result deleteMyVisitor(Long id) {
        UserVisitor userVisitor = userVisitorDao.selectById(id);
        if (userVisitor == null) {
            return Result.error("访客已被删除！");
        } else {
            int i = userVisitorDao.deleteById(userVisitor.getId());
            if (i > 0) {
                return Result.success();
            } else {
                return Result.error();
            }
        }

    }

    @Override
    public Result deleteMyBrowse(Long id) {

        UserBrowse userBrowse = userBrowseDao.selectById(id);
        if (userBrowse == null) {
            return Result.error("足迹已被删除！");
        } else {
            int i = userBrowseDao.deleteById(id);
            if (i > 0) {
                return Result.success();
            } else {
                return Result.error();
            }
        }
    }


}
