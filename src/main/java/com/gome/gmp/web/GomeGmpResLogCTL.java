package com.gome.gmp.web;

import javax.annotation.Resource;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.Page;
import com.gome.framework.base.BaseRestController;
import com.gome.gmp.business.GomeGmpResLogBS;
import com.gome.gmp.common.BusinessUtil;
import com.gome.gmp.common.constant.Constants;
import com.gome.gmp.common.mybaits.plugins.page.PageModel;
import com.gome.gmp.model.vo.GomeGmpResLogVO;

/**
 * 变更记录
 *
 * @author wubin
 */
@RestController
@RequestMapping("/log")
public class GomeGmpResLogCTL extends BaseRestController {

	@Resource
	private GomeGmpResLogBS gomeGmpResLogBS;

	/**
	 * 列表视图
	 * 
	 * @return
	 */
	@RequestMapping(value = "/{proId}", method = RequestMethod.GET)
	public ModelAndView toFind(Model model,@PathVariable String proId) {
		model.addAttribute(Constants.PROJECT_VIEW_ATTRSTR,Constants.PROJECT_VIEW_PRO);
		return new ModelAndView("/log/findLog");
	}

	/**
	 * 列表视图
	 * 
	 * @return
	 */
	@RequestMapping(value = "/find/{proId}/{pageNum}", method = RequestMethod.GET)
	public PageModel find(@PathVariable String proId, @PathVariable Integer pageNum) {
		BusinessUtil.setPageNum(pageNum);
		GomeGmpResLogVO gomeGmpResLogVO = new GomeGmpResLogVO();
		gomeGmpResLogVO.setProId(proId);
		Page<GomeGmpResLogVO> pageData = gomeGmpResLogBS.findGomeGmpResLog(gomeGmpResLogVO);
		PageModel model = new PageModel();
		model.setPageData(pageData);
		return model;
	}
}
