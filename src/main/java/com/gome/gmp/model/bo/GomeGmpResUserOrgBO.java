package com.gome.gmp.model.bo;

import com.gome.gmp.common.base.BaseObject;

/**
 * 用户与组织关系表
 * 
 * @author wangchangtie
 *
 */
public class GomeGmpResUserOrgBO extends BaseObject {

	private static final long serialVersionUID = -5223703036516651687L;
	
	/** 表主键id */
	private int id;

	/** 用户表主键id */
	private Integer userTid;

	/** 组织id */
	private String orgId;

	public Integer getUserTid() {
		return userTid;
	}

	public void setUserTid(Integer userTid) {
		this.userTid = userTid;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}