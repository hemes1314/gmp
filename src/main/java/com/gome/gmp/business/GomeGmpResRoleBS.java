package com.gome.gmp.business;

import java.util.List;

import com.gome.gmp.model.vo.GomeGmpResRoleVO;

/**
 * 角色
 *
 * @author wubin
 */
public interface GomeGmpResRoleBS {

	/**
	 * 保存角色
	 * 
	 * @param gomeGmpResProjectVO
	 * @return
	 * @author wubin
	 */
	Long saveResRole(GomeGmpResRoleVO gomeGmpResRoleVO);

	/**
	 * 生成默认的五个角色
	 * 
	 * @author wubin
	 * @return
	 */
	List<GomeGmpResRoleVO> defaultRoleInfo();
}
