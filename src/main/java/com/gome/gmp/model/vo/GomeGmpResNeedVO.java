package com.gome.gmp.model.vo;

import java.util.Date;
import java.util.List;

import com.gome.gmp.model.bo.GomeGmpDictItemBO;
import com.gome.gmp.model.bo.GomeGmpResNeedBO;
import com.gome.gmp.model.bo.GomeGmpResRelatedUserBO;
import com.gome.gmp.model.bo.GomeGmpResUserBO;

/**
 * @author Administrator
 */
public class GomeGmpResNeedVO extends GomeGmpResNeedBO {

	/**  */
	private static final long serialVersionUID = 869102393421655637L;

	// 项目id
	private String proId;

	// 项目名称
	private String proTitle;

	// 业务部门名称
	private String unitBsName;

	// 关键用户
	private String keyUser;

	// 优先级名称
	private String priorityName;

	// 开始时间
	private Date startTime;

	// 结束时间
	private Date endTime;

	// 交付人名称
	private String payUserName;

	// 交付部门
	private String payUnitName;

	// 提报人名
	private String createUserName;

	// 状态名称
	private String statusName;

	// 登录用户id
	private Long loginUserId;

	// 部门
	private String[] orgId;

	// 子部门
	private String[] childOrgId;

	// 小组
	private String[] groupId;

	// 等级
	private Integer level;

	private String teamId;

	List<GomeGmpResRelatedUserBO> relatedUsers;

	List<GomeGmpResUserBO> userBO;

	List<GomeGmpResDatasynVO> uploadFileInfo;

	// 优先级
	private List<GomeGmpDictItemBO> priorityIdsV;

	// 业务部门
	private List<GomeGmpResOrgVO> resUnitV;

	// 交付部门
	private List<GomeGmpDictItemBO> payUnitV;

	// 状态名称
	private String statesName;

	// 状态名称
	private List<GomeGmpDictItemBO> statusV;

	private List<GomeGmpResDatasynVO> oldUploadFileInfo;

	// 开始时间
	private String startTimes;

	// 结束时间
	private String endTimes;

	public String getProTitle() {
		return proTitle;
	}

	public void setProTitle(String proTitle) {
		this.proTitle = proTitle;
	}

	public List<GomeGmpResDatasynVO> getOldUploadFileInfo() {
		return oldUploadFileInfo;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public void setOldUploadFileInfo(List<GomeGmpResDatasynVO> oldUploadFileInfo) {
		this.oldUploadFileInfo = oldUploadFileInfo;
	}

	public String getUnitBsName() {
		return unitBsName;
	}

	public void setUnitBsName(String unitBsName) {
		this.unitBsName = unitBsName;
	}

	public String getKeyUser() {
		return keyUser;
	}

	public void setKeyUser(String keyUser) {
		this.keyUser = keyUser;
	}

	public String getPriorityName() {
		return priorityName;
	}

	public void setPriorityName(String priorityName) {
		this.priorityName = priorityName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getPayUserName() {
		return payUserName;
	}

	public void setPayUserName(String payUserName) {
		this.payUserName = payUserName;
	}

	public String getPayUnitName() {
		return payUnitName;
	}

	public void setPayUnitName(String payUnitName) {
		this.payUnitName = payUnitName;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getStatesName() {
		return statesName;
	}

	public void setStatesName(String statesName) {
		this.statesName = statesName;
	}

	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	public List<GomeGmpResRelatedUserBO> getRelatedUsers() {
		return relatedUsers;
	}

	public void setRelatedUsers(List<GomeGmpResRelatedUserBO> relatedUsers) {
		this.relatedUsers = relatedUsers;
	}

	public List<GomeGmpResUserBO> getUserBO() {
		return userBO;
	}

	public void setUserBO(List<GomeGmpResUserBO> userBO) {
		this.userBO = userBO;
	}

	public List<GomeGmpResDatasynVO> getUploadFileInfo() {
		return uploadFileInfo;
	}

	public void setUploadFileInfo(List<GomeGmpResDatasynVO> uploadFileInfo) {
		this.uploadFileInfo = uploadFileInfo;
	}

	public List<GomeGmpDictItemBO> getPriorityIdsV() {
		return priorityIdsV;
	}

	public void setPriorityIdsV(List<GomeGmpDictItemBO> priorityIdsV) {
		this.priorityIdsV = priorityIdsV;
	}
	
	public List<GomeGmpResOrgVO> getResUnitV() {
		return resUnitV;
	}

	public void setResUnitV(List<GomeGmpResOrgVO> resUnitV) {
		this.resUnitV = resUnitV;
	}

	public List<GomeGmpDictItemBO> getPayUnitV() {
		return payUnitV;
	}

	public void setPayUnitV(List<GomeGmpDictItemBO> payUnitV) {
		this.payUnitV = payUnitV;
	}

	public List<GomeGmpDictItemBO> getStatusV() {
		return statusV;
	}

	public void setStatusV(List<GomeGmpDictItemBO> statusV) {
		this.statusV = statusV;
	}

	public String[] getOrgId() {
		return orgId;
	}

	public void setOrgId(String[] orgId) {
		this.orgId = orgId;
	}

	public String[] getChildOrgId() {
		return childOrgId;
	}

	public void setChildOrgId(String[] childOrgId) {
		this.childOrgId = childOrgId;
	}

	public String[] getGroupId() {
		return groupId;
	}

	public void setGroupId(String[] groupId) {
		this.groupId = groupId;
	}

	public String getStartTimes() {
		return startTimes;
	}

	public void setStartTimes(String startTimes) {
		this.startTimes = startTimes;
	}

	public String getEndTimes() {
		return endTimes;
	}

	public void setEndTimes(String endTimes) {
		this.endTimes = endTimes;
	}

	public Long getLoginUserId() {
		return loginUserId;
	}

	public void setLoginUserId(Long loginUserId) {
		this.loginUserId = loginUserId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
}