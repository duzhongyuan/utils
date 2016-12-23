/**   
 * Copyright © 2016 广州佳都数据服务有限公司. All rights reserved.
 * Company 广州佳都数据服务有限公司	POSP系统
 *
 * @Title: DateUtils.java 
 * @Prject: FinancialPlatform_ParamDownload
 * @Package: com.pci.FinancialPlatform.tools 
 * @Description: 时间日期工具类
 * @author: dzy   
 * @date: 2016年11月8日 下午8:16:30 
 * @version: V1.0   
 */
package com.pci.data.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;


/** 
 * @ClassName: DateUtils 
 * @Description: 时间日期工具类
 * @since: 1.0.0
 * @author: dzy
 * @date: 2016年11月8日 下午8:16:30  
 */
public class CustomDateUtils extends DateUtils{

	/**
	 * 日期格式(年-月-日  : yyyy-MM-dd)
	 */
	public static final SimpleDateFormat DATE10 = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 日期格式(年月日  : yyyyMMdd)
	 */
	public static final SimpleDateFormat DATE8 = new SimpleDateFormat("yyyyMMdd");
	
	/**
	 * 日期格式(月日 : MMdd)
	 */
	public static final SimpleDateFormat DATE4 = new SimpleDateFormat("MMdd");
	
	/**
	 * 日期格式(年-月-日 时:分:秒  yyyy-MM-dd HH:mm:ss)
	 */
	public static final SimpleDateFormat TIME19 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 日期格式(年月日 时:分:秒   yyyyMMdd HH:mm:ss)
	 */
	public static final SimpleDateFormat TIME17 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
	
	/**
	 * 日期格式(年月日时分秒  yyyyMMddHHmmss)
	 */
	public static final SimpleDateFormat TIME14 = new SimpleDateFormat("yyyyMMddHHmmss");
	
	/**
	 * 日期格式(年月日时间秒毫秒  yyyyMMddHHmmssSSS)
	 */
	public static final SimpleDateFormat DATE17 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	/**
	 * 日期格式(年月日时分  yyyyMMddHHmm)
	 */
	public static final SimpleDateFormat TIME12 = new SimpleDateFormat("yyyyMMddHHmm");
	
	/**
	 * 日期格式(年月日时分秒  yyMMddHHmmss)
	 */
	public static final SimpleDateFormat TIME12_SECOND = new SimpleDateFormat("yyMMddHHmmss");
	
	/**
	 * 日期格式(时分秒  HHmmss)
	 */
	public static final SimpleDateFormat TIME6 = new SimpleDateFormat("HHmmss");
	
	/**
	 * 日期格式(时分  HHmm)
	 */
	public static final SimpleDateFormat TIME4 = new SimpleDateFormat("HHmm");
	
	/**
	 * 一天的毫秒数
	 */
	private static final long TIME_MILLIS_OF_DAY = 24 * 60 * 60 * 1000; // 一天的毫秒数
	private static final String[] parsePatterns = 
		{"yyyyMMddHHmmss", "yyyyMMdd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd"};
	
	/**
	 * @Title: currentDateToString10 
	 * @Description: 获取当前系统时间
	 * @since: 1.0.0
	 * @return	格式：yyyy-MM-dd
	 */
	public static String currentDateToString10() {
		return DATE10.format(new Date());
	}
	
	/**
	 * @Title: currentDateToString8 
	 * @Description: 获取当前系统日期
	 * @since: 1.0.0
	 * @return 格式：yyyyMMdd
	 */
	public static String currentDateToString8() {
		return DATE8.format(new Date());
	}

	/**
	 * @Title: currentDateToString4 
	 * @Description: 获取当前系统日期
	 * @since: 1.0.0
	 * @return 格式：MMdd
	 */
	public static String currentDateToString4() {
		return DATE4.format(new Date());
	}

