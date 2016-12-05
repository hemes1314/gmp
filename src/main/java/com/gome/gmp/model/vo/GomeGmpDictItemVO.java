package com.gome.gmp.model.vo;

import com.gome.gmp.model.bo.GomeGmpDictItemBO;


/**
 * 
 *
 * @author wubin
 */
public class GomeGmpDictItemVO extends GomeGmpDictItemBO {

	private static final long serialVersionUID = 4300050993132565020L;
	
	// 开始日期
	private String startDate;
	
	// 结束日期
	private String endDate;
	
	// 是否是节假日(1:是，0：否)
	private String isHoliday = "1";

	
	public String getStartDate() {
		return startDate;
	}

	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	
	public String getEndDate() {
		return endDate;
	}

	
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	
	public String getIsHoliday() {
		return isHoliday;
	}

	
	public void setIsHoliday(String isHoliday) {
		this.isHoliday = isHoliday;
	}
	
	
}
