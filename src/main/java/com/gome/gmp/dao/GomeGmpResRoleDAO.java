package com.gome.gmp.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gome.gmp.model.bo.GomeGmpResRoleBO;
import com.gome.gmp.model.vo.GomeGmpResRoleVO;

/**
 * 角色dao
 * 
 * @author Administrator
 */
@Repository("gomeGmpResRoleDAO")
public interface GomeGmpResRoleDAO {

	/**
	 * 保存角色
	 * 
	 * @param gomeGmpResRoleBO
	 * @return
	 * @author wubin
	 */
	int saveGomeGmpResRoleBO(GomeGmpResRoleBO gomeGmpResRoleBO);

	/**
	 * 删除角色
	 * 
	 * @param id
	 * @return
	 * @author wubin
	 */
	int deleteGomeGmpResRoleBOById(Long id);

	/**
	 * 更新角色
	 * 
	 * @param gomeGmpResRoleBO
	 * @return
	 * @author wubin
	 */
	int updateGomeGmpResRoleBOById(GomeGmpResRoleBO gomeGmpResRoleBO);

	/**
	 * 查询单个角色
	 * 
	 * @param id
	 * @return
	 * @author wubin
	 */
	GomeGmpResRoleBO findGomeGmpResRoleBOById(Long id);

	/**
	 * 条件查询角色
	 * 
	 * @param gomeGmpResRoleVO
	 * @return
	 * @author wubin
	 */
	List<GomeGmpResRoleVO> findGomeGmpResRoleVO(GomeGmpResRoleVO gomeGmpResRoleVO);
}