package com.gome.gmp.common;

import java.net.URISyntaxException;

import com.gome.framework.logging.Logger;
import com.gome.framework.logging.LoggerFactory;

/**
 * 路径相关公共函数
 * 
 * @author WuBin
 */
public class PathUtil {

	private static Logger logger = LoggerFactory.getLogger(PathUtil.class);

	/**
	 * WEB-INF name
	 */
	private static String WEB_INF_NAME = "WEB-INF";

	/**
	 * classes absolute path
	 */
	private static String CLASS_PATH = null;

	/**
	 * WebRoot absolute path
	 */
	private static String ROOT_PATH = null;

	/**
	 * WebRoot name
	 */
	private static String ROOT_NAME = null;

	/**
	 * WEB_INF absolute path
	 */
	private static String WEB_INF_PATH = null;

	/**
	 * Project Name
	 */
	private static String PRO_NAME = null;

	private PathUtil() {
	}

	static {
		init();
	}

	/**
	 * init
	 * 
	 * @throws URISyntaxException
	 */
	private static void init() {
		// 获取classpath
		// CLASS_PATH = PathUtil.class.getResource("/").getPath();
		try {
			CLASS_PATH = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		int subIndex = CLASS_PATH.indexOf(WEB_INF_NAME);
		// 获取WEB-INF路径
		if (subIndex != -1) {
			WEB_INF_PATH = CLASS_PATH.substring(0, subIndex).toString().concat(WEB_INF_NAME);
			sb.append(CLASS_PATH.substring(0, WEB_INF_PATH.lastIndexOf("/")));
			WEB_INF_PATH = WEB_INF_PATH.concat("/");
		} else {
			subIndex = CLASS_PATH.substring(0, CLASS_PATH.length() - 1).lastIndexOf("/");
			sb.append(CLASS_PATH.substring(0, subIndex));
		}
		// 获取WebRoot路径
		ROOT_PATH = sb.toString().concat("/");
		// 获取WebRoot名称
		ROOT_NAME = sb.substring(sb.lastIndexOf("/") + 1, sb.length());
		// 获取工程名
		if (ROOT_NAME.toLowerCase().indexOf("webroot") > -1 || ROOT_NAME.toLowerCase().indexOf("webapp") > -1) {
			String proPath = ROOT_PATH.substring(0, ROOT_PATH.lastIndexOf(ROOT_NAME) - 1);
			PRO_NAME = proPath.substring(proPath.lastIndexOf("/") + 1, proPath.length());
		} else {
			PRO_NAME = ROOT_NAME;
		}
	}

	/**
	 * 获取classpath
	 * 
	 * @return
	 * @throws URISyntaxException
	 * @see [类、类#方法、类#成员]
	 */
	public static String getClassPath() {
		try {
			CLASS_PATH = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath();
		} catch (URISyntaxException e) {
			logger.error(e.getMessage(), e);
		}
		return CLASS_PATH;
	}

	/**
	 * 获取classpath
	 * 
	 * @param proName:项目名称
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String getClassPath(String proName) {
		init();
		return CLASS_PATH.replace(PRO_NAME, proName);
	}

	/**
	 * 获取某类的包路径
	 * 
	 * @param T
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String getClassPath(Class<?> T) {
		String className = T.getName();
		StringBuffer sb = new StringBuffer();
		sb.append(CLASS_PATH).append(className.substring(0, className.lastIndexOf(".")).replace(".", "/")).append("/");
		return sb.toString();
	}

	/**
	 * 获取WebRoot路径
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String getRootPath() {
		return ROOT_PATH;
	}

	/**
	 * 获取WebRoot名称
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String getRootName() {
		return ROOT_NAME;
	}

	/**
	 * 获取WEB-INF路径
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String getWebInfPath() {
		return WEB_INF_PATH;
	}

	/**
	 * 测试
	 * 
	 * @throws URISyntaxException
	 */
	public static void main(String[] args) throws URISyntaxException {
	}
}