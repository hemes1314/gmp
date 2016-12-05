package com.gome.gmp.model.bo;

import java.util.Date;

import com.gome.framework.base.BaseBO;


/**
 * @author tx_chenxin
 */
@SuppressWarnings("serial")
public class GomeGmpResNeedBO extends BaseBO {
	
	/** id */
	private String needId;
	/** 标题 */
	private String title;
	/** 业务部门 */
	private String unitBsId;
	/** 关键用户id */
	private Integer keyUserId;
	/** 优先级 */
	private Integer priorityId;
	/** 交付人id */
	private Integer payUserId;
	/** 转交前交付人id */
	private Integer prePayUserId;
	/** 提出时间 */
	private Date postTime;
	/** 计划上线时间 */
	private Date planTime;
	/** 平台 */
	private String ptId;
	/** OA审批号 */
	private String oaId;
	/** 附件 */
	private String appendex;
	/** 项目简述 */
	private String proRemark;
	/** 功能描述 */
	private String functionDesc;
	/** 需求目标 */
	private String needTarget;
	/** 状态(1.已完成 2.已拒绝 3.已接受 4..待处理 5.已处理 ) */
	private Integer states;
	/** 创建人 */
	private String createUser;
	/** 交付部门 */
	private String payUnitId;
	/**  1.项目 2.敏捷 3.需求 */
	private Integer type;
	/** 拒绝原因 */
	private String deny;
	/** 说明原因 */
	private String reason;
	/** 更新用户 */
	private Integer updateUser;
	
	public String getNeedId() {
		return this.needId;
	}
	public void setNeedId(String needId) {
		this.needId = needId;
	}
	public String getTitle() {
		return this.title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getKeyUserId() {
		return this.keyUserId;
	}
	public void setKeyUserId(Integer keyUserId) {
		this.keyUserId = keyUserId;
	}
	public Integer getPriorityId() {
		return this.priorityId;
	}
	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}
	public Integer getPayUserId() {
		return this.payUserId;
	}
	public void setPayUserId(Integer payUserId) {
		this.payUserId = payUserId;
	}
	public Date getPostTime() {
		return this.postTime;
	}
	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}
	public Date getPlanTime() {
		return this.planTime;
	}
	public void setPlanTime(Date planTime) {
		this.planTime = planTime;
	}
	public String getPtId() {
		return this.ptId;
	}
	public void setPtId(String ptId) {
		this.ptId = ptId;
	}
	public String getOaId() {
		return this.oaId;
	}
	public void setOaId(String oaId) {
		this.oaId = oaId;
	}
	public String getAppendex() {
		return this.appendex;
	}
	public void setAppendex(String appendex) {
		this.appendex = appendex;
	}
	public String getProRemark() {
		return this.proRemark;
	}
	public void setProRemark(String proRemark) {
		this.proRemark = proRemark;
	}
	public String getFunctionDesc() {
		return this.functionDesc;
	}
	public void setFunctionDesc(String functionDesc) {
		this.functionDesc = functionDesc;
	}
	public String getNeedTarget() {
		return this.needTarget;
	}
	public void setNeedTarget(String needTarget) {
		this.needTarget = needTarget;
	}
	public Integer getStates() {
		return this.states;
	}
	public void setStates(Integer states) {
		this.states = states;
	}
	public String getCreateUser() {
		return this.createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getUnitBsId() {
		return unitBsId;
	}
	
	public void setUnitBsId(String unitBsId) {
		this.unitBsId = unitBsId;
	}
	
	public String getPayUnitId() {
		return payUnitId;
	}
	
	public void setPayUnitId(String payUnitId) {
		this.payUnitId = payUnitId;
	}
	public Integer getType() {
		return this.type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getDeny() {
		return this.deny;
	}
	public void setDeny(String deny) {
		this.deny = deny;
	}
	public String getReason() {
		return this.reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Integer getPrePayUserId() {
		return prePayUserId;
	}
	public void setPrePayUserId(Integer prePayUserId) {
		this.prePayUserId = prePayUserId;
	}
	public Integer getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(Integer updateUser) {
		this.updateUser = updateUser;
	}
}