package com.gome.gmp.common.diamond;

/**  
 * @Project: gome-gcc-common
 * @Title: DiamondClient.java
 * @Package: com.gome.bg.diamond
 * @Description: 获取服务器端配置信息
 * @author: QIJJ 
 * @since: 2015-4-27 下午2:01:56
 * @Copyright: 2015. All rights reserved.
 * @Version: v1.0   
 */

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.gome.gmp.common.util.ConfigUtil;
import com.gome.gmp.common.util.FileConfigUtil;
import com.taobao.diamond.manager.DiamondManager;
import com.taobao.diamond.manager.ManagerListener;
import com.taobao.diamond.manager.ManagerListenerAdapter;
import com.taobao.diamond.manager.impl.DefaultDiamondManager;

/**
 * @ClassName DiamondClient
 * @Description: 获取服务器端配置信息
 * @author: QIJJ
 * @since: 2015-4-27 下午2:01:56
 */
public class DiamondClient {

	public static DiamondManager diamondManager = null;
	static Properties properties;

	private static Logger logger = Logger.getLogger(DiamondClient.class);
	private static final long TIME_OUT = 5000L;
	private static final String separator = ",";
	private static String diamondIpList;
	private static String filePath = "/app/weblogic/.diamond.domain";
	private static String configContent;
	private static Map configMap; 

	public static Properties getProperties(List<String> diamondList) {
		if (null == properties) {
			init(diamondList);
		}
		return properties;
	}

	public Map getConfigMap() {
		return configMap;
	}

	public void setConfigMap(Map configMap) {
		this.configMap = configMap;
	}

	public static Properties getProperties() {
		if (null == properties) {
			reloadAllValues();
		}
		return properties;
	}

	/**
	 * 根据key从map中取值
	 * 
	 * @param key
	 * @return
	 */
	public static Object getValueByKey(String key) {
		if (null == properties) {
			return MapUtils.isEmpty(configMap) ? null : configMap.get(key);
		}
		return properties.get(key);
	}

	public static String getStringValueByKey(String key) {
		return (String) getValueByKey(key);
	}

	public static int getIntValueByKey(String key) {
		return Integer.parseInt((String) getValueByKey(key));
	}

	public static double getDoubleValueByKey(String key) {
		return Double.parseDouble((String) getValueByKey(key));
	}

	public static boolean getBooleanValueByKey(String key) {
		return Boolean.parseBoolean((String) (getValueByKey(key)));
	}

	public static String getStringValueByKey(String key, String defaultV) {
		Object value = getValueByKey(key);
		if (value == null) {
			return defaultV;
		}
		return (String) value;
	}

	public static int getIntValueByKey(String key, int defaultV) {
		Object value = getValueByKey(key);
		if (value == null) {
			return defaultV;
		}
		return Integer.parseInt((String) value);
	}

	public static double getDoubleValueByKey(String key, double defaultV) {
		Object value = getValueByKey(key);
		if (value == null) {
			return defaultV;
		}
		return Double.parseDouble((String) value);
	}

	public static boolean getBooleanValueByKey(String key, boolean defaultV) {
		Object value = getValueByKey(key);
		if (value == null) {
			return defaultV;
		}
		return Boolean.parseBoolean((String) (value));
	}

	/**
	 * 重新缓存数据到map
	 */
	public static void reloadAllValues() {
		// 同步获取一份有效的配置信息，按照 本地文件->diamond服务器->上一次正确配置的snapshot的优先顺序获取，
		// 如果这些途径都无效，则返回null
		String availableConfigureInfomation = getDiamondManager().getAvailableConfigureInfomation(5000).trim();
		logger.debug("从diamond取到的数据是：" + availableConfigureInfomation);
		if (StringUtils.isNotEmpty(availableConfigureInfomation)) {
			properties = new Properties();
			try {
				properties.load(new ByteArrayInputStream(availableConfigureInfomation.getBytes()));
			} catch (IOException e) {
				logger.error("根据diamond数据流转成properties异常" + e.getMessage(), e);
			}
		} else {
			logger.error("从diamond取出的数据为空，请检查配置");
		}
	}

