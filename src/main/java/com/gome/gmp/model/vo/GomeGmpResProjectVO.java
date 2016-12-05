package com.gome.gmp.model.vo;

import java.util.Date;
import java.util.List;

import com.gome.gmp.common.FieldMeta;
import com.gome.gmp.model.bo.GomeGmpDictItemBO;
import com.gome.gmp.model.bo.GomeGmpResOrgBO;
import com.gome.gmp.model.bo.GomeGmpResProjectBO;
import com.gome.gmp.model.bo.GomeGmpResUserBO;

/**
 * @author Administrator
 */
public class GomeGmpResProjectVO extends GomeGmpResProjectBO {

	/**  */
	private static final long serialVersionUID = 110775339723493657L;

	// 查询列表类型, init,初始默认列表
	@FieldMeta(isLog = false)
	private String listType;
	
	// 完成工时
	@FieldMeta(isLog = false)
	private Double finishHour;

	// 计划工时
	@FieldMeta(isLog = false)
	private Double planHour;

	// 完成工时/计划工时
	@FieldMeta(isLog = false)
	private Long finishPlan;

	// 完成百分比
	@FieldMeta(isLog = false)
	private String percentage;

	// 需求
	@FieldMeta(isLog = false)
	private String demandName;

	// 状态
	@FieldMeta(isLog = false)
	private String statusName;

	// 项目经理
	@FieldMeta(isLog = false)
	private String bpName;

	// 项目经理
	@FieldMeta(isLog = false)
	private String[] bpIds;

	@FieldMeta(isLog = false)
	private List<GomeGmpResUserBO> bpIdsV;

	// 业务部门
	@FieldMeta(isLog = false)
	private String unitBsName;

	// 所有级别部门
	@FieldMeta(isLog = false)
	private String unitBsIdAll;

	// 关键用户
	@FieldMeta(isLog = false)
	private String keyUserName;

	// 优先级
	@FieldMeta(isLog = false)
	private String priorityName;

	@FieldMeta(isLog = false)
	private List<GomeGmpDictItemBO> priorityIdsV;

	// 实施阶段
	@FieldMeta(isLog = false)
	private String actualizeName;

	@FieldMeta(isLog = false)
	private List<GomeGmpDictItemBO> actualizesV;

	// 进度状态
	@FieldMeta(isLog = false)
	private String scheduleName;

	@FieldMeta(isLog = false)
	private List<GomeGmpDictItemBO> scheduleIdsV;

	// 更新人姓名
	@FieldMeta(isLog = false)
	private String updateUserName;

	// 创建人姓名
	@FieldMeta(isLog = false)
	private String createUserName;

	// 查询参数
	@FieldMeta(isLog = false)
	private String qryType;

	// 结束时间
	@FieldMeta(isLog = false)
	private Date endTime;

	@FieldMeta(isLog = false)
	private Date startCreateTime;

	@FieldMeta(isLog = false)
	private Date endCreateTime;

	@FieldMeta(isLog = false)
	private boolean afterStartTime;

	// 登录用户id
	@FieldMeta(isLog = false)
	private Long loginUserId;

	// 当前用户所在组织级别
	@FieldMeta(isLog = false)
	private Integer loginUserOrgLevel;

	// 是否为小组组员
	@FieldMeta(isLog = false)
	private Boolean isMember;

	// 是否有相关人权限
	@FieldMeta(isLog = false)
	private Long isAuth;

	// 任务状态
	@FieldMeta(isLog = false)
	private String[] taskStatus;

	// 任务状态名称
	@FieldMeta(isLog = false)
	private List<GomeGmpDictItemBO> taskStatusV;

	// 项目相关人
	@FieldMeta(isLog = false)
	private String[] proRelatedUsers;

	// 部门
	@FieldMeta(isLog = false)
	private String[] orgIds;

	// 子部门
	@FieldMeta(isLog = false)
	private String[] childOrgIds;

	// 小组
	@FieldMeta(isLog = false)
	private String[] groupIds;

	// 优先级
	@FieldMeta(isLog = false)
	private String[] priorityIds;

	// 进度状态
	@FieldMeta(isLog = false)
	private String[] scheduleIds;

	// 实施阶段
	@FieldMeta(isLog = false)
	private String[] actualizes;

	// 业务部门
	@FieldMeta(isLog = false)
	private List<GomeGmpResOrgVO> resUnitV;

	// 用户所在中心部门
	@FieldMeta(isLog = false)
	private List<GomeGmpResOrgVO> userCenterOrgList;

	// 用户所在部门 查询条件
	@FieldMeta(isLog = false)
	private List<GomeGmpResOrgBO> userOrgBoList;

	// 涉及系统ID
	@FieldMeta(isLog = false)
	private String systemIds;

	// 涉及系统NAME
	@FieldMeta(isLog = false)
	private String systemNames;

