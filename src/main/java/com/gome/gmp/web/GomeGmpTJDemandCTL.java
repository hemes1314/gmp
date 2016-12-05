package com.gome.gmp.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.Page;
import com.gome.framework.base.BaseRestController;
import com.gome.gmp.business.GomeGmpTJDemandBS;
import com.gome.gmp.common.BusinessUtil;
import com.gome.gmp.common.FileExportUtil;
import com.gome.gmp.common.filter.CommandContext;
import com.gome.gmp.common.mybaits.plugins.page.PageModel;
import com.gome.gmp.common.util.DateUtil;
import com.gome.gmp.model.vo.GomeGmpResNeedVO;
import com.gome.gmp.model.vo.GomeGmpTJDemandVO;

@RestController
@RequestMapping("/datas")
public class GomeGmpTJDemandCTL extends BaseRestController {

	@Resource
	private GomeGmpTJDemandBS gomeGmpResDataBS;

	/**
	 * 提报需求统计列表
	 **/
	@RequestMapping(value = "/tjDemand", method = RequestMethod.POST)
	public List<Map<String, Object>> tjDemand(@RequestBody GomeGmpResNeedVO gomeGmpResNeedVO) {
		List<Map<String, Object>> bo = gomeGmpResDataBS.tjDemand(gomeGmpResNeedVO);
		return bo;
	}

	/**
	 * 跳转提报需求部门视图
	 **/
	@RequestMapping(value = "/todataList", method = RequestMethod.GET)
	public ModelAndView todataList(@ModelAttribute GomeGmpResNeedVO gomeGmpResNeedVO) {
		// 自然月
		String[] monthFL = DateUtil.getMonthFirstAndLast(DateUtil.getDateTime());
		gomeGmpResNeedVO.setStartTimes(monthFL[0]);
		gomeGmpResNeedVO.setEndTimes(monthFL[1]);
		return new ModelAndView("/data/commitNeed");
	}

	/**
	 * 导出当前数据
	 **/
	@RequestMapping(value = "/exportD", method = RequestMethod.GET)
	public void exportD(HttpServletRequest request, HttpServletResponse response, @ModelAttribute GomeGmpResNeedVO gomeGmpResNeedVO) {
		List<Map<String, Object>> list = gomeGmpResDataBS.tjDemand(gomeGmpResNeedVO);
		List<String> title = new ArrayList<String>();
		title.add("部门");
		title.add("提报需求");
		title.add("已受理需求");
		title.add("已拒绝需求");
		title.add("待受理需求");
		title.add("已完成需求");
		title.add("月内上线需求");
		for (Map<String, Object> data : list) {
			data.remove("orgId");
		}
		FileExportUtil.getExportExcel("commitDemand.xls", title, list);
	}

	/**
	 * 根据组织统计 查看 orgId
	 **/
	@RequestMapping(value = "/tjPeopleInfo", method = RequestMethod.POST)
	public List<Map<String, Object>> tjPeopleInfo(@RequestBody GomeGmpResNeedVO gomeGmpResNeedVO) {
		List<Map<String, Object>> bo = gomeGmpResDataBS.tjPeopleInfo(gomeGmpResNeedVO);
		return bo;
	}

	/**
	 * 跳转当前部门下提报人视图
	 **/
	@RequestMapping(value = "/toPeopleList", method = RequestMethod.GET)
	public ModelAndView toPeopleList(@ModelAttribute GomeGmpResNeedVO gomeGmpResNeedVO) {
		return new ModelAndView("/data/commitPeople");
	}

	/**
	 * 根据提报人查看对应的 需求列表
	 **/
	@RequestMapping(value = "/tjPeopleList/{pageNum}", method = RequestMethod.POST)
	public PageModel tjPeopleList(@ModelAttribute GomeGmpResNeedVO gomeGmpResNeedVO, @PathVariable Integer pageNum) {
		BusinessUtil.setPageNum(pageNum);
		Page<GomeGmpResNeedVO> pageData = gomeGmpResDataBS.tjPeopleList(gomeGmpResNeedVO);
		PageModel model = new PageModel();
		model.setPageData(pageData);
		return model;
	}

