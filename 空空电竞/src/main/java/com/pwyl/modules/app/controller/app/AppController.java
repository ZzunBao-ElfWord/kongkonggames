package com.pwyl.modules.app.controller.app;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.annotation.Login;
import com.pwyl.modules.app.annotation.LoginUser;
import com.pwyl.modules.app.dao.UserCertificationDao;
import com.pwyl.modules.app.dao.UserVipDao;
import com.pwyl.modules.app.entity.Msg;
import com.pwyl.modules.app.entity.UserCertification;
import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.app.entity.UserVip;
import com.pwyl.modules.app.service.AppService;
import com.pwyl.modules.app.service.MsgService;
import com.pwyl.modules.app.service.UserService;
import com.pwyl.modules.app.service.UserVipService;
import com.pwyl.modules.common.service.CommonInfoService;
import com.pwyl.modules.utils.InvitationCodeUtil;
import com.pwyl.modules.utils.MD5Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * APP登录授权
 */
@RestController
@RequestMapping("/app/user")
@Api(value = "APP管理", tags = {"APP管理"})
public class AppController {

    @Autowired
    private UserService userService;
    @Autowired
    private AppService appService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private UserVipService userVipService;
    @Autowired
    private MsgService msgService;

    @Login
    @PostMapping("/setBabyPwd")
    @ApiOperation("设置青少年模式密码")
    public Result setBabyPwd(@LoginUser UserEntity user,String babyPwd){
        user.setBabyPwd(babyPwd);
        user.setBabyModel(1);
        userService.updateById(user);
        return Result.success();
    }

    @Login
    @PostMapping("/closeBaby")
    @ApiOperation("关闭青少年模式")
    public Result closeBabyPwd(@LoginUser UserEntity user,String babyPwd){
        if(!user.getBabyPwd().equals(babyPwd)){
            return Result.error("密码不正确！");
        }
        user.setBabyModel(2);
        userService.updateById(user);
        return Result.success();
    }

    @Login
    @PostMapping("/iKnow")
    @ApiOperation("青少年模式默认关闭")
    public Result closeBabyPwd(@LoginUser UserEntity user){
        if(user.getBabyModel()!=null){
            return Result.error("系统错误，请刷新页面后重试！");
        }
        user.setBabyModel(2);
        userService.updateById(user);
        return Result.success();
    }

    @Login
    @PostMapping("/updateBabyPwd")
    @ApiOperation("修改青少年模式密码")
    public Result updateBabyPwd(Long userId,String babyPwd,String msg){
        UserEntity user = userService.getById(userId);
        Msg byPhoneAndCode = msgService.findByPhoneAndCode(user.getPhone(), msg);
        if(byPhoneAndCode==null){
            return Result.error("验证码错误！");
        }
        msgService.removeById(byPhoneAndCode.getId());
        user.setBabyPwd(babyPwd);
        userService.updateById(user);
        return Result.success();
    }

    @Login
    @PostMapping("/updateOnLine")
    @ApiOperation("修改在线状态")
    public Result updateOnLine(@LoginUser UserEntity user, Integer onLine) {
        user.setOnLine(onLine);
        userService.updateById(user);
        return Result.success();
    }

    @Login
    @PostMapping("/updateSendMsg")
    @ApiOperation("修改是否接受消息推送")
    public Result updateSendMsg(@LoginUser UserEntity user,Integer isSendMsg){
        user.setIsSendMsg(isSendMsg);
        userService.updateById(user);
        return Result.success();
    }


    @Login
    @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("用户端修改密码")
    public Result updatePwd(@LoginUser UserEntity user, String pwd, String oldPwd) {
        if (!user.getPassword().equals(DigestUtils.sha256Hex(oldPwd))) {
            return Result.error("原始密码不正确！");
        }
        if (pwd.equals(oldPwd)) {
            return Result.error("新密码不能与旧密码相同！");
        }
        user.setPassword(DigestUtils.sha256Hex(pwd));
        userService.updateById(user);
        return Result.success();
    }

    @Login
    @RequestMapping(value = "/updatePhone", method = RequestMethod.POST)
    @ApiOperation("用户端换绑手机号")
    @ResponseBody
    public Result updatePhone(@RequestAttribute("userId") Long userId, @RequestParam String phone, @RequestParam String msg) {
        return userService.updatePhone(phone, msg, userId);
    }

    @Login
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    @ApiOperation("用户修改个人信息")
    @ResponseBody
    public Result updateUser(@RequestBody UserEntity userEntity,@RequestAttribute("userId") Long userId) {
        userEntity.setUserId(userId);
        userService.updateById(userEntity);
        return Result.success();
    }

    @Login
    @RequestMapping(value = "/updateUserImageUrl", method = RequestMethod.POST)
    @ApiOperation("用户修改头像")
    @ResponseBody
    public Result updateUserImageUrl(@LoginUser UserEntity user, String avatar) {
        user.setAvatar(avatar);
        userService.updateById(user);
        return Result.success();
    }

    @Login
    @RequestMapping(value = "/updateUserName", method = RequestMethod.POST)
    @ApiOperation("用户修改昵称")
    @ResponseBody
    public Result updateUserName(@LoginUser UserEntity user, String userName) {
        user.setUserName(userName);
        userService.updateById(user);
        return Result.success();
    }

    @Login
    @RequestMapping(value = "/selectUserById", method = RequestMethod.GET)
    @ApiOperation("获取用户详细信息")
    @ResponseBody
    public Result selectUserById(@LoginUser UserEntity user, HttpServletRequest request) {
        return userService.selectUserDetails(user, request);
    }


    @RequestMapping(value = "/selectNewApp", method = RequestMethod.GET)
    @ApiOperation("升级检测")
    @ResponseBody
    public Result selectNewApp() {
        return Result.success().put("data", appService.selectNewApp());
    }

    @RequestMapping(value = "/updateClientId", method = RequestMethod.POST)
    @ApiOperation("用户端绑定消息推送clientId")
    @ResponseBody
    @Login
    public Result updateClientId(String clientId,@RequestAttribute Long userId,Integer sysPhone) {
        UserEntity userEntity = userService.getById(userId);
        if (userEntity != null) {
            //检查是否有其他用户设置这个clientId 如果有 则清除其他用户
            userService.updateUserClientIdIsNull(clientId);
            userEntity.setClientid(clientId);
            userEntity.setSysPhone(sysPhone);
            userService.updateById(userEntity);
            return Result.success();
        }
        return Result.error("用户不存在！");
    }

    @GetMapping("/selectUserByUserId")
    @ApiOperation("查看用户信息")
    @ResponseBody
    @Login
    public Result selectUserByUserId(@LoginUser UserEntity user,Long userId){
        return userService.selectUserByUserId(userId,user.getUserId());
    }

    @GetMapping("/selectUserDetailsByUserId")
    @ApiOperation("查询用户主页信息")
    @ResponseBody
    public Result selectUserDetailsByUserId(Long userId){
        return userService.selectUserDetailsByUserId(userId);
    }

    @GetMapping("/selectUserByOrdersCount")
    @ApiOperation("根据接单数量进行排序")
    public Result selectUserByOrdersCount(Integer page,Integer limit){
        return userService.selectUserByOrdersCount(page, limit);
    }


}
