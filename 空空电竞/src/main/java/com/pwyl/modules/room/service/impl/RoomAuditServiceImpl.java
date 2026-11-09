package com.pwyl.modules.room.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwyl.common.utils.DateUtils;
import com.pwyl.common.utils.PageUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.room.dao.RoomAuditDao;
import com.pwyl.modules.room.entity.Room;
import com.pwyl.modules.room.entity.RoomAudit;
import com.pwyl.modules.room.service.RoomAuditService;
import com.pwyl.modules.room.service.RoomService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 房间
 */
@Service
public class RoomAuditServiceImpl extends ServiceImpl<RoomAuditDao, RoomAudit> implements RoomAuditService {

    @Autowired
    private RoomService roomService;


    @Override
    public String notifyRoomAudit(JSONObject jsonObject){
        String suggestion = jsonObject.getString("Suggestion");
        JSONArray labelsArray = jsonObject.getJSONArray("Labels");
        StringBuilder labels=new StringBuilder();
        if(labelsArray!=null && labelsArray.size()>0){
            for (int i=0;i<labelsArray.size();i++){
                JSONObject jsonObject1 = labelsArray.getJSONObject(i);
                labels.append(jsonObject1.getString("Label"));
                if((i+1)<labelsArray.size()){
                    labels.append(",");
                }
            }
        }
        JSONArray imageSegmentsArray = jsonObject.getJSONArray("AudioSegments");
        StringBuilder urls=new StringBuilder();
        StringBuilder texts=new StringBuilder();
        if(imageSegmentsArray!=null && imageSegmentsArray.size()>0){
            for (int i=0;i<imageSegmentsArray.size();i++){
                JSONObject jsonObject1 = imageSegmentsArray.getJSONObject(i);
                JSONObject result = jsonObject1.getJSONObject("Result");
                if(result!=null){
                    String url = result.getString("Url");
                    String text = result.getString("Text");
                    urls.append(url);
                    texts.append(text);
                    if((i+1)<imageSegmentsArray.size()){
                        urls.append(",");
                        texts.append(",");
                    }

                }
            }
        }


        //根据url获取信息 segment-trtc/{sdk_app_id}/audio_{room_id}_{user_id}_{timestamp}.mp3（音频格式）
        String url = urls.toString();
        if(!"Pass".equals(suggestion) &&StringUtils.isNotEmpty(url)){
            RoomAudit roomAudit=new RoomAudit();
            String[] split = url.split(",");
            String urlStr = split[0];
            String appId=urlStr.substring(urlStr.indexOf("segment-trtc/")+13,urlStr.indexOf("/audio_"));
            String audio=urlStr.substring(urlStr.indexOf("/audio_")+7,urlStr.indexOf(".mp3"));
            String[] s1 = audio.split("_");
            String roomNo=s1[0];
            String userId=s1[1];
            String timestamp=s1[2];
            roomAudit.setRoomNo(roomNo);
            roomAudit.setUserId(Long.parseLong(userId));
            Room room = roomService.selectRoomByRoomNo(roomNo);
            roomAudit.setRoomId(room.getRoomId());
            roomAudit.setRoomUserId(room.getUserId());
            roomAudit.setUrlTime(DateUtils.format(new Date(Long.parseLong(String.valueOf(timestamp+"000")))));
            roomAudit.setSuggestion(suggestion);
            roomAudit.setLabels(labels.toString());
            roomAudit.setUrl(url);
            roomAudit.setStatus(1);
            roomAudit.setAudioText(texts.toString());
            roomAudit.setCreateTime(DateUtils.format(new Date()));
            baseMapper.insert(roomAudit);
        }

//
        return "success";
    }

    @Override
    public Result selectRoomAuditList(Integer page,Integer limit,Long userId,Long roomUserId, String roomName, String roomNo,
                                      String roomUserName,String roomPhone, String userName,String phone,Integer status){
        return Result.success().put("data",new PageUtils(baseMapper.selectRoomAuditList(new Page<>(page,limit),userId,roomUserId,
                roomName,roomNo,roomUserName,roomPhone,userName,phone,status)));
    }

    @Override
    public Result auditRoomAudit(String ids,Integer status,Integer auditType,String auditContent){
        for (String id:ids.split(",")){
            try {
                RoomAudit roomAudit = baseMapper.selectById(id);
                if(roomAudit.getStatus()==1){
                    roomAudit.setAuditTime(DateUtils.format(new Date()));
                    if(status==1){
                        roomAudit.setStatus(2);
                        roomAudit.setAuditType(auditType);
                        roomAudit.setAuditContent(auditContent);
                        if(auditType==1){
                            //封禁直播间 前端已处理
                        }else if(auditType==2){
                            //封号
                            //踢出直播间
                            roomService.roomRemoveUser(roomAudit.getRoomNo(),roomAudit.getUserId());
                        }else{
                            //警告 前端已处理
                        }
                    }else{
                        roomAudit.setStatus(3);
                    }
                    baseMapper.updateById(roomAudit);
                }
            }catch (Exception e){
                e.printStackTrace();
                log.error("审核直播间违规异常："+e.getMessage(),e);
            }

        }
        return Result.success();
    }


}
