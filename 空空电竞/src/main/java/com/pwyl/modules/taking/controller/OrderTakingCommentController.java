package com.pwyl.modules.taking.controller;

import com.pwyl.common.utils.Result;
import com.pwyl.modules.taking.service.OrderTakingCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/takingComment")
@Api(value = "评论", tags = {"评论"})
public class OrderTakingCommentController {

    @Autowired
    private OrderTakingCommentService orderTakingCommentService;

    /**
     * 查看我的评论
     */
    @GetMapping("/queryMyComment")
    public Result queryMyComment(Long userId) {
        return orderTakingCommentService.queryMyComment(userId);
    }


    @GetMapping("/selectCommentByOrderTakingUserId")
    @ApiOperation("查看我发布的服务的评论")
    public Result selectCommentByOrderTakingUserId(Integer page, Integer limit, Long userId) {
        return orderTakingCommentService.selectCommentByOrderTakingUserId(page, limit, userId);
    }

    @GetMapping("/selectOrderTakingComment")
    @ApiOperation("查看评论")
    public Result selectOrderTakingComment(Integer page, Integer limit, Long id,String userName,String phone,String content,Long userId) {
        return orderTakingCommentService.selectOrderTakingComment(page, limit, id,userName,phone,content,userId);
    }

    @PostMapping("/deleteOrderTakingComment/{id}")
    @ApiOperation("删除评论")
    public Result deleteOrderTakingComment(@PathVariable Long id) {
        orderTakingCommentService.removeById(id);
        return Result.success();
    }

}
