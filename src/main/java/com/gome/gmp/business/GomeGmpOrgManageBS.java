package com.gome.gmp.business;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gome.gmp.model.bo.GomeGmpResOrgBO;
import com.gome.gmp.model.bo.GomeGmpResUserBO;
import com.gome.gmp.model.vo.GomeGmpResOrgVO;

public interface GomeGmpOrgManageBS {

	int saveGomeGmpResOrgBO(GomeGmpResOrgBO gomeGmpResOrgBO);
	
	/**
	 * 根据id获取部门信息
	 * @param orgId
	 * @return
	 */
	public GomeGmpResOrgBO findOrgBOByOrgId(String orgId);
	
	/**
	 * 根据小组ID获取组员
	 * @param orgId
	 * @param userTids 用户id
	 * @return
	 */
	public List<GomeGmpResOrgVO> findTeamMembers(String orgId,String userTids);

	/**
	 * 添加组织
	 * 
	 * @param viewOrg
	 * @return
	 */
	public Map<String, Object> addOrg(GomeGmpResOrgVO viewOrg);

	/**
	 * 获取我的组织
	 * 
	 * @param userInfo
	 * @param gomeGmpResOrgVO
	 * @return
	 */
	public List<GomeGmpResOrgVO> getMyOrganization(GomeGmpResUserBO userInfo, GomeGmpResOrgVO gmpResOrgVO);

	/**
	 * 删除组织
	 * 
	 * @param orgId
	 * @param userBo 
	 * @return
	 */
	public int deleteOrg(String orgId, GomeGmpResUserBO userBo);

	/**
	 * 根据父组织id获取下一级组织
	 * 
	 * @param orgParent
	 * @return
	 */
	public List<GomeGmpResOrgVO> getLowerLeverOrgByParent(String orgParent);
	
	/**
	 * 修改组织
	 * 
	 * @param viewOrg
	 * @param userBo 
	 * @return
	 */
	public Map<String, Object> updateOrg(GomeGmpResOrgVO viewOrg, GomeGmpResUserBO userBo);
	
	/**
	 * 获取用户组织结构
	 * @param orgIds
	 * @param userBo
	 * @return
	 */
	public List<GomeGmpResOrgVO> getOrgFramework(String[] orgIds);
	
	/**
	 * 初始化组织缓存
	 */
	public void initOrgInfoEhcache();
	
	/**
	 * 根据级别初始化组织缓存
	 * 
	 * @param level
	 */
	public void initOrgInfoEhcache(int level);
	
	/**
	 * 获取用户部门信息
	 * @param userBo
	 * @return
	 */
	Map<String, Object> getUserOrgInfo(GomeGmpResUserBO userBo);
	
	/**
	 * 获取所有组织级别ID
	 * @param orgBo
	 * @param orgId
	 * @return
	 */
	public String getAllLevelOrgId(GomeGmpResOrgBO orgBo, String orgId);
	
	/**
	 * 获取所有组织级别名称
	 * @param orgBo
	 * @param orgName
	 * @return
	 */
	public String getAllLevelOrgName(GomeGmpResOrgBO orgBo, String orgName);
	
	/**
	 * 分组递归获取所有组织的领导
	 * @param orgBo
	 * @param orgName
	 * @return
	 */
	HashSet<String> getAllLevelLeaderId(GomeGmpResOrgBO orgBo, HashSet<String> leaderIds);
	
	/**
	 * 根据组织级别获取组织
	 * @param orglevel
	 * @return
	 */
	public List<GomeGmpResOrgVO> getOrgListByLevel(Integer orglevel);
	
	/**
	 * 获取所有级别组织列表
	 * @param orgBo
	 * @param orgName
	 * @return
	 */
	public List<GomeGmpResOrgBO> getUserALLLevelOrgList(GomeGmpResOrgBO orgBo,List<GomeGmpResOrgBO> resOrgList);
	
	/**
	 * 
	 * @param orgIds
	 * @param queryType 
	 * @param userBo
	 * @return
	 */
	public List<GomeGmpResOrgVO> getOrgQueryCondition(String[] orgIds,String queryType, GomeGmpResUserBO userBo);
	
	/**
	 * 获取上级及上级的上级领导
	 * 
	 * @param userId
	 * @return
	 * @author wubin
	 */
	public Set<String> getAllLevelLeaderId(Integer userId);
	
	/**
	 * 获取当前组织下所有的成员
	 * @param orgId
	 * @return
	 */
	public String[] getOrgAllMembers(String orgId);
}
