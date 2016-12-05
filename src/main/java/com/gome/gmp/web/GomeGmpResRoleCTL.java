package com.gome.gmp.web;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gome.framework.base.BaseRestController;
import com.gome.gmp.business.GomeGmpResRoleBS;
import com.gome.gmp.model.vo.GomeGmpResRoleVO;

/**
 * 角色
 *
 * @author wubin
 */
@RestController
@RequestMapping("/role")
public class GomeGmpResRoleCTL extends BaseRestController {

	@Resource
	private GomeGmpResRoleBS gomeGmpResRelatedUserBS;

	/**
	 * 增加关联角色
	 * 
	 * @param pro
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Long save(@RequestBody GomeGmpResRoleVO gomeGmpResRoleVO) {
		long roleId = gomeGmpResRelatedUserBS.saveResRole(gomeGmpResRoleVO);
		return roleId;
	}
}
