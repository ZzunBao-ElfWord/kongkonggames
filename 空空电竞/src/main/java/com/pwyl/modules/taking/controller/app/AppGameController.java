package com.pwyl.modules.taking.controller.app;

import com.pwyl.common.utils.Result;
import com.pwyl.modules.taking.service.GameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("app/appGame")
@Api("app游戏分类")
public class AppGameController {

    private GameService gameService;

    @GetMapping("/queryGameName")
    @ApiOperation("查询首页游戏分类")
    public Result queryGameName() {
        return gameService.queryGameName();
    }


    @GetMapping("/selectGameList")
    @ApiOperation("查询所有游戏分类")
    public Result selectGameList(){
        return gameService.selectGameList();
    }





}
