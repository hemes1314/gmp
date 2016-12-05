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

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.gome.framework.base.BaseRestController;
import com.gome.gmp.business.GomeGmpDictItemBS;
import com.gome.gmp.business.GomeGmpResUpdatedMonitorBS;
import com.gome.gmp.common.BusinessUtil;
import com.gome.gmp.common.FileExportUtil;
import com.gome.gmp.common.mybaits.plugins.page.PageModel;
import com.gome.gmp.model.vo.GomeGmpResProjectVO;

@RestController
@RequestMapping("/monitor")
public class GomeGmpResUpdatedMonitorCTL extends BaseRestController {

	@Resource
	private GomeGmpResUpdatedMonitorBS gmpResUpdatedMonitorBS;

	@Resource
	private GomeGmpDictItemBS gomeGmpDictItemBS;

	
	/**
	 *
	 ***/
	@RequestMapping(value = "getUpdateCountList", method = RequestMethod.POST)
	public List<Map<String, Object>> findUpdateCount(@RequestBody GomeGmpResProjectVO gomeGmpResProjectVO) {
		// 去掉重复查询条件：org
		BusinessUtil.repeatOrg(gomeGmpResProjectVO, false);
		if (gomeGmpResProjectVO.getOrgIds() == null || gomeGmpResProjectVO.getOrgIds().length == 0) {
			return null;
		}
		List<Map<String, Object>> result = gmpResUpdatedMonitorBS.getGomeGmpResUpdatedCountBO(gomeGmpResProjectVO);
		Integer updateCount = 0;
		Integer noUpdatedCount = 0;
		Float updatePct = (float) 0;
		Integer updatedPauseCount = 0;
		Integer updatedCloseCount = 0;
		if (result.size() > 0) {
			for (int i = 0; i < result.size(); i++) {
				Map<String, Object> row = result.get(i);
				updateCount += Integer.valueOf(row.get("updated_count").toString());
				noUpdatedCount += Integer.valueOf(row.get("no_updated_count").toString());
				updatedPauseCount += Integer.valueOf(row.get("updated_pause_count").toString());
				updatedCloseCount += Integer.valueOf(row.get("updated_close_count").toString());
			}
			int tempCount = updateCount + noUpdatedCount;
			if (tempCount == 0) {
				updatePct = (float) 0;
			} else {
				float tempUpdatePct = (float) updateCount / (float) tempCount * 100;
				updatePct = (float) Math.rint(tempUpdatePct);
			}
			Map<String, Object> total = new HashMap<String, Object>();
			total.put("org_id", "");
			total.put("org_name", "总计");
			total.put("updated_count", updateCount);
			total.put("no_updated_count", noUpdatedCount);
			total.put("updated_pause_count", updatedPauseCount);
			total.put("updated_close_count", updatedCloseCount);
			total.put("update_pct", updatePct);
			result.add(total);
		}
		return result;
	}

	/**
	 *
	 ***/
	@RequestMapping(value = "getUpdatedList/{pageNum}", method = RequestMethod.POST)
	public PageModel findUpdatedProjects(@PathVariable Integer pageNum, @RequestBody GomeGmpResProjectVO gomeGmpResProjectVO) {
		BusinessUtil.setPageNum(pageNum);
		Page<Map<String, Object>> result = gmpResUpdatedMonitorBS.getUpdatedProjectsById(gomeGmpResProjectVO);
		PageModel model = new PageModel();
		model.setPageData(result);
		return model;
	}

	/**
	 *
	 ***/
	@RequestMapping(value = "getNoUpdatedList/{pageNum}", method = RequestMethod.POST)
	public PageModel findNoUpdatedProjects(@PathVariable Integer pageNum, @RequestBody GomeGmpResProjectVO gomeGmpResProjectVO) {
		BusinessUtil.setPageNum(pageNum);
		Page<Map<String, Object>> result = gmpResUpdatedMonitorBS.getNoUpdatedProjectsById(gomeGmpResProjectVO);
		PageModel model = new PageModel();
		model.setPageData(result);
		return model;
	}

	/**
	 *
	 ***/
	@RequestMapping(value = "getPauseList/{pageNum}", method = RequestMethod.POST)
	public PageModel findPauseProjects(@PathVariable Integer pageNum, @RequestBody GomeGmpResProjectVO gomeGmpResProjectVO) {
		BusinessUtil.setPageNum(pageNum);
		Page<Map<String, Object>> result = gmpResUpdatedMonitorBS.getPauseProjectsById(gomeGmpResProjectVO);
		PageModel model = new PageModel();
		model.setPageData(result);
		return model;
	}

