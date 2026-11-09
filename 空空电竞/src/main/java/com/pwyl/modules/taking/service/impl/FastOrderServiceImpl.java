package com.pwyl.modules.taking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwyl.common.utils.DateUtils;
import com.pwyl.common.utils.PageUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.app.entity.UserMoney;
import com.pwyl.modules.app.entity.UserMoneyDetails;
import com.pwyl.modules.app.service.UserMoneyDetailsService;
import com.pwyl.modules.app.service.UserMoneyService;
import com.pwyl.modules.app.service.UserService;
import com.pwyl.modules.common.entity.CommonInfo;
import com.pwyl.modules.common.service.CommonInfoService;
import com.pwyl.modules.message.entity.MessageInfo;
import com.pwyl.modules.message.service.MessageService;
import com.pwyl.modules.taking.dao.FastOrderDao;
import com.pwyl.modules.taking.dao.FastOrderTakeDao;
import com.pwyl.modules.taking.entity.FastOrder;
import com.pwyl.modules.taking.entity.Game;
import com.pwyl.modules.taking.entity.OrderTaking;
import com.pwyl.modules.taking.service.FastOrderService;
import com.pwyl.modules.taking.service.FastOrderTakeService;
import com.pwyl.modules.taking.service.GameService;
import com.pwyl.modules.utils.SenInfoCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import weixin.popular.bean.message.templatemessage.TemplateMessageItem;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
@Slf4j
public class FastOrderServiceImpl extends ServiceImpl<FastOrderDao, FastOrder> implements FastOrderService {

    @Autowired
    private GameService gameService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private FastOrderTakeDao fastOrderTakeDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    private ReentrantReadWriteLock reentrantReadWriteLock=new ReentrantReadWriteLock(true);
    /**
     * 创建线程池
     */
    private ExecutorService newCachedThreadPool = new ThreadPoolExecutor(30, 100, 0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());

    @Override
    public Result selectMyFastCount(Long userId){
        return Result.success().put("data",baseMapper.selectFastOrderCountByUserIdAndTime(userId));
    }

    @Override
    public Result insertFastOrder(FastOrder fastOrder){
        reentrantReadWriteLock.writeLock().lock();
        try{
            FastOrder fastOrder1 = selectFastOrder(fastOrder.getUserId());
            if(fastOrder1!=null){
                return Result.error("当前有正在约玩的服务，请完成后再来发布吧！");
            }
            fastOrder.setFastMoney(BigDecimal.ZERO);
            //快速约玩是否付费
            String value = commonInfoService.findOne(278).getValue();
            if("是".equals(value)){
                //快速约玩每日免费次数
                boolean flag=false;
                String free = commonInfoService.findOne(280).getValue();
                if(StringUtils.isNotBlank(free)){
                    int i = Integer.parseInt(free);
                    //获取已经发布的次数
                    int myCount = baseMapper.selectFastOrderCountByUserIdAndTime(fastOrder.getUserId());
                    if(myCount>=i){
                        flag=true;
                    }
                }else{
                    flag=true;
                }
                if(flag){
                    //快速约玩收费金额
                    String moneyStr = commonInfoService.findOne(279).getValue();
                    BigDecimal money=new BigDecimal(moneyStr);
                    UserMoney userMoney = userMoneyService.selectUserMoneyByUserId(fastOrder.getUserId());
                    if(userMoney.getMoney().doubleValue()<money.doubleValue()){
                        return Result.error("账户余额不足！");
                    }
                    userMoneyService.updateMoney(2, fastOrder.getUserId(), money.doubleValue());
                    UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
                    userMoneyDetails.setMoney(money);
                    userMoneyDetails.setUserId(fastOrder.getUserId());
                    userMoneyDetails.setContent("扣除金额："+money);
                    userMoneyDetails.setTitle("发布快速约玩");
                    userMoneyDetails.setType(2);
                    userMoneyDetails.setCreateTime(DateUtils.format(new Date()));
                    userMoneyDetailsService.save(userMoneyDetails);
                    fastOrder.setFastMoney(money);
                }
            }
            fastOrder.setCreateTime(DateUtils.format(new Date()));
            fastOrder.setStatus(1);
            baseMapper.insert(fastOrder);
            String dateTime=fastOrder.getCreateTime();
            if(StringUtils.isNotBlank(fastOrder.getOrderTakingTime())){
                dateTime=fastOrder.getOrderTakingTime();
            }
            String finalDateTime = dateTime;
            Game game = gameService.getById(fastOrder.getGameId());
            newCachedThreadPool.submit(new Runnable() {
                @Override
                public void run() {
                    send(game,fastOrder.getUserId(), finalDateTime);
                }
            });

            return Result.success();
        }catch (Exception e){
            e.printStackTrace();
            log.error("发布约玩异常："+e.getMessage(),e);
        }finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return Result.error("系统繁忙，请稍后再试！");
    }


