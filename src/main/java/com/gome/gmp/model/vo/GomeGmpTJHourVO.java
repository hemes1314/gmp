package com.gome.gmp.model.vo;

import java.util.List;

/**
 * 项目工时统计
 *
 * @author wubin
 */
public class GomeGmpTJHourVO {

	/**
	 * 关联项目
	 */
	private String proId;

	/**
	 * 实际工时
	 */
	private Double realHour = 0.0;

	/**
	 * 计划工时
	 */
	private Double planHour = 0.0;

	/**
	 * 百分比
	 */
	private Long percentage = 0L;

	/**
	 * 项目统计详情列表
	 */
	private List<GomeGmpResDailyVO> details;

	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}
	
	public Double getRealHour() {
		return realHour;
	}

	public void setRealHour(Double realHour) {
		this.realHour = realHour;
	}
	
	public Double getPlanHour() {
		return planHour;
	}
	
	public void setPlanHour(Double planHour) {
		this.planHour = planHour;
	}

	public Long getPercentage() {
		return percentage;
	}
	
	public void setPercentage(Long percentage) {
		this.percentage = percentage;
	}

	public List<GomeGmpResDailyVO> getDetails() {
		return details;
	}

	public void setDetails(List<GomeGmpResDailyVO> details) {
		this.details = details;
	}
}
