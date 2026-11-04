package com.pwyl.modules.coupon.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 *
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils
{
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    
    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     * 
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     * 
     * @return String
     */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format)
    {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts)
    {
        try
        {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e)
        {
            return null;
        }
    }
    
    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 秒杀时间判断
     * @param startTime
     * @param endTime
     * @return
     */
    public static String secKillTime(String startTime, String endTime){
        String s = "";
        SimpleDateFormat sf = new SimpleDateFormat(YYYY_MM_DD);
        String nowTime = sf.format(new Date());
        try{
            long now = sf.parse(nowTime).getTime();
            long start = sf.parse(startTime).getTime();
            long end = sf.parse(endTime).getTime();
            if (now < start){
                s = "未开始";
            } else if (now >= start && now <= end) {
                s = "进行中";
            }else{
                s = "已结束";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 用户端秒杀时段是否开始判断
     * @param startHours 整点开始
     * @return
     */
    public static Integer secKillStartHours(Integer startHours, Integer nextHours){
        Integer result = 0;
        String time = DateUtil.createTime().substring(11,13);
        if (time.startsWith("0")){
            time = time.substring(1,2);
        }
        int now = Integer.parseInt(time);
        int start = Integer.parseInt(startHours.toString());
        int end = Integer.parseInt(nextHours.toString());
        if (now < start){
            result = 3; //未开始
        } else if (now >= start && now < end) {
            result = 2; //进行中
        }else{
            result = 1; //已结束
        }
        return result;
    }


    /**
     * 合同状态工具类
     * @param date 日期
     * @param years 年份
     * @return
     */
    public static String getContractStatus(String date, String years){
        String status = "有效";
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date start = sf.parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(start);
            int year = Integer.parseInt(years);
            c.add(Calendar.YEAR, + year);
            start = c.getTime(); //得到过期时间
            long time = start.getTime();
            long now = new Date().getTime();
            if (time < now){
                status = "无效";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return status;
    }

    /**
     * 是否过期
     * @param failureTime 过期时间
     * @return true 已过期
     */
    public static boolean checkTime(String failureTime){
        try{
            SimpleDateFormat sf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
            Date f = sf.parse(failureTime);
            return f.before(new Date());
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }


}
