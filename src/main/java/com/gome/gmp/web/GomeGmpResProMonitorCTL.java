package com.gome.gmp.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.gome.framework.base.BaseRestController;
import com.gome.gmp.business.GomeGmpOrgManageBS;
import com.gome.gmp.business.GomeGmpResProMonitorBS;
import com.gome.gmp.common.FileExportUtil;
import com.gome.gmp.common.filter.CommandContext;
import com.gome.gmp.common.util.DateUtil;
import com.gome.gmp.model.vo.GomeGmpResProjectVO;

@RestController
@RequestMapping("/proMonitor")
public class GomeGmpResProMonitorCTL extends BaseRestController {

	@Resource
	private GomeGmpResProMonitorBS gmpResProMonitorBS;

	@Resource
	private GomeGmpOrgManageBS gomeGmpOrgManageBS;

	/**
	 * 项目监控列表视图
	 *
	 ***/
	@RequestMapping(value = "/toProMonitor", method = RequestMethod.GET)
	public ModelAndView toProMonitor(@ModelAttribute GomeGmpResProjectVO gomeGmpResProjectVO) {
		gomeGmpResProjectVO = gmpResProMonitorBS.loadQueryMonitorDetailConditions(gomeGmpResProjectVO);
		List<String> result = gmpResProMonitorBS.toYear(gomeGmpResProjectVO);
		gomeGmpResProjectVO.setYears(result);
		return new ModelAndView("/monitor/proMonitor");
	}
	
	
	/**
	 * 更新监控
	 * 
	 * @return String
	 * @author rl_wanglijie
	 */
	@RequestMapping(value = "/toUpeProMonitor", method = RequestMethod.GET)
	public ModelAndView toUpeProMonitor(@ModelAttribute GomeGmpResProjectVO gomeGmpResProjectVO) {
		gomeGmpResProjectVO = gmpResProMonitorBS.loadQueryMonitorDetailConditions(gomeGmpResProjectVO);
		Date now = new Date();
		Date preWeekNow = DateUtil.addDays(now, -6);
		// 上周的今天
		gomeGmpResProjectVO.setStartTime(preWeekNow);
		// 当天
		gomeGmpResProjectVO.setEndTime(now);
		return new ModelAndView("/monitor/updatedMonitor");
	}

	/**
	 * 统计
	 ***/
	@RequestMapping(value = "/findPro", method = RequestMethod.POST)
	public List<Map<String, Object>> findPro(@RequestBody GomeGmpResProjectVO gmpResProjectVO) {
		List<Map<String, Object>> proMonitor = gmpResProMonitorBS.findPro(gmpResProjectVO);
		return proMonitor;
	}

	/**
	 * 近期/新增/即将上线/风险/延期
	 **/
	@RequestMapping(value = "/monitor/{monitorType}", method = RequestMethod.POST)
	public List<Map<String, Object>> monitor(@RequestBody GomeGmpResProjectVO gmpResProjectVO, @PathVariable String monitorType) {
		List<Map<String, Object>> promonitor = gmpResProMonitorBS.monitorData(gmpResProjectVO, monitorType);
		return promonitor;
	}

	/**
	 * 项目监控导出
	 * 
	 * @param gomeGmpResProjectVO
	 */
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public void export(@ModelAttribute GomeGmpResProjectVO gomeGmpResProjectVO) {
		List<Map<String, Object>> list = gmpResProMonitorBS.findPro(gomeGmpResProjectVO);
		String exportFileName = gmpResProMonitorBS.getExportFileName(gomeGmpResProjectVO, "");
		String colnums[] = new String[] { "内容", "全部任务", "近期上线", "即将上线", "风险任务", "已延期任务", "新增任务"};
		List<String[]> dataList = new ArrayList<String[]>();
		if(list!=null && list.size()>0){
			for (Map<String, Object> map : list) {
				String data[] = new String[colnums.length];
				data[0] = StringUtils.defaultString((String)map.get("proType"));
				data[1] = StringUtils.defaultString(((Long)map.get("allPro")).toString());
				data[2] = StringUtils.defaultString(((Long)map.get("nearOnline")).toString());
				data[3] = StringUtils.defaultString(((Long)map.get("soonOnline")).toString());
				data[4] = StringUtils.defaultString(((Long)map.get("riskPro")).toString());
				data[5] = StringUtils.defaultString(((Long)map.get("delayPro")).toString());
				data[6] = StringUtils.defaultString(((Long)map.get("newlyPro")).toString());
				dataList.add(data);
			}
		}
		FileExportUtil.exportDataToExcelXLSX(exportFileName,colnums,dataList,CommandContext.getResponse(),exportFileName, null);
		
	}

	/**
	 * 项目监控明细导出
	 * 
	 * @param gmpResProjectVO
	 * @param monitorType
	 */
	@RequestMapping(value = "/detailExport/{monitorType}", method = RequestMethod.GET)
	public void detailExport(@ModelAttribute GomeGmpResProjectVO gmpResProjectVO, @PathVariable String monitorType) {
		List<Map<String, Object>> list = gmpResProMonitorBS.monitorData(gmpResProjectVO, monitorType);
		String exportFileName = gmpResProMonitorBS.getExportFileName(gmpResProjectVO, monitorType);
		String colnums[] = new String[] { "项目ID", "项目名称", "任务状态", "优先级", "状态进度", "实施阶段", "完成度", "上线日期", "项目经理" };
		List<String[]> dataList = new ArrayList<String[]>();
		if(list!=null && list.size()>0){
			for (Map<String, Object> map : list) {
				String data[] = new String[colnums.length];
				data[0] = StringUtils.defaultString((String)map.get("pro_id"));
				data[1] = StringUtils.defaultString((String)map.get("title"));
				data[2] = StringUtils.defaultString((String)map.get("status_name"));
				data[3] = StringUtils.defaultString((String)map.get("priority_name"));
				data[4] = StringUtils.defaultString((String)map.get("schedule_name"));
				data[5] = StringUtils.defaultString((String)map.get("actualize_name"));
				data[6] = StringUtils.defaultString((String)map.get("percentage"));
				data[7] = StringUtils.defaultString(map.get("plan_time") == null ? "" : DateUtil.getDate((Date)map.get("plan_time")));
				data[8] = StringUtils.defaultString((String)map.get("bp_name"));
				dataList.add(data);
			}
		}
		FileExportUtil.exportDataToExcelXLSX(exportFileName,colnums,dataList,CommandContext.getResponse(),exportFileName, null);
	}
}
