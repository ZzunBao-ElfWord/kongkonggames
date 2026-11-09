package com.pwyl.modules.orders.controller.app;

import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.annotation.Login;
import com.pwyl.modules.orders.service.PayOrderService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/app/appOrder")
@Api(value = "app充值订单", tags = {"app充值订单"})
public class AppPayOrderController {

    @Autowired
    private PayOrderService orderService;

    /**
     * app充值订单
     *
     * @param userId
     * @param money
     * @return
     */
    @Login
    @RequestMapping(value = "insertOrder", method = RequestMethod.POST)
    public Result insertOrder(@RequestAttribute Long userId, BigDecimal money, Long payClassifyId) {
        return orderService.insertOrder(userId, money, payClassifyId);
    }


}