	/**
	 * 跳转提报需求视图
	 **/
	@RequestMapping(value = "/toNeedList", method = RequestMethod.GET)
	public ModelAndView toNeedList(@ModelAttribute GomeGmpResNeedVO gomeGmpResNeedVO) {
		return new ModelAndView("/data/findReadNeed");
	}

	/**
	 * 年度项目统计
	 * 
	 */
	@RequestMapping(value = "/proTJ", method = RequestMethod.POST)
	public List<Map<String, Object>> proTJ(@RequestBody GomeGmpTJDemandVO gomeGmpTJDemandVO) {
		List<Map<String, Object>> result = gomeGmpResDataBS.proTJ(gomeGmpTJDemandVO);
		return result;
	}

	/**
	 * 年度项目视图
	 **/
	@RequestMapping(value = "/toProTJ", method = RequestMethod.GET)
	public ModelAndView toProTJ(@ModelAttribute GomeGmpTJDemandVO gomeGmpTJDemandVO) {
		List<String> result = gomeGmpResDataBS.toProTJ(gomeGmpTJDemandVO);
		gomeGmpTJDemandVO.setYears(result);
		return new ModelAndView("/data/totalDate");
	}

	/**
	 * 年度项目项
	 **/
	@RequestMapping(value = "/proTJNum/{pageNum}", method = RequestMethod.POST)
	public PageModel proTJNum(@RequestBody GomeGmpTJDemandVO gomeGmpTJDemandVO, @PathVariable Integer pageNum) {
		BusinessUtil.setPageNum(pageNum);
		Page<GomeGmpTJDemandVO> pageData = gomeGmpResDataBS.proTJNum(gomeGmpTJDemandVO);
		PageModel model = new PageModel();
		model.setPageData(pageData);
		return model;
	}

	/**
	 * 项目进行中项
	 **/
	@RequestMapping(value = "/jxzTJNum/{pageNum}", method = RequestMethod.POST)
	public PageModel jxzTJNum(@RequestBody GomeGmpTJDemandVO gomeGmpTJDemandVO, @PathVariable Integer pageNum) {
		BusinessUtil.setPageNum(pageNum);
		Page<GomeGmpTJDemandVO> pageData = gomeGmpResDataBS.jxzTJNum(gomeGmpTJDemandVO);
		PageModel model = new PageModel();
		model.setPageData(pageData);
		return model;
	}

	/**
	 * 项目已上线，暂停，未启动，风 险、延期项
	 **/
	@RequestMapping(value = "/ztTJNum/{pageNum}", method = RequestMethod.POST)
	public PageModel ztTJNum(@RequestBody GomeGmpTJDemandVO gomeGmpTJDemandVO, @PathVariable Integer pageNum) {
		BusinessUtil.setPageNum(pageNum);
		Page<GomeGmpTJDemandVO> pageData = gomeGmpResDataBS.ztTJNum(gomeGmpTJDemandVO);
		PageModel model = new PageModel();
		model.setPageData(pageData);
		return model;
	}

	/**
	 * 本周项目统计
	 * 
	 */
	@RequestMapping(value = "/bzProTJ", method = RequestMethod.POST)
	public List<Map<String, Object>> bzProTJ(@RequestBody GomeGmpTJDemandVO gomeGmpTJDemandVO) {
		List<Map<String, Object>> result = gomeGmpResDataBS.bzProTJ(gomeGmpTJDemandVO);
		return result;
	}

	/**
	 * 本周项目已上线,暂停项
	 * 
	 */
	@RequestMapping(value = "/bzProTJNum/{pageNum}", method = RequestMethod.POST)
	public PageModel bzProTJNum(@RequestBody GomeGmpTJDemandVO gomeGmpTJDemandVO, @PathVariable Integer pageNum) {
		BusinessUtil.setPageNum(pageNum);
		Page<GomeGmpTJDemandVO> pageData = gomeGmpResDataBS.bzProTJNum(gomeGmpTJDemandVO);
		PageModel model = new PageModel();
		model.setPageData(pageData);
		return model;
	}

