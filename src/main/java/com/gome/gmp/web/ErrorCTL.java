package com.gome.gmp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gome.framework.base.BaseController;

/**
 * 错误页面控制器
 * 
 * @author fuhaining
 */
@Controller
public class ErrorCTL extends BaseController {

	/**
	 * 非法请求
	 * 
	 * @return
	 */
	@RequestMapping(value = "/400")
	public String _400() {
		return "400";
	}

	/**
	 * 找不到页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/404")
	public String _404() {
		return "404";
	}

	/**
	 * 服务器错误
	 * 
	 * @return
	 */
	@RequestMapping(value = "/50x")
	public String _50x() {
		return "50x";
	}
}