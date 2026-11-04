package com.pwyl.modules.orders.service.impl;

import com.alipay.api.domain.OrderShopInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwyl.common.utils.DateUtils;
import com.pwyl.common.utils.PageUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.dao.UserDao;
import com.pwyl.modules.app.dao.UserMoneyDao;
import com.pwyl.modules.app.dao.VipDetailsDao;
import com.pwyl.modules.app.dao.VipDiscountDao;
import com.pwyl.modules.app.entity.*;
import com.pwyl.modules.app.service.UserMoneyDetailsService;
import com.pwyl.modules.app.service.UserMoneyService;
import com.pwyl.modules.app.service.UserService;
import com.pwyl.modules.app.service.UserVipService;
import com.pwyl.modules.common.entity.CommonInfo;
import com.pwyl.modules.common.service.CommonInfoService;
import com.pwyl.modules.consortia.entity.Consortia;
import com.pwyl.modules.consortia.service.ConsortiaService;
import com.pwyl.modules.coupon.entity.SelfCouponUser;
import com.pwyl.modules.coupon.respository.SelfCouponUserJpaRepository;
import com.pwyl.modules.coupon.service.SelfCouponUserService;
import com.pwyl.modules.message.entity.MessageInfo;
import com.pwyl.modules.message.service.MessageService;
import com.pwyl.modules.orders.dao.OrdersDao;
import com.pwyl.modules.orders.entity.Orders;
import com.pwyl.modules.orders.response.OrderAllResponse;
import com.pwyl.modules.orders.service.OrdersService;
import com.pwyl.modules.pay.dao.PayDetailsDao;
import com.pwyl.modules.pay.entity.PayDetails;
import com.pwyl.modules.taking.dao.FastOrderDao;
import com.pwyl.modules.taking.dao.FastOrderTakeDao;
import com.pwyl.modules.taking.dao.GameDao;
import com.pwyl.modules.taking.dao.OrderTakingDao;
import com.pwyl.modules.taking.entity.FastOrder;
import com.pwyl.modules.taking.entity.FastOrderTake;
import com.pwyl.modules.taking.entity.Game;
import com.pwyl.modules.taking.entity.OrderTaking;
import com.pwyl.modules.taking.service.FastOrderService;
import com.pwyl.modules.taking.service.OrderTakingService;
import com.pwyl.modules.utils.AliPayOrderUtil;
import com.pwyl.modules.utils.SenInfoCheckUtil;
import com.pwyl.modules.utils.excel.ExcelData;
import jodd.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weixin.popular.bean.card.Discount;
import weixin.popular.bean.message.templatemessage.TemplateMessageItem;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
@Slf4j
public class OrdersServiceImpl extends ServiceImpl<OrdersDao, Orders> implements OrdersService {

    @Autowired
    private OrderTakingDao orderTakingDao;
    @Autowired
    private UserVipService userVipService;
    @Autowired
    private VipDetailsDao vipDetailsDao;
    @Autowired
    private PayDetailsDao payDetailsDao;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private VipDiscountDao vipDiscountDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private GameDao gameDao;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderTakingService orderTakingService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private SelfCouponUserJpaRepository selfCouponUserJpaRepository;
    @Autowired
    private FastOrderDao fastOrderDao;
    @Autowired
    private FastOrderTakeDao fastOrderTakeDao;
    @Autowired
    private ConsortiaService consortiaService;
    private ReentrantReadWriteLock reentrantReadWriteLock=new ReentrantReadWriteLock(true);

    @Override
    public int selectMyOrdersCount(Long userId, String time){
        return baseMapper.selectMyOrdersCount(userId, time);
    }

    @Override
    public int selectTakeOrdersCount( Long userId, String time){
        return baseMapper.selectTakeOrdersCount(userId, time);
    }

