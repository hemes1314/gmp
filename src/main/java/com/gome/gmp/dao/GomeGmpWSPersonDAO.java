package com.gome.gmp.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gome.gmp.model.bo.GomeGmpResUserBO;

/**
 * 同步基础数据-人员dao
 *
 * @author wubin
 */
@Repository("gomeGmpWSPersonDAO")
public interface GomeGmpWSPersonDAO {

	/**
	 * 删除离职的员工
	 * 
	 * @param pernrs
	 * @return
	 * @author wubin
	 */
	int deleteGomeGmpResUserBOByPernr(String pernrs);

	/**
	 * 查找出存在的员工
	 * 
	 * @param pernrs
	 * @return
	 * @author wubin
	 */
	List<String> findGomeGmpResUserBOByPernr(String pernrs);

	/**
	 * 新增员工
	 * 
	 * @param users
	 * @return
	 * @author wubin
	 */
	int saveGomeGmpResUserBO(List<GomeGmpResUserBO> users);

	/**
	 * 修改员工
	 * 
	 * @param users
	 * @return
	 * @author wubin
	 */
	int updateGomeGmpResUserBO(GomeGmpResUserBO userBO);

	/**
	 * 更新已经存在但是没有员工编号的存在
	 * 
	 * @param userBO
	 * @return
	 * @author wubin
	 */
	int updatePernrForUserName(GomeGmpResUserBO userBO);
}
