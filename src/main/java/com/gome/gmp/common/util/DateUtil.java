package com.gome.gmp.common.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.StringTokenizer;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.FastDateFormat;

/**
 * 日期处理函数
 * 
 * @author wubin
 */
@SuppressWarnings("all")
public class DateUtil {

	/**
	 * 其他时间值转换为毫秒单位：秒
	 */
	public static final long UNIT_SECOND_TIME = 1000;

	/**
	 * 其他时间值转换为毫秒单位：分钟
	 */
	public static final long UNIT_MINUS_TIME = 60 * UNIT_SECOND_TIME;

	/**
	 * 其他时间值转换为毫秒单位：小时
	 */
	public static final long UNIT_HOUR_TIME = 60 * UNIT_MINUS_TIME;

	/**
	 * 其他时间值转换为毫秒单位：天
	 */
	public static final long UNIT_DAY_TIME = 24 * UNIT_HOUR_TIME;

	/**
	 * 其他时间值转换为毫秒单位：周
	 */
	public static final long UNIT_WEEK_TIME = 7 * UNIT_DAY_TIME;

	/**
	 * 时间单位名称：秒
	 */
	public static final String UNIT_SECOND_NAME = "秒";

	/**
	 * 时间单位名称：分钟
	 */
	public static final String UNIT_MINUS_NAME = "分钟";

	/**
	 * 时间单位名称：小时
	 */
	public static final String UNIT_HOUR_NAME = "小时";

	/**
	 * 时间单位名称：天
	 */
	public static final String UNIT_DAY_NAME = "天";

	/**
	 * 缺省日期时间格式字符串
	 */
	public static final String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";

	/**
	 * 缺省日期时间格式
	 */
	public static final FastDateFormat DEFAULT_DATE_FORMAT = FastDateFormat.getInstance(PATTERN_YYYY_MM_DD);

	/**
	 * 缺省日期时间格式字符串
	 */
	public static final String PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 日期格式化模式（时间类型数据），精确到分
	 * <p>
	 * 日期格式化模式，使用此模式将日期格式化为“2012-10-08 10:10”，一般用于时间类型数据格式化
	 * </p>
	 */
	public static final String PATTERN_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

	/**
	 * 日期格式化模式（时间类型数据），精确到时
	 * <p>
	 * 日期格式化模式，使用此模式将日期格式化为“2012-10-08 10”，一般用于时间类型数据格式化
	 * </p>
	 */
	public static final String PATTERN_YYYY_MM_DD_HH = "yyyy-MM-dd HH";

	public static final String PATTERN_YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";

	/**
	 * 缺省日期时间格式
	 */
	public static final FastDateFormat DEFAULT_DATETIME_FORMAT = FastDateFormat.getInstance(PATTERN_YYYY_MM_DD_HH_MM_SS);

	/**
	 * @return 地区属性
	 */
	public static Locale getCurrentLocale() {
		return Locale.getDefault();
	}

	/**
	 * 私有构造函数
	 */
	private DateUtil() {
	}

