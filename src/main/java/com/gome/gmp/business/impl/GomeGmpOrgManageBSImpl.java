package com.gome.gmp.business.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gome.gmp.business.GomeGmpDictItemBS;
import com.gome.gmp.business.GomeGmpOrgManageBS;
import com.gome.gmp.business.GomeGmpResProjectBS;
import com.gome.gmp.business.GomeGmpResUserBS;
import com.gome.gmp.business.GomeGmpResUserOrgBS;
import com.gome.gmp.common.constant.Constants;
import com.gome.gmp.dao.GomeGmpResOrgDAO;
import com.gome.gmp.dao.GomeGmpResUserOrgDAO;
import com.gome.gmp.model.bo.GomeGmpResOrgBO;
import com.gome.gmp.model.bo.GomeGmpResUserBO;
import com.gome.gmp.model.bo.GomeGmpResUserOrgBO;
import com.gome.gmp.model.vo.GomeGmpResOrgVO;

@Service("gomeGmpOrgManageBS")
public class GomeGmpOrgManageBSImpl extends BaseBS implements GomeGmpOrgManageBS {

	private static Logger logger = LoggerFactory.getLogger(GomeGmpOrgManageBSImpl.class);

	@Resource
	private GomeGmpResUserBS gomeGmpResUserBS;

	@Resource
	private GomeGmpResUserOrgBS gomeGmpResUserOrgBS;

	@Resource
	private GomeGmpResOrgDAO gomeGmpResOrgDAO;

	@Resource
	private GomeGmpResUserOrgDAO gomeGmpResUserOrgDAO;

	@Resource
	private GomeGmpResProjectBS gomeGmpResProjectBS;
	
	@Resource
	private GomeGmpDictItemBS gomeGmpDictItemBS;

	@Override
	public int saveGomeGmpResOrgBO(GomeGmpResOrgBO gomeGmpResOrg) {
		int addOrgRow = gomeGmpResOrgDAO.addGomeGmpResOrg(gomeGmpResOrg);
		return addOrgRow;
	}

	@Override
	public GomeGmpResOrgBO findOrgBOByOrgId(String orgId) {
		if(orgId != null && orgId.length() % 2 != 0) {
			orgId = "0" + orgId;
		}
		return gomeGmpResOrgDAO.getOrgById(orgId);
	}

	/**
	 * 根据小组ID获取组员
	 */
	@Override
	public List<GomeGmpResOrgVO> findTeamMembers(String orgId, String userTids) {
		List<GomeGmpResOrgVO> resultList = gomeGmpResOrgDAO.findTeamMembers(orgId);
		if (StringUtils.isNotBlank(userTids) && resultList != null && resultList.size() > 0) {
			List<GomeGmpResOrgVO> addMemberList = new ArrayList<GomeGmpResOrgVO>();// 后增加的组员
			String userTidAry[] = userTids.split(";");
			for (GomeGmpResOrgVO orgVO : resultList) {
				for (String userTid : userTidAry) {
					if (orgVO.getId().toString().equals(userTid)) {
						addMemberList.add(orgVO);
					}
				}
			}
			resultList = addMemberList;
		}
		return resultList;
	}

	/**
	 * 添加组织
	 */
	@Override
	public Map<String, Object> addOrg(GomeGmpResOrgVO viewOrg) {
		if (viewOrg.getOrgParent() == null) {
			viewOrg.setOrgParent("");
		}
		List<GomeGmpResOrgVO> allOrgList = gomeGmpResOrgDAO.getAllOrgListByOrgParent(viewOrg.getOrgParent());
		if (allOrgList != null && allOrgList.size() > 0) {
			String newOrgId = this.getNewOrgId(allOrgList);
			viewOrg.setOrgId(viewOrg.getOrgParent() + newOrgId);
		} else {
			viewOrg.setOrgId(viewOrg.getOrgParent() + "01");
		}
		int addOrgRow = gomeGmpResOrgDAO.addGomeGmpResOrg(viewOrg);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("addOrgRow", addOrgRow);
		resultMap.put("addGmpOrgVo", viewOrg);
		int addUserRow = gomeGmpResUserBS.updateGomeGmpResUser(viewOrg);
		resultMap.put("addUserRow", addUserRow);
		gomeGmpResUserOrgBS.addGmpResUserOrg(viewOrg);
		return resultMap;
	}

