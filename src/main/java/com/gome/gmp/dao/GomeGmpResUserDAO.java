package com.gome.gmp.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gome.gmp.model.bo.GomeGmpResUserBO;

/**
 * @author Administrator
 */
@Repository("gomeGmpResUserDAO")
public interface GomeGmpResUserDAO {

	int saveGomeGmpResUserBO(GomeGmpResUserBO gomeGmpResUserBO);

	int deleteGomeGmpResUserBOById(Long id);

	public int updateGmpResUserById(GomeGmpResUserBO gomeGmpResUserBO);

	List<GomeGmpResUserBO> findGomeGmpResUser(GomeGmpResUserBO bo);

	GomeGmpResUserBO userLogin(String username, String password);

	GomeGmpResUserBO testUserName(String username);

	GomeGmpResUserBO findGomeGmpResUserBOById(long id);

	int updateGomeGmpResUserLastLoginTime(GomeGmpResUserBO gomeGmpResUserBO);

	GomeGmpResUserBO adminLogin(String username, String password);

	int updateAdminPassword(String password);

	/**
	 * 根据参数模糊匹配用户
	 * 
	 * @param queryMap
	 * @return
	 */
	public List<GomeGmpResUserBO> matchingUserByParms(Map<String, Object> queryMap);

	public int clearGomeGmpResUserOrgId(String orgId);

	public int clearGomeGmpResUserOrgIdWithId(Long id);
}