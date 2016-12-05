package com.gome.gmp.model.vo;

import com.gome.gmp.common.FieldMeta;
import com.gome.gmp.model.bo.GomeGmpResTaskBO;

/**
 * @author Administrator
 */
public class GomeGmpResTaskVO extends GomeGmpResTaskBO {

	/**  */
	private static final long serialVersionUID = 904779120937821411L;

	@FieldMeta(name = "人员名称", isLog = false)
	private String userName;

	@FieldMeta(name = "统计的开始时间", isLog = false)
	private String strStartDate;

	@FieldMeta(name = "统计的结束时间", isLog = false)
	private String strEndDate;

	@FieldMeta(name = "统计的平均计划时间", isLog = false)
	private double avgPlanTime;

	@FieldMeta(name = "前置任务id", isLog = false)
	private Integer preTaskId;

	@FieldMeta(name = "前置任务名称", isLog = false)
	private String preTaskName;

	public String getStrStartDate() {
		return strStartDate;
	}

	public double getAvgPlanTime() {
		return avgPlanTime;
	}

	public void setAvgPlanTime(double avgPlanTime) {
		this.avgPlanTime = avgPlanTime;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPreTaskName() {
		return preTaskName;
	}

	public void setPreTaskName(String preTaskName) {
		this.preTaskName = preTaskName;
	}

	public Integer getPreTaskId() {
		return preTaskId;
	}

	public void setPreTaskId(Integer preTaskId) {
		this.preTaskId = preTaskId;
	}

}