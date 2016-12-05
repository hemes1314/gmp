package com.gome.gmp.ws.person;

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
import com.gome.gmp.ws.person.pi.SIIHR053REQ;
import com.gome.gmp.ws.person.pi.ZHRPAMD;
import com.gome.gmp.ws.person.pi.ZHRPAMDResponse;
import com.gome.gmp.ws.person.pi.ZHRPAMDResponse.TOUT;

/**
 * 人员同步业务
 * 
 * @author wubin
 */
@Service("personService")
public class PersonService extends StartFileService {

	private static Logger logger = LoggerFactory.getLogger(PersonService.class);

	// @Resource(name = "personDao")
	// ImportToDB<ZHRPAMD2> importDao;
	@Resource(name = "gomeGmpWSHRBS")
	private GomeGmpWSHRBS gomeGmpWSHRBS;

	private String url;

	private String userName;

	private String password;

	private volatile boolean started;

	private final Object startLock = new Object();

	private SIIHR053REQ service;

	/**
	 * 同步数据
	 */
	@Override
	@Transactional
	public void takeData() {
		// 检查配置，检查锁
		checkInit();
		// 获取开始结束时间
		String startDate = getStartDate();
		String endDate = getEndDate();
		// 接口参数组装
		ZHRPAMD zhrpamd = new ZHRPAMD();
		zhrpamd.setJYID("1");
		zhrpamd.setBEGDA(startDate);
		zhrpamd.setENDDA(endDate);
		com.gome.gmp.ws.person.pi.ZHRPAMD.TOUT zTOUT = new com.gome.gmp.ws.person.pi.ZHRPAMD.TOUT();
		zTOUT.getItem();
		zhrpamd.setTOUT(zTOUT);
		try {
			// 调用接口
			ZHRPAMDResponse resp = service.siIHR053REQ(zhrpamd);
			TOUT tout = resp.getTOUT();
			// 导入库
			int ret = gomeGmpWSHRBS.importPersonData(tout.getItem());
			// int ret = importDao.importData(tout.getItem());
			// 日志
			logger.info("batch import person,accept size:" + tout.getItem().size() + ",ret：" + ret + "," + new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").format(new java.util.Date()));
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
		this.url = env.getProperty(Constants.WS_SAP_HR_PERSON_URL);
		this.userName = env.getProperty(Constants.WS_SAP_HR_USERNAME);
		this.password = env.getProperty(Constants.WS_SAP_HR_PASSWORD);
		JaxWsProxyFactoryBean bean = new JaxWsProxyFactoryBean();
		bean.setServiceClass(SIIHR053REQ.class);
		bean.setAddress(url);
		bean.setUsername(this.userName);
		bean.setPassword(this.password);
		this.service = (SIIHR053REQ) bean.create();
	}

	/**
	 * 获取结束时间
	 */
	private String getEndDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
	}

	/**
	 * 获取接口类型
	 */
	@Override
	protected String getInterfaceType() {
		return "person";
	}
}