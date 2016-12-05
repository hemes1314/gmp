package com.gome.gmp.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gome.gmp.model.bo.GomeGmpResProjectBO;
import com.gome.gmp.model.vo.GomeGmpResTaskVO;
import com.gome.gmp.model.vo.GomeGmpTJCalendarVO;

/**
 * @author Administrator
 */
@Repository("gomeGmpResProCalendarDAO")
public interface GomeGmpResProCalendarDAO {

	List<GomeGmpResProjectBO> findGomeGmpResProjectBOList();

	public List<GomeGmpTJCalendarVO> findGomeGmpResProjectBOListByDate(GomeGmpTJCalendarVO calendarVo);

	public List<GomeGmpTJCalendarVO> findGomeGmpResScheduleCountListByMonth(GomeGmpTJCalendarVO calendarVo);

	List<GomeGmpResTaskVO> findGomeGmpResPlanScheduleBOListByDate(GomeGmpResTaskVO gomeGmpResTaskVO);
}