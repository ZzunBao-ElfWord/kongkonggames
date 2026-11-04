package com.pwyl.modules.app.controller.app;

import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.annotation.Login;
import com.pwyl.modules.app.entity.VipDetails;
import com.pwyl.modules.app.service.VipDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/VipDetails")
@AllArgsConstructor
@Api("app 会员详情信息")
public class AppVipDetailsController {
    private VipDetailsService appVipDetailsService;
    /**
     * 查询会员的详情信息
     *
     * @return
     */
    @Login
    @ApiParam("查询会员的详情信息")
    @GetMapping("/selectVipDetails")
    public Result selectVipDetails() {
        return appVipDetailsService.selectVipDetails();
    }

    /**
     * 添加会员的详情信息
     *
     * @return
     */
    @Login
    @ApiParam("添加会员的详情信息")
    @GetMapping("/insertVipDetails")
    public Result insertVipDetails(VipDetails vipDetails) {
        return appVipDetailsService.insertVipDetails(vipDetails);

    }
}


