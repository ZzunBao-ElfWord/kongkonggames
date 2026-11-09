package com.pwyl.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwyl.common.utils.PageUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.dao.UserDao;
import com.pwyl.modules.app.dao.UserFollowDao;
import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.app.entity.UserFollow;
import com.pwyl.modules.app.response.UserFollowResponse;
import com.pwyl.modules.app.service.UserFollowService;
import lombok.AllArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class UserFollowServiceImpl extends ServiceImpl<UserFollowDao, UserFollow> implements UserFollowService {

    @Autowired
    private UserDao userDao;
    private ReentrantReadWriteLock reentrantReadWriteLock=new ReentrantReadWriteLock(true);

    @Override
    public Result insert(Long userId, Long followUserId) {
        reentrantReadWriteLock.writeLock().lock();
        try{
            //查询是否关注过此接单
            QueryWrapper<UserFollow> queryWrapper = new QueryWrapper();
            queryWrapper.eq("user_id", userId);
            queryWrapper.eq("follow_user_id", followUserId);
            UserFollow user = baseMapper.selectOne(queryWrapper);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //如果没关注 关注
            if (user == null) {
                String data = simpleDateFormat.format(new Date());
                UserFollow userFollow = new UserFollow();
                userFollow.setUserId(userId);
                userFollow.setFollowUserId(followUserId);
                userFollow.setCreateTime(data);
                baseMapper.insert(userFollow);
                return Result.success("关注成功");
            } else {
                //关注了 取消关注
                baseMapper.deleteById(user.getFollowId());
                return Result.success("取消关注成功");
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("关注出错了！"+e.getMessage(),e);
        }finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return Result.error("系统繁忙，请稍后再试！");
    }

    @Override
    public Result selectMyFollow(Long userId, Long page, Long limit) {
        IPage<UserFollowResponse> iPage = new Page<>(page, limit);
        //查看我的关注
        IPage iPage1 = baseMapper.selectMyFollow(iPage, userId);
        List<UserFollowResponse> lists = iPage1.getRecords();
        for (UserFollowResponse userFollowResponse : lists) {
            if (userFollowResponse != null) {
                //用我关注的用户id 去查询是否关注过我
                List<UserFollowResponse> list = baseMapper.selectMyFollow1(userFollowResponse.getUserId());
                if (list.size() > 0) {
                    for (UserFollowResponse userFollow : list) {
                        if (userFollow.getUserId().equals(userId)) {
                            //他也关注我   state 1
                            userFollowResponse.setStatus(1);
                            break;
                        } else {
                            //他没关注我   state 2
                            userFollowResponse.setStatus(2);
                        }
                    }
                } else {
                    //他没关注我   state 2
                    userFollowResponse.setStatus(2);
                }
            }
        }

        return Result.success().put("data", new PageUtils(iPage1));
    }

    @Override
    public Result selectFans(Long userId, Long page, Long limit) {
        IPage<UserFollowResponse> iPage = new Page<>(page, limit);
        //查看我的粉丝
        IPage page1 = baseMapper.selectFans(iPage, userId);
        List<UserFollowResponse> lists = page1.getRecords();
        for (UserFollowResponse userFollowResponse : lists) {
            if (userFollowResponse != null) {
                //查询我是否关注我的粉丝
                List<UserFollowResponse> list = baseMapper.selectFans1(userFollowResponse.getUserId());
                if (list.size() > 0) {
                    for (UserFollowResponse userFollow : list) {
                        if (userFollow.getUserId().equals(userId)) {
                            //我关注了   state 1
                            userFollowResponse.setStatus(1);
                            break;
                        } else {
                            //我没关注   state 2
                            userFollowResponse.setStatus(2);
                        }
                    }
                } else {
                    //他没关注我   state 2
                    userFollowResponse.setStatus(2);
                }
            }

        }
        return Result.success().put("data", new PageUtils(page1));
    }

    @Override
    public Result selectFollowUser(Long userId, Long followUserId) {
        QueryWrapper<UserFollow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("follow_user_id", followUserId);
        UserFollow userFollow = baseMapper.selectOne(queryWrapper);
        if (userFollow != null) {
            return Result.success().put("data", true);
        } else {
            return Result.success().put("data", false);
        }
    }
}
