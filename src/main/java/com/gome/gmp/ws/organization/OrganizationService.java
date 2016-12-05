package com.gome.gmp.ws.organization;

import java.text.SimpleDateFormat;

import javax.annotation.Resource;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gome.framework.AppContext;
import com.gome.framework.Env;
import com.gome.gmp.business.GomeGmpWSHRBS;
import com.gome.gmp.common.constant.Constants;
import com.gome.gmp.ws.StartFileService;
import com.gome.gmp.ws.organization.pi.SIIHR055REQ;
import com.gome.gmp.ws.organization.pi.ZHRORGEHMD;
import com.gome.gmp.ws.organization.pi.ZHRORGEHMDResponse;
import com.gome.gmp.ws.organization.pi.ZHRORGEHMDResponse.ITOUT;

/**
 * 组织数据同步业务
 * 
 * @author wubin
 */
@Service("organizationService")
public class OrganizationService extends StartFileService {

	private static Logger logger = LoggerFactory.getLogger(OrganizationService.class);

	// @Resource(name = "organizationDao")
	// ImportToDB<ZHRORGEHMD2> importDao;
	@Resource(name = "gomeGmpWSHRBS")
	private GomeGmpWSHRBS gomeGmpWSHRBS;

	private String url;

	private String userName;

	private String password;

	private volatile boolean started;

	private final Object startLock = new Object();

	private SIIHR055REQ service;

	/**
	 * 同步数据
	 */
	@SuppressWarnings("unused")
	@Override
	@Transactional
	public void takeData() {
		// 检查配置，检查锁
		checkInit();
		// 获取开始结束时间
		String startDate = getStartDate();
		String endDate = getEndDate();
		// 接口参数组装
		ZHRORGEHMD zhrorgehmd = new ZHRORGEHMD();
		zhrorgehmd.setJYID("1");
		zhrorgehmd.setBEGDA(startDate);
		zhrorgehmd.setENDDA(endDate);
		com.gome.gmp.ws.organization.pi.ZHRORGEHMD.ITOUT zTOUT = new com.gome.gmp.ws.organization.pi.ZHRORGEHMD.ITOUT();
		zTOUT.getItem();
		zhrorgehmd.setITOUT(zTOUT);
		try {
			// 调用接口
			ZHRORGEHMDResponse resp = service.siIHR055REQ(zhrorgehmd);
			ITOUT tout = resp.getITOUT();
			// 导入库
			// int ret = importDao.importData(tout.getItem());
			int ret = 0;
			// ret = gomeGmpWSHRBS.importOrganizationData(tout.getItem());
			// 日志
			logger.info("batch import Organization,ret：" + ret + "," + new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").format(new java.util.Date()));
			// 设置下次开始时间为结束时间
			setEndDate(endDate);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 检查初始化配置
	 */
	private void checkInit() {
		if (!started) {
			synchronized (startLock) {
				initData();
			}
			started = true;
		}
	}

	/**
	 * 初始化配置
	 */
	void initData() {
		Env env = AppContext.getEnv();
		this.url = env.getProperty(Constants.WS_SAP_HR_ORG_URL);
		this.userName = env.getProperty(Constants.WS_SAP_HR_USERNAME);
		this.password = env.getProperty(Constants.WS_SAP_HR_PASSWORD);
		JaxWsProxyFactoryBean bean = new JaxWsProxyFactoryBean();
		bean.setServiceClass(SIIHR055REQ.class);
		bean.setAddress(url);
		bean.setUsername(this.userName);
		bean.setPassword(this.password);
		this.service = (SIIHR055REQ) bean.create();
	}

	/**
	 * 获取结束时间
	 */
	private String getEndDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
	}

	/**
	 * 获取目录名称
	 */
	@Override
	protected String getInterfaceType() {
		return "organization";
	}
}