	private String getNewOrgId(List<GomeGmpResOrgVO> allOrgList) {
		String resultOrgId = "";
		for (int i = 0; i < allOrgList.size(); i++) {
			int next = i + 1;
			resultOrgId = allOrgList.get(i).getOrgId();
			resultOrgId = resultOrgId.substring(resultOrgId.length() - 2, resultOrgId.length());
			if (next == allOrgList.size()) {
				Integer intNewOrgId = Integer.valueOf(resultOrgId);
				intNewOrgId++;
				resultOrgId = String.valueOf(intNewOrgId);
				if (resultOrgId.length() == 1) {
					resultOrgId = "0" + resultOrgId;
				}
				return resultOrgId;
			}
			String nextOrgId = allOrgList.get(next).getOrgId();
			nextOrgId = nextOrgId.substring(nextOrgId.length() - 2, nextOrgId.length());
			Integer intOrgId = Integer.valueOf(resultOrgId);
			Integer intNextId = Integer.valueOf(nextOrgId);
			if ((intNextId - intOrgId) == 1) {
				continue;
			}
			intOrgId++;
			resultOrgId = String.valueOf(intOrgId);
			if (resultOrgId.length() == 1) {
				resultOrgId = "0" + resultOrgId;
			}
			return resultOrgId;// w问题 1
		}
		return resultOrgId;
	}

	/**
	 * 获取我的组织
	 */
	@Override
	public List<GomeGmpResOrgVO> getMyOrganization(GomeGmpResUserBO userInfo, GomeGmpResOrgVO gmpResOrgVO) {
		List<GomeGmpResOrgVO> reslutList = new ArrayList<GomeGmpResOrgVO>();
		Map<String, Object> parmsMap = new HashMap<String, Object>();
		parmsMap.put("queryType", "myOrg");
		if(StringUtils.isNoneBlank(userInfo.getOrgId())){
			parmsMap.put("orgId", userInfo.getOrgId());
			List<String> orgParentList = new ArrayList<String>();
			for (int i = 1; i < userInfo.getOrgId().length()+1; i++) {
				if(i%2==0){
					orgParentList.add(userInfo.getOrgId().substring(0,i));
				}
			}
			parmsMap.put("orgParents", orgParentList.toArray());
		}
		List<GomeGmpResOrgVO> orgVoList = gomeGmpResOrgDAO.getOrgListByParms(parmsMap);
		if (orgVoList != null && orgVoList.size() > 0) {
			if (StringUtils.isBlank(userInfo.getAuthority()) && StringUtils.isNoneBlank(userInfo.getOrgId())) {
				GomeGmpResOrgBO orgBo = gomeGmpResOrgDAO.getOrgById(userInfo.getOrgId());
				if (orgBo.getOrgLevel() == Constants.ORGLEVEL_FOURTH) {
					List<GomeGmpResOrgVO> membersList = gomeGmpResOrgDAO.findTeamMembers(userInfo.getOrgId());
					orgVoList.addAll(membersList);
				}
			}
			reslutList = getResultOrgVoList(orgVoList);
		}
		return reslutList;
	}

	/**
	 * 组装需要的list
	 * 
	 * @param orgVoList
	 * @return
	 */
	private List<GomeGmpResOrgVO> getResultOrgVoList(List<GomeGmpResOrgVO> orgVoList) {
		List<GomeGmpResOrgVO> resultOrgVoList = new ArrayList<GomeGmpResOrgVO>();
		Iterator<GomeGmpResOrgVO> it = orgVoList.iterator();
		while (it.hasNext()) {
			GomeGmpResOrgVO d = it.next();
			Iterator<GomeGmpResOrgVO> temp = resultOrgVoList.iterator();
			boolean flag = false;
			while (temp.hasNext()) {
				GomeGmpResOrgVO dt = temp.next();
				if (StringUtils.isNotBlank(d.getOrgLeader()) && StringUtils.isNotBlank(dt.getOrgLeader()) && d.getOrgId().equals(dt.getOrgId()) && !d.getOrgLeader().equals(dt.getOrgLeader())) {
					dt.setOrgLeader(dt.getOrgLeader() + ";" + d.getOrgLeader());
					dt.setUserId(dt.getUserId() + ";" + d.getUserId());
					dt.setUserName(dt.getUserName() + ";" + d.getUserName());
					dt.setLeaderId(dt.getLeaderId() + ";" + d.getLeaderId());
					dt.setLeaderName(dt.getLeaderName() + ";" + d.getLeaderName());
					flag = true;
					break; 
				}
			}
			if (flag == false) {
				resultOrgVoList.add(d);
			}
		}
		return resultOrgVoList;
	}

