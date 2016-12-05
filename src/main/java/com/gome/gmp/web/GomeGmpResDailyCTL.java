package com.gome.gmp.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.Page;
import com.gome.framework.base.BaseRestController;
import com.gome.framework.logging.Logger;
import com.gome.framework.logging.LoggerFactory;
import com.gome.gmp.business.GomeGmpResDailyBS;
import com.gome.gmp.common.BusinessUtil;
import com.gome.gmp.common.base.Message;
import com.gome.gmp.common.constant.Constants;
import com.gome.gmp.common.mybaits.plugins.page.PageModel;
import com.gome.gmp.common.util.DateUtil;
import com.gome.gmp.model.bo.GomeGmpResDailyBO;
import com.gome.gmp.model.bo.GomeGmpResUserBO;
import com.gome.gmp.model.vo.GomeGmpResDailyDetailVO;
import com.gome.gmp.model.vo.GomeGmpResDailyVO;

/**
 * 日报控制层
 * 
 * @author wangchangtie
 *
 */
@RestController
@RequestMapping("/daily")
public class GomeGmpResDailyCTL extends BaseRestController {

	private static final Logger logger = LoggerFactory.getLogger(GomeGmpResDailyCTL.class);

	@Resource(name = "gomeGmpResDailyBs")
	private GomeGmpResDailyBS gomeGmpResDailyBs;

	/**
	 * 跳转到添加
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getDaily", method = RequestMethod.GET)
	public ModelAndView getDaily(Model model,@ModelAttribute GomeGmpResDailyVO viewDaily) {
		viewDaily = gomeGmpResDailyBs.getDailyDetails(viewDaily, BusinessUtil.getLoginUser(getRequest()).getId());
		model.addAttribute(Constants.PROJECT_VIEW_ATTRSTR,Constants.PROJECT_VIEW_DAILY);
		return new ModelAndView("/daily/createDaily");
	}

	/**
	 * 获取日志
	 * 
	 * @param pageNum
	 * @return
	 */
	@RequestMapping(value = "/getDailyData/{pageNum}", method = { RequestMethod.POST })
	public PageModel getDailyData(@PathVariable Integer pageNum) {
		GomeGmpResUserBO loginUser = BusinessUtil.getLoginUser(getRequest());
		BusinessUtil.setPageNum(pageNum);
		Page<GomeGmpResDailyVO> dailyList = gomeGmpResDailyBs.findUserAllDaily(loginUser.getId().intValue());
		PageModel model = new PageModel();
		model.setPageData(dailyList);
		return model;
	}

	/**
	 * 删除日志
	 */
	@RequestMapping(value = "/deleteDaily/{dailyId}", method = RequestMethod.POST, produces = "application/json")
	public int deleteDaily(@PathVariable String dailyId) {
		int row = gomeGmpResDailyBs.deleteGomeGmpResDailyBOById(dailyId);
		return row;
	}

