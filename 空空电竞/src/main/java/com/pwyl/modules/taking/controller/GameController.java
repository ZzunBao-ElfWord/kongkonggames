package com.pwyl.modules.taking.controller;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.taking.entity.Game;
import com.pwyl.modules.taking.service.GameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/game")
@Api("游戏分类")
public class GameController {
    private GameService gameService;

    /**
     * 查看游戏分类
     */
    @GetMapping("/queryGameName")
    @ApiOperation("查看游戏分类")
    public Result queryAllGameName(Long page, Long limit,Integer gameType,Integer status) {
        return gameService.queryAllGameName(page, limit,gameType,status);
    }

    @PostMapping("/updateGameSort")
    @ApiOperation("修改排序")
    public Result updateGameSort(Game game){
        gameService.updateById(game);
        return Result.success();
    }


    /**
     * 添加游戏分类
     */
    @GetMapping("/addGameName")
    @ApiOperation("添加游戏分类")
    public Result addGameName(Game game) {
        return gameService.addGameName(game);
    }

    /**
     * 修改游戏分类
     */
    @GetMapping("/updateGameName")
    @ApiOperation("修改游戏分类")
    public Result updateGameName(Game game) {
        return gameService.updateGameName(game);
    }

    /**
     * 删除游戏分类
     */
    @GetMapping("/deleteGameName")
    @ApiOperation("删除游戏分类")
    public Result deleteGameName(Long id) {
        return gameService.deleteGameName(id);
    }

    /**
     * 查看启用的游戏分类
     */
    @GetMapping("/queryGame")
    @ApiOperation("查看启用的游戏分类")
    public Result queryGameName() {
        return gameService.queryGameName();
    }

    /**
     * 是否启用游戏分类
     */
    @GetMapping("/enableGameName")
    @ApiOperation("是否启用游戏分类")
    public Result enableGameName(Long status, Long id) {
        if (status == null || id == null) {
            return Result.error("启用分类的条件为空");
        } else {
            return gameService.enableGameName(status, id);
        }
    }


}
