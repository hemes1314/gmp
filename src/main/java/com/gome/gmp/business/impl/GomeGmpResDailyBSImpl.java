package com.gome.gmp.business.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.gome.framework.logging.Logger;
import com.gome.framework.logging.LoggerFactory;
import com.gome.gmp.business.GomeGmpDictItemBS;
import com.gome.gmp.business.GomeGmpResDailyBS;
import com.gome.gmp.business.GomeGmpResProjectBS;
import com.gome.gmp.business.GomeGmpTaskBS;
import com.gome.gmp.common.base.Message;
import com.gome.gmp.common.constant.Constants;
import com.gome.gmp.common.util.DateUtil;
import com.gome.gmp.dao.GomeGmpResDailyDAO;
import com.gome.gmp.model.bo.GomeGmpDictItemBO;
import com.gome.gmp.model.bo.GomeGmpResDailyBO;
import com.gome.gmp.model.bo.GomeGmpResProjectBO;
import com.gome.gmp.model.bo.GomeGmpResTaskBO;
import com.gome.gmp.model.bo.GomeGmpResUserBO;
import com.gome.gmp.model.vo.GomeGmpResDailyDetailVO;
import com.gome.gmp.model.vo.GomeGmpResDailyVO;

/**
 * 日志业务逻辑层
 * 
 * @author be_kangfengping
 */
@Service("gomeGmpResDailyBs")
public class GomeGmpResDailyBSImpl extends BaseBS implements GomeGmpResDailyBS {

	private static final Logger logger = LoggerFactory.getLogger(GomeGmpResDailyBSImpl.class);

	@Resource(name = "gomeGmpDictItemBS")
	private GomeGmpDictItemBS gomeGmpDictItemBS;

	@Resource
	private GomeGmpResProjectBS gomeGmpResProjectBS;
	
	@Resource
	private GomeGmpTaskBS gomeGmpTaskBS;

	@Resource(name = "gomeGmpResDailyDAO")
	private GomeGmpResDailyDAO gomeGmpResDailyDAO;

	@Override
	public int deleteGomeGmpResDailyBOById(String id) {
		return gomeGmpResDailyDAO.deleteGomeGmpResDailyBOById(id);
	}

	@Override
	public GomeGmpResDailyBO findGomeGmpResDailyBOById(String id) {
		return gomeGmpResDailyDAO.findGomeGmpResDailyBOById(id);
	}

	@Override
	public Page<GomeGmpResDailyVO> findUserAllDaily(Integer userID) {
		return gomeGmpResDailyDAO.findUserAllDaily(userID);
	}

	@Override
	public GomeGmpResDailyBO findUserRecentDaily(String userId) {
		return gomeGmpResDailyDAO.findUserRecentDaily(userId);
	}

	/**
	 * 根据时间校验是否有重复的日报
	 */
	@Override
	public Map<String, Object> checkRepetDailyByTime(GomeGmpResDailyBO dailyBo, Long userTid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("isRepet", false);
		if (dailyBo != null && userTid != null) {
			dailyBo.setCreateUser(userTid.intValue());
			List<String> repetList = gomeGmpResDailyDAO.checkRepetDailyByTime(dailyBo);
			if (repetList != null && repetList.size() > 0) {
				resultMap.put("isRepet", true);
				resultMap.put("repetList", repetList);
				resultMap.put("repetSize", repetList.size());
			}
		} else {
			resultMap.put("isRepet", true);
		}
		return resultMap;
	}

	@Override
	public int addDaily(GomeGmpResDailyBO daily, GomeGmpResUserBO userBo) {
		daily.setCreateUser(userBo.getId().intValue());
		daily.setCreateTime(new Date());
		int addRow = gomeGmpResDailyDAO.addDaily(daily);
		updateRelateProTask(daily);
		return addRow;
	}

	private void updateRelateProTask(GomeGmpResDailyBO daily) {
		if (daily.getProTaskId()!=null && daily.getProTaskSchedule() != null) {
			GomeGmpResTaskBO taskBo = new GomeGmpResTaskBO();
			taskBo.setId(Long.valueOf(daily.getProTaskId()).longValue());
			taskBo.setSchedule(daily.getProTaskSchedule());
			gomeGmpTaskBS.updateGomeGmpTaskById(taskBo);
		}
	}