	/**
	 * 获取当前系统时间
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getSysDate", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Message getSysDate() {
		Message msg = Message.success();
		msg.setData(DateUtil.getDateTime());
		return msg;
	}

	/**
	 * 新增日报
	 * 
	 * @param daily
	 * @return
	 */
	@RequestMapping(value = "/addDaily", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Message addDaily(@ModelAttribute GomeGmpResDailyBO viewDaily) {
		Message msg = null;
		GomeGmpResUserBO user = BusinessUtil.getLoginUser(getRequest());
		if (user.getId() != null && viewDaily != null) {
			boolean notBeyond = gomeGmpResDailyBs.isBeyondSysTime(viewDaily);
			if(!notBeyond){
				msg = Message.failure("日报时间超过当前系统时间 不可添加日报");
				return msg;
			}
			Map<String, Object> isRepetMap = gomeGmpResDailyBs.checkRepetDailyByTime(viewDaily, user.getId());
			boolean isRepet = (boolean) isRepetMap.get("isRepet");
			if (isRepet) {
				String startTimeZone = DateUtil.getDateTime(viewDaily.getStartTime());
				String endTimeZone = DateUtil.getDateTime(viewDaily.getEndTime());
				msg = Message.failure("此时间段(" + startTimeZone + "-" + endTimeZone.split(" ")[1] + ")存在重复日报");
				return msg;
			}
			int addRow = gomeGmpResDailyBs.addDaily(viewDaily, user);
			if (addRow > 0) {
				msg = Message.success("添加日报成功");
				BusinessUtil.setPageNum(1);
				logger.debug("addDaily 添加日报成功   用户Id:" + user.getUserId());
				GomeGmpResDailyBO recentDaily = gomeGmpResDailyBs.findUserRecentDaily(user.getId().toString());
				if (recentDaily != null) {
					msg.setData(recentDaily);
				}
			} else {
				msg = Message.failure("添加日报失败");
			}
		} else {
			msg = Message.failure("获取用户信息失败");
		}
		return msg;
	}

	/**
	 * 修改日志
	 * 
	 * @param daily
	 * @return
	 */
	@RequestMapping(value = "/updateDaily", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Message updateDaily(@ModelAttribute GomeGmpResDailyBO viewDaily) {
		Message msg = null;
		if (viewDaily != null) {
			boolean notBeyond = gomeGmpResDailyBs.isBeyondSysTime(viewDaily);
			if(!notBeyond){
				msg = Message.failure("日报时间超过当前系统时间 不可添加日报");
				return msg;
			}
			GomeGmpResUserBO user = BusinessUtil.getLoginUser(getRequest());
			if (user != null && user.getId() != null) {
				msg = gomeGmpResDailyBs.updateDaily(viewDaily, user);
			} else {
				msg = Message.failure("获取用户信息失败");
			}
		} else {
			msg = Message.failure();
		}
		return msg;
	}

	/**
	 * 日报工时统计
	 * 
	 * @param strStartDate
	 * @param strEndDate
	 * @param userTid
	 * @return
	 */
	@RequestMapping(value = "getDailyHoursSchedule", method = RequestMethod.POST)
	public Message getDailyHoursScheduleTest(@Param("targetDate") String targetDate, @Param("userTid") String userTid) {
		Message msg = Message.success();
		String startDateStr = "";
		String endDateStr = "";
		int differenceMonth = 0;
		if (StringUtils.isNotEmpty(targetDate)) {
			differenceMonth = DateUtil.calculateDifferenceMonth(targetDate);
		}
		startDateStr = DateUtil.getLastMonthEndSundayByMonthNum(differenceMonth); // 上个月最后一个周日
		endDateStr = DateUtil.getNextMonthFirstSaturdayByMonthNum(differenceMonth);// 下个月第一个周六
		GomeGmpResUserBO loginUser = BusinessUtil.getLoginUser(getRequest());
		if (StringUtils.isEmpty(userTid)) {
			userTid = loginUser.getId().toString();
		}
		List<GomeGmpResDailyVO> scheduleList = gomeGmpResDailyBs.getDailyHoursScheduleList(startDateStr, endDateStr, userTid);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("dataList", scheduleList);
		dataMap.put("sysDate", DateUtil.getDateTime());
		dataMap.put("dateRange", startDateStr + ";" + endDateStr);
		msg.setData(dataMap);
		return msg;
	}

	/**
	 * 查看日报详情
	 * 
	 * @param inputVO
	 * @return
	 */
	@RequestMapping(value = "getDailyDetailInfo", method = RequestMethod.POST)
	public List<GomeGmpResDailyDetailVO> getDailyDetailInfo(@Param("dailyDates") String[] dailyDates, @Param("userTid") String userTid) {
		if (StringUtils.isEmpty(userTid)) {
			userTid = BusinessUtil.getLoginUserId(getRequest()).toString();
		}
		List<GomeGmpResDailyDetailVO> resultList = gomeGmpResDailyBs.getDailyDetailInfo(dailyDates, userTid);
		return resultList;
	}
}