	/**
	 * 删除组织
	 */
	@Override
	public int deleteOrg(String orgId, GomeGmpResUserBO userBo) {
		int delOrgRow = 0;
		// 查询所有当前组织下级组织
		GomeGmpResOrgVO org = new GomeGmpResOrgVO();
		org.setOrgId(orgId);
		List<GomeGmpResOrgVO> orgList = new ArrayList<GomeGmpResOrgVO>();
		orgList = this.getLowerLeverOrg(org, orgList);
		if (orgList != null && orgList.size() > 0) {
			List<GomeGmpResOrgVO> orgVoList = getResultOrgVoList(orgList);
			for (GomeGmpResOrgVO orgVo : orgVoList) {
				if (StringUtils.isNotBlank(orgVo.getOrgId())) {
					String relevantOrgId = orgVo.getOrgId();
					if (gomeGmpResOrgDAO.deleteOrgByOrgId(relevantOrgId) > 0) {
						delOrgRow++;
					}
					if (gomeGmpResUserBS.clearGomeGmpResUserOrgId(relevantOrgId) > 0) {
						logger.debug("deleteOrg 删除用户表内组织id成功    orgId:" + relevantOrgId);
					}
					GomeGmpResUserOrgBO userOrg = new GomeGmpResUserOrgBO();
					userOrg.setOrgId(relevantOrgId);
					gomeGmpResUserOrgBS.changeUserOrgRelationByOperType(userOrg, "del", userBo);
				}
			}
		}
		return delOrgRow;
	}

	/**
	 * 
	 * 递归取得所属下级组织
	 * 
	 * @param org
	 * @param orgList
	 * @return
	 */
	private List<GomeGmpResOrgVO> getLowerLeverOrg(GomeGmpResOrgVO org, List<GomeGmpResOrgVO> orgList) {
		orgList.add(org);
		List<GomeGmpResOrgVO> childList = gomeGmpResOrgDAO.getLowerLeverOrgByParent(org.getOrgId());
		for (int i = 0; i < childList.size(); i++) {
			GomeGmpResOrgVO child = childList.get(i);
			orgList = this.getLowerLeverOrg(child, orgList);
		}
		return orgList;
	}

	/**
	 * 根据父组织id获取下一级组织
	 */
	@Override
	public List<GomeGmpResOrgVO> getLowerLeverOrgByParent(String orgParent) {
		List<GomeGmpResOrgVO> childList = gomeGmpResOrgDAO.getLowerLeverOrgByParent(orgParent);
		List<GomeGmpResOrgVO> orgVoList = getResultOrgVoList(childList);
		return orgVoList;
	}

	/**
	 * 修改组织
	 */
	@Override
	public Map<String, Object> updateOrg(GomeGmpResOrgVO viewOrg, GomeGmpResUserBO userBo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(viewOrg.getOrgId())) {
			GomeGmpResOrgBO baseOrg = this.findOrgBOByOrgId(viewOrg.getOrgId());
			// 更新组织名称
			int upeOrgRow = gomeGmpResOrgDAO.updateOrgByOrgId(viewOrg);
			resultMap.put("upeOrgRow", upeOrgRow);
			// 组织名称如果修改则更新缓存
			if(baseOrg.getOrgName() != null && !baseOrg.getOrgName().equals(viewOrg.getOrgName())) {
				initOrgInfoEhcache(baseOrg.getOrgLevel());
			}
			// 将用户所在组织置空
			if (StringUtils.isNotBlank(baseOrg.getOrgLeader()) && !baseOrg.getOrgLeader().equals(viewOrg.getOrgLeader())) {
				gomeGmpResUserBS.clearGomeGmpResUserOrgId(viewOrg.getOrgId());
			}
			// 更新领导所在组织
			if (StringUtils.isNotBlank(viewOrg.getOrgLeader())) {
				int upeUserRow = gomeGmpResUserBS.updateGomeGmpResUser(viewOrg);
				resultMap.put("upeUserRow", upeUserRow);
			}
			// 更新领导与组织关系表
			int upeUserOrgRow = gomeGmpResUserOrgBS.updateUserOrgRelation(viewOrg, userBo);
			resultMap.put("upeUserOrgRow", upeUserOrgRow);
		}
		return resultMap;
	}

