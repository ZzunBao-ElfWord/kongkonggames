package com.pwyl.modules.taking.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwyl.modules.taking.entity.Game;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GameDao extends BaseMapper<Game> {

    List<Integer> selectGameGroupGameType();

}
