package com.gome.gmp.model.bo;

import java.util.Date;

import com.gome.framework.base.BaseBO;

/**
 * @author Administrator
 */
public class GomeGmpResDatasynBO extends BaseBO {

	/**  */
	private static final long serialVersionUID = 1939972011409415702L;

	/** 资料名称 */
	private String fileName;

	/** 文件地址 */
	private String filePath;

	/** 物理文件路径 */
	private String physicalPath;

	/** 关联项目id */
	private String proId;

	/** 需求id */
	private String needId;

	/** 1:项目，2：敏捷 */
	private Integer proType;

	/** 上传人 */
	private Integer uploadUserId;

	/** 上传时间 */
	private Date uploadTime;

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getPhysicalPath() {
		return this.physicalPath;
	}

	public void setPhysicalPath(String physicalPath) {
		this.physicalPath = physicalPath;
	}

	public String getProId() {
		return this.proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	public String getNeedId() {
		return this.needId;
	}

	public void setNeedId(String needId) {
		this.needId = needId;
	}

	public Integer getProType() {
		return this.proType;
	}

	public void setProType(Integer proType) {
		this.proType = proType;
	}

	public Integer getUploadUserId() {
		return this.uploadUserId;
	}

	public void setUploadUserId(Integer uploadUserId) {
		this.uploadUserId = uploadUserId;
	}

	public Date getUploadTime() {
		return this.uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
}