package com.pwyl.modules.talk.controller.app;


import com.pwyl.common.utils.DateUtils;
import com.pwyl.modules.app.annotation.Login;
import com.pwyl.modules.app.entity.Risk;
import com.pwyl.modules.app.service.RiskService;
import com.pwyl.modules.talk.entity.Comment;
import com.pwyl.modules.talk.entity.GoodTalk;
import com.pwyl.modules.talk.entity.Talk;
import com.pwyl.modules.talk.entity.TalkReward;
import com.pwyl.modules.talk.service.CommentService;
import com.pwyl.modules.talk.service.GoodTalkService;
import com.pwyl.modules.talk.service.TalkRewardService;
import com.pwyl.modules.talk.service.TalkService;
import com.pwyl.modules.talk.utils.Result;
import com.pwyl.modules.talk.utils.ResultUtil;
import com.pwyl.modules.utils.SenInfoCheckUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@Api(value = "论坛", tags = {"论坛"})
@RequestMapping(value = "/app/gambit")
public class AppGambitController {

    @Autowired
    private TalkService talkService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private GoodTalkService goodTalkService;
    @Autowired
    private TalkRewardService talkRewardService;
    @Autowired
    private RiskService riskService;


    @PostMapping("/saveTalk")
    @ApiOperation("发布动态")
    @Login
    public Result saveTalk(@RequestAttribute Long userId, @RequestBody Talk talk){
        talk.setUserId(userId);
        if(!SenInfoCheckUtil.contentFilter(talk.getContent())){
            Risk risk=new Risk();
            risk.setUserId(talk.getUserId());
            risk.setContent("系统检测触发关键词："+talk.getContent());
            risk.setRiskType(2);
            risk.setCreateTime(DateUtils.format(new Date()));
            riskService.save(risk);
            return ResultUtil.error(-200,"内容存在违规信息，请检查后重新提交！");
        }
        if(StringUtils.isNotEmpty(talk.getImg())){
            for(String img:talk.getImg().split(",")){
                if(!img.contains("mp4")){
                    if(!SenInfoCheckUtil.imgFilter(SenInfoCheckUtil.createFileItem(img,null))){
                        Risk risk=new Risk();
                        risk.setUserId(talk.getUserId());
                        risk.setContent("系统检测图片信息违规："+img);
                        risk.setRiskType(2);
                        risk.setCreateTime(DateUtils.format(new Date()));
                        riskService.save(risk);
                        return ResultUtil.error(-200,"图片存在违规信息，请检查后重新提交！");
                    }
                }

            }
        }

        talkService.saveTalk(talk);
        return ResultUtil.success();
    }

    @PostMapping("/updateTalk")
    @ApiOperation("修改动态")
    public Result updateTalk(@RequestBody Talk talk){
        if(!SenInfoCheckUtil.contentFilter(talk.getContent())){
            Risk risk=new Risk();
            risk.setUserId(talk.getUserId());
            risk.setContent("系统检测触发关键词："+talk.getContent());
            risk.setRiskType(2);
            risk.setCreateTime(DateUtils.format(new Date()));
            riskService.save(risk);
            return ResultUtil.error(-200,"内容存在违规信息，请检查后重新提交！");
        }
        if(StringUtils.isNotEmpty(talk.getImg())){
            for(String img:talk.getImg().split(",")){
                if(!img.contains("mp4")){
                    if(!SenInfoCheckUtil.imgFilter(SenInfoCheckUtil.createFileItem(img,null))){
                        Risk risk=new Risk();
                        risk.setUserId(talk.getUserId());
                        risk.setContent("系统检测图片信息违规："+img);
                        risk.setRiskType(2);
                        risk.setCreateTime(DateUtils.format(new Date()));
                        riskService.save(risk);
                        return ResultUtil.error(-200,"图片存在违规信息，请检查后重新提交！");
                    }
                }

            }
        }
        talkService.updateTalk(talk);
        return ResultUtil.success();
    }


    @PostMapping("/updateTalkStatus")
    @ApiOperation("修改动态状态")
    public Result updateTalkStatus(Long talkId){
        talkService.updateTalkStatus(talkId);
        return ResultUtil.success();
    }

