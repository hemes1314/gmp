package com.gome.gmp.model.vo;

import java.math.BigDecimal;
import java.util.List;

import com.gome.gmp.model.bo.GomeGmpDictItemBO;
import com.gome.gmp.model.bo.GomeGmpResDailyBO;
import com.gome.gmp.model.bo.GomeGmpResProjectBO;

/**
 * @author Administrator
 */
public class GomeGmpResDailyVO extends GomeGmpResDailyBO {

	/**  */
	private static final long serialVersionUID = 8280617539666484542L;

	private String hourDate;

	private BigDecimal totalHour;

	private String roleName;

	private String createUserName;

	// 统计的开始时间
	private String strStartDate;

	// 统计的结束时间
	private String strEndDate;

	private String actionDate;

	// 项目名
	private String proTitle;
	
	// 日报类型名称
	private String dailyTypeName;

	private List<GomeGmpDictItemBO> dailyTypeList;

	private List<GomeGmpResProjectBO> relateProjectList;

	public String getProTitle() {
		return proTitle;
	}

	public void setProTitle(String proTitle) {
		this.proTitle = proTitle;
	}

	public String getDailyTypeName() {
		return dailyTypeName;
	}

	public void setDailyTypeName(String dailyTypeName) {
		this.dailyTypeName = dailyTypeName;
	}

	private Integer rowspanNum;

	public String getStrStartDate() {
		return strStartDate;
	}

	public void setStrStartDate(String strStartDate) {
		this.strStartDate = strStartDate;
	}

	public String getStrEndDate() {
		return strEndDate;
	}

	public void setStrEndDate(String strEndDate) {
		this.strEndDate = strEndDate;
	}

	public String getActionDate() {
		return actionDate;
	}

	public void setActionDate(String actionDate) {
		this.actionDate = actionDate;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getHourDate() {
		return hourDate;
	}

	public void setHourDate(String hourDate) {
		this.hourDate = hourDate;
	}

	public BigDecimal getTotalHour() {
		return totalHour;
	}

	public void setTotalHour(BigDecimal totalHour) {
		this.totalHour = totalHour;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public Integer getRowspanNum() {
		return rowspanNum;
	}

	public void setRowspanNum(Integer rowspanNum) {
		this.rowspanNum = rowspanNum;
	}

	public List<GomeGmpDictItemBO> getDailyTypeList() {
		return dailyTypeList;
	}

	public void setDailyTypeList(List<GomeGmpDictItemBO> dailyTypeList) {
		this.dailyTypeList = dailyTypeList;
	}

	public List<GomeGmpResProjectBO> getRelateProjectList() {
		return relateProjectList;
	}

	public void setRelateProjectList(List<GomeGmpResProjectBO> relateProjectList) {
		this.relateProjectList = relateProjectList;
	}
}