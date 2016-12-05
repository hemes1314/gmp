package com.gome.gmp.model.bo;

import java.math.BigDecimal;
import java.util.Date;

import com.gome.framework.base.BaseBO;
import com.gome.gmp.common.FieldMeta;

/**
 * @author Administrator
 */
public class GomeGmpResTaskBO extends BaseBO {

	/**  */
	private static final long serialVersionUID = 1691316878993627356L;

	@FieldMeta(name = "项目id", isLog = false)
	private String proId;

	@FieldMeta(name = "任务名称", isLog = true)
	private String taskName;

	@FieldMeta(name = "工期", isLog = true)
	private Integer workPeriod;

	@FieldMeta(name = "开始时间", isLog = true)
	private Date startTime;

	@FieldMeta(name = "结束时间 ", isLog = true)
	private Date endTime;

	@FieldMeta(name = "前置任务号", isLog = true)
	private Integer preTaskNum;

	@FieldMeta(name = "进度", isLog = true)
	private Integer schedule;

	@FieldMeta(name = "BUG号", isLog = true)
	private String bugId;

	@FieldMeta(name = "计划工时", isLog = true)
	private BigDecimal planTime;

	@FieldMeta(name = "人员", isLog = true)
	private Integer userId;

	@FieldMeta(name = "项目职责 ", isLog = true)
	private String rebuke;

	@FieldMeta(name = "项目类型", isLog = false)
	private Integer proType;

	@FieldMeta(name = "任务号 ", isLog = false)
	private Integer taskNum;
	
	/**
	 * 任务人员类型
	 */
	private Integer taskUserType;

	public String getProId() {
		return this.proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	public String getTaskName() {
		return this.taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Integer getWorkPeriod() {
		return workPeriod;
	}

	public void setWorkPeriod(Integer workPeriod) {
		this.workPeriod = workPeriod;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getPreTaskNum() {
		return preTaskNum;
	}

	public void setPreTaskNum(Integer preTaskNum) {
		this.preTaskNum = preTaskNum;
	}

	public String getRebuke() {
		return rebuke;
	}

	public void setRebuke(String rebuke) {
		this.rebuke = rebuke;
	}

	public Integer getTaskNum() {
		return taskNum;
	}

	public void setTaskNum(Integer taskNum) {
		this.taskNum = taskNum;
	}

	public Integer getSchedule() {
		return this.schedule;
	}

	public void setSchedule(Integer schedule) {
		this.schedule = schedule;
	}

	public String getBugId() {
		return this.bugId;
	}

	public void setBugId(String bugId) {
		this.bugId = bugId;
	}

	public BigDecimal getPlanTime() {
		return planTime;
	}

	public void setPlanTime(BigDecimal planTime) {
		this.planTime = planTime;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getProType() {
		return this.proType;
	}

	public void setProType(Integer proType) {
		this.proType = proType;
	}

	public Integer getTaskUserType() {
		return taskUserType;
	}
	
	public void setTaskUserType(Integer taskUserType) {
		this.taskUserType = taskUserType;
	}
}