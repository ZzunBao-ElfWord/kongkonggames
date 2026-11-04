package com.pwyl.modules.app.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.entity.VipDetails;

public interface VipDetailsService extends IService<VipDetails> {
    /**
     * 查询会员的详情信息
     *
     * @return
     */
    Result selectVipDetails();

    /**
     * 添加会员的详情信息
     *
     * @return
     */
    Result insertVipDetails(VipDetails vipDetails);

}
