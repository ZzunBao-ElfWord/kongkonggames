package com.pwyl.modules.pay.service;

import com.pwyl.common.utils.Result;
import com.pwyl.modules.orders.entity.Orders;

import javax.servlet.http.HttpServletRequest;


/**
 * @author fang
 * @date 2020/2/26
 */
public interface WxService {

    Result payOrder(Long id, Integer type, HttpServletRequest request) throws Exception;

    String payBack(String resXml,Integer type);

    boolean refund(Orders orders);

}