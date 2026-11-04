package com.pwyl.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwyl.common.utils.DateUtils;
import com.pwyl.common.utils.PageUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.dao.*;
import com.pwyl.modules.app.entity.*;
import com.pwyl.modules.app.service.*;
import com.pwyl.modules.message.entity.MessageInfo;
import com.pwyl.modules.message.service.MessageService;
import com.pwyl.modules.orders.dao.OrdersDao;
import com.pwyl.modules.orders.entity.Orders;
import com.pwyl.modules.orders.service.OrdersService;
import com.pwyl.modules.pay.entity.PayClassify;
import com.pwyl.modules.pay.service.PayClassifyService;
import jodd.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
@Slf4j
public class UserMoneyServiceImpl extends ServiceImpl<UserMoneyDao, UserMoney> implements UserMoneyService {
    @Autowired
    private UserMoneyDetailsDao userMoneyDetailsDao;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private UserVipDao userVipDao;
    @Autowired
    private VipDetailsDao vipDetailsDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private PayClassifyService payClassifyService;

    @Override
    public Result inviteMoneyConvertMoney(Long userId, Long payClassifyId){
        UserMoney userMoney = selectUserMoneyByUserId(userId);
        PayClassify payClassify = payClassifyService.getById(payClassifyId);
        if(userMoney.getEarningsMoney().doubleValue()<payClassify.getPrice().doubleValue()){
            return Result.error("收益不足，请重新输入！");
        }
        String time = DateUtils.format(new Date());
        BigDecimal payMoney=payClassify.getMoney();
        if(payClassify.getGiveMoney()!=null && payClassify.getGiveMoney().doubleValue()>0){
            payMoney=payMoney.add(payClassify.getGiveMoney());
        }
        updateEarningsMoney(2, userId, payClassify.getPrice().doubleValue());
        UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
        userMoneyDetails.setUserId(userId);
        userMoneyDetails.setTitle("收益充值钻石");
        userMoneyDetails.setContent("扣除收益："+payClassify.getPrice()+"，到账钻石："+payMoney);
        userMoneyDetails.setType(2);
        userMoneyDetails.setClassify(10);
        userMoneyDetails.setMoney(payClassify.getPrice());
        userMoneyDetails.setCreateTime(time);
        userMoneyDetailsService.save(userMoneyDetails);

        baseMapper.updateMayMoney(1, userId, payMoney.doubleValue());
        userMoneyDetails = new UserMoneyDetails();
        userMoneyDetails.setUserId(userId);
        userMoneyDetails.setTitle("收益充值钻石");
        userMoneyDetails.setContent("扣除收益:" + payClassify.getPrice() + ",到账钻石：" + payMoney);
        userMoneyDetails.setType(1);
        userMoneyDetails.setMoney(payMoney);
        userMoneyDetails.setCreateTime(time);
        userMoneyDetailsService.save(userMoneyDetails);
        return Result.success();
    }

    @Override
    public void updateMoney(int i, Long userId, double money) {
        baseMapper.updateMayMoney(i, userId, money);
    }

    @Override
    public void updateEarningsMoney(int i, Long userId, double money) {
        baseMapper.updateEarningsMoney(i, userId, money);
    }

    @Override
    public UserMoney selectUserMoneyByUserId(Long userId) {
        return baseMapper.selectOne(new QueryWrapper<UserMoney>().eq("user_id", userId));
    }

    @Override
    public Double selectMyProfit(Long userId) {

        return userMoneyDetailsDao.selectMyProfit(userId);
    }

