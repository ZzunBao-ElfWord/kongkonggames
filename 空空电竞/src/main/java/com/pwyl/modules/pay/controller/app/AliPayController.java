package com.pwyl.modules.pay.controller.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.annotation.Login;
import com.pwyl.modules.app.dao.UserMoneyDao;
import com.pwyl.modules.app.entity.UserMoney;
import com.pwyl.modules.app.entity.UserMoneyDetails;
import com.pwyl.modules.app.service.UserMoneyDetailsService;
import com.pwyl.modules.app.service.UserMoneyService;
import com.pwyl.modules.common.entity.CommonInfo;
import com.pwyl.modules.common.service.CommonInfoService;
import com.pwyl.modules.orders.dao.OrdersDao;
import com.pwyl.modules.orders.dao.PayOrderDao;
import com.pwyl.modules.orders.entity.Orders;
import com.pwyl.modules.orders.entity.PayOrder;
import com.pwyl.modules.orders.service.OrdersService;
import com.pwyl.modules.pay.config.AliPayConstants;
import com.pwyl.modules.pay.dao.PayDetailsDao;
import com.pwyl.modules.pay.entity.PayDetails;
import com.pwyl.modules.utils.AmountCalUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 支付宝支付处理--暂不做同步处理、回调方式使用异步
 */
