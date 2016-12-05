package com.gome.gmp.ws;

import java.util.Date;

import com.gome.gmp.common.PathUtil;
import com.gome.gmp.common.PropertiesUtil;
import com.gome.gmp.common.util.DateUtil;

/**
 * 数据同步抽象父类
 */
public abstract class StartFileService extends TakeDataService {

	/**
	 * 业务目录
	 * 
	 * @return
	 */
	protected abstract String getInterfaceType();

	/**
	 * 取开始时间
	 * 
	 * @return
	 */
	protected String getStartDate() {
		String startTimeKey = "sap.hr.ws." + getInterfaceType() + ".startTime";
		PropertiesUtil.setFile(PathUtil.getClassPath() + "conf/env/sap.properties");
		String startTime = PropertiesUtil.getValue(startTimeKey);
		if (startTime != null) {
			return startTime;
		} else {
			String startDate = DateUtil.getDate(DateUtil.addDays(new Date(), -1));
			PropertiesUtil.setValue(startTimeKey, startDate);
			return startDate;
		}
	}

	/**
	 * 设置下次开始时间
	 * 
	 * @param date
	 */
	protected void setEndDate(String date) {
		String startTimeKey = "sap.hr.ws." + getInterfaceType() + ".startTime";
		PropertiesUtil.setFile(PathUtil.getClassPath() + "conf/env/sap.properties");
		PropertiesUtil.setValue(startTimeKey, date);
	}
}
