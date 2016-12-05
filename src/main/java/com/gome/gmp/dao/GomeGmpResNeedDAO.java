package com.gome.gmp.dao;




import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.gome.gmp.model.vo.GomeGmpResNeedVO;

/**
 * @author Administrator
 */
@Repository("gomeGmpResNeedDAO")
public interface GomeGmpResNeedDAO {
    //保存
	int saveGomeGmpResNeedBO(GomeGmpResNeedVO gomeGmpResNeedVO);
	 //删除
	int deleteGomeGmpResNeedBOById(String needId);
	 //修改
	int updateGomeGmpResNeedBO(GomeGmpResNeedVO gomeGmpResNeedVO);
	 //根据ID查询
	GomeGmpResNeedVO findGomeGmpResNeedBOById(String id);
	 //查询需求列表
	Page<GomeGmpResNeedVO> findGomeGmpResNeedBO(GomeGmpResNeedVO vo);

	/**
	 * 获取需受理项目数量
	 * 
	 * @return
	 * @author wubin
	 */
	int findAcceptCount(Long loginUserId);
}