    @Override
    public Result insertOrders(Orders orders) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        orders.setOrdersNo(AliPayOrderUtil.createOrderId());
        OrderTaking orderTaking = orderTakingDao.selectById(orders.getOrderTakingId());
        orders.setOrderTakingUserId(orderTaking.getUserId());
        //设置订单状态
        orders.setState("0");
        //设置创建时间
        orders.setCreateTime(df.format(new Date()));
        //设置更新时间
        orders.setUpdateTime(df.format(new Date()));
        orders.setOrdersType(1L);
        //用户类型
        orders.setType((long) 1);
        orders.setOrderType(1);
        //设置假射出状态
        orders.setIsdelete((long) 0);
        UserVip userVip = userVipService.selectUserVipByUserId(orders.getUserId());
        try {
            if (userVip != null && df.parse(userVip.getEndTime()).getTime()>System.currentTimeMillis() && userVip.getIsVip()==1) {
                orders.setType((long) 1);
                BigDecimal money = orderTaking.getMemberMoney();
                orders.setOrderTakingMoney(money);
                BigDecimal moneys = money.multiply(BigDecimal.valueOf(orders.getOrderNumber()));
                //设置支付金额
                orders.setPayMoney(moneys);
            }else{
                orders.setType((long) 2);
                BigDecimal money = orderTaking.getMoney();
                orders.setOrderTakingMoney(money);
                BigDecimal nmoney = money.multiply(BigDecimal.valueOf(orders.getOrderNumber()));
                //设置支付金额
                orders.setPayMoney(nmoney);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        UserMoney userMoney = userMoneyService.selectUserMoneyByUserId(orders.getUserId());
        if(userMoney.getMoney().doubleValue()>=orders.getPayMoney().doubleValue()){
            UserEntity userEntity = userService.selectUserById(orders.getUserId());
            userMoneyService.updateMoney(2,orders.getUserId(),orders.getPayMoney().doubleValue());
            UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
            userMoneyDetails.setMoney(orders.getPayMoney());
            userMoneyDetails.setUserId(orders.getUserId());
            userMoneyDetails.setContent("支付订单："+orders.getOrdersNo());
            userMoneyDetails.setTitle("下单成功");
            userMoneyDetails.setType(2);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
            userMoneyDetailsService.save(userMoneyDetails);
            MessageInfo messageInfo=new MessageInfo();
            messageInfo.setContent("订单下单成功："+orders.getOrdersNo());
            messageInfo.setTitle("订单通知");
            messageInfo.setState(String.valueOf(4));
            messageInfo.setUserName(userEntity.getUserName());
            messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
            messageInfo.setCreateAt(simpleDateFormat.format(new Date()));
            messageInfo.setIsSee("0");
            messageService.saveBody(messageInfo);
            if(StringUtil.isNotBlank(userEntity.getClientid())){
                userService.pushToSingle(messageInfo.getTitle(),messageInfo.getContent(),userEntity.getClientid());
            }
            String userName=userEntity.getUserName();
            orders.setState("1");
            baseMapper.insert(orders);
            OrderTaking byId = orderTakingService.getById(orders.getOrderTakingId());
            userEntity = userService.selectUserById(byId.getUserId());
            MessageInfo messageInfos=new MessageInfo();
            messageInfos.setContent("订单接单成功："+orders.getOrdersNo());
            messageInfos.setTitle("订单通知");
            messageInfos.setState(String.valueOf(4));
            messageInfos.setUserName(userEntity.getUserName());
            messageInfos.setUserId(String.valueOf(userEntity.getUserId()));
            messageInfos.setCreateAt(simpleDateFormat.format(new Date()));
            messageInfos.setIsSee("0");
            messageService.saveBody(messageInfos);
            if(StringUtil.isNotBlank(userEntity.getClientid())){
                userService.pushToSingle(messageInfos.getTitle(),messageInfos.getContent(),userEntity.getClientid());
            }
            if(StringUtils.isNotEmpty(userEntity.getPhone())){
                userService.sendMsg(userEntity.getPhone(),"newOrders2",String.valueOf(orders.getOrdersId()));
            }

            if(userEntity.getIsSendMsg()==null || userEntity.getIsSendMsg()==1){
                //小程序订阅号消息推送
                CommonInfo one = commonInfoService.findOne(235);
                List<String> msgList=new ArrayList<>();
                msgList.add(orders.getOrdersNo());//订单号
                Game game = gameDao.selectById(byId.getGameId());
                msgList.add(game.getGameName());//商品名称
                msgList.add(String.valueOf(orders.getOrderNumber()));//商品数量
                msgList.add(orders.getCreateTime());//下单时间
                msgList.add(userName);//客户名称
                if (StringUtils.isNotEmpty(userEntity.getOpenId())) {
                    SenInfoCheckUtil.sendMsg(userEntity.getOpenId(),one.getValue(),msgList,1);
                }
            }

            if(StringUtils.isNotBlank(userEntity.getWxOpenId())){
                String apkey = commonInfoService.findOne(809).getValue();
                LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
                data.put("character_string10", new TemplateMessageItem(orders.getOrdersNo(), "#d71345"));
                Game game = gameDao.selectById(byId.getGameId());
                data.put("thing14", new TemplateMessageItem(game.getGameName(), "#d71345"));
                data.put("character_string11", new TemplateMessageItem(String.valueOf(orders.getOrderNumber()), "#d71345"));
                data.put("time13", new TemplateMessageItem(orders.getCreateTime(), "#d71345"));
                data.put("thing4", new TemplateMessageItem(userName, "#d71345"));
                userService.sendWxMessage(apkey, data, userEntity.getWxOpenId());
            }


            if(orders.getFastOrderId()!=null){
                FastOrder fastOrder = fastOrderDao.selectById(orders.getFastOrderId());
                fastOrder.setOrdersUserId(byId.getUserId());
                fastOrder.setStatus(2);
                fastOrderDao.updateById(fastOrder);
                //将没选中的人改为取消
                fastOrderTakeDao.updateFastOrderTake(orders.getFastOrderId());
                //将选中的人改为匹配成功
                FastOrderTake fastOrderTake = fastOrderTakeDao.selectById(orders.getFastOrderTakeId());
                fastOrderTake.setStatus(2);
                fastOrderTakeDao.updateById(fastOrderTake);
            }

            return Result.success();
        }else{
            return Result.error("金币不足，请充值！");
        }
    }

        /**
     * 生成订单
     *
     * @param userId
     * @param id
     * @return
     */
    @Override
    public Result generateOrder(Long userId, Long id, Long orderType, Long orderNumber, String remarks,Long couponId,Long fastOrderId,Long fastOrderTakeId) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //判断订单类型
        if (orderType != null) {
            //创建订单模板
            Orders orders = new Orders();
            //查看会员信息
            UserVip userVip = userVipService.selectUserVipByUserId(userId);
            //接单
            if (orderType == 1) {
                if(orderNumber<=0){
                    return Result.error("订单数量异常，请刷新页面后重试！");
                }
                //接单的订单
                OrderTaking orderTaking = orderTakingDao.selectById(id);
                //接单的订单是否存在
                if (orderTaking == null || orderTaking.getIsdelete()==1) {
                    return Result.error("订单不存在，请刷新页面后重试！");
                }
                if(orderTaking.getStatus()!=0){
                    return Result.error("当前订单已下架或系统审核中，暂不支持下单！");
                }
                //设置订单编号
                orders.setOrdersNo(AliPayOrderUtil.createOrderId());
                orders.setOrderTakingUserId(orderTaking.getUserId());
                //设置用户id
                orders.setUserId(userId);
                //设置接单id
                orders.setOrderTakingId(id);
                //设置订单状态
                orders.setState("0");
                //设置创建时间
                orders.setCreateTime(df.format(new Date()));
                //设置更新时间
                orders.setUpdateTime(df.format(new Date()));
                //设置订单种类
                orders.setOrdersType(orderType);
                //用户类型
                orders.setType((long) 1);
                orders.setOrderType(1);
                //判断是否是快速约玩
                if (fastOrderId != null) {
                    orders.setOrderType(2);
                    orders.setFastOrderId(fastOrderId);
                    orders.setFastOrderTakeId(fastOrderTakeId);
                }
                //订单数量
                if (orderNumber != null) {
                    try {
                        if (userVip != null && df.parse(userVip.getEndTime()).getTime()>System.currentTimeMillis() && userVip.getIsVip()==1) {
                            orders.setType((long) 1);
                            BigDecimal money = orderTaking.getMemberMoney();
                            orders.setOrderTakingMoney(money);
                            BigDecimal moneys = money.multiply(BigDecimal.valueOf(orderNumber));
                            //设置支付金额
                            orders.setPayMoney(moneys);
                        }else{
                            orders.setType((long) 2);
                            BigDecimal money = orderTaking.getMoney();
                            orders.setOrderTakingMoney(money);
                            BigDecimal nmoney = money.multiply(BigDecimal.valueOf(orderNumber));
                            //设置支付金额
                            orders.setPayMoney(nmoney);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        log.error("生成订单会员价格出异常："+e.getMessage(),e);
                        orders.setType((long) 2);
                        BigDecimal money = orderTaking.getMoney();
                        orders.setOrderTakingMoney(money);
                        BigDecimal nmoney = money.multiply(BigDecimal.valueOf(orderNumber));
                        //设置支付金额
                        orders.setPayMoney(nmoney);
                    }
                    //判断是否使用优惠券
                    if(couponId!=null && couponId!=0){
                        SelfCouponUser selfCouponUser = selfCouponUserJpaRepository.findById(couponId).orElse(null);
                        if(selfCouponUser!=null && selfCouponUser.getUserId().equals(userId)){
                            String minMoney = selfCouponUser.getMinMoney();
                            BigDecimal money=new BigDecimal(minMoney);
                            if(orders.getPayMoney().doubleValue()>=money.doubleValue()){
                                orders.setCouponId(couponId);
                                BigDecimal couponMoney = new BigDecimal(selfCouponUser.getLessMoney());
                                orders.setCouponMoney(couponMoney);
                                orders.setPayMoney(orders.getPayMoney().subtract(couponMoney));
                                selfCouponUser.setStatus(2);
                                selfCouponUserJpaRepository.save(selfCouponUser);
                            }else{
                                return Result.error("优惠券使用条件不满足！");
                            }
                        }else{
                            return Result.error("优惠券不存在！");
                        }
                    }

                    //设置订单数量
                    orders.setOrderNumber(orderNumber);
                    //设置备注
                    orders.setRemarks(remarks);
                    //设置假射出状态
                    orders.setIsdelete((long) 0);
                    //生成订单
                    baseMapper.insert(orders);
                    return Result.success().put("data", orders);
                } else {
                    return Result.error("订单数量为空！订单生成失败");
                }

            } else {
                //会员
                //设置订单编号
                orders.setOrdersNo(AliPayOrderUtil.createOrderId());
                //设置用户id
                orders.setUserId(userId);
                //设置要开通的会员类型id
                orders.setVipDetailsId(id);
                //查询会员支付价钱
                VipDetails vipDetails = vipDetailsDao.selectOne(new QueryWrapper<VipDetails>().eq("id", id));
                //设置订单金额
                // vip_details.money 是旧版柚币价格，支付宝必须使用人民币会员价。
                orders.setPayMoney(resolveVipCashPrice(vipDetails));
                //设置订单状态 待支付
                orders.setState("0");
                //设置创建时间
                orders.setCreateTime(df.format(new Date()));
                //设置更新时间
                orders.setUpdateTime(df.format(new Date()));
                orders.setIsdelete((long) 0);
                //设置订单种类
                orders.setOrdersType(orderType);
                baseMapper.insert(orders);
                return Result.success().put("data", orders);
            }
        } else {
            return Result.error("订单类型异常，请刷新页面后重试！");
        }
    }

    private BigDecimal resolveVipCashPrice(VipDetails vipDetails) {
        if (vipDetails == null || vipDetails.getVipNameType() == null) {
            throw new IllegalArgumentException("会员类型不存在");
        }
        switch (vipDetails.getVipNameType()) {
            case 0:
                return new BigDecimal("28");
            case 1:
                return new BigDecimal("68");
            case 2:
                return new BigDecimal("168");
            default:
                throw new IllegalArgumentException("不支持的会员类型");
        }
    }

    @Override
    public Result selectMyOrder(Long userId, IPage iPage, String status) {
        return Result.success().put("data", new PageUtils(baseMapper.selectMyOrder(iPage, userId, status)));
    }

    @Override
    public Result selectMyOrdersCount(Long userId){
        //0待支付1待服务2已完成3已退款4进行中
        int count0 = baseMapper.selectCount(new QueryWrapper<Orders>().eq("user_id", userId).eq("orders_type",1).eq("state", 0));
        int count1 = baseMapper.selectCount(new QueryWrapper<Orders>().eq("user_id", userId).eq("orders_type",1).eq("state", 1));
        int count2 = baseMapper.selectCount(new QueryWrapper<Orders>().eq("user_id", userId).eq("orders_type",1).eq("state", 2));
        int count4 = baseMapper.selectCount(new QueryWrapper<Orders>().eq("user_id", userId).eq("orders_type",1).eq("state", 4));
        Map<String,Object> result=new HashMap<>();
        result.put("count0",count0);
        result.put("count1",count1);
        result.put("count2",count2);
        result.put("count4",count4);
        return Result.success().put("data",result);
    }

    @Override
    public Result selectTeamOrdersList(Integer page, Integer limit, Long userId,Integer type, Integer status) {
        return Result.success().put("data", new PageUtils(baseMapper.selectTeamOrdersList(new Page<>(page,limit),userId,type,status)));
    }

    @Override
    public Result selectTeamUserList(Integer page, Integer limit, String invitationCode,Integer type,Long userId) {
        return Result.success().put("data", new PageUtils(baseMapper.selectTeamUserList(new Page<>(page,limit),invitationCode,type,userId)));
    }

    @Override
    public Double selectOrdersMoneyCountByUserId(Long userId,Integer type){
        return baseMapper.selectOrdersMoneyCountByUserId(userId,type);
    }

    @Override
    public Integer selectUserCountByInvitationCode(String invitationCode,Integer type){
        return baseMapper.selectUserCountByInvitationCode(invitationCode,type);
    }


    @Override
    public Result investOrder(Long userId, Double money, Integer classify) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        PayDetails payDetails = new PayDetails();
        //分类（ 1app微信 2微信公众号 3微信小程序 4支付宝）
        payDetails.setClassify(classify);
        //订单编号
        payDetails.setOrderId(AliPayOrderUtil.createOrderId());
        //金额
        payDetails.setMoney(money);
        //用户id
        payDetails.setUserId(userId);
        //状态0待支付 1支付成功 2失败
        payDetails.setState(0);
        //创建时间
        payDetails.setCreateTime(simpleDateFormat.format(new Date()));
        payDetailsDao.insert(payDetails);
        return Result.success();
    }