	@Override
	public Message updateDaily(GomeGmpResDailyBO dailyBo, GomeGmpResUserBO userBo) {
		Message msg = null;
		if (dailyBo.getStartTime() != null) {
			if (StringUtils.isNotBlank(dailyBo.getType().toString()) && dailyBo.getType() != 3) {
				if (dailyBo.getStartTime().getTime() > System.currentTimeMillis()) {
					return Message.failure("日报开始时间不能超过当前系统时间");
				}
			}
			List<GomeGmpResDailyBO> upeDailyList = new ArrayList<GomeGmpResDailyBO>();
			upeDailyList.add(dailyBo);
			dailyBo.setCreateUser(userBo.getId().intValue());
			Map<String, Object> noonTimeMsMap = getDailyNoonTime(dailyBo.getStartTime());
			long dayNoonStartTimeMs = (long) noonTimeMsMap.get("noonStart");
			long dayNoonEndTimeMs = (long) noonTimeMsMap.get("noonEnd");
			long viewUpeStartTimeMs = dailyBo.getStartTime().getTime();
			List<GomeGmpResDailyBO> dayDailyList = gomeGmpResDailyDAO.getDailyListByStartDay(dailyBo);
			long hoursMs = 0;
			int notSameFlag = 0;// 工时不一样的标志
			for (int i = 0; i < dayDailyList.size(); i++) {
				GomeGmpResDailyBO thisDaily = dayDailyList.get(i);
				long prevModifyStartTime = thisDaily.getStartTime().getTime(); // 修改前的这一条日报的开始时间
				long prevModifyEndTime = thisDaily.getEndTime().getTime(); // 修改前的这一条日报的结束时间
				if (dailyBo.getId().toString().equals(thisDaily.getId().toString())) {
					notSameFlag = i;
					hoursMs = new Double((dailyBo.getHours() - thisDaily.getHours()) * DateUtil.UNIT_HOUR_TIME).longValue(); // 修改的工时差
					if (i == 0 && viewUpeStartTimeMs != prevModifyStartTime) {// 修改的是否为当日日报第一条
																				// 开始时间是否有改动
						if ((prevModifyStartTime == dayNoonStartTimeMs && viewUpeStartTimeMs == dayNoonEndTimeMs) || (prevModifyStartTime == dayNoonEndTimeMs && viewUpeStartTimeMs == dayNoonStartTimeMs)) {
							continue;
						}
						if (hoursMs == 0) {// 工时未改动
							// 改了的时间差
							hoursMs = viewUpeStartTimeMs - prevModifyStartTime;
						} else if (hoursMs != 0) {// 工时改动情况下，日报开始时间也更改
							long flagMs = viewUpeStartTimeMs - prevModifyStartTime;
							hoursMs += flagMs;
						}
					}
				}
				if (hoursMs == 0) {
					continue;
				}
				if (i > notSameFlag) {
					GomeGmpResDailyBO upeDaily = thisDaily;
					long upeDailyStratTimeMs = 0, upeDailyEndTimeMs = 0;
					upeDailyStratTimeMs = prevModifyStartTime + hoursMs;
					if (prevModifyStartTime <= dayNoonStartTimeMs && upeDailyStratTimeMs > dayNoonStartTimeMs) {
						upeDailyStratTimeMs += DateUtil.UNIT_HOUR_TIME;
					} else if (prevModifyStartTime >= dayNoonEndTimeMs && upeDailyStratTimeMs < dayNoonEndTimeMs) {
						upeDailyStratTimeMs += -DateUtil.UNIT_HOUR_TIME;
					}
					upeDailyEndTimeMs = upeDailyStratTimeMs + new Double(thisDaily.getHours() * DateUtil.UNIT_HOUR_TIME).longValue();
					if ((prevModifyEndTime <= dayNoonStartTimeMs && upeDailyEndTimeMs > dayNoonStartTimeMs && upeDailyEndTimeMs <= dayNoonEndTimeMs) || (prevModifyEndTime > dayNoonEndTimeMs && upeDailyEndTimeMs <= dayNoonEndTimeMs && upeDailyEndTimeMs > dayNoonStartTimeMs)) {
						upeDailyEndTimeMs += DateUtil.UNIT_HOUR_TIME;
					}
					upeDaily.setStartTime(new Date(upeDailyStratTimeMs));
					upeDaily.setEndTime(new Date(upeDailyEndTimeMs));
					String startDateStr = DateUtil.getDate(upeDaily.getStartTime());
					String endDateStr = DateUtil.getDate(upeDaily.getEndTime());
					if (!startDateStr.equals(endDateStr)) {
						return Message.failure("修改此日报日期,当日后续日报时间已跨天");
					}
					upeDailyList.add(upeDaily);
				}
			}
			if (upeDailyList != null && upeDailyList.size() > 0) {
				for (GomeGmpResDailyBO upeDailyBo : upeDailyList) {
					gomeGmpResDailyDAO.updateGomeGmpResDailyBOById(upeDailyBo);
					logger.debug("updateDaily 修改日报成功   用户Id:" + userBo.getUserId() + ",日志Id:" + upeDailyBo.getId());
				}
				msg = Message.success();
			}
		} else {
			msg = Message.failure();
		}
		return msg;
	}

	/**
	 * 日报工时统计列表
	 */
	@Override
	public List<GomeGmpResDailyVO> getDailyHoursScheduleList(String dailyStartDateStr, String dailyStartEndStr, String userTid) {
		GomeGmpResDailyVO dailyVo = new GomeGmpResDailyVO();
		long dailyEndTimeMs = DateUtil.getDate(dailyStartEndStr, DateUtil.PATTERN_YYYY_MM_DD).getTime();
		dailyVo.setStrStartDate(dailyStartDateStr);
		if (dailyEndTimeMs > System.currentTimeMillis()) {
			dailyVo.setStrEndDate(DateUtil.getDateTime());
		} else {
			dailyVo.setStrEndDate(dailyStartEndStr);
		}
		dailyVo.setCreateUser(Integer.parseInt(userTid));
		List<GomeGmpResDailyVO> scheduleList = gomeGmpResDailyDAO.getDailyHoursScheduleList(dailyVo);
		return scheduleList;
	}

