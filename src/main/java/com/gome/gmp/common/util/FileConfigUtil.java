package com.gome.gmp.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.locks.Lock;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 读取文件 Created by wangshuai-ds on 2015/12/21.
 */
public class FileConfigUtil {
	private static Logger logger = Logger.getLogger(FileConfigUtil.class);
	private final static Lock lock = new java.util.concurrent.locks.ReentrantLock();
	private String url;
	private String filePath;

	public FileConfigUtil() {
	}

	/**
	 * 构造函数
	 * 
	 * @param filePath
	 */
	public FileConfigUtil(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * 读取配置文件
	 */
	public void readPropertFile() {
		File file = new File(filePath);
		if (file.exists()) {
			lock.lock();
			String line = "";
			try {
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				while ((line = br.readLine()) != null) {
					if (!line.contains("#")) {
						url = line.toString().trim();
						break;
					}
				}
				br.close();
				fr.close();// 关闭输入流，释放连接
			} catch (Exception e) {
				logger.error("加载.diamond.domain 文件出错:" + e.getMessage());
			} finally {
				lock.unlock();
			}
		} else {
			logger.error("配置文件" + filePath + "不存在，请检查。");
		}
	}

	/**
	 * 获取值
	 * 
	 * @return
	 */
	public synchronized String getValue() {
		this.readPropertFile();
		if (StringUtils.isBlank(url)) {
			return "";
		}
		return getUrl();
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
