package com.pwyl.modules.app.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwyl.modules.app.entity.Risk;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;


@Mapper
public interface RiskDao extends BaseMapper<Risk> {

    IPage<Risk> selectRiskList(Page<Risk> page,String userName,String phone,Integer riskType);

    IPage<Map<String,Object>> selectRiskGroupList(Page<Map<String,Object>> page, String userName, String phone,Integer riskSort);

}
