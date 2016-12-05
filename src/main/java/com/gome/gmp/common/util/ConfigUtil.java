package com.gome.gmp.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 配置文件获取和管理
 * 
 * @author zhangdapeng
 */
public class ConfigUtil {

	public static final String SYSTEM_CONFIG_FILENAME = "systemConfig.properties";

	private Properties prop;
	private final String propFile = "systemConfig.properties";
	private volatile long lastModifiedTime;// 最后修改时间
	private File xmlFile;
	private final Lock lock = new java.util.concurrent.locks.ReentrantLock();
	private static Logger logger = Logger.getLogger(ConfigUtil.class);

	private static final java.util.concurrent.ConcurrentHashMap<String, ConfigUtil> proMap = new ConcurrentHashMap<String, ConfigUtil>();

	/**
	 * 
	 * @param propFile
	 * @param pathType
	 *	 relative相对路径, absolute绝对路径
	 */
	private ConfigUtil(String propFile, String pathType) {
		if (StringUtils.isNotEmpty(pathType) && "absolute".equals(pathType)) {
			xmlFile = new File(propFile);
		} else {
			xmlFile = new File(Thread.currentThread().getContextClassLoader().getResource(propFile).getFile());
		}

		logger.info("加载路径----------------------" + xmlFile.getPath());
		if (xmlFile.exists()) {
			lastModifiedTime = xmlFile.lastModified();
			this.initPropertFile();
		} else {
			logger.error("配置文件" + propFile + "不存在，请检查。");
		}
	}

	/**
	 * 取相对路径配置文件实例
	 * 
	 * @param propFile
	 * @return
	 */
	public static ConfigUtil getInstance(String propFile) {
		if (proMap.get(propFile) == null) {
			proMap.putIfAbsent(propFile, new ConfigUtil(propFile, null));
		}
		return proMap.get(propFile);
	}
	
	/**
	 * 取绝对路径配置文件实例
	 * 
	 * @param propFile
	 * @return
	 */
	public static ConfigUtil getInstanceByAbsolutePath(String propFile) {
		if (proMap.get(propFile) == null) {
			proMap.putIfAbsent(propFile, new ConfigUtil(propFile, "absolute"));
		}
		return proMap.get(propFile);
	}

	/**
	 * 初始化配置文件
	 */
	public void initPropertFile() {
		if (prop == null || this.checkFileReload()) {
			try {
				lock.lock();
				prop = new Properties();
				InputStream ins = new FileInputStream(xmlFile);
				prop.load(ins);
			} catch (Exception e) {
				logger.error("加载systemConfig.properties 文件出错:" + e.getMessage());
			} finally {
				lock.unlock();
			}
		}
	}

	/**
	 * 获取值
	 * 
	 * @param value
	 * @return
	 */
	public synchronized String getValue(String key) {
		this.initPropertFile();
		String v = prop.getProperty(key);
		if (StringUtils.isBlank(v)) {
			return "";
		}
		return v.trim();
	}

	public synchronized void setValue(String key, String value) {
		this.initPropertFile();
		prop.setProperty(key, value);
	}

	/**
	 * 判断是否需要重新加载xml文件
	 */
	private boolean checkFileReload() {
		long new_time = xmlFile.lastModified();
		boolean returnresult = false;// 是否需要重新加载，false不需要，true需要
		if (new_time == 0) {
			logger.info("配置文件" + propFile + "不存在，请检查。");
		} else if (new_time > lastModifiedTime) {
			lastModifiedTime = new_time;
			returnresult = true;
		}
		return returnresult;
	}

}