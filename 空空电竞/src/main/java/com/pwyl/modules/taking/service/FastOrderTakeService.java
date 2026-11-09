package com.pwyl.modules.taking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.taking.entity.FastOrder;
import com.pwyl.modules.taking.entity.FastOrderTake;
import com.pwyl.modules.taking.entity.OrderTaking;

import java.util.List;

public interface FastOrderTakeService extends IService<FastOrderTake> {

    Result insertFastOrderTake(FastOrderTake fastOrderTake);

    Result cancelFastOrderTake(Long fastOrderTakeId);

    Result selectFastOrderTakeList(Long fastOrderId);

    Result selectFastOrderTakeLists(Long fastOrderId);

    Result selectFastOrderTakePage(Integer page,Integer limit,FastOrder fastOrder);

}

