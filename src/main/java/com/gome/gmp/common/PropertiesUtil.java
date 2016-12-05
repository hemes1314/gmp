package com.gome.gmp.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * properties文件读取工具类
 * 
 * @author wubin
 */
public class PropertiesUtil {

	private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

	private static Properties properties = null;

	private static FileInputStream inputFile = null;

	private static FileOutputStream outputFile = null;

	private static String filePath = null;

	private PropertiesUtil() {
	}

	public static boolean setFile(String path) {
		try {
			filePath = path;
			properties = new Properties();
			inputFile = new FileInputStream(filePath);
			properties.load(inputFile);
		} catch (FileNotFoundException ex) {
			if (logger.isDebugEnabled()) {
				logger.debug("读取属性文件" + filePath + "失败,文件路径错误或者文件不存在!");
			}
			return false;
		} catch (IOException ex) {
			if (logger.isDebugEnabled()) {
				logger.debug("装载文件" + filePath + "失败!");
			}
			return false;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("读取属性文件" + filePath + "成功!");
		}
		return true;
	}

	/**
	 * 得到key的值
	 * 
	 * @param key取得其值的键
	 * @return key的值
	 */
	public static String getValue(String key) {
		if (properties.containsKey(key)) {
			String value = properties.getProperty(key);
			return value;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("无此配置项：" + key);
			}
			return null;
		}
	}

	/**
	 * 改变或添加一个key的值,当key存在于properties文件中时该key的值被value所代替， 当key不存在时,该key的值是value
	 * 
	 * @param key
	 *            要存入的键
	 * @param value
	 *            要存入的值
	 */
	public static void setValue(String key, String value) {
		setValue(key, value, "");
	}

	/**
	 * 改变或添加一个key的值,当key存在于properties文件中时该key的值被value所代替， 当key不存在时,该key的值是value
	 * 
	 * @param key
	 *            要存入的键
	 * @param value
	 *            要存入的值
	 */
	public static void setValue(String key, String value, String description) {
		try {
			outputFile = new FileOutputStream(filePath);
			properties.setProperty(key, value);
			properties.store(outputFile, description);
			outputFile.close();
		} catch (IOException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("Visit " + filePath + " for updating " + key + " value error.");
			}
		}
	}

	/**
	 * Test
	 * 
	 * @param args
	 * @see [类、类#方法、类#成员]
	 */
	public static void main(String[] args) {
		String filePath = PathUtil.getClassPath() + "/conf/env/sap.properties";
		PropertiesUtil.setFile(filePath);
		// System.out.println(PropertiesUtil.getValue("sap.hr.ws.person.url"));
	}
}
