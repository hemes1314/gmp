package com.gome.gmp.business;

import java.util.List;

import com.gome.gmp.model.bo.GomeGmpResProjectBO;
import com.gome.gmp.model.bo.GomeGmpResUserBO;
import com.gome.gmp.model.vo.GomeGmpResTaskVO;
import com.gome.gmp.model.vo.GomeGmpTJCalendarVO;

/**
 * 
 *
 * @author wubin
 */
public interface GomeGmpResProCalendarBS {

	List<GomeGmpResProjectBO> findGomeGmpResProjectBOList();

	List<GomeGmpTJCalendarVO> findGomeGmpResProjectBOListByDate(GomeGmpTJCalendarVO calendarVo, GomeGmpResUserBO userBo);

	public List<GomeGmpTJCalendarVO> findGomeGmpResScheduleCountListByMonth(GomeGmpTJCalendarVO calendarVo, GomeGmpResUserBO userBo);

	public List<GomeGmpResTaskVO> findGomeGmpResPlanScheduleBOListByDate(GomeGmpResTaskVO gomeGmpResTaskVO);
}
