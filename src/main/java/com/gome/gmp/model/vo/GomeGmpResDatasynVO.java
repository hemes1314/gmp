package com.gome.gmp.model.vo;

import java.util.ArrayList;
import java.util.List;

import com.gome.gmp.model.bo.GomeGmpResDatasynBO;

/**
 * @author Administrator
 */
public class GomeGmpResDatasynVO extends GomeGmpResDatasynBO {

	/**  */
	private static final long serialVersionUID = -6607781507644729398L;

	// 项目名称
	private String title;

	// 项目名称
	private String strSearch;
	// 上传者
	private String userName;

	private List<Long> deleteIds;

	/** 上传时间YYYY-MM-DD */
	private String strUploadTime;

	public String getStrUploadTime() {
		return strUploadTime;
	}

	public void setStrUploadTime(String strUploadTime) {
		this.strUploadTime = strUploadTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	// 资料列表
	private List<GomeGmpResDatasynVO> dataList;

	public String getTitle() {
		return title;
	}

	public List<Long> getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(List<Long> deleteIds) {
		this.deleteIds = deleteIds;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStrSearch() {
		return strSearch;
	}

	public void setStrSearch(String strSearch) {
		this.strSearch = strSearch;
	}

	public List<GomeGmpResDatasynVO> getDataList() {
		if (dataList == null) {
			dataList = new ArrayList<GomeGmpResDatasynVO>();
		}
		return dataList;
	}

	public void setDataList(List<GomeGmpResDatasynVO> dataList) {
		this.dataList = dataList;
	}
}