    @Override
    public Result cancelOrder(Long id, String status) {
        reentrantReadWriteLock.writeLock().lock();
        try {
            return cancelOrders(id,status);
        }catch (Exception e){
            log.error("修改订单状态异常："+e.getMessage(),e);
            e.printStackTrace();
        }finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return Result.error("系统繁忙，请稍后再试！");
    }

    @Transactional
    public Result cancelOrders(Long id, String status){
        Orders orders = baseMapper.selectById(id);
        if (orders != null) {
            if ("1".equals(orders.getState()) || "4".equals(orders.getState())) {
                String oldStatus=orders.getState();
                orders.setState(status);

                if ("2".equals(status)) {
                    //完成订单
                    OrderTaking orderTaking = orderTakingDao.selectById(orders.getOrderTakingId());
                    Integer ordersCount = baseMapper.selectCount(new QueryWrapper<Orders>().eq("order_taking_id", orders.getOrderTakingId()).eq("state", 2).eq("orders_type", 1));
                    ordersCount++;
                    orderTaking.setCount(ordersCount);
                    orderTaking.setId(orders.getOrderTakingId());
                    orderTakingService.updateById(orderTaking);
                    BigDecimal orderTakingMoney=orders.getOrderTakingMoney();
                    if(orderTakingMoney==null){
                        orderTakingMoney=orderTaking.getOldMoney();
                    }
                    BigDecimal add = orderTakingMoney.multiply(BigDecimal.valueOf(orders.getOrderNumber()));
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = sdf.format(new Date());
                    //判断是否开启分销
                    String value = commonInfoService.findOne(209).getValue();
                    if("是".equals(value)){
                        //分销
                        UserEntity userEntity = userService.selectUserById(orderTaking.getUserId());
                        //陪玩官
                        BigDecimal peiMoney=new BigDecimal("0.00");
                        if(userEntity.getRate().doubleValue()==0 || userEntity.getRate().doubleValue()<0){
                            peiMoney=add;
                        }else{
                            peiMoney=add.multiply(userEntity.getRate()).setScale(2,BigDecimal.ROUND_DOWN);;
                        }
                        orders.setRate(peiMoney);
                        userMoneyService.updateEarningsMoney(1,orderTaking.getUserId(),peiMoney.doubleValue());
                        UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                        userMoneyDetails.setUserId(orderTaking.getUserId());
                        userMoneyDetails.setTitle("订单完成："+orders.getOrdersNo());
                        userMoneyDetails.setContent("订单完成，到账金额：" +peiMoney);
                        userMoneyDetails.setType(1);
                        userMoneyDetails.setClassify(10);
                        userMoneyDetails.setMoney(peiMoney);
                        userMoneyDetails.setCreateTime(time);
                        userMoneyDetailsService.save(userMoneyDetails);
                        UserEntity ordersUser = userService.selectUserById(orders.getUserId());

                        //直属
                        UserEntity zhiUser = userService.queryByInvitationCode(ordersUser.getInviterCode());
                        if(zhiUser!=null){
                            if(zhiUser.getZhiRate().doubleValue()!=0 && zhiUser.getZhiRate().doubleValue()>0){
                                peiMoney=add.multiply(zhiUser.getZhiRate()).setScale(2,BigDecimal.ROUND_DOWN);;
                                orders.setZhiRate(peiMoney);
                                orders.setZhiUserId(zhiUser.getUserId());
                                userMoneyService.updateEarningsMoney(1,zhiUser.getUserId(),peiMoney.doubleValue());
                                userMoneyDetails = new UserMoneyDetails();
                                userMoneyDetails.setUserId(zhiUser.getUserId());
                                userMoneyDetails.setByUserId(orders.getUserId());
                                userMoneyDetails.setTitle("一级佣金订单完成："+orders.getOrdersNo());
                                userMoneyDetails.setContent("一级佣金订单完成，到账金额：" +peiMoney);
                                userMoneyDetails.setType(1);
                                userMoneyDetails.setClassify(1);
                                userMoneyDetails.setMoney(peiMoney);
                                userMoneyDetails.setCreateTime(time);
                                userMoneyDetailsService.save(userMoneyDetails);
                            }
                            //非直属
                            UserEntity feiUser = userService.queryByInvitationCode(zhiUser.getInviterCode());
                            if(feiUser!=null){
                                if(feiUser.getFeiRate().doubleValue()!=0 && feiUser.getFeiRate().doubleValue()>0){
                                    peiMoney=add.multiply(feiUser.getFeiRate()).setScale(2,BigDecimal.ROUND_DOWN);;
                                    orders.setFeiRate(peiMoney);
                                    orders.setFeiUserId(feiUser.getUserId());
                                    userMoneyService.updateEarningsMoney(1,feiUser.getUserId(),peiMoney.doubleValue());
                                    userMoneyDetails = new UserMoneyDetails();
                                    userMoneyDetails.setUserId(feiUser.getUserId());
                                    userMoneyDetails.setByUserId(orders.getUserId());
                                    userMoneyDetails.setTitle("二级佣金订单完成："+orders.getOrdersNo());
                                    userMoneyDetails.setContent("二级佣金订单完成，到账金额：" +peiMoney);
                                    userMoneyDetails.setType(1);
                                    userMoneyDetails.setClassify(1);
                                    userMoneyDetails.setMoney(peiMoney);
                                    userMoneyDetails.setCreateTime(time);
                                    userMoneyDetailsService.save(userMoneyDetails);
                                }
                            }
                        }
                        //判断陪玩的有没有公会 如果有公会 则对公会的会长进行返利
                        if (userEntity.getConsortiaId()!=null){
                            Consortia consortia = consortiaService.getById(userEntity.getConsortiaId());
                            if(consortia!=null){
                                UserEntity consortiaUser = userService.selectUserById(consortia.getUserId());
                                if(consortiaUser!=null && consortiaUser.getConsortiaOrdersRate()!=null && consortiaUser.getConsortiaOrdersRate().doubleValue()>0){
                                    peiMoney = add.multiply(consortiaUser.getConsortiaOrdersRate());
                                    peiMoney = peiMoney.setScale(2,BigDecimal.ROUND_DOWN);;
                                    orders.setConsortiaRate(peiMoney);
                                    orders.setConsortiaUserId(consortiaUser.getUserId());
                                    userMoneyService.updateEarningsMoney(1, consortiaUser.getUserId(), peiMoney.doubleValue());
                                    userMoneyDetails = new UserMoneyDetails();
                                    userMoneyDetails.setUserId(consortiaUser.getUserId());
                                    userMoneyDetails.setByUserId(userEntity.getUserId());
                                    userMoneyDetails.setTitle("陪陪订单：" + orders.getOrdersNo());
                                    userMoneyDetails.setContent("公会陪陪："+userEntity.getUserName()+"，订单完成，到账佣金：" + peiMoney);
                                    userMoneyDetails.setType(1);
                                    userMoneyDetails.setClassify(5);
                                    userMoneyDetails.setMoney(peiMoney);
                                    userMoneyDetails.setCreateTime(time);
                                    userMoneyDetailsService.save(userMoneyDetails);
                                }
                            }
                        }
                    }else{
                        orders.setRate(add);
                        userMoneyService.updateEarningsMoney(1,orderTaking.getUserId(),add.doubleValue());
                        UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                        userMoneyDetails.setUserId(orderTaking.getUserId());
                        userMoneyDetails.setTitle("订单完成："+orders.getOrdersNo());
                        userMoneyDetails.setContent("订单完成，到账金额：" +add);
                        userMoneyDetails.setType(1);
                        userMoneyDetails.setClassify(10);
                        userMoneyDetails.setMoney(add);
                        userMoneyDetails.setCreateTime(time);
                        userMoneyDetailsService.save(userMoneyDetails);
                    }
                    BigDecimal pingRate = orders.getPayMoney().subtract(orders.getRate());
                    if(orders.getZhiRate()!=null){
                        pingRate=pingRate.subtract(orders.getZhiRate());
                    }
                    if(orders.getFeiRate()!=null){
                        pingRate=pingRate.subtract(orders.getFeiRate());
                    }
                    orders.setPingRate(pingRate);
                    orders.setEndTime(time);
                }else if("3".equals(status)){
                    if (!"1".equals(oldStatus)) {
                        if("2".equals(oldStatus)){
                            return Result.error("当前订单已完成！");
                        }else if("3".equals(oldStatus)){
                            return Result.error("当前订单已退款！");
                        }else{
                            return Result.error("当前订单正在进行中，无法退款！");
                        }
                    }
                    //退款
                    userMoneyService.updateMoney(1,orders.getUserId(),orders.getPayMoney().doubleValue());
                    UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                    userMoneyDetails.setUserId(orders.getUserId());
                    userMoneyDetails.setTitle("订单退款："+orders.getOrdersNo());
                    userMoneyDetails.setContent("订单退款："+orders.getPayMoney());
                    userMoneyDetails.setType(1);
                    userMoneyDetails.setMoney(orders.getPayMoney());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    userMoneyDetails.setCreateTime(sdf.format(new Date()));
                    userMoneyDetailsService.save(userMoneyDetails);
                    UserEntity userEntity = userService.selectUserById(orders.getUserId());
                    if(userEntity!=null && (userEntity.getIsSendMsg()==null || userEntity.getIsSendMsg()==1)){
                        //小程序订阅号消息推送
                        CommonInfo one = commonInfoService.findOne(236);
                        List<String> msgList=new ArrayList<>();
                        msgList.add(orders.getOrdersNo());//订单号
                        msgList.add(String.valueOf(orders.getPayMoney()));//退款金额
                        msgList.add("您的下单被取消了，金额已退款至你的钱包");//温馨提示

                        if (StringUtils.isNotEmpty(userEntity.getOpenId())) {
                            SenInfoCheckUtil.sendMsg(userEntity.getOpenId(),one.getValue(),msgList,2);
                        }
                    }

                    if(userEntity!=null && StringUtils.isNotBlank(userEntity.getWxOpenId())){
                        String apkey = commonInfoService.findOne(810).getValue();
                        LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
                        data.put("character_string5", new TemplateMessageItem(orders.getOrdersNo(), "#d71345"));
                        OrderTaking orderTaking = orderTakingDao.selectById(orders.getOrderTakingId());
                        Game game = gameDao.selectById(orderTaking.getGameId());
                        data.put("thing1", new TemplateMessageItem(game.getGameName(), "#d71345"));
                        data.put("amount2", new TemplateMessageItem(String.valueOf(orders.getPayMoney()), "#d71345"));
                        data.put("time4", new TemplateMessageItem(DateUtils.format(new Date()), "#d71345"));
                        userService.sendWxMessage(apkey, data, userEntity.getWxOpenId());
                    }

                }
                baseMapper.updateById(orders);
                if(orders.getCouponId()!=null && orders.getCouponId()!=0){
                    SelfCouponUser selfCouponUser = selfCouponUserJpaRepository.findById(orders.getCouponId()).orElse(null);
                    selfCouponUser.setStatus(1);
                    selfCouponUserJpaRepository.save(selfCouponUser);
                }
                return Result.success();
            }else if("0".equals(orders.getState())){
                baseMapper.deleteById(id);
                if(orders.getCouponId()!=null && orders.getCouponId()!=0){
                    SelfCouponUser selfCouponUser = selfCouponUserJpaRepository.findById(orders.getCouponId()).orElse(null);
                    selfCouponUser.setStatus(1);
                    selfCouponUserJpaRepository.save(selfCouponUser);
                }
                return Result.success();
            }
            return Result.error("订单状态异常，请刷新后重试！");
        } else {
            return Result.error("订单不存在！");
        }
    }


