package com.gome.gmp.business.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gome.gmp.business.GomeGmpOrgManageBS;
import com.gome.gmp.business.GomeGmpResHoursCountBS;
import com.gome.gmp.common.util.DateUtil;
import com.gome.gmp.dao.GomeGmpDictItemDAO;
import com.gome.gmp.dao.GomeGmpResDailyDAO;
import com.gome.gmp.dao.GomeGmpResOrgDAO;
import com.gome.gmp.dao.GomeGmpResUserDAO;
import com.gome.gmp.model.bo.GomeGmpResDailyBO;
import com.gome.gmp.model.bo.GomeGmpResUserBO;
import com.gome.gmp.model.vo.GomeGmpDictItemVO;
import com.gome.gmp.model.vo.GomeGmpResDailyDetailVO;
import com.gome.gmp.model.vo.GomeGmpResOrgVO;
import com.gome.gmp.model.vo.GomeGmpTJCountVO;

@Service
public class GomeGmpResHoursCountBSImpl implements GomeGmpResHoursCountBS {

	@Resource
	private GomeGmpDictItemDAO gomeGmpDictItemDAO;
	
	@Resource
	private GomeGmpOrgManageBS gomeGmpOrgManageBS;
	
	@Resource
	private GomeGmpResDailyDAO gomeGmpResDailyDAO;
	
	@Resource
	private GomeGmpResUserDAO gomeGmpResUserDAO;
	
	@Resource
	private GomeGmpResOrgDAO gomeGmpResOrgDAO;
	
	private static String USER_HOURS = "userHours";
	
	private static String USER_WORK_DAY_COUNT = "workDayCount";

	/**
	 * 部门工时统计
	 * 
	 * @param gomeGmpTJCountVO
	 * @return
	 * @author wubin
	 */
	@Override
	public List<GomeGmpResOrgVO> findGomeGmpResOrgHoursCountBOById(GomeGmpTJCountVO gomeGmpTJCountVO){
		// 设置部门级别
		GomeGmpResOrgVO orgVO = new GomeGmpResOrgVO();
		String[] orgIdList = gomeGmpTJCountVO.getOrgIds();
		if (orgIdList != null && orgIdList.length > 0) {
			if(gomeGmpTJCountVO.getSubOrg()) {
				orgVO.setQueryType(null);
			} else {
				orgVO.setQueryType("thisLevel");
			}
		}
		orgVO.setOrgIdList(orgIdList);
		List<GomeGmpResOrgVO> rtnList = gomeGmpResOrgDAO.getOrgFrameworkList(orgVO);
		// 当日7点以后有工时算作出勤；没有则不算出勤； 0点到7点的工时算作前一日的工时；
		gomeGmpTJCountVO = formatDate(gomeGmpTJCountVO);
		rtnList = processList(rtnList, gomeGmpTJCountVO, false);
		return rtnList;
	}
	
	/**
	 * 格式化时间
	 * 
	 * @param gomeGmpTJCountVO
	 * @return
	 * @author wubin
	 */
	private GomeGmpTJCountVO formatDate(GomeGmpTJCountVO gomeGmpTJCountVO) {
		String startDate = gomeGmpTJCountVO.getStartDate();
		gomeGmpTJCountVO.setStartTime(DateUtil.getDate(startDate + " 07:00:00"));
		
		String endDate = gomeGmpTJCountVO.getEndDate();
		endDate = DateUtil.getDate(DateUtil.addDays(DateUtil.getDate(endDate, DateUtil.PATTERN_YYYY_MM_DD), 1))+" 07:00:00";
		gomeGmpTJCountVO.setEndTime(DateUtil.getDate(endDate));
		return gomeGmpTJCountVO;
	}
	
