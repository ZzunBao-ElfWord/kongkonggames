package com.pwyl.modules.search.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwyl.modules.search.entity.Search;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SearchDao extends BaseMapper<Search> {
}