	/**
	 *
	 ***/
	@RequestMapping(value = "getCloseList/{pageNum}", method = RequestMethod.POST)
	public PageModel findCloseProjects(@PathVariable Integer pageNum, @RequestBody GomeGmpResProjectVO gomeGmpResProjectVO) {
		BusinessUtil.setPageNum(pageNum);
		Page<Map<String, Object>> result = gmpResUpdatedMonitorBS.getCloseProjectsById(gomeGmpResProjectVO);
		PageModel model = new PageModel();
		model.setPageData(result);
		return model;
	}

	/**
	 *
	 ***/
	@RequestMapping(value = "exportData", method = RequestMethod.GET)
	public void exportData(HttpServletRequest request, HttpServletResponse response, @ModelAttribute GomeGmpResProjectVO gomeGmpResProjectVO) {
		// 去掉重复查询条件：org
		BusinessUtil.repeatOrg(gomeGmpResProjectVO, false);
		List<Map<String, Object>> result = gmpResUpdatedMonitorBS.getGomeGmpResUpdatedCountBO(gomeGmpResProjectVO);
		Integer updateCount = 0;
		Integer noUpdatedCount = 0;
		Float updatePct = (float) 0;
		Integer updatedPauseCount = 0;
		Integer updatedCloseCount = 0;
		if (result.size() > 0) {
			for (int i = 0; i < result.size(); i++) {
				Map<String, Object> row = result.get(i);
				updateCount += Integer.valueOf(row.get("updated_count").toString());
				noUpdatedCount += Integer.valueOf(row.get("no_updated_count").toString());
				updatedPauseCount += Integer.valueOf(row.get("updated_pause_count").toString());
				updatedCloseCount += Integer.valueOf(row.get("updated_close_count").toString());
			}
			int tempCount = updateCount + noUpdatedCount + updatedPauseCount + updatedCloseCount;
			if (tempCount == 0) {
				updatePct = (float) 0;
			} else {
				float tempUpdatePct = (float) updateCount / (float) tempCount * 100;
				updatePct = (float) Math.rint(tempUpdatePct);
			}
			Map<String, Object> total = new LinkedHashMap<String, Object>();
			total.put("org_id", "");
			total.put("org_name", "总计");
			total.put("no_updated_count", noUpdatedCount);
			total.put("updated_count", updateCount);
			total.put("update_pct", updatePct);
			total.put("updated_pause_count", updatedPauseCount);
			total.put("updated_close_count", updatedCloseCount);
			result.add(total);
			List<String> title = new ArrayList<String>();
			title.add("部门");
			title.add("未更新");
			title.add("已更新");
			title.add("更新占比");
			title.add("暂停项目");
			title.add("关闭项目");
			title.add("更新排名");
			Date now = new Date();
			String fileName = "updatedMonitor";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			fileName = fileName + sdf.format(now) + ".xls";
			List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < result.size(); i++) {
				Map<String, Object> data = result.get(i);
				data.remove("org_id");
				Map<String, Object> newData = new LinkedHashMap<String, Object>();
				newData.put("org_name", data.get("org_name"));
				newData.put("no_updated_count", data.get("no_updated_count"));
				newData.put("updated_count", data.get("updated_count"));
				newData.put("update_pct", Double.valueOf(data.get("update_pct").toString()).intValue() + "%");
				newData.put("updated_pause_count", data.get("updated_pause_count"));
				newData.put("updated_close_count", data.get("updated_close_count"));
				if (i == result.size() - 1) {
					newData.put("sort", "");
				} else {
					newData.put("sort", "" + (i + 1));
				}
				exportData.add(newData);
			}
			FileExportUtil.getExportExcel(fileName, title, exportData);
		} else {
			List<String> title = new ArrayList<String>();
			title.add("部门");
			title.add("未更新");
			title.add("已更新");
			title.add("更新占比");
			title.add("暂停项目");
			title.add("关闭项目");
			title.add("更新排名");
			Date now = new Date();
			String fileName = "updatedMonitor";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			fileName = fileName + sdf.format(now) + ".xls";
			List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
			FileExportUtil.getExportExcel(fileName, title, exportData);
		}
	}
}
