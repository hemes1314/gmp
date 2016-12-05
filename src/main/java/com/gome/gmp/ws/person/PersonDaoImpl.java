package com.gome.gmp.ws.person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.gome.gmp.ws.ImportToDB;
import com.gome.gmp.ws.person.pi.ZHRPAMD2;

/**
 * 人员同步dao
 * 
 * @author wubin
 */
@Repository("personDao")
public class PersonDaoImpl implements ImportToDB<ZHRPAMD2> {

	private static Logger logger = LoggerFactory.getLogger(PersonDaoImpl.class);

	public static final String COMMON_GROUP_NAME = "PF_COMMON_QUERY";

	@Resource
	JdbcTemplate jdbcTemplate;

	/**
	 * 组织数据格式化
	 */
	String getOrgId(ZHRPAMD2 zm) {
		String non = "00000000";
		if (!non.equals(zm.getORGEHL5())) {
			return zm.getORGEHL5();
		}
		if (!non.equals(zm.getORGEHL4())) {
			return zm.getORGEHL4();
		}
		if (!non.equals(zm.getORGEHL3())) {
			return zm.getORGEHL3();
		}
		if (!non.equals(zm.getORGEHL2())) {
			return zm.getORGEHL2();
		}
		return zm.getORGEHL1();
	}

	/**
	 * 导入数据
	 */
	@Override
	public int importData(List<ZHRPAMD2> datas) {
		List<String> ids = new ArrayList<String>();
		Map<String, ZHRPAMD2> addOrUpdate = new HashMap<String, ZHRPAMD2>();
		StringBuilder delStrs = new StringBuilder();
		StringBuilder idStrs = new StringBuilder();
		for (ZHRPAMD2 ZHRPAMD2 : datas) {
			if ("G2".equals(ZHRPAMD2.getPERSK())) {
				// 删除离职
				delStrs.append("'").append(ZHRPAMD2.getPERNR()).append("'").append(",");
			} else {
				// 未离职集合
				idStrs.append("'").append(ZHRPAMD2.getPERNR()).append("'").append(",");
				ids.add(ZHRPAMD2.getPERNR());
				addOrUpdate.put(ZHRPAMD2.getPERNR(), ZHRPAMD2);
			}
		}
		// 更新已经存在但是没有员工编号的存在
		List<String> oldExsits = updatePernrForUserNamesReturnExsit(datas);
		// 删除离职的员工
		int deleteCount = 0;
		if (delStrs.length() > 1) {
			String deleteSql = "delete from gome_gmp_res_user where pernr in (%s)";
			String tempStrs = delStrs.substring(0, delStrs.length() - 1);
			deleteSql = String.format(deleteSql, tempStrs);
			deleteCount = jdbcTemplate.update(deleteSql);
		}
		List<String> exstisIds = null;
		// 查找出存在的员工
		if (idStrs.length() > 1) {
			String getPERNRSql = "select pernr from gome_gmp_res_user where pernr in (%s)";
			String tempStrs = idStrs.substring(0, idStrs.length() - 1);
			getPERNRSql = String.format(getPERNRSql, tempStrs);
			exstisIds = jdbcTemplate.queryForList(getPERNRSql, String.class);
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
			if (oldExsits.contains(id) || exstisIds.contains(id)) {
				continue;
			}
			adds.add(addOrUpdate.get(id));
		}
		// 新增
		String insertSql = "INSERT INTO gome_gmp_res_user " + "(pernr,user_id, user_name, unit_id,email) VALUES (?, ?, ?, ?, ?)";
		List<Object[]> rets = new ArrayList<Object[]>();
		for (ZHRPAMD2 zm : adds) {
			Object[] param = new Object[] { zm.getPERNR(), zm.getZADACCOUNT(), zm.getVORNA() + zm.getNACHN(), getOrgId(zm), zm.getEMAIL() };
			rets.add(param);
		}
		int[] inRets = jdbcTemplate.batchUpdate(insertSql, rets);
		// 修改
		List<ZHRPAMD2> modifys = new ArrayList<ZHRPAMD2>();
		for (String id : exstisIds) {
			modifys.add(addOrUpdate.get(id));
		}
		String updateSql = "update gome_gmp_res_user set user_name=?,unit_id=?,email=?,update_time=now() where pernr=?";
		rets = new ArrayList<Object[]>();
		for (ZHRPAMD2 zm : modifys) {
			Object[] param = new Object[] { zm.getVORNA() + zm.getNACHN(), getOrgId(zm), zm.getEMAIL(), zm.getPERNR() };
			rets.add(param);
		}
		int[] upRets = jdbcTemplate.batchUpdate(updateSql, rets);
		if (logger.isInfoEnabled()) {
			logger.info("import person data :delete=" + deleteCount + ",update=" + 0 + ",insert:" + inRets.length);
		}
		return inRets.length + upRets.length + deleteCount;
	}

	/**
	 * 更新已经存在的人员
	 */
	public List<String> updatePernrForUserNamesReturnExsit(List<ZHRPAMD2> datas) {
		List<String> retexsit = new ArrayList<String>();
		for (ZHRPAMD2 ZHRPAMD2 : datas) {
			String pernr = ZHRPAMD2.getPERNR();
			String username = ZHRPAMD2.getZADACCOUNT();
			int tmp = updatePernrForUserName(pernr, username);
			if (tmp != 0) {
				retexsit.add(ZHRPAMD2.getPERNR());
			}
		}
		return retexsit;
	}

	/**
	 * 更新人员工号
	 */
	public int updatePernrForUserName(String pernr, String username) {
		return jdbcTemplate.update("update gome_gmp_res_user set pernr='" + pernr + "' where user_id='" + username + "'");
	}
}