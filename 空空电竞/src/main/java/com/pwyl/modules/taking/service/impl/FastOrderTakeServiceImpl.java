package com.pwyl.modules.taking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.app.service.UserService;
import com.pwyl.modules.taking.dao.FastOrderTakeDao;
import com.pwyl.modules.taking.dao.OrderTakingDao;
import com.pwyl.modules.taking.entity.FastOrder;
import com.pwyl.modules.taking.entity.FastOrderTake;
import com.pwyl.modules.taking.entity.OrderTaking;
import com.pwyl.modules.taking.service.FastOrderService;
import com.pwyl.modules.taking.service.FastOrderTakeService;
import com.pwyl.modules.taking.service.GameService;
import com.pwyl.modules.taking.service.OrderTakingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
@Slf4j
public class FastOrderTakeServiceImpl extends ServiceImpl<FastOrderTakeDao, FastOrderTake> implements FastOrderTakeService {

    @Autowired
    private FastOrderService fastOrderService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderTakingDao orderTakingDao;
    private ReentrantReadWriteLock reentrantReadWriteLock=new ReentrantReadWriteLock(true);

    @Override
    public Result insertFastOrderTake(FastOrderTake fastOrderTake){
        reentrantReadWriteLock.writeLock().lock();
        try{
            FastOrder fastOrder = fastOrderService.getById(fastOrderTake.getFastOrderId());
            Integer gameCount = orderTakingDao.selectCount(new QueryWrapper<OrderTaking>().eq("game_id", fastOrder.getGameId()).eq("status",0).eq("user_id",fastOrderTake.getUserId()));
            if(gameCount==0){
                return Result.error("您还没有发布过这个游戏的服务，无法抢单！");
            }
            if(fastOrder.getStatus()!=1){
                return Result.error("抢单失败，当前订单已经匹配成功！");
            }
            Integer count = baseMapper.selectCount(new QueryWrapper<FastOrderTake>().eq("fast_order_id", fastOrderTake.getFastOrderId()).eq("user_id", fastOrderTake.getUserId()));
            if(count>0){
                return Result.error("您已经抢过单了！");
            }
            fastOrderTake.setStatus(1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            fastOrderTake.setCreateTime(sdf.format(new Date()));
            baseMapper.insert(fastOrderTake);
            return Result.success();
        }catch (Exception e){
            e.printStackTrace();
            log.error("发起抢单异常："+e.getMessage(),e);
        }finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return Result.error("系统繁忙，请稍后再试！");
    }

    @Override
    public Result cancelFastOrderTake(Long fastOrderTakeId){
        FastOrderTake fastOrderTake = baseMapper.selectById(fastOrderTakeId);
        if(fastOrderTake!=null){
            if(fastOrderTake.getStatus()==2){
                return Result.error("订单已经匹配成功，请前往我的订单中进行操作！");
            }
            fastOrderTake.setStatus(3);
        }
        return Result.success();
    }

    @Override
    public Result selectFastOrderTakeList(Long fastOrderId){
        FastOrder fastOrder = fastOrderService.getById(fastOrderId);
        String[] split = fastOrder.getMoney().split("~");
        fastOrder.setMinMoney(new BigDecimal(split[0]));
        fastOrder.setMaxMoney(new BigDecimal(split[1]));
        List<OrderTaking> orderTakings = baseMapper.selectFastOrderTakeList(fastOrder);
        return Result.success().put("data",orderTakings);
    }

    @Override
    public Result selectFastOrderTakeLists(Long fastOrderId){
        FastOrder fastOrder = fastOrderService.getById(fastOrderId);
        String[] split = fastOrder.getMoney().split("~");
        fastOrder.setMinMoney(new BigDecimal(split[0]));
        fastOrder.setMaxMoney(new BigDecimal(split[1]));
        List<OrderTaking> orderTakings = baseMapper.selectFastOrderTakeList(fastOrder);
        OrderTaking orderTaking=null;
        if(orderTakings.size()>=1){
            orderTaking=orderTakings.get(0);
        }
        return Result.success().put("data",orderTaking);
    }


    @Override
    public Result selectFastOrderTakePage(Integer page,Integer limit,FastOrder fastOrder){
        Page<OrderTaking> pages=new Page<>(page,limit);
        return Result.success().put("data",baseMapper.selectFastOrderTakePage(pages,fastOrder));
    }




}
