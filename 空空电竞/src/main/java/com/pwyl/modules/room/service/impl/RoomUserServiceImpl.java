package com.pwyl.modules.room.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwyl.common.utils.PageUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.room.dao.RoomUserDao;
import com.pwyl.modules.room.entity.RoomUser;
import com.pwyl.modules.room.service.RoomUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 房间用户
 */
@Service
public class RoomUserServiceImpl extends ServiceImpl<RoomUserDao,RoomUser> implements RoomUserService {

    private ReentrantReadWriteLock reentrantReadWriteLock=new ReentrantReadWriteLock(true);

    @Override
    public Result selectRoomUserList(Integer page, Integer limit, Long userId, String userName, String phone, Long roomId,
                                     String roomName, String roomNo,Integer isSeat,Integer status){
        return Result.success().put("data",new PageUtils(baseMapper.selectRoomUserList(new Page<>(page,limit),userId,userName,
                phone,roomId,roomName,roomName,isSeat,status)));
    }

    @Override
    public RoomUser selectRoomUserByUserIdAndRoomId(Long roomId,Long userId){
        return baseMapper.selectOne(new QueryWrapper<RoomUser>().eq("room_id",roomId).eq("user_id",userId));
    }

    @Override
    public RoomUser selectRoomUserBySeatNoAndRoomId(Long roomId,String seatNo){
        reentrantReadWriteLock.writeLock().lock();
        try {
            return baseMapper.selectOne(new QueryWrapper<RoomUser>().eq("room_id",roomId).eq("is_seat",1).eq("seat_no",seatNo));
        }catch (Exception e){
            e.getMessage();
        }finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return null;
    }

    @Override
    public List<UserEntity> selectUserByRoomUserId(Long roomId){
        return baseMapper.selectUserByRoomUserId(roomId);
    }

}
