package com.gome.gmp.business;

import java.util.List;

import com.gome.gmp.model.bo.GomeGmpResUserBO;
import com.gome.gmp.model.vo.GomeGmpResOrgVO;

/**
 * 
 *
 * @author be_kangfengping
 */
public interface GomeGmpResUserBS {

	int saveGomeGmpResUserBO(GomeGmpResUserBO gomeGmpResUserBO);

	int deleteGomeGmpResUserBOById(Long id);


	GomeGmpResUserBO userLogin(String username, String password);

	GomeGmpResUserBO testUserName(String username);

	GomeGmpResUserBO adminLogin(String username, String password);

	int updateAdminPassword(String password);

	GomeGmpResUserBO findGomeGmpResUserBOById(long id);

	int updateGomeGmpResUserLastLoginTime(GomeGmpResUserBO gomeGmpResUserBO);

	
	/**
	 * 清空用户表内orgId
	 * @param orgId
	 * @return
	 */
	public int clearGomeGmpResUserOrgId(String orgId);

	/**
	 * 更新用户信息
	 * 
	 * @param resOrgVo
	 * @return
	 */
	public int updateGomeGmpResUser(GomeGmpResOrgVO resOrgVo);

	/**
	 * 根据条件匹配用户
	 * 
	 * @param tempUserStr
	 * @param matchType
	 * @return
	 */
	public List<GomeGmpResUserBO> matchingUserByParms(String tempUserStr, String matchType);
	
	public int clearGomeGmpResUserOrgIdWithId(Long id);
}
