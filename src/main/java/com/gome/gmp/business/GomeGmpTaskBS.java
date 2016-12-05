package com.gome.gmp.business;

import java.util.List;

import com.gome.gmp.model.bo.GomeGmpResTaskBO;


/**
 * 项目任务服务层
 * @author wangchangtie
 *
 */
public interface GomeGmpTaskBS {
	
	/**
	 * 根据用户的任务项目获取任务列表
	 * @param proId
	 * @param userId
	 * @return
	 */
	public List<GomeGmpResTaskBO> getProTasksByParms(String proId, Long userId);
	
	/**
	 * 更新项目任务排期
	 * @param taskBo
	 * @return
	 */
	public int updateGomeGmpTaskById(GomeGmpResTaskBO taskBo);
	
}
