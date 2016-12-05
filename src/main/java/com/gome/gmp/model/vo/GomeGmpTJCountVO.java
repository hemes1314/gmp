package com.gome.gmp.model.vo;

public class GomeGmpTJCountVO extends GomeGmpResProjectVO {

	/**  */
	private static final long serialVersionUID = 7841893315833264279L;
	private Integer level;
	private String teamId;
	// 是否是下级
	private boolean subOrg;

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public boolean getSubOrg() {
		return subOrg;
	}
	
	public void setSubOrg(boolean subOrg) {
		this.subOrg = subOrg;
	}
}
