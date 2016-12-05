package com.gome.gmp.model.bo;

import java.util.Date;

import com.gome.gmp.common.base.BaseObject;

/**
 * @author Administrator
 */
@SuppressWarnings("serial")
public class GomeGmpResDailyBO extends BaseObject {

	/** 主键Id */
	private Long id;

	/** 需求id */
	private Integer needId;

	/** 类型 */
	private Integer type;

	/** 开始时间 */
	private Date startTime;

	/** 结束时间 */
	private Date endTime;

	/** 工作内容 */
	private String workContent;

	/** 工时 */
	private Double hours;

	/** 关联项目id */
	private String proId;

	/** 关联项目任务id */
	private Integer proTaskId;

	/** 关联项目名称 */
	private String proTaskName;

	/** 关联项目进度 */
	private Integer proTaskSchedule;

	/** 创建人 */
	private Integer createUser;

	/** 创建时间 */
	private Date createTime;

	/** 操作时间 */
	private Date operateTime;

	public Integer getNeedId() {
		return needId;
	}

	public void setNeedId(Integer needId) {
		this.needId = needId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setProId(String proId) {
		this.proId = proId;
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

	public String getWorkContent() {
		return this.workContent;
	}

	public void setWorkContent(String workContent) {
		this.workContent = workContent;
	}

	public Double getHours() {
		return this.hours;
	}

	public void setHours(Double hours) {
		this.hours = hours;
	}

	public String getProId() {
		return proId;
	}

	public Integer getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public String getProTaskName() {
		return proTaskName;
	}

	public void setProTaskName(String proTaskName) {
		this.proTaskName = proTaskName;
	}

	public Integer getProTaskId() {
		return proTaskId;
	}

	public void setProTaskId(Integer proTaskId) {
		this.proTaskId = proTaskId;
	}

	public Integer getProTaskSchedule() {
		return proTaskSchedule;
	}

	public void setProTaskSchedule(Integer proTaskSchedule) {
		this.proTaskSchedule = proTaskSchedule;
	}
}