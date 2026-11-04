package com.pwyl.modules.taking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.taking.entity.Game;

public interface GameService extends IService<Game> {

    /**
     * 查询首页游戏分类
     *
     * @return
     */
    Result queryGameName();

    Result selectGameList();


    /**
     * 添加游戏分类
     *
     * @param
     * @return
     */
    Result addGameName(Game game);

    /**
     * 修改游戏分类
     *
     * @param
     * @return
     */
    Result updateGameName(Game game);

    /**
     * 删除游戏分类
     *
     * @param id
     * @return
     */
    Result deleteGameName(Long id);

    /**
     * 查看所有游戏分类信息
     *
     * @param page
     * @param limit
     * @return
     */
    Result queryAllGameName(Long page, Long limit,Integer gameType,Integer status);

    /**
     * 是否启用游戏分类
     */
    Result enableGameName(Long status, Long id);
}
