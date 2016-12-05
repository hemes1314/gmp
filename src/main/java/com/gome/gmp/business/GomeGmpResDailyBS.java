package com.gome.gmp.business;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.gome.gmp.common.base.Message;
import com.gome.gmp.model.bo.GomeGmpResDailyBO;
import com.gome.gmp.model.bo.GomeGmpResUserBO;
import com.gome.gmp.model.vo.GomeGmpResDailyDetailVO;
import com.gome.gmp.model.vo.GomeGmpResDailyVO;

/**
 * 日报服务层接口
 * 
 * @author wangchangtie
 *
 */
public interface GomeGmpResDailyBS {

	int deleteGomeGmpResDailyBOById(String id);

	GomeGmpResDailyBO findGomeGmpResDailyBOById(String id);

	Page<GomeGmpResDailyVO> findUserAllDaily(Integer userID);

	/**
	 * 根据时间校验是否有重复的日报
	 * 
	 * @param dailyBo
	 * @param userTid
	 * @return true:重复,false:不重复
	 */
	public Map<String, Object> checkRepetDailyByTime(GomeGmpResDailyBO dailyBo, Long userTid);

	/**
	 * 添加日报
	 * 
	 * @param viewDaily
	 * @param user
	 * @return
	 */
	public int addDaily(GomeGmpResDailyBO viewDaily, GomeGmpResUserBO user);

	/**
	 * 获取用户最后一次写日报时间
	 * 
	 * @param userID
	 * @return
	 */
	public GomeGmpResDailyBO findUserRecentDaily(String userID);

	/**
	 * 修改日志
	 * 
	 * @param viewDaily
	 * @param userBo
	 * @return
	 */
	public Message updateDaily(GomeGmpResDailyBO viewDaily, GomeGmpResUserBO userBo);

	/**
	 * 日报工时统计列表
	 * 
	 * @param dailyStartDateStr
	 * @param dailyEndDateStr
	 * @param userTid
	 * @return
	 */
	public List<GomeGmpResDailyVO> getDailyHoursScheduleList(String dailyStartDateStr, String dailyEndDateStr, String userTid);

	/**
	 * 日报详情
	 * 
	 * @param dailyDates
	 * @param userTid
	 * @return
	 */
	public List<GomeGmpResDailyDetailVO> getDailyDetailInfo(String[] dailyDates, String userTid);

	/**
	 * 校验日报时间是否在午休时间内
	 * 
	 * @param viewDaily
	 * @return true 不在午休时间内，false在午休时间段内
	 */
	public boolean checkDailyTimeInNoon(GomeGmpResDailyBO viewDaily);

	/**
	 * 日报开始时间是否大于当前系统时间
	 * 
	 * @param viewDaily
	 * @return
	 */
	public boolean isBeyondSysTime(GomeGmpResDailyBO viewDaily);
	
	/**
	 * 获取用户日报明细
	 * @param viewDaily
	 * @param userTid
	 * @return
	 */
	public GomeGmpResDailyVO getDailyDetails(GomeGmpResDailyVO viewDaily, Long userTid);
}