    @Override
    public Result deleteOrder(Long id) {
        Orders orders = baseMapper.selectById(id);
        if (orders != null) {
            orders.setIsdelete((long) 1);
            baseMapper.update(orders, new QueryWrapper<Orders>().eq("orders_id", id));
            return Result.success();
        } else {
            return Result.error("订单不存在！");
        }
    }

    @Override
    public Result queryOrders(Long id) {
        Orders orders = baseMapper.selectById(id);
        OrderTaking orderTaking = orderTakingDao.selectById(orders.getOrderTakingId());
        if(orderTaking!=null){
            orders.setGame(gameDao.selectById(orderTaking.getGameId()));
            orders.setOrderTaking(orderTaking);
            UserEntity userEntity = userDao.selectById(orderTaking.getUserId());
            orders.setUser(userEntity);
            orders.setOrdersUser(userDao.selectById(orders.getUserId()));
        }
        return Result.success().put("data", orders);
    }

    @Override
    public Result queryOrdersAll(Long page, Long limit, Long type,String orderName,  String name, Long status, Long userId, Integer orderType,Integer ordersType,String startTime,String endTime,String ordersNo) {
        Page<Orders> iPage = new Page<>(page, limit);
        if (StringUtils.isNotEmpty(name)) {
            name = "%" + name + "%";
        }
        return Result.success().put("data", new PageUtils(baseMapper.queryOrdersAll(iPage, type,orderName, name, status, userId, orderType,ordersType,startTime,endTime,ordersNo)));
    }


