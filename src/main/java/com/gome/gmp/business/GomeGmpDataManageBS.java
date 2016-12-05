package com.gome.gmp.business;


import java.util.List;

import com.github.pagehelper.Page;
import com.gome.gmp.model.bo.GomeGmpResDatasynBO;
import com.gome.gmp.model.vo.GomeGmpResDatasynVO;

public interface GomeGmpDataManageBS {
	
	List<GomeGmpResDatasynVO> findGomeGmpResDatasynBOByCondition(GomeGmpResDatasynVO gomeGmpResDatasynVO);
	
	Page<GomeGmpResDatasynVO> findGomeGmpResDatasynProjectBOByCondition(GomeGmpResDatasynVO gomeGmpResDatasynVO);
	
	Page<GomeGmpResDatasynVO> findGomeGmpResDatasynFileBOByCondition(GomeGmpResDatasynVO gomeGmpResDatasynVO);
	
	int deleteGomeGmpResDatasynBOById(Long id);
	
	int	saveGomeGmpResDatasyn(GomeGmpResDatasynBO gomeGmpResDatasynBO);
	
	GomeGmpResDatasynBO findNewDatasynBOByUserId(Long userId);
	
	GomeGmpResDatasynBO findGomeGmpResDatasynBOById(Long id);
}
