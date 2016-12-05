package com.gome.gmp.model.vo;

import com.gome.gmp.model.bo.GomeGmpResRoleBO;

/**
 * @author Administrator
 */
public class GomeGmpResRoleVO extends GomeGmpResRoleBO {

	/**  */
	private static final long serialVersionUID = -9121398956242237316L;
	private String[] roleNames;

	public String[] getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String[] roleNames) {
		this.roleNames = roleNames;
	}

}