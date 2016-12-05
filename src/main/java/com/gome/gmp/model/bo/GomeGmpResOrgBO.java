package com.gome.gmp.model.bo;

import com.gome.framework.base.BaseBO;

/**
 * @author Administrator
 */
public class GomeGmpResOrgBO extends BaseBO {

	/**  */
	private static final long serialVersionUID = 8984725651095136794L;

	/** 组织id */
	private String orgId;

	/** 组织名称 */
	private String orgName;

	/** 组织上级节点 */
	private String orgParent;

	/** 组织人数 */
	private String orgNum;

	/** 层级 */
	private Integer orgLevel;

	/** 组织leader */
	private String orgLeader;

	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgParent() {
		return this.orgParent;
	}

	public void setOrgParent(String orgParent) {
		this.orgParent = orgParent;
	}

	public String getOrgNum() {
		return this.orgNum;
	}

	public void setOrgNum(String orgNum) {
		this.orgNum = orgNum;
	}

	public Integer getOrgLevel() {
		return orgLevel;
	}

	public void setOrgLevel(Integer orgLevel) {
		this.orgLevel = orgLevel;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgLeader() {
		return orgLeader;
	}

	public void setOrgLeader(String orgLeader) {
		this.orgLeader = orgLeader;
	}
}