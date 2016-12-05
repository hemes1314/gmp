package com.gome.gmp.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gome.gmp.model.bo.GomeGmpResProSysBO;
import com.gome.gmp.model.vo.GomeGmpResProSysVO;

/**
 * @author Administrator
 */
@Repository("gomeGmpResProSysDAO")
public interface GomeGmpResProSysDAO {

	int saveGomeGmpResProSysBO(GomeGmpResProSysVO gomeGmpResProSysVO);

	int deleteGomeGmpResProSysBOById(Long id);

	int deleteGomeGmpResProSysBOByProId(String proId);

	int updateGomeGmpResProSysBOById(GomeGmpResProSysBO gomeGmpResProSysBO);

	List<GomeGmpResProSysVO> findGomeGmpResProSysBO(GomeGmpResProSysVO gomeGmpResProSysVO);
}