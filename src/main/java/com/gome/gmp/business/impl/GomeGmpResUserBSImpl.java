package com.gome.gmp.business.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gome.gmp.business.GomeGmpResUserBS;
import com.gome.gmp.dao.GomeGmpResUserDAO;
import com.gome.gmp.model.bo.GomeGmpResUserBO;
import com.gome.gmp.model.vo.GomeGmpResOrgVO;

/**
 * 用户业务逻辑层
 * 
 * @author be_kangfengping
 */
@Service("gomeGmpResUserBS")
public class GomeGmpResUserBSImpl extends BaseBS implements GomeGmpResUserBS {

	@Resource(name = "gomeGmpResUserDAO")
	private GomeGmpResUserDAO gomeGmpResUserDAO;

	@Override
	public int saveGomeGmpResUserBO(GomeGmpResUserBO gomeGmpResUserBO) {
		return gomeGmpResUserDAO.saveGomeGmpResUserBO(gomeGmpResUserBO);
	}

	@Override
	public int deleteGomeGmpResUserBOById(Long id) {
		return gomeGmpResUserDAO.deleteGomeGmpResUserBOById(id);
	}

	@Override
	public GomeGmpResUserBO userLogin(String username, String password) {
		return gomeGmpResUserDAO.userLogin(username, password);
	}

	@Override
	public GomeGmpResUserBO testUserName(String username) {
		return gomeGmpResUserDAO.testUserName(username);
	}

	@Override
	public GomeGmpResUserBO findGomeGmpResUserBOById(long id) {
		return gomeGmpResUserDAO.findGomeGmpResUserBOById(id);
	}

	@Override
	public int clearGomeGmpResUserOrgId(String orgId) {
		return gomeGmpResUserDAO.clearGomeGmpResUserOrgId(orgId);
	}

	@Override
	public int updateGomeGmpResUserLastLoginTime(GomeGmpResUserBO gomeGmpResUserBO) {
		return gomeGmpResUserDAO.updateGomeGmpResUserLastLoginTime(gomeGmpResUserBO);
	}

	@Override
	public int clearGomeGmpResUserOrgIdWithId(Long id) {
		return gomeGmpResUserDAO.clearGomeGmpResUserOrgIdWithId(id);
	}

	@Override
	public GomeGmpResUserBO adminLogin(String username, String password) {
		return gomeGmpResUserDAO.adminLogin(username, password);
	}

	@Override
	public int updateAdminPassword(String password) {
		return gomeGmpResUserDAO.updateAdminPassword(password);
	}

	@Override
	public int updateGomeGmpResUser(GomeGmpResOrgVO resOrgVo) {
		int updateResUser = 0;
		String orgLeaders[] = resOrgVo.getOrgLeader().split(";");
		for (String orgLeader : orgLeaders) {
			GomeGmpResUserBO userBO = new GomeGmpResUserBO();
			userBO.setId(Long.parseLong(orgLeader));
			userBO.setOrgId(resOrgVo.getOrgId());
			if (gomeGmpResUserDAO.updateGmpResUserById(userBO) > 0) {
				updateResUser++;
			}
		}
		return updateResUser;
	}

	/**
	 * 根据条件匹配用户
	 */
	@Override
	public List<GomeGmpResUserBO> matchingUserByParms(String tempUserStr, String matchType) {
		String tempUserStrs[] = tempUserStr.split(";");
		String matchUserStr = "";
		if (tempUserStrs.length > 1) {
			matchUserStr = tempUserStrs[tempUserStrs.length - 1];
		} else {
			matchUserStr = tempUserStr;
		}
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("matchUserStr", matchUserStr);
		queryMap.put("matchType", matchType);
		return gomeGmpResUserDAO.matchingUserByParms(queryMap);
	}
}
