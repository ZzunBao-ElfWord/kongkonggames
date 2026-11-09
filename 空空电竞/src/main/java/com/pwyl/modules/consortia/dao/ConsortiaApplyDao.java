package com.pwyl.modules.consortia.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwyl.modules.consortia.entity.ConsortiaApply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ConsortiaApplyDao extends BaseMapper<ConsortiaApply> {

    IPage<ConsortiaApply> selectConsortiaApplyList(Page<ConsortiaApply> page,@Param("consortiaId") Long consortiaId,@Param("status") Integer status,@Param("classify") Integer classify);

}
