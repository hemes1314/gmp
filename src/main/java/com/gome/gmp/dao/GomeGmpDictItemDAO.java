package com.gome.gmp.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gome.gmp.model.bo.GomeGmpDictItemBO;
import com.gome.gmp.model.vo.GomeGmpDictItemVO;

/**
 * 字典dao
 * 
 * @author Administrator
 */
@Repository("gomeGmpDictItemDAO")
public interface GomeGmpDictItemDAO {

	/**
	 * 查询字典数据
	 * 
	 * @return
	 */
	List<GomeGmpDictItemBO> findGomeGmpDictItemBO(GomeGmpDictItemBO gomeGmpDictItemVO);

	/**
	 * 获取节假日
	 * 
	 * @return
	 */
	List<String> holidayDates(GomeGmpDictItemVO gomeGmpDictItemVO);

	/**
	 * 获取日志类型
	 * 
	 * @return
	 */
	List<GomeGmpDictItemBO> getDailyType();
}