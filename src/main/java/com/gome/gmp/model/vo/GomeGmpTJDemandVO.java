package com.gome.gmp.model.vo;

import java.util.Date;
import java.util.List;

/**
 * 统计上线分布
 *
 *
 */
public class GomeGmpTJDemandVO extends GomeGmpResProjectVO {

	/**  */
	private static final long serialVersionUID = -8998372611893450939L;

	/**
	 * 项目统计详情列表
	 */
	// 资料详情
	private List<GomeGmpResDailyVO> details;

	// 数组年
	private List<String> years;

	// 年
	private String year;

	// 月
	private String month;

	// 组织id
	private Integer orgId;

	// 开始时间
	private Date startTimes;

	// 结束时间
	private Date endTimes;

	// 页面传值
	private Integer queryType;

	public List<GomeGmpResDailyVO> getDetails() {
		return details;
	}

	public void setDetails(List<GomeGmpResDailyVO> details) {
		this.details = details;
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

	public Date getStartTimes() {
		return startTimes;
	}

	public void setStartTimes(Date startTimes) {
		this.startTimes = startTimes;
	}

	public Date getEndTimes() {
		return endTimes;
	}

	public void setEndTimes(Date endTimes) {
		this.endTimes = endTimes;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Integer getQueryType() {
		return queryType;
	}

	public void setQueryType(Integer queryType) {
		this.queryType = queryType;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
}
