package com.pwyl.modules.orders.service;

import com.pwyl.common.utils.Result;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.math.BigDecimal;

public interface PayOrderService {
    /**
     * 生成充值订单
     * @param userId
     * @param money
     * @return
     */
    Result insertOrder( Long userId, BigDecimal money,Long payClassifyId);
}