	/**
	 * 单例diamondManager
	 * 
	 * @param dataId
	 * @param group
	 * @return
	 */
	public static DiamondManager getDiamondManager() {
		if (diamondManager == null) {
			String configPath = System.getProperty("catalina.home") + "/conf/" + ConfigUtil.SYSTEM_CONFIG_FILENAME;
			ConfigUtil configUtil = ConfigUtil.getInstanceByAbsolutePath(configPath);
			String dataId = configUtil.getValue("diamond.dataId");
			String group = configUtil.getValue("diamond.group");
			String serverIp = configUtil.getValue("diamond.serverIp");
			if (StringUtils.isEmpty(dataId) || StringUtils.isEmpty(group) || StringUtils.isEmpty(serverIp)) {
				throw new IllegalStateException("文件:" + configPath + "中缺少diamond必备配置项(serverIp,dataId,group)");
			} else {
				diamondManager = new DefaultDiamondManager(dataId, group, new ManagerListener() {
					@Override
					public void receiveConfigInfo(String configInfo) {
						// 客户端处理数据的逻辑
						logger.info("配置更改前-------------------------:" + properties.values());
						reloadAllValues();
						logger.info("配置更改后------------------------:" + properties.values());
					}

					@Override
					public Executor getExecutor() {
						return null;
					}
				}, serverIp);
			}
		}
		return diamondManager;
	}

	/**
	 * init(读取多个dataId 与 groupId )
	 * 
	 * @param diamondList
	 * @return void
	 * @author luantian
	 * @exception
	 * @since 1.0.0
	 */
	private static void init(List<String> diamondList) {
		ManagerListenerAdapter diamondListener = new ManagerListenerAdapter() {
			public void receiveConfigInfo(String configInfo) {
				DiamondClient.configContent = configInfo;
				DiamondClient.setConfigMap();
			}
		};
		if (org.apache.commons.lang.StringUtils.isNotEmpty(filePath)) {
			FileConfigUtil fileConfigUtil = new FileConfigUtil(System.getProperty("user.home") + "/.diamond.domain");
			diamondIpList = fileConfigUtil.getValue();
		}
		logger.info("diaond-->filePath:" + System.getProperty("user.home") + " change diamondIpList:" + diamondIpList);
		if (diamondList != null && diamondIpList != null) {
			for (String str : diamondList) {
				// dataid
				String dataId = "";
				String groupId = "";
				if (str.indexOf(":") > -1) {
					dataId = str.substring(0, str.indexOf(":"));
				}
				if (str.lastIndexOf(":") > -1) {
					groupId = str.substring(str.indexOf(":") + 1, str.lastIndexOf(":"));
				}
				if (!StringUtils.isEmpty(dataId) && !StringUtils.isEmpty(groupId)) {
					DefaultDiamondManager manager = new DefaultDiamondManager(dataId, groupId, diamondListener, diamondIpList);
					configContent = manager.getAvailableConfigureInfomation(TIME_OUT);
					logger.debug("从diamond取到的数据是：" + configContent);
					setConfigMap();
				} else {
					logger.error("diamond数据配置properties异常: DataId:" + dataId + ",Group:" + groupId);
				}
			}
		} else {
			logger.error("diamond数据配置properties异常: diamondBeanList is null or diamondIpList is null");
		}
	}

	private static void setConfigMap() {
		if (org.apache.commons.lang.StringUtils.isNotEmpty(configContent)) {
			if (properties == null) {
				properties = new Properties();
			}
			try {
				properties.load(new StringReader(configContent));
				configMap = properties;
			} catch (IOException var2) {
				logger.error("diamond数据流转成properties异常" + var2.getMessage());
			}
		}

	}

	public String getDiamondIpList() {
		return diamondIpList;
	}

	public void setDiamondIpList(String diamondIpList) {
		this.diamondIpList = diamondIpList;
	}

	public String getConfigContent() {
		return configContent;
	}

	public void setConfigContent(String configContent) {
		this.configContent = configContent;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
