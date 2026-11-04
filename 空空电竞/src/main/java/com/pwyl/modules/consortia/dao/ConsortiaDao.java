package com.pwyl.modules.consortia.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwyl.modules.consortia.entity.Consortia;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ConsortiaDao extends BaseMapper<Consortia> {

    int insertConsortia(Consortia consortia);

    IPage<Consortia> selectConsortiaList(Page<Consortia> page, String consortiaName, String userName,String phone);

}
