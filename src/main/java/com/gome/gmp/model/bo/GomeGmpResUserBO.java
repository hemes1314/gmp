package com.gome.gmp.model.bo;

import com.gome.framework.base.BaseBO;

/**
 * @author Administrator
 */
public class GomeGmpResUserBO extends BaseBO {

	/**  */
	private static final long serialVersionUID = -5223703036516651687L;

	/** 员工编号 */
	private String pernr;

	/** 用户id */
	private String userId;

	/** 用户名称 */
	private String userName;

	/** 用户密码 */
	private String password;

	/** 部门id */
	private Integer unitId;

	/** 组织id */
	private String orgId;

	/** 邮箱 */
	private String email;

	/** 最后登录时间 */
	private String lastLogtime;

	/** 备注 */
	private String remark;

	/** 权限 */
	private String authority;

	public String getPernr() {
		return pernr;
	}

	public void setPernr(String pernr) {
		this.pernr = pernr;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getUnitId() {
		return this.unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLastLogtime() {
		return this.lastLogtime;
	}

	public void setLastLogtime(String lastLogtime) {
		this.lastLogtime = lastLogtime;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}
}