	// 返回结果
	@FieldMeta(isLog = false)
	private String returnData;

	// 涉及系统
	private List<GomeGmpResProSysVO> systems;

	// 涉及系统V
	@FieldMeta(isLog = false)
	private List<GomeGmpDictItemBO> systemsV;

	// 关联角色
	private List<GomeGmpResRelatedUserVO> relatedUsers;

	// 关联任务
	private List<GomeGmpResTaskVO> relatedTasks;

	// 任务数量
	@FieldMeta(isLog = false)
	private Integer taskNum;

	// 上传文件
	@FieldMeta(isLog = false)
	private List<GomeGmpResDatasynVO> uploadFileInfo;

	// 旧上传文件处理
	@FieldMeta(isLog = false)
	private List<GomeGmpResDatasynVO> oldUploadFileInfo;

	// 项目变更记录
	private String changeLogs;

	/** 项目状态汉字 */
	@FieldMeta(isLog = false)
	private String actionState;

	/** 开始时间 */
	@FieldMeta(isLog = false)
	private String startDate;

	/** 结束时间 */
	@FieldMeta(isLog = false)
	private String endDate;

	/** 组织ID */
	@FieldMeta(isLog = false)
	private String orgKey;

	/** 数组年 */
	private List<String> years;

	/** 年 */
	private String year;

	public Double getFinishHour() {
		return finishHour;
	}

	public void setFinishHour(Double finishHour) {
		this.finishHour = finishHour;
	}

	public Double getPlanHour() {
		return planHour;
	}

	public void setPlanHour(Double planHour) {
		this.planHour = planHour;
	}

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

	public String getOrgKey() {
		return orgKey;
	}

	public void setOrgKey(String orgKey) {
		this.orgKey = orgKey;
	}

	public String getActionState() {
		return actionState;
	}

	public void setActionState(String actionState) {
		this.actionState = actionState;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	public Long getIsAuth() {
		return isAuth;
	}

	public void setIsAuth(Long isAuth) {
		this.isAuth = isAuth;
	}

	public String[] getPriorityIds() {
		return priorityIds;
	}

	public void setPriorityIds(String[] priorityIds) {
		this.priorityIds = priorityIds;
	}

	public String[] getScheduleIds() {
		return scheduleIds;
	}

	public void setScheduleIds(String[] scheduleIds) {
		this.scheduleIds = scheduleIds;
	}

	public String[] getActualizes() {
		return actualizes;
	}

	public Long getLoginUserId() {
		return loginUserId;
	}

	public void setLoginUserId(Long loginUserId) {
		this.loginUserId = loginUserId;
	}

	public void setActualizes(String[] actualizes) {
		this.actualizes = actualizes;
	}

	public String[] getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String[] taskStatus) {
		this.taskStatus = taskStatus;
	}

	public List<GomeGmpDictItemBO> getTaskStatusV() {
		return taskStatusV;
	}

	public void setTaskStatusV(List<GomeGmpDictItemBO> taskStatusV) {
		this.taskStatusV = taskStatusV;
	}

	public String getDemandName() {
		return demandName;
	}

