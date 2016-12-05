package com.gome.gmp.common;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.gome.framework.AppContext;
import com.gome.framework.Env;
import com.gome.framework.logging.Logger;
import com.gome.framework.logging.LoggerFactory;
import com.gome.gmp.common.constant.Constants;
import com.gome.gmp.common.mybaits.plugins.page.PageModel;
import com.gome.gmp.model.bo.GomeGmpResOrgBO;
import com.gome.gmp.model.bo.GomeGmpResUserBO;
import com.gome.gmp.model.vo.GomeGmpResDatasynVO;
import com.gome.gmp.model.vo.GomeGmpResProjectVO;

/**
 * 
 *
 * @author wubin
 */
public class BusinessUtil {

	protected static Logger LOG = LoggerFactory.getLogger(BusinessUtil.class);

	/**
	 * 上传文件名前缀：项目
	 */
	private static final String UPLOAD_FILE_PREFIX_PROJECT = "pro_";

	/**
	 * 上传文件名前缀：敏捷
	 */
	private static final String UPLOAD_FILE_PREFIX_AGILE = "agile_";

	/**
	 * 上传文件名前缀：需求
	 */
	private static final String UPLOAD_FILE_PREFIX_DEMAND = "demand_";

	/**
	 * 设置页码
	 * 
	 * @param pageNum
	 */
	public static void setPageNum(Integer pageNum) {
		PageHelper.startPage(pageNum, PageModel.PAGE_SIZE);
	}

