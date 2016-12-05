package com.gome.gmp.business.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.gome.gmp.business.GomeGmpWSHRBS;
import com.gome.gmp.dao.GomeGmpWSPersonDAO;
import com.gome.gmp.dao.GomeGmpWSUnitDAO;
import com.gome.gmp.model.bo.GomeGmpResUnitBO;
import com.gome.gmp.model.bo.GomeGmpResUserBO;
import com.gome.gmp.ws.organization.pi.ZHRORGEHMD2;
import com.gome.gmp.ws.person.PersonDaoImpl;
import com.gome.gmp.ws.person.pi.ZHRPAMD2;

/**
 * WebService中HR接口数据导入
 *
 * @author wubin
 */
@Service("gomeGmpWSHRBS")
public class GomeGmpWSHRBSImpl implements GomeGmpWSHRBS {

	private static Logger logger = LoggerFactory.getLogger(PersonDaoImpl.class);

	@Resource
	private GomeGmpWSPersonDAO gomeGmpWSPersonDAO;

	@Resource
	private GomeGmpWSUnitDAO gomeGmpWSUnitDAO;

	@Resource
	private SqlSessionFactory sqlSessionFactory;

	/**
	 * 导入人员数据
	 * 
	 * @see com.gome.gmp.business.GomeGmpWSHRBS#importPersonData(java.util.List)
	 */
	@Override
	public int importPersonData(List<ZHRPAMD2> datas) {
		if (datas == null || datas.size() == 0) {
			return 0;
		}
		List<String> ids = new ArrayList<String>();
		Map<String, ZHRPAMD2> addOrUpdate = new HashMap<String, ZHRPAMD2>();
		StringBuilder delStrs = new StringBuilder();
		StringBuilder idStrs = new StringBuilder();
		for (ZHRPAMD2 ZHRPAMD2 : datas) {
			if ("G2".equals(ZHRPAMD2.getPERSK()) || StringUtils.isBlank(ZHRPAMD2.getZADACCOUNT())) {
				// 删除离职和没有员工编号的员工
				delStrs.append("'").append(ZHRPAMD2.getZADACCOUNT()).append("'").append(",");
			} else {
				// 未离职集合
				idStrs.append("'").append(ZHRPAMD2.getZADACCOUNT()).append("'").append(",");
				ids.add(ZHRPAMD2.getZADACCOUNT());
				addOrUpdate.put(ZHRPAMD2.getZADACCOUNT(), ZHRPAMD2);
			}
		}
		// 更新已经存在但是没有员工编号的存在
		// List<String> oldExsits = updatePernrForUserNamesReturnExsit(datas);
		// 删除离职的员工
		int deleteCount = 0;
		if (delStrs.length() > 1) {
			String tempStrs = delStrs.substring(0, delStrs.length() - 1);
			deleteCount = gomeGmpWSPersonDAO.deleteGomeGmpResUserBOByPernr(tempStrs);
		}
		List<String> exstisIds = null;
		// 查找出存在的员工
		if (idStrs.length() > 1) {
			String tempStrs = idStrs.substring(0, idStrs.length() - 1);
			exstisIds = gomeGmpWSPersonDAO.findGomeGmpResUserBOByPernr(tempStrs);
		}
		if (exstisIds == null) {
			exstisIds = new ArrayList<String>();
		}
		// 计算出新增和需要修改的员工
		for (String exstisId : exstisIds) {
			ids.remove(exstisId);
		}
		List<ZHRPAMD2> adds = new ArrayList<ZHRPAMD2>();
		for (String id : ids) {
			// 已存在的用户，改为更改
			// oldExsits.contains(id) ||
			if (exstisIds.contains(id)) {
				continue;
			}
			adds.add(addOrUpdate.get(id));
			exstisIds.add(id);
		}
		// 新增
		List<GomeGmpResUserBO> rets = new ArrayList<GomeGmpResUserBO>();
		for (ZHRPAMD2 zm : adds) {
			GomeGmpResUserBO gomeGmpResUserBO = new GomeGmpResUserBO();
			gomeGmpResUserBO.setPernr(zm.getPERNR());
			gomeGmpResUserBO.setUserId(zm.getZADACCOUNT());
			gomeGmpResUserBO.setUserName(zm.getVORNA() + zm.getNACHN());
			gomeGmpResUserBO.setUnitId(getOrgId(zm));
			gomeGmpResUserBO.setEmail(zm.getEMAIL());
			rets.add(gomeGmpResUserBO);
		}
		int inRets = 0;
		if (rets != null && rets.size() > 0) {
			inRets = gomeGmpWSPersonDAO.saveGomeGmpResUserBO(rets);
		}
		// 修改
		List<ZHRPAMD2> modifys = new ArrayList<ZHRPAMD2>();
		for (String id : exstisIds) {
			modifys.add(addOrUpdate.get(id));
		}
		rets = new ArrayList<GomeGmpResUserBO>();
		for (ZHRPAMD2 zm : modifys) {
			GomeGmpResUserBO gomeGmpResUserBO = new GomeGmpResUserBO();
			gomeGmpResUserBO.setPernr(zm.getPERNR());
			gomeGmpResUserBO.setUserId(zm.getZADACCOUNT());
			gomeGmpResUserBO.setUserName(zm.getVORNA() + zm.getNACHN());
			gomeGmpResUserBO.setUnitId(getOrgId(zm));
			gomeGmpResUserBO.setEmail(zm.getEMAIL());
			rets.add(gomeGmpResUserBO);
		}
		int upRets = 0;
		if (rets != null && rets.size() > 0) {
			for (GomeGmpResUserBO userBO : rets) {
				int upRet = gomeGmpWSPersonDAO.updateGomeGmpResUserBO(userBO);
				if (upRet > 0) {
					upRets++;
				}
			}
		}
		if (logger.isInfoEnabled()) {
			logger.info("import person data :delete=" + deleteCount + ",update=" + upRets + ",insert:" + inRets);
		}
		return inRets + upRets + deleteCount;
	}