    @Override
    public synchronized Result payTakingOrder(Long userId, Long orderId) {
        //时间类型
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //查看订单
        Orders orders = ordersDao.selectOne(new QueryWrapper<Orders>().eq("orders_id", orderId));
        if (orders == null) {
            return Result.error("订单不存在！");
        } else {
            // 会员已改为人民币直付，禁止旧客户端继续通过柚币余额开通。
            if (!Long.valueOf(1L).equals(orders.getOrdersType())) {
                return Result.error("会员购买请使用支付宝支付");
            }
            //如果是接单订单
            if (orders.getOrdersType() == 1) {
                if (orders.getState().equals("1")) {
                    return Result.error("订单进行中！");
                } else if (orders.getState().equals("2")) {
                    return Result.error("订单已完成！");
                } else if (orders.getState().equals("3")) {
                    return Result.error("订单已退款！");
                } else {
                    //订单状态为待支付
                    //查看用户账户余额
                    UserMoney userMoney = selectUserMoneyByUserId(userId);
                    if (userMoney == null) {
                        return Result.error("用户钱包信息不存在！");
                    } else {
                        //用户余额
                        BigDecimal money = userMoney.getMoney();
                        int i = money.compareTo(orders.getPayMoney());
                        if (i >= 0) {
                            //修改我的的余额
                            baseMapper.updateMayMoney(2, userId, orders.getPayMoney().doubleValue());
                            //设置订单状态
                            orders.setState("1");
                            //设置更新时间
                            orders.setUpdateTime(simpleDateFormat.format(new Date()));
                            //更新到订单表中
                            ordersDao.updateById(orders);
                            return Result.success("支付成功!");

                        } else {
                            return Result.error("余额不足!");
                        }
                    }
                }
            } else {
                //会员订单
                if (orders.getState().equals("1")) {
                    return Result.error("订单进行中！");
                } else if (orders.getState().equals("2")) {
                    return Result.error("订单已完成！");
                } else if (orders.getState().equals("3")) {
                    return Result.error("订单已退款！");
                } else {
                    //订单的状态为待支付
                    //查看用户账户余额
                    UserMoney userMoney = selectUserMoneyByUserId(userId);
                    //用户余额
                    BigDecimal money = userMoney.getMoney();
                    int i = money.compareTo(orders.getPayMoney());
                    if (i >= 0) {
                        Long vipTypeId = orders.getVipDetailsId();
                        //查看要开通会员类型
                        VipDetails vipDetails = vipDetailsDao.selectOne(new QueryWrapper<VipDetails>().eq("id", vipTypeId));
                        UserEntity userEntity = userService.selectUserById(userId);
                        UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
                        MessageInfo messageInfo=new MessageInfo();
                        userMoneyDetails.setMoney(orders.getPayMoney());
                        userMoneyDetails.setUserId(orders.getUserId());
                        if ((vipDetails.getVipNameType().equals(0))) {
                            //月
                            userMoneyDetails.setContent("开通月卡会员");
                            messageInfo.setContent("订单下单成功");
                        } else if ((vipDetails.getVipNameType()).equals(1)) {
                            //季
                            userMoneyDetails.setContent("开通季卡会员");
                            messageInfo.setContent("订单下单成功");
                        } else {
                            //年
                            userMoneyDetails.setContent("开通年费会员");
                            messageInfo.setContent("开通年费会员");
                        }
                        userMoneyDetails.setTitle("开通会员");
                        userMoneyDetails.setType(2);
                        userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
                        userMoneyDetailsService.save(userMoneyDetails);
                        messageInfo.setTitle("开通会员");
                        messageInfo.setState(String.valueOf(4));
                        messageInfo.setUserName(userEntity.getUserName());
                        messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
                        messageInfo.setCreateAt(simpleDateFormat.format(new Date()));
                        messageInfo.setIsSee("0");
                        messageService.saveBody(messageInfo);
                        if(StringUtil.isNotBlank(userEntity.getClientid())){
                            userService.pushToSingle(messageInfo.getTitle(),messageInfo.getContent(),userEntity.getClientid());
                        }
                        //修改我的的余额
                        baseMapper.updateMayMoney(2, userId, orders.getPayMoney().doubleValue());
                        //设置订单状态
                        orders.setState("2");
                        //设置更新时间
                        orders.setUpdateTime(simpleDateFormat.format(new Date()));
                        //更新到订单表中
                        ordersDao.updateById(orders);
                        //查看会员类型

                        //查看用户是否是会员
                        UserVip userVip = userVipDao.selectOne(new QueryWrapper<UserVip>().eq("user_id", userId));
                        //日历
                        Calendar cal = Calendar.getInstance();
                        if (userVip != null) {
                            //是会员
                            //查看会员到期时间
                            Date endDate = null;
                            try {
                                endDate = simpleDateFormat.parse(userVip.getEndTime());
                            } catch (Exception e) {
                                e.getMessage();
                            }
                            //查看会员是否到期
                            if (endDate != null && System.currentTimeMillis() < (endDate.getTime())) {
                                //没有到期
                                if (vipDetails != null) {
                                    //设置会员到期时间到日历
                                    cal.setTime(endDate);
                                    //判断会员的续费时间
                                    if ((vipDetails.getVipNameType().equals(0))) {
                                        //月
                                        cal.add(Calendar.MONTH, 1);
                                    } else if ((vipDetails.getVipNameType()).equals(1)) {
                                        //季
                                        cal.add(Calendar.MONTH, 3);
                                    } else {
                                        //年
                                        cal.add(Calendar.YEAR, 1);
                                    }
                                    //设置会员的到期时间
                                    userVip.setEndTime(simpleDateFormat.format(cal.getTime()));
                                    userVip.setIsVip(1);
                                    //更新会员信息
                                    userVipDao.updateById(userVip);
                                    //填写邀请码则奖励金币 没有则不奖励
                                    addMoney(userId, vipDetails);
                                    return Result.success("开通成功！");
                                } else {
                                    return Result.error("会员类型详情为空！");
                                }
                                //没有开通过会员 或会员已经到期
                            } else {
                                //到期了
                                //将现在的时间设置到日历中去
                                cal.setTime(new Date());
                                //判断会员续费的时间
                                if ((vipDetails.getVipNameType()).equals(0)) {
                                    //月
                                    cal.add(Calendar.MONTH, 1);
                                } else if ((vipDetails.getVipNameType()).equals(1)) {
                                    //季
                                    cal.add(Calendar.MONTH, 3);
                                } else {
                                    //年
                                    cal.add(Calendar.YEAR, 1);
                                }
                                //设置会员的到期时间
                                userVip.setEndTime(simpleDateFormat.format(cal.getTime()));
                                userVip.setIsVip(1);
                                //更新会员信息
                                userVipDao.updateById(userVip);
                                //填写邀请码则奖励金币 没有则不奖励
                                addMoney(userId, vipDetails);
                                return Result.success("开通成功！");
                            }

                        } else {
                            //不是会员
                            //创建会员对象
                            UserVip userVip1 = new UserVip();
                            //设置会员类型
                            userVip1.setVipNameType(vipDetails.getVipNameType());
                            //设置开通会员的用户id
                            userVip1.setUserId(userId);
                            //设置会员的购买时间
                            userVip1.setCreateTime(simpleDateFormat.format(new Date()));
                            //将现在时间设置到日历中
                            cal.setTime(new Date());
                            //判断会员的续费时间
                            if ((vipDetails.getVipNameType()).equals(0)) {
                                //月
                                cal.add(Calendar.MONTH, 1);
                            } else if ((vipDetails.getVipNameType()).equals(1)) {
                                //季
                                cal.add(Calendar.MONTH, 3);
                            } else {
                                //年
                                cal.add(Calendar.YEAR, 1);
                            }
                            //设置会员的到期时间
                            userVip1.setEndTime(simpleDateFormat.format(cal.getTime()));
                            userVip1.setIsVip(1);
                            //设置会员
                            userVipDao.insert(userVip1);
                            //填写邀请码则奖励金币 没有则不奖励
                            addMoney(userId, vipDetails);
                            return Result.success("开通成功！");
                        }

                    } else {
                        return Result.error("余额不足!");
                    }


                }
            }

        }
    }