	/**
	 * 从Cookie中获取用户信息
	 * 
	 * @param request
	 * @return GomeGmpResUserBO
	 */
	public static GomeGmpResUserBO getLoginUser(HttpServletRequest request) {
		GomeGmpResUserBO userInfo = new GomeGmpResUserBO();
		try {
			String us = "";
			Cookie[] cookies = request.getCookies();
			if (cookies != null && cookies.length > 0) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equalsIgnoreCase("user")) { // 获取键
						try {
							us = URLDecoder.decode(cookie.getValue(), "UTF-8");
						} catch (Exception e) {
							LOG.error("getLoginUser 获取用户信息失败,编码转换异常 " + e.getMessage(), e);
						}
					}
				}
				if (StringUtils.isNotBlank(us)) {
					userInfo = JSON.parseObject(us, GomeGmpResUserBO.class);
				}
			}
		} catch (Exception e) {
			LOG.error("getLoginUser 获取用户信息失败：" + e.getMessage(), e);
		}
		return userInfo;
	}

	/**
	 * 获取用户ID
	 * 
	 * @param request
	 * @return
	 */
	public static Long getLoginUserId(HttpServletRequest request) {
		return getLoginUser(request).getId();
	}

	/**
	 * 上传文件
	 * 
	 * @author wubin
	 */
	public static List<GomeGmpResDatasynVO> uploadFiles(CommonsMultipartFile[] files, HttpServletRequest request) {
		return uploadFiles(null, files, request);
	}

	public static List<GomeGmpResDatasynVO> uploadFiles(String proType, CommonsMultipartFile[] files, HttpServletRequest request) {
		List<GomeGmpResDatasynVO> uploadFileInfo = new ArrayList<GomeGmpResDatasynVO>();
		if (files == null) {
			return uploadFileInfo;
		}
		for (MultipartFile file : files) {
			// 单文件上传开始
			// 取得上传文件
			if (!file.isEmpty()) {
				// 取得当前上传文件的文件名称
				String fileName = file.getOriginalFilename();
				// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
				if (StringUtils.isNotBlank(fileName)) {
					// 记录上传过程起始时的时间，用来计算上传时间
					int pre = (int) System.currentTimeMillis();
					if (LOG.isDebugEnabled()) {
						LOG.debug("上传文件名称：" + file.getOriginalFilename());
						LOG.debug("上传文件大小：" + file.getSize() / (1024.0 * 1024) + "M");
					}
					// 重命名上传后的文件名
					String extension = ""; // 获取扩展名
					if (fileName.indexOf(".") > -1) {
						extension = fileName.substring(fileName.lastIndexOf("."));
					}
					String newfileName = UUID.randomUUID().toString() + extension;
					// 定义上传路径
					// String path =
					// request.getSession().getServletContext().getRealPath("upload");
					Env env = AppContext.getEnv();
					String path = null;
					if (Constants.UPLOAD_PRO_TYPE_PROJECT.equals(proType)) {
						path = env.getProperty(Constants.UPLOAD_PATH_KEY_PROJECT);
						newfileName = UPLOAD_FILE_PREFIX_PROJECT + newfileName;
					} else if (Constants.UPLOAD_PRO_TYPE_AGILE.equals(proType)) {
						path = env.getProperty(Constants.UPLOAD_PATH_KEY_AGILE);
						newfileName = UPLOAD_FILE_PREFIX_AGILE + newfileName;
					} else if (Constants.UPLOAD_PRO_TYPE_DEMAND.equals(proType)) {
						path = env.getProperty(Constants.UPLOAD_PATH_KEY_DEMAND);
						newfileName = UPLOAD_FILE_PREFIX_DEMAND + newfileName;
					} else {
						path = env.getProperty(Constants.UPLOAD_PATH_KEY_OTHERS);
					}
					path = path.replaceAll("//", "/");
					// 不存在则创建文件夹
					File targetFile = new File(path, newfileName);
					if (!targetFile.exists()) {
						targetFile.mkdirs();
					}
					try {
						// 方法设置此抽象路径名的存取权限
						targetFile.setReadable(true);
						// 设置所有者对于此抽象路径名执行权限
						targetFile.setExecutable(true);
						// 秒传ms
						file.transferTo(targetFile);
					} catch (Exception e) {
						LOG.error(e.getMessage(), e);
					}
					// 存储文件信息
					GomeGmpResDatasynVO dataSyn = new GomeGmpResDatasynVO();
					dataSyn.setFileName(fileName);
					dataSyn.setFilePath(newfileName);
					dataSyn.setPhysicalPath(path + newfileName);
					uploadFileInfo.add(dataSyn);
					// 记录上传该文件后的时间
					int finaltime = (int) System.currentTimeMillis();
					LOG.info("文件：\"/gmp/" + newfileName + "\"上传成功，耗时：" + (finaltime - pre) + "ms");
				}
			}
			// 单文件上传结束
		}
		return uploadFileInfo;
	}

	/**
	 * 去掉重复条件：org
	 * 
	 * @param gomeGmpResProjectVO
	 * @author wubin
	 */
	public static void repeatOrg(GomeGmpResProjectVO gomeGmpResProjectVO) {
		repeatOrg(gomeGmpResProjectVO, true);
	}

	/**
	 * 去掉重复条件：org
	 * 
	 * @param gomeGmpResProjectVO
	 * @author wubin
	 */
	public static void repeatOrg(GomeGmpResProjectVO gomeGmpResProjectVO, boolean containCenter) {
		// 如果全选则不查询此条件
		if ((isCheckAll(gomeGmpResProjectVO.getOrgIds()) && gomeGmpResProjectVO.getChildOrgIds() == null && gomeGmpResProjectVO.getGroupIds() == null) || isCheckAll(gomeGmpResProjectVO.getOrgIds()) && isCheckAll(gomeGmpResProjectVO.getChildOrgIds()) && gomeGmpResProjectVO.getGroupIds() == null || isCheckAll(gomeGmpResProjectVO.getOrgIds()) && isCheckAll(gomeGmpResProjectVO.getChildOrgIds()) && isCheckAll(gomeGmpResProjectVO.getGroupIds())) {
			if (containCenter) {
				gomeGmpResProjectVO.setOrgIds(null);
			} else {
				List<String> orgIdList = new ArrayList<String>();
				Collections.addAll(orgIdList, gomeGmpResProjectVO.getOrgIds());
				for (String id : orgIdList) {
					if ("-1".equals(id)) {
						orgIdList.remove(id);
						break;
					}
				}
				gomeGmpResProjectVO.setOrgIds(orgIdList.toArray(new String[orgIdList.size()]));
			}
			gomeGmpResProjectVO.setChildOrgIds(null);
			gomeGmpResProjectVO.setGroupIds(null);
		}
		// 去掉重复查询条件
		if (gomeGmpResProjectVO.getGroupIds() != null && gomeGmpResProjectVO.getGroupIds().length != 0 && !"".equals(gomeGmpResProjectVO.getGroupIds()[0]) && !isCheckAll(gomeGmpResProjectVO.getGroupIds())) {
			gomeGmpResProjectVO.setOrgIds(gomeGmpResProjectVO.getGroupIds());
		} else {
			if (gomeGmpResProjectVO.getChildOrgIds() != null && gomeGmpResProjectVO.getChildOrgIds().length != 0 && !"".equals(gomeGmpResProjectVO.getChildOrgIds()[0]) && !isCheckAll(gomeGmpResProjectVO.getChildOrgIds())) {
				gomeGmpResProjectVO.setOrgIds(gomeGmpResProjectVO.getChildOrgIds());
			}
		}
		// 全不选时置空
		if (gomeGmpResProjectVO.getOrgIds() != null && gomeGmpResProjectVO.getOrgIds().length == 1 && StringUtils.isBlank(gomeGmpResProjectVO.getOrgIds()[0])) {
			gomeGmpResProjectVO.setOrgIds(null);
		}
	}

	/**
	 * 去掉重复条件：org
	 * 
	 * @param gomeGmpResProjectVO
	 * @author wubin
	 */
	public static void repeatOrg(GomeGmpResProjectVO gomeGmpResProjectVO, List<GomeGmpResOrgBO> orgList) {
		if (gomeGmpResProjectVO.getOrgIds() != null && orgList != null && gomeGmpResProjectVO.getOrgIds().length == orgList.size() && (gomeGmpResProjectVO.getChildOrgIds() == null || gomeGmpResProjectVO.getChildOrgIds().length == 0) && (gomeGmpResProjectVO.getGroupIds() == null || gomeGmpResProjectVO.getGroupIds().length == 0)) {
			gomeGmpResProjectVO.setOrgIds(null);
		} else {
			// 去掉重复查询条件：org
			BusinessUtil.repeatOrg(gomeGmpResProjectVO);
		}
	}

	private static boolean isCheckAll(String[] arr) {
		if (arr == null) {
			return false;
		}
		for (String orgId : arr) {
			if ("-1".equals(orgId)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断字符串的字符集
	 * 
	 * @param str
	 * @return
	 * @author wubin
	 */
	public static String getEncoding(String str) {
		String encode = "GB2312";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s = encode;
				return s;
			}
		} catch (Exception exception) {
		}
		encode = "ISO-8859-1";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s1 = encode;
				return s1;
			}
		} catch (Exception exception1) {
		}
		encode = "UTF-8";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s2 = encode;
				return s2;
			}
		} catch (Exception exception2) {
		}
		encode = "GBK";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s3 = encode;
				return s3;
			}
		} catch (Exception exception3) {
		}
		return "";
	}

	/**
	 * 获取系统访问的相对路径，如:/WTAS
	 *
	 * @return
	 */
	public static String getContextPath() {
		return System.getProperty(Constants.SYSTEM_CONTEXT_PATH_KEY);
	}

	/**
	 * 修改一个bean(源)中的属性值，该属性值从目标bean获取
	 *
	 * @param dest
	 *            目标bean，其属性将被复制到源bean中
	 * @param src
	 *            需要被修改属性的源bean
	 * @param filtNullProps
	 *            源bean的null属性是否覆盖目标的属性 <li>true : 源bean中只有为null的属性才会被覆盖 <li>
	 *            false : 不管源bean的属性是否为null，均覆盖
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void copyBean(Object dest, Object src, boolean filtNullProps) throws IllegalArgumentException, IllegalAccessException {
		if (dest.getClass() == src.getClass()) {
			// 目标bean的所有字段
			Field[] destField = dest.getClass().getDeclaredFields();
			// 源bean的所有字段
			Field[] srcField = src.getClass().getDeclaredFields();
			for (int i = 0; i < destField.length; i++) {
				String destFieldName = destField[i].getName();
				String destFieldType = destField[i].getGenericType().toString();
				for (int n = 0; n < srcField.length; n++) {
					String srcFieldName = srcField[n].getName();
					String srcFieldType = srcField[n].getGenericType().toString();
					// String srcTypeName =
					// srcField[n].getType().getSimpleName();
					if (destFieldName.equals(srcFieldName) && destFieldType.equals(srcFieldType)) {
						destField[i].setAccessible(true);
						srcField[n].setAccessible(true);
						Object srcValue = srcField[n].get(src);
						Object destValue = destField[i].get(dest);
						if (filtNullProps) {
							// 源bean中的属性已经非空，则不覆盖
							if (srcValue == null) {
								srcField[n].set(src, destValue);
							}
						} else {
							srcField[n].set(dest, srcValue);
						}
					}
				}
			}
		}
	}

	/**
	 * 根据字段的值获取该字段
	 *
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static Field getFieldByFieldName(Object obj, String fieldName) {
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
			}
		}
		return null;
	}

	/**
	 * 获取对象某一字段的值
	 *
	 * @param obj
	 * @param fieldName
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Object getValueByFieldName(Object obj, String fieldName) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field field = getFieldByFieldName(obj, fieldName);
		Object value = null;
		if (field != null) {
			if (field.isAccessible()) {
				value = field.get(obj);
			} else {
				field.setAccessible(true);
				value = field.get(obj);
				field.setAccessible(false);
			}
		}
		return value;
	}

	/**
	 * 向对象的某一字段上设置值
	 *
	 * @param obj
	 * @param fieldName
	 * @param value
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void setValueByFieldName(Object obj, String fieldName, Object value) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field field = obj.getClass().getDeclaredField(fieldName);
		if (field.isAccessible()) {
			field.set(obj, value);
		} else {
			field.setAccessible(true);
			field.set(obj, value);
			field.setAccessible(false);
		}
	}
}
