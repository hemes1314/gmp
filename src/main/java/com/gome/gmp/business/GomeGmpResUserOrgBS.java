package com.gome.gmp.business;

import com.gome.gmp.model.bo.GomeGmpResUserBO;
import com.gome.gmp.model.bo.GomeGmpResUserOrgBO;
import com.gome.gmp.model.vo.GomeGmpResOrgVO;

/**
 * 用户与组织关联关系服务接口
 * 
 * @author wangchangtie
 *
 */
public interface GomeGmpResUserOrgBS {

	/**
	 * 添加用户与组织的关联关系
	 * 
	 * @param viewOrg
	 * @return
	 */
	public int addGmpResUserOrg(GomeGmpResOrgVO resOrgVo);

	/**
	 * 根据操作类型更改用户与组织的关系
	 * 
	 * @param userOrg
	 * @param operType
	 * @return
	 */
	public int changeUserOrgRelationByOperType(GomeGmpResUserOrgBO userOrg, String operType,GomeGmpResUserBO userBo);

	/**
	 * 修改用户与组织的关系
	 * 
	 * @param resOrgVo
	 * @param userBo 
	 * @return
	 */
	public int updateUserOrgRelation(GomeGmpResOrgVO resOrgVo, GomeGmpResUserBO userBo);
}
