package com.gome.gmp.business.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gome.gmp.business.GomeGmpManualBS;
import com.gome.gmp.common.util.HolidayUtil;
import com.gome.gmp.dao.GomeGmpManualDAO;


/**
 * 
 *
 * @author wubin
 */
@Service
public class GomeGmpManualBSImpl implements GomeGmpManualBS {

	@Resource
	private GomeGmpManualDAO gomeGmpManualDAO;
	
	@Resource
	private SqlSessionFactory sqlSessionFactory;
	
	/**
	 * 更新节假日
	 * 
	 * @return
	 * @author wubin
	 */
	@Transactional
	@Override
		
	public Map<String, Integer> updateHolidays() {
		List<String> holidays = HolidayUtil.getHolidaysByThisYear();
		// 更新本年均为非节假日
		gomeGmpManualDAO.resetHolidays();
		
		// 修改为休息日
		Map<String, Object> paramMap = new HashMap<String, Object>(); 
		paramMap.put("dateList", holidays);
		paramMap.put("isHoliday", 1);
		int rets = gomeGmpManualDAO.update2Holiday(paramMap);
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("TOTAL", holidays.size());
		map.put("SUCCESS", rets);
		return map;
	}
}
