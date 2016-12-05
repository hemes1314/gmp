package com.gome.gmp.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 应用访问过滤器
 * @author: wubin
 */
@WebFilter(filterName = "commandContextFilter", urlPatterns = { "/*" })
public class CommandContextFilter implements Filter {

	protected FilterConfig filterConfig = null;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		try {
			HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) res;
			// 存取request
			CommandContext.setRequest(request);
			CommandContext.setResponse(response);
			chain.doFilter(req, res);
		} finally {
			// 清空所有线程变量
			CommandContext.close();
		}
	}

	@Override
	public void destroy() {
		this.filterConfig = null;
	}
}