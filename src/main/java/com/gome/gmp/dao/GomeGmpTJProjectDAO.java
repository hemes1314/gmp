package com.gome.gmp.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gome.gmp.model.vo.GomeGmpResDailyVO;
import com.gome.gmp.model.vo.GomeGmpResProjectVO;
import com.gome.gmp.model.vo.GomeGmpTJHourVO;

/**
 * 项目统计dao
 * 
 * @author wubin
 */
@Repository("gomeGmpTJProjectDAO")
public interface GomeGmpTJProjectDAO {

	/**
	 * 状态分布
	 * 
	 * @param gomeGmpResProjectVO
	 * @return
	 * @author wubin
	 */
	List<Map<String, Object>> stateDistribution(GomeGmpResProjectVO gomeGmpResProjectVO);

	/**
	 * 项目工时统计表头
	 * 
	 * @param gomeGmpTJHourVO
	 * @return
	 * @author wubin
	 */
	GomeGmpTJHourVO workHour(GomeGmpTJHourVO gomeGmpTJHourVO);

	/**
	 * 项目工时统计详情
	 * 
	 * @param gomeGmpTJHourVO
	 * @return
	 * @author wubin
	 */
	List<GomeGmpResDailyVO> workHourDetail(GomeGmpTJHourVO gomeGmpTJHourVO);
}
