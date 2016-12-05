package com.gome.gmp.common.mybaits.plugins.page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by zhangdapeng on 2015/5/7.
 */
public class Pager<E> {

	private int pageNo = 1;

	private int pageSize = 10; // 每页显示记录数

	private int startRow = 1;

	private int endRow;

	private int total; // 总记录数

	private int totalPage; // 总页数

	private List<E> list;

	private String dbType;// 数据库类型。默认mysql

	private Map<String, Object> params = new HashMap<String, Object>(); // 查询条件

	private String sortOrder;

	private String sortField;

	public static String DEFAULT_PAGESIZE = "10";

	private boolean entityOrField; // true:需要分页的地方，传入的参数就是Page实体；false:需要分页的地方，传入的参数所代表的实体拥有Page属性

	public Pager() {
	}

	public Pager(String pageNo, String pageSize) {
		this.pageNo = StringUtils.isNotEmpty(pageNo) ? Integer.parseInt(pageNo) : 0;
		this.pageSize = StringUtils.isNotEmpty(pageSize) ? Integer.parseInt(pageSize) : 20;
	}

	public void setTotal(int total) {
		this.total = total;
		this.totalPage = (total % this.pageSize == 0 ? total / this.pageSize : total / this.pageSize + 1);
		this.startRow = (this.pageNo - 1) * this.pageSize;
		this.endRow = this.startRow + this.pageSize;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
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

	public int getTotal() {
		return total;
	}

	public List<E> getList() {
		return list;
	}

	public void setList(List<E> list) {
		this.list = list;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public boolean isEntityOrField() {
		return entityOrField;
	}

	public void setEntityOrField(boolean entityOrField) {
		this.entityOrField = entityOrField;
	}

	@Override
	public String toString() {
		return "Pager [pageNo=" + pageNo + ", pageSize=" + pageSize + ", startRow=" + startRow + ", endRow=" + endRow + ", total=" + total + ", totalPage=" + totalPage + ", list=" + list + ", dbType=" + dbType + ", params=" + params + ", sortOrder=" + sortOrder + ", sortField=" + sortField + "]";
	}
}