	/**
	 * @Title: currentTimeToString19 
	 * @Description: 获取当前系统时间
	 * @since: 1.0.0
	 * @return 格式：yyyy-MM-dd HH:mm:ss
	 */
	public static String currentTimeToString19() {
		return TIME19.format(new Date());
	}

	/**
	 * @Title: currentTimeToString17 
	 * @Description: 获取当前系统时间
	 * @since: 1.0.0
	 * @return 格式：yyyyMMdd HH:mm:ss	
	 */
	public static String currentTimeToString17() {
		return TIME17.format(new Date());
	}

	/**
	 * @Title: currentDateToString17 
	 * @Description: 获取当前系统时间
	 * @since: 1.0.0
	 * @return	yyyyMMddHHmmssSSS
	 */
	public static String currentDateToString17(){
		return DATE17.format(new Date());
	}
	
	/**
	 * @Title: currentTimeToString14 
	 * @Description: 获取当前系统时间
	 * @since: 1.0.0
	 * @return 格式：yyyyMMddHHmmss
	 */
	public static String currentTimeToString14() {
		return TIME14.format(new Date());
	}

	/**
	 * @Title: currentTimeToString12 
	 * @Description: 获取当前系统时间
	 * @since: 1.0.0
	 * @return 格式：yyyyMMddHHmm
	 */
	public static String currentTimeToString12() {
		return TIME12.format(new Date());
	}

	/**
	 * @Title: currentTimeToString12ForSecond 
	 * @Description: 获取当前系统时间
	 * @since: 1.0.0
	 * @return 格式：yyMMddHHmmss
	 */
	public static String currentTimeToString12ForSecond() {
		return TIME12_SECOND.format(new Date());
	}

	/**
	 * @Title: currentTimeToString6 
	 * @Description: 获取当前系统时间
	 * @since: 1.0.0
	 * @return 格式：HHmmss
	 */
	public static String currentTimeToString6() {
		return TIME6.format(new Date());
	}

	/**
	 * @Title: currentTimeToString4 
	 * @Description: 获取当前系统时间
	 * @since: 1.0.0
	 * @return 格式：HHmm
	 */
	public static String currentTimeToString4() {
		return TIME4.format(new Date());
	}

	/**
	 * @Title: yesterdayToString8 
	 * @Description: 获取昨天日期
	 * @since: 1.0.0
	 * @return 格式：yyyyMMdd
	 */
	public static String yesterdayToString8() {
		return getAddDaysToString(-1, DATE8);
	}

	/**
	 * @Title: yesterdayToString4 
	 * @Description: 获取昨天日期
	 * @since: 1.0.0
	 * @return 格式：MMdd
	 */
	public static String yesterdayToString4() {
		return getAddDaysToString(-1, DATE4);
	}

	/**
	 * @Title: getAddDaysToString 
	 * @Description: 获取当前系统日期几天前或几天后的日期
	 * @since: 1.0.0
	 * @param days 天数（days<0表示几天前，days>0表示几天后，days=0表示当日）
	 * @param format 日期格式
	 * @return
	 */
	public static String getAddDaysToString(int days, SimpleDateFormat format) {
		return format.format(new Date((System.currentTimeMillis() + (days * TIME_MILLIS_OF_DAY))));
	}

	/**
	 * @Title: stringToDate 
	 * @Description: 将日期字符串转成Date对象
	 * @since: 1.0.0
	 * @param str		日期字符串  如yyyyMMddHHmmss
	 * @return			Date日期对象
	 * @throws Exception
	 */
	public static Date stringToDate(String str) throws Exception {
		Date date = null;
		try {
			date = parseDate(str, parsePatterns);
		} catch (ParseException e) {
			throw new RuntimeException(str + " to Date format error.", e);
		}
		return date;
	}
	
	
	public static long StringToTime(String dateStr) throws Exception {
		long time = 0l;
		Date date = null;
		try {
			date = parseDate(dateStr, parsePatterns);
			time = date.getTime();
		} catch (ParseException e) {
			throw new RuntimeException(dateStr + " to Date format error.", e);
		}
		return time;
	}
	
}
