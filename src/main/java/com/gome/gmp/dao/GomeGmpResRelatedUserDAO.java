package com.gome.gmp.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gome.gmp.model.bo.GomeGmpResRelatedUserBO;
import com.gome.gmp.model.vo.GomeGmpResRelatedUserVO;

/**
 * 关联用户dao
 * 
 * @author Administrator
 */
@Repository("gomeGmpResRelatedUserDAO")
public interface GomeGmpResRelatedUserDAO {

	/**
	 * 保存关联用户
	 * 
	 * @param gomeGmpResRelatedUserBO
	 * @return
	 * @author wubin
	 */
	int saveGomeGmpResRelatedUserBO(GomeGmpResRelatedUserBO gomeGmpResRelatedUserBO);

	/**
	 * 根据id删除关联用户
	 * 
	 * @param id
	 * @return
	 * @author wubin
	 */
	int deleteGomeGmpResRelatedUserBOById(Long id);

	/**
	 * 根据项目id删除关联用户
	 * 
	 * @param proId
	 * @return
	 * @author wubin
	 */
	int deleteGomeGmpResRelatedUserBOByProId(String proId);

	/**
	 * 更新关联用户
	 * 
	 * @param gomeGmpResRelatedUserBO
	 * @return
	 * @author wubin
	 */
	int updateGomeGmpResRelatedUserBOById(GomeGmpResRelatedUserBO gomeGmpResRelatedUserBO);

	/**
	 * 查询关联用户
	 * 
	 * @param gomeGmpResRelatedUserVO
	 * @return
	 * @author wubin
	 */
	List<GomeGmpResRelatedUserVO> findGomeGmpResRelatedUserBO(GomeGmpResRelatedUserVO gomeGmpResRelatedUserVO);
}