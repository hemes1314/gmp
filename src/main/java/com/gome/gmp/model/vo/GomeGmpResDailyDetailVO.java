package com.gome.gmp.model.vo;

import java.util.List;

public class GomeGmpResDailyDetailVO extends GomeGmpResDailyVO {
	/**  */
	private static final long serialVersionUID = -9060239635263287349L;

	// 查询时间
	private String[] strDateList;

	private Integer userId;
	// 日志时间
	private String dailyDate;
	// 日志类型
	private String itemName;
	// 开始分钟
	private String startPoint;
	// 结束分钟
	private String endPoint;
	// 项目名
	private String title;
	// 画面更新时间
	private String updatePoint;

	public String getUpdatePoint() {
		return updatePoint;
	}

	public void setUpdatePoint(String updatePoint) {
		this.updatePoint = updatePoint;
	}

	List<GomeGmpResDailyDetailVO> gomeGmpResDailyDetailVOList;

	public String[] getStrDateList() {
		return strDateList;
	}

	public void setStrDateList(String[] strDateList) {
		this.strDateList = strDateList;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getDailyDate() {
		return dailyDate;
	}

	public void setDailyDate(String dailyDate) {
		this.dailyDate = dailyDate;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(String startPoint) {
		this.startPoint = startPoint;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<GomeGmpResDailyDetailVO> getGomeGmpResDailyDetailVOList() {
		return gomeGmpResDailyDetailVOList;
	}

	public void setGomeGmpResDailyDetailVOList(List<GomeGmpResDailyDetailVO> gomeGmpResDailyDetailVOList) {
		this.gomeGmpResDailyDetailVOList = gomeGmpResDailyDetailVOList;
	}

}
