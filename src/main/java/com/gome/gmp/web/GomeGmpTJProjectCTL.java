package com.gome.gmp.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.gome.framework.base.BaseRestController;
import com.gome.gmp.business.GomeGmpOrgManageBS;
import com.gome.gmp.business.GomeGmpTJProjectBS;
import com.gome.gmp.common.BusinessUtil;
import com.gome.gmp.common.FileExportUtil;
import com.gome.gmp.common.constant.Constants;
import com.gome.gmp.common.filter.CommandContext;
import com.gome.gmp.common.util.DateUtil;
import com.gome.gmp.model.vo.GomeGmpResProjectVO;
import com.gome.gmp.model.vo.GomeGmpTJHourVO;

/**
 * 项目统计
 *
 * @author wubin
 */
@RestController
@RequestMapping("/tj")
public class GomeGmpTJProjectCTL extends BaseRestController {

	private static Logger logger = LoggerFactory.getLogger(GomeGmpTJProjectCTL.class);
	
	@Resource
	private GomeGmpTJProjectBS gomeGmpTjProjectBS;

	@Resource
	private GomeGmpOrgManageBS gomeGmpOrgManageBS;

	/**
	 * 状态分布
	 * 
	 * @return
	 * @author wubin
	 */
	@RequestMapping(value = "/status", method = RequestMethod.GET)
	public ModelAndView toStatus(@ModelAttribute GomeGmpResProjectVO gomeGmpResProjectVO) {
		Date now = new Date();
		Date preWeekNow = DateUtil.addDays(now, -7);
		// 上周的今天
		gomeGmpResProjectVO.setStartCreateTime(preWeekNow);
		// 当天
		gomeGmpResProjectVO.setEndCreateTime(now);
		return new ModelAndView("/tj/status");
	}

	/**
	 * 状态分布
	 * 
	 * @param gomeGmpResProjectVO
	 * @return
	 * @author wubin
	 */
	@RequestMapping(value = "/status/data", method = RequestMethod.POST)
	// @RequestBody ajax 自己定制json时使用，@ModelAttribute 提交表单时使用
	public List<Map<String, Object>> statusData(@RequestBody GomeGmpResProjectVO gomeGmpResProjectVO) {
		// 如果部门全选则显示所有项目 ，并去掉重复查询条件：org
		BusinessUtil.repeatOrg(gomeGmpResProjectVO);
		// 统计数据
		// 只统计任务状态为打开的
		gomeGmpResProjectVO.setTaskStatus(new String[] { "2" });
		getRequest().getSession().setAttribute(Constants.TJ_STATUS_FIND_PARAM, gomeGmpResProjectVO);
		logger.debug("状态分布，查询条件存入session，【"+JSON.toJSONString(gomeGmpResProjectVO)+"】");
		List<Map<String, Object>> result = gomeGmpTjProjectBS.stateDistribution(gomeGmpResProjectVO);
		return result;
	}

	/**
	 * 状态分布
	 * 
	 * @param gomeGmpResProjectVO
	 * @return
	 * @author wubin
	 */
	@RequestMapping(value = "/status/export", method = RequestMethod.GET)
	public void statusExport() {
		// 查询数据
		GomeGmpResProjectVO gomeGmpResProjectVO = (GomeGmpResProjectVO) getRequest().getSession().getAttribute(Constants.TJ_STATUS_FIND_PARAM);
		List<Map<String, Object>> result = gomeGmpTjProjectBS.stateDistribution(gomeGmpResProjectVO);
		// 行转列
		Map<String, Object> convertMap = new HashMap<String, Object>();
		convertMap.put("状态", "操作");
		for (Map<String, Object> map : result) {
			convertMap.put(String.valueOf(map.get("name")), map.get("value"));
		}
		// 标题
		String titleList[] = new String[] { "状态", "未启动", "正常", "提前", "延期风险", "已延期", "已上线", "待上线", "已取消", "内部上线", "暂停", "暂缓", "待排期", "部分上线" };
		// 列表数据
		List<Map<String, Object>> contentList = new ArrayList<Map<String, Object>>();
		Map<String, Object> expMap = new LinkedHashMap<String, Object>();
		for (String title : titleList) {
			Object value = convertMap.get(title);
			expMap.put(title, value == null ? "0" : value);
		}
		contentList.add(expMap);
		// 导出文件
		String title = "状态分布导出";
		// 文件名
		String fileName = "状态分布导出文件";
		// 组织数据
		List<String[]> dataList = new ArrayList<String[]>();
		for (int i = 0; i < contentList.size(); i++) {
			Map<String, Object> expData = contentList.get(i);
			String data[] = new String[titleList.length];
			for (int j = 0; j < titleList.length; j++) {
				data[j] = StringUtils.defaultString(String.valueOf(expData.get(titleList[j])));
			}
			dataList.add(data);
		}
		// 导出
		FileExportUtil.exportDataToExcelXLSX(title, titleList, dataList, CommandContext.getResponse(), fileName, null);
	}

	/**
	 * 项目工时统计
	 * 
	 * @param gomeGmpResProjectVO
	 * @return
	 * @author wubin
	 */
	@RequestMapping(value = "/workHour/{proType}/{proId}", method = RequestMethod.GET)
	public ModelAndView workHour(Model model,@PathVariable Integer proType, @PathVariable String proId) {
		GomeGmpTJHourVO gomeGmpTJHourVO = new GomeGmpTJHourVO();
		gomeGmpTJHourVO.setProId(proId);
		gomeGmpTJHourVO = gomeGmpTjProjectBS.workHour(gomeGmpTJHourVO);
		model.addAttribute(Constants.PROJECT_VIEW_ATTRSTR,Constants.PROJECT_VIEW_DAILY);
		return new ModelAndView("/tj/workHour").addObject(gomeGmpTJHourVO);
	}
}
