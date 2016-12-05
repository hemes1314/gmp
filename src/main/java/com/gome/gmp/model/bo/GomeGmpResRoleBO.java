package com.gome.gmp.model.bo;

import com.gome.framework.base.BaseBO;

/**
 * @author Administrator
 */
public class GomeGmpResRoleBO extends BaseBO {

	/**  */
	private static final long serialVersionUID = 1323099435663827030L;

	/** 角色名称 */
	private String roleName;

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}