	/**
	 * 导入部门数据
	 * 
	 * @see com.gome.gmp.business.GomeGmpWSHRBS#importOrganizationData(java.util.List)
	 */
	@Override
	public int importOrganizationData(List<ZHRORGEHMD2> datas) {
		if (datas == null || datas.size() == 0) {
			return 0;
		}
		int delete = gomeGmpWSUnitDAO.deleteGomeGmpResUnits();
		logger.info("delete organization,size:" + delete);
		List<GomeGmpResUnitBO> params = convertToOrgParam(datas);
		int rets = gomeGmpWSUnitDAO.saveGomeGmpResUnitBO(params);
		return rets;
	}

	/**
	 * 获取部门id
	 * 
	 * @param zm
	 * @return
	 * @author wubin
	 */
	private int getOrgId(ZHRPAMD2 zm) {
		String non = "00000000";
		if (!non.equals(zm.getORGEHL5())) {
			non = zm.getORGEHL5();
		}
		if (!non.equals(zm.getORGEHL4())) {
			non = zm.getORGEHL4();
		}
		if (!non.equals(zm.getORGEHL3())) {
			non = zm.getORGEHL3();
		}
		if (!non.equals(zm.getORGEHL2())) {
			non = zm.getORGEHL2();
		}
		non = zm.getORGEHL1();
		return NumberUtils.toInt(non);
	}
	/**
	 * 更新已经存在但是没有员工编号的存在
	 * 
	 * @param datas
	 * @return
	 * @author wubin
	 */
	// public List<String> updatePernrForUserNamesReturnExsit(List<ZHRPAMD2> datas) {
	// List<String> retexsit = new ArrayList<String>();
	// for (ZHRPAMD2 ZHRPAMD2 : datas) {
	// String pernr = ZHRPAMD2.getPERNR();
	// String username = ZHRPAMD2.getZADACCOUNT();
	// int tmp = updatePernrForUserName(pernr, username);
	// if (tmp != 0) {
	// retexsit.add(ZHRPAMD2.getPERNR());
	// }
	// }
	// return retexsit;
	// }

	/**
	 * 更新已经存在但是没有员工编号的存在
	 * 
	 * @param pernr
	 * @param username
	 * @return
	 * @author wubin
	 */
	// public int updatePernrForUserName(String pernr, String username) {
	// GomeGmpResUserBO userBO = new GomeGmpResUserBO();
	// userBO.setPernr(pernr);
	// userBO.setUserName(username);
	// int tmp = gomeGmpWSPersonDAO.updatePernrForUserName(userBO);
	// return tmp;
	// }
	/**
	 * 参数转化
	 * 
	 * @param datas
	 * @return
	 * @author wubin
	 */
	private List<GomeGmpResUnitBO> convertToOrgParam(List<ZHRORGEHMD2> datas) {
		List<GomeGmpResUnitBO> rets = new ArrayList<GomeGmpResUnitBO>();
		Map<String, GomeGmpResUnitBO> cached = new HashMap<String, GomeGmpResUnitBO>();
		for (ZHRORGEHMD2 zm : datas) {
			Integer parentId = Integer.valueOf(zm.getPARID());
			if (parentId != null && parentId == 0) {
				parentId = null;
			}
			if (cached.containsKey(zm.getOBJID())) {
				// 如果包含9999就证明日期是99991231 之类的，不然就是201x-xx-xx所以直接判断如下
				if (!zm.getENDDA().toString().contains("9999")) {
					logger.error("exsit objID:" + JSON.toJSONString(zm));
					continue;
				}
				cached.remove(zm.getOBJID());
			}
			GomeGmpResUnitBO gomeGmpResUnitBO = new GomeGmpResUnitBO();
			gomeGmpResUnitBO.setId(Long.valueOf(zm.getOBJID()));
			gomeGmpResUnitBO.setUnitName(zm.getSTEXT());
			gomeGmpResUnitBO.setUnitParent(parentId);
			gomeGmpResUnitBO.setUnitLevel(zm.getORGLV().intValue());
			cached.put(zm.getOBJID(), gomeGmpResUnitBO);
		}
		for (String key : cached.keySet()) {
			rets.add(cached.get(key));
		}
		cached.clear();
		return rets;
	}
}
