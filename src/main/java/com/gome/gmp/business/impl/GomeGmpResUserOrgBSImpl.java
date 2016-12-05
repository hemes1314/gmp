package com.gome.gmp.business.impl;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gome.gmp.business.GomeGmpResUserOrgBS;
import com.gome.gmp.dao.GomeGmpResUserOrgDAO;
import com.gome.gmp.model.bo.GomeGmpResUserBO;
import com.gome.gmp.model.bo.GomeGmpResUserOrgBO;
import com.gome.gmp.model.vo.GomeGmpResOrgVO;

/**
 * 用户与组织关联关系业务逻辑层
 * 
 * @author wangchangtie
 *
 */
@Service("gomeGmpResUserOrgBS")
public class GomeGmpResUserOrgBSImpl extends BaseBS implements GomeGmpResUserOrgBS {

	private static Logger logger = LoggerFactory.getLogger(GomeGmpResUserOrgBSImpl.class);

	@Resource(name = "gomeGmpResUserOrgDAO")
	private GomeGmpResUserOrgDAO gomeGmpResUserOrgDAO;

	/**
	 * 添加用户与组织的关联关系
	 */
	@Override
	public int addGmpResUserOrg(GomeGmpResOrgVO resOrgVo) {
		int addResUserOrg = 0;
		if (StringUtils.isNotBlank(resOrgVo.getLeaderId())) {
			String orgLeaders[] = resOrgVo.getOrgLeader().split(";");
			for (String orgLeader : orgLeaders) {
				GomeGmpResUserOrgBO userOrg = new GomeGmpResUserOrgBO();
				userOrg.setUserTid(Integer.parseInt(orgLeader));
				userOrg.setOrgId(resOrgVo.getOrgId());
				if (gomeGmpResUserOrgDAO.addGmpResUserOrg(userOrg) > 0) {
					addResUserOrg++;
				}
			}
		}
		return addResUserOrg;
	}

	/**
	 * 根据操作类型更改用户与组织的关系
	 */
	@Override
	public int changeUserOrgRelationByOperType(GomeGmpResUserOrgBO userOrg, String operType,GomeGmpResUserBO userBo) {
		int operRow = 0;
		if (StringUtils.isNoneBlank(operType) && "del".equals(operType)) {
			operRow = gomeGmpResUserOrgDAO.delUserOrgRelationByOrgId(userOrg.getOrgId());
			logger.debug("用户:("+userBo.getUserId()+","+userBo.getUserName()+")操作=> operType:" + operType + ",orgId:" + userOrg.getOrgId() + ",操作行数:" + operRow);
		} else if (StringUtils.isNoneBlank(operType) && "upe".equals(operType)) {
			operRow = gomeGmpResUserOrgDAO.upeUserOrgRelation(userOrg);
			logger.debug("用户:("+userBo.getUserId()+","+userBo.getUserName()+")操作=> operType:" + operType + ",orgId:" + userOrg.getOrgId() + ",userTid:" + userOrg.getUserTid() + ",操作行数:" + operRow);
		}
		return operRow;
	}

	/**
	 * 修改用户与组织的关系
	 */
	@Override
	public int updateUserOrgRelation(GomeGmpResOrgVO resOrgVo,GomeGmpResUserBO userBo) {
		int upeResUserOrg = 0;
		GomeGmpResUserOrgBO userOrg = new GomeGmpResUserOrgBO();
		userOrg.setOrgId(resOrgVo.getOrgId());
		this.changeUserOrgRelationByOperType(userOrg, "del",userBo);
		if (StringUtils.isNotBlank(resOrgVo.getLeaderId())) {
			upeResUserOrg = addGmpResUserOrg(resOrgVo);
		}
		return upeResUserOrg;
	}
}
