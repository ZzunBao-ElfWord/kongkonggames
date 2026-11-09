package com.pwyl.modules.helpCenter.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.helpCenter.entity.HelpClassify;
import com.pwyl.modules.helpCenter.entity.HelpWord;
import com.pwyl.modules.helpCenter.service.HelpClassifyService;
import com.pwyl.modules.helpCenter.service.HelpWordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(value = "帮助中心", tags = {"帮助中心"})
@RequestMapping(value = "/app/help")
public class AppHelpController {


    @Autowired
    private HelpClassifyService helpClassifyService;
    @Autowired
    private HelpWordService helpWordService;

    @GetMapping("/selectHelpList")
    @ApiOperation("查询帮助列表")
    public Result selectHelpList(){
        List<HelpClassify> helpClassifyList = helpClassifyService.list(new QueryWrapper<HelpClassify>().orderByAsc("sort"));
        for(HelpClassify helpClassify:helpClassifyList){
            List<HelpWord> helpWordList = helpWordService.list(new QueryWrapper<HelpWord>().eq("help_classify_id", helpClassify.getHelpClassifyId()).orderByAsc("sort"));
            helpClassify.setHelpWordList(helpWordList);
        }
        return Result.success().put("data",helpClassifyList);
    }

    @GetMapping("/selectHelpWordDetails")
    @ApiOperation("查询文档详情")
    public Result selectHelpWordDetails(Long helpWordId){
        return Result.success().put("data",helpWordService.getById(helpWordId));
    }



}
