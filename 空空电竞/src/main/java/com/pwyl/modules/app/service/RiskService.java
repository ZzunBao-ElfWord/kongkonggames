package com.pwyl.modules.app.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.entity.Risk;


public interface RiskService extends IService<Risk> {

    Result selectRiskList(Integer page,Integer limit, String userName, String phone, Integer riskType);

    Result selectRiskGroupList(Integer page,Integer limit, String userName, String phone, Integer riskSort);

}
