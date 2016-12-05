package com.gome.gmp.common.mybaits.plugins.page;

import java.util.HashMap;
import java.util.Map;

import com.github.pagehelper.Page;
import com.gome.gmp.common.constant.Constants;

/**
 * 集成分页信息的模型
 * @author wangchangtie
 *
 */
public class PageModel {

	public static int PAGE_SIZE = 20;

	/**
	 * 视图路径
	 */
	private String targetUrl;

	/**
	 * 页码，从1开始
	 */
	private int pageNum;

	/**
	 * 页面大小
	 */
	private int pageSize;

	/**
	 * 起始行
	 */
	private int startRow;

	/**
	 * 末行
	 */
	private int endRow;

	/**
	 * 总数
	 */
	private long total;

	/**
	 * 总页数
	 */
	private int pages;

	/**
	 * 结果区分
	 */
	private String resultCode;

	/**
	 * 弹出信息
	 */
	private String message;

	/**
	 * 返回数据
	 */
	private Object resultData;
	
	/**
	 * 查询条件
	 */
	private Map<String, Object> params = new HashMap<String, Object>(); 

	public void setPageData(Page<?> pageData) {
		this.resultData = pageData.getResult();
		this.pageNum = pageData.getPageNum();
		this.pages = pageData.getPages();
		this.pageSize = pageData.getPageSize();
		this.total = pageData.getTotal();
		this.startRow = pageData.getStartRow();
		this.endRow = pageData.getEndRow();
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = Constants.PROJECT_PATH + targetUrl;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getEndRow() {
		return endRow;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public Object getResultData() {
		return resultData;
	}

	public void setResultData(Object resultData) {
		this.resultData = resultData;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
	public Map<String, Object> getParams() {
		return params;
	}

	
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
}