	/**
	 * 本周进行中
	 **/
	@RequestMapping(value = "/bzjxzTJNum/{pageNum}", method = RequestMethod.POST)
	public PageModel bzjxzTJNum(@RequestBody GomeGmpTJDemandVO gomeGmpTJDemandVO, @PathVariable Integer pageNum) {
		BusinessUtil.setPageNum(pageNum);
		Page<GomeGmpTJDemandVO> pageData = gomeGmpResDataBS.bzjxzTJNum(gomeGmpTJDemandVO);
		PageModel model = new PageModel();
		model.setPageData(pageData);
		return model;
	}

	/**
	 * 本周新增
	 **/
	@RequestMapping(value = "/bzXzTJNum/{pageNum}", method = RequestMethod.POST)
	public PageModel bzXzTJNum(@RequestBody GomeGmpTJDemandVO gomeGmpTJDemandVO, @PathVariable Integer pageNum) {
		BusinessUtil.setPageNum(pageNum);
		Page<GomeGmpTJDemandVO> pageData = gomeGmpResDataBS.bzXzTJNum(gomeGmpTJDemandVO);
		PageModel model = new PageModel();
		model.setPageData(pageData);
		return model;
	}

	/**
	 * 本周风险
	 **/
	@RequestMapping(value = "/bzFxTJNum/{pageNum}", method = RequestMethod.POST)
	public PageModel bzFxTJNum(@RequestBody GomeGmpTJDemandVO gomeGmpTJDemandVO, @PathVariable Integer pageNum) {
		BusinessUtil.setPageNum(pageNum);
		Page<GomeGmpTJDemandVO> pageData = gomeGmpResDataBS.bzFxTJNum(gomeGmpTJDemandVO);
		PageModel model = new PageModel();
		model.setPageData(pageData);
		return model;
	}

	/**
	 * 下周计划上线
	 **/
	@RequestMapping(value = "/xzJhsxTJNum/{pageNum}", method = RequestMethod.POST)
	public PageModel xzJhsxTJNum(@RequestBody GomeGmpTJDemandVO gomeGmpTJDemandVO, @PathVariable Integer pageNum) {
		BusinessUtil.setPageNum(pageNum);
		Page<GomeGmpTJDemandVO> pageData = gomeGmpResDataBS.xzJhsxTJNum(gomeGmpTJDemandVO);
		PageModel model = new PageModel();
		model.setPageData(pageData);
		return model;
	}

	/**
	 * 项目状态分布
	 * 
	 */
	@RequestMapping(value = "/ztProTJ", method = RequestMethod.POST)
	public List<Map<String, Object>> ztProTJ(@RequestBody GomeGmpTJDemandVO gomeGmpTJDemandVO) {
		List<Map<String, Object>> result = gomeGmpResDataBS.ztProTJ(gomeGmpTJDemandVO);
		return result;
	}

	/**
	 * 项目一周状态分布项
	 * 
	 */
	@RequestMapping(value = "/bzZtTJNum/{pageNum}", method = RequestMethod.POST)
	public PageModel bzZtTJNum(@RequestBody GomeGmpTJDemandVO gomeGmpTJDemandVO, @PathVariable Integer pageNum) {
		BusinessUtil.setPageNum(pageNum);
		Page<GomeGmpTJDemandVO> pageData = gomeGmpResDataBS.bzZtTJNum(gomeGmpTJDemandVO);
		PageModel model = new PageModel();
		model.setPageData(pageData);
		return model;
	}

	/**
	 * 部门数据统计
	 *
	 */
	@RequestMapping(value = "/unitProTJ", method = RequestMethod.POST)
	public List<Map<String, Object>> unitProTJ(@RequestBody GomeGmpTJDemandVO gomeGmpTJDemandVO) {
		List<Map<String, Object>> result = gomeGmpResDataBS.unitProTJ(gomeGmpTJDemandVO);
		return result;
	}

