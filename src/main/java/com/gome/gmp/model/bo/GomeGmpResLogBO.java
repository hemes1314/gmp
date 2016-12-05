package com.gome.gmp.model.bo;

import com.gome.framework.base.BaseBO;

/**
 * @author Administrator
 */
public class GomeGmpResLogBO extends BaseBO {

	/**  */
	private static final long serialVersionUID = -8152099862098686096L;

	/** 项目id */
	private String proId;

	/**  */
	private String operateType;

	/** 操作列 */
	private String operateColumn;

	/** 操作后值 */
	private String columnVal;

	/** 操作内容 */
	private String content;

	/** 备注 */
	private String memo;

	/** 创建人 */
	private Integer createUser;

	public String getProId() {
		return this.proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	public String getOperateType() {
		return this.operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getOperateColumn() {
		return this.operateColumn;
	}

	public void setOperateColumn(String operateColumn) {
		this.operateColumn = operateColumn;
	}

	public String getColumnVal() {
		return this.columnVal;
	}

	public void setColumnVal(String columnVal) {
		this.columnVal = columnVal;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Integer getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}
}