package com.gome.gmp.model.vo;

import com.gome.gmp.model.bo.GomeGmpResRelatedUserBO;

/**
 * @author Administrator
 */
public class GomeGmpResRelatedUserVO extends GomeGmpResRelatedUserBO {

	/**  */
	private static final long serialVersionUID = 5413511606589461574L;

	/**
	 * 项目名称
	 */
	private String proName;

	/**
	 * 用户名称
	 */
	private String userName;

	/**
	 * 角色名称
	 */
	private String roleName;

	/**
	 * 删除标志
	 */
	private boolean delete;

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

}