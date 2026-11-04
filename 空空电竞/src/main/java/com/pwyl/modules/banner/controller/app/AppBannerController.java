package com.pwyl.modules.banner.controller.app;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.banner.service.BannerService;
import com.pwyl.modules.taking.response.OrderTakingResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author liyuan
 * @date 2021/8/9
 */
@Slf4j
@RestController
@Api(value = "app banner图", tags = {"app banner图"})
@RequestMapping(value = "/app/banner")
public class AppBannerController {


    @Autowired
    private BannerService bannerService;

    @RequestMapping(value = "/selectBannerList", method = RequestMethod.GET)
    @ApiOperation("查询所有banner图")
    @ResponseBody
    public Result selectBannerList(Integer classify) {
        return Result.success().put("data", bannerService.selectBannerList(classify));
    }


}