    @PostMapping("/deleteTalk")
    @ApiOperation("用户端假删除状态")
    public Result deleteTalk(Long talkId){
        talkService.deleteTalk(talkId);
        return ResultUtil.success();
    }

    @PostMapping("/deleteTalkById")
    @ApiOperation("删除动态")
    public Result deleteTalkById(Long talkId){
        talkService.deleteTalkById(talkId);
        return ResultUtil.success();
    }

    @GetMapping("/selectTalkPage")
    @ApiOperation("查询动态")
    public Result selectTalkPage(Integer page, Integer size, Integer status, Long userId, Long byUserId, Long followUserId,
                                 Integer isDelete,String nickName,String content,Integer videoOnly){
        return ResultUtil.success(talkService.selectTalkPage(page, size,status,userId ,byUserId,followUserId,isDelete,nickName,content,videoOnly));
    }

    @GetMapping("/selectById")
    @ApiOperation("查询动态详情")
    public Result selectById(Long talkId){
        return ResultUtil.success(talkService.selectById(talkId));
    }

    @GetMapping("/selectTalkByTalkId")
    @ApiOperation("查询动态详情")
    public Result selectTalkPage(Long talkId, Integer status, Long userId, Long byUserId){
        return ResultUtil.success(talkService.selectTalkByTalkId(talkId,status,userId ,byUserId));
    }

    @GetMapping("/selectCommentPageAdmin")
    @ApiOperation("查询评论")
    public Result selectCommentPageAdmin(Integer page, Integer limit, Long userId, Long talkId,String userName,String phone,String content){
        return ResultUtil.success(commentService.selectCommentPage(page,limit,userId,talkId,userName,phone,content));
    }


    @PostMapping("/saveComment")
    @ApiOperation("发布评论")
    public Result saveComment(@RequestBody Comment comment){
        if(!SenInfoCheckUtil.contentFilter(comment.getContent())){
            Risk risk=new Risk();
            risk.setUserId(comment.getUserId());
            risk.setContent("系统检测触发关键词："+comment.getContent());
            risk.setRiskType(3);
            risk.setCreateTime(DateUtils.format(new Date()));
            riskService.save(risk);
            return ResultUtil.error(-200,"内容存在违规信息，请检查后重新提交！");
        }
        commentService.saveComment(comment);
        return ResultUtil.success();
    }

    @PostMapping("/deleteCommentById")
    @ApiOperation("删除评论")
    public Result deleteCommentById(Long commentId){
        commentService.deleteComment(commentId);
        return ResultUtil.success();
    }

    @PostMapping("/saveGoodTalk")
    @ApiOperation("点赞动态")
    public Result saveGoodTalk(@RequestBody GoodTalk goodTalk){
        goodTalkService.saveGoodTalk(goodTalk);
        return ResultUtil.success();
    }


    @GetMapping("/selectCommentPage")
    @ApiOperation("查询评论")
    public Result selectCommentPage(Integer page, Integer size, Long userId, Long talkId){
        return ResultUtil.success(commentService.selectCommentPage(page,size,userId,talkId));
    }

    @GetMapping("/selectCommentParentIdPage")
    @ApiOperation("查询评论详情")
    public Result selectCommentParentIdPage(Integer page, Integer size, Long userId, Long talkId, Long parentId){
        return ResultUtil.success(commentService.selectCommentParentIdPage(page,size,userId,talkId,parentId));
    }

    @GetMapping("/addShare")
    @ApiOperation("分享")
    public Result addShare(Long talkId){
        talkService.addShare(talkId);
        return ResultUtil.success();
    }

    @PostMapping("/insertTalkReward")
    @ApiOperation("打赏动态")
    public Result insertTalkReward(@RequestBody TalkReward talkReward){
        return talkRewardService.insertTalkReward(talkReward);
    }

    @PostMapping("/deleteTalkRewardById")
    @ApiOperation("删除打赏记录")
    public Result deleteTalkRewardById(Long rewardId){
        talkRewardService.deleteTalkReward(rewardId);
        return ResultUtil.success();
    }

    @GetMapping("/selectTalkRewardPage")
    @ApiOperation("查看打赏记录")
    public Result selectTalkRewardPage(Integer page, Integer size, Long talkId){
        return ResultUtil.success(talkRewardService.selectTalkRewardPage(page, size, talkId));
    }


}
