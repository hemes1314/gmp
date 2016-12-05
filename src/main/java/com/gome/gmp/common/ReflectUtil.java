package com.gome.gmp.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gome.framework.logging.Logger;
import com.gome.framework.logging.LoggerFactory;
import com.gome.gmp.common.util.DateUtil;

/**
 * 反射工具类-变更记录相关
 *
 * @author wubin
 */
public class ReflectUtil {

	protected static Logger LOG = LoggerFactory.getLogger(ReflectUtil.class);

	/**
	 * 对比两个对象不同属性值
	 * 
	 * 
	 * @param obj1
	 * @param Obj2
	 * @return
	 * @throws Exception
	 * @author wubin
	 */
	public static <T> List<String> compare(T obj1, T obj2) throws Exception {
		List<String> result = new ArrayList<String>();
		Field[] fs = obj1.getClass().getDeclaredFields();
		result.addAll(diffColNames(fs, obj1, obj2));
		Field[] superFs = obj1.getClass().getSuperclass().getDeclaredFields();
		result.addAll(diffColNames(superFs, obj1, obj2));
		return result;
	}

	/**
	 * 对比两个对象值不同的列
	 * 
	 * @param fs
	 * @param obj1
	 * @param obj2
	 * @return
	 * @throws Exception
	 */
	public static <T> List<String> diffColNames(Field[] fs, T obj1, T obj2) throws Exception {
		List<String> result = new ArrayList<String>();
		for (Field f : fs) {
			// 获取是否需要记录日志
			boolean isLog = ReflectUtil.getColIsLog(obj2, f.getName());
			if (!isLog) {
				continue;
			}
			f.setAccessible(true);
			Object v1 = f.get(obj1);
			Object v2 = f.get(obj2);
			String objectType = f.getGenericType().toString();
			if ((v1 != null && v1.getClass().isArray()) || (v2 != null && v2.getClass().isArray()) || objectType.indexOf("java.util.List") > -1 || (new Integer(0).equals(v1) && v2 == null)) {
				continue;
			}
			if (!equals(v1, v2)) {
				result.add(f.getName());
			}
		}
		return result;
	}

	/**
	 * 对象对比
	 * 
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	public static boolean equals(Object obj1, Object obj2) {
		if (obj1 == obj2) {
			return true;
		}
		if (obj1 == null || obj2 == null) {
			return false;
		}
		return obj1.equals(obj2);
	}

	/**
	 * 根据属性名获取属性值
	 * 
	 * @param fieldName
	 * @param o
	 * @return
	 */
	public static String getFieldValueByName(String fieldName, Object o) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = o.getClass().getMethod(getter, new Class[] {});
			Object valObj = method.invoke(o, new Object[] {});
			String value = String.valueOf(valObj);
			if (valObj instanceof Date) {
				value = DateUtil.getDate((Date) valObj);
			}
			return value;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 获取中文名 获取字段中需包含fieldMeta的注解
	 * 
	 * @param o
	 * @param col
	 * @return
	 */
	public static String getColCName(Object o, String col) {
		FieldMeta meta = getFieldMeta(o, col);
		if (meta == null) {
			return col;
		}
		String colCName = meta.name();
		if (colCName == null) {
			return col;
		}
		return colCName;
	}

	/**
	 * 获取是否需要记录日志 获取字段中需包含fieldMeta的注解
	 * 
	 * @param o
	 * @param col
	 * @return
	 */
	public static boolean getColIsLog(Object o, String col) {
		FieldMeta meta = getFieldMeta(o, col);
		if (meta == null) {
			return false;
		}
		return meta.isLog();
	}

	/**
	 * 获取自定义注解
	 * 
	 * @param o
	 * @param col
	 * @return
	 */
	private static FieldMeta getFieldMeta(Object o, String col) {
		Field f = null;
		try {
			f = o.getClass().getDeclaredField(col);
		} catch (NoSuchFieldException e) {
			try {
				f = o.getClass().getSuperclass().getDeclaredField(col);
			} catch (NoSuchFieldException | SecurityException e1) {
				LOG.error(e1);
				return null;
			}
		} catch (SecurityException e1) {
			LOG.error(e1);
			return null;
		}
		return f.getAnnotation(FieldMeta.class);
	}
}