	/**
	 * 结果集再计算
	 * 
	 * @param rtnList
	 * @return
	 * @author wubin
	 */
	private List<GomeGmpResOrgVO> processList(List<GomeGmpResOrgVO> rtnList, GomeGmpTJCountVO gomeGmpTJCountVO, boolean isUser) {
		
		// 查询节假日日期
		GomeGmpDictItemVO gomeGmpDictItemVO = new GomeGmpDictItemVO();
		gomeGmpDictItemVO.setStartDate(gomeGmpTJCountVO.getStartDate());
		gomeGmpDictItemVO.setEndDate(gomeGmpTJCountVO.getEndDate());
		gomeGmpDictItemVO.setIsHoliday("1");
		// 节假日
		List<String> holidays = gomeGmpDictItemDAO.holidayDates(gomeGmpDictItemVO);
		Set<String> holidaySet = new HashSet<String>(holidays);
		// 查询总天数
		int queryDays = DateUtil.daysBetween(gomeGmpTJCountVO.getStartDate(), gomeGmpTJCountVO.getEndDate());
		// 应出勤天数
		int shouldWorkDays = queryDays - holidays.size();
		
		// 计算实际工时
		// 计算人员工时
		if(isUser) {
			
			GomeGmpResUserBO bo = new GomeGmpResUserBO();
			bo.setOrgId(gomeGmpTJCountVO.getTeamId());
			List<GomeGmpResUserBO> users = gomeGmpResUserDAO.findGomeGmpResUser(bo);
			
			// 总工时
			for(GomeGmpResUserBO user : users) {
				// 人数
				GomeGmpResOrgVO gomeGmpResOrgVO = new GomeGmpResOrgVO();
				gomeGmpResOrgVO.setId(user.getId());
				gomeGmpResOrgVO.setUserId(user.getUserId());
				gomeGmpResOrgVO.setUserName(user.getUserName());
				gomeGmpResOrgVO.setMemberCount(1);
				
				// 2.根据人员查询时间段内所有的日报，计算实际工时
				Map<String, Double> userMap = computeUserHours(user, gomeGmpTJCountVO, holidaySet);
				Double userHours = userMap.get(USER_HOURS);
				Double workDayCount = userMap.get(USER_WORK_DAY_COUNT);
				
				// 应出勤天数
				gomeGmpResOrgVO.setWeekdayCount(workDayCount);
				// 人员的日平均工时=人员实际工时/实际上班天数
				if(workDayCount != 0.0) {
					gomeGmpResOrgVO.setAvgHour(userHours / workDayCount);
				}
				// 人员计算计划总工时=人员应出勤天数*8h
				gomeGmpResOrgVO.setPlanHoursCount(workDayCount*8);
				// 人员实际工时
				gomeGmpResOrgVO.setHoursCount(userHours);
				
				
				rtnList.add(gomeGmpResOrgVO);
			}
		} else {
			// 计算部门工时
			// 1.根据部门查询人员
			for(GomeGmpResOrgVO gomeGmpResOrgVO : rtnList) {
				// 部门人数
				int orgMembers = 0;
				// 计划总工时
				double orgPlanHours = 0.0;
				// 实际总工时
				double orgActualHours = 0.0;
				// 计算日均工时 = 人员实际工时/实际上班天数求和
				double orgAvgHour = 0.0;
				GomeGmpResUserBO bo = new GomeGmpResUserBO();
				bo.setOrgId(gomeGmpResOrgVO.getOrgId());
				List<GomeGmpResUserBO> userBoList = gomeGmpResUserDAO.findGomeGmpResUser(bo);
				if(userBoList!=null && userBoList.size()>0){
					orgMembers = userBoList.size();
					gomeGmpResOrgVO.setMemberCount(orgMembers);
					for(GomeGmpResUserBO user : userBoList) {
						// 2.根据人员查询时间段内所有的日报，计算实际工时
						Map<String, Double> userMap = computeUserHours(user, gomeGmpTJCountVO, holidaySet);
						Double userHours = userMap.get(USER_HOURS);
						Double workDayCount = userMap.get(USER_WORK_DAY_COUNT);
						orgActualHours += userHours;
						// 人员的日平均工时=人员实际工时/实际上班天数
						if(workDayCount != 0.0) {
							orgAvgHour += (userHours / workDayCount);
						}
					}
				}
				orgPlanHours = orgMembers * shouldWorkDays * 8;
				gomeGmpResOrgVO.setPlanHoursCount(orgPlanHours);
				gomeGmpResOrgVO.setAvgHour(orgAvgHour/orgMembers);
				gomeGmpResOrgVO.setHoursCount(orgActualHours);
			}
		}
		
		// 每一级都加上计划总工时（人数*应出勤天数*8h)；应出勤天数需排除周末和法定节假日；
		for(GomeGmpResOrgVO gomeGmpResOrgVO : rtnList) {
			
			Double avgHour = gomeGmpResOrgVO.getAvgHour();
			Double planHoursCount = gomeGmpResOrgVO.getPlanHoursCount();
			
			if(planHoursCount == 0.0) {
				continue;
			}
			
			// 实际工时取小数点后两位
			BigDecimal bigDecimal = new BigDecimal(avgHour);  
			avgHour = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			gomeGmpResOrgVO.setAvgHour(avgHour);
			
			// 实际/计划
			if(gomeGmpResOrgVO.getHoursCount() != null) {
				double percent = gomeGmpResOrgVO.getHoursCount()/gomeGmpResOrgVO.getPlanHoursCount();
				BigDecimal bigDecimal2 = new BigDecimal(percent*100);  
				percent = bigDecimal2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  
				gomeGmpResOrgVO.setPercent(percent);
			}
		}
		return rtnList;
	}
	
