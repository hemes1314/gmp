package com.gome.gmp.model.vo;

import java.util.List;

import com.gome.gmp.model.bo.GomeGmpResOrgBO;

/**
 * @author Administrator
 */
public class GomeGmpResOrgVO extends GomeGmpResOrgBO {

	/**  */
	private static final long serialVersionUID = 935471137788145213L;

	// 用户ID
	private String userId;

	// 查询下拉列表类型
	private String queryType;
	
	// 用户名
	private String userName;

	// 组织IDList
	private String[] orgIdList;

	// 计划总工时
	private Double planHoursCount = 0.0;

	// 总工时
	private Double hoursCount;

	// 部门人数
	private Integer memberCount;

	// 日均工时
	private Double avgHour = 0.0;

	// 出勤天数
	private Double weekdayCount;

	// 实际总工时除以计划总工时
	private double percent = 0.0;

	// 组织Leader名
	private String leaderName;

	// 组织LeaderId
	private String leaderId;

	// 用户所在组织级别
	private Integer userOrgLevel;

	public String getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(String leaderId) {
		this.leaderId = leaderId;
	}

	private List<GomeGmpResOrgVO> gomeGmpResOrgVOList;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String[] getOrgIdList() {
		return orgIdList;
	}

	public void setOrgIdList(String[] orgIdList) {
		this.orgIdList = orgIdList;
	}

	public Double getHoursCount() {
		return hoursCount;
	}

	public void setHoursCount(Double hoursCount) {
		this.hoursCount = hoursCount;
	}

	public Integer getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(Integer memberCount) {
		this.memberCount = memberCount;
	}

	public Double getAvgHour() {
		return avgHour;
	}

	public void setAvgHour(Double avgHour) {
		this.avgHour = avgHour;
	}

	public List<GomeGmpResOrgVO> getGomeGmpResOrgVOList() {
		return gomeGmpResOrgVOList;
	}

	public void setGomeGmpResOrgVOList(List<GomeGmpResOrgVO> gomeGmpResOrgVOList) {
		this.gomeGmpResOrgVOList = gomeGmpResOrgVOList;
	}

	public String getLeaderName() {
		return leaderName;
	}

	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}

	public Integer getUserOrgLevel() {
		return userOrgLevel;
	}

	public void setUserOrgLevel(Integer userOrgLevel) {
		this.userOrgLevel = userOrgLevel;
	}

	public Double getPlanHoursCount() {
		return planHoursCount;
	}

	public void setPlanHoursCount(Double planHoursCount) {
		this.planHoursCount = planHoursCount;
	}

	public double getPercent() {
		return percent;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}
	
	public Double getWeekdayCount() {
		return weekdayCount;
	}
	
	public void setWeekdayCount(Double weekdayCount) {
		this.weekdayCount = weekdayCount;
	}

	public String getQueryType() {
		return queryType;
	}

	
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
}