package com.pwyl.modules.app.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwyl.common.utils.PageUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.dao.RiskDao;
import com.pwyl.modules.app.entity.Risk;
import com.pwyl.modules.app.service.RiskService;
import com.pwyl.modules.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("RiskService")
public class RiskServiceImpl extends ServiceImpl<RiskDao, Risk> implements RiskService {

    @Autowired
    private UserService userService;

    @Override
    public Result selectRiskList(Integer page,Integer limit, String userName, String phone, Integer riskType){
        IPage<Risk> riskIPage = baseMapper.
                selectRiskList(new Page<>(page, limit), userName, phone, riskType);
        List<Risk> records = riskIPage.getRecords();
        for(Risk risk:records){
            risk.setUser(userService.getById(risk.getUserId()));
        }
        return Result.success().put("data",new PageUtils(riskIPage));
    }


    @Override
    public Result selectRiskGroupList(Integer page,Integer limit, String userName, String phone, Integer riskSort){
        IPage<Map<String, Object>> mapIPage = baseMapper.selectRiskGroupList(new Page<>(page, limit), userName, phone, riskSort);
        List<Map<String, Object>> records = mapIPage.getRecords();
        for(Map<String, Object> risk:records){
            risk.put("user",userService.getById(Long.parseLong(String.valueOf(risk.get("userId")))));
        }
        return Result.success().put("data",new PageUtils(mapIPage));
    }






}