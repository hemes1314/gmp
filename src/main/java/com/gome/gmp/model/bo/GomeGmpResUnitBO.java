package com.gome.gmp.model.bo;

import com.gome.framework.base.BaseBO;


/**
 * @author Administrator
 */
public class GomeGmpResUnitBO extends BaseBO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**  */
	private String unitName;
	/**  */
	private Integer unitParent;
	/**  */
	private Integer unitLevel;
	
	public String getUnitName() {
		return this.unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public Integer getUnitParent() {
		return this.unitParent;
	}
	public void setUnitParent(Integer unitParent) {
		this.unitParent = unitParent;
	}
	public Integer getUnitLevel() {
		return this.unitLevel;
	}
	public void setUnitLevel(Integer unitLevel) {
		this.unitLevel = unitLevel;
	}
}