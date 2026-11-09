package com.pwyl.modules.talk.controller;


import com.pwyl.modules.talk.entity.*;
import com.pwyl.modules.talk.service.*;
import com.pwyl.modules.talk.utils.Result;
import com.pwyl.modules.talk.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "论坛", tags = {"论坛"})
@RequestMapping(value = "/gambit")
public class GambitController {

    @Autowired
    private TalkService talkService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private GoodTalkService goodTalkService;
    @Autowired
    private TalkRewardService talkRewardService;


    @PostMapping("/saveTalk")
    @ApiOperation("发布动态")
    public Result saveTalk(@RequestBody Talk talk){
        talkService.saveTalk(talk);
        return ResultUtil.success();
    }

    @PostMapping("/updateTalkStatus")
    @ApiOperation("修改动态状态")
    public Result updateTalkStatus(Long talkId){
        talkService.updateTalkStatus(talkId);
        return ResultUtil.success();
    }

    @PostMapping("/auditTalkStatus")
    @ApiOperation("修改动态状态")
    public Result updateTalkStatus(Long talkId,Integer status,String auditContent){
        talkService.updateTalkStatus(talkId,status,auditContent);
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
    public Result selectTalkPage(Integer page, Integer size, Integer status, Long userId, Long byUserId,
                                 Long followUserId,Integer isDelete,String nickName,String content,Integer videoOnly){
        return ResultUtil.success(talkService.selectTalkPage(page, size,status,userId ,byUserId,followUserId,isDelete,nickName,content,videoOnly));
    }

    @PostMapping("/deleteTalk")
    @ApiOperation("用户端假删除状态")
    public Result deleteTalk(Long talkId){
        talkService.deleteTalk(talkId);
        return ResultUtil.success();
    }

    @GetMapping("/selectTalkByTalkId")
    @ApiOperation("查询动态详情")
    public Result selectTalkPage(Long talkId, Integer status, Long userId, Long byUserId){
        return ResultUtil.success(talkService.selectTalkByTalkId(talkId,status,userId ,byUserId));
    }


    @PostMapping("/saveComment")
    @ApiOperation("发布评论")
    public Result saveComment(@RequestBody Comment comment){
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
