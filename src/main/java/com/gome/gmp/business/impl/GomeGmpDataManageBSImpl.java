package com.gome.gmp.business.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.gome.gmp.business.GomeGmpDataManageBS;
import com.gome.gmp.dao.GomeGmpResDatasynDAO;
import com.gome.gmp.model.bo.GomeGmpResDatasynBO;
import com.gome.gmp.model.vo.GomeGmpResDatasynVO;
@Service
public class GomeGmpDataManageBSImpl implements GomeGmpDataManageBS {

	@Resource
	private GomeGmpResDatasynDAO gomeGmpResDatasynDAO;

	@Override
	public Page<GomeGmpResDatasynVO> findGomeGmpResDatasynProjectBOByCondition(GomeGmpResDatasynVO gomeGmpResDatasynVO){
		return gomeGmpResDatasynDAO.findGomeGmpResDatasynProjectBOByCondition(gomeGmpResDatasynVO);
	}
	@Override
	public Page<GomeGmpResDatasynVO> findGomeGmpResDatasynFileBOByCondition(GomeGmpResDatasynVO gomeGmpResDatasynVO){
		return gomeGmpResDatasynDAO.findGomeGmpResDatasynFileBOByCondition(gomeGmpResDatasynVO);
	}
	
	@Override
	public List<GomeGmpResDatasynVO> findGomeGmpResDatasynBOByCondition(GomeGmpResDatasynVO gomeGmpResDatasynVO){
		return gomeGmpResDatasynDAO.findGomeGmpResDatasynBOByCondition(gomeGmpResDatasynVO);
	}
	
	@Override
	public int deleteGomeGmpResDatasynBOById(Long id){
		return gomeGmpResDatasynDAO.deleteGomeGmpResDatasynBOById(id);
	}
	
	@Override
	public int	saveGomeGmpResDatasyn(GomeGmpResDatasynBO gomeGmpResDatasynBO){
	
		return gomeGmpResDatasynDAO.saveGomeGmpResDatasynBO(gomeGmpResDatasynBO);

	}
	
	@Override
	public GomeGmpResDatasynBO findNewDatasynBOByUserId(Long userId){
		return gomeGmpResDatasynDAO.findNewDatasynBOByUserId(userId);
	}
	@Override
	public GomeGmpResDatasynBO findGomeGmpResDatasynBOById(Long id){
		return gomeGmpResDatasynDAO.findGomeGmpResDatasynBOById(id);
	}
}
