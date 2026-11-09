package com.pwyl.modules.pay.apple.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.pwyl.modules.app.dao.UserVipDao;
import com.pwyl.modules.app.dao.VipDetailsDao;
import com.pwyl.modules.app.entity.UserMoney;
import com.pwyl.modules.app.entity.UserMoneyDetails;
import com.pwyl.modules.app.entity.UserVip;
import com.pwyl.modules.app.entity.VipDetails;
import com.pwyl.modules.app.service.UserMoneyDetailsService;
import com.pwyl.modules.app.service.UserMoneyService;
import com.pwyl.modules.pay.apple.dao.AppleIapOrderDao;
import com.pwyl.modules.pay.apple.dto.Java11VerifyResponse;
import com.pwyl.modules.pay.apple.entity.AppleIapOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 苹果内购资产发放服务，所有数据库操作在同一事务内原子完成。
 * 幂等保障：
 * 1. 订单状态条件更新(3 -> 4)，并发/重试只有一次进入发放逻辑；
 * 2. user_money_details.transaction_id 唯一索引兜底柚币不重复到账；
 * 3. 会员行 user_id 唯一索引 + select for update 行锁，保证时长叠加不丢失。
 */
@Slf4j
@Service
public class AppleIapGrantService {

    private final AppleIapOrderDao orderDao;
    private final UserMoneyService userMoneyService;
    private final UserMoneyDetailsService userMoneyDetailsService;
    private final UserVipDao userVipDao;
    private final VipDetailsDao vipDetailsDao;

    public AppleIapGrantService(AppleIapOrderDao orderDao,
                                UserMoneyService userMoneyService,
                                UserMoneyDetailsService userMoneyDetailsService,
                                UserVipDao userVipDao,
                                VipDetailsDao vipDetailsDao) {
        this.orderDao = orderDao;
        this.userMoneyService = userMoneyService;
        this.userMoneyDetailsService = userMoneyDetailsService;
        this.userVipDao = userVipDao;
        this.vipDetailsDao = vipDetailsDao;
    }

    /**
     * 发放权益。若订单已被其他线程/任务发放，直接返回最新订单，不重复发放。
     */
    @Transactional(rollbackFor = Exception.class)
    public AppleIapOrder grant(AppleIapOrder order, Java11VerifyResponse verified) {
        String now = IapTimeUtils.now();

        // 第一步：条件抢占发货权，只有 status=3(待发货) 的订单能更新为 4(已发货)
        UpdateWrapper<AppleIapOrder> lockUpdate = new UpdateWrapper<AppleIapOrder>()
                .eq("id", order.getId())
                .eq("status", AppleIapOrder.STATUS_PENDING_DELIVER)
                .set("status", AppleIapOrder.STATUS_DELIVERED)
                .set("deliver_time", now)
                .set("update_time", now);
        int locked = orderDao.update(null, lockUpdate);
        if (locked == 0) {
            // 已被其他线程/定时任务处理，直接返回当前状态，不重复发放
            return orderDao.selectById(order.getId());
        }

        AppleIapOrder latest;
        if (AppleIapOrder.BIZ_COIN == order.getBizType()) {
            latest = grantCoin(order, verified, now);
        } else if (AppleIapOrder.BIZ_MEMBERSHIP == order.getBizType()) {
            latest = grantMembership(order, verified, now);
        } else {
            throw AppleIapException.retry("未知的苹果内购业务类型：" + order.getBizType());
        }
        log.info("Apple IAP 发货成功 orderId={}, userId={}, transactionId={}, productId={}",
                order.getId(), order.getUserId(), order.getTransactionId(), order.getProductId());
        return latest;
    }

    /**
     * 柚币(消耗型)发放：原子加钱 + 写带 transactionId 的唯一流水。
     */
    private AppleIapOrder grantCoin(AppleIapOrder order, Java11VerifyResponse verified, String now) {
        int grantValue = verified.getGrantValue();
        if (grantValue <= 0) {
            throw AppleIapException.permanent("Apple 商品配置的柚币数量不正确：" + verified.getProductId());
        }
        BigDecimal amount = BigDecimal.valueOf(grantValue);

        UserMoney wallet = userMoneyService.selectUserMoneyByUserId(order.getUserId());
        if (wallet == null) {
            throw AppleIapException.retry("用户钱包不存在，柚币发放失败");
        }

        // 原子加钱：money = money + amount
        userMoneyService.updateMoney(1, order.getUserId(), amount.doubleValue());

        UserMoneyDetails detail = new UserMoneyDetails();
        detail.setUserId(order.getUserId());
        detail.setTitle("Apple内购充值");
        detail.setContent("Apple内购商品：" + verified.getProductId() + "，到账柚子：" + amount.toPlainString());
        detail.setType(1);
        detail.setState(2);
        detail.setMoney(amount);
        detail.setTransactionId(order.getTransactionId());
        detail.setCreateTime(now);
        if (!userMoneyDetailsService.save(detail)) {
            throw AppleIapException.retry("写入钱包明细失败，柚币发放失败");
        }

        UpdateWrapper<AppleIapOrder> finish = new UpdateWrapper<AppleIapOrder>()
                .eq("id", order.getId())
                .set("coin_amount", amount)
                .set("grant_value", grantValue)
                .set("update_time", now);
        orderDao.update(null, finish);
        return orderDao.selectById(order.getId());
    }

