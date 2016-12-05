package com.gome.gmp.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.gome.framework.base.BaseRestController;
import com.gome.gmp.business.GomeGmpOrgManageBS;
import com.gome.gmp.business.GomeGmpResHoursCountBS;
import com.gome.gmp.common.BusinessUtil;
import com.gome.gmp.common.FileExportUtil;
import com.gome.gmp.common.util.DateUtil;
import com.gome.gmp.model.bo.GomeGmpResUserBO;
import com.gome.gmp.model.vo.GomeGmpResOrgVO;
import com.gome.gmp.model.vo.GomeGmpTJCountVO;

@RestController
@RequestMapping("/hourCount")
public class GomeGmpHourCountCTL extends BaseRestController {

	@Resource
	private GomeGmpResHoursCountBS gomeGmpResHoursCountBS;
	
	@Resource
	private GomeGmpOrgManageBS gomeGmpOrgManageBS;

	@SuppressWarnings("unchecked")
	@RequestMapping("/scheduleStatistics")
	public ModelAndView scheduleStatistics(Model model) {
		GomeGmpResUserBO userBo = BusinessUtil.getLoginUser(getRequest());
		Map<String, Object> userOrgInfoMap = gomeGmpOrgManageBS.getUserOrgInfo(userBo);
		model.addAttribute("hasUserOrg",userBo.getOrgId());
		model.addAttribute("userPermission",userBo.getAuthority());
		model.addAttribute("userOrgList",(List<GomeGmpResOrgVO>) userOrgInfoMap.get("userOrgList"));
		model.addAttribute("userOrgLevel",(Integer) userOrgInfoMap.get("userOrgLevel"));
		
		Date now = new Date();
		Date preWeekNow = DateUtil.addDays(now, -6);
		model.addAttribute("startTime", preWeekNow);
		model.addAttribute("endTime", now);
		ModelAndView modelView = new ModelAndView("/scheduleStatistics/scheduleStatistics");
		modelView.addObject("pageType", "1");
		return modelView;
	}
 
	@RequestMapping("/scheduleStatisticsSub")
	public ModelAndView scheduleStatisticsSub(@RequestParam String orgId,Model model) {
		ModelAndView modelView = new ModelAndView("/scheduleStatistics/scheduleStatisticsSub");
		String pageType = "2";
		if(orgId.length() == 6)
		{
			pageType = "3";
		}
		modelView.addObject("pageType", pageType);
		GomeGmpResUserBO userBo = BusinessUtil.getLoginUser(getRequest());
		Map<String, Object> userOrgInfoMap = gomeGmpOrgManageBS.getUserOrgInfo(userBo);
		model.addAttribute("userOrgLevel",(Integer) userOrgInfoMap.get("userOrgLevel"));
		return modelView;
	}

	@RequestMapping("/scheduleStatisticsMember")
	public ModelAndView scheduleStatisticsMember(Model model) {
		GomeGmpResUserBO userBo = BusinessUtil.getLoginUser(getRequest());
		Map<String, Object> userOrgInfoMap = gomeGmpOrgManageBS.getUserOrgInfo(userBo);
		model.addAttribute("userOrgLevel",(Integer) userOrgInfoMap.get("userOrgLevel"));
		return new ModelAndView("/scheduleStatistics/scheduleStatisticsMember");
	}

	/**
	 * 部门工时统计
	 ***/
	@RequestMapping(value = "/orgHourCount", method = RequestMethod.POST, produces = "application/json")
	public List<GomeGmpResOrgVO> findOrgHourCount(@RequestBody GomeGmpTJCountVO gomeGmpTJCountVO) {
		BusinessUtil.repeatOrg(gomeGmpTJCountVO, false);
		List<GomeGmpResOrgVO> groupBeen = gomeGmpResHoursCountBS.findGomeGmpResOrgHoursCountBOById(gomeGmpTJCountVO);
		return groupBeen;
	}
	
	/**
	 * 子部门工时统计
	 ***/
	@RequestMapping(value = "/subOrgHourCount", method = RequestMethod.POST, produces = "application/json")
	public List<GomeGmpResOrgVO> findSubOrgHourCount(@RequestBody GomeGmpTJCountVO gomeGmpTJCountVO) {
		BusinessUtil.repeatOrg(gomeGmpTJCountVO, false);
		gomeGmpTJCountVO.setSubOrg(true);
		List<GomeGmpResOrgVO> groupBeen = gomeGmpResHoursCountBS.findGomeGmpResOrgHoursCountBOById(gomeGmpTJCountVO);
		return groupBeen;
	}

	/**
	 * 人员工时统计
	 ***/
	@RequestMapping(value = "/memberHourCount", method = RequestMethod.POST, produces = "application/json")
	public List<GomeGmpResOrgVO> findUserHourCount(@RequestBody GomeGmpTJCountVO gomeGmpTJCountVO) {
		gomeGmpTJCountVO.setTeamId(gomeGmpTJCountVO.getOrgIds()[0]);
		List<GomeGmpResOrgVO> groupBeen = gomeGmpResHoursCountBS.findGomeGmpResUserHoursCountBOById(gomeGmpTJCountVO);
		return groupBeen;
	}

	/**
	 * 导出数据
	 ***/
	@RequestMapping(value = "exportData", method = RequestMethod.GET)
	public void exportData(HttpServletRequest request, HttpServletResponse response, @ModelAttribute GomeGmpTJCountVO gomeGmpTJCountVO) {
		BusinessUtil.repeatOrg(gomeGmpTJCountVO, false);
		if (gomeGmpTJCountVO.getOrgIds() == null) {
			gomeGmpTJCountVO.setLevel(0);
		} else {
			String[] orgIdList = gomeGmpTJCountVO.getOrgIds();
			if (orgIdList.length > 0) {
				gomeGmpTJCountVO.setLevel(orgIdList[0].length());
			} else {
				gomeGmpTJCountVO.setLevel(0);
			}
		}
		List<GomeGmpResOrgVO> groupBeen = gomeGmpResHoursCountBS.findGomeGmpResOrgHoursCountBOById(gomeGmpTJCountVO);
		List<String> title = new ArrayList<String>();
		title.add("部门");
		title.add("计划总工时");
		title.add("实际总工时");
		title.add("实际/计划");
		title.add("部门人数");
		title.add("日平均工时");
		Date now = new Date();
		String fileName = "hourCount";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		fileName = fileName + sdf.format(now) + ".xls";
		List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < groupBeen.size(); i++) {
			GomeGmpResOrgVO data = groupBeen.get(i);
			Map<String, Object> newData = new LinkedHashMap<String, Object>();
			newData.put("org_name", data.getOrgName());
			newData.put("plan_hours_count", data.getPlanHoursCount());
			newData.put("hours_count", data.getHoursCount());
			newData.put("percent", data.getPercent()+"%");
			newData.put("member_count", data.getMemberCount());
			newData.put("avg_hour", data.getAvgHour());
			exportData.add(newData);
		}
		FileExportUtil.getExportExcel(fileName, title, exportData);
	}
}
