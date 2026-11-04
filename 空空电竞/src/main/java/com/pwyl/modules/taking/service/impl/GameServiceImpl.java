package com.pwyl.modules.taking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.taking.dao.GameDao;
import com.pwyl.modules.taking.entity.Game;
import com.pwyl.modules.taking.service.GameService;
import com.pwyl.modules.taking.service.OrderTakingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GameServiceImpl extends ServiceImpl<GameDao, Game> implements GameService {

    @Autowired
    private OrderTakingService orderTakingService;


    @Override
    public Result queryGameName() {
        QueryWrapper<Game> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 0).orderByAsc("sort");
        List<Game> games = baseMapper.selectList(queryWrapper);
        return Result.success().put("data", games);
    }


    @Override
    public Result selectGameList(){
        List<Integer> integers = baseMapper.selectGameGroupGameType();
        List<Map<String,Object>> result=new ArrayList<>();
        for(Integer gameType:integers){
            List<Game> games = baseMapper.selectList(new QueryWrapper<Game>().eq("game_type", gameType).eq("status", "0").orderByAsc("sort"));
            Map<String,Object> map=new HashMap<>();
            map.put("gameType",gameType);
            map.put("games",games);
            result.add(map);
        }
        return Result.success().put("data",result);
    }



    @Override
    public Result addGameName(Game game) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        game.setCreateTime(simpleDateFormat.format(new Date()));
        game.setUpdateTime(simpleDateFormat.format(new Date()));
        game.setStatus((long) 0);
        int i = baseMapper.insert(game);
        if (i > 0) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    @Override
    public Result updateGameName(Game game) {
        Game game1 = baseMapper.selectById(game.getId());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        game.setUpdateTime(simpleDateFormat.format(new Date()));
        int i = baseMapper.updateById(game);
        if (i > 0) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    @Override
    public Result deleteGameName(Long id) {

        Game game = baseMapper.selectById(id);
        if (game != null) {
            baseMapper.deleteById(id);
            return Result.success();
        } else {
            return Result.error("游戏信息不存在！");

        }
    }

    @Override
    public Result queryAllGameName(Long page, Long limit,Integer gameType,Integer status) {
        Page<Game> page1 = new Page<>(page, limit);
        return Result.success().put("data", baseMapper.selectPage(page1,
                new QueryWrapper<Game>()
                        .eq(status!=null && status!=-1,"status",status)
                        .eq(gameType!=null,"game_type",gameType).orderByAsc("sort")));
    }

    @Override
    public Result enableGameName(Long status, Long id) {
        Game game = baseMapper.selectById(id);
        if (game == null) {
            return Result.error("游戏分类信息不存在");
        } else {
            game.setStatus(status);
            game.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            baseMapper.updateById(game);
            return Result.success();
        }

    }


}
