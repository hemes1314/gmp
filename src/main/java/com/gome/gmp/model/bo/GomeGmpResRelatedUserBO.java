package com.gome.gmp.model.bo;

import com.gome.framework.base.BaseBO;

/**
 * @author tx_chenxin
 */
public class GomeGmpResRelatedUserBO extends BaseBO {

	/**  */
	private static final long serialVersionUID = 7366708298400079774L;

	/** 项目id */
	private String proId;

	/** 用户id */
	private Integer userId;

	/** 角色id */
	private Integer roleId;

	/** 1:项目，2:敏捷需求 3.需求 */
	private Integer proType;

	/**  */
	private String needId;
	
	/** 相关人类型  */
	private Integer relatedUserType;

	public String getProId() {
		return this.proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getProType() {
		return this.proType;
	}

	public void setProType(Integer proType) {
		this.proType = proType;
	}

	public String getNeedId() {
		return this.needId;
	}

	public void setNeedId(String needId) {
		this.needId = needId;
	}

	public Integer getRelatedUserType() {
		return relatedUserType;
	}

	public void setRelatedUserType(Integer relatedUserType) {
		this.relatedUserType = relatedUserType;
	}
}