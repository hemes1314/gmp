package com.gome.gmp.model.bo;

import com.gome.framework.base.BaseBO;

/**
 * @author Administrator
 */
public class GomeGmpResProSysBO extends BaseBO {

	/**  */
	private static final long serialVersionUID = -8905255373064233898L;

	/** 项目id */
	private String proId;

	/** 设计系统 */
	private Integer sysId;

	public String getProId() {
		return this.proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	public Integer getSysId() {
		return this.sysId;
	}

	public void setSysId(Integer sysId) {
		this.sysId = sysId;
	}
}