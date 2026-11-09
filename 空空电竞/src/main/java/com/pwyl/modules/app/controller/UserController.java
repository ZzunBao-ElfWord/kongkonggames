package com.pwyl.modules.app.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwyl.common.utils.PageUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.dao.UserCertificationDao;
import com.pwyl.modules.app.dao.UserMoneyDao;
import com.pwyl.modules.app.entity.*;
import com.pwyl.modules.app.response.HomeMessageResponse;
import com.pwyl.modules.app.response.UserMessageResponse;
import com.pwyl.modules.app.service.*;
import com.pwyl.modules.consortia.entity.Consortia;
import com.pwyl.modules.consortia.service.ConsortiaService;
import com.pwyl.modules.orders.service.OrdersService;
import com.pwyl.modules.pay.entity.PayDetails;
import com.pwyl.modules.pay.service.PayDetailsService;
import com.pwyl.modules.sys.controller.AbstractController;
import com.pwyl.modules.taking.dao.OrderTakingDao;
import com.pwyl.modules.talk.dao.TalkRepository;
import com.pwyl.modules.utils.AmountCalUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fang
 * @date 2020/7/30
 */
@RestController
@Api(value = "用户管理", tags = {"用户管理"})
@RequestMapping(value = "/user")
public class UserController  extends AbstractController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private PayDetailsService payDetailsService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private UserVipService userVipService;
    @Autowired
    private OrderTakingDao orderTakingDao;
    @Autowired
    private TalkRepository talkRepository;
    @Autowired
    private UserCertificationDao userCertificationDao;
    @Autowired
    private ConsortiaService consortiaService;
    @Autowired
    private UserCertificationService userCertificationService;

    @PostMapping("/setBabyPwd")
    @ApiOperation("开启青少年模式密码")
    public Result setBabyPwd(Long userId,String babyPwd){
        UserEntity user = userService.getById(userId);
        user.setBabyPwd(babyPwd);
        user.setBabyModel(1);
        userService.updateById(user);
        return Result.success();
    }

    @PostMapping("/updateBabyPwd")
    @ApiOperation("修改青少年模式密码")
    public Result updateBabyPwd(Long userId,String babyPwd){
        UserEntity user = userService.getById(userId);
        user.setBabyPwd(babyPwd);
        userService.updateById(user);
        return Result.success();
    }


    @PostMapping("/closeBaby")
    @ApiOperation("关闭青少年模式")
    public Result closeBabyPwd(Long userId){
        UserEntity user = userService.getById(userId);
        user.setBabyModel(2);
        userService.updateById(user);
        return Result.success();
    }

    @RequestMapping(value = "/selectUserByInvitationCode", method = RequestMethod.GET)
    @ApiOperation("获取用户详细信息")
    @ResponseBody
    public Result selectUserByInvitationCode(String invitationCode) {
        Map<String, Object> map = new HashMap<>();
        UserEntity userEntity = userService.queryByInvitationCode(invitationCode);
        Long userId=userEntity.getUserId();
        UserMoney userMoney = userMoneyService.selectUserMoneyByUserId(userId);
        Double money = 0.0;
        if (userMoney != null) {
            money = userMoney.getMoney().doubleValue();
        }
        Double earningsMoney = 0.0;
        if (userMoney != null) {
            earningsMoney = userMoney.getEarningsMoney().doubleValue();
        }
        //查询用户钱包
        Double m = money;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        //查询本月充值
        Double consume = payDetailsService.instantselectSumPay(date, userId);
        //查询本月提现
        Double income = userMoneyDetailsService.monthIncome(date, userId);
        //查询邀请人数
        int count = userService.queryInviterCount(userEntity.getInvitationCode());
        //本月接单
        int takeOrdersCount = ordersService.selectTakeOrdersCount(userId, date);
        //本月订单
        int myOrdersCount = ordersService.selectMyOrdersCount(userId, date);
        UserVip userVip = userVipService.getOne(new QueryWrapper<UserVip>().eq("user_id",userId));
        if(userVip!=null){
            userEntity.setIsVip(userVip.getIsVip());
            userEntity.setVipEndTime(userVip.getEndTime());
        }
        if(userEntity.getConsortiaId()!=null){
            Consortia consortia = consortiaService.getById(userEntity.getConsortiaId());
            if(consortia!=null){
                userEntity.setConsortiaName(consortia.getConsortiaName());
                userEntity.setConsortiaNo(consortia.getConsortiaNo());
            }
        }
        UserCertification userCertification = userCertificationDao.selectOne(new QueryWrapper<UserCertification>().eq("user_id", userId));
        map.put("userCertification", userCertification);
        map.put("userEntity", userEntity);
        map.put("money", m);
        map.put("earningsMoney", earningsMoney);
        map.put("consume", consume);
        map.put("income", income);
        map.put("count", count);
        map.put("takeOrdersCount", takeOrdersCount);
        map.put("myOrdersCount", myOrdersCount);
        return Result.success().put("data", map);
    }


    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @ApiOperation("获取用户详细信息")
    @ResponseBody
    public Result selectUserById(@ApiParam("用户id") @PathVariable Long userId) {
        Map<String, Object> map = new HashMap<>();
        UserEntity userEntity = userService.queryByUserId(userId);
        UserMoney userMoney = userMoneyService.selectUserMoneyByUserId(userId);
        Double money = 0.0;
        if (userMoney != null) {
            money = userMoney.getMoney().doubleValue();
        }
        Double earningsMoney = 0.0;
        if (userMoney != null) {
            earningsMoney = userMoney.getEarningsMoney().doubleValue();
        }
        //查询用户钱包
        Double m = money;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        //查询本月充值
        Double consume = payDetailsService.instantselectSumPay(date, userId);
        //查询本月提现
        Double income = userMoneyDetailsService.monthIncome(date, userId);
        //查询邀请人数
        int count = userService.queryInviterCount(userEntity.getInvitationCode());
        //本月接单
        int takeOrdersCount = ordersService.selectTakeOrdersCount(userId, date);
        //本月订单
        int myOrdersCount = ordersService.selectMyOrdersCount(userId, date);UserVip userVip = userVipService.getOne(new QueryWrapper<UserVip>().eq("user_id",userId));
        if(userVip!=null){
            userEntity.setIsVip(userVip.getIsVip());
            userEntity.setVipEndTime(userVip.getEndTime());
        }
        if(userEntity.getConsortiaId()!=null){
            Consortia consortia = consortiaService.getById(userEntity.getConsortiaId());
            if(consortia!=null){
                userEntity.setConsortiaName(consortia.getConsortiaName());
                userEntity.setConsortiaNo(consortia.getConsortiaNo());
            }
        }
        UserCertification userCertification = userCertificationDao.selectOne(new QueryWrapper<UserCertification>().eq("user_id", userId));
        map.put("userCertification", userCertification);
        map.put("userEntity", userEntity);
        map.put("money", m);
        map.put("earningsMoney", earningsMoney);
        map.put("consume", consume);
        map.put("income", income);
        map.put("count", count);
        map.put("takeOrdersCount", takeOrdersCount);
        map.put("myOrdersCount", myOrdersCount);
        return Result.success().put("data", map);
    }

    @RequestMapping(value = "/addCannotMoney/{userId}/{money}/{type}", method = RequestMethod.POST)
    @ApiOperation("修改金额")
    @ResponseBody
    public Result addCannotMoney(@PathVariable("userId") Long userId,@PathVariable("money") Double money,@PathVariable("type") Integer type){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(new Date());
        if(type==1){
            userMoneyService.updateMoney(1,userId,money);
            UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
            userMoneyDetails.setMoney(BigDecimal.valueOf(money));
            userMoneyDetails.setUserId(userId);
            userMoneyDetails.setContent("系统充值："+money);
            userMoneyDetails.setTitle("系统充值金额");
            userMoneyDetails.setType(1);
            userMoneyDetails.setCreateTime(format);
            userMoneyDetailsService.save(userMoneyDetails);
            PayDetails payDetails = new PayDetails();
            payDetails.setState(1);
            payDetails.setCreateTime(format);
            payDetails.setOrderId("系统充值金额");
            payDetails.setUserId(userId);
            payDetails.setMoney(money);
            payDetails.setClassify(type);
            payDetailsService.save(payDetails);
        }else{
            userMoneyService.updateMoney(2,userId,money);
            UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
            userMoneyDetails.setMoney(BigDecimal.valueOf(money));
            userMoneyDetails.setUserId(userId);
            userMoneyDetails.setContent("系统减少："+money);
            userMoneyDetails.setTitle("系统减少金额");
            userMoneyDetails.setType(2);
            userMoneyDetails.setCreateTime(format);
            userMoneyDetailsService.save(userMoneyDetails);
        }
        return Result.success();
    }

    @RequestMapping(value = "/updateEarningsMoney", method = RequestMethod.POST)
    @ApiOperation("修改金额")
    @ResponseBody
    public Result updateEarningsMoney(Long userId,Double money,Integer type){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(new Date());
        if(type==1){
            userMoneyService.updateEarningsMoney(1,userId,money);
            UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
            userMoneyDetails.setMoney(BigDecimal.valueOf(money));
            userMoneyDetails.setUserId(userId);
            userMoneyDetails.setContent("系统充值："+money);
            userMoneyDetails.setTitle("系统充值收益");
            userMoneyDetails.setType(1);
            userMoneyDetails.setClassify(10);
            userMoneyDetails.setCreateTime(format);
            userMoneyDetailsService.save(userMoneyDetails);
        }else{
            userMoneyService.updateEarningsMoney(2,userId,money);
            UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
            userMoneyDetails.setMoney(BigDecimal.valueOf(money));
            userMoneyDetails.setUserId(userId);
            userMoneyDetails.setContent("系统减少："+money);
            userMoneyDetails.setTitle("系统减少收益");
            userMoneyDetails.setType(2);
            userMoneyDetails.setClassify(10);
            userMoneyDetails.setCreateTime(format);
            userMoneyDetailsService.save(userMoneyDetails);
        }
        return Result.success();
    }


    @RequestMapping(value = "/selectUserList", method = RequestMethod.GET)
    @ApiOperation("查询所有用户列表")
    @ResponseBody
    public Result selectUserList(Integer page, Integer limit,
                                 @ApiParam("用户id 手机号 昵称 模糊搜索") String phone,
                                 @ApiParam("性别 1男 2女") Integer sex,
                                 @ApiParam("来源  ") String platform,
                                 @ApiParam("手机终端") String sysPhone,
                                 @ApiParam("-1所有人 0普通用户 1会员用户") Integer member,
                                 @ApiParam("开始时间") String startTime,
                                 @ApiParam("结束时间") String endTime,
                                 @ApiParam("用户昵称") String userName,
                                 @ApiParam("邀请码") String inviterCode,
                                 @ApiParam("状态 1正常 2封禁") Integer status,
                                 @ApiParam("-1全部用户  0普通用户  1陪玩管用户") Integer isAuthentication) {
        return Result.success().put("data", userService.selectUserPage(page, limit, phone, sex, platform, sysPhone, status,member,startTime,endTime,userName,inviterCode,isAuthentication));
    }

    @RequestMapping(value = "/selectUserLists", method = RequestMethod.GET)
    @ApiOperation("查询所有用户列表")
    @ResponseBody
    public Result selectUserLists(Integer page, Integer limit,
                                  @ApiParam("用户id 手机号 昵称 模糊搜索") String phone,
                                  @ApiParam("性别 1男 2女") Integer sex,
                                  @ApiParam("来源  ") String platform,
                                  @ApiParam("手机终端") String sysPhone,
                                  @ApiParam("-1所有人 0普通用户 1会员用户") Integer member,
                                  @ApiParam("开始时间") String startTime,
                                  @ApiParam("结束时间") String endTime,
                                  @ApiParam("用户昵称") String userName,
                                  @ApiParam("状态 1正常 2封禁") Integer status,
                                  @ApiParam("-1全部用户  0普通用户  1陪玩管用户") Integer isAuthentication) {
        String inviterCode=getUser().getInviterCode();
        return Result.success().put("data", userService.selectUserPage(page, limit, phone, sex, platform, sysPhone, status,member,startTime,endTime,userName,inviterCode,isAuthentication));
    }


    @RequestMapping(value = "/deleteUserByUserId/{userId}", method = RequestMethod.POST)
    @ApiOperation("删除用户")
    @ResponseBody
    public Result deleteUserByUserId(@PathVariable("userId") Long userId) {
        userService.removeById(userId);
        return Result.success();
    }

    @RequestMapping(value = "/updateUserByUserId", method = RequestMethod.POST)
    @ApiOperation("修改用户")
    @ResponseBody
    public Result updateUserByUserId(@RequestBody UserEntity userEntity) {
        UserEntity userEntity1 = userService.selectUserById(userEntity.getUserId());
        if(StringUtils.isNotBlank(userEntity.getPhone())){
            if(userEntity1.getPhone()!=null && !userEntity1.getPhone().equals(userEntity.getPhone())){
                int phone = userService.count(new QueryWrapper<UserEntity>().eq("phone", userEntity.getPhone()));
                if(phone>0){
                    return Result.error("当前手机号已被注册！");
                }
            }
        }
        if(StringUtils.isNotBlank(userEntity.getInvitationCode())){
            userService.update(null, Wrappers.<UserEntity>lambdaUpdate()
                    .set(UserEntity::getInviterCode,userEntity.getInvitationCode())
                    .eq(UserEntity::getInviterCode,userEntity1.getInvitationCode()));
        }
        userService.updateById(userEntity);
        return Result.success();
    }

    @PostMapping("/updateUserIntInviterCode")
    @ApiOperation("修改用户邀请人邀请码")
    public Result updateUserIntInviterCode(Long userId,String inviterCode){
        UserEntity userEntity = userService.selectUserById(userId);
        UserEntity userEntity1 = userService.queryByInvitationCode(inviterCode);
        if(userEntity1==null){
            return Result.error("邀请码用户不存在！");
        }
        userEntity.setInviterCode(inviterCode);
        userService.updateById(userEntity);
        return Result.success();
    }


    @PostMapping("/updateOnLine")
    @ApiOperation("修改在线状态")
    public Result updateOnLine(Long userId, Integer onLine) {
        UserEntity user = userService.getById(userId);
        user.setOnLine(onLine);
        userService.updateById(user);
        return Result.success();
    }

    @RequestMapping(value = "/updateUserIsAuthentication", method = RequestMethod.POST)
    @ApiOperation("开启或关闭用户实名认证")
    @ResponseBody
    public Result updateUserIsAuthentication(@RequestBody UserCertification userCertification) {
        UserEntity byId = userService.getById(userCertification.getUserId());
        if (byId.getIsAuthentication()==null || byId.getIsAuthentication()==0) {
            userCertificationService.remove(new QueryWrapper<UserCertification>().eq("user_id",userCertification.getUserId()));
            byId.setIsAuthentication(1);
            userCertification.setStatus(1L);
            userCertificationService.save(userCertification);
        } else {
            byId.setIsAuthentication(0);
            userCertificationService.remove(new QueryWrapper<UserCertification>().eq("user_id",userCertification.getUserId()));
        }
        userService.updateById(byId);
        return Result.success();
    }

    @PostMapping("/updateUserVip")
    @ApiOperation("修改用户会员")
    public Result updateUserVip(Long userId){
        UserVip userVip = userVipService.getOne(new QueryWrapper<UserVip>().eq("user_id",userId));
        Date time=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(userVip!=null){
            if(userVip.getIsVip()==1){
                try {
                    time=sdf.parse(userVip.getEndTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }else{
            userVip=new UserVip();
            userVip.setCreateTime(sdf.format(new Date()));
            userVip.setUserId(userId);
            userVip.setVipNameType(0);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.add(Calendar.MONTH, 1);
        userVip.setEndTime(sdf.format(cal.getTime()));
        userVip.setIsVip(1);
        if(userVip.getVipId()!=null){
            userVipService.updateById(userVip);
        }else{
            userVipService.save(userVip);
        }
        return Result.success();
    }



    @RequestMapping(value = "/updateUserStatusByUserId", method = RequestMethod.GET)
    @ApiOperation("禁用或启用用户")
    @ResponseBody
    public Result updateUserByUserId(Long userId) {
        UserEntity byId = userService.getById(userId);
        if (byId.getStatus().equals(1)) {
            byId.setStatus(2);
            //任务下架
            orderTakingDao.updateTakingStatusByUserId(byId.getUserId());
            //动态下架
            talkRepository.updateTalkStatusByUserId(byId.getUserId());
        } else {
            byId.setStatus(1);
        }
        userService.updateById(byId);
        return Result.success();
    }


    /**
     * 获取openid
     *
     * @param code 微信code
     * @return openid
     */
    @GetMapping("/openId/{code:.+}/{userId}")
    @ApiOperation("根据code获取openid")
    public Result getOpenid(@PathVariable("code") String code, @PathVariable("userId") Long userId) {
        return userService.getOpenId(code, userId);
    }

    /**
     * 信息分析
     *
     * @return
     */
    @GetMapping("/homeMessage")
    @ApiOperation("信息分析")
    public Result homeMessage() {
        HomeMessageResponse homeMessageResponse = new HomeMessageResponse();
        //   0查总   1查天  2查月  3查年
        //设置总用户人数
        homeMessageResponse.setTotalUsers(userService.queryUserCount(0, null));
        //设置今日新增
        homeMessageResponse.setNewToday(userService.queryUserCount(1, null));
        //设置本月新增
        homeMessageResponse.setNewMonth(userService.queryUserCount(2, null));
        //设置本年新增
        homeMessageResponse.setNewYear(userService.queryUserCount(3, null));

        //设置总收入
        homeMessageResponse.setTotalRevenue(userService.queryPayMoney(0));
        //设置今日收入
        homeMessageResponse.setTodayRevenue(userService.queryPayMoney(1));
        //设置本月收入
        homeMessageResponse.setMonthRevenue(userService.queryPayMoney(2));
        //设置本年收入
        homeMessageResponse.setYearRevenue(userService.queryPayMoney(3));

        return Result.success().put("data", homeMessageResponse);
    }

    /**
     * 接单分析
     *
     * @return
     */
    @GetMapping("/takingOrdersMessage")
    @ApiOperation("接单分析")
    public Result takingOrdersMessage(Long page, Long limit, String date, Long type) {
        Page<Map<String, Object>> iPage = new Page<>(page, limit);
        return userService.takingOrdersMessage(iPage, type, date);
    }

    /**
     * 用户分析
     */
    @GetMapping("/userMessage")
    @ApiOperation("用户分析")
    public Result userMessage(String date, int type) {
        UserMessageResponse userMessageResponse = new UserMessageResponse();
        userMessageResponse.setTotalNumber(userService.queryUserCount(type, date));
        userMessageResponse.setVipUserNumber(userService.userMessage(date, type));
        userMessageResponse.setUserNumber(userService.queryUserCount(type, date) - userService.userMessage(date, type));
        return Result.success().put("data", userMessageResponse);
    }

    @GetMapping("/userPlatform")
    @ApiOperation("用户来源统计")
    public Result userPlatForm(String data,Integer type){
        int appCount = userService.userPlatform("app", data, type);
        int wxCount = userService.userPlatform("小程序", data, type);
        int qq = userService.userPlatform("qq", data, type);
        int wx = userService.userPlatform("微信", data, type);
        int userCount = userService.queryUserCount(type, data);
        int userPhoneCount = userService.queryUserByPhoneCount(type, data);
        Map<String,Object> result=new HashMap<>();
        result.put("appCount",appCount);result.put("wxCount",wxCount);
        result.put("userCount",userCount);result.put("userPhoneCount",userPhoneCount);
        result.put("qq",qq);result.put("wx",wx);
        return Result.success().put("data",result);
    }

    @GetMapping("/selectUserByOrdersCount")
    @ApiOperation("根据接单数量进行排序")
    public Result selectUserByOrdersCount(Integer page,Integer limit){
        return userService.selectUserByOrdersCount(page, limit);
    }


}