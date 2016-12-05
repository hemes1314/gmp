package com.gome.gmp.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.gome.framework.base.BaseRestController;
import com.gome.gmp.business.GomeGmpOrgManageBS;
import com.gome.gmp.business.GomeGmpResProCalendarBS;
import com.gome.gmp.common.BusinessUtil;
import com.gome.gmp.common.constant.Constants;
import com.gome.gmp.model.bo.GomeGmpResOrgBO;
import com.gome.gmp.model.bo.GomeGmpResUserBO;
import com.gome.gmp.model.vo.GomeGmpResOrgVO;
import com.gome.gmp.model.vo.GomeGmpTJCalendarVO;

/**
 * 日历管理
 * 
 * @author rl_wanglijie
 */
@RestController
@RequestMapping("/calendar")
public class GomeGmpResProCalendarCTL extends BaseRestController {

	@Resource
	private GomeGmpResProCalendarBS gomeGmpResProCalendarBS;
	
	@Resource
	private GomeGmpOrgManageBS gomeGmpOrgManageBS;

	/**
	 * 日历管理
	 * 
	 * @return String
	 * @author rl_wanglijie
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/calendar")
	public ModelAndView calendarData(Model model) {
		GomeGmpResUserBO userBo = BusinessUtil.getLoginUser(getRequest());
		Map<String, Object> userOrgInfoMap = gomeGmpOrgManageBS.getUserOrgInfo(userBo);
		model.addAttribute("hasUserOrg",userBo.getOrgId());
		model.addAttribute("userPermission",userBo.getAuthority());
		model.addAttribute("orgBoList",(List<GomeGmpResOrgBO>) userOrgInfoMap.get("orgBoList"));
		model.addAttribute("userOrgList",(List<GomeGmpResOrgVO>) userOrgInfoMap.get("userOrgList"));
		model.addAttribute("userOrgLevel",(Integer) userOrgInfoMap.get("userOrgLevel"));
		model.addAttribute(Constants.PROJECT_VIEW_ATTRSTR,Constants.PROJECT_VIEW_CALENDAR);
		return new ModelAndView("/calendar/calendar");
	}

	/**
	 * 工时日历
	 * 
	 * @return String
	 * @author rl_wanglijie
	 */
	@RequestMapping("/scheduleCalendar")
	public ModelAndView scheduleData(Model model) {
		model.addAttribute(Constants.PROJECT_VIEW_ATTRSTR,Constants.PROJECT_VIEW_DAILY);
		return new ModelAndView("/calendar/scheduleCalendar");
	}

	/**
	 * 日历管理
	 * 
	 * @return
	 * @author rl_wanglijie
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "getProInfoForCalendar", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public List<Map> getProInfoForAlendar(GomeGmpTJCalendarVO calendarVo) {
		GomeGmpResUserBO userBo = BusinessUtil.getLoginUser(getRequest());
		List<GomeGmpTJCalendarVO> pros = gomeGmpResProCalendarBS.findGomeGmpResScheduleCountListByMonth(calendarVo,userBo);
		List<Map> resultList = new ArrayList<Map>();
		for (GomeGmpTJCalendarVO vo : pros) {
			Map<String, Object> result = new HashMap<String, Object>();
			String key = vo.getActionDate();
			List<String> list = new ArrayList<String>();
			if (vo.getStartCount() > 0) {
				String start = "有" + vo.getStartCount() + "个项目启动";
				list.add(start);
			}
			if (vo.getPlanCount() > 0) {
				String plan = "计划上线" + vo.getPlanCount() + "个项目";
				list.add(plan);
			}
			if (list.size() == 2) {
				String[] value = (String[]) list.toArray(new String[list.size()]);
				result.put("time", key);
				result.put("name", value);
				resultList.add(result);
				continue;
			}
			if (vo.getOnlineCount() > 0) {
				String online = "有" + vo.getOnlineCount() + "个项目上线";
				list.add(online);
			}
			if (list.size() == 2) {
				String[] value = (String[]) list.toArray(new String[list.size()]);
				result.put("time", key);
				result.put("name", value);
				resultList.add(result);
				continue;
			}
			if (vo.getDelayCount() > 0) {
				String delay = "有" + vo.getDelayCount() + "个项目延期";
				list.add(delay);
			}
			String[] value = (String[]) list.toArray(new String[list.size()]);
			result.put("time", key);
			result.put("name", value);
			resultList.add(result);
		}
		return resultList;
	}

	/**
	 * 日历详细
	 * 
	 * @return
	 * @author rl_wanglijie
	 */
	@RequestMapping(value = "getDetailInfoForCalendar", method = RequestMethod.POST)
	public List<GomeGmpTJCalendarVO> getDetailInfoForCalendar(GomeGmpTJCalendarVO calendarVo) {
		GomeGmpResUserBO userBo = BusinessUtil.getLoginUser(getRequest());
		List<GomeGmpTJCalendarVO> pros = gomeGmpResProCalendarBS.findGomeGmpResProjectBOListByDate(calendarVo,userBo);
		return pros;
	}

}
