package com.gome.gmp.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gome.gmp.model.bo.GomeGmpResOrgBO;
import com.gome.gmp.model.vo.GomeGmpResOrgVO;

/**
 * 组织管理DAO
 * 
 * @author wangchangtie
 *
 */
@Repository("gomeGmpResOrgDAO")
public interface GomeGmpResOrgDAO {

	/**
	 * 添加组织
	 * 
	 * @param gmpResOrg
	 * @return
	 */
	public int addGomeGmpResOrg(GomeGmpResOrgBO gmpResOrg);

	/**
	 * 根据参数获取组织列表
	 * 
	 * @param parmsMap
	 * @return
	 */
	public List<GomeGmpResOrgVO> getOrgListByParms(Map<String, Object> parmsMap);

	/**
	 * 根据父级部门获取下级组织
	 * 
	 * @param orgParent
	 * @return
	 */
	public List<GomeGmpResOrgVO> getLowerLeverOrgByParent(String orgParent);

	/**
	 * 根据组织id删除组织
	 * 
	 * @param orgId
	 * @return
	 */
	public int deleteOrgByOrgId(String orgId);
	
	/**
	 * 获取小组组员
	 * @param orgId
	 * @return
	 */
	public List<GomeGmpResOrgVO> findTeamMembers(String orgId);
	
	public List<GomeGmpResOrgBO> findGomeGmpResSelectOrgBO(GomeGmpResOrgVO gomeGmpResOrgVO);

	public GomeGmpResOrgBO getOrgById(String orgId);

	public List<GomeGmpResOrgVO> getAllOrgListByOrgParent(String orgParent);

	public int updateOrgByOrgId(GomeGmpResOrgBO orgBO);
	
	public List<GomeGmpResOrgVO> getOrgFrameworkList(GomeGmpResOrgVO orgVO);
}