    /**
     * 自动续期会员发放：Apple 返回的订阅到期时间是唯一可信来源。
     *
     * 沙盒会在几分钟内快速产生多笔续期交易，不能按每笔交易再叠加
     * 1/3/12 个自然月，否则一次订阅会被错误延长到数年以后。
     * 这里只允许更晚的 Apple 到期时间推进会员期限；恢复旧交易时不会回退，
     * 同一 transactionId 仍由订单状态机保证只发放一次。
     */
    private AppleIapOrder grantMembership(AppleIapOrder order, Java11VerifyResponse verified, String now) {
        int months = verified.getGrantValue();
        Integer vipNameType = resolveVipNameType(verified);
        if (vipNameType == null || months <= 0) {
            throw AppleIapException.retry("不支持的 Apple 会员商品或时长：" + verified.getProductId());
        }
        String appleEndTime = IapTimeUtils.formatInstant(verified.getExpiresDate());
        if (appleEndTime == null) {
            throw AppleIapException.retry("Apple 会员订阅缺少有效到期时间");
        }
        Date appleEndDate = IapTimeUtils.parseLenient(appleEndTime);
        if (!appleEndDate.after(new Date())) {
            throw AppleIapException.permanent("该 Apple 会员订阅已过期");
        }
        VipDetails vipDetails = vipDetailsDao.selectOne(
                new QueryWrapper<VipDetails>().eq("vip_name_type", vipNameType));
        if (vipDetails == null) {
            throw AppleIapException.retry("会员类型配置不存在：vipNameType=" + vipNameType);
        }

        // 幂等初始化会员行(并发首购只插入一条)，随后行锁锁定
        UserVip init = new UserVip();
        init.setUserId(order.getUserId());
        init.setVipNameType(vipNameType);
        init.setIsVip(1);
        init.setCreateTime(now);
        init.setEndTime(now);
        userVipDao.insertIgnoreVip(init);

        UserVip userVip = userVipDao.selectForUpdate(order.getUserId());
        if (userVip == null) {
            throw AppleIapException.retry("会员记录初始化失败，请稍后重试");
        }

        // 仅允许 Apple 的更晚到期时间推进权益，历史恢复交易不能回退会员期限。
        String newEndTime = appleEndTime;
        boolean appleExpiryExtendsMembership = true;
        if (userVip.getEndTime() != null) {
            Date currentEnd = IapTimeUtils.parseLenient(userVip.getEndTime());
            if (!appleEndDate.after(currentEnd)) {
                newEndTime = userVip.getEndTime();
                appleExpiryExtendsMembership = false;
            }
        }

        if (appleExpiryExtendsMembership) {
            userVip.setVipNameType(vipNameType);
        }
        userVip.setEndTime(newEndTime);
        userVip.setIsVip(1);
        int changed = userVipDao.updateById(userVip);
        if (changed != 1) {
            throw AppleIapException.retry("会员开通失败，请稍后重试");
        }

        UpdateWrapper<AppleIapOrder> finish = new UpdateWrapper<AppleIapOrder>()
                .eq("id", order.getId())
                .set("vip_name_type", vipNameType)
                .set("vip_end_time", newEndTime)
                .set("grant_value", months)
                .set("update_time", now);
        orderDao.update(null, finish);
        return orderDao.selectById(order.getId());
    }

    /**
     * 会员商品映射：优先按 Java11 商品ID映射，兼容按 grantValue 月数映射。
     * 0001=月会员(vipNameType=0)，0002=季会员(1)，0003=年会员(2)。
     */
    private Integer resolveVipNameType(Java11VerifyResponse verified) {
        String productId = verified.getProductId();
        if ("0001".equals(productId)) {
            return 0;
        }
        if ("0002".equals(productId)) {
            return 1;
        }
        if ("0003".equals(productId)) {
            return 2;
        }
        switch (verified.getGrantValue()) {
            case 1:
                return 0;
            case 3:
                return 1;
            case 12:
                return 2;
            default:
                return null;
        }
    }
}