@Slf4j
@RestController
@Api(value = "支付宝支付", tags = {"支付宝支付"})
@RequestMapping("/app/aliPay")
public class AliPayController {

    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private PayOrderDao ordersDao;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private UserMoneyDao userMoneyDao;
    @Autowired
    private PayDetailsDao payDetailsDao;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private OrdersDao membershipOrdersDao;
    private ReentrantReadWriteLock reentrantReadWriteLock=new ReentrantReadWriteLock(true);

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @RequestMapping(value = "/notifyApp", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public void notifyApp(HttpServletRequest request, HttpServletResponse response) {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        try {
            log.info("回调成功！！！");
            boolean flag = AlipaySignature.rsaCheckV1(params, commonInfoService.findOne(64).getValue(), AliPayConstants.CHARSET, "RSA2");
            log.info(flag + "回调验证信息");
            if (flag) {
                String tradeStatus = params.get("trade_status");
                if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                    reentrantReadWriteLock.writeLock().lock();
                    try{
                        //支付宝返回的订单编号
                        String outTradeNo = params.get("out_trade_no");
                        log.error(outTradeNo);
                        //支付宝支付单号
                        String tradeNo = params.get("trade_no");
                        //设置查询条件
                        QueryWrapper<PayOrder> queryWrapper = new QueryWrapper<>();
                        //根据订单编号去查询订单
                        queryWrapper.eq("orders_no", outTradeNo);
                        //去订单表中查询到该订单
                        PayOrder orders = ordersDao.selectOne(queryWrapper);
                        PayDetails payDetails = payDetailsDao.selectByOrderId(outTradeNo);
                        if (payDetails.getState() == 0) {
                            payDetailsDao.updateState(payDetails.getId(), 1, sdf.format(new Date()), "");
                            //改变订单状态
                            orders.setState(1);
                            orders.setPayWay(4);
                            //设置订单更新时间
                            orders.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                            ordersDao.updateById(orders);
                            //调用处理接口
                            userMoneyDao.updateMayMoney(1, orders.getUserId(), orders.getMoney().doubleValue());
                            UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                            userMoneyDetails.setUserId(orders.getUserId());
                            userMoneyDetails.setTitle("支付宝充值");
                            userMoneyDetails.setContent("支付宝充值:"+orders.getPayMoney()+",到账金币：" +orders.getMoney());
                            userMoneyDetails.setType(1);
                            userMoneyDetails.setMoney(orders.getMoney());
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            userMoneyDetails.setCreateTime(sdf.format(new Date()));
                            userMoneyDetailsService.save(userMoneyDetails);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        log.error("支付宝回调异常："+e.getMessage());
                    }finally {
                        reentrantReadWriteLock.writeLock().unlock();
                    }
                }
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            log.info("回调验证失败！！！");
        }
    }

    @Login
    @ApiOperation("支付宝支付订单")
    @RequestMapping(value = "/payOrder", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result payOrder(Long id) {
        //通知页面地址
        CommonInfo one = commonInfoService.findOne(19);
        String returnUrl = one.getValue() + "/#/pages/task/recharge";
        CommonInfo one3 = commonInfoService.findOne(12);
        String name = one3 == null ? "陪玩接单" : one3.getValue();
        String url = one.getValue() + "/pwyl-api/app/aliPay/notifyApp";
        log.info("回调地址:" + url);
        PayOrder orders = ordersDao.selectById(id);
        try {
            AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", commonInfoService.findOne(63).getValue(), commonInfoService.findOne(65).getValue(), "json", AliPayConstants.CHARSET, commonInfoService.findOne(64).getValue(), "RSA2");
            AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
            JSONObject order = new JSONObject();
            order.put("out_trade_no", orders.getOrdersNo()); //订单号
            order.put("subject", name); //商品标题
            order.put("product_code", "QUICK_WAP_WAY");
            order.put("body", name);//商品名称
            BigDecimal money = orders.getPayMoney();
            order.put("total_amount", money); //金额
            alipayRequest.setBizContent(order.toString());
            //在公共参数中设置回跳和通知地址
            alipayRequest.setNotifyUrl(url);
            //通知页面地址
            alipayRequest.setReturnUrl(returnUrl);
            String form = alipayClient.pageExecute(alipayRequest).getBody();
            PayDetails payDetails = payDetailsDao.selectByOrderId(orders.getOrdersNo());
            if(payDetails==null){
                payDetails = new PayDetails();
                payDetails.setState(0);
                payDetails.setCreateTime(sdf.format(new Date()));
                payDetails.setOrderId(orders.getOrdersNo());
                payDetails.setUserId(orders.getUserId());
                payDetails.setMoney(money.doubleValue());
                payDetails.setClassify(4);
                payDetailsDao.insert(payDetails);
            }
            return Result.success().put("data", form);
        } catch (AlipayApiException e) {
            log.error("CreatPayOrderForH5", e);
        }
        return Result.error("获取订单信息错误！");
    }


    @ApiOperation("充值")
    @RequestMapping(value = "/payApp",method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result payApp(Long id){
        CommonInfo one = commonInfoService.findOne(19);
        CommonInfo one3 = commonInfoService.findOne(12);
        String name=one3==null?"陪玩接单":one3.getValue();
        PayOrder orders = ordersDao.selectById(id);
        if (orders.getState().equals(1)) {
            return Result.error("订单已支付，请不要重复点击！");
        }
        String result="";
        try {
            String url=one.getValue()+"/pwyl-api/app/aliPay/notifyApp";
            //实例化客户端
            AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", commonInfoService.findOne(63).getValue(), commonInfoService.findOne(65).getValue(), "json", AliPayConstants.CHARSET, commonInfoService.findOne(64).getValue(), "RSA2");
            //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
            AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
            //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
            model.setBody(name);
            model.setSubject(name);
            model.setOutTradeNo(orders.getOrdersNo());
            model.setTimeoutExpress("30m");
            model.setTotalAmount(orders.getPayMoney().toString());
            model.setProductCode("QUICK_MSECURITY_PAY");
            request.setBizModel(model);
            request.setNotifyUrl(url);
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            if(response.isSuccess()){
                result=response.getBody();
            } else {
               return Result.error("获取订单失败！");
            }
            PayDetails payDetails = payDetailsDao.selectByOrderId(orders.getOrdersNo());
            if(payDetails==null){
                payDetails = new PayDetails();
                payDetails.setState(0);
                payDetails.setCreateTime(sdf.format(new Date()));
                payDetails.setOrderId(orders.getOrdersNo());
                payDetails.setUserId(orders.getUserId());
                payDetails.setMoney(orders.getPayMoney().doubleValue());
                payDetails.setClassify(4);
                payDetailsDao.insert(payDetails);
            }
            return Result.success().put("data",result);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return Result.error(-100,"获取订单失败！");
    }

    @Login
    @ApiOperation("安卓会员支付宝 App 支付")
    @RequestMapping(value = "/payVipApp", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result payVipApp(@RequestAttribute Long userId, Long ordersId) {
        Orders orders = membershipOrdersDao.selectById(ordersId);
        Result validation = validateVipOrder(orders, userId);
        if (validation != null) {
            return validation;
        }
        CommonInfo domain = commonInfoService.findOne(19);
        try {
            AlipayClient alipayClient = new DefaultAlipayClient(
                    AliPayConstants.REQUEST_URL,
                    commonInfoService.findOne(63).getValue(),
                    commonInfoService.findOne(65).getValue(),
                    "json", AliPayConstants.CHARSET,
                    commonInfoService.findOne(64).getValue(), "RSA2");
            AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
            model.setBody("会员服务");
            model.setSubject("会员服务");
            model.setOutTradeNo(orders.getOrdersNo());
            model.setTimeoutExpress("30m");
            model.setTotalAmount(orders.getPayMoney().toString());
            model.setProductCode("QUICK_MSECURITY_PAY");
            request.setBizModel(model);
            request.setNotifyUrl(domain.getValue() + "/pwyl-api/app/aliPay/notifyVipApp");
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            if (!response.isSuccess()) {
                return Result.error("获取会员支付订单失败");
            }
            saveVipPayDetails(orders);
            return Result.success().put("data", response.getBody());
        } catch (AlipayApiException e) {
            log.error("创建会员支付宝 App 订单失败", e);
            return Result.error("获取会员支付订单失败");
        }
    }

    @Login
    @ApiOperation("安卓会员支付宝 H5 支付")
    @RequestMapping(value = "/payVipOrder", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result payVipOrder(@RequestAttribute Long userId, Long ordersId) {
        Orders orders = membershipOrdersDao.selectById(ordersId);
        Result validation = validateVipOrder(orders, userId);
        if (validation != null) {
            return validation;
        }
        CommonInfo domain = commonInfoService.findOne(19);
        try {
            AlipayClient alipayClient = new DefaultAlipayClient(
                    AliPayConstants.REQUEST_URL,
                    commonInfoService.findOne(63).getValue(),
                    commonInfoService.findOne(65).getValue(),
                    "json", AliPayConstants.CHARSET,
                    commonInfoService.findOne(64).getValue(), "RSA2");
            AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
            JSONObject order = new JSONObject();
            order.put("out_trade_no", orders.getOrdersNo());
            order.put("subject", "会员服务");
            order.put("product_code", "QUICK_WAP_WAY");
            order.put("body", "会员服务");
            order.put("total_amount", orders.getPayMoney());
            request.setBizContent(order.toString());
            request.setNotifyUrl(domain.getValue() + "/pwyl-api/app/aliPay/notifyVipApp");
            request.setReturnUrl(domain.getValue() + "/#/my/vip/index");
            String form = alipayClient.pageExecute(request).getBody();
            saveVipPayDetails(orders);
            return Result.success().put("data", form);
        } catch (AlipayApiException e) {
            log.error("创建会员支付宝 H5 订单失败", e);
            return Result.error("获取会员支付订单失败");
        }
    }

    @ApiOperation("安卓会员支付宝异步通知")
    @RequestMapping(value = "/notifyVipApp", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public String notifyVipApp(HttpServletRequest request) {
        Map<String, String> params = readNotifyParams(request);
        try {
            boolean signatureValid = AlipaySignature.rsaCheckV1(
                    params, commonInfoService.findOne(64).getValue(),
                    AliPayConstants.CHARSET, "RSA2");
            if (!signatureValid) {
                return "failure";
            }
            String tradeStatus = params.get("trade_status");
            if (!"TRADE_SUCCESS".equals(tradeStatus) && !"TRADE_FINISHED".equals(tradeStatus)) {
                return "failure";
            }
            if (!commonInfoService.findOne(63).getValue().equals(params.get("app_id"))) {
                return "failure";
            }

            Orders orders = membershipOrdersDao.selectOne(
                    new QueryWrapper<Orders>().eq("orders_no", params.get("out_trade_no")));
            if (orders == null || orders.getOrdersType() == null || orders.getOrdersType() != 2L) {
                return "failure";
            }
            BigDecimal paidAmount = new BigDecimal(params.get("total_amount"));
            if (paidAmount.compareTo(orders.getPayMoney()) != 0) {
                log.error("会员支付宝回调金额不一致，订单: {}, 应付: {}, 实付: {}",
                        orders.getOrdersNo(), orders.getPayMoney(), paidAmount);
                return "failure";
            }

            PayDetails payDetails = payDetailsDao.selectByOrderId(orders.getOrdersNo());
            if (payDetails != null && payDetails.getState() != null && payDetails.getState() == 1) {
                return "success";
            }
            Result completion = userMoneyService.completePaidVipOrder(orders.getOrdersId());
            Object code = completion.get("code");
            if (!(code instanceof Number) || ((Number) code).intValue() != 0) {
                log.error("会员支付宝到账处理失败: {}", completion.get("msg"));
                return "failure";
            }
            if (payDetails == null) {
                saveVipPayDetails(orders);
                payDetails = payDetailsDao.selectByOrderId(orders.getOrdersNo());
            }
            payDetailsDao.updateState(payDetails.getId(), 1, sdf.format(new Date()), params.get("trade_no"));
            return "success";
        } catch (Exception e) {
            log.error("会员支付宝回调处理失败", e);
            return "failure";
        }
    }

    private Result validateVipOrder(Orders orders, Long userId) {
        if (orders == null || orders.getOrdersType() == null || orders.getOrdersType() != 2L) {
            return Result.error("会员订单不存在");
        }
        if (!orders.getUserId().equals(userId)) {
            return Result.error("无权支付该会员订单");
        }
        if (!"0".equals(orders.getState())) {
            return Result.error("会员订单已支付或状态异常");
        }
        return null;
    }

    private void saveVipPayDetails(Orders orders) {
        PayDetails payDetails = payDetailsDao.selectByOrderId(orders.getOrdersNo());
        if (payDetails == null) {
            payDetails = new PayDetails();
            payDetails.setState(0);
            payDetails.setCreateTime(sdf.format(new Date()));
            payDetails.setOrderId(orders.getOrdersNo());
            payDetails.setUserId(orders.getUserId());
            payDetails.setMoney(orders.getPayMoney().doubleValue());
            payDetails.setClassify(4);
            payDetails.setType(2);
            payDetails.setRemark("支付宝会员购买");
            payDetailsDao.insert(payDetails);
        }
    }

    private Map<String, String> readNotifyParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
            params.put(entry.getKey(), String.join(",", entry.getValue()));
        }
        return params;
    }

    /*public String getGeneralOrder() {
        Date date = new Date();
        String newString = String.format("%0" + 4 + "d", (int) ((Math.random() * 9 + 1) * 1000));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(date);
        return format + newString;
    }*/


    /**
     * 说明： 支付宝订单退款
     *
     * @return 公共返回参数 code,msg,   响应参数实例: https://docs.open.alipay.com/api_1/alipay.trade.refund
     */
    public String alipayRefund(Orders orders) {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", commonInfoService.findOne(63).getValue(), commonInfoService.findOne(65).getValue(), "json", AliPayConstants.CHARSET, commonInfoService.findOne(64).getValue(), "RSA2");
        AlipayTradeRefundRequest alipay_request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(orders.getOrdersNo());//订单编号
        //model.setTradeNo(orders.getTradeNo());//支付宝订单交易号
        model.setRefundAmount(orders.getPayMoney().toString());//退款金额 不得大于订单金额
        model.setRefundReason(orders.getRefundContent());//退款说明
        model.setOutRequestNo(orders.getOrdersNo());//标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
        alipay_request.setBizModel(model);
        try {
            AlipayTradeRefundResponse alipay_response = alipayClient.execute(alipay_request);
            String alipayRefundStr = alipay_response.getBody();
            log.info(alipayRefundStr);
            return alipayRefundStr;
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }


}