    public void send(Game game,Long userId,String dateTime){
        /**
         * 发布约玩 给所有的陪陪发送约玩消息
         */
        UserEntity user = userService.getById(userId);
        List<UserEntity> userEntityList = userService.list(new QueryWrapper<UserEntity>().eq("is_authentication", 1));
        for(UserEntity userEntity:userEntityList){
            try{
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setContent("用户："+user.getUserName()+" 发布约玩："+game.getGameName());
                messageInfo.setTitle("约玩通知");
                messageInfo.setState(String.valueOf(7));
                messageInfo.setUserName(userEntity.getUserName());
                messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
                messageInfo.setIsSee("0");
                messageService.saveBody(messageInfo);

                if (userEntity.getIsSendMsg() == null || userEntity.getIsSendMsg() == 1) {
                    //小程序订阅号消息推送
                    CommonInfo one = commonInfoService.findOne(728);
                    List<String> msgList = new ArrayList<>();
                    msgList.add(game.getGameName());//商品名称
                    msgList.add(dateTime);//下单时间
                    if (StringUtils.isNotEmpty(userEntity.getOpenId())) {
                        SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), one.getValue(), msgList, 5);
                    }
                    if(StringUtils.isNotEmpty(userEntity.getClientid())){
                        userService.pushToSingle("约玩通知","用户："+user.getUserName()+" 发布约玩："+game.getGameName(),userEntity.getClientid());
                    }
                }

                if(StringUtils.isNotBlank(userEntity.getWxOpenId())){
                    String apkey = commonInfoService.findOne(811).getValue();
                    LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
                    data.put("thing15", new TemplateMessageItem(game.getGameName(), "#d71345"));
                    data.put("time6", new TemplateMessageItem(dateTime, "#d71345"));
                    userService.sendWxMessage(apkey, data, userEntity.getWxOpenId());
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public Result selectFastOrderDetails(Long fastOrderId){
        FastOrder fastOrder = baseMapper.selectById(fastOrderId);
        fastOrder.setGame(gameService.getById(fastOrder.getGameId()));
        return Result.success().put("data",fastOrder);
    }


    @Override
    public Result selectFastOrderPage(Integer page,Integer limit,Long userId,Integer status,String userName,String phone,Integer templateType){
        Page<FastOrder> pages=new Page<>(page,limit);
        IPage<FastOrder> fastOrderIPage = baseMapper.selectFastOrderPage(pages, userId, status, userName, phone,templateType);
        return Result.success().put("data",new PageUtils(fastOrderIPage));
    }

    @Override
    public Result selectFastOrderTake(Integer page,Integer limit,Long userId,Integer templateType){
        Page<FastOrder> pages=new Page<>(page,limit);
        IPage<FastOrder> fastOrderIPage = baseMapper.selectFastOrderTake(pages, userId,templateType);
        return Result.success().put("data",new PageUtils(fastOrderIPage));
    }

    @Override
    public Result selectFastOrderTakeListByUserId(Integer page,Integer limit,Long userId,Integer status){
        Page<FastOrder> pages=new Page<>(page,limit);
        IPage<FastOrder> fastOrderIPage = baseMapper.selectFastOrderTakeListByUserId(pages, userId,status);
        return Result.success().put("data",new PageUtils(fastOrderIPage));
    }


    @Override
    public FastOrder selectFastOrder(Long userId){
        return baseMapper.selectOne(new QueryWrapper<FastOrder>().eq("user_id", userId).eq("status", 1).orderByDesc("create_time"));
    }


    @Override
    public Result cancelFastOrder(Long fastOrderId){
        FastOrder fastOrder = baseMapper.selectById(fastOrderId);
        if(fastOrder.getStatus()!=1){
            return Result.error("当前状态不允许取消！");
        }
        //判断 发布时是否支付快速约玩金额
        if(fastOrder.getFastMoney()!=null && fastOrder.getFastMoney().doubleValue()>0){
            //判断是否有陪玩抢单 如果有 则退款 没有则退款
            String[] split = fastOrder.getMoney().split("~");
            fastOrder.setMinMoney(new BigDecimal(split[0]));
            fastOrder.setMaxMoney(new BigDecimal(split[1]));
            List<OrderTaking> orderTakings = fastOrderTakeDao.selectFastOrderTakeList(fastOrder);
            if(orderTakings.size()==0){
                BigDecimal money=fastOrder.getFastMoney();
                //没有陪玩抢单 进行退款
                userMoneyService.updateMoney(1, fastOrder.getUserId(), money.doubleValue());
                UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
                userMoneyDetails.setMoney(money);
                userMoneyDetails.setUserId(fastOrder.getUserId());
                userMoneyDetails.setContent("退还金额："+money);
                userMoneyDetails.setTitle("取消快速约玩");
                userMoneyDetails.setType(1);
                userMoneyDetails.setCreateTime(DateUtils.format(new Date()));
                userMoneyDetailsService.save(userMoneyDetails);
            }
        }

        fastOrder.setStatus(3);
        baseMapper.updateById(fastOrder);
        fastOrderTakeDao.updateFastOrderTake(fastOrder.getFastOrderId());
        return Result.success();
    }


    @Scheduled(cron = "0 */1 * * * ?")
    public void overtimeFastOrder(){
        List<FastOrder> fastOrders = baseMapper.selectOverTimeFastOrder();
        for(FastOrder fastOrder:fastOrders){
            try{
                if(fastOrder.getFastMoney()!=null && fastOrder.getFastMoney().doubleValue()>0){
                    BigDecimal money=fastOrder.getFastMoney();
                    //没有陪玩抢单 进行退款
                    userMoneyService.updateMoney(1, fastOrder.getUserId(), money.doubleValue());
                    UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
                    userMoneyDetails.setMoney(money);
                    userMoneyDetails.setUserId(fastOrder.getUserId());
                    userMoneyDetails.setContent("退还金额："+money);
                    userMoneyDetails.setTitle("超时自动取消快速约玩");
                    userMoneyDetails.setType(1);
                    userMoneyDetails.setCreateTime(DateUtils.format(new Date()));
                    userMoneyDetailsService.save(userMoneyDetails);
                }
                fastOrder.setStatus(3);
                baseMapper.updateById(fastOrder);
                fastOrderTakeDao.updateFastOrderTake(fastOrder.getFastOrderId());
                UserEntity user = userService.selectUserById(fastOrder.getUserId());
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setContent("您发布的快速约玩超时，系统已自动取消！");
                messageInfo.setTitle("超时自动取消快速约玩");
                messageInfo.setState(String.valueOf(5));
                messageInfo.setUserName(user.getUserName());
                messageInfo.setUserId(String.valueOf(user.getUserId()));
                messageInfo.setCreateAt(DateUtils.format(new Date()));
                messageInfo.setIsSee("0");
                messageService.saveBody(messageInfo);
            }catch (Exception e){
                e.printStackTrace();
                log.error("超时取消异常："+e.getMessage(),e);
            }

        }
    }








}
