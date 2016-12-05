package com.gome.gmp.model.bo;

import java.util.Date;

import com.gome.framework.base.BaseBO;
import com.gome.gmp.common.FieldMeta;

/**
 * @author Administrator
 */
public class GomeGmpResProjectBO extends BaseBO {

	/**  */
	private static final long serialVersionUID = -6706985667576898858L;

	@FieldMeta(name = "主键")
	private String proId;

	@FieldMeta(name = "bud号")
	private String bugId;

	@FieldMeta(name = "标题")
	private String title;

	@FieldMeta(name = "相关需求")
	private String demandId;

	@FieldMeta(name = "项目经理")
	private Integer bpId;

	@FieldMeta(name = "业务部门")
	private String unitBsId;

	@FieldMeta(name = "关键用户")
	private Integer keyUserId;

	@FieldMeta(name = "svn地址")
	private String svnAddr;

	@FieldMeta(name = "状态")
	private Integer statusId;

	@FieldMeta(name = "优先级")
	private Integer priorityId;

	@FieldMeta(name = "进度")
	private Integer scheduleId;

	@FieldMeta(name = "实施阶段")
	private Integer actualize;

	@FieldMeta(name = "简述")
	private String remark;

	@FieldMeta(name = "近期完成工作")
	private String nearWorkDone;

	@FieldMeta(name = "近期计划工作")
	private String nearWorkPlan;

	@FieldMeta(name = "风险问题描述")
	private String riskRemark;

	@FieldMeta(name = "类型")
	private Integer proType;

	@FieldMeta(name = "是否提交")
	private Integer isCommit;

	@FieldMeta(name = "开始时间")
	private Date startTime;

	@FieldMeta(name = "计划上线时间")
	private Date planTime;

	@FieldMeta(name = "完成时间")
	private Date finishTime;

	@FieldMeta(name = "项目工期")
	private Integer proPeriod;

	/** 更新人 */
	private Integer updateUser;

	/** 创建人 */
	private Integer createUser;

	public String getProId() {
		return this.proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDemandId() {
		return this.demandId;
	}

	public void setDemandId(String demandId) {
		this.demandId = demandId;
	}

	public Integer getBpId() {
		return this.bpId;
	}

	public void setBpId(Integer bpId) {
		this.bpId = bpId;
	}

	public String getUnitBsId() {
		return unitBsId;
	}

	public void setUnitBsId(String unitBsId) {
		this.unitBsId = unitBsId;
	}

	public Integer getKeyUserId() {
		return this.keyUserId;
	}

	public void setKeyUserId(Integer keyUserId) {
		this.keyUserId = keyUserId;
	}

	public String getSvnAddr() {
		return this.svnAddr;
	}

	public void setSvnAddr(String svnAddr) {
		this.svnAddr = svnAddr;
	}

	public Integer getStatusId() {
		return this.statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getPriorityId() {
		return this.priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public Integer getScheduleId() {
		return this.scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Integer getActualize() {
		return this.actualize;
	}

	public void setActualize(Integer actualize) {
		this.actualize = actualize;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getNearWorkDone() {
		return this.nearWorkDone;
	}

	public void setNearWorkDone(String nearWorkDone) {
		this.nearWorkDone = nearWorkDone;
	}

	public String getNearWorkPlan() {
		return this.nearWorkPlan;
	}

	public void setNearWorkPlan(String nearWorkPlan) {
		this.nearWorkPlan = nearWorkPlan;
	}

	public String getRiskRemark() {
		return this.riskRemark;
	}

	public void setRiskRemark(String riskRemark) {
		this.riskRemark = riskRemark;
	}

	public Integer getProType() {
		return this.proType;
	}

	public void setProType(Integer proType) {
		this.proType = proType;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getPlanTime() {
		return this.planTime;
	}

	public void setPlanTime(Date planTime) {
		this.planTime = planTime;
	}

	public Date getFinishTime() {
		return this.finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public Integer getProPeriod() {
		return this.proPeriod;
	}

	public void setProPeriod(Integer proPeriod) {
		this.proPeriod = proPeriod;
	}

	public Integer getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(Integer updateUser) {
		this.updateUser = updateUser;
	}

	public Integer getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}

	public String getBugId() {
		return bugId;
	}

	public void setBugId(String bugId) {
		this.bugId = bugId;
	}

	public Integer getIsCommit() {
		return isCommit;
	}

	public void setIsCommit(Integer isCommit) {
		this.isCommit = isCommit;
	}
}