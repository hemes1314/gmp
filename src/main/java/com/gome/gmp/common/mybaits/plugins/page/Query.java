package com.gome.gmp.common.mybaits.plugins.page;

import java.util.Map;

/**
 * 封装查询蚕食和查询条件
 *
 * @author dendy
 *
 */
public class Query {
    private Map<String, Object> queryParams;
    private Pager pager;

    public Map<String, Object> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(Map<String, Object> queryParams) {
        this.queryParams = queryParams;
    }

	
	public Pager getPager() {
		return pager;
	}

	
	public void setPager(Pager pager) {
		this.pager = pager;
	}

    //省略getter和setter
}