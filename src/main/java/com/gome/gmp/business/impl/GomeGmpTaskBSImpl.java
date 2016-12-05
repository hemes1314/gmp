package com.gome.gmp.business.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gome.gmp.business.GomeGmpTaskBS;
import com.gome.gmp.dao.GomeGmpResTaskDAO;
import com.gome.gmp.model.bo.GomeGmpResTaskBO;

/**
 * 项目任务服务实现层
 * 
 * @author wangchangtie
 *
 */
@Service("gomeGmpTaskBS")
public class GomeGmpTaskBSImpl implements GomeGmpTaskBS {
	
	@Resource
	private GomeGmpResTaskDAO gomeGmpResTaskDAO;

	@Override
	public List<GomeGmpResTaskBO> getProTasksByParms(String proId, Long userId) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("proId", proId);
		queryMap.put("userId", userId);
		List<GomeGmpResTaskBO> resultList = gomeGmpResTaskDAO.getProTasksByParms(queryMap);
		return resultList;
	}

	@Override
	public int updateGomeGmpTaskById(GomeGmpResTaskBO taskBo) {
		return gomeGmpResTaskDAO.updateGomeGmpTaskById(taskBo);
	}
	
}
