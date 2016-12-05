package com.gome.gmp.web;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gome.framework.base.BaseRestController;
import com.gome.gmp.business.GomeGmpTaskBS;
import com.gome.gmp.common.BusinessUtil;
import com.gome.gmp.common.base.Message;
import com.gome.gmp.model.bo.GomeGmpResTaskBO;

/**
 * 项目任务控制层
 * @author wangchangtie
 *
 */
@RestController
@RequestMapping("/task")
public class GomeGmpTaskCTL extends BaseRestController {

	private static Logger logger = LoggerFactory.getLogger(GomeGmpTaskCTL.class);

	@Resource
	private GomeGmpTaskBS gomeGmpTaskBS;
	
	/**
	 * 
	 * @param viewOrg
	 * @return
	 */
	@RequestMapping(value = "/getProTasks", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Message getProTasks(@RequestParam("proId") String proId) {
		Message msg = null;
		if(StringUtils.isNotBlank(proId)){
			List<GomeGmpResTaskBO> proTaskList = gomeGmpTaskBS.getProTasksByParms(proId,BusinessUtil.getLoginUserId(getRequest()));
			if(proTaskList!=null && proTaskList.size()>0){
				msg = Message.success();
				msg.setData(proTaskList);
			}
		} else {
			msg = Message.failure("参数错误,获取项目任务失败");
		}
		return msg;
	}
	
	
	
}