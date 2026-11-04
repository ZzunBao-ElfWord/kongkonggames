package com.pwyl.modules.taking.controller.app;

import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.annotation.Login;
import com.pwyl.modules.taking.service.OrderTakingCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/app/takingComment")
@Api(value = "APP评论|点赞", tags = {"APP评论|点赞"})
public class AppOrderTakingCommentController {

    private OrderTakingCommentService orderTakingCommentService;

    /**
     * 查看接单下的所有评论内容  时间  评论人  评论人图像  评论点赞次数
     *
     * @param page
     * @param limit
     * @param id
     */
    @CrossOrigin
    @GetMapping("/selectOrderTakingComment")
    @ApiOperation("查看评论")
    public Result selectOrderTakingComment(Integer page, Integer limit, Long id,String userName,String phone,String content) {
        return orderTakingCommentService.selectOrderTakingComment(page, limit, id,userName,phone,content,null);
    }

    /**
     * 有赞时取消点赞  没赞时点赞
     *
     * @param commentId
     * @param userId
     * @return
     */
    @Login
    @GetMapping("/updateGoodsNum")
    @ApiOperation("点赞评论")
    public Result updateGoodsNum(Long commentId, @RequestAttribute("userId") Long userId) {
        return orderTakingCommentService.updateGoodsNum(commentId, userId);
    }

    @Login
    @GetMapping("/selectCommentByOrderTakingUserId")
    @ApiOperation("查看我发布的服务的评论")
    public Result selectCommentByOrderTakingUserId(Integer page, Integer limit,@RequestAttribute Long userId) {
        return orderTakingCommentService.selectCommentByOrderTakingUserId(page, limit, userId);
    }

    @Login
    @GetMapping("/selectCommentByUserId")
    @ApiOperation("查看我发布的评论")
    public Result selectCommentByUserId(Integer page, Integer limit,@RequestAttribute Long userId) {
        return orderTakingCommentService.selectCommentByUserId(page, limit, userId);
    }

    @Login
    @PostMapping("/deleteOrderTakingComment")
    @ApiOperation("删除评论")
    public Result deleteOrderTakingComment(Long id) {
        orderTakingCommentService.removeById(id);
        return Result.success();
    }

    /**
     * 添加评论
     */
    @Login
    @PostMapping("/addGoodsNum")
    @ApiOperation("添加评论")
    public Result addGoodsNum(Long id, @RequestAttribute("userId") Long userId, String content, Integer score,Long ordersId) {
        return orderTakingCommentService.addGoodsNum(id, userId, content, score,ordersId);
    }

    @Login
    @GetMapping("/selectTakingCommentCount")
    @ApiOperation("查询评论次数")
    public Result selectTakingCommentCount(Long ordersId, @RequestAttribute("userId") Long userId) {
        return Result.success().put("data",orderTakingCommentService.selectTakingCommentCount(ordersId, userId));
    }


}