	/**
	 * 获取日报详情
	 */
	@Override
	public List<GomeGmpResDailyDetailVO> getDailyDetailInfo(String[] dailyDates, String userTid) {
		List<GomeGmpResDailyDetailVO> dailyDetailVoList = new ArrayList<GomeGmpResDailyDetailVO>();
		if (dailyDates != null && dailyDates.length > 0) {
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("dailyDates", dailyDates);
			queryMap.put("userTid", userTid);
			List<GomeGmpResDailyDetailVO> resultList = gomeGmpResDailyDAO.getDailyDetailInfo(queryMap);
			if (resultList != null && resultList.size() > 0) {
				GomeGmpResDailyDetailVO dailyDetailVo = new GomeGmpResDailyDetailVO();
				for (int i = 0; i < resultList.size(); i++) {
					GomeGmpResDailyDetailVO row = resultList.get(i);
					if (i == 0) {
						dailyDetailVo = new GomeGmpResDailyDetailVO();
						dailyDetailVo.setDailyDate(row.getDailyDate());
						dailyDetailVo.setGomeGmpResDailyDetailVOList(new ArrayList<GomeGmpResDailyDetailVO>());
						dailyDetailVo.getGomeGmpResDailyDetailVOList().add(row);
					} else {
						if (!dailyDetailVo.getDailyDate().equals(row.getDailyDate())) {
							dailyDetailVoList.add(dailyDetailVo);
							dailyDetailVo = new GomeGmpResDailyDetailVO();
							dailyDetailVo.setDailyDate(row.getDailyDate());
							dailyDetailVo.setGomeGmpResDailyDetailVOList(new ArrayList<GomeGmpResDailyDetailVO>());
							dailyDetailVo.getGomeGmpResDailyDetailVOList().add(row);
						} else {
							dailyDetailVo.getGomeGmpResDailyDetailVOList().add(row);
						}
					}
					if (i == resultList.size() - 1) {
						dailyDetailVoList.add(dailyDetailVo);
					}
				}
			}
		}
		return dailyDetailVoList;
	}

	/**
	 * 校验时间是否在午休时间内
	 */
	@Override
	public boolean checkDailyTimeInNoon(GomeGmpResDailyBO dailyBo) {
		boolean notIn = true;
		if (dailyBo.getStartTime() != null && dailyBo.getEndTime() != null) {
			Map<String, Object> noonTimeMsMap = getDailyNoonTime(dailyBo.getStartTime());
			long dayNoonStartTimeMs = (long) noonTimeMsMap.get("noonStart");
			long dayNoonEndTimeMs = (long) noonTimeMsMap.get("noonEnd");
			long startTimeMs = dailyBo.getStartTime().getTime();
			long endTimeMs = dailyBo.getEndTime().getTime();
			if ((startTimeMs <= dayNoonStartTimeMs && dayNoonStartTimeMs < endTimeMs) || (startTimeMs >= dayNoonStartTimeMs && endTimeMs <= dayNoonEndTimeMs)) {
				notIn = false;
			}
		}
		return notIn;
	}

	private Map<String, Object> getDailyNoonTime(Date startTime) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String noonStartTimeStr = DateUtil.getDate(startTime) + " 12:30:00";
		String noonEndTimeStr = DateUtil.getDate(startTime) + " 13:30:00";
		long dayNoonStartTimeMs = DateUtil.getDate(noonStartTimeStr).getTime();
		long dayNoonEndTimeMs = DateUtil.getDate(noonEndTimeStr).getTime();
		resultMap.put("noonStart", dayNoonStartTimeMs);
		resultMap.put("noonEnd", dayNoonEndTimeMs);
		return resultMap;
	}

	@Override
	public boolean isBeyondSysTime(GomeGmpResDailyBO dailyBo) {
		boolean notBeyond = true;
		if (dailyBo.getType() != null && dailyBo.getType() != Constants.DAILYTYPE_REST) {
			if (dailyBo.getStartTime() != null) {
				if (dailyBo.getStartTime().getTime() >= System.currentTimeMillis()) {
					notBeyond = false;
				}
			} else {
				notBeyond = false;
			}
		}
		return notBeyond;
	}

	/**
	 * 获取用户日报明细
	 */
	@Override
	public GomeGmpResDailyVO getDailyDetails(GomeGmpResDailyVO viewDaily, Long userTid) {
		// 获取日报数据字典类型
		List<GomeGmpDictItemBO> dailyTypeList = gomeGmpDictItemBS.getDailyType();
		viewDaily.setDailyTypeList(dailyTypeList);
		// 获取用户关联项目
		List<GomeGmpResProjectBO> dailyRelateProjectList = gomeGmpResProjectBS.getDailyRelateProjectsByUser(userTid);
		viewDaily.setRelateProjectList(dailyRelateProjectList);
		return viewDaily;
	}
}
