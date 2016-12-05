package com.gome.gmp.model.vo;

import java.util.Date;
import java.util.List;

import com.gome.gmp.common.FieldMeta;

public class GomeGmpTJCalendarVO extends GomeGmpResProjectVO {

	/**  */
	private static final long serialVersionUID = 9209292818720853358L;

	private List<GomeGmpTJCalendarVO> calenderList;

	// 日历
	/** 检索月份 */
	@FieldMeta(isLog = false)
	private String strMonth;
	
	@FieldMeta(isLog = false)
	private String strDate;

	/** 状态发生时间 */
	@FieldMeta(isLog = false)
	private String actionDate;

	/** 启动的项目计数 */
	@FieldMeta(isLog = false)
	private Integer startCount;

	/** 计划上线的项目计数 */
	@FieldMeta(isLog = false)
	private Integer planCount;

	/** 已上线的项目计数 */
	@FieldMeta(isLog = false)
	private Integer onlineCount;

	/** 延期的项目计数 */
	@FieldMeta(isLog = false)
	private Integer delayCount;

	/** 部门名称 */
	@FieldMeta(isLog = false)
	private String unitName;

	/** 未更新 */
	@FieldMeta(isLog = false)
	private Integer noUpdatedCount;

	/** 已更新 */
	@FieldMeta(isLog = false)
	private Integer updatedCount;

	/** 更新占比 */
	@FieldMeta(isLog = false)
	private Integer updatePct;

	/** 暂停项目 */
	@FieldMeta(isLog = false)
	private Integer updatedPauseCount;

	/** 关闭项目 */
	@FieldMeta(isLog = false)
	private Integer updatedCloseCount;

	/** 上线时间 */
	@FieldMeta(isLog = false)
	private Date onlineTime;

	public List<GomeGmpTJCalendarVO> getCalenderList() {
		return calenderList;
	}

	public void setCalenderList(List<GomeGmpTJCalendarVO> calenderList) {
		this.calenderList = calenderList;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Integer getNoUpdatedCount() {
		return noUpdatedCount;
	}

	public void setNoUpdatedCount(Integer noUpdatedCount) {
		this.noUpdatedCount = noUpdatedCount;
	}

	public Integer getUpdatedCount() {
		return updatedCount;
	}

	public void setUpdatedCount(Integer updatedCount) {
		this.updatedCount = updatedCount;
	}

	public Integer getUpdatePct() {
		return updatePct;
	}

	public void setUpdatePct(Integer updatePct) {
		this.updatePct = updatePct;
	}

	public Integer getUpdatedPauseCount() {
		return updatedPauseCount;
	}

	public void setUpdatedPauseCount(Integer updatedPauseCount) {
		this.updatedPauseCount = updatedPauseCount;
	}

	public Integer getUpdatedCloseCount() {
		return updatedCloseCount;
	}

	public void setUpdatedCloseCount(Integer updatedCloseCount) {
		this.updatedCloseCount = updatedCloseCount;
	}

	public Date getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(Date onlineTime) {
		this.onlineTime = onlineTime;
	}

	public String getActionDate() {
		return actionDate;
	}

	public void setActionDate(String actionDate) {
		this.actionDate = actionDate;
	}

	public Integer getStartCount() {
		return startCount;
	}

	public void setStartCount(Integer startCount) {
		this.startCount = startCount;
	}

	public Integer getPlanCount() {
		return planCount;
	}

	public void setPlanCount(Integer planCount) {
		this.planCount = planCount;
	}

	public Integer getOnlineCount() {
		return onlineCount;
	}

	public void setOnlineCount(Integer onlineCount) {
		this.onlineCount = onlineCount;
	}

	public Integer getDelayCount() {
		return delayCount;
	}

	public void setDelayCount(Integer delayCount) {
		this.delayCount = delayCount;
	}

	public String getStrMonth() {
		return strMonth;
	}

	public void setStrMonth(String strMonth) {
		this.strMonth = strMonth;
	}

	
	public String getStrDate() {
		return strDate;
	}

	
	public void setStrDate(String strDate) {
		this.strDate = strDate;
	}
}