    /**
     * 支付宝异步通知验签、验金额成功后调用。这里只开通会员，不扣除柚币。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized Result completePaidVipOrder(Long orderId) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Orders orders = ordersDao.selectById(orderId);
        if (orders == null || orders.getOrdersType() == null || orders.getOrdersType() != 2L) {
            return Result.error("会员订单不存在");
        }
        if ("2".equals(orders.getState())) {
            return Result.success("会员订单已处理");
        }
        if (!"0".equals(orders.getState())) {
            return Result.error("会员订单状态异常");
        }

        VipDetails vipDetails = vipDetailsDao.selectById(orders.getVipDetailsId());
        UserEntity userEntity = userService.selectUserById(orders.getUserId());
        if (vipDetails == null || userEntity == null) {
            return Result.error("会员订单数据异常");
        }

        // 先标记完成；本方法有事务保护，后续失败时会整体回滚。
        orders.setState("2");
        orders.setUpdateTime(simpleDateFormat.format(new Date()));
        ordersDao.updateById(orders);

        String memberText;
        if (vipDetails.getVipNameType().equals(0)) {
            memberText = "开通月卡会员";
        } else if (vipDetails.getVipNameType().equals(1)) {
            memberText = "开通季卡会员";
        } else {
            memberText = "开通年费会员";
        }

        UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
        userMoneyDetails.setMoney(orders.getPayMoney());
        userMoneyDetails.setUserId(orders.getUserId());
        userMoneyDetails.setContent("支付宝" + memberText);
        userMoneyDetails.setTitle("开通会员");
        userMoneyDetails.setType(2);
        userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
        userMoneyDetailsService.save(userMoneyDetails);

        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setContent(memberText + "成功");
        messageInfo.setTitle("开通会员");
        messageInfo.setState(String.valueOf(4));
        messageInfo.setUserName(userEntity.getUserName());
        messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
        messageInfo.setCreateAt(simpleDateFormat.format(new Date()));
        messageInfo.setIsSee("0");
        messageService.saveBody(messageInfo);
        if (StringUtil.isNotBlank(userEntity.getClientid())) {
            userService.pushToSingle(messageInfo.getTitle(), messageInfo.getContent(), userEntity.getClientid());
        }

        UserVip userVip = userVipDao.selectOne(new QueryWrapper<UserVip>().eq("user_id", orders.getUserId()));
        Calendar cal = Calendar.getInstance();
        Date baseDate = new Date();
        if (userVip != null && userVip.getEndTime() != null) {
            try {
                Date currentEnd = simpleDateFormat.parse(userVip.getEndTime());
                if (currentEnd.after(baseDate)) {
                    baseDate = currentEnd;
                }
            } catch (ParseException e) {
                log.warn("会员到期时间格式异常，将从当前时间续费: {}", userVip.getEndTime());
            }
        }
        cal.setTime(baseDate);
        if (vipDetails.getVipNameType().equals(0)) {
            cal.add(Calendar.MONTH, 1);
        } else if (vipDetails.getVipNameType().equals(1)) {
            cal.add(Calendar.MONTH, 3);
        } else {
            cal.add(Calendar.YEAR, 1);
        }

        if (userVip == null) {
            userVip = new UserVip();
            userVip.setUserId(orders.getUserId());
            userVip.setCreateTime(simpleDateFormat.format(new Date()));
            userVip.setVipNameType(vipDetails.getVipNameType());
            userVip.setEndTime(simpleDateFormat.format(cal.getTime()));
            userVip.setIsVip(1);
            userVipDao.insert(userVip);
        } else {
            userVip.setVipNameType(vipDetails.getVipNameType());
            userVip.setEndTime(simpleDateFormat.format(cal.getTime()));
            userVip.setIsVip(1);
            userVipDao.updateById(userVip);
        }

        // 保留安卓原有邀请奖励规则；会员费用本身不再扣除柚币。
        addMoney(orders.getUserId(), vipDetails, orders.getPayMoney());
        return Result.success("开通成功！");
    }

    @Override
    public PageUtils balanceDetailed(Long userId, Page<UserMoneyDetails> pages,Integer classify) {
        return new PageUtils(userMoneyDetailsDao.selectPage(pages,new QueryWrapper<UserMoneyDetails>()
                .eq("user_id",userId).isNull(classify==null,"classify")
                .in(classify!=null && classify==10,"classify",1,10,5,6)
                .eq(classify!=null && classify!=10,"classify",classify)
                .orderByDesc("create_time ")));
    }
    @Override
    public Result profitDetailed(Long userId, IPage ipage) {
        //收益明细
        return null;
    }


    /**
     * 开通成功会员后  奖励金币
     *
     * @param userId
     * @param
     */
    public void addMoney(Long userId, VipDetails vipDetails) {
        addMoney(userId, vipDetails, vipDetails.getMoney());
    }

