package com.gome.gmp.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gome.gmp.model.bo.GomeGmpResTaskBO;
import com.gome.gmp.model.vo.GomeGmpResTaskVO;

/**
 * 排期dao
 * 
 * @author Administrator
 */
@Repository("gomeGmpResTaskDAO")
public interface GomeGmpResTaskDAO {

	/**
	 * 保存排期
	 * 
	 * @param gomeGmpResTaskVO
	 * @return
	 * @author wubin
	 */
	int saveGomeGmpResTaskBO(GomeGmpResTaskVO gomeGmpResTaskVO);

	/**
	 * 根据项目id删除排期
	 * 
	 * @param proId
	 * @return
	 * @author wubin
	 */
	int deleteGomeGmpResTaskBOByProId(String proId);

	/**
	 * 根据id更新项目任务排期
	 * 
	 * @param taskBo
	 * @return
	 * @author wubin
	 */
	int updateGomeGmpTaskById(GomeGmpResTaskBO taskBo);

	/**
	 * 查询排期
	 * 
	 * @param gomeGmpResTaskVO
	 * @return
	 * @author wubin
	 */
	List<GomeGmpResTaskVO> findGomeGmpResTaskBO(GomeGmpResTaskVO gomeGmpResTaskVO);
	
	/**
	 * 根据参数获取项目任务排期
	 * @param queryMap
	 * @return
	 */
	public List<GomeGmpResTaskBO> getProTasksByParms(Map<String, Object> queryMap);
}