    @Override
    public ExcelData ordersListExcel(Long type,String orderName,  String name, Long status, Long userId, Integer orderType,Integer ordersType, String startTime, String endTime,String ordersNo){
        if (StringUtils.isNotEmpty(name)) {
            name = "%" + name + "%";
        }
        List<OrderAllResponse> orderAllResponses = baseMapper.queryOrdersAllExcel(type, name,orderName, status, userId, orderType,ordersType,startTime,endTime,ordersNo);
        ExcelData data = new ExcelData();
        data.setName("订单列表");
        List<String> titles = new ArrayList();
        titles.add("编号");titles.add("订单用户");titles.add("接单陪玩");titles.add("订单编号");
        titles.add("支付金额");titles.add("订单类型");titles.add("一级佣金");titles.add("二级佣金");titles.add("陪玩官佣金");titles.add("会长佣金");
        titles.add("平台佣金");titles.add("备注");titles.add("订单状态");titles.add("创建时间");
        data.setTitles(titles);
        List<List<Object>> rows = new ArrayList();
        for(OrderAllResponse orderAllResponse:orderAllResponses){
            List<Object> row = new ArrayList();
            row.add(orderAllResponse.getOrdersId());row.add(orderAllResponse.getOrdersUserName());row.add(orderAllResponse.getUserName());row.add(orderAllResponse.getOrdersNo());
            row.add(orderAllResponse.getPayMoney());
            if(orderAllResponse.getOrdersType()==1){
                row.add("服务订单");
            }else{
                row.add("约玩订单");
            }
            row.add(orderAllResponse.getZhiRate()==null?0:orderAllResponse.getZhiRate());
            row.add(orderAllResponse.getFeiRate()==null?0:orderAllResponse.getFeiRate());
            row.add(orderAllResponse.getRate()==null?0:orderAllResponse.getRate());
            row.add(orderAllResponse.getConsortiaRate()==null?0:orderAllResponse.getConsortiaRate());
            row.add(orderAllResponse.getPingRate()==null?0:orderAllResponse.getPingRate());
            row.add(StringUtils.isNotBlank(orderAllResponse.getRemarks())?"":orderAllResponse.getRemarks());
            //0待支付1待服务2已完成3已退款4进行中
            if(orderAllResponse.getState()==0){
                row.add("待支付");
            }else if(orderAllResponse.getState()==1){
                row.add("待服务");
            }else if(orderAllResponse.getState()==2){
                row.add("已完成");
            }else if(orderAllResponse.getState()==3){
                row.add("已退款");
            }else if(orderAllResponse.getState()==4){
                row.add("进行中");
            }else{
                row.add("未知");
            }
            row.add(orderAllResponse.getCreateTime());
            rows.add(row);
        }
        data.setRows(rows);
        return data;
    }

