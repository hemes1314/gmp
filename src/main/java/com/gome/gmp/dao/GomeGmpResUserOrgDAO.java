package com.gome.gmp.dao;

import org.springframework.stereotype.Repository;

import com.gome.gmp.model.bo.GomeGmpResUserOrgBO;

/**
 * 用户与组织关联关系DAO
 * 
 * @author wangchangtie
 *
 */
@Repository("gomeGmpResUserOrgDAO")
public interface GomeGmpResUserOrgDAO {

	/**
	 * 添加用户与组织的关联关系
	 * 
	 * @param userOrg
	 * @return
	 */
	public int addGmpResUserOrg(GomeGmpResUserOrgBO userOrg);

	/**
	 * 根据组织Id删除组织与用户关系
	 * 
	 * @param userTid
	 * @return
	 */
	public int delUserOrgRelationByOrgId(String orgId);

	/**
	 * 
	 * @param userOrg
	 * @return
	 */
	public int upeUserOrgRelation(GomeGmpResUserOrgBO userOrg);
}