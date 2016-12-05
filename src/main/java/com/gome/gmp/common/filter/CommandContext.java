package com.gome.gmp.common.filter;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 线程上下文
 * @author: wubin
 */
public class CommandContext {

	/**
	 * SERVLET_REQUEST
	 */
	private static final String HTTP_SERVLET_REQUEST = "HTTP_SERVLET_REQUEST";

	/**
	 * SERVLET_RESPONSE
	 */
	private static final String HTTP_SERVLET_RESPONSE = "HTTP_SERVLET_RESPONSE";

	/**
	 * @Description: 线程变量容器,生命周期：线程回收
	 * @author: wubin
	 * @date: 2012-8-16
	 */
	protected static class ThreadLocalMap extends ThreadLocal<Object> {

		@Override
		public Object initialValue() {
			return new HashMap<Object, Object>();
		}
	}

	/**
	 * 线程变量管理器
	 */
	private static ThreadLocalMap contextManager = new ThreadLocalMap();

	@SuppressWarnings("unchecked")
	protected static Map<Object, Object> getContext() {
		return (Map<Object, Object>) contextManager.get();
	}

	/**
	 * 清空所有线程变量
	 */
	public static void close() {
		getContext().clear();
	}

	/**
	 * 设置一个线程变量
	 * 
	 * @param key
	 * @param value
	 */
	protected static void put(Object key, Object value) {
		getContext().put(key, value);
	}

	/**
	 * 取得一个线程变量
	 * 
	 * @param key
	 * @return
	 */
	protected static Object get(Object key) {
		return getContext().get(key);
	}

	/**
	 * 移除一个线程变量
	 * 
	 * @param key
	 */
	public static void remove(Object key) {
		getContext().remove(key);
	}

	/**
	 * 取得一个string类型的线程变量，没有就返回默认值
	 * 
	 * @param key
	 * @param def
	 * @return
	 */
	protected static String getString(String key, String def) {
		Object oo = getContext().get(key);
		if (oo != null) {
			if (oo instanceof String) {
				return (String) oo;
			} else {
				return oo.toString();
			}
		}
		return def;
	}

	/**
	 * @Description: 设置request
	 * @author: wubin
	 * @param request
	 */
	public static void setRequest(HttpServletRequest request) {
		put(HTTP_SERVLET_REQUEST, request);
	}

	/**
	 * @Description: 获取request
	 * @author: wubin
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		HttpServletRequest request = (HttpServletRequest) get(HTTP_SERVLET_REQUEST);
		return request;
	}

	/**
	 * @Description: 设置response
	 * @author: wubin
	 * @param request
	 */
	public static void setResponse(HttpServletResponse response) {
		put(HTTP_SERVLET_RESPONSE, response);
	}

	/**
	 * @Description: 获取response
	 * @author: wubin
	 * @return
	 */
	public static HttpServletResponse getResponse() {
		HttpServletResponse response = (HttpServletResponse) get(HTTP_SERVLET_RESPONSE);
		return response;
	}
}