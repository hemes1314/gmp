package com.gome.gmp.web;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gome.framework.base.BaseRestController;
import com.gome.framework.util.ObjectFactoryUtil;
import com.gome.gmp.business.GomeGmpManualBS;
import com.gome.gmp.ws.person.PersonService;

/**
 * 手动执行
 *
 * @author wubin
 */
@RestController
@RequestMapping("/manual")
public class GomeGmpManualCTL extends BaseRestController {

	private static Logger logger = LoggerFactory.getLogger(GomeGmpManualCTL.class);
	
	@Resource
	private GomeGmpManualBS gomeGmpManualBS;
	
	/**
	 * 数据同步
	 * 
	 * @return
	 * @author wubin
	 */
	@RequestMapping(value = "/sync", method = RequestMethod.GET)
	public String sync() {
		PersonService personService = (PersonService) ObjectFactoryUtil.getObject("@personService");
		personService.takeDataForAgent();
		return "同步数据模块手动调用成功！";
	}
	
	/**
	 * 更新节假日
	 * 
	 * @return
	 * @author wubin
	 */
	@RequestMapping(value = "/updateHolidays", method = RequestMethod.GET)
	public String updateHolidays() {
		
		Map<String, Integer> retMap = gomeGmpManualBS.updateHolidays();
		String msg = "更新节假日完成，总数："+retMap.get("TOTAL")+",成功："+retMap.get("SUCCESS");
		logger.info(msg);
		return msg;
	}
}
