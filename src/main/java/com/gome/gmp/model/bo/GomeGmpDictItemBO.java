package com.gome.gmp.model.bo;

import com.gome.framework.base.BaseBO;

/**
 * @author Administrator
 */
public class GomeGmpDictItemBO extends BaseBO {

	/**  */
	private static final long serialVersionUID = 7131703084628852799L;

	/** 类型 */
	private String groupType;

	/** 项目id */
	private String itemId;

	/** 项目名称 */
	private String itemName;

	/**  */
	private Integer sort;

	public String getGroupType() {
		return this.groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getItemId() {
		return this.itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getSort() {
		return this.sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
}