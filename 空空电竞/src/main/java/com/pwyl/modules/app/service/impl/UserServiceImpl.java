package com.pwyl.modules.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.ClientException;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.getui.push.v2.sdk.ApiHelper;
import com.getui.push.v2.sdk.GtApiConfiguration;
import com.getui.push.v2.sdk.api.PushApi;
import com.getui.push.v2.sdk.common.ApiResult;
import com.getui.push.v2.sdk.dto.req.Audience;
import com.getui.push.v2.sdk.dto.req.message.PushChannel;
import com.getui.push.v2.sdk.dto.req.message.PushDTO;
import com.getui.push.v2.sdk.dto.req.message.PushMessage;
import com.getui.push.v2.sdk.dto.req.message.android.GTNotification;
import com.getui.push.v2.sdk.dto.req.message.ios.Alert;
import com.getui.push.v2.sdk.dto.req.message.ios.Aps;
import com.getui.push.v2.sdk.dto.req.message.ios.IosDTO;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.pwyl.common.utils.DateUtils;
import com.pwyl.common.utils.PageUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.dao.MsgDao;
import com.pwyl.modules.app.dao.UserCertificationDao;
import com.pwyl.modules.app.dao.UserDao;
import com.pwyl.modules.app.dao.UserMoneyDao;
import com.pwyl.modules.app.entity.*;
import com.pwyl.modules.app.service.RiskService;
import com.pwyl.modules.app.service.UserCertificationService;
import com.pwyl.modules.app.service.UserService;
import com.pwyl.modules.app.utils.JwtUtils;
import com.pwyl.modules.app.utils.UserConstantInterface;
import com.pwyl.modules.common.entity.CommonInfo;
import com.pwyl.modules.common.service.CommonInfoService;
import com.pwyl.modules.file.utils.Md5Utils;
import com.pwyl.modules.invite.service.InviteService;
import com.pwyl.modules.message.entity.MessageInfo;
import com.pwyl.modules.message.service.MessageService;
import com.pwyl.modules.orders.dao.OrdersDao;
import com.pwyl.modules.orders.entity.Orders;
import com.pwyl.modules.taking.dao.OrderTakingCommentDao;
import com.pwyl.modules.taking.entity.Game;
import com.pwyl.modules.taking.entity.OrderTaking;
import com.pwyl.modules.taking.service.GameService;
import com.pwyl.modules.taking.service.OrderTakingService;
import com.pwyl.modules.utils.CusAccessObjectUtil;
import com.pwyl.modules.utils.HttpClientUtil;
import com.pwyl.modules.utils.InvitationCodeUtil;
import com.pwyl.modules.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weixin.popular.api.MessageAPI;
import weixin.popular.api.SnsAPI;
import weixin.popular.bean.message.templatemessage.TemplateMessage;
import weixin.popular.bean.message.templatemessage.TemplateMessageItem;
import weixin.popular.bean.message.templatemessage.TemplateMessageResult;
import weixin.popular.util.JsonUtil;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.SendResult;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 用户
 *
 * @author fang
 * @date 2021/2/27
 */

