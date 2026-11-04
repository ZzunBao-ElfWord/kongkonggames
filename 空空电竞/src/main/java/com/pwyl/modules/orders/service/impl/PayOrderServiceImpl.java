package com.pwyl.modules.orders.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.common.service.CommonInfoService;
import com.pwyl.modules.orders.dao.PayOrderDao;
import com.pwyl.modules.orders.entity.PayOrder;
import com.pwyl.modules.orders.service.PayOrderService;
import com.pwyl.modules.pay.entity.PayClassify;
import com.pwyl.modules.pay.service.PayClassifyService;
import com.pwyl.modules.utils.AliPayOrderUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class PayOrderServiceImpl extends ServiceImpl<PayOrderDao, PayOrder> implements PayOrderService {

    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private PayClassifyService payClassifyService;

    @Override
    public Result insertOrder(Long userId, BigDecimal money, Long payClassifyId) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //创建订单模板
        PayOrder order = new PayOrder();
        if (payClassifyId != null) {
            PayClassify byId = payClassifyService.getById(payClassifyId);
            if (byId == null) {
                return Result.error("金额错误，请刷新页面后重试！");
            }
            order.setPayMoney(byId.getPrice());
            order.setMoney(byId.getMoney());
            order.setGiveMoney(byId.getGiveMoney());
        } else {
            if (money.doubleValue() <= 0) {
                return Result.error("金额错误，请重新输入！");
            }
            //设置充值金额
            order.setPayMoney(money);
            //查询金币比例
            String scale = commonInfoService.findOne(142).getValue();
            //计算
            BigDecimal sumMoney = (money.multiply(new BigDecimal(scale))).setScale(2, BigDecimal.ROUND_HALF_UP);
            //设置金币金额
            order.setMoney(money);
            BigDecimal giveMoney = sumMoney.subtract(money);
            order.setGiveMoney(giveMoney);
        }
        //订单编号
        order.setOrdersNo(AliPayOrderUtil.createOrderId());
        //状态
        order.setState(0);
        //用户id
        order.setUserId(userId);
        //设置创建时间
        order.setCreateTime(simpleDateFormat.format(new Date()));
        //插入到表中
        baseMapper.insert(order);

        return Result.success().put("data", order);
    }
}
