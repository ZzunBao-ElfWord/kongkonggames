package com.pwyl.modules.taking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwyl.common.utils.PageUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.dao.UserCertificationDao;
import com.pwyl.modules.app.dao.UserDao;
import com.pwyl.modules.app.entity.UserCertification;
import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.app.service.UserBrowseService;
import com.pwyl.modules.app.service.UserService;
import com.pwyl.modules.common.entity.CommonInfo;
import com.pwyl.modules.common.service.CommonInfoService;
import com.pwyl.modules.message.entity.MessageInfo;
import com.pwyl.modules.message.service.MessageService;
import com.pwyl.modules.orders.dao.OrdersDao;
import com.pwyl.modules.orders.entity.Orders;
import com.pwyl.modules.search.service.AppSearchService;
import com.pwyl.modules.taking.dao.GameDao;
import com.pwyl.modules.taking.dao.OrderTakingDao;
import com.pwyl.modules.taking.entity.OrderTaking;
import com.pwyl.modules.taking.entity.OrderTakingReward;
import com.pwyl.modules.taking.response.MyReleaseResponse;
import com.pwyl.modules.taking.response.OrderTakingResponse;
import com.pwyl.modules.taking.response.TakingDetailsResponse;
import com.pwyl.modules.taking.response.TakingResponse;
import com.pwyl.modules.taking.service.OrderTakingRewardService;
import com.pwyl.modules.taking.service.OrderTakingService;
import com.pwyl.modules.utils.SenInfoCheckUtil;
import jodd.util.StringUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderTakingServiceImpl extends ServiceImpl<OrderTakingDao, OrderTaking> implements OrderTakingService {

    @Autowired
    private UserCertificationDao userCertificationDao;
    @Autowired
    private OrderTakingDao orderTakingDao;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private AppSearchService appSearchService;
    @Autowired
    private GameDao gameDao;
    @Autowired
    private UserBrowseService userBrowseService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private OrderTakingRewardService orderTakingRewardService;

    @Override
    public Result queryTaking(String city,String longitude, String latitude, Long userId, String like, String condition, Long bannerid, Long isRecommend, Long id, Page<OrderTaking> iPage, Long sex, String by,Integer classify) {
        String str = null;
        if (like != null) {
            str = "%" + like + "%";
            //添加搜索记录
            appSearchService.insetAppSearch(like, userId);
        }
        Integer con=null;
        if(StringUtils.isNotEmpty(condition)){
            con=Integer.parseInt(condition);
        }
        IPage<OrderTaking> i = orderTakingDao.queryTaking(iPage,city, longitude, latitude, str, con, bannerid, isRecommend, id, sex, by,classify);
        return Result.success().put("data", new PageUtils(i));
    }

    @Override
    public Result queryLowTaking(Integer page,Integer limit,String longitude,String latitude) {
        if (page == null || limit==null) {
            return Result.success().put("data", orderTakingDao.queryLowTaking(longitude,latitude));
        } else {
            return Result.success().put("data", new PageUtils(orderTakingDao.queryLowTakings(new Page<>(page,limit),longitude,latitude)));
        }
    }


    @Override
    public Result queryTakingDetails(Long id, Long userId,String longitude,String latitude) {
        OrderTaking takingDetailsResponse = baseMapper.queryTakingDetails(id,longitude,latitude);
        int rewardCount = orderTakingRewardService.count(new QueryWrapper<OrderTakingReward>().eq("order_taking_id", id));
        int myRewardCount=0;
        if(userId!=null){
            myRewardCount = orderTakingRewardService.count(new QueryWrapper<OrderTakingReward>().eq("order_taking_id", id).eq("user_id", userId));
            //添加浏览足迹
            userBrowseService.addAmount(userId,takingDetailsResponse.getUserId(), id);
            //添加访客
            userBrowseService.addVisitor(userId, takingDetailsResponse.getUserId());
        }
        takingDetailsResponse.setRewardCount(rewardCount);
        takingDetailsResponse.setMyRewardCount(myRewardCount);
        return Result.success().put("data",takingDetailsResponse);
    }

    @Override
    public Result insertOrderTaking(OrderTaking orderTaking) {
        //查看用户是否实名认证
        UserEntity userEntity = userService.selectUserById(orderTaking.getUserId());
        if (userEntity.getIsAuthentication()!=null && userEntity.getIsAuthentication()==1) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            BigDecimal money=orderTaking.getMoney();
            orderTaking.setOldMoney(money);
            //价格  (发布价格乘平台费率)
            CommonInfo rateInfo = commonInfoService.findOne(155);
            BigDecimal rate = money.multiply(new BigDecimal(rateInfo.getValue()));
            BigDecimal moneys = money.add(rate);
            orderTaking.setMoney(moneys);
            //计算会员价格
            CommonInfo rateMemberInfo = commonInfoService.findOne(156);
            BigDecimal rateMember = moneys.multiply(new BigDecimal(rateMemberInfo.getValue()));
            orderTaking.setMemberMoney(rateMember);
            //创建时间
            orderTaking.setCreateTime(simpleDateFormat.format(new Date()));
            //修改时间
            orderTaking.setUpdateTime(simpleDateFormat.format(new Date()));
            //接单状态0进行中1待审核2已取消
            orderTaking.setStatus(1);
            orderTaking.setIsdelete((long) 0);
            orderTaking.setIsRecommend("1");
            int i = baseMapper.insert(orderTaking);
            if (i > 0) {
                return Result.success("发布成功！");
            } else {
                return Result.error("发布失败！");
            }

        } else {
            return Result.error("请先进行实名认证!");
        }
    }

    @Override
    public Result selectMyRelease(Long userId, Long page, Long limit, String status) {
        Page<OrderTaking> iPage = new Page<>(page, limit);
        PageUtils pageUtils = new PageUtils(baseMapper.selectMyRelease(iPage, userId, status));
        return Result.success().put("data", pageUtils);

    }


    @Override
    public Result deleteOrderTaking(Long id) {
        if (id == null) {
            return Result.error("删除id为空");
        } else {
            int i = baseMapper.deleteById(id);
            if (i > 0) {
                return Result.success();
            } else {
                return Result.error();
            }
        }
    }

    @Override
    public Result queryAllOrderTaking(Integer page,Integer limit, Long gameId, Long status, String userName, Long userId,Integer classify,String longitude,String latitude) {
        String str = null;
        if (userName != null && !(userName.equals(""))) {
            str = "%" + userName + "%";
        }
        IPage<OrderTaking> iPage1 = baseMapper.queryAllOrderTaking(new Page<OrderTaking>(page,limit), gameId, status, str,userId,classify,longitude,latitude);
        return Result.success().put("data", new PageUtils(iPage1));
    }

    @Override
    public Result auditorOrderTaking(Long id, Integer status,String content) {
        OrderTaking orderTaking = baseMapper.selectById(id);
        if (orderTaking == null) {
            return Result.error("接单信息不存在！");
        }else if(orderTaking.getStatus()!=1){
            return Result.error("已经审核过了！");
        }else {
            orderTaking.setStatus(status);
            orderTaking.setContent(content);
            baseMapper.updateById(orderTaking);

            UserEntity userEntity = userService.selectUserById(orderTaking.getUserId());
            MessageInfo messageInfo=new MessageInfo();
            if(status==0){
                messageInfo.setContent("您发布的订单审核通过了！");
            }else{
                messageInfo.setContent("您发布的订单审核不通过！");
            }
            messageInfo.setTitle("发布信息审核通知");
            messageInfo.setState(String.valueOf(4));
            messageInfo.setUserName(userEntity.getUserName());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            messageInfo.setCreateAt(simpleDateFormat.format(new Date()));
            messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
            messageInfo.setIsSee("0");
            messageService.saveBody(messageInfo);
            if(StringUtil.isNotBlank(userEntity.getClientid())){
                userService.pushToSingle(messageInfo.getTitle(),messageInfo.getContent(),userEntity.getClientid());
            }

            return Result.success();
        }
    }

    @Override
    public Result updateTakingStatus(Long id, Integer status,String content) {
        OrderTaking orderTaking = new OrderTaking();
        orderTaking.setId(id);
        orderTaking.setStatus(status);
        orderTaking.setContent(content);
        baseMapper.updateById(orderTaking);
        return Result.success();
    }

    @Override
    public Result deleteTaking(Long id) {
        OrderTaking orderTaking = baseMapper.selectById(id);
        if (orderTaking != null) {
            orderTaking.setIsdelete((long) 1);
            baseMapper.update(orderTaking, new QueryWrapper<OrderTaking>().eq("id", id));
            return Result.success();
        } else {
            return Result.error("订单不存在！");
        }
    }

    @Override
    public Result queryTakingOrder(Long id,Long userId) {
        OrderTaking orderTaking = baseMapper.selectById(id);
        if (orderTaking != null) {
            orderTaking.setGame(gameDao.selectById(orderTaking.getGameId()));
            int rewardCount = orderTakingRewardService.count(new QueryWrapper<OrderTakingReward>().eq("order_taking_id", id));
            int myRewardCount=0;
            if(userId!=null){
                myRewardCount = orderTakingRewardService.count(new QueryWrapper<OrderTakingReward>().eq("order_taking_id", id).eq("user_id", userId));
            }
            orderTaking.setRewardCount(rewardCount);
            orderTaking.setMyRewardCount(myRewardCount);
        }
        return Result.success().put("data", orderTaking);
    }

    @Override
    public Result updateTakingOrder(OrderTaking orderTaking) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BigDecimal money = orderTaking.getMoney();
        //价格
        orderTaking.setOldMoney(money);
        //价格  (发布价格乘平台费率)
        CommonInfo rateInfo = commonInfoService.findOne(155);
        BigDecimal rate = money.multiply(new BigDecimal(rateInfo.getValue()));
        BigDecimal moneys = money.add(rate);
        orderTaking.setMoney(moneys);
        //计算会员价格
        CommonInfo rateMemberInfo = commonInfoService.findOne(156);
        BigDecimal rateMember = moneys.multiply(new BigDecimal(rateMemberInfo.getValue()));
        orderTaking.setMemberMoney(rateMember);
        //创建时间
        orderTaking.setUpdateTime(simpleDateFormat.format(new Date()));
        //接单状态0进行中1待审核2已取消
        orderTaking.setStatus(1);
        baseMapper.updateById(orderTaking);
        return Result.success();
    }

    @Override
    public Result updateTakingOrders(OrderTaking orderTaking) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //创建时间
        orderTaking.setUpdateTime(simpleDateFormat.format(new Date()));
        baseMapper.updateById(orderTaking);
        return Result.success();
    }


    @Override
    public Result queryMyTakingOrder(Long userId, Long page, Long limit, Long status) {
        Page<OrderTaking> ipage = new Page<>(page, limit);
        IPage<OrderTaking> iPage = baseMapper.selectPage(ipage, new QueryWrapper<OrderTaking>().eq("status", 0).or().eq("status", 2));
        List<OrderTaking> orderTakings = iPage.getRecords();
        for (OrderTaking orderTaking : orderTakings) {
            if (orderTaking != null) {
                QueryWrapper queryWrapper = new QueryWrapper<Orders>().eq("order_taking_id", orderTaking.getId());
                if (status != null && status != 0) {
                    queryWrapper.eq("state", status);
                }
                List<Orders> orders = ordersDao.selectList(queryWrapper);
                for (Orders order : orders) {
                    UserEntity user = userDao.selectOne(new QueryWrapper<UserEntity>().eq("user_id", order.getUserId()));
                    order.setUser(user);
                }
                orderTaking.setOrders(orders);
            }
        }
        return Result.success().put("data", iPage);
    }
}