@Service("userService")
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private MsgDao msgDao;
    @Autowired
    private JwtUtils jwtUtils;
    private int number = 1;
    @Autowired
    private InviteService inviteService;
    @Autowired
    private UserMoneyDao userMoneyDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserCertificationDao userCertificationDao;
    @Autowired
    private OrderTakingCommentDao orderTakingCommentDao;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private RiskService riskService;
    @Autowired
    private OrderTakingService orderTakingService;
    @Autowired
    private GameService gameService;

    @Override
    public Result selectConsortiaPersonList(Integer page,Integer limit,String userName,String phone,Long consortiaId){
        return Result.success().put("data",new PageUtils(baseMapper.selectPage(new Page<UserEntity>(page,limit),
                new QueryWrapper<UserEntity>()
                        .eq("consortia_id",consortiaId)
                        .eq(StringUtils.isNotBlank(userName),"user_name",userName)
                        .eq(StringUtils.isNotBlank(phone),"phone",phone))));
    }

    @Override
    public int updateUserConsortiaIsNullByUserId(Long userId){
        return baseMapper.updateUserConsortiaIsNullByUserId(userId);
    }

    @Override
    public int updateUserConsortiaIsNullByConsortiaId(Long consortiaId){
        return baseMapper.updateUserConsortiaIsNullByConsortiaId(consortiaId);
    }


    @Override
    public UserEntity queryByPhone(String phone) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("phone", phone));
    }

    @Override
    public UserEntity queryByOpenId(String openId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("open_id", openId));
    }

    @Override
    public UserEntity queryByWxOpenId(String openId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("wx_open_id", openId));
    }

    @Override
    public UserEntity queryByAppleId(String appleId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("apple_id", appleId));
    }

    @Override
    public UserEntity queryByUserId(Long userId) {
        UserEntity userEntity = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("user_id", userId));
        /*if(userEntity.getIsAuthentication()==null || userEntity.getIsAuthentication()!=1){
            UserCertification userCertification = userCertificationDao.selectOne(new QueryWrapper<UserCertification>().eq("user_id", userId));
            if(userCertification!=null && userCertification.getStatus()==1){
                userEntity.setIsAuthentication(1);
                baseMapper.updateById(userEntity);
            }
        }*/
        return userEntity;
    }

    @Override
    public UserEntity queryByInvitationCode(String invitationCode) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("invitation_code", invitationCode));
    }

    @Override
    public Result updatePhone(String phone, String msg, Long userId) {
        Msg msg1 = msgDao.findByPhoneAndCode(phone, msg);
        //校验短信验证码
        if (msg1 != null) {
            UserEntity userInfo = queryByPhone(phone);
            if (userInfo != null) {
                return Result.error("手机号已经被其他账号绑定");
            } else {
                UserEntity one = baseMapper.selectById(userId);
                one.setPhone(phone);
                baseMapper.updateById(one);
                return Result.success();
            }
        }
        return Result.error("验证码不正确");
    }

    @Override
    public Result iosRegister(String appleId) {
        if (StringUtils.isEmpty(appleId)) {
            return Result.error("账号信息获取失败，请退出重试！");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        // 根据返回的user实体类，判断用户是否是新用户，不是的话，更新最新登录时间，是的话，将用户信息存到数据库
        UserEntity userInfo = queryByAppleId(appleId);
        if (userInfo != null) {
            if (userInfo.getStatus().equals(2)) {
                return Result.error("账号已被封禁，请联系客服处理！");
            }
            userInfo.setUpdateTime(date);
            baseMapper.updateById(userInfo);
            //返回用户信息
            UserEntity user = queryByAppleId(appleId);
            return getResult(user);
        } else {
            return Result.error(-200, "请先绑定手机号账号！");
        }
    }

    @Override
    public Result wxLogin(String code) {
        try {
            //微信小程序APPID
            String appid = commonInfoService.findOne(45).getValue();
            //微信小程序秘钥
            String secret = commonInfoService.findOne(46).getValue();
            // 配置请求参数
            Map<String, String> param = new HashMap<>();
            param.put("appid", appid);
            param.put("secret", secret);
            param.put("js_code", code);
            param.put("grant_type", UserConstantInterface.WX_LOGIN_GRANT_TYPE);
            param.put("scope", "snsapi_userinfo");
            // 发送请求
            String wxResult = HttpClientUtil.doGet(UserConstantInterface.WX_LOGIN_URL, param);
            log.info(wxResult);
            JSONObject jsonObject = JSONObject.parseObject(wxResult);
            // 获取参数返回的
            String session_key = jsonObject.get("session_key").toString();
            //返回微信小程序openId
            String open_id = jsonObject.get("openid").toString();
            //判断是否注册过
            UserEntity userEntity = queryByOpenId(open_id);
            Map<String, String> map = new HashMap<>();
            // 封装返回小程序
            map.put("session_key", session_key);
            map.put("open_id", open_id);
            if (jsonObject.get("unionid") != null) {
                String unionid = jsonObject.get("unionid").toString();
                map.put("unionid", unionid);
            } else {
                map.put("unionid", "-1");
            }

            if (userEntity == null) {
                map.put("flag", "1");
            } else {
                map.put("flag", userEntity.getPhone());
            }
            return Result.success("登陆成功").put("data", map);
        } catch (Exception e) {
            log.error("登陆失败："+e.getMessage(),e);
            return Result.success("登录失败！");
        }
    }


    @Override
    public Result wxRegister(UserEntity userInfo1) {
        if (StringUtils.isEmpty(userInfo1.getOpenId())) {
            return Result.error("账号信息获取失败，请退出重试！");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        // 根据返回的user实体类，判断用户是否是新用户，不是的话，更新最新登录时间，是的话，将用户信息存到数据库
        UserEntity userInfo = queryByOpenId(userInfo1.getOpenId());
        if (userInfo != null) {
            if (userInfo.getStatus().equals(2)) {
                return Result.error("账号已被封禁，请联系客服处理！");
            }
            if (StringUtils.isNotEmpty(userInfo1.getPhone())) {
                userInfo.setPhone(userInfo1.getPhone());
            }
            userInfo.setUpdateTime(date);
            baseMapper.updateById(userInfo);
        } else {
            //判断是否在app登陆过  手机号是否有账号
            UserEntity userByMobile = queryByPhone(userInfo1.getPhone());
            if (userByMobile != null) {
                //有账号则绑定账号
                userByMobile.setOpenId(userInfo1.getOpenId());
                baseMapper.updateById(userByMobile);
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setContent("小程序账号绑定成功！");
                messageInfo.setTitle("系统通知");
                messageInfo.setState(String.valueOf(5));
                messageInfo.setUserName(userByMobile.getUserName());
                messageInfo.setUserId(String.valueOf(userByMobile.getUserId()));
                messageInfo.setCreateAt(sdf.format(new Date()));
                messageInfo.setIsSee("0");
                messageService.saveBody(messageInfo);
                if (userByMobile.getStatus().equals(2)) {
                    return Result.error("账号已被封禁，请联系客服处理！");
                }
            } else {
                if (StringUtils.isEmpty(userInfo1.getInviterCode())) {
                    userInfo1.setInviterCode(commonInfoService.findOne(88).getValue());
                }
                //没有则生成新账号
                userInfo1.setCreateTime(date);
                userInfo1.setPlatform("小程序");
                userInfo1.setStatus(1);
                if(StringUtils.isNotEmpty(userInfo1.getPhone())){
                    userInfo1.setPassword(DigestUtils.sha256Hex(userInfo1.getPhone()));
                }else{
                    userInfo1.setPassword(DigestUtils.sha256Hex(userInfo1.getOpenId()));
                }
                userInfo1.setRate(new BigDecimal(commonInfoService.findOne(206).getValue()));
                userInfo1.setZhiRate(new BigDecimal(commonInfoService.findOne(207).getValue()));
                userInfo1.setFeiRate(new BigDecimal(commonInfoService.findOne(208).getValue()));
                userInfo1.setInvitationCode(InvitationCodeUtil.toSerialCode());
                baseMapper.insertUser(userInfo1);
                //给用户创建钱包
                UserMoney userMoney = new UserMoney();
                userMoney.setUserId(userInfo1.getUserId());
                userMoney.setMoney(BigDecimal.valueOf(0));
                userMoney.setEarningsMoney(BigDecimal.ZERO);
                userMoneyDao.insert(userMoney);
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setContent("恭喜您，账号注册成功！");
                messageInfo.setTitle("系统通知");
                messageInfo.setState(String.valueOf(5));
                messageInfo.setUserName(userInfo1.getUserName());
                messageInfo.setUserId(String.valueOf(userInfo1.getUserId()));
                messageInfo.setCreateAt(sdf.format(new Date()));
                messageInfo.setIsSee("0");
                messageService.saveBody(messageInfo);
                UserEntity userEntity = queryByInvitationCode(userInfo1.getInviterCode());
                if (userEntity != null) {
                    inviteService.saveBody(userInfo1.getUserId(), userEntity);
                }
            }
        }
        //返回用户信息
        UserEntity user = queryByOpenId(userInfo1.getOpenId());
        return getResult(user);
    }


    @Override
    public Result wxBindMobile(String phone, String code, String wxOpenId, String token, String platform, Integer sysPhone) {
        Msg byPhoneAndCode = msgDao.findByPhoneAndCode(phone, code);
        if (byPhoneAndCode == null) {
            return Result.error("验证码错误");
        }
        msgDao.deleteById(byPhoneAndCode.getId());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date());
        UserEntity userInfo = queryByPhone(phone);
        if (userInfo != null) {
            if (StringUtils.isNotEmpty(userInfo.getWxOpenId())) {
                return Result.error("当前手机号已经被其他微信绑定");
            }
            //小程序登陆过
            userInfo.setWxOpenId(wxOpenId);
            String s = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + wxOpenId);
            AppUserInfo user = JsonUtil.parseObject(s, AppUserInfo.class);
            if (user != null && user.getNickname() != null) {
                if (user.getHeadimgurl() != null) {
                    userInfo.setAvatar(user.getHeadimgurl());
                }
                userInfo.setSex(user.getSex());
                if (user.getNickname() != null) {
                    userInfo.setUserName(user.getNickname().replaceAll("(\\d{3})\\d*([0-9a-zA-Z]{4})", "$1****$2"));
                }
            }
            baseMapper.updateById(userInfo);
        } else {
            //小程序没有登陆过
            userInfo = new UserEntity();
            String s = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + wxOpenId);
            AppUserInfo user = JsonUtil.parseObject(s, AppUserInfo.class);
            if (user != null && user.getNickname() != null) {
                if (user.getHeadimgurl() != null) {
                    userInfo.setAvatar(user.getHeadimgurl());
                }
                userInfo.setSex(user.getSex());
                if (user.getNickname() != null) {
                    userInfo.setUserName(user.getNickname().replaceAll("(\\d{3})\\d*([0-9a-zA-Z]{4})", "$1****$2"));
                }
            }
            userInfo.setWxOpenId(wxOpenId);
            userInfo.setPhone(phone);
            userInfo.setPlatform(platform);
            userInfo.setCreateTime(time);
            userInfo.setSysPhone(sysPhone);
            userInfo.setStatus(1);
            userInfo.setUpdateTime(time);
            baseMapper.insert(userInfo);
        }
        UserEntity userEntity = queryByWxOpenId(userInfo.getWxOpenId());
        return getResult(userEntity);
    }

    @Override
    public Result iosBindMobile(String phone, String code, String appleId, String platform, Integer sysPhone) {
        Msg byPhoneAndCode = msgDao.findByPhoneAndCode(phone, code);
        if (byPhoneAndCode == null) {
            return Result.error("验证码错误");
        }
        msgDao.deleteById(byPhoneAndCode.getId());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date());
        UserEntity userInfo = queryByPhone(phone);
        if (userInfo != null) {
            if (StringUtils.isNotEmpty(userInfo.getAppleId())) {
                return Result.error("当前手机号已经被其他苹果绑定");
            }
            userInfo.setAppleId(appleId);
            userInfo.setUpdateTime(simpleDateFormat.format(new Date()));
            baseMapper.updateById(userInfo);
        } else {
            userInfo = new UserEntity();
            userInfo.setSex(0);
            userInfo.setUserName(phone.replaceAll("(\\d{3})\\d*([0-9a-zA-Z]{4})", "$1****$2"));
            userInfo.setPhone(phone);
            userInfo.setPlatform(platform);
            userInfo.setCreateTime(time);
            userInfo.setSysPhone(sysPhone);
            userInfo.setStatus(1);
            userInfo.setUpdateTime(time);
            baseMapper.insert(userInfo);
        }
        UserEntity userEntity = queryByAppleId(userInfo.getAppleId());
        return getResult(userEntity);
    }


    @Override
    public Result wxAppLogin(String wxOpenId, String token) {
        UserEntity userEntity = queryByWxOpenId(wxOpenId);
        if (userEntity != null) {
            if (userEntity.getStatus().equals(2)) {
                return Result.error("账号已被禁用，请联系客服处理！");
            }
            String s = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + wxOpenId);
            AppUserInfo user = JsonUtil.parseObject(s, AppUserInfo.class);
            if (user != null && user.getNickname() != null) {
                if (user.getHeadimgurl() != null) {
                    userEntity.setAvatar(user.getHeadimgurl());
                }
                userEntity.setSex(user.getSex());
                if (user.getNickname() != null) {
                    userEntity.setUserName(user.getNickname().replaceAll("(\\d{3})\\d*([0-9a-zA-Z]{4})", "$1****$2"));
                }
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userEntity.setUpdateTime(sdf.format(new Date()));
            baseMapper.updateById(userEntity);
            return getResult(userEntity);
        } else {
            return Result.error(-200, "请先绑定手机号账号！");
        }
    }


    @Override
    public Result loginByOpenId(String openId) {
        UserEntity userEntity = queryByWxOpenId(openId);
        if(userEntity==null){
            return Result.error(-200,"未注册！");
        }
        String token = jwtUtils.generateToken(userEntity.getUserId());
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("expire", jwtUtils.getExpire());
        map.put("user", userEntity);
        return Result.success(map);
    }



    @Override
    public Result registerCode(String phone, String msg, String platform, Integer sysPhone,String openId,String inviterCode) {
        Msg msg1 = msgDao.findByPhoneAndCode(phone, msg);
        //校验短信验证码
        if (msg1 == null) {
            return Result.error("验证码不正确");
        }
        msgDao.deleteById(msg1.getId());
        //校验手机号是否存在
        UserEntity userInfo = queryByPhone(phone);
        if (userInfo != null) {
            if (userInfo.getStatus().equals(2)) {
                return Result.error("账号已被禁用，请联系客服处理！");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userInfo.setUpdateTime(sdf.format(new Date()));
            baseMapper.updateById(userInfo);
            return getResult(userInfo);
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = simpleDateFormat.format(new Date());
            userInfo = new UserEntity();
            userInfo.setPhone(phone);
            userInfo.setInviterCode(inviterCode);
            if (StringUtils.isEmpty(userInfo.getInviterCode())) {
                userInfo.setInviterCode(commonInfoService.findOne(88).getValue());
            }
            userInfo.setUserName(phone.replaceAll("(\\d{3})\\d*([0-9a-zA-Z]{4})", "$1****$2"));
            userInfo.setPlatform(platform);
            userInfo.setCreateTime(time);
            userInfo.setSysPhone(sysPhone);
            userInfo.setStatus(1);
            userInfo.setWxOpenId(openId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userInfo.setUpdateTime(sdf.format(new Date()));
            baseMapper.insertUser(userInfo);
            msgDao.deleteById(msg1.getId());
            //给用户创建钱包
            UserMoney userMoney = new UserMoney();
            userMoney.setUserId(userInfo.getUserId());
            userMoney.setMoney(BigDecimal.valueOf(0));
            userMoney.setEarningsMoney(BigDecimal.ZERO);
            userMoneyDao.insert(userMoney);
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setContent("恭喜您，账号注册成功！");
            messageInfo.setTitle("系统通知");
            messageInfo.setState(String.valueOf(5));
            messageInfo.setUserName(userInfo.getUserName());
            messageInfo.setUserId(String.valueOf(userInfo.getUserId()));
            messageInfo.setCreateAt(sdf.format(new Date()));
            messageInfo.setIsSee("0");
            messageService.saveBody(messageInfo);
            UserEntity userEntity = queryByInvitationCode(userInfo.getInviterCode());
            if (userEntity != null) {
                inviteService.saveBody(userInfo.getUserId(), userEntity);
            }
            return getResult(userInfo);
        }
    }


    @Override
    public Result login(String phone, String pwd) {
        UserEntity userEntity = queryByPhone(phone);
        if (userEntity == null) {
            return Result.error("手机号未注册！");
        }
        if (!userEntity.getPassword().equals(DigestUtils.sha256Hex(pwd))) {
            return Result.error("密码不正确！");
        }
        if (userEntity.getStatus().equals(2)) {
            return Result.error("账号已被禁用，请联系客服处理！");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        userEntity.setUpdateTime(sdf.format(new Date()));
        baseMapper.updateById(userEntity);
        return getResult(userEntity);
    }


    @Override
    public Result getResult(UserEntity user) {
        //生成token
        String token = jwtUtils.generateToken(user.getUserId());
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("expire", jwtUtils.getExpire());
        map.put("user", user);
        return Result.success(map);
    }


    @Override
    public Result sendMsg(String phone, String state) {
        int codes = (int) ((Math.random() * 9 + 1) * 100000);
        String code = String.valueOf(codes);
        System.out.println("sendMsg code is " + code);
        SmsSingleSenderResult result = null;
        /*if ("bindWx".equals(state)) {
            UserEntity userByPhone = queryByPhone(phone);
            if (userByPhone != null && StringUtils.isNotEmpty(userByPhone.getWxOpenId())) {
                return Result.error("当前手机号已被其他微信账号绑定");
            }
        } else if ("bindIos".equals(state)) {
            UserEntity userByPhone = queryByPhone(phone);
            if (userByPhone != null && StringUtils.isNotEmpty(userByPhone.getAppleId())) {
                return Result.error("当前手机号已被其他苹果账号绑定");
            }
        }*/
        if (phone != null) {
            if("forget".equals(state)){
                UserEntity userByPhone = queryByPhone(phone);
                if (userByPhone == null) {
                    return Result.error("手机号未注册");
                }
            }else{
                UserEntity userByPhone = queryByPhone(phone);
                if (userByPhone != null) {
                    return Result.error("当前手机号已被其他账号绑定");
                }
            }

        }
        CommonInfo three = commonInfoService.findOne(79);//实际这里是type
        //默认使用腾讯云
        if (three == null || "1".equals(three.getValue())) {
            //腾讯云短信发送
            return sendMsgTencent(phone, state, code);
        } else if("2".equals(three.getValue())){
            //阿里云短信发送
            return sendMsgAlibaba(phone,state, code);
        }else if("3".equals(three.getValue())){
            //短信宝短信发送
            return sendMsgDXB(phone,state,code);
        }else {
            Msg byPhone = msgDao.findByPhone(phone);
            if (byPhone != null) {
                byPhone.setCode(String.valueOf(code));
                byPhone.setPhone(phone);
                msgDao.updateById(byPhone);
            } else {
                Msg msg = new Msg();
                msg.setCode(String.valueOf(code));
                msg.setPhone(phone);
                msgDao.insert(msg);
            }

            return Result.error("验证码：" + code);
        }
    }

    @Override
    public Result sendMsg(String phone, String state,String code) {
        CommonInfo three = commonInfoService.findOne(79);
        //默认使用腾讯云
        if (three == null || "1".equals(three.getValue())) {
            //腾讯云短信发送
            return sendMsgTencent(phone, state, code);
        } else if("2".equals(three.getValue())){
            //阿里云短信发送
            if("register".equals(state) || "forget".equals(state) || "bind".equals(state)){
                return sendMsgAlibaba(phone,state, code);
            }
            return Result.success();
        }else if("3".equals(three.getValue())){
            return sendMsgDXB(phone,state,code);
        }else {
            Msg byPhone = msgDao.findByPhone(phone);
            if (byPhone != null) {
                byPhone.setCode(String.valueOf(code));
                byPhone.setPhone(phone);
                msgDao.updateById(byPhone);
            } else {
                Msg msg = new Msg();
                msg.setCode(String.valueOf(code));
                msg.setPhone(phone);
                msgDao.insert(msg);
            }

            return Result.error("验证码：" + code);
        }
    }


    private Result sendMsgAlibaba(String phone,String state, String code) {
        //阿里云短信accessKeyId
        CommonInfo three = commonInfoService.findOne(83);
        String accessKeyId = three.getValue();
        //阿里云短信accessSecret
        CommonInfo four = commonInfoService.findOne(84);
        String accessSecret = four.getValue();
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonInfo name = commonInfoService.findOne(6);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", name.getValue());
        String value = commonInfoService.findOne(80).getValue();
        switch (state) {
            case "register":
                value = commonInfoService.findOne(80).getValue();
                break;
            case "forget":
                value = commonInfoService.findOne(81).getValue();
                break;
            case "bind":
                value = commonInfoService.findOne(82).getValue();
                break;
        }
        request.putQueryParameter("TemplateCode", value);
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            String data = response.getData();
            JSONObject jsonObject = JSON.parseObject(data);
            if ("OK".equals(jsonObject.get("Code"))) {
                Msg byPhone = msgDao.findByPhone(phone);
                if (byPhone != null) {
                    byPhone.setCode(String.valueOf(code));
                    byPhone.setPhone(phone);
                    msgDao.updateById(byPhone);
                } else {
                    Msg msg = new Msg();
                    msg.setCode(String.valueOf(code));
                    msg.setPhone(phone);
                    msgDao.insert(msg);
                }
                return Result.success("login");
            } else {
                if (jsonObject.get("Message").toString().contains("分钟")) {
                    return Result.error("短信发送过于频繁，请一分钟后再试！");
                } else if (jsonObject.get("Message").toString().contains("小时")) {
                    return Result.error("短信发送过于频繁，请一小时后再试！");
                } else if (jsonObject.get("Message").toString().contains("天")) {
                    return Result.error("短信发送过于频繁，请明天再试！");
                }
                log.info(jsonObject.get("Message").toString());
                return Result.error("短信发送失败！");
            }
        } catch (ClientException | com.aliyuncs.exceptions.ClientException e) {
            e.printStackTrace();
        }
        return Result.error("验证码发送失败");
    }


    private Result sendMsgTencent(String phone, String state, String code) {
        SmsSingleSenderResult result = null;
        try {
            CommonInfo three = commonInfoService.findOne(31);
            String clientId = three.getValue();

            CommonInfo four = commonInfoService.findOne(32);
            String clientSecret = four.getValue();
            CommonInfo name = commonInfoService.findOne(6);
            /**
             * 发送短信验证码的状态、
             *
             * 在h5登录环境中 传的状态不是以下三种状态
             */
            SmsSingleSender ssender = new SmsSingleSender(Integer.parseInt(clientId), clientSecret);
            switch (state) {
                case "register":
                    result = ssender.send(0, "86", phone, "【" + name.getValue() + "】验证码: " + code + "，此验证码可用于登录或注册，10分钟内有效，如非您本人操作，可忽略本条消息", "", "");
                    break;
                case "forget":
                    result = ssender.send(0, "86", phone, "【" + name.getValue() + "】验证码: " + code + "，您正在执行找回密码操作，10分钟内有效，如非您本人操作，可忽略本条消息", "", "");
                    break;
                case "bind":
                    result = ssender.send(0, "86", phone, "【" + name.getValue() + "】验证码: " + code + "，您正在执行绑定手机号操作，10分钟内有效，如非您本人操作，可忽略本条消息", "", "");
                    break;
                case "dx":
                    result = ssender.send(0, "86", phone, "【" + name.getValue() + "】您有"+code+"条未读消息，赶快上线查看吧！", "", "");
                    break;
                case "weiGui":
                    result = ssender.send(0, "86", phone, "【" + name.getValue() + "】您好，您的账号存在违规，原因："+code, "", "");
                    break;
                case "orders":
                    result = ssender.send(0, "86", phone, "【" + name.getValue() + "】您好，您有一个订单请尽快处理，原因："+code, "", "");
                    break;
                case "newOrders":
                    Orders orders = ordersDao.selectById(Long.parseLong(code));
                    OrderTaking orderTaking = orderTakingService.getById(orders.getOrderTakingId());
                    Game game = gameService.getById(orderTaking.getGameId());
                    UserEntity userEntity = baseMapper.selectById(orders.getUserId());
                    result = ssender.send(0, "86", phone, "【" + name.getValue() + "】客户名称:"+userEntity.getUserName()+
                            "。下单时间:"+orders.getCreateTime()+"，订单服务类目:"+game.getGameName()+"，具体金额:"+orders.getPayMoney()+"金币，请您尽快上线接单，完成订单。", "", "");
                    break;
                default:
                    result = ssender.send(0, "86", phone, "【" + name.getValue() + "】验证码: " + code + "，此验证码可用于登录或注册，10分钟内有效，如非您本人操作，可忽略本条消息", "", "");
                    break;
            }

            if (result.result == 0) {
                Msg byPhone = msgDao.findByPhone(phone);
                if (byPhone != null) {
                    byPhone.setCode(String.valueOf(code));
                    byPhone.setPhone(phone);
                    msgDao.updateById(byPhone);
                } else {
                    Msg msg = new Msg();
                    msg.setCode(String.valueOf(code));
                    msg.setPhone(phone);
                    msgDao.insert(msg);
                }
                UserEntity userByPhone = queryByPhone(phone);
                if (userByPhone != null) {
                    return Result.success("login");
                } else {
                    return Result.success("register");
                }
            } else {
                return Result.error(6, result.errMsg);
            }
        } catch (HTTPException | JSONException | IOException e) {
            // HTTP 响应码错误
            e.printStackTrace();
        }
        return Result.error("验证码发送失败");
    }


    private Result sendMsgDXB(String phone, String state, String code) {
        CommonInfo three = commonInfoService.findOne(164);
        CommonInfo four = commonInfoService.findOne(165);
        CommonInfo name = commonInfoService.findOne(6);
        String testUsername = three.getValue(); //在短信宝注册的用户名
        String testPassword = four.getValue(); //在短信宝注册的密码
        String value="";
        switch (state) {
            case "register":
                value = "【" + name.getValue() + "】验证码: " + code + "，此验证码可用于登录或注册，10分钟内有效，如非您本人操作，可忽略本条消息";
                break;
            case "forget":
                value = "【" + name.getValue() + "】验证码: " + code + "，您正在执行找回密码操作，10分钟内有效，如非您本人操作，可忽略本条消息";
                break;
            case "bind":
                value = "【" + name.getValue() + "】验证码: " + code + "，您正在执行绑定手机号操作，10分钟内有效，如非您本人操作，可忽略本条消息";
                break;
            case "dx":
                value = "【" + name.getValue() + "】您有"+code+"条未读消息，赶快上线查看吧！";
                break;
            case "weiGui":
                value = "【" + name.getValue() + "】您好，您的账号存在违规，原因："+code;
                break;
            case "orders":
                value = "【" + name.getValue() + "】您好，您有一个订单请尽快处理，原因："+code;
                break;
            case "newOrders2":
                value = "【" + name.getValue() + "】您好，您有一条新的订单，赶快上线查看吧！";
                break;
            case "newOrders":
                Orders orders = ordersDao.selectById(Long.parseLong(code));
                OrderTaking orderTaking = orderTakingService.getById(orders.getOrderTakingId());
                Game game = gameService.getById(orderTaking.getGameId());
                UserEntity userEntity = baseMapper.selectById(orders.getUserId());
                value = "【" + name.getValue() + "】客户名称:"+userEntity.getUserName()+
                        "。下单时间:"+orders.getCreateTime()+"，订单服务类目:"+game.getGameName()+"，具体金额:"+orders.getPayMoney()+"金币，请您尽快上线接单，完成订单。";
                break;
            default:
                value = "【" + name.getValue() + "】验证码: " + code + "，此验证码可用于登录或注册，10分钟内有效，如非您本人操作，可忽略本条消息";
                break;
        }
        StringBuilder httpArg = new StringBuilder();
        httpArg.append("u=").append(testUsername).append("&");
        httpArg.append("p=").append(Md5Utils.md5s(testPassword)).append("&");
        httpArg.append("m=").append(phone).append("&");
        httpArg.append("c=").append(Md5Utils.encodeUrlString(value, "UTF-8"));
        String result = Md5Utils.request("https://api.smsbao.com/sms", httpArg.toString());
        log.error("短信包返回值："+result);
        if ("0".equals(result)) {
            Msg byPhone = msgDao.findByPhone(phone);
            if (byPhone != null) {
                byPhone.setCode(String.valueOf(code));
                byPhone.setPhone(phone);
                msgDao.updateById(byPhone);
            } else {
                Msg msg = new Msg();
                msg.setCode(String.valueOf(code));
                msg.setPhone(phone);
                msgDao.insert(msg);
            }
            UserEntity userByPhone = queryByPhone(phone);
            if (userByPhone != null) {
                return Result.success("login");
            } else {
                return Result.success("register");
            }
        } else {
//            return ResultUtil.error(6, result.errMsg);
            if("30".equals(result)){
                return Result.error( "错误密码");
            }else if("40".equals(result)){
                return Result.error( "账号不存在");
            }else if("41".equals(result)){
                return Result.error( "余额不足");
            }else if("43".equals(result)){
                return Result.error( "IP地址限制");
            }else if("50".equals(result)){
                return Result.error( "内容含有敏感词");
            }else if("51".equals(result)){
                return Result.error( "手机号码不正确");
            }
        }

        return Result.error("验证码发送失败");
    }


    @Override
    public Result getOpenId(String code, Long userId) {
        try {
            //微信appid
            CommonInfo one = commonInfoService.findOne(5);
            //微信秘钥
            CommonInfo two = commonInfoService.findOne(21);
            String openid = SnsAPI.oauth2AccessToken(one.getValue(), two.getValue(), code).getOpenid();
            if (StringUtils.isNotEmpty(openid)) {
                UserEntity userEntity = new UserEntity();
                userEntity.setUserId(userId);
                userEntity.setOpenId(openid);
                baseMapper.updateById(userEntity);
                return Result.success().put("data", openid);
            }
            return Result.error("获取失败");
        } catch (Exception e) {
            log.error("GET_OPENID_FAIL");
            return Result.error("获取失败,出错了！");
        }
    }

    @Override
    public UserEntity selectUserById(Long userId) {
        UserEntity userEntity = baseMapper.selectById(userId);
        if(userEntity.getRate()==null){
            userEntity.setRate(new BigDecimal(commonInfoService.findOne(206).getValue()));
        }
        if(userEntity.getZhiRate()==null){
            userEntity.setZhiRate(new BigDecimal(commonInfoService.findOne(207).getValue()));
        }
        if(userEntity.getFeiRate()==null){
            userEntity.setFeiRate(new BigDecimal(commonInfoService.findOne(208).getValue()));
        }
        baseMapper.updateById(userEntity);
        return userEntity;
    }


    @Override
    public PageUtils selectUserPage(Integer page, Integer limit, String search, Integer sex, String platform, String sysPhone, Integer status,Integer member,String startTime,String endTime,String userName,String inviterCode,Integer isAuthentication) {
        Page<UserEntity> pages = new Page<>(page, limit);
        return new PageUtils(baseMapper.selectUserPage(pages, search, sex, platform, sysPhone, status,member,startTime,endTime,userName,inviterCode,isAuthentication));
    }

    @Override
    public int queryInviterCount(String inviterCode) {
        return baseMapper.queryInviterCount(inviterCode);
    }

    @Override
    public int queryUserCount(int type, String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        if (date == null || date == "") {
            date = simpleDateFormat.format(new Date());
        }
        return baseMapper.queryUserCount(type, date);
    }

    @Override
    public int queryUserByPhoneCount(int type, String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        if (StringUtils.isEmpty(date)) {
            date = simpleDateFormat.format(new Date());
        }
        return baseMapper.queryUserByPhoneCount(type, date);
    }

    @Override
    public Double queryPayMoney(int type) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        String date = simpleDateFormat.format(new Date());
        return baseMapper.queryPayMoney(type, date);
    }

    @Override
    public IPage<Map<String, Object>> queryCourseOrder(Page<Map<String, Object>> iPage, int type, String date) {
        return baseMapper.queryCourseOrder(iPage, type, date);
    }

    @Override
    public int userMessage(String date, int type) {
        return baseMapper.userMessage(date, type);
    }


    @Override
    public void pushToSingle(String title, String content, String clientId) {
        try{
            if(StringUtils.isNotEmpty(clientId)){
                UserEntity userEntity = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("clientid", clientId));
                GtApiConfiguration apiConfiguration = new GtApiConfiguration();
                //填写应用配置
                apiConfiguration.setAppId(commonInfoService.findOne(61).getValue());
                apiConfiguration.setAppKey(commonInfoService.findOne(60).getValue());
                apiConfiguration.setMasterSecret(commonInfoService.findOne(62).getValue());
                // 接口调用前缀，请查看文档: 接口调用规范 -> 接口前缀, 可不填写appId
                apiConfiguration.setDomain("https://restapi.getui.com/v2/");
                // 实例化ApiHelper对象，用于创建接口对象
                ApiHelper apiHelper = ApiHelper.build(apiConfiguration);
                // 创建对象，建议复用。目前有PushApi、StatisticApi、UserApi
                PushApi pushApi = apiHelper.creatApi(PushApi.class);
                //根据cid进行单推
                PushDTO<Audience> pushDTO = new PushDTO<Audience>();
                // 设置推送参数
                pushDTO.setRequestId(System.currentTimeMillis() + "");
                PushMessage pushMessage = new PushMessage();
                if(userEntity==null || userEntity.getSysPhone()==null || userEntity.getSysPhone()==1){
                    //安卓推送
                    GTNotification notification = new GTNotification();
                    pushDTO.setPushMessage(pushMessage);
                    // 配置通知栏图标
                    notification.setLogo(commonInfoService.findOne(19).getValue() + "/logo.png"); //配置通知栏图标，需要在客户端开发时嵌入，默认为push.png
                    // 配置通知栏网络图标
                    notification.setLogoUrl(commonInfoService.findOne(19).getValue() + "/logo.png");
                    notification.setTitle(title);
                    notification.setBody(content);
                    notification.setClickType("startapp");
                    notification.setUrl(commonInfoService.findOne(19).getValue());
                    notification.setChannelLevel("3");
                    pushMessage.setNotification(notification);
                }else{
                    pushMessage.setTransmission(title);
                    pushDTO.setPushMessage(pushMessage);
                    PushChannel pushChannel=new PushChannel();
                    IosDTO iosDTO=new IosDTO();
                    Aps aps=new Aps();
                    Alert alert=new Alert();
                    alert.setTitle(title);
                    alert.setBody(content);
                    aps.setAlert(alert);
                    aps.setSound("default");
                    iosDTO.setAps(aps);
                    pushChannel.setIos(iosDTO);
                    pushDTO.setPushChannel(pushChannel);
                }
                // 设置接收人信息
                Audience audience = new Audience();
                audience.addCid(clientId);
                pushDTO.setAudience(audience);
                // 进行cid单推
                ApiResult<Map<String, Map<String, String>>> apiResult = pushApi.pushToSingleByCid(pushDTO);
                if (apiResult.isSuccess()) {
                    // success
                    log.error("消息推送成功：" + apiResult.getData());
                } else {
                    // failed
                    log.error("消息推送成功失败：code:" + apiResult.getCode() + ", msg: " + apiResult.getMsg());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("消息推送异常："+e.getMessage(),e);
        }
    }

    @Override
    public Result loginApp(String phone, String password) {
        //md5加密
        String pwd = DigestUtils.sha256Hex(password);
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        UserEntity userEntity = baseMapper.selectOne(queryWrapper);
        if (userEntity == null) {
            return Result.error("手机号未注册！");
        }
        if (!userEntity.getPassword().equals(pwd)) {
            return Result.error("密码不正确！");
        }
        if (userEntity.getStatus().equals(2)) {
            return Result.error("账号已被禁用，请联系客服处理！");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        userEntity.setUpdateTime(sdf.format(new Date()));
        baseMapper.updateById(userEntity);
        return getResult(userEntity);

    }

    @Override
    public Result registApp(String userName, String phone, String password, String msg, String platform, String inviterCode,String openId) {
        Msg msg1 = msgDao.findByPhoneAndCode(phone, msg);
        //校验短信验证码
        if (msg1 == null) {
            return Result.error("验证码不正确");
        }
        msgDao.deleteById(msg1.getId());
        //校验手机号是否存在
        UserEntity userInfo = queryByPhone(phone);
        if (userInfo != null) {
            return Result.error("手机号已经被注册！");

        } else {
            if(StringUtils.isEmpty(inviterCode)){
                inviterCode=commonInfoService.findOne(88).getValue();
            }
            UserEntity userEntity = queryByInvitationCode(inviterCode);
            if(userEntity==null){
                return Result.error("邀请码不正确");
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = simpleDateFormat.format(new Date());
            userInfo = new UserEntity();
            userInfo.setPhone(phone);
            userInfo.setUserName(userName);
            //对密码进行MD5加密
            userInfo.setPassword(DigestUtils.sha256Hex(password));
            userInfo.setPlatform(platform);
            userInfo.setCreateTime(time);
            userInfo.setStatus(1);
            userInfo.setInviterCode(inviterCode);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userInfo.setCreateTime(sdf.format(new Date()));
            userInfo.setInvitationCode(InvitationCodeUtil.toSerialCode());
            userInfo.setWxOpenId(openId);
            baseMapper.insertUser(userInfo);
            //设置自己邀请码
            baseMapper.updateById(userInfo);
            //给用户创建钱包
            UserMoney userMoney = new UserMoney();
            userMoney.setUserId(userInfo.getUserId());
            userMoney.setMoney(BigDecimal.valueOf(0));
            userMoney.setEarningsMoney(BigDecimal.ZERO);
            userMoneyDao.insert(userMoney);
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setContent("恭喜您，账号注册成功！");
            messageInfo.setTitle("系统通知");
            messageInfo.setState(String.valueOf(5));
            messageInfo.setUserName(userInfo.getUserName());
            messageInfo.setUserId(String.valueOf(userInfo.getUserId()));
            messageInfo.setCreateAt(sdf.format(new Date()));
            messageInfo.setIsSee("0");
            messageService.saveBody(messageInfo);
            inviteService.saveBody(userInfo.getUserId(), userEntity);
            return getResult(userInfo);
        }

    }


    @Override
    public Result forgetPwd(String pwd, String phone, String msg) {
        try {
            Msg byPhoneAndCode = msgDao.findByPhoneAndCode(phone, msg);
            //校验短信验证码
            if (byPhoneAndCode == null) {
                return Result.error("验证码不正确");
            }
            UserEntity userByPhone = queryByPhone(phone);
            userByPhone.setPassword(DigestUtils.sha256Hex(pwd));
            msgDao.deleteById(byPhoneAndCode.getId());
            baseMapper.updateById(userByPhone);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("服务器内部错误");
        }
    }

    @Override
    public Result takingOrdersMessage(Page<Map<String, Object>> iPage, Long type, String date) {
        //接单分析
        return Result.success().put("data", new PageUtils(baseMapper.takingOrdersMessage(iPage, type, date)));
    }

    @Override
    public Result selectUserByUserId(Long userId,Long byUserId){
        UserEntity userEntity = baseMapper.selectUserByUserId(userId, byUserId);
        userEntity.setOpenId(null);
        userEntity.setZhiFuBao(null);
        userEntity.setZhiFuBaoName(null);
        userEntity.setWxImg(null);
        userEntity.setUserIp(null);
        userEntity.setIpAddress(null);
        userEntity.setPhone(null);
        userEntity.setPassword(null);
        return Result.success().put("data",userEntity);
    }

    @Override
    public int updateUserClientIdIsNull(String clientid){
        return baseMapper.updateUserClientIdIsNull(clientid);
    }

    @Override
    public Result selectUserDetailsByUserId(Long userId){
        UserEntity userEntity = baseMapper.selectById(userId);
        userEntity.setOpenId(null);
        userEntity.setZhiFuBao(null);
        userEntity.setZhiFuBaoName(null);
        userEntity.setWxImg(null);
        userEntity.setUserIp(null);
        userEntity.setIpAddress(null);
        userEntity.setPhone(null);
        userEntity.setPassword(null);
        UserCertification userCertification = userCertificationDao.selectOne(new QueryWrapper<UserCertification>().eq("user_id", userId));
        if(userCertification!=null){
            userCertification.setSkillImg(userCertification.getSkillImg()+","+userCertification.getLifePhoto());
            userCertification.setIdNumber("1");
            userCertification.setFront(null);
            userCertification.setBack(null);
            userCertification.setName(null);
        }

        //评分
        Double score = orderTakingCommentDao.selectAvgScoreByUserId(userId);
        //下单数量
        int ordersCount = ordersDao.selectOrdersCountByUserId(userId);
        Map<String,Object> result=new HashMap<>();
        result.put("user",userEntity);
        result.put("userCertification",userCertification);
        result.put("score",score);
        result.put("ordersCount",ordersCount);
        return Result.success().put("data",result);
    }

    @Override
    public Result selectUserDetails(UserEntity user, HttpServletRequest request){
        if (user.getRate() == null) {
            user.setRate(new BigDecimal(commonInfoService.findOne(206).getValue()));
        }
        if (user.getZhiRate() == null) {
            user.setZhiRate(new BigDecimal(commonInfoService.findOne(207).getValue()));
        }
        if (user.getFeiRate() == null) {
            user.setFeiRate(new BigDecimal(commonInfoService.findOne(208).getValue()));
        }
        String value1 = commonInfoService.findOne(806).getValue();
        if("是".equals(value1)){
            String format = DateUtils.formatY(new Date());
            //每日获取一次ip地址
            if (StringUtils.isEmpty(user.getRecentlyData()) || !format.equals(user.getRecentlyData())) {
                user.setRecentlyData(format);
                try {
                    String ip = CusAccessObjectUtil.getIpAddrs(request);
                    String ip1 = CusAccessObjectUtil.getIp(request);
                    log.error("用户名：" + user.getUserName() + "------" + ip + ",,,," + ip1);
                    String key = commonInfoService.findOne(807).getValue();
                    String address = CusAccessObjectUtil.getAddress(ip,key);
                    String[] privinces = {"北京市", "天津市", "河北省", "山西省", "内蒙古", "辽宁省", "吉林省", "黑龙江省", "上海市", "江苏省", "浙江省", "安徽省", "福建省", "江西省", "山东省", "河南省", "湖北省", "湖南省", "广东省", "广西", "海南省", "重庆市", "四川省", "贵州省", "云南省", "西藏", "陕西省", "甘肃省", "青海省", "宁夏", "新疆", "台湾省", "香港", "澳门"};
                    //判断是否属于国内 再判断是否为高风险地区  如果是
                    Integer china = 2, riskGrade = 2;
                    for (String privince : privinces) {
                        if (address.startsWith(privince)) {
                            china = 1;
                            //如果是国内 在判断是否是风险地区
                            String value = commonInfoService.findOne(701).getValue();
                            String[] split = value.split(",");
                            if(StringUtils.isNotBlank(value)){
                                for (String str : split) {
                                    if (address.startsWith(str)) {
                                        riskGrade = 1;
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                    }
                    user.setUserIp(ip);
                    user.setIpAddress(address);
                    if(china==2){
                        user.setStatus(china);
                        Risk risk=new Risk();
                        risk.setUserId(user.getUserId());
                        risk.setContent("ip归属地为国外，已封号："+address);
                        risk.setRiskType(1);
                        risk.setCreateTime(DateUtils.format(new Date()));
                        riskService.save(risk);
                    }

                    if(riskGrade==1){
                        user.setRisk(riskGrade);
                        Risk risk=new Risk();
                        risk.setUserId(user.getUserId());
                        risk.setContent("ip归属地为风险地区，已标记为风险账号："+address);
                        risk.setRiskType(1);
                        risk.setCreateTime(DateUtils.format(new Date()));
                        riskService.save(risk);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        baseMapper.updateById(user);
        user.setOpenId(null);
        user.setUserIp(null);
        user.setIpAddress(null);
        user.setPassword(null);
        return Result.success().put("data", user);
    }

    @Override
    public Result selectUserByOrdersCount(Integer page,Integer limit){
        return Result.success().put("data",new PageUtils(baseMapper.selectUserByOrdersCount(new Page<UserEntity>(page,limit))));
    }

    @Override
    public int userPlatform(String platform,String date,Integer type){
        return baseMapper.userPlatform(platform, date, type);
    }

    @Override
    public void sendWxMessage(String templateId, LinkedHashMap<String, TemplateMessageItem> data, String openid) {
        try{
            String url;
            url=commonInfoService.findOne(19).getValue();
            TemplateMessage templateMessage = new TemplateMessage();
            templateMessage.setTouser(openid);
            templateMessage.setTemplate_id(templateId);
            templateMessage.setData(data);
            templateMessage.setUrl(url);
            TemplateMessageResult templateMessageResult = MessageAPI.messageTemplateSend(getWxToken(), templateMessage);
            if (templateMessageResult.isSuccess()) {
                new SendResult();
            } else {
                new SendResult();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private String getWxToken() {
        String appid = commonInfoService.findOne(5).getValue();
        String secret = commonInfoService.findOne(21).getValue();
        String jsonResult = HttpClientUtil.doPost("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + secret);
        JSONObject parseObject = JSON.parseObject(jsonResult);
        log.info("=========accessTokenOut--gzh========="+parseObject.toJSONString());

        String errcode = parseObject.getString("errcode");
        return parseObject.getString("access_token");
    }



}