	/**
	 * 人员工时统计
	 * 
	 * @param gomeGmpTJCountVO
	 * @return
	 * @author wubin
	 */
	@Override
	public List<GomeGmpResOrgVO> findGomeGmpResUserHoursCountBOById(GomeGmpTJCountVO gomeGmpTJCountVO){
		gomeGmpTJCountVO = formatDate(gomeGmpTJCountVO);
		List<GomeGmpResOrgVO> rtnList = processList(new ArrayList<GomeGmpResOrgVO>(), gomeGmpTJCountVO, true);
		return rtnList;
	}
	
	/**
	 * 计算人员工时
	 * 
	 * @param dailys
	 * @param tjStartDate
	 * @param holidaySet
	 * @return
	 * @author wubin
	 */
	private Map<String, Double> computeUserHours(GomeGmpResUserBO user, GomeGmpTJCountVO gomeGmpTJCountVO, Set<String> holidaySet) {
		
		// 2.根据人员查询时间段内所有的日报，计算实际工时
		GomeGmpResDailyBO gomeGmpResDailyBO = new GomeGmpResDailyBO();
		gomeGmpResDailyBO.setStartTime(gomeGmpTJCountVO.getStartTime());
		gomeGmpResDailyBO.setEndTime(gomeGmpTJCountVO.getEndTime());
		gomeGmpResDailyBO.setCreateUser(user.getId().intValue());
		List<GomeGmpResDailyDetailVO> dailys = gomeGmpResDailyDAO.getDailyByDate(gomeGmpResDailyBO);
		
		// 3.计算实际工时，按照日期计算工时
		Map<String, Double> date2Hour = new LinkedHashMap<String, Double>();
		for(GomeGmpResDailyBO daily : dailys) {
			Date startTime = daily.getStartTime();
			Date endTime = daily.getEndTime();
			
			// 开始时间小于7点的拆分计算工时
			int startHour = DateUtil.getHours(startTime);
			int endHour = DateUtil.getHours(endTime);
			String startDateStr = DateUtil.getDate(startTime);
			String endDateStr = DateUtil.getDate(endTime);
			if(startHour < 7) {
				Date time7 = DateUtil.getDate(startDateStr + " 07:00:00");
				// 如果是第一天则把7：00以后计算工时
				if(startDateStr.equals(gomeGmpTJCountVO.getStartDate())) {
					Double hours = DateUtil.getDiffHours(time7, endTime);
					if(hours > 0) {
						addHours(date2Hour, endDateStr, hours);
					}
				} else {
					// 如果不是第一天，
					// 如果结束小时数小于7,则将工时计入前一天工时
					String preDateStr = DateUtil.getDate(DateUtil.addDays(startTime, -1));
					if(endHour < 7) {
						addHours(date2Hour, preDateStr, daily.getHours());
					} else {
						// 7点以前计入昨天工时，7点以后计入今天工时
						Double preHours = DateUtil.getDiffHours(startTime, time7);
						Double nextHours = DateUtil.getDiffHours(time7, endTime);
						addHours(date2Hour, preDateStr, preHours);
						addHours(date2Hour, startDateStr, nextHours);
					}
				}
			} else {
				// 开始时间大于7点，结束时间小于24点的直接计入工时
				// 结束时间大于24点的切分记录
				if(endHour <= 24) {
					addHours(date2Hour,startDateStr, daily.getHours());
				} else {
					Date time24 = DateUtil.getDate(startDateStr + " 24:00:00");
					Double preHours = DateUtil.getDiffHours(startTime, time24);
					Double nextHours = DateUtil.getDiffHours(time24, endTime);
					addHours(date2Hour, startDateStr, preHours);
					addHours(date2Hour, endDateStr, nextHours);
				}
			}
		}
		// 节假日工时不计入实际工时
		double userHours = 0.0;
		// 上班天数
		Double workDayCount = 0.0;
		for(String date : date2Hour.keySet()) {
			if(!holidaySet.contains(date)) {
				workDayCount++;
				userHours += date2Hour.get(date);
			}
		}
		
		Map<String, Double> rtnMap = new HashMap<String, Double>();
		rtnMap.put(USER_HOURS, userHours);
		rtnMap.put(USER_WORK_DAY_COUNT, workDayCount);
		return rtnMap;
	}
	
	/**
	 * 增加工时
	 * 
	 * @return
	 * @author wubin
	 */
	private Map<String, Double> addHours(Map<String, Double> date2Hour, String date, Double hours) {
		
		if(date2Hour.containsKey(date)) {
			date2Hour.put(date, date2Hour.get(date)+hours);
		} else {
			date2Hour.put(date, hours);
		}
		return date2Hour;
	}
}
