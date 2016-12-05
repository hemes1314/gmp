package com.gome.gmp.business.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gome.gmp.business.GomeGmpResRoleBS;
import com.gome.gmp.dao.GomeGmpResRoleDAO;
import com.gome.gmp.model.vo.GomeGmpResRoleVO;

/**
 * 关联角色
 *
 * @author wubin
 */
@Service("gomeGmpResRoleBS")
public class GomeGmpResRoleBSImpl implements GomeGmpResRoleBS {

	@Resource(name = "gomeGmpResRoleDAO")
	private GomeGmpResRoleDAO gomeGmpResRoleDAO;

	/**
	 * 增加关联角色
	 * 
	 * @param gomeGmpResProjectVO
	 * @return
	 * @author wubin
	 */
	@Transactional
	@Override
	public Long saveResRole(GomeGmpResRoleVO gomeGmpResRoleVO) {
		// 查询是否有此角色
		List<GomeGmpResRoleVO> roles = gomeGmpResRoleDAO.findGomeGmpResRoleVO(gomeGmpResRoleVO);
		if (roles == null || roles.size() <= 0) {
			// 没有则添加
			gomeGmpResRoleDAO.saveGomeGmpResRoleBO(gomeGmpResRoleVO);
			return gomeGmpResRoleVO.getId();
		} else {
			return roles.get(0).getId();
		}
	}

	/**
	 * 生成默认的五个角色
	 * 
	 * @author wubin
	 */
	@Transactional
	@Override
	public List<GomeGmpResRoleVO> defaultRoleInfo() {
		// 五个默认角色
		String[] roleNames = new String[] { "产品负责人", "开发负责人", "测试负责人", "UED负责人", "PMO负责人" };
		GomeGmpResRoleVO gomeGmpResRoleVO = new GomeGmpResRoleVO();
		gomeGmpResRoleVO.setRoleNames(roleNames);
		List<GomeGmpResRoleVO> existsRoleNames = gomeGmpResRoleDAO.findGomeGmpResRoleVO(gomeGmpResRoleVO);
		
		if (existsRoleNames.size() < 5) {
			// 数组转Map
			Map<String, String> roleNamesMap = new HashMap<String, String>();
			for (String role : roleNames) {
				roleNamesMap.put(role, null);
			}
			// 需初始化的五个角色
			for (GomeGmpResRoleVO role : existsRoleNames) {
				if (roleNamesMap.containsKey(role.getRoleName())) {
					roleNamesMap.remove(role.getRoleName());
				}
			}
			List<GomeGmpResRoleVO> saveList = new ArrayList<GomeGmpResRoleVO>();
			for (String roleName : roleNamesMap.keySet()) {
				GomeGmpResRoleVO role = new GomeGmpResRoleVO();
				role.setRoleName(roleName);
				saveList.add(role);
			}
			// 保存
			for (GomeGmpResRoleVO role : saveList) {
				gomeGmpResRoleDAO.saveGomeGmpResRoleBO(role);
			}
			existsRoleNames.addAll(saveList);
		}
		// 结果排序
		Map<String, GomeGmpResRoleVO> list2Map = new HashMap<String, GomeGmpResRoleVO>();
		for(GomeGmpResRoleVO role : existsRoleNames) {
			list2Map.put(role.getRoleName(), role);
		}
		existsRoleNames = new ArrayList<GomeGmpResRoleVO>();
		for(String roleName : roleNames) {
			existsRoleNames.add(list2Map.get(roleName));
		}
		return existsRoleNames;
	}
}
