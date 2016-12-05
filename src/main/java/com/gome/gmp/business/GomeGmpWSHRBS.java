package com.gome.gmp.business;

import java.util.List;

import com.gome.gmp.ws.organization.pi.ZHRORGEHMD2;
import com.gome.gmp.ws.person.pi.ZHRPAMD2;

/**
 * WebService中HR接口数据导入
 *
 * @author wubin
 */
public interface GomeGmpWSHRBS {

	/**
	 * 导入人员数据
	 * 
	 * @param datas
	 * @return
	 */
	int importPersonData(List<ZHRPAMD2> datas);

	/**
	 * 导入部门数据
	 * 
	 * @param datas
	 * @return
	 */
	int importOrganizationData(List<ZHRORGEHMD2> datas);
}
