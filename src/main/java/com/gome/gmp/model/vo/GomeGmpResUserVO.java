package com.gome.gmp.model.vo;

import com.gome.gmp.model.bo.GomeGmpResUserBO;

/**
 * @author Administrator
 */
public class GomeGmpResUserVO extends GomeGmpResUserBO {

	/**  */
	private static final long serialVersionUID = -99589318038334389L;

	/** 所属分组 */
	private String orgGroup;

	public String getOrgGroup() {
		return orgGroup;
	}

	public void setOrgGroup(String orgGroup) {
		this.orgGroup = orgGroup;
	}
}