//	/**
//	 * 判断成员是否已在组织
//	 * 
//	 * @param orgVo
//	 */
//	@SuppressWarnings("unused")
//	private void checkOrgIncludeThisUser(GomeGmpResOrgVO orgVo) {
//		if (orgVo.getLeaderId() != null) {
//			GomeGmpResUserBO userCheck = gomeGmpResUserBS.testUserName(orgVo.getLeaderId());
//			if (userCheck.getOrgId() != null && !"".equals(userCheck.getOrgId()) && !orgVo.getOrgId().equals(userCheck.getOrgId())) {
//				GomeGmpResOrgBO groupBeen = this.findOrgBOByOrgId(userCheck.getOrgId());
//				String group = groupBeen.getOrgName();
//				group = this.getAllLevelOrgName(groupBeen, group);
//			}
//		}
//	}

	@Override
	public List<GomeGmpResOrgVO> getOrgFramework(String[] orgIds) {
		GomeGmpResOrgVO orgVO = new GomeGmpResOrgVO();
		if (orgIds != null && orgIds.length > 0) {
			orgVO.setOrgIdList(orgIds);
		}
		return gomeGmpResOrgDAO.getOrgFrameworkList(orgVO);
	}

	@Override
	public Map<String, Object> getUserOrgInfo(GomeGmpResUserBO userBo) {
		Map<String, Object> reslutMap = new HashMap<String, Object>();
		List<GomeGmpResOrgVO> userOrgList = new ArrayList<GomeGmpResOrgVO>();
		List<GomeGmpResOrgVO> orgBoList = new ArrayList<GomeGmpResOrgVO>();
		List<GomeGmpResOrgVO> firstUnitOrglist = getOrgListByLevel(Constants.ORGLEVEL_FIRST);
		if (userBo != null && firstUnitOrglist != null && firstUnitOrglist.size() > 0) {
			if (!"1".equals(userBo.getAuthority())) { // 用户不是管理员
				if (StringUtils.isNotEmpty(userBo.getOrgId())) {// 用户有组织
					// 根据用户所在组织获取用户的级别
					GomeGmpResOrgBO orgBo = this.findOrgBOByOrgId(userBo.getOrgId());
					if (orgBo != null && orgBo.getOrgLevel() != null) {
						reslutMap.put("userOrgLevel", orgBo.getOrgLevel());
						userOrgList = this.getUserQueryOrgList(orgBo, userOrgList);// 获取用户列表查询条件
						orgBoList = this.getQueryOrgBoList(orgBo, userOrgList);
						reslutMap.put("orgIds", userBo.getOrgId().split(" "));
						if ( orgBo.getOrgLevel() == Constants.ORGLEVEL_FOURTH && (orgBo.getOrgLeader()==null || (orgBo.getOrgLeader()!=null && orgBo.getOrgLeader().indexOf(userBo.getId().toString()) == -1)) ) {
							// 小组组员
							reslutMap.put("isMember", true);
						}
					}
				} else {// 用户没有组织
					userOrgList = null;
				}
			} else {
				userOrgList = firstUnitOrglist;
				orgBoList = this.getOrgFramework(null);
			}
		}
		reslutMap.put("orgBoList", orgBoList);
		reslutMap.put("userOrgList", userOrgList);
		return reslutMap;
	}
	
	//获取当前用户，组织列表查询权限
	private List<GomeGmpResOrgVO> getQueryOrgBoList(GomeGmpResOrgBO orgBo, List<GomeGmpResOrgVO> userOrgList) {
		List<GomeGmpResOrgVO> orgBoList = new ArrayList<GomeGmpResOrgVO>();
		if (userOrgList != null && userOrgList.size() > 0) {
			for (GomeGmpResOrgVO orgVo : userOrgList) {
				String[] orgIds = orgVo.getOrgId().split(" ");
				if (orgVo.getOrgLevel() == Constants.ORGLEVEL_FIRST && orgBo.getOrgLevel() <= Constants.ORGLEVEL_SECOND) {
					orgBoList = this.getOrgFramework(orgIds);
				} else if (orgVo.getOrgLevel() == Constants.ORGLEVEL_SECOND && orgBo.getOrgLevel() > Constants.ORGLEVEL_SECOND) {
					orgBoList = this.getOrgFramework(orgIds);
				}
			}
		}
		return orgBoList;
	}

	// 获取用户列表查询条件
	private List<GomeGmpResOrgVO> getUserQueryOrgList(GomeGmpResOrgBO orgBo, List<GomeGmpResOrgVO> userOrgList) {
		List<GomeGmpResOrgBO> resOrgList = new ArrayList<GomeGmpResOrgBO>();
		resOrgList.add(orgBo);
		resOrgList = getUserALLLevelOrgList(orgBo, resOrgList);
		if (resOrgList != null && resOrgList.size() > 0) {
			for (GomeGmpResOrgBO resOrgBo : resOrgList) {
				GomeGmpResOrgVO resOrgVo = new GomeGmpResOrgVO();
				resOrgVo.setOrgId(resOrgBo.getOrgId());
				resOrgVo.setOrgName(resOrgBo.getOrgName());
				resOrgVo.setOrgParent(resOrgBo.getOrgParent());
				resOrgVo.setOrgLevel(resOrgBo.getOrgLevel());
				resOrgVo.setOrgLeader(resOrgBo.getOrgLeader());
				userOrgList.add(resOrgVo);
			}
		}
		return userOrgList;
	}

	public List<GomeGmpResOrgBO> getUserALLLevelOrgList(GomeGmpResOrgBO orgBo, List<GomeGmpResOrgBO> resOrgList) {
		if (StringUtils.isNotBlank(orgBo.getOrgParent())) {
			GomeGmpResOrgBO resOrg = this.findOrgBOByOrgId(orgBo.getOrgParent());
			resOrgList.add(resOrg);
			return this.getUserALLLevelOrgList(resOrg, resOrgList);
		}
		return resOrgList;
	}

	/**
	 * 初始化组织缓存
	 */
	@Override
	public void initOrgInfoEhcache() {
		for (int level = 1; level <= Constants.ORGLEVEL_FOURTH; level++) {
			Map<String, Object> parmsMap = new HashMap<String, Object>();
			parmsMap.put("orgLevel", level);
			List<GomeGmpResOrgVO> orgVoList = gomeGmpResOrgDAO.getOrgListByParms(parmsMap);
			putEhcache(Constants.EHCACHE_TYPE_ORGLEVEL, Constants.EHCACHE_KEY_ORGLEVEL + level, orgVoList);
		}
	}
	
	/**
	 * 根据级别初始化组织缓存
	 * 
	 * @param level
	 */
	@Override
	public void initOrgInfoEhcache(int level) {
		Map<String, Object> parmsMap = new HashMap<String, Object>();
		parmsMap.put("orgLevel", level);
		List<GomeGmpResOrgVO> orgVoList = gomeGmpResOrgDAO.getOrgListByParms(parmsMap);
		putEhcache(Constants.EHCACHE_TYPE_ORGLEVEL, Constants.EHCACHE_KEY_ORGLEVEL + level, orgVoList);
	}

	/**
	 * 分组递归获取所有组织级别id
	 * 
	 * @param orgBo
	 * @param orgId
	 * @return
	 */
	@Override
	public String getAllLevelOrgId(GomeGmpResOrgBO orgBo, String orgId) {
		if (StringUtils.isNotBlank(orgBo.getOrgParent())) {
			GomeGmpResOrgBO resOrg = this.findOrgBOByOrgId(orgBo.getOrgParent());
			orgId = resOrg.getOrgId() + "-" + orgId;
			return this.getAllLevelOrgId(resOrg, orgId);
		}
		return orgId;
	}

	/**
	 * 分组递归获取所有组织级别名称
	 * 
	 * @param orgBo
	 * @param orgName
	 * @return
	 */
	@Override
	public String getAllLevelOrgName(GomeGmpResOrgBO orgBo, String orgName) {
		if (StringUtils.isNotBlank(orgBo.getOrgParent())) {
			GomeGmpResOrgBO resOrg = this.findOrgBOByOrgId(orgBo.getOrgParent());
			orgName = resOrg.getOrgName() + "-" + orgName;
			return this.getAllLevelOrgName(resOrg, orgName);
		}
		return orgName;
	}

	/**
	 * 分组递归获取所有组织的领导
	 * 
	 * @param orgBo
	 * @param orgName
	 * @return
	 */
	@Override
	public HashSet<String> getAllLevelLeaderId(GomeGmpResOrgBO orgBo, HashSet<String> leaderIds) {
		if (StringUtils.isNotBlank(orgBo.getOrgParent())) {
			GomeGmpResOrgBO resOrg = this.findOrgBOByOrgId(orgBo.getOrgParent());
			if (StringUtils.isNoneBlank(resOrg.getOrgLeader())) {
				for (String leaderId : resOrg.getOrgLeader().split(";")) {
					leaderIds.add(leaderId);
				}
			}
			return this.getAllLevelLeaderId(resOrg, leaderIds);
		}
		return leaderIds;
	}

	/**
	 * 根据组织级别获取组织
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<GomeGmpResOrgVO> getOrgListByLevel(Integer orglevel) {
		return getFromEhCache(Constants.EHCACHE_TYPE_ORGLEVEL, Constants.EHCACHE_KEY_ORGLEVEL + orglevel, List.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GomeGmpResOrgVO> getOrgQueryCondition(String[] orgIds, String queryType, GomeGmpResUserBO userBo) {
		if (orgIds != null && orgIds.length > 0 && "".equals(orgIds[0])) {
			return null;
		}
		List<GomeGmpResOrgVO> orgList = new ArrayList<GomeGmpResOrgVO>();
		if (StringUtils.isNotBlank(queryType) && "permission".equals(queryType) && orgIds == null) {
			Map<String, Object> userOrgInfoMap = this.getUserOrgInfo(userBo);
			orgList = (List<GomeGmpResOrgVO>) userOrgInfoMap.get("orgBoList");
		} else {
			orgList = this.getOrgFramework(orgIds);
		}
		return orgList;
	}

	/**
	 * 获取上级及上级的上级领导
	 * 
	 * @param userId
	 * @return
	 * @author wubin
	 */
	public Set<String> getAllLevelLeaderId(Integer userId) {
		// 获取用户所在组织
		GomeGmpResUserBO gomeGmpResUserBO = gomeGmpResUserBS.findGomeGmpResUserBOById(userId);
		// 根据组织id获取组织信息
		GomeGmpResOrgBO orgBo = findOrgBOByOrgId(gomeGmpResUserBO.getOrgId());
		if (orgBo == null) {
			return null;
		}
		// 获取所有等级领导id
		Set<String> leaderIds = getAllLevelLeaderId(orgBo, new HashSet<String>());
		return leaderIds;
	}
	
	/**
	 * 获取当前组织下所有的成员
	 */
	@Override
	public String[] getOrgAllMembers(String orgId) {
		Set<String> allMembersSet = new HashSet<String>();
		GomeGmpResOrgVO org = new GomeGmpResOrgVO();
		org.setOrgId(orgId);
		Map<String, Object> parmsMap = new HashMap<String, Object>();
		parmsMap.put("orgId", orgId);
		List<GomeGmpResOrgVO> orgList = gomeGmpResOrgDAO.getOrgListByParms(parmsMap);
		orgList = this.getLowerLeverOrg(org, orgList);
		orgList.addAll(gomeGmpResOrgDAO.findTeamMembers(orgId));
		if (orgList != null && orgList.size() > 0) {
			for (GomeGmpResOrgVO resOrgVO : orgList) {
				if(resOrgVO!=null && StringUtils.isNotBlank(resOrgVO.getOrgLeader())){
					allMembersSet.add(resOrgVO.getOrgLeader());
				}
				if(resOrgVO!=null && resOrgVO.getId()!=null && StringUtils.isNotBlank(resOrgVO.getId().toString())){
					allMembersSet.add(resOrgVO.getId().toString());
				}
			}
		}
		return (String[])allMembersSet.toArray(new String[allMembersSet.size()]);
	}
	
	public static void main(String[] args) {
		String testStr = "01010101";
		for (int i = 1; i < testStr.length()+1; i++) {
			if(i%2==0){
				testStr.substring(0,i);
			}
		}
	}
}