	/**
	 * 部门进行中项
	 *
	 */
	@RequestMapping(value = "/unitJxzTJNum/{pageNum}", method = RequestMethod.POST)
	public PageModel unitJxzTJNum(@RequestBody GomeGmpTJDemandVO gomeGmpTJDemandVO, @PathVariable Integer pageNum) {
		BusinessUtil.setPageNum(pageNum);
		Page<GomeGmpTJDemandVO> pageData = gomeGmpResDataBS.unitJxzTJNum(gomeGmpTJDemandVO);
		PageModel model = new PageModel();
		model.setPageData(pageData);
		return model;
	}

	/**
	 * 部门已上线项
	 *
	 */
	@RequestMapping(value = "/unitYsxTJNum/{pageNum}", method = RequestMethod.POST)
	public PageModel unitYsxTJNum(@RequestBody GomeGmpTJDemandVO gomeGmpTJDemandVO, @PathVariable Integer pageNum) {
		BusinessUtil.setPageNum(pageNum);
		Page<GomeGmpTJDemandVO> pageData = gomeGmpResDataBS.unitYsxTJNum(gomeGmpTJDemandVO);
		PageModel model = new PageModel();
		model.setPageData(pageData);
		return model;
	}

	/**
	 * 项目风 险、延期项
	 **/
	@RequestMapping(value = "/unitztTJNum/{pageNum}", method = RequestMethod.POST)
	public PageModel unitztTJNum(@RequestBody GomeGmpTJDemandVO gomeGmpTJDemandVO, @PathVariable Integer pageNum) {
		BusinessUtil.setPageNum(pageNum);
		Page<GomeGmpTJDemandVO> pageData = gomeGmpResDataBS.unitztTJNum(gomeGmpTJDemandVO);
		PageModel model = new PageModel();
		model.setPageData(pageData);
		return model;
	}

	/**
	 * 上线分布统计
	 ***/
	@RequestMapping(value = "/onlineTJ", method = RequestMethod.POST)
	public List<Map<String, Object>> onlineTJ(@RequestBody GomeGmpTJDemandVO gomeGmpTJDemandVO) {
		List<Map<String, Object>> result = gomeGmpResDataBS.onlineTJ(gomeGmpTJDemandVO);
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		@SuppressWarnings("unused")
		int resultCount = result.size();
		int start = 0;
		for (int i = 0; i < result.size(); i++) {
			Map<String, Object> data = result.get(i);
			String[] onlineMonth = data.get("onlineMonth").toString().split("-");
			int startMonth = Integer.valueOf(onlineMonth[1]);
			for (int j = start + 1;  j < startMonth; j++) {
				Map<String, Object> newData = new HashMap<String, Object>();
				String month = j > 9 ? "" + j : "0" + j;
				newData.put("onlineMonth", gomeGmpTJDemandVO.getYear() + "-" + month);
				newData.put("cnt", "0");
				newData.put("onlineYear", gomeGmpTJDemandVO.getYear());
				returnList.add(newData);
			}
			returnList.add(data);
			start = startMonth;
		}
		for (int i = start + 1; i <= 12; i++) {
			Map<String, Object> data = new HashMap<String, Object>();
			String month = i > 9 ? "" + i : "0" + i;
			data.put("onlineMonth", gomeGmpTJDemandVO.getYear() + "-" + month);
			data.put("cnt", "0");
			data.put("onlineYear", gomeGmpTJDemandVO.getYear());
			returnList.add(data);
		}
		return returnList;
	}

	/**
	 * 上线分布视图
	 **/
	@RequestMapping(value = "/toOnlineTJ", method = RequestMethod.GET)
	public ModelAndView toOnlineTJ(@ModelAttribute GomeGmpTJDemandVO gomeGmpTJDemandVO) {
		List<String> result = gomeGmpResDataBS.onlineYear(gomeGmpTJDemandVO);
		gomeGmpTJDemandVO.setYears(result);
		return new ModelAndView("/data/onLineData");
	}

	/**
	 * 导出上线数据
	 **/
	@RequestMapping(value = "/onlineTJExport", method = RequestMethod.GET)
	public void onLineExport(HttpServletRequest request, HttpServletResponse response, @ModelAttribute GomeGmpTJDemandVO gomeGmpTJDemandVO) {
		List<Map<String, Object>> list = gomeGmpResDataBS.onlineTJExport(gomeGmpTJDemandVO);
		// 行转列
		Map<String, Object> convertMap = new HashMap<String, Object>();
		convertMap.put("月份", "数量");
		for (Map<String, Object> map : list) {
			convertMap.put(String.valueOf(map.get("onlineMonth")), map.get("cnt"));
		}
		// 标题
		String titleList[] = new String[] { "月份", "01月", "02月", "03月", "04月", "05月", "06月", "07月", "08月", "09月", "10月", "11月", "12月" };
		// 列表数据
		List<Map<String, Object>> contentList = new ArrayList<Map<String, Object>>();
		Map<String, Object> expMap = new LinkedHashMap<String, Object>();
		for (String title : titleList) {
			Object value = convertMap.get(title);
			expMap.put(title, value == null ? "0" : value);
		}
		contentList.add(expMap);
		// 导出文件
		String title = "上线分布导出";
		// 文件名
		String fileName = "上线分布导出导出文件" + "_" + new SimpleDateFormat("yyyy-MM-dd HHmmss").format(new Date());
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
	 * 上线统计分布项
	 **/
	@RequestMapping(value = "/onlineTJNum/{pageNum}", method = RequestMethod.POST)
	public PageModel onlineTJNum(@RequestBody GomeGmpTJDemandVO gomeGmpTJDemandVO, @PathVariable Integer pageNum) {
		BusinessUtil.setPageNum(pageNum);
		Page<GomeGmpTJDemandVO> pageData = gomeGmpResDataBS.onlineTJNum(gomeGmpTJDemandVO);
		PageModel model = new PageModel();
		model.setPageData(pageData);
		return model;
	}

	/**
	 * 查询项目列表
	 **/
	@RequestMapping(value = "/toUnit", method = RequestMethod.GET)
	public ModelAndView toUnit(@ModelAttribute GomeGmpTJDemandVO gomeGmpTJDemandVO) {
		return new ModelAndView("/data/findReadOnly");
	}

	/**
	 * 部门视图
	 **/
	@RequestMapping(value = "/toUnitTJ", method = RequestMethod.GET)
	public ModelAndView toUnitTJ(@ModelAttribute GomeGmpTJDemandVO gomeGmpTJDemandVO) {
		Date now = new Date();
		Date preWeekNow = DateUtil.addDays(now, -6);
		// 上周的今天
		gomeGmpTJDemandVO.setStartTime(preWeekNow);
		// 当天
		gomeGmpTJDemandVO.setEndTime(now);
		return new ModelAndView("/data/unitTJData");
	}

	/**
	 * 各部门数据统计
	 * 
	 **/
	@RequestMapping(value = "/unitTJ", method = RequestMethod.POST)
	public List<Map<String, Object>> unitTJ(@RequestBody GomeGmpTJDemandVO gomeGmpTJDemandVO) {
		List<Map<String, Object>> result = gomeGmpResDataBS.unitTJ(gomeGmpTJDemandVO);
		return result;
	}

	/**
	 * 各部门数据统计项
	 * 
	 **/
	@RequestMapping(value = "/unitTJNum/{pageNum}", method = RequestMethod.POST)
	public PageModel unitTJNum(@RequestBody GomeGmpTJDemandVO gomeGmpTJDemandVO, @PathVariable Integer pageNum) {
		BusinessUtil.setPageNum(pageNum);
		Page<GomeGmpTJDemandVO> pageData = gomeGmpResDataBS.unitTJNum(gomeGmpTJDemandVO);
		PageModel model = new PageModel();
		model.setPageData(pageData);
		return model;
	}
}
