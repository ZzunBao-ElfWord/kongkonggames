package com.pwyl.modules.app.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.entity.UserCertification;
import com.pwyl.modules.app.service.UserCertificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/userCertification")
@Api("实 名 认 证")
public class UserCertificationController {
    private UserCertificationService userCertificationService;


    /**
     * 实名列表
     */
    @RequestMapping("/queryCertification")
    @ApiOperation("实名列表")
    public Result queryCertification(Long page, Long limit, @ApiParam("状态") String status, @ApiParam("姓名") String name) {

        return userCertificationService.queryCertification(page, limit, status, name);
    }

    @PostMapping("/updateCertification")
    @ApiOperation("修改实名认证信息")
    public Result updateCertification(@RequestBody UserCertification userCertification){
        userCertificationService.updateById(userCertification);
        return Result.success();
    }


    @PostMapping("/deleteCertification")
    @ApiOperation("删除实名认证信息")
    public Result deleteCertification(Long id){
        userCertificationService.removeById(id);
        return Result.success();
    }

    /**
     * 查询已经进行实名的用户
     */
    @RequestMapping("/queryUserCertification")
    @ApiOperation("查询已经进行实名的用户")
    public Result queryUserCertification(Long page, Long limit, String name,String phone) {
        if (page == null || limit == null) {
            return Result.error("分页条件为空");
        } else {
            IPage<UserCertification> iPage = new Page<>(page, limit);

            return userCertificationService.queryUserCertification(iPage,name,phone);
        }
    }

    /**
     * 审核实名认证
     */
    @RequestMapping("/auditorUserCertification")
    @ApiOperation("审核实名认证")
    public Result auditorUserCertification(Long status, Long userId, String remek) {
        if (status == null || userId == null) {
            return Result.error("审核入参为空");
        } else {
            return userCertificationService.auditorUserCertification(status, userId, remek);
        }
    }


}
