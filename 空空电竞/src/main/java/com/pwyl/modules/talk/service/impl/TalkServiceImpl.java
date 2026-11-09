package com.pwyl.modules.talk.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwyl.modules.common.dao.CommonInfoDao;
import com.pwyl.modules.talk.dao.TalkRepository;
import com.pwyl.modules.talk.entity.Talk;
import com.pwyl.modules.talk.service.CommentService;
import com.pwyl.modules.talk.service.TalkService;
import com.pwyl.modules.talk.utils.Result;
import com.pwyl.modules.talk.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 动态
 */
@Service
public class TalkServiceImpl implements TalkService {

    @Autowired
    private TalkRepository talkRepository;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommonInfoDao commonInfoDao;

    @Override
    public Talk saveTalk(Talk talk){
        talk.setStatus(1);
        talk.setShareNum(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        talk.setCreateTime(sdf.format(new Date()));
        return talkRepository.save(talk);
    }

    @Override
    public Talk updateTalk(Talk talk){
        talk.setStatus(1);
        return talkRepository.save(talk);
    }

    @Override
    public Result updateTalkStatus(Long talkId){
        Talk talk = talkRepository.findById(talkId).orElse(null);
        if(talk.getStatus().equals(2)){
            talk.setStatus(3);
        }else{
            talk.setStatus(1);
        }
        talkRepository.save(talk);
        return ResultUtil.success();
    }

    @Override
    public Result updateTalkStatus(Long talkId,Integer status,String auditContent){
        Talk talk = talkRepository.findById(talkId).orElse(null);
        talk.setStatus(status);
        talk.setAuditContent(auditContent);
        talkRepository.save(talk);
        return ResultUtil.success();
    }

    @Override
    public Result deleteTalk(Long talkId){
        Talk talk = talkRepository.findById(talkId).orElse(null);
        talk.setIsDelete(1);
        talkRepository.save(talk);
        return ResultUtil.success();
    }


    @Override
    public void deleteTalkById(Long id){
        talkRepository.deleteById(id);
    }

    @Override
    public Map<String,Object> selectTalkPage(Integer page,Integer size,Integer status,Long userId,Long byUserId,Long followUserId,
                                             Integer isDelete,String nickName,String content,Integer videoOnly){
        Map<String,Object> result=new HashMap<>();
        page=page+1;
        IPage<Map<String, Object>> mapIPage = commonInfoDao.selectTalkPage(new Page<>(page, size), status, userId, byUserId,
                followUserId,isDelete,nickName,content,videoOnly);
        List<Map<String, Object>> contents = mapIPage.getRecords();
        List<Map<String, Object>> list=new ArrayList<>();
        for(int i =0;i<contents.size();i++){
            Map<String,Object> newMap=new HashMap<>();
            Map<String, Object> map=contents.get(i);
            newMap.putAll(map);
            newMap.put("commentPage",commentService.selectCommentPage(0,4,null,Long.parseLong(String.valueOf(map.get("talkId")))));
            list.add(newMap);
        }
        result.put("list",list);
        result.put("page",mapIPage);
        return result;

    }

    @Override
    public Map<String,Object> selectTalkByTalkId(Long talkId,Integer status,Long userId,Long byUserId){
        return talkRepository.selectTalkByTalkId(talkId, status==null?0:status, userId==null?0:userId, byUserId);
    }

    @Override
    public Talk selectById(Long talk){
        return talkRepository.findById(talk).orElse(null);
    }

    @Override
    public int addShare(Long talkId){
        return talkRepository.updateAddShareNum(talkId);
    }





}
