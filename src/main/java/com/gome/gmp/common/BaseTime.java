/* 创建日期 2006-6-21
 *
 * 作者 毛志乾 (zqmao)
 *
 * 项目名称：电报终端 (NTS)
 * 
 * Copyright 2005 Metarnet, Inc. All right reserved. */
package com.gome.gmp.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author wubin
 * 
 *         基本时间处理类,取得当前日期相类参数
 */
public class BaseTime {

	/** <code>SECOND</code> 15min */
	public static final long SECOND = 1000;

	/** <code>MIN</code> 的注释 */
	public static final long MIN = 60 * SECOND;

	/** <code>HOUR</code> 的注释 */
	public static final long HOUR = 60 * MIN;

	/** <code>DAY</code> 的注释 */
	public static final long DAY = 24 * HOUR;

	/** <code>WEEK</code> 7day */
	public static final long WEEK = 7 * DAY;

	/** <code>MONTH</code> 31day */
	public static final long MONTH = DAY * 30;

	/** <code>SEC_SEC</code> 的注释 */
	public static final int SEC_SEC = 0;

	/** <code>MIN_MIN</code> 的注释 */
	public static final int MIN_MIN = 1;

	/** <code>HOUR_HOUR</code> 的注释 */
	public static final int HOUR_HOUR = 2;

	/** <code>DAY_DAY</code> 的注释 */
	public static final int DAY_DAY = 3;

	/** <code>WEEK_WEEK</code> 的注释 */
	public static final int WEEK_WEEK = 4;

	/** <code>MONTH_MONTH</code> 的注释 */
	public static final int MONTH_MONTH = 5;

	// 日期格式
	public static String dateFormat = "yyyy-MM-dd";

	// 时间格式
	public static String timeFormat = "HH:mm:ss";

	// 完成日期格式
	public static String chinaFormat = dateFormat + " " + timeFormat;

	// mm/dd/yyy HH:mm格式
	public static String dateTimeString = "MM/dd/yyyy HH:mm";

	/**
	 * @return 地区属性
	 */
	public static Locale getCurrentLocale() {
		return Locale.getDefault();
	}

	/**
	 * @return 当前系统时间 (String 形式）
	 */
	public static String getTime() {
		SimpleDateFormat bartDateFormat = new SimpleDateFormat(chinaFormat);
		Date date = new Date();
		return bartDateFormat.format(date);
	}

	/**
	 * @return 当前系统时间 (String 形式）
	 */
	public static String getShortTime() {
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		Date date = new Date();
		return bartDateFormat.format(date);
	}

	/**
	 * @param format
	 * @return 当前系统时间 (String 形式）
	 */
	public static String getShortTime(String format) {
		SimpleDateFormat bartDateFormat = new SimpleDateFormat(format);
		Date date = new Date();
		return bartDateFormat.format(date);
	}

	public static String getDateBeginTime() {
		String date = getShortTime(dateFormat);
		return date + " 00:00:00";
	}

	/**
	 * 得到当前的小时数
	 * 
	 * @return int
	 */
	public static int getHour() {
		Locale currentLocale = getCurrentLocale();
		Calendar rightNow = Calendar.getInstance(currentLocale);
		Date currentDate = rightNow.getTime();
		SimpleDateFormat formater = new SimpleDateFormat("HH");
		return Integer.parseInt(formater.format(currentDate));
	}

	/**
	 * 得到当前的分钟数
	 * 
	 * @return int
	 */
	public static int getMinute() {
		Locale currentLocale = getCurrentLocale();
		Calendar rightNow = Calendar.getInstance(currentLocale);
		Date currentDate = rightNow.getTime();
		SimpleDateFormat formater = new SimpleDateFormat("mm");
		return Integer.parseInt(formater.format(currentDate));
	}

	/**
	 * 得到当前的周数
	 * 
	 * @return int
	 */
	public static int getWeekInYear() {
		Locale currentLocale = getCurrentLocale();
		Calendar rightNow = Calendar.getInstance(currentLocale);
		Date currentDate = rightNow.getTime();
		SimpleDateFormat formater = new SimpleDateFormat("ww");
		return Integer.parseInt(formater.format(currentDate));
	}

	/**
	 * @return 当前月的第一天
	 */
	public static String getFirstOfMonth() {// TODO deprecate this method
		String strMonth = null;
		int month = getMonth();
		if (month < 10) {
			strMonth = "0" + month;
		} else {
			strMonth = "" + month;
		}
		return getYear() + "-" + strMonth + "-" + "01 00:00:00";
	}

	/**
	 * 得到当前的星期数
	 * 
	 * @return int
	 */
	public static int getDayInWeek() {
		Locale currentLocale = getCurrentLocale();
		Calendar rightNow = Calendar.getInstance(currentLocale);
		Date currentDate = rightNow.getTime();
		SimpleDateFormat formater = new SimpleDateFormat("FF");
		return Integer.parseInt(formater.format(currentDate));
	}

	/**
	 * 得到当前日期是几号
	 * 
	 * @return int
	 */
	public static int getDayInMonth() {
		Locale currentLocale = getCurrentLocale();
		Calendar rightNow = Calendar.getInstance(currentLocale);
		Date currentDate = rightNow.getTime();
		SimpleDateFormat formater = new SimpleDateFormat("dd");
		return Integer.parseInt(formater.format(currentDate));
	}

	/**
	 * 得到当前的月数
	 * 
	 * @return int
	 */
	public static int getMonth() {
		Locale currentLocale = getCurrentLocale();
		Calendar rightNow = Calendar.getInstance(currentLocale);
		Date currentDate = rightNow.getTime();
		SimpleDateFormat formater = new SimpleDateFormat("MM");
		return Integer.parseInt(formater.format(currentDate));
	}

	/**
	 * 得到当前的秒数
	 * 
	 * @return int
	 */
	public static int getSecond() {
		Locale currentLocale = getCurrentLocale();
		Calendar rightNow = Calendar.getInstance(currentLocale);
		Date currentDate = rightNow.getTime();
		SimpleDateFormat formater = new SimpleDateFormat("ss");
		return Integer.parseInt(formater.format(currentDate));
	}

	/**
	 * 得到当前的年
	 * 
	 * @return int
	 */
	public static int getYear() {
		Locale currentLocale = getCurrentLocale();
		Calendar rightNow = Calendar.getInstance(currentLocale);
		Date currentDate = rightNow.getTime();
		SimpleDateFormat formater = new SimpleDateFormat("yyyy");
		return Integer.parseInt(formater.format(currentDate));
	}

	/**
	 * 获取当前时间
	 * 
	 * @return Date
	 */
	public static Date getNow() {
		Calendar rightNow = Calendar.getInstance();
		Date dt = rightNow.getTime();
		return dt;
	}
}
