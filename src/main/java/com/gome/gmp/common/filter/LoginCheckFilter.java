package com.gome.gmp.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginCheckFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest hRequest = (HttpServletRequest) request;
		HttpServletResponse hResponse = (HttpServletResponse) response;
		String requeatURL = hRequest.getRequestURI();
		if (requeatURL.equals("/gmp/login") || requeatURL.equals("/gmp/toLogin") || requeatURL.startsWith("/gmp/static")) {
			chain.doFilter(request, response);
			return;
		}
		Cookie[] cookies = hRequest.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equalsIgnoreCase("user")) { // 获取键
				// cookie.setMaxAge(1 * 60 * 30);
				// hResponse.addCookie(cookie);
					chain.doFilter(request, hResponse);
					return;
				}
			}
		}
		String path = hRequest.getContextPath();
		hResponse.sendRedirect(path + "/login");
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}
	// public void doFilter(ServletRequest arg0, ServletResponse arg1,
	// FilterChain chain) throws IOException, ServletException {
	// HttpServletRequest request = (HttpServletRequest)arg0;
	// HttpServletResponse response = (HttpServletResponse)arg1;
	// UserResult result = SsoUserCookieTools.checkIsLoginByCookie(request,
	// response);
	// if(!result.isSuccess()) {
	// this.toNext(request, response);
	// } else {
	// chain.doFilter(request, response);
	// }
	// }
	//
	// protected void toNext(HttpServletRequest request, HttpServletResponse
	// response) throws IOException {
	// String referer = request.getHeader("referer");
	// if(StringUtils.isBlank(referer)) {
	// response.sendRedirect(SsoUserCookieTools.GOME_LOGIN + "?orginURI=" +
	// request.getHeader("referer"));
	// } else {
	// response.sendRedirect(SsoUserCookieTools.GOME_LOGIN);
	// }
	//
	// }
}
