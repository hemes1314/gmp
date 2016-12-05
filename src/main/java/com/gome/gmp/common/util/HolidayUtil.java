package com.gome.gmp.common.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class HolidayUtil {
	
	private static Logger logger = LoggerFactory.getLogger(HolidayUtil.class);

	private static final String HTTP_URL = "http://apis.baidu.com/xiaogg/holiday/holiday";
	
	private static final String API_KEY = "ec81ed1d94142fec06661ac20d5d1901";
	
	/**
	 * 获取本年节假日
	 * @param yyyy
	 * @return
	 * @author wubin
	 */
	public static List<String> getHolidaysByThisYear() {
		return getHolidaysByYear(com.gome.framework.util.DateUtil.getYear(new Date())+"");
	}
	
	/**
	 * 根据年份获取节假日
	 * 
	 * @param yyyy
	 * @return
	 * @author wubin
	 */
	public static List<String> getHolidaysByYear(String yyyy) {
		
		List<String> holidays = new ArrayList<String>();
		for(int i = 1; i <= 12; i++)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(yyyy);
			if(i < 10) {
				sb.append("0");
			}
			sb.append(i);
			holidays.addAll(getHolidaysByMonth(sb.toString()));
		}
		return holidays;
	}
	
	/**
	 * 根据月份获取节假日
	 * 
	 * @param yyyyMM
	 * @return
	 * @author wubin
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getHolidaysByMonth(String yyyyMM) {
		
		String[] arr = DateUtil.getDatesByMonth(yyyyMM, "yyyyMMdd");
		
		// 组装某月每天日期串
		StringBuffer sb = new StringBuffer();
		for(int i = 1; i <= arr.length; i++) {
			sb.append(arr[i-1]).append(",");
		}
		String httpArg = sb.toString();
		
		// 获取日期对应是否是节假日
		String jsonResult = request(HTTP_URL, "d="+httpArg);
		Map<String, String> resultMap = JSON.parseObject(jsonResult, Map.class);
		
		// 将节假日筛选(0:工作日，1:周末，2:节假日)
		List<String> holidays = new ArrayList<String>();
		for(String date : httpArg.split(",")) {
			String isHoliday = String.valueOf(resultMap.get(date));
			if(!"0".equals(isHoliday)) {
				String formatDate = com.gome.framework.util.DateUtil.format(com.gome.framework.util.DateUtil.parse(date, "yyyyMMdd"), "yyyy-MM-dd");
				holidays.add(formatDate);
			}
		}
		
		return holidays;
	}
	
	/**
	 * @param urlAll
	 *            :请求接口
	 * @param httpArg
	 *            :参数
	 * @return 返回结果
	 */
	public static String request(String httpUrl, String httpArg) {
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		httpUrl = httpUrl + "?" + httpArg;
		try {
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			// 填入apikey到HTTP header
			connection.setRequestProperty("apikey", API_KEY);
			connection.connect();
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sbf.append(strRead);
				sbf.append("\r\n");
			}
			reader.close();
			result = sbf.toString();
		} catch (Exception e) {
			logger.error("请求获取节假日接口报错！", e);
		}
		return result;
	}
	
	public static void main(String[] args) {
		
		long start = System.currentTimeMillis();
		System.out.println(getHolidaysByThisYear()); 
		long time = System.currentTimeMillis()-start;
		System.out.println("->用时："+time+"ms");
	}
}
