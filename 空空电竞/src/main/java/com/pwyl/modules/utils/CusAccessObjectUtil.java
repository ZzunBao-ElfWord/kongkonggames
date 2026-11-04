package com.pwyl.modules.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取对象的IP地址等信息
 * @author fang
 * @date 2020/9/23
 */
public class CusAccessObjectUtil {

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * <p>
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * <p>
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
     * 192.168.1.100
     * <p>
     * 用户真实IP为： 192.168.1.110
     *
     * @param request
     * @return
     */
    public static String getIpAddrs(HttpServletRequest request)
            throws Exception {
        if (request == null) {
            throw (new Exception("getIpAddr method HttpServletRequest Object is null"));
        }
        String ipString = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getRemoteAddr();
        }

        // 多个路由时，取第一个非unknown的ip
        String[] arr = ipString.split(",");
        for (String str : arr) {
            if (!"unknown".equalsIgnoreCase(str)) {
                ipString = str;
                break;
            }
        }


        return ipString;
    }

    public static String getIp(HttpServletRequest request) {
        //目前则是网关ip
        String ip = request.getHeader("X-Real-IP");
        if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
            int index = ip.indexOf(',');
            if (index != -1) {
                //只获取第一个值
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            //取不到真实ip则返回空，不能返回内网地址。
            return "";

        }
    }


    /*public static String getAddress(String ip) {
        //""
        //String url = "http://opendata.baidu.com/api.php?query=" + ip + "&co=&resource_id=6006&t=" + System.currentTimeMillis() + "&ie=utf8&oe=utf-8&format=json";
        String url="https://ip.useragentinfo.com/json?ip="+ip;
        String str = HttpClientUtil.doGet(url);
        //{"country": "中国", "short_name": "CN", "province": "陕西省", "city": "西安市", "area": "雁塔区", "isp": "电信", "net": "", "ip": "222.91.197.83", "code": 200, "desc": "success"}
        com.alibaba.fastjson.JSONObject jsStr = JSONObject.parseObject(str);
        System.err.println(jsStr.toJSONString());
        String country = jsStr.getString("country");
        if("中国".equals(country)){
            //如果为中国则不需要拼接国家
            country="";
        }
        String province = jsStr.getString("province");
        String city = jsStr.getString("city");
        String area = jsStr.getString("area");
        String isp = jsStr.getString("isp");
        return country+province+city+area+" "+isp;
    }*/


    public static String getAddress(String ip,String key) {
        //""
        //String url = "http://opendata.baidu.com/api.php?query=" + ip + "&co=&resource_id=6006&t=" + System.currentTimeMillis() + "&ie=utf8&oe=utf-8&format=json";
        String host = "https://ipcity.market.alicloudapi.com";
        String path = "/ip/city/query";
        String method = "GET";
        String appcode = key;
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("coordsys", "coordsys");
        querys.put("ip", ip);


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            String json=EntityUtils.toString(response.getEntity());
            System.out.println(json);
            JSONObject jsonObject= JSON.parseObject(json);
            String code = jsonObject.getString("code");
            if("200".equals(code)){
                JSONObject data = jsonObject.getJSONObject("data");
                JSONObject result = data.getJSONObject("result");
                String country = result.getString("country");
                String prov = result.getString("prov");
                String city = result.getString("city");
                String isp = result.getString("isp");

                if("中国".equals(country)){
                    //如果为中国则不需要拼接国家
                    country="";
                }
                return country+prov+city+" "+isp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }




    //    // 测试
    public static void main(String[] args) {
        //String ip = "222.91.197.83";
        //String ip = "125.188.7.127";
        String ip = "222.91.197.83";

        String host = "https://ipcity.market.alicloudapi.com";
        String path = "/ip/city/query";
        String method = "GET";
        String appcode = "REDACTED_CONFIGURE_LOCALLY";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("coordsys", "coordsys");
        querys.put("ip", ip);


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            String json=EntityUtils.toString(response.getEntity());
            System.out.println(json);
            JSONObject jsonObject= JSON.parseObject(json);
            String code = jsonObject.getString("code");
            if("200".equals(code)){
                JSONObject data = jsonObject.getJSONObject("data");
                JSONObject result = data.getJSONObject("result");
                String country = result.getString("country");
                String city = result.getString("city");
                String isp = result.getString("isp");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }





}