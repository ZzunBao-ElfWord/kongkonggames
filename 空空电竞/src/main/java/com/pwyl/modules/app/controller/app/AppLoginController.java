package com.pwyl.modules.app.controller.app;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.app.service.IAppleService;
import com.pwyl.modules.app.service.UserService;
import com.pwyl.modules.app.utils.UserConstantInterface;
import com.pwyl.modules.app.utils.WxPhone;
import com.pwyl.modules.common.entity.CommonInfo;
import com.pwyl.modules.common.service.CommonInfoService;
import com.pwyl.modules.utils.HttpClientUtil;
import com.pwyl.modules.utils.MD5Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import weixin.popular.api.SnsAPI;
import weixin.popular.bean.sns.SnsToken;

import java.util.HashMap;
import java.util.Map;

/**
 * APP登录授权
 *
 */
@RestController
@RequestMapping("/app/Login")
@Api("APP登录接口")
@Slf4j
public class AppLoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private IAppleService appleService;
    @Autowired
    private CommonInfoService commonInfoService;


    @ApiOperation("微信小程序登陆")
    @RequestMapping(value = "/wxLogin", method = RequestMethod.GET)
    public Result wxLogin(@ApiParam("小程序code码") String code){
        return userService.wxLogin(code);
    }


    @ApiOperation("小程序登录新增或修改个人信息")
    @RequestMapping(value = "/insertWxUser", method = RequestMethod.POST)
    public Result insertWxUser(@RequestBody UserEntity userInfo){
        return userService.wxRegister(userInfo);
    }


    @RequestMapping(value = "/appleLogin", method = RequestMethod.GET)
    @ApiOperation("苹果登陆获取appleUserId")
    public Result loginVerify(@RequestParam("identityToken") String identityToken) {
        try {
            log.info("苹果token：{}", identityToken);
            JSONObject jsonObject = JSON.parseObject(identityToken);
            JSONObject userInfo = jsonObject.getJSONObject("userInfo");
            String identityTokens = userInfo.getString("identityToken");
            return appleService.getAppleUserInfo(identityTokens);
        } catch (Exception e) {
            log.error("苹果token校验失败：{}", identityToken, e);
            return Result.error("苹果账号验证失败，请退出重试！");
        }
    }


    @ApiOperation("苹果登录")
    @RequestMapping(value = "/insertAppleUser", method = RequestMethod.GET)
    public Result insertAppleUser(@RequestParam String appleId){
        return userService.iosRegister(appleId);
    }

    @RequestMapping(value = "/iosBindMobile", method = RequestMethod.POST)
    @ApiOperation("苹果登录绑定手机号")
    @ResponseBody
    public Result iosBindMobile(@RequestParam String phone,@RequestParam String code,@RequestParam String appleId,@RequestParam String platform,@RequestParam Integer sysPhone) {
        return userService.iosBindMobile(phone, code, appleId, platform, sysPhone);
    }


    @RequestMapping(value = "/wxAppLogin", method = RequestMethod.POST)
    @ApiOperation("微信APP登录")
    @ResponseBody
    public Result wxAppLogin(@RequestParam String wxOpenId,@RequestParam String token) {
        return userService.wxAppLogin(wxOpenId,token);
    }


    @RequestMapping(value = "/wxBindMobile", method = RequestMethod.POST)
    @ApiOperation("微信登录绑定手机号")
    @ResponseBody
    public Result wxBindMobile(@RequestParam String phone,@RequestParam String code,@RequestParam String wxOpenId,@RequestParam String token,@RequestParam String platform,@RequestParam Integer sysPhone) {
        return userService.wxBindMobile(phone, code, wxOpenId, token, platform, sysPhone);
    }

    @ApiOperation("用户端openid登录呢")
    @RequestMapping(value = "/openid/login", method = RequestMethod.GET)
    @ResponseBody
    public Result loginByOpenId(@RequestParam String openId) {
        return userService.loginByOpenId(openId);
    }


    @RequestMapping(value = "/registerCode", method = RequestMethod.POST)
    @ApiOperation("app或h5注册或登录")
    @ResponseBody
    public Result registerCode( String phone, String msg,String platform, Integer sysPhone,String openId,String inviterCode) {
        return userService.registerCode(phone,msg,platform,sysPhone,openId,inviterCode);
    }

    @ApiOperation("用户端发送验证码")
    @RequestMapping(value = "/sendMsg/{phone}/{state}", method = RequestMethod.GET)
    @ResponseBody
    public Result sendMsg(@PathVariable String phone, @PathVariable String state) {
        return userService.sendMsg(phone, state);
    }

    @ApiOperation("解密手机号")
    @RequestMapping(value = "/selectPhone",method = RequestMethod.POST)
    public Result getPhoneNumberBeanS5(@RequestBody WxPhone wxPhone) {
        return UserConstantInterface.decryptS5(wxPhone.getDecryptData(), wxPhone.getKey(), wxPhone.getIv());
    }
    @ApiParam("登录app")
    @RequestMapping(value = "/loginApp",method = RequestMethod.POST)
    public  Result loginApp(@RequestParam String phone,@RequestParam String password){
        return userService.loginApp(phone,password);
    }
    @CrossOrigin
    @ApiParam("注册app")
    @RequestMapping(value = "/registApp",method = RequestMethod.POST)
    public  Result registApp(@RequestParam String userName,@RequestParam String phone,@RequestParam String password,
                             String msg,@RequestParam String platform,String invitation,String openId){
       return userService.registApp(userName,phone,password,msg,platform,invitation,openId);
    }

    @ApiOperation("用户端忘记密码")
    @RequestMapping(value = "/forgetPwd", method = RequestMethod.POST)
    @ResponseBody
    public Result forgetPwd(String pwd, String phone, String msg) {
        return userService.forgetPwd(pwd, phone, msg);
    }

    @GetMapping("/selectCity")
    @ApiOperation("根据经纬度获取城市")
    public Result selectCity(String lat, String lng) {
        String way = commonInfoService.findOne(414).getValue();
        if("1".equals(way)){
            CommonInfo one = commonInfoService.findOne(217);
            String url = "https://apis.map.qq.com/ws/geocoder/v1/";
            Map<String, String> maps = new HashMap<>();
            maps.put("location", lat + "," + lng);
            maps.put("key", one.getValue());
            String data = HttpClientUtil.doGet(url, maps);
            JSONObject jsonObject = JSON.parseObject(data);
            String status = jsonObject.getString("status");
            if ("0".equals(status)) {
                JSONObject result = jsonObject.getJSONObject("result");
                JSONObject adInfo = result.getJSONObject("ad_info");
                String city = adInfo.getString("city");
                return Result.success().put("data", city);
            } else {
                log.error("转换失败！！！原因:" + jsonObject.getString("message"));
            }
            return Result.error("获取定位失败！");
        }else{
            String value = commonInfoService.findOne(415).getValue();
            String url="http://api.tianditu.gov.cn/geocoder";
            Map<String,String> param=new HashMap<>();
            JSONObject postStr=new JSONObject();
            postStr.put("lon",lng);
            postStr.put("lat",lat);
            postStr.put("ver","1");
            param.put("postStr",postStr.toJSONString());
            param.put("type","geocode");
            param.put("tk",value);
            String s = HttpClientUtil.doGet(url,param);
            JSONObject jsonObject = JSONObject.parseObject(s);
            String status = jsonObject.getString("status");
            if("0".equals(status)){
                JSONObject result = jsonObject.getJSONObject("result");
                JSONObject addressComponent = result.getJSONObject("addressComponent");
                String city = addressComponent.getString("city");
                return Result.success().put("data", city);
            }
            return Result.error("获取定位失败！");
        }
    }


    @GetMapping("/getOpenId")
    @ApiOperation("公众号根据code换取openId")
    public Result getOpenId(String code,Long userId) {
        try {
            //微信appid
            CommonInfo one = commonInfoService.findOne(5);
            //微信秘钥
            CommonInfo two = commonInfoService.findOne(21);
            SnsToken snsToken = SnsAPI.oauth2AccessToken(one.getValue(), two.getValue(), code);
            String openid = snsToken.getOpenid();
            return Result.success().put("data",openid);
        } catch (Exception e) {
            throw new RuntimeException("GET_OPENID_FAIL");
        }

    }


    @GetMapping("/bindOpenId")
    @ApiOperation("用户绑定公众号openId")
    public Result bindOpenId(Long userId,String openId){
        UserEntity userEntity=new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setWxOpenId(openId);
        userService.updateById(userEntity);
        return Result.success();
    }



}
