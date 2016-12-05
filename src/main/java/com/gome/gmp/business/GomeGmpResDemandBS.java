package com.gome.gmp.business;





import com.github.pagehelper.Page;
import com.gome.gmp.model.vo.GomeGmpResNeedVO;

public interface GomeGmpResDemandBS {
     //删除需求
	Integer deleteGomeGmpResNeedBOById(String needId);
	/**
	 * 撤销转交
	 * @param gomeGmpResNeedVO
	 * @return
	 * @author wubin
	 */
	Integer revokeGomeGmpResNeedBOById(String needId);
	 //修改需求
	int updateGomeGmpResNeedBO(GomeGmpResNeedVO gomeGmpResNeedVO);
	 //查询需求
	Page<GomeGmpResNeedVO> findGomeGmpResNeedBO(GomeGmpResNeedVO needVO);
	 //保存需求
	int saveGomeGmpResNeedBO(GomeGmpResNeedVO needVO);
	//转交操作
	int transfer(GomeGmpResNeedVO needVO);
	//拒绝操作
	int deny(GomeGmpResNeedVO needVO);
    //根据ID查询
	GomeGmpResNeedVO findGomeGmpResNeedBOById(GomeGmpResNeedVO vo);
	//打开操作
	int updateOpen(GomeGmpResNeedVO vo);
	
	/**
	 * 获取需受理项目
	 * 
	 * @return
	 * @author wubin
	 */
	int findAcceptCount();
}
