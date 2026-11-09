package com.pwyl.modules.pay.service;


import com.pwyl.common.utils.PageUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.pay.entity.CashOut;
import com.pwyl.modules.utils.excel.ExcelData;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CashOutService {

    PageUtils selectCashOutList(Integer page, Integer limit, String zhifubaoName, String zhifubao, String userId,Integer classify,Integer state,Integer cashType, String startTime, String endTime);

    ExcelData excelPayDetails(String zhifubaoName, String zhifubao, String userId, Integer classify, Integer state, Integer cashType, String startTime, String endTime);

    int saveBody(CashOut cashOut);

    int update(CashOut cashOut);

    CashOut selectById(Long id);

    void cashOutSuccess(UserEntity userByWxId, String date, String money, String payWay, String url);

    List<CashOut> selectCashOutLimit3();

    void refundSuccess(UserEntity userByWxId, String date, String money, String url, String content);

    Double selectCashOutSum(Long userId, Date startTime, Date endTime);

    Double sumMoney(String time, Integer flag);

    Integer countMoney(String time, Integer flag);

    Integer stayMoney(String time, Integer flag);

    void updateMayMoney(int i, Long userId, Double money);

    Result cashMoney(Long userId, Double money,Integer classify);

}