	/**
	 * 获取当前日期 格式：yyyy-MM-dd
	 * 
	 * @author wubin
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String getDate() {
		Date date = new Date();
		return DateFormatUtils.ISO_DATE_FORMAT.format(date);
	}

	/**
	 * 获取指定日期的字符串形式 格式：yyyy-MM-dd
	 * 
	 * @author l00137148
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String getDate(Date date) {
		if (date == null) {
			return "";
		}
		return DateFormatUtils.ISO_DATE_FORMAT.format(date);
	}

	/**
	 * 获取指定日期格式的当前日期
	 * 
	 * @author wubin
	 * @param format
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String getDateByFormatString(String format) {
		Date date = new Date();
		return FastDateFormat.getInstance(format).format(date);
	}

	/**
	 * 获取指定日期格式的当前日期
	 * 
	 * @author wubin
	 * @param format
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String dateFormatString(Date date, String format) {
		return FastDateFormat.getInstance(format).format(date);
	}

	/**
	 * 获取当前日期（包含时间） 格式：yyyy-MM-dd HH:mm:ss
	 * 
	 * @author wubin
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String getDateTime() {
		Date date = new Date();
		return getDateTime(date);
	}

	/**
	 * 获取指定日期（包含时间）的字符串格式 格式：yyyy-MM-dd HH:mm:ss
	 * 
	 * @author l00137148
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String getDateTime(Date date) {
		if (date == null) {
			return "";
		}
		return DEFAULT_DATETIME_FORMAT.format(date);
	}

	/**
	 * 把yyyy-MM-dd HH:mm:ss字符串转换成Date对象
	 * 
	 * @param time
	 *            String
	 * @return Date
	 */
	public static Date getDate(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_YYYY_MM_DD_HH_MM_SS);
		return sdf.parse(time, new ParsePosition(0));
	}

	/**
	 * 把yyyy-MM-dd HH:mm:ss字符串转换成Date对象
	 * 
	 * @param time
	 *            String
	 * @return Date
	 */
	public static Date getDate(String time, String format) {
		// 检查time是否为空
		if (time == null) {
			return null;
		}
		// 如果pattern为空
		if (format == null) {
			// 设置pattern为PATTERN_YYYY_MM_DD_HH_MM_SS
			format = PATTERN_YYYY_MM_DD_HH_MM_SS;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(time, new ParsePosition(0));
	}

	/**
	 * 对日期增加相应年份 年份可以传入负值
	 * 
	 * @param date
	 * @param amount
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static Date addYears(Date date, int amount) {
		return add(date, Calendar.YEAR, amount);
	}

	/**
	 * 对日期增加相应月份 月份可以传入负值
	 * 
	 * @param date
	 * @param amount
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static Date addMonths(Date date, int amount) {
		return add(date, Calendar.MONTH, amount);
	}

	/**
	 * 对日期增加相应的天数
	 * 
	 * @param date
	 * @param amount
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static Date addDays(Date date, int amount) {
		Date rtnDate = add(date, Calendar.DATE, amount);
		return rtnDate;
	}

	/**
	 * Adds to a date returning a new object. The original date object is
	 * unchanged.
	 * 
	 * @param date
	 *            the date, not null
	 * @param calendarField
	 *            the calendar field to add to
	 * @param amount
	 *            the amount to add, may be negative
	 * @return the new date object with the amount added
	 * @throws IllegalArgumentException
	 *             if the date is null
	 */
	public static Date add(Date date, int calendarField, int amount) {
		if (date == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(calendarField, amount);
		return c.getTime();
	}

	/**
	 * 获得年份和月份信息的字符串形式 格式：yyyyMM
	 * 
	 * @param month
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String getYearMonthInfo(int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + month);
		if (10 > cal.get(Calendar.MONTH) + 1) {
			return cal.get(Calendar.YEAR) + "0" + (cal.get(Calendar.MONTH) + 1);
		} else {
			return cal.get(Calendar.YEAR) + "" + (cal.get(Calendar.MONTH) + 1);
		}
	}

	/**
	 * 获得当前年份和月份信息的字符串形式 格式：yyyyMM
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String getYearMonthInfo() {
		return getYearMonthInfo(0);
	}

	/**
	 * 取当月第一天 的字符串形式 格式：yyyy-MM-dd
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String getFirstDateCurrentMonth() {
		return getFirstDateByMonth(0);
	}

	/**
	 * 取当月最后一天的字符串形式 格式：yyyy-MM-dd
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String getLastDateCurrentMonth() {
		return getLastDateByMonth(0);
	}

	/**
	 * 获取上个月最后一天的字符串形式 格式：yyyy-MM-dd
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String getLastMonthLastDayByCurrent() {
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date strDateTo = calendar.getTime();
		return getDate(strDateTo);
	}

	/**
	 * 获得某月第一天的字符串形式 格式：yyyy-MM-dd
	 * 
	 * @param month
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String getFirstDateByMonth(int month) {
		Calendar cal = Calendar.getInstance();
		int current_month = cal.get(Calendar.MONTH) + month;
		cal.set(Calendar.MONTH, current_month);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		return getDate(cal.getTime());
	}

	/**
	 * 获得某月最后一天的字符串形式 格式：yyyy-MM-dd
	 * 
	 * @param month
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String getLastDateByMonth(int month) {
		Calendar cal = Calendar.getInstance();
		int current_month = cal.get(Calendar.MONTH) + month + 1;
		cal.set(Calendar.MONTH, current_month);
		cal.set(Calendar.DATE, 0);
		return getDate(cal.getTime());
	}

	/**
	 * 得到两个日期相差小时数
	 * 
	 * @param endDate
	 * @param beginDate
	 * @return 两个日期相差小时数
	 */
	public static long diffHour(Date endDate, Date beginDate) {
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(endDate);
		Calendar beginCalendar = Calendar.getInstance();
		beginCalendar.setTime(beginDate);
		long diff = endCalendar.getTimeInMillis() - beginCalendar.getTimeInMillis();
		return diff / UNIT_HOUR_TIME;
	}

	/**
	 * 得到两个日期之间的年月信息
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return String[]
	 */
	public static String[] getYearAndMonthCount(String beginDate, String endDate) {
		if (beginDate == null) {
			beginDate = getDateTime();
		}
		if (endDate == null) {
			endDate = getDateTime();
		}
		int[] beginDates = getYYMMDDHHMISS(beginDate);
		int[] endDates = getYYMMDDHHMISS(endDate);
		int year = beginDates[0];
		int month = beginDates[1];
		int monthCount = (endDates[0] - year) * 12 + (endDates[1] - month);
		if (monthCount <= 0) {
			return new String[] { getYear() + "" + getMonth() };
		}
		String[] yearAndMonths = new String[monthCount + 1];
		for (int i = 0; i <= monthCount; i++) {
			if (month > 12) {
				month = 1;
				year++;
			}
			yearAndMonths[i] = year + "" + month;
			month++;
		}
		return yearAndMonths;
	}

	/**
	 * 得到一天的起始日期和结束日期
	 * 
	 * @param time
	 * @return String[2]{起始日期,结束日期}
	 */
	public static String[] getDayBeginAndEnd(String dateTime) {
		String shortTime = getShortDateFromFormat(dateTime);
		return new String[] { (shortTime + " 00:00:00"), (shortTime + " 23:59:59") };
	}

	/**
	 * 得到传入日期所在的周的起始日期和终止日期
	 * 
	 * @param time
	 * @return 传入日期所在的周的起始日期和终止日期
	 */
	public static Date[] getWeekFirstAndLast(String dateTime) {
		String fromatTime = getShortDateFromFormat(dateTime) + " 00:00:00";
		String dayForWeek = getDayInWeek(fromatTime);
		int from = 0;
		int to = 0;
		if ("星期日".equals(dayForWeek)) {
			from = 6;
			to = 0;
		} else if ("星期一".equals(dayForWeek)) {
			from = 0;
			to = 6;
		} else if ("星期二".equals(dayForWeek)) {
			from = 1;
			to = 5;
		} else if ("星期三".equals(dayForWeek)) {
			from = 2;
			to = 4;
		} else if ("星期四".equals(dayForWeek)) {
			from = 3;
			to = 3;
		} else if ("星期五".equals(dayForWeek)) {
			from = 4;
			to = 2;
		} else if ("星期六".equals(dayForWeek)) {
			from = 5;
			to = 1;
		}
		Date fromDate = addDays(getDate(fromatTime), (-1 * from));
		Date toDate = addDays(getDate(fromatTime), to);
		return new Date[] { fromDate, toDate };
	}

	/**
	 * 得到当前日期所在月的第一天和最后一天时间
	 * 
	 * @param time
	 * @return 当前日期所在月的第一天和最后一天时间
	 */
	public static String[] getMonthFirstAndLast(String dateTime) {
		String shortTime = getShortDateFromFormat(dateTime);
		String[] YMD = shortTime.split("-");
		String fromDate = YMD[0] + "-" + YMD[1] + "-01 00:00:00";
		int maxDayInMonth = getDaysOfMonth(Integer.parseInt(YMD[0]), Integer.parseInt(YMD[1]));
		String toDate = YMD[0] + "-" + YMD[1] + "-" + maxDayInMonth + " 23:59:59";
		return new String[] { fromDate, toDate };
	}

	/**
	 * 得到当前日期所在年的第一天和最后一天时间
	 * 
	 * @param time
	 * @return 当前日期所在年的第一天和最后一天时间
	 */
	public static String[] getYearFirstAndLast(String dateTime) {
		String shortTime = getShortDateFromFormat(dateTime);
		String[] YMD = shortTime.split("-");
		String fromDate = YMD[0] + "-01-01 00:00:00";
		String toDate = YMD[0] + "-12-31 23:59:59";
		return new String[] { fromDate, toDate };
	}

	/**
	 * 得到给定日期是本月的第几周星期数
	 * 
	 * @param time
	 * @return int
	 */
	public static String getDayInWeek(String time) {
		Date currentDate = getDate(time);
		SimpleDateFormat formater = new SimpleDateFormat("EE");
		return formater.format(currentDate);
	}

	/**
	 * 状态时间是否为当月时间
	 * 
	 * @param terminal
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean isNowMonth(String dateStr) {
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String defaultTime = sdf.format(new Date());
		String statusDate = null;
		try {
			statusDate = new SimpleDateFormat("yyyyMM").format(new SimpleDateFormat("yyyy-MM").parse(dateStr));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (defaultTime.equals(statusDate)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 得到指定日期的年，月，日，小时，分钟，秒
	 * 
	 * @param date
	 *            字符串可以是(yyyy-MM-dd hh:mm:ss)或（yyyy-MM-dd)
	 * @return int
	 */
	public static int[] getYYMMDDHHMISS(String date) {
		int[] iYYMMDDHHMISS = new int[] { 0, 0, 0, 0, 0, 0 };
		String[] dates = date.trim().split(" ");
		String[] days = dates[0].split("-");
		for (int i = 0; i < days.length; i++) {
			iYYMMDDHHMISS[i] = Integer.parseInt(days[i]);
		}
		if (dates.length == 2) {
			String[] times = dates[1].split(":");
			for (int i = 0; i < times.length; i++) {
				iYYMMDDHHMISS[i + 3] = Integer.parseInt(times[i]);
			}
		}
		return iYYMMDDHHMISS;
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
	 * 取没有时间的日期字段
	 * 
	 * @param formatTime
	 * @return 日期字段
	 */
	public static String getShortDateFromFormat(String formatTime) {
		return getDateAndTimeFromFormat(formatTime)[0];
	}

	/**
	 * 将一个格式化的日期字符串分割成日期和时间数组 <br>
	 * [yyyy-mm-dd hh:mi:ss] ---> {{mmdd},{hhmu}};
	 * 
	 * @param formatTime
	 * @return 日期和时间数组
	 */
	public static String[] getDateAndTimeFromFormat(String formatTime) {
		if (formatTime == null || "".equals(formatTime)) {
			return new String[] { null, null };
		}
		StringTokenizer tokFormat = new StringTokenizer(formatTime, " ");
		// 年-月-日
		String strDate[] = null;
		// 小时-分钟-秒
		String strTime[] = null;
		while (tokFormat.hasMoreTokens()) {
			String strFormat = tokFormat.nextToken().trim();
			if (strFormat == null || strFormat.length() == 0) {
				return null;
			}
			// 日期
			if (strFormat.indexOf("-") > 0) {
				strDate = strFormat.split("-");
			}
			if (strFormat.indexOf(":") > 0) {
				strTime = strFormat.split(":");
			}
		}
		for (int i = 0; i < strDate.length; i++) {
			if (i == 0) {
				if (strDate[i].length() == 2) {
					strDate[i] = "20" + strDate[i];
				}
			} else {
				if (strDate[i].length() < 2) {
					strDate[i] = "0" + strDate[i];
				}
			}
		}
		for (int i = 0; i < strTime.length; i++) {
			if (strDate[i].length() < 2) {
				strDate[i] = "0" + strDate[i];
			}
		}
		String mmdd = strDate[0] + "-" + strDate[1] + "-" + strDate[2];
		String hhmu = strTime[0] + ":" + strTime[1] + ":" + strTime[2];
		return new String[] { mmdd, hhmu };
	}

	/**
	 * 求给定 某年、某月的最大天数.例如getDaysOfMonth(2000,1)范围31,getDaysOfMonth(2000,2)返回28
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @return 给定年、月的最大天数(1月返回31,2月返回28或29,3月返回31,...,12月返回31)
	 */
	static public int getDaysOfMonth(int year, int month) {
		return (int) ((toLongTime(month == 12 ? (year + 1) : year, month == 12 ? 1 : (month + 1), 1) - toLongTime(year, month, 1)) / (24 * 60 * 60 * 1000));
	}

	/**
	 * 从给定的 year,mongth,day 得到时间的long值表示(a point in time that is <tt>time</tt>
	 * milliseconds after January 1, 1970 00:00:00 GMT).
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @return 给定的 year,mongth,day 得到时间的long值表示
	 */
	public static long toLongTime(int year, int month, int day) {
		return toLongTime(year, month, day, 0, 0, 0);
	}

	/**
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param min
	 * @param sec
	 * @return 时间的long值表示
	 */
	public static long toLongTime(int year, int month, int day, int hour, int min, int sec) {
		Calendar staticCal = null;
		if (staticCal == null) {
			staticCal = Calendar.getInstance();// new GregorianCalendar();
		}
		staticCal.clear();
		staticCal.set(Calendar.YEAR, year);
		staticCal.set(Calendar.MONTH, month - 1);
		staticCal.set(Calendar.DAY_OF_MONTH, day); // day-1??
		staticCal.set(Calendar.HOUR_OF_DAY, hour);
		staticCal.set(Calendar.MINUTE, min);
		staticCal.set(Calendar.SECOND, sec);
		return staticCal.getTime().getTime();
	}

	/**
	 * 根据与当前时间的月份差 获取该月上个月的 最后一个周日
	 * 
	 * @param month
	 *            0 表示当前月,month<0 表示过去的,month>0表示将来的
	 * @return
	 */
	public static String getLastMonthEndSundayByMonthNum(int month) {
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.MONTH, month);// 月份+1
		instance.set(Calendar.DAY_OF_MONTH, 1);// 天设为一个月的第一天
		instance.add(Calendar.DAY_OF_MONTH, -1);// 本月最后一天
		instance.add(Calendar.DAY_OF_MONTH, 1 - instance.get(Calendar.DAY_OF_WEEK));// 根据月末最后一天是星期几，向前偏移至最近的周日
		Date strDateTo = instance.getTime();
		return getDate(strDateTo);
	}

	/**
	 * 根据与当前时间的月份差 获取该月下个月的 第一个周六
	 * 
	 * @param month
	 *            0 表示当前月,month<0 表示过去的,month>0表示将来的
	 * @return
	 */
	public static String getNextMonthFirstSaturdayByMonthNum(int month) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 1 + month);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int i = 1;
		while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
			cal.set(Calendar.DAY_OF_MONTH, i++);
		}
		Date firstSaturday = cal.getTime();
		return getDate(firstSaturday);
	}

	/**
	 * 计算目标日期与当前日期相差的月份差值
	 * 
	 * @param targetDate
	 * @return
	 */
	public static int calculateDifferenceMonth(String targetDate) {
		int differenceMonth = 0;
		Calendar targetCalendar = new GregorianCalendar();
		targetCalendar.setTime(DateUtil.getDate(targetDate, DateUtil.PATTERN_YYYY_MM_DD));
		Calendar nowCalendar = new GregorianCalendar();
		nowCalendar.setTime(new Date());
		differenceMonth = (targetCalendar.get(Calendar.YEAR) - nowCalendar.get(Calendar.YEAR)) * 12 + targetCalendar.get(Calendar.MONTH) - nowCalendar.get(Calendar.MONTH);
		return differenceMonth;
	}

	/**
	 * 获取某月份的所有日期
	 * 
	 * @param yyyyMM
	 * @param pattern
	 * @return
	 * @author wubin
	 */
	public static String[] getDatesByMonth(String yyyyMM, String pattern) {
		int lastDay = com.gome.framework.util.DateUtil.getLastDayOfMonth(com.gome.framework.util.DateUtil.parse(yyyyMM, "yyyyMM"));
		String[] arr = new String[lastDay];
		// 组装某月每天日期串
		StringBuffer sb = null;
		for (int i = 1; i <= lastDay; i++) {
			sb = new StringBuffer();
			sb.append(yyyyMM);
			if (i < 10) {
				sb.append("0");
			}
			sb.append(i);
			String date = com.gome.framework.util.DateUtil.format(com.gome.framework.util.DateUtil.parse(sb.toString(), "yyyyMMdd"), pattern);
			arr[i - 1] = date;
		}
		return arr;
	}

	/**
	 * 获取某月份的所有日期，默认格式：yyyy-MM-dd
	 * 
	 * @param yyyyMM
	 * @return
	 * @author wubin
	 */
	public static String[] getDatesByMonth(String yyyyMM) {
		return getDatesByMonth(yyyyMM, com.gome.framework.util.DateUtil.DATE_FORMAT_DATE_STRING);
	}

	/**
	 * 计算两个时间的小时差
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 * @author wubin
	 */
	public static double getDiffHours(Date startTime, Date endTime) {
		long diff = endTime.getTime() - startTime.getTime();
		double hours = diff / (1000 * 60 * 60);
		return hours;
	}

	/**
	 * 获取小时数
	 * 
	 * @param dateTime
	 * @return
	 * @author wubin
	 */
	public static int getHours(Date dateTime) {
		Calendar startCal = Calendar.getInstance(Locale.CHINA);
		startCal.setTime(dateTime);
		int hour = startCal.get(Calendar.HOUR_OF_DAY);// 小时
		return hour;
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param earlyDateStr
	 *            较小的时间
	 * @param lateDateStr
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(String earlyDateStr, String lateDateStr) {
		Date earlyDate = getDate(earlyDateStr,PATTERN_YYYY_MM_DD);
		Date lateDate = getDate(lateDateStr,PATTERN_YYYY_MM_DD);
		Calendar cal = Calendar.getInstance();
		cal.setTime(earlyDate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(lateDate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (UNIT_DAY_TIME);
		return Integer.parseInt(String.valueOf(between_days))+1;
	}
}
