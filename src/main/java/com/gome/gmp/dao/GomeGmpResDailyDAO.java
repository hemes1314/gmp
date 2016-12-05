package com.gome.gmp.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.gome.gmp.model.bo.GomeGmpResDailyBO;
import com.gome.gmp.model.vo.GomeGmpResDailyDetailVO;
import com.gome.gmp.model.vo.GomeGmpResDailyVO;

/**
 * @author Administrator
 */
@Repository("gomeGmpResDailyDAO")
public interface GomeGmpResDailyDAO {

	int deleteGomeGmpResDailyBOById(String id);

	int updateGomeGmpResDailyBOById(GomeGmpResDailyBO gomeGmpResDailyBO);

	GomeGmpResDailyBO findGomeGmpResDailyBOById(String id);

	Page<GomeGmpResDailyVO> findUserAllDaily(Integer userID);
	
	/**
	 * 根据时间校验是否有重复的日报
	 * 
	 * @param dailyBo
	 * @return
	 */
	public List<String> checkRepetDailyByTime(GomeGmpResDailyBO dailyBo);

	/**
	 * 添加日报
	 * 
	 * @param daily
	 * @return
	 */
	public int addDaily(GomeGmpResDailyBO daily);

	/**
	 * 用户最近一次写日报时间
	 * 
	 * @param userId
	 * @return
	 */
	public GomeGmpResDailyBO findUserRecentDaily(String userId);

	/**
	 * 根据开始日期查询日期当天的日报
	 * 
	 * @param dailyBo
	 * @return
	 */
	public List<GomeGmpResDailyBO> getDailyListByStartDay(GomeGmpResDailyBO dailyBo);
	
	/**
	 * 日报工时统计
	 * @param dailyVo
	 * @return
	 */
	public List<GomeGmpResDailyVO> getDailyHoursScheduleList(GomeGmpResDailyVO dailyVo);
	
	/**
	 * 日报详情
	 * @param queryMap
	 * @return
	 */
	public List<GomeGmpResDailyDetailVO> getDailyDetailInfo(Map<String, Object> queryMap);
	
	/**
	 * 查询时间段内用户的日报
	 * @param queryMap
	 * @return
	 */
	public List<GomeGmpResDailyDetailVO> getDailyByDate(GomeGmpResDailyBO dailyBo);
}