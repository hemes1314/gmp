package com.gome.gmp.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.gome.gmp.model.bo.GomeGmpResDatasynBO;
import com.gome.gmp.model.vo.GomeGmpResDatasynVO;

/**
 * 资料dao
 * 
 * @author Administrator
 */
@Repository("gomeGmpResDatasynDAO")
public interface GomeGmpResDatasynDAO {

	/**
	 * 保存资料
	 * 
	 * @param gomeGmpResDatasynBO
	 * @return
	 * @author wubin
	 */
	int saveGomeGmpResDatasynBO(GomeGmpResDatasynBO gomeGmpResDatasynBO);

	/**
	 * 根据id删除资料
	 * 
	 * @param id
	 * @return
	 * @author wubin
	 */
	int deleteGomeGmpResDatasynBOById(Long id);

	/**
	 * 根据项目id保存资料
	 * 
	 * @param proId
	 * @return
	 * @author wubin
	 */
	int deleteGomeGmpResDatasynBOByProId(String proId);

	/**
	 * 删除资料
	 * 
	 * @param gomeGmpResDatasynVO
	 * @return
	 * @author wubin
	 */
	int deleteGomeGmpResDatasynBOByIds(GomeGmpResDatasynVO gomeGmpResDatasynVO);

	int deleteGomeGmpResDatasynBOByNeedId(String needId);

	int deleteGomeGmpResDatasynBOByIdsByNeedId(GomeGmpResDatasynVO gomeGmpResDatasynVO);

	/**
	 * 更新资料
	 * 
	 * @param gomeGmpResDatasynBO
	 * @return
	 * @author wubin
	 */
	int updateGomeGmpResDatasynBOById(GomeGmpResDatasynBO gomeGmpResDatasynBO);

	/**
	 * 查询单个资料
	 * 
	 * @param id
	 * @return
	 * @author wubin
	 */
	GomeGmpResDatasynBO findGomeGmpResDatasynBOById(Long id);

	Page<GomeGmpResDatasynVO> findGomeGmpResDatasynProjectBOByCondition(GomeGmpResDatasynVO gomeGmpResDatasynVO);

	Page<GomeGmpResDatasynVO> findGomeGmpResDatasynFileBOByCondition(GomeGmpResDatasynVO gomeGmpResDatasynVO);

	List<GomeGmpResDatasynVO> findGomeGmpResDatasynBOByCondition(GomeGmpResDatasynVO gomeGmpResDatasynVO);

	GomeGmpResDatasynBO findNewDatasynBOByUserId(Long userId);
}