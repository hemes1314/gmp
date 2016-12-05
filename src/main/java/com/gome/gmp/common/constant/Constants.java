package com.gome.gmp.common.constant;

/**
 * @Project: gome-gmp
 * @Title: Constants.java
 * @Package: com.gome.gmp.common.constant
 * @Description: 常量公共服务类
 * @author: wangchangtie
 * @since: 2016-5-24 上午10:56:07
 * @Copyright: 2016. All rights reserved.
 * @Version: v1.0
 */
public interface Constants {

	/**
	 * 项目管理头部菜单视图类型
	 */
	public static final String PROJECT_VIEW_ATTRSTR = "proViewType";

	/**
	 * 项目管理头部菜单视图类型——项目
	 */
	public static final Integer PROJECT_VIEW_PRO = 1;

	/**
	 * 项目管理头部菜单视图类型——需求
	 */
	public static final Integer PROJECT_VIEW_NEED = 2;

	/**
	 * 项目管理头部菜单视图类型——日报工时
	 */
	public static final Integer PROJECT_VIEW_DAILY = 3;

	/**
	 * 项目管理头部菜单视图类型——日历
	 */
	public static final Integer PROJECT_VIEW_CALENDAR = 4;

	/**
	 * 组织级别——1级
	 */
	public static final Integer ORGLEVEL_FIRST = 1;

	/**
	 * 组织级别——2级
	 */
	public static final Integer ORGLEVEL_SECOND = 2;

	/**
	 * 组织级别——3级
	 */
	public static final Integer ORGLEVEL_THIRD = 3;

	/**
	 * 组织级别——4级
	 */
	public static final Integer ORGLEVEL_FOURTH = 4;
	
	/**
	 * 日报类型——项目
	 */
	public static final Integer DAILYTYPE_PROJECT = 1;
	
	/**
	 * 日报类型——需求
	 */
	public static final Integer DAILYTYPE_AGILE = 2;
	
	/**
	 * 日报类型——公休
	 */
	public static final Integer DAILYTYPE_REST = 3;
	
	/**
	 * 日报类型——生产问题
	 */
	public static final Integer DAILYTYPE_PRDPROBLEM = 4;
	
	/**
	 * 日报类型——行政工作
	 */
	public static final Integer DAILYTYPE_EXECUTIVE = 5;
	
	/**
	 * 日报类型——技能
	 */
	public static final Integer DAILYTYPE_SKILL = 6;
	
	/**
	 * 日报类型——其他
	 */
	public static final Integer DAILYTYPE_OTHER = 7;

	/**
	 * excel 临时存放路径
	 */
	public static final String EXCEL_TEMP_PATH = "tempPath";

	public static final String SYSTEM_CONTEXT_PATH_KEY = null;

	public static final String USER_IN_SESSION = null;

	public static final String PROJECT_PATH = "/gmp";

	public static final String PRO_TYPE = "proType";

	/**
	 * 本地缓存类型 ———— 数据字典list
	 */
	public static final String EHCACHE_TYPE_DICTITEM_LIST = "dictItemList";

	/**
	 * 本地缓存类型 ———— 数据字典map
	 */
	public static final String EHCACHE_TYPE_DICTITEM_MAP = "dictItemMap";
	
	/**
	 * 本地缓存类型 ———— 组织级别
	 */
	public static final String EHCACHE_TYPE_ORGLEVEL = "orgLevel";
	
	/**
	 * 本地缓存Key ———— 组织级别
	 */
	public static final String EHCACHE_KEY_ORGLEVEL = "org_";
	
	/**
	 * 状态码：成功 (0901：GMP 000：通用)
	 */
	public static final String SUCCESS = "0901000001";

	/**
	 * 状态码：失败
	 */
	public static final String FAILD = "0901000000";

	/**
	 * 状态分布查询参数
	 */
	public static final String TJ_STATUS_FIND_PARAM = "TJ_STATUS_FIND_PARAM";

	/**
	 * 上传类型：项目
	 */
	public static final String UPLOAD_PRO_TYPE_PROJECT = "1";

	/**
	 * 上传类型：敏捷
	 */
	public static final String UPLOAD_PRO_TYPE_AGILE = "2";

	/**
	 * 上传类型：需求
	 */
	public static final String UPLOAD_PRO_TYPE_DEMAND = "3";

	/**
	 * 上传文件路径配置KEY：项目
	 */
	public static final String UPLOAD_PATH_KEY_PROJECT = "gomeGmp.fileUpload.projectPath";

	/**
	 * 上传文件路径配置KEY：敏捷
	 */
	public static final String UPLOAD_PATH_KEY_AGILE = "gomeGmp.fileUpload.agilePath";

	/**
	 * 上传文件路径配置KEY：需求
	 */
	public static final String UPLOAD_PATH_KEY_DEMAND = "gomeGmp.fileUpload.demandPath";

	/**
	 * 上传文件路径配置KEY：其他
	 */
	public static final String UPLOAD_PATH_KEY_OTHERS = "gomeGmp.fileUpload.othersPath";

	/**
	 * 组织同步接口URL
	 */
	public static final String WS_SAP_HR_ORG_URL = "gomeGmp.sapHrWs.orgUrl";

	/**
	 * 人员同步接口URL
	 */
	public static final String WS_SAP_HR_PERSON_URL = "gomeGmp.sapHrWs.personUrl";

	/**
	 * HR同步接口用户名
	 */
	public static final String WS_SAP_HR_USERNAME = "gomeGmp.sapHrWs.username";

	/**
	 * HR同步接口密码
	 */
	public static final String WS_SAP_HR_PASSWORD = "gomeGmp.sapHrWs.password";
}
