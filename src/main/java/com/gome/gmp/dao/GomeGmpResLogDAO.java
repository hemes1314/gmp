package com.gome.gmp.dao;

import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.gome.gmp.model.bo.GomeGmpResLogBO;
import com.gome.gmp.model.vo.GomeGmpResLogVO;

/**
 * 变更记录dao
 * 
 * @author Administrator
 */
@Repository("gomeGmpResLogDAO")
public interface GomeGmpResLogDAO {

	/**
	 * 保存变更记录
	 * 
	 * @param gomeGmpResLogBO
	 * @return
	 * @author wubin
	 */
	int saveGomeGmpResLogBO(GomeGmpResLogBO gomeGmpResLogBO);

	/**
	 * 删除变更记录
	 * 
	 * @param id
	 * @return
	 * @author wubin
	 */
	int deleteGomeGmpResLogBOById(Long id);

	/**
	 * 查询变更记录
	 * 
	 * @param gomeGmpResLogVO
	 * @return
	 * @author wubin
	 */
	Page<GomeGmpResLogVO> findGomeGmpResLogBO(GomeGmpResLogVO gomeGmpResLogVO);
}