package com.gome.gmp.ws.organization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.gome.gmp.ws.ImportToDB;
import com.gome.gmp.ws.organization.pi.ZHRORGEHMD2;

/**
 * 组织数据同步dao
 * 
 * @author wubin
 */
@Repository("organizationDao")
public class OrganizationDaoImpl implements ImportToDB<ZHRORGEHMD2> {

	private static Logger logger = LoggerFactory.getLogger(OrganizationDaoImpl.class);

	@Resource
	JdbcTemplate jdbcTemplate;

	/**
	 * 导入数据
	 */
	@Override
	public int importData(List<ZHRORGEHMD2> datas) {
		int delete = deleteAll();
		logger.info("delete organization,size:" + delete);
		String sql = "INSERT INTO gome_gmp_res_unit (id,unit_name,unit_parent,unit_level) VALUES (?,?,?,?);";
		int[] rets = jdbcTemplate.batchUpdate(sql, convertToParam(datas));
		return rets.length;
	}

	/**
	 * 参数转化
	 */
	List<Object[]> convertToParam(List<ZHRORGEHMD2> datas) {
		List<Object[]> rets = new ArrayList<Object[]>();
		Map<String, Object[]> cached = new HashMap<String, Object[]>();
		for (ZHRORGEHMD2 zm : datas) {
			Integer parenId = Integer.valueOf(zm.getPARID());
			if (parenId != null && parenId == 0) {
				parenId = null;
			}
			if (cached.containsKey(zm.getOBJID())) {
				// 如果包含9999就证明日期是99991231 之类的，不然就是201x-xx-xx所以直接判断如下
				if (!zm.getENDDA().toString().contains("9999")) {
					logger.error("exsit objID:" + JSON.toJSONString(zm));
					continue;
				}
				cached.remove(zm.getOBJID());
			}
			Object[] tempParam = new Object[] { Integer.valueOf(zm.getOBJID()), zm.getSTEXT(), parenId, zm.getORGLV() };
			cached.put(zm.getOBJID(), tempParam);
		}
		for (String key : cached.keySet()) {
			rets.add(cached.get(key));
		}
		cached.clear();
		return rets;
	}

	/**
	 * 删除部门数据
	 */
	public int deleteAll() {
		return jdbcTemplate.update("delete from gome_gmp_res_unit  where id is not null");
	}
}