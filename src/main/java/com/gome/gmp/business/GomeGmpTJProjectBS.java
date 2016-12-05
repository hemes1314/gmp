package com.gome.gmp.business;

import java.util.List;
import java.util.Map;

import com.gome.gmp.model.vo.GomeGmpResProjectVO;
import com.gome.gmp.model.vo.GomeGmpTJHourVO;

/**
 * 项目统计
 *
 * @author wubin
 */
public interface GomeGmpTJProjectBS {

	/**
	 * 状态分布
	 * 
	 * @param gomeGmpResProjectVO
	 * @return
	 * @author wubin
	 */
	List<Map<String, Object>> stateDistribution(GomeGmpResProjectVO gomeGmpResProjectVO);

	/**
	 * 项目工时统计
	 * 
	 * @param gomeGmpTJHourVO
	 * @return
	 * @author wubin
	 */
	GomeGmpTJHourVO workHour(GomeGmpTJHourVO gomeGmpTJHourVO);
}
