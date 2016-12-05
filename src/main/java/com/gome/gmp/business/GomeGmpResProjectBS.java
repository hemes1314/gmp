package com.gome.gmp.business;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.gome.gmp.model.bo.GomeGmpResProjectBO;
import com.gome.gmp.model.bo.GomeGmpResUserBO;
import com.gome.gmp.model.vo.GomeGmpResProjectVO;
import com.gome.gmp.model.vo.GomeGmpResTaskVO;

/**
 * 项目
 *
 * @author wubin
 */
public interface GomeGmpResProjectBS {

	/**
	 * 保存项目信息
	 * @param saveParms 
	 * 
	 * @param oldPro
	 * @param newPro
	 * @return
	 */
	public int saveGomeGmpResProject(GomeGmpResProjectVO gomeGmpResProjectVO, Map<String, Object> saveParms);

	/**
	 * 根据id删除项目信息
	 * 
	 * @see com.gome.gmp.business.GomeGmpResProjectBS#deleteGomeGmpResProjectBOById(java.lang.Long)
	 */
	public Integer deleteGomeGmpResProjectById(String proId);

	/**
	 * 更新项目信息
	 * @param saveParms 
	 * 
	 * @see com.gome.gmp.business.GomeGmpResProjectBS#updateGomeGmpResProjectBOById(com.gome.gmp.model.bo.GomeGmpResProjectBO)
	 */
	public int updateGomeGmpResProject(GomeGmpResProjectVO gomeGmpResProjectVO, Map<String, Object> saveParms);

	/**
	 * 发送报警邮件
	 * @param gomeGmpResProjectVO
	 * @author wubin
	 * @param saveParms 
	 */
	public void sendMails(GomeGmpResProjectVO gomeGmpResProjectVO, Map<String, Object> saveParms);
	
	/**
	 * 查询项目信息
	 * 
	 * @param gomeGmpResProjectVO
	 * @return
	 */
	public Page<GomeGmpResProjectVO> findGomeGmpResProject(GomeGmpResProjectVO gomeGmpResProjectVO);

	/**
	 * 根据id查询项目信息
	 * 
	 * @see com.gome.gmp.business.GomeGmpResProjectBS#findGomeGmpResProjectBOById(java.lang.Long)
	 */
	public GomeGmpResProjectVO findGomeGmpResProjectById(GomeGmpResProjectVO gomeGmpResProjectVO);

	/**
	 * 导出项目详情
	 * 
	 * @param proId
	 * @author wubin
	 */
	public void detailExport(String proId);

	/**
	 * 甘特图数据
	 * 
	 * @param proId
	 * @return
	 * @author wubin
	 */
	public List<GomeGmpResTaskVO> findGanttData(GomeGmpResTaskVO gomeGmpResTaskVO);

	/**
	 * 校验bugId
	 * 
	 * @param gomeGmpResProjectVO
	 * @return
	 * @author wubin
	 */
	public boolean validateBugId(GomeGmpResProjectVO gomeGmpResProjectVO);

	/**
	 * 关闭项目
	 * 
	 * @param proId
	 * @return
	 */
	public int closePro(String proId, Long userTid);
	
	/**
	 * 查询项目/需求列表
	 * @param projectVo
	 * @param loginUserId
	 * @return
	 */
	public Page<GomeGmpResProjectVO> queryGomeProjectPage(GomeGmpResProjectVO projectVo, Long loginUserId);
	
	/**
	 * /**
	 * 根据类型加载数据字典
	 * @param projectVo
	 * @param operateType
	 * @param userBo 
	 * @return
	 */
	public GomeGmpResProjectVO loadQueryConditionData(GomeGmpResProjectVO projectVo, String operateType, GomeGmpResUserBO userBo);
	
	/**
	 * 根据当前登录用户获取查询条件
	 * @param gomeGmpResProjectVO
	 * @param loginUser
	 * @return
	 */
	public GomeGmpResProjectVO userQueryCondition(GomeGmpResProjectVO gomeGmpResProjectVO, GomeGmpResUserBO loginUser);
	
	/**
	 * 处理查询的orgId
	 * @param projectVo
	 * @return
	 */
	public GomeGmpResProjectVO handleQueryOrgId(GomeGmpResProjectVO projectVo);
	
	/**
	 * 获取用户关联项目
	 * @param id
	 * @return
	 */
	public List<GomeGmpResProjectBO> getDailyRelateProjectsByUser(Long userId);

}
