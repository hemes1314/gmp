package com.gome.gmp.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.gome.framework.base.BaseRestController;

/**
 * 主页
 * 
 * @author wubin
 */
@RestController
public class LayoutCTL extends BaseRestController {

	/**
	 * 主界面
	 * 
	 * @return
	 * @author wubin
	 */
	@RequestMapping(value = "/layout", method = RequestMethod.GET)
	public ModelAndView layout() {
		return new ModelAndView("/common/layout/frame");
	}

	/**
	 * 主界面top部分
	 * 
	 * @return
	 * @author wubin
	 */
	@RequestMapping(value = "/top", method = RequestMethod.GET)
	public ModelAndView top() {
		return new ModelAndView("/common/layout/top");
	}

	/**
	 * 主界面主要部分
	 * 
	 * @return
	 * @author wubin
	 */
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public ModelAndView main() {
		return new ModelAndView("/common/layout/main");
	}

	/**
	 * 主界面左侧部分
	 * 
	 * @return
	 * @author wubin
	 */
	@RequestMapping(value = "/left", method = RequestMethod.GET)
	public ModelAndView left() {
		return new ModelAndView("/common/layout/left");
	}
}
