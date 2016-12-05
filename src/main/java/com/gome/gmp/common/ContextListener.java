
package com.gome.gmp.common;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.gome.framework.util.ObjectFactoryUtil;
import com.gome.gmp.business.GomeGmpDictItemBS;
import com.gome.gmp.business.GomeGmpOrgManageBS;
import com.gome.gmp.ws.person.PersonService;

/**
 * 
 *
 * @author wubin
 */
// @WebListener
public class ContextListener implements ServletContextListener {

	/**
	 * 容器初始化监听
	 * 
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		GomeGmpDictItemBS gomeGmpDictItemBS = (GomeGmpDictItemBS) ObjectFactoryUtil.getObject("@gomeGmpDictItemBS");
		gomeGmpDictItemBS.initDictItemEhcache();
		GomeGmpOrgManageBS orgManageBS = (GomeGmpOrgManageBS) ObjectFactoryUtil.getObject("@gomeGmpOrgManageBS");
		orgManageBS.initOrgInfoEhcache();
		PersonService personService = (PersonService) ObjectFactoryUtil.getObject("@personService");
		personService.takeDataForAgent();
		// OrganizationService organizationService = (OrganizationService) ObjectFactoryUtil.getObject("@organizationService");
		// organizationService.takeDataForAgent();
	}

	/**
	 * 容器销毁监听
	 * 
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
}
