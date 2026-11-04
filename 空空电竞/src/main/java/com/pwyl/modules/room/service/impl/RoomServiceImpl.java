package com.pwyl.modules.room.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwyl.common.utils.PageUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.common.service.CommonInfoService;
import com.pwyl.modules.room.dao.RoomDao;
import com.pwyl.modules.room.entity.Room;
import com.pwyl.modules.room.entity.RoomUser;
import com.pwyl.modules.room.service.RoomService;
import com.pwyl.modules.room.service.RoomUserService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.trtc.v20190722.TrtcClient;
import com.tencentcloudapi.trtc.v20190722.models.DismissRoomByStrRoomIdRequest;
import com.tencentcloudapi.trtc.v20190722.models.DismissRoomByStrRoomIdResponse;
import com.tencentcloudapi.trtc.v20190722.models.RemoveUserByStrRoomIdRequest;
import com.tencentcloudapi.trtc.v20190722.models.RemoveUserByStrRoomIdResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 房间
 */
@Service
public class RoomServiceImpl extends ServiceImpl<RoomDao, Room> implements RoomService {

    @Autowired
    private RoomUserService roomUserService;
    @Autowired
    private CommonInfoService commonInfoService;
    private ReentrantReadWriteLock reentrantReadWriteLock=new ReentrantReadWriteLock(true);

    @Override
    public Result selectRoomList(Integer page,Integer limit,Long userId,String roomNo,String roomName,String userName,
                                 Integer status,Long gameId,Integer roomType,String search){
        IPage<Room> roomIPage = baseMapper.selectRoomList(new Page<>(page, limit), userId, roomNo, roomName, userName, status, gameId,roomType,search);
        List<Room> records = roomIPage.getRecords();
        for (Room room:records){
            room.setUserEntityList(roomUserService.selectUserByRoomUserId(room.getRoomId()));
        }
        return Result.success().put("data",new PageUtils(roomIPage));
    }

    @Override
    public Result selectRoomDetails(Long roomId,Long userId){
        reentrantReadWriteLock.writeLock().lock();
        try {
            Room room = baseMapper.selectById(roomId);
            room.setRoomUserCount(roomUserService.count(new QueryWrapper<RoomUser>().eq("room_id",roomId).eq("status",1)));
            return Result.success().put("data",room);
        }catch (Exception e){
            e.printStackTrace();
            log.error("查询语音房详情出错！"+e.getMessage(),e);
        }finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return Result.error("系统繁忙，请稍后再试！");
    }

    @Override
    public Room selectRoomByRoomNo(String roomNo){
        return baseMapper.selectOne(new QueryWrapper<Room>().eq("room_no",roomNo));
    }

    @Override
    public String setRoomNo() {
        int t = (int) ((Math.random() * 9 + 1) * 1000000);
        String s = String.valueOf(t);
        Room room = selectRoomByRoomNo(s);
        if(room==null){
            return s;
        }
        return setRoomNo();
    }

    @Override
    public Result roomRemoveUser(String roomNo,Long userId){
        String secretId = commonInfoService.findOne(815).getValue();
        String secretKey = commonInfoService.findOne(816).getValue();
        String sdkAppId = commonInfoService.findOne(814).getValue();
        try{
            Credential cred = new Credential(secretId, secretKey);
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("trtc.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            TrtcClient client = new TrtcClient(cred, "ap-beijing", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            RemoveUserByStrRoomIdRequest req = new RemoveUserByStrRoomIdRequest();
            req.setSdkAppId(Long.parseLong(sdkAppId));
            req.setRoomId(roomNo);

            String[] userIds1 = {String.valueOf(userId)};
            req.setUserIds(userIds1);
            // 返回的resp是一个RemoveUserByStrRoomIdResponse的实例，与请求对象对应
            RemoveUserByStrRoomIdResponse resp = client.RemoveUserByStrRoomId(req);
            String s = RemoveUserByStrRoomIdResponse.toJsonString(resp);
            JSONObject jsonObject=JSONObject.parseObject(s);
            JSONObject response = jsonObject.getJSONObject("Response");
            if(response!=null){
                JSONObject error = response.getJSONObject("Error");
                if(error!=null){
                    String message = error.getString("Message");
                    log.error("移除用户异常："+message);
                    return Result.error("移除用户失败，请稍后再试！");
                }
            }
            Room room = selectRoomByRoomNo(roomNo);
            RoomUser roomUser = roomUserService.selectRoomUserByUserIdAndRoomId(room.getRoomId(), userId);
            roomUser.setStatus(3);
            roomUserService.updateById(roomUser);
            return Result.success("移除成功！");
        } catch (TencentCloudSDKException e) {
            log.error("移除用户错误！"+e.getErrorCode(),e);
        }
        return Result.error("系统繁忙，请稍后再试！");
    }


    @Override
    public Result dismissRoom(String roomNo){
        String secretId = commonInfoService.findOne(815).getValue();
        String secretKey = commonInfoService.findOne(816).getValue();
        String sdkAppId = commonInfoService.findOne(814).getValue();
        try{
            Credential cred = new Credential(secretId, secretKey);
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("trtc.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            TrtcClient client = new TrtcClient(cred, "ap-beijing", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            DismissRoomByStrRoomIdRequest req = new DismissRoomByStrRoomIdRequest();
            req.setSdkAppId(Long.parseLong(sdkAppId));
            req.setRoomId(roomNo);
            // 返回的resp是一个DismissRoomByStrRoomIdResponse的实例，与请求对象对应
            DismissRoomByStrRoomIdResponse resp = client.DismissRoomByStrRoomId(req);
            String s = RemoveUserByStrRoomIdResponse.toJsonString(resp);
            JSONObject jsonObject=JSONObject.parseObject(s);
            JSONObject response = jsonObject.getJSONObject("Response");
            if(response!=null){
                JSONObject error = response.getJSONObject("Error");
                if(error!=null){
                    String message = error.getString("Message");
                    log.error("解散异常："+message);
                    return Result.error("解散房间失败，请稍后再试！");
                }
            }
            Room room = selectRoomByRoomNo(roomNo);
            room.setStatus(2);
            baseMapper.updateById(room);
            return Result.success("解散成功！");
        } catch (TencentCloudSDKException e) {
            log.error("解散房间错误！"+e.getErrorCode(),e);
        }
        return Result.error("系统繁忙，请稍后再试！");
    }


}
