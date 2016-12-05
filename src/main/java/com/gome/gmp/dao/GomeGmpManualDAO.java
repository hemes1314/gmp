package com.gome.gmp.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 手动执行dao
 * 
 * @author Administrator
 */
@Repository("gomeGmpManualDAO")
public interface GomeGmpManualDAO {

	/**
	 * 重置所有日期为非休息日
	 * 
	 * @return
	 */
	int resetHolidays();

	/**
	 * 修改为休息日
	 * 
	 * @return
	 */
	int update2Holiday(Map<String, Object> paramMap);
}