	public void setDemandName(String demandName) {
		this.demandName = demandName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getBpName() {
		return bpName;
	}

	public void setBpName(String bpName) {
		this.bpName = bpName;
	}

	public String getUnitBsName() {
		return unitBsName;
	}

	public void setUnitBsName(String unitBsName) {
		this.unitBsName = unitBsName;
	}

	public String getKeyUserName() {
		return keyUserName;
	}

	public void setKeyUserName(String keyUserName) {
		this.keyUserName = keyUserName;
	}

	public String getPriorityName() {
		return priorityName;
	}

	public void setPriorityName(String priorityName) {
		this.priorityName = priorityName;
	}

	public String getActualizeName() {
		return actualizeName;
	}

	public void setActualizeName(String actualizeName) {
		this.actualizeName = actualizeName;
	}

	public List<GomeGmpResProSysVO> getSystems() {
		return systems;
	}

	public void setSystems(List<GomeGmpResProSysVO> systems) {
		this.systems = systems;
	}

	public List<GomeGmpDictItemBO> getSystemsV() {
		return systemsV;
	}

	public void setSystemsV(List<GomeGmpDictItemBO> systemsV) {
		this.systemsV = systemsV;
	}

	public List<GomeGmpResRelatedUserVO> getRelatedUsers() {
		return relatedUsers;
	}

	public void setRelatedUsers(List<GomeGmpResRelatedUserVO> relatedUsers) {
		this.relatedUsers = relatedUsers;
	}

	public List<GomeGmpResTaskVO> getRelatedTasks() {
		return relatedTasks;
	}

	public void setRelatedTasks(List<GomeGmpResTaskVO> relatedTasks) {
		this.relatedTasks = relatedTasks;
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

	public List<GomeGmpDictItemBO> getActualizesV() {
		return actualizesV;
	}

	public void setActualizesV(List<GomeGmpDictItemBO> actualizesV) {
		this.actualizesV = actualizesV;
	}

	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	public List<GomeGmpDictItemBO> getScheduleIdsV() {
		return scheduleIdsV;
	}

	public void setScheduleIdsV(List<GomeGmpDictItemBO> scheduleIdsV) {
		this.scheduleIdsV = scheduleIdsV;
	}

	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getSystemIds() {
		return systemIds;
	}

	public void setSystemIds(String systemIds) {
		this.systemIds = systemIds;
	}

	public Long getFinishPlan() {
		return finishPlan;
	}

	public void setFinishPlan(Long finishPlan) {
		this.finishPlan = finishPlan;
	}

	public List<GomeGmpResDatasynVO> getOldUploadFileInfo() {
		return oldUploadFileInfo;
	}

	public void setOldUploadFileInfo(List<GomeGmpResDatasynVO> oldUploadFileInfo) {
		this.oldUploadFileInfo = oldUploadFileInfo;
	}

	public String getSystemNames() {
		return systemNames;
	}

	public void setSystemNames(String systemNames) {
		this.systemNames = systemNames;
	}

	public Date getStartCreateTime() {
		return startCreateTime;
	}

	public void setStartCreateTime(Date startCreateTime) {
		this.startCreateTime = startCreateTime;
	}

	public Date getEndCreateTime() {
		return endCreateTime;
	}

	public void setEndCreateTime(Date endCreateTime) {
		this.endCreateTime = endCreateTime;
	}

	public boolean isAfterStartTime() {
		return afterStartTime;
	}

	public void setAfterStartTime(boolean afterStartTime) {
		this.afterStartTime = afterStartTime;
	}

	public Integer getTaskNum() {
		return taskNum;
	}

	public void setTaskNum(Integer taskNum) {
		this.taskNum = taskNum;
	}

	public String getChangeLogs() {
		return changeLogs;
	}

	public void setChangeLogs(String changeLogs) {
		this.changeLogs = changeLogs;
	}

	public String getQryType() {
		return qryType;
	}

	public void setQryType(String qryType) {
		this.qryType = qryType;
	}

	public String getReturnData() {
		return returnData;
	}

	public void setReturnData(String returnData) {
		this.returnData = returnData;
	}

	public List<String> getYears() {
		return years;
	}

	public void setYears(List<String> years) {
		this.years = years;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public List<GomeGmpResOrgVO> getResUnitV() {
		return resUnitV;
	}

	public void setResUnitV(List<GomeGmpResOrgVO> resUnitV) {
		this.resUnitV = resUnitV;
	}

	public Integer getLoginUserOrgLevel() {
		return loginUserOrgLevel;
	}

	public void setLoginUserOrgLevel(Integer loginUserOrgLevel) {
		this.loginUserOrgLevel = loginUserOrgLevel;
	}

	public List<GomeGmpResOrgVO> getUserCenterOrgList() {
		return userCenterOrgList;
	}

	public void setUserCenterOrgList(List<GomeGmpResOrgVO> userCenterOrgList) {
		this.userCenterOrgList = userCenterOrgList;
	}

	public List<GomeGmpResOrgBO> getUserOrgBoList() {
		return userOrgBoList;
	}

	public void setUserOrgBoList(List<GomeGmpResOrgBO> userOrgBoList) {
		this.userOrgBoList = userOrgBoList;
	}

	public String[] getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(String[] orgIds) {
		this.orgIds = orgIds;
	}

	public String[] getChildOrgIds() {
		return childOrgIds;
	}

	public void setChildOrgIds(String[] childOrgIds) {
		this.childOrgIds = childOrgIds;
	}

	public String[] getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(String[] groupIds) {
		this.groupIds = groupIds;
	}

	public String[] getBpIds() {
		return bpIds;
	}

	public void setBpIds(String[] bpIds) {
		this.bpIds = bpIds;
	}

	public List<GomeGmpResUserBO> getBpIdsV() {
		return bpIdsV;
	}

	public void setBpIdsV(List<GomeGmpResUserBO> bpIdsV) {
		this.bpIdsV = bpIdsV;
	}

	public String getUnitBsIdAll() {
		return unitBsIdAll;
	}

	public void setUnitBsIdAll(String unitBsIdAll) {
		this.unitBsIdAll = unitBsIdAll;
	}

	public String[] getProRelatedUsers() {
		return proRelatedUsers;
	}

	public void setProRelatedUsers(String[] proRelatedUsers) {
		this.proRelatedUsers = proRelatedUsers;
	}

	public Boolean getIsMember() {
		return isMember;
	}

	public void setIsMember(Boolean isMember) {
		this.isMember = isMember;
	}

	
	public String getListType() {
		return listType;
	}

	
	public void setListType(String listType) {
		this.listType = listType;
	}
}