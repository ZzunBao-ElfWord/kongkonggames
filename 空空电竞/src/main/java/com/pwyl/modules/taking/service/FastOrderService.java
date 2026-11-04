package com.pwyl.modules.taking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.taking.entity.FastOrder;

public interface FastOrderService extends IService<FastOrder> {

    Result selectMyFastCount(Long userId);

    Result insertFastOrder(FastOrder fastOrder);

    Result selectFastOrderDetails(Long fastOrderId);

    Result selectFastOrderPage(Integer page,Integer limit,Long userId,Integer status,String userName,String phone,Integer templateType);

    Result selectFastOrderTake(Integer page,Integer limit,Long userId,Integer templateType);

    Result selectFastOrderTakeListByUserId(Integer page,Integer limit,Long userId,Integer status);

    FastOrder selectFastOrder(Long userId);

    Result cancelFastOrder(Long fastOrderId);


}