    private void addMoney(Long userId, VipDetails vipDetails, BigDecimal paymentAmount) {
        //是否填写过邀请码
        BigDecimal add=paymentAmount;
        String time = DateUtils.format(new Date());
        UserEntity userEntity = userDao.selectById(userId);
        if (userEntity.getInviterCode() != null) {
            //直属
            UserEntity zhiUser = userService.queryByInvitationCode(userEntity.getInviterCode());
            if(zhiUser!=null){
                if(zhiUser.getZhiRate().doubleValue()!=0 && zhiUser.getZhiRate().doubleValue()>0){
                    BigDecimal peiMoney=add.multiply(zhiUser.getZhiRate()).setScale(2,BigDecimal.ROUND_DOWN);;
                    updateEarningsMoney(1,zhiUser.getUserId(),peiMoney.doubleValue());
                    UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                    userMoneyDetails.setUserId(zhiUser.getUserId());
                    userMoneyDetails.setByUserId(zhiUser.getUserId());
                    userMoneyDetails.setTitle("一级佣金邀请开通会员完成："+userEntity.getUserName());
                    userMoneyDetails.setContent("一级佣金邀请开通会员完成，到账金额：" +peiMoney);
                    userMoneyDetails.setType(1);
                    userMoneyDetails.setClassify(10);
                    userMoneyDetails.setMoney(peiMoney);
                    userMoneyDetails.setCreateTime(time);
                    userMoneyDetailsService.save(userMoneyDetails);
                }
                //非直属
                UserEntity feiUser = userService.queryByInvitationCode(zhiUser.getInviterCode());
                if(feiUser!=null){
                    if(feiUser.getFeiRate().doubleValue()!=0 && feiUser.getFeiRate().doubleValue()>0){
                        BigDecimal peiMoney=add.multiply(feiUser.getFeiRate()).setScale(2,BigDecimal.ROUND_DOWN);;
                        updateEarningsMoney(1,feiUser.getUserId(),peiMoney.doubleValue());
                        UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                        userMoneyDetails.setUserId(feiUser.getUserId());
                        userMoneyDetails.setByUserId(feiUser.getUserId());
                        userMoneyDetails.setTitle("二级佣金邀请开通会员完成："+userEntity.getUserName());
                        userMoneyDetails.setContent("二级佣金邀请开通会员完成，到账金额：" +peiMoney);
                        userMoneyDetails.setType(1);
                        userMoneyDetails.setClassify(10);
                        userMoneyDetails.setMoney(peiMoney);
                        userMoneyDetails.setCreateTime(time);
                        userMoneyDetailsService.save(userMoneyDetails);
                    }
                }
            }
        } else {
            log.error("没有填写过邀请码 无法奖励金币");
        }
    }
}
