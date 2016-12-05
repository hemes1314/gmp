package com.gome.gmp.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gome.gmp.model.bo.GomeGmpResUnitBO;

/**
 * 同步基础数据-人员dao
 *
 * @author wubin
 */
@Repository("gomeGmpWSUnitDAO")
public interface GomeGmpWSUnitDAO {

	/**
	 * 删除部门
	 * 
	 * @param pernrs
	 * @return
	 * @author wubin
	 */
	int deleteGomeGmpResUnits();

	/**
	 * 插入部门
	 * 
	 * @param gomeGmpResUnitBO
	 * @return
	 * @author wubin
	 */
	int saveGomeGmpResUnitBO(List<GomeGmpResUnitBO> params);
}
