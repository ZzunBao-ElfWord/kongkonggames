package com.pwyl.modules.chats.utils;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {
	/**
	 * 判断是否是购物节日期
	 * @param date 
	 * @return true 是
	 */
	public static boolean isShoppingFestival(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		 
		//判断是否是双十一
		if(month==11 && day==11){
			return true;
		}

		//判断是否是双十二
		if(month==12 && day==12){
			return true;
		}
		 
		return false;
	}
	
	/**
	 * 获取两个日期之间左右年月
	 * @param minDate
	 * @param maxDate
	 * @return
	 * @throws ParseException
	 */
	public static List<String> getMonthBetween(Date minDate, Date maxDate) throws ParseException {
	    ArrayList<String> result = new ArrayList<String>();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

	    Calendar min = Calendar.getInstance();
	    Calendar max = Calendar.getInstance();

	    min.setTime(minDate);
	    min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

	    max.setTime(maxDate);
	    max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

	    Calendar curr = min;
	    while (curr.before(max)) {
	     result.add(sdf.format(curr.getTime()));
	     curr.add(Calendar.MONTH, 1);
	    }

	    return result;
	  }
	
	/**
	 * 获取指定月份天数
	 * @param year 年份（四位数）
	 * @param month 月份（从1开始）
	 * @return
	 */
	public static int getMonthDays(int year, int month) {
	    if (month == 2) {
	        if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
	            return 29;
	        } else {
	            return 28;
	        }
	    } else if (month == 4 || month == 6 || month == 9 || month == 11) {
	        return 30;
	    } else {
	        return 31;
	    }
	}
	
	/** 
	 * 判断时间是否在时间段内 
	 *  
	 * @param date 
	 *            当前时间 yyyy-MM-dd HH:mm:ss 
	 * @param strDateBegin 
	 *            开始时间 00:00
	 * @param strDateEnd 
	 *            结束时间 00:05
	 * @return 在时间段内返回true
	 */  
	public static boolean isInDate(Date date, String strDateBegin, String strDateEnd) {  
		
		if(date==null || StringUtils.isBlank(strDateBegin) || StringUtils.isBlank(strDateEnd)){
			return false;
		}
		
	    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");  
	    String strDate = sdf.format(date);  
	    // 截取当前时间时分秒  
	    int strDateH = Integer.parseInt(strDate.substring(0, 2));  
	    int strDateM = Integer.parseInt(strDate.substring(3, 5));  
	    // 截取开始时间时分秒  
	    int strDateBeginH = Integer.parseInt(strDateBegin.substring(0, 2));  
	    int strDateBeginM = Integer.parseInt(strDateBegin.substring(3, 5));  
	    // 截取结束时间时分秒  
	    int strDateEndH = Integer.parseInt(strDateEnd.substring(0, 2));  
	    int strDateEndM = Integer.parseInt(strDateEnd.substring(3, 5));  
	    
	    if(strDateH >= strDateBeginH && strDateH <= strDateEndH){
	    	
	    	//判断开始时间和结束时间的小时是否一样
	    	if(strDateBeginH == strDateEndH){ //是
	    		
	    		//
	    		if(strDateH == strDateBeginH){
	    			if(strDateM >= strDateBeginM && strDateM <= strDateEndM){
	    				return true;
	    			}
	    		}else{
	    			return false;
	    		}
	    		
	    	}else{ //否
	    		
	    		if(strDateH == strDateBeginH){
	    			
	    			if(strDateM >= strDateBeginM){
	    				return true;
	    			}else{
	    				return false;
	    			}
	    			
	    		}else if(strDateH == strDateEndH){
	    			
	    			if(strDateM <= strDateEndM){
	    				return true;
	    			}else{
	    				return false;
	    			}
	    			
	    		}else{
	    			return true;
	    		}
	    		
	    	}
	    }else{
    		return false;
    	}
	    
		return false;
	}
	/**
	 *
	 * @param pattern，字符串的format格式，例如：yyyy-MM-dd HH:mm:ss
	 * @param date,需要转换为指定格式的日期对象
	 * @return
	 */
	public static String getFormatStrByPatternAndDate(String pattern,Date date){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(date);
	}
	public static Date getDataByFormatString(String pattern,String dateFormatStr){
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			return simpleDateFormat.parse(dateFormatStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将日期的时分秒转为 00:00:00
	 * @param date
	 * @return
	 * @throws
	 */
	public static Date lowDate(Date date){
		String lowDate = getFormatStrByPatternAndDate("yyyy-MM-dd",date) + " 00:00:00";
		return getDataByFormatString("yyyy-MM-dd HH:mm:ss",lowDate);
	}

	/**
	 * 将日期的时分秒转为 23:59:59
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date hightDate(Date date){
		String lowDate = getFormatStrByPatternAndDate("yyyy-MM-dd",date) + " 23:59:59";
		return getDataByFormatString("yyyy-MM-dd HH:mm:ss",lowDate);
	}
	
	/**
	 * 计算d1 到 d2 相差多少时间
	 * @param d1 未来的时间
	 * @param d2 现在的时间
	 * @return 数组下标 0 天 1 时 2 分 3 秒
	 */
	public static long[] dateDiff(Date d1, Date d2) throws ParseException {
		long nd = 1000*24*60*60;//一天的毫秒数
		long nh = 1000*60*60;//一小时的毫秒数
		long nm = 1000*60;//一分钟的毫秒数
		long ns = 1000;//一秒钟的毫秒数
		//获得两个时间的毫秒时间差异
		long diff = d1.getTime() - d2.getTime();
		long day = diff/nd;//计算差多少天
		long hour = diff%nd/nh;//计算差多少小时
		long min = diff%nd%nh/nm;//计算差多少分钟
		long sec = diff%nd%nh%nm/ns;//计算差多少秒
		return new long[]{day,hour,min,sec};
	}
	
	/**
	 * 判断 start 是否大于 end
	 * @param start
	 * @param end
	 * @return
	 * @throws ParseException 
	 */
	public static boolean startThanEnd(Date start, Date end) throws ParseException{
		long[] result = dateDiff(start, end);
		if(result[0]>=0 && result[1]>=0 && result[2]>=0 && result[3]>=0){
			return true;
		}
		return false;
	}
	
	/**
	 * 获取指定日期指定分钟后的日期
	 * @param date
	 * @param minute
	 * @return
	 */
	public static Date getLaterMinute(Date date, Long minute) {
		minute = minute == null ? 0 : minute;
		long curren = date.getTime();
		curren += minute * 60 * 1000;
		return new Date(curren);
	}
	
	/**
	 * 获取指定日期指定分钟前的日期
	 * @param date
	 * @param minute
	 * @return
	 */
	public static Date getPreviouslyMinute(Date date, Long minute) {
		minute = minute == null ? 0 : minute;
		long curren = date.getTime();
		curren -= minute * 60 * 1000;
		return new Date(curren);
	}
	
	/**
	 * 获取指定日期指定天数后的日期
	 * @param date 指定的时间
	 * @param later 指定的天数
	 * @return
	 */
	public static Date getLaterDay(Date date, Long later){
		later = later == null ? 0 : later;
		long current = date.getTime();
		return new Date(current + later * 24 * 60 * 60 * 1000);
	}

	
	/**
	 * 获取指定日期指定天数前的日期
	 * @param date 指定的时间
	 * @param later 指定的天数
	 * @return
	 */
	public static Date getPreviouslyDay(Date date, Long later){
		later = later == null ? 0 : later;
		long current = date.getTime();
		return new Date(current - later * 24 * 60 * 60 * 1000);
	}

	/**
	 * 获取指定日期指定小时后的日期
	 * @param date 指定的时间
	 * @param later 指定的小时
	 * @return
	 */
	public static Date getLaterHour(Date date, Long later){
		later = later == null ? 0 : later;
		long current = date.getTime();
		return new Date(current + later * 60 * 60 * 1000);
	}

	/**
	 * 获取指定日期指定小时前的日期
	 * @param date 指定的时间
	 * @param later 指定的小时
	 * @return
	 */
	public static Date getPreviouslyHour(Date date, Long later){
		later = later == null ? 0 : later;
		long current = date.getTime();
		return new Date(current - later * 60 * 60 * 1000);
	}
	
	/**
	  * 得到本周周一
	  * @return 
	  */
	 public static Date getMondayOfWeek() {
		 Calendar c = Calendar.getInstance();
		 int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		 if (day_of_week == 0){
			 day_of_week = 7;
		 }
		 c.add(Calendar.DATE, -day_of_week + 1);
		 return c.getTime();
	 }

	 /**
	  * 得到本周周日
	  * @return 
	  */
	 public static Date getSundayOfWeek() {
		 Calendar c = Calendar.getInstance();
		 int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		 if (day_of_week == 0){
			 day_of_week = 7;
	 	}
		 c.add(Calendar.DATE, -day_of_week + 7);
		 return c.getTime();
	 }
	 
	 /**
	 * 获取当前月的第一天
	 * @return
	 */
	public static Date getFirstDayOfMonth(){
		Calendar c = Calendar.getInstance();    
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
        return c.getTime();
	}

	/**
	 * 获取当前月的最后一天
	 * @return
	 */
	public static Date getLastDayOfMonth(){
		Calendar ca = Calendar.getInstance();    
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
		return ca.getTime();
	}
	
	/**
	 * 获取上一个月的第一天
	 * @return
	 */
	public static Date getFirstDayOfPreviouslyMonth(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH,1);
        return calendar.getTime();
	}

	/**
	 * 获取上一个月的最后一天
	 * @return
	 */
	public static Date getLastDayOfPreviouslyMonth(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		return calendar.getTime();
	}
	
	/**
	 * 获取上上一个月的第一天
	 * @return
	 */
	public static Date getFirstDayOfPPreviouslyMonth(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -2);
		calendar.set(Calendar.DAY_OF_MONTH,1);
        return calendar.getTime();
	}

	/**
	 * 获取上上一个月的最后一天
	 * @return
	 */
	public static Date getLastDayOfPPreviouslyMonth(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		return calendar.getTime();
	}
	
	/**
	 * 获取指定日期是星期几
	 * @param date
	 * @return
	 */
	public static int getDayOfWeek(Date date) {
		  Calendar cal = Calendar.getInstance();
		  cal.setTime(date);
		  //一周第一天是否为星期天
		  boolean isFirstSunday = (cal.getFirstDayOfWeek() == Calendar.SUNDAY);
		  //获取周几
		  int weekDay = cal.get(Calendar.DAY_OF_WEEK);
		  //若一周第一天为星期天，则-1
		  if (isFirstSunday) {
		  		weekDay = weekDay - 1;
			  	if (weekDay == 0) {
			  		weekDay = 7;
			  	}
		  }
		  return weekDay;
	}

	/**
	 * 获取创建时间
	 * @return
	 */
	public static String createTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	/**
	 * 获取创建时间
	 * @return
	 */
	public static String createDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

	/**
	 * 结束时间
	 * @return
	 */
	public static String endTime(Integer hours) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = DateUtils.addHours(new Date(), hours);
		return sf.format(date);
	}

	/**
	 * 结束时间
	 * @param days 日期
	 * @return
	 */
	public static String addDays(String nowTime, Integer days) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try{
			Date date = DateUtils.addDays(sf.parse(nowTime), days);
			return sf.format(date);
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	//判断是否在规定的时间内签到 nowTime 当前时间 beginTime规定开始时间 endTime规定结束时间
	public static boolean betweenStratEndTime(String startTime, String endTime) throws Exception{
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sf.format(new Date());
		//设置当前时间
		Calendar date = Calendar.getInstance();
		date.setTime(sf.parse(now));
		//设置开始时间
		Calendar start = Calendar.getInstance();
		start.setTime(sf.parse(startTime));//开始时间
		//设置结束时间
		Calendar end = Calendar.getInstance();
		start.setTime(sf.parse(endTime));//开始时间
		//处于开始时间之后，和结束时间之前的判断

		if ((start.after(date) && end.before(date))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取当前日期的几天前/后的日期：传入-1为一天前
	 * @return
	 */
	public static String getBeforeDay(int num){
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, num);
		return sf.format(cal.getTime());
	}

	/**
	 * 获取当前日期的几月前/后的日期：传入-1为上月
	 * @return
	 */
	public static String getBeforeMonth(int num){
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MONTH, num);
		return sf.format(cal.getTime());
	}

	/**
	 * 是否开始
	 * @param startTime 开始时间
	 * @return true 已开始
	 */
	public static boolean isStart(String startTime){
		try{
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date f = sf.parse(startTime);
			return f.before(new Date());
		}catch (Exception e){
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 获取指定时间几分钟前的时间
	 * @return
	 */
	public static String getBeforeMinute(String time, int num){
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		try{
			Date timeDate = sf.parse(time);
			cal.setTime(timeDate);
			cal.add(Calendar.MINUTE, -num);
			return sf.format(cal.getTime());
		}catch (Exception e){
			e.printStackTrace();
		}
		return time;
	}

}