    @Override
    public Result selectMyTakeOrders(Integer page,Integer limit,Long userId,Integer status){
        return Result.success().put("data",new PageUtils(baseMapper.selectMyTakeOrders(new Page<>(page,limit),userId,status)));
    }

    @Override
    public Result payMoney(Long ordersId){
        Orders orders = baseMapper.selectById(ordersId);
        if(orders!=null){
            if(orders.getOrderNumber()<=0){
                baseMapper.deleteById(orders.getOrdersId());
                return Result.error("订单数量异常，请刷新页面后重试！");
            }
            UserMoney userMoney = userMoneyService.selectUserMoneyByUserId(orders.getUserId());
            if(userMoney.getMoney().doubleValue()>=orders.getPayMoney().doubleValue()){
                UserEntity userEntity = userService.selectUserById(orders.getUserId());
                userMoneyService.updateMoney(2,orders.getUserId(),orders.getPayMoney().doubleValue());
                UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
                userMoneyDetails.setMoney(orders.getPayMoney());
                userMoneyDetails.setUserId(orders.getUserId());
                userMoneyDetails.setContent("支付订单："+orders.getOrdersNo());
                userMoneyDetails.setTitle("下单成功");
                userMoneyDetails.setType(2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
                userMoneyDetailsService.save(userMoneyDetails);
                MessageInfo messageInfo=new MessageInfo();
                messageInfo.setContent("订单下单成功："+orders.getOrdersNo());
                messageInfo.setTitle("订单通知");
                messageInfo.setState(String.valueOf(4));
                messageInfo.setUserName(userEntity.getUserName());
                messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
                messageInfo.setCreateAt(simpleDateFormat.format(new Date()));
                messageInfo.setIsSee("0");
                messageService.saveBody(messageInfo);
                if(StringUtil.isNotBlank(userEntity.getClientid())){
                    userService.pushToSingle(messageInfo.getTitle(),messageInfo.getContent(),userEntity.getClientid());
                }
                String userName=userEntity.getUserName();
                orders.setState("1");
                baseMapper.updateById(orders);
                OrderTaking byId = orderTakingService.getById(orders.getOrderTakingId());
                userEntity = userService.selectUserById(byId.getUserId());
                MessageInfo messageInfos=new MessageInfo();
                messageInfos.setContent("订单接单成功："+orders.getOrdersNo());
                messageInfos.setTitle("订单通知");
                messageInfos.setState(String.valueOf(4));
                messageInfos.setUserName(userEntity.getUserName());
                messageInfos.setUserId(String.valueOf(userEntity.getUserId()));
                messageInfos.setCreateAt(simpleDateFormat.format(new Date()));
                messageInfos.setIsSee("0");
                messageService.saveBody(messageInfos);
                if(StringUtil.isNotBlank(userEntity.getClientid())){
                    userService.pushToSingle(messageInfos.getTitle(),messageInfos.getContent(),userEntity.getClientid());
                }
                if(StringUtils.isNotEmpty(userEntity.getPhone())){
                    userService.sendMsg(userEntity.getPhone(),"newOrders2",String.valueOf(orders.getOrdersId()));
                }

                if(userEntity.getIsSendMsg()==null || userEntity.getIsSendMsg()==1){
                    //小程序订阅号消息推送
                    CommonInfo one = commonInfoService.findOne(235);
                    List<String> msgList=new ArrayList<>();
                    msgList.add(orders.getOrdersNo());//订单号
                    Game game = gameDao.selectById(byId.getGameId());
                    msgList.add(game.getGameName());//商品名称
                    msgList.add(String.valueOf(orders.getOrderNumber()));//商品数量
                    msgList.add(orders.getCreateTime());//下单时间
                    msgList.add(userName);//客户名称
                    if (StringUtils.isNotEmpty(userEntity.getOpenId())) {
                        SenInfoCheckUtil.sendMsg(userEntity.getOpenId(),one.getValue(),msgList,1);
                    }
                }
                if(StringUtils.isNotBlank(userEntity.getWxOpenId())){
                    String apkey = commonInfoService.findOne(809).getValue();
                    LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
                    data.put("character_string10", new TemplateMessageItem(orders.getOrdersNo(), "#d71345"));
                    Game game = gameDao.selectById(byId.getGameId());
                    data.put("thing14", new TemplateMessageItem(game.getGameName(), "#d71345"));
                    data.put("character_string11", new TemplateMessageItem(String.valueOf(orders.getOrderNumber()), "#d71345"));
                    data.put("time13", new TemplateMessageItem(orders.getCreateTime(), "#d71345"));
                    data.put("thing4", new TemplateMessageItem(userName, "#d71345"));
                    userService.sendWxMessage(apkey, data, userEntity.getWxOpenId());
                }

                if(orders.getFastOrderId()!=null){
                    FastOrder fastOrder = fastOrderDao.selectById(orders.getFastOrderId());
                    fastOrder.setOrdersUserId(byId.getUserId());
                    fastOrder.setStatus(2);
                    fastOrderDao.updateById(fastOrder);
                    //将没选中的人改为取消
                    fastOrderTakeDao.updateFastOrderTake(orders.getFastOrderId());
                    //将选中的人改为匹配成功
                    FastOrderTake fastOrderTake = fastOrderTakeDao.selectById(orders.getFastOrderTakeId());
                    fastOrderTake.setStatus(2);
                    fastOrderTakeDao.updateById(fastOrderTake);
                }

                return Result.success();
            }else{
                return Result.error("金币不足，请充值！");
            }
        }else{
            return Result.error("订单不存在，请刷新后重试！");
        }
    }

    @Override
    public PageUtils incomeAnalysisOrders(Integer page, Integer limit, String time, Integer flag) {
        Page<Map<String,Object>> pages=new Page<>(page,limit);
        return new PageUtils(baseMapper.incomeAnalysisOrders(pages,time,flag));
    }

    @Override
    public PageUtils selectTakingUserList(Integer page, Integer limit, String userName, String phone) {
        Page<Map<String,Object>> pages=new Page<>(page,limit);
        return new PageUtils(baseMapper.selectTakingUserList(pages,userName,phone));
    }

    @Override
    public PageUtils selectUserMoneyOrdersList(Integer page, Integer limit, String userName, String phone) {
        Page<Map<String,Object>> pages=new Page<>(page,limit);
        return new PageUtils(baseMapper.selectUserMoneyOrdersList(pages,userName,phone));
    }

    @Override
    public PageUtils selectGiveRewardList(Integer page, Integer limit, String userName, String phone) {
        Page<Map<String,Object>> pages=new Page<>(page,limit);
        return new PageUtils(baseMapper.selectGiveRewardList(pages,userName,phone));
    }



    @Override
    public Integer selectOrdersCountByState(Integer state,Integer orderType,Integer flag,String time){
        return baseMapper.selectOrdersCountByState(state,orderType, flag, time);
    }

    @Override
    public BigDecimal selectOrdersMoneyByTime(Integer type,Integer flag,String time){
        return baseMapper.selectOrdersMoneyByTime(type,flag,time);
    }



}
