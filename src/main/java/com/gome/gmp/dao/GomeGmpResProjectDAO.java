package com.gome.gmp.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.gome.gmp.model.bo.GomeGmpResProjectBO;
import com.gome.gmp.model.bo.GomeGmpResUserBO;
import com.gome.gmp.model.vo.GomeGmpResProjectVO;

/**
 * 项目dao
 * 
 * @author wubin
 */
@Repository("gomeGmpResProjectDAO")
public interface GomeGmpResProjectDAO {

	/**
	 * 保存项目
	 * 
	 * @param gomeGmpResProjectBO
	 * @return
	 * @author wubin
	 */
	int saveGomeGmpResProjectBO(GomeGmpResProjectBO gomeGmpResProjectBO);

	/**
	 * 删除项目
	 * 
	 * @param proId
	 * @return
	 * @author wubin
	 */
	int deleteGomeGmpResProjectBOById(String proId);

	/**
	 * 更新项目
	 * 
	 * @param gomeGmpResProjectBO
	 * @return
	 * @author wubin
	 */
	int updateGomeGmpResProjectBOById(GomeGmpResProjectBO gomeGmpResProjectBO);

	/**
	 * 更新单个项目属性
	 * 
	 * @param gomeGmpResProjectBO
	 * @return
	 * @author wubin
	 */
	int updateProjectPropertyById(GomeGmpResProjectBO gomeGmpResProjectBO);

	/**
	 * 查询项目
	 * 
	 * @param gomeGmpResProjectVO
	 * @return
	 * @author wubin
	 */
	Page<GomeGmpResProjectVO> findGomeGmpResProjectBO(GomeGmpResProjectVO gomeGmpResProjectVO);

	/**
	 * 验证bug id
	 * 
	 * @param gomeGmpResProjectVO
	 * @return
	 * @author wubin
	 */
	int validateBugId(GomeGmpResProjectVO gomeGmpResProjectVO);
	
	/**
	 * 获取查询的项目相关的项目经理
	 * 
	 * @param gomeGmpResProjectVO
	 * @return
	 * @author wubin
	 */
	List<GomeGmpResUserBO> getProRelatedBps(GomeGmpResProjectVO gomeGmpResProjectVO);
	
	/**
	 * 根据当前用户获取关联项目
	 * @param userId
	 * @return
	 */
	public List<GomeGmpResProjectBO> getDailyRelateProjectsByUser(Long userId);
}