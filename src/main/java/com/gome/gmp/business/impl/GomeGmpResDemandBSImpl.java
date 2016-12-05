package com.gome.gmp.business.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.gome.framework.logging.Logger;
import com.gome.framework.logging.LoggerFactory;
import com.gome.framework.util.DateUtil;
import com.gome.gmp.business.GomeGmpResDemandBS;
import com.gome.gmp.common.BusinessUtil;
import com.gome.gmp.common.filter.CommandContext;
import com.gome.gmp.dao.GomeGmpResDatasynDAO;
import com.gome.gmp.dao.GomeGmpResNeedDAO;
import com.gome.gmp.dao.GomeGmpResRelatedUserDAO;
import com.gome.gmp.dao.GomeGmpResUserDAO;
import com.gome.gmp.model.bo.GomeGmpResDatasynBO;
import com.gome.gmp.model.vo.GomeGmpResDatasynVO;
import com.gome.gmp.model.vo.GomeGmpResNeedVO;


@Service
public class GomeGmpResDemandBSImpl implements GomeGmpResDemandBS {

	protected Logger LOG = LoggerFactory.getLogger(this.getClass());
	@Resource(name = "gomeGmpResNeedDAO")
	private GomeGmpResNeedDAO gomeGmpResNeedDAO;
	@Resource(name = "gomeGmpResDatasynDAO")
	private GomeGmpResDatasynDAO gomeGmpResDatasynDAO;
	@Resource(name = "gomeGmpResRelatedUserDAO")
	private GomeGmpResRelatedUserDAO gomeGmpResRelatedUserDAO;
	@Resource(name = "gomeGmpResUserDAO")
	private GomeGmpResUserDAO gomeGmpResUserDAO;

	/**
	 * 需求列表
	 **/

	@Override
	public Page<GomeGmpResNeedVO> findGomeGmpResNeedBO(GomeGmpResNeedVO needVO) {
		
		Page<GomeGmpResNeedVO> vo = gomeGmpResNeedDAO.findGomeGmpResNeedBO(needVO);
		return vo;
	}

	/**
	 * 需求创建
	 **/
	@Override
	public int saveGomeGmpResNeedBO(GomeGmpResNeedVO needVO) {
     
		String needId = UUID.randomUUID().toString();
		needVO.setNeedId(needId);
		// 获取当前用户保存
		Long userId = BusinessUtil.getLoginUserId(CommandContext.getRequest());
		needVO.setCreateUser(String.valueOf(userId));
		needVO.setCreateTime(DateUtil.currDate());
		// 资料保存
		List<GomeGmpResDatasynVO> uploadFiles = needVO.getUploadFileInfo();
		for (GomeGmpResDatasynVO uploadFile : uploadFiles) {
			uploadFile.setNeedId(needId);
			uploadFile.setUploadUserId(NumberUtils.toInt(needVO.getCreateUser()));
			uploadFile.setUploadTime(DateUtil.currDate());
			uploadFile.setProType(3);
			gomeGmpResDatasynDAO.saveGomeGmpResDatasynBO(uploadFile);
		}
		if (needVO.getOldUploadFileInfo() != null) {
			List<GomeGmpResDatasynVO> oldUploadFiles = needVO.getOldUploadFileInfo();
			List<Long> ids = new ArrayList<Long>();
			for (GomeGmpResDatasynVO gomeGmpResDatasynVO : oldUploadFiles) {
				if (gomeGmpResDatasynVO.getId() != null) {
					ids.add(gomeGmpResDatasynVO.getId());
				}
			}
			GomeGmpResDatasynVO gomeGmpResDatasynVO = new GomeGmpResDatasynVO();
			gomeGmpResDatasynVO.setDeleteIds(ids);
			gomeGmpResDatasynVO.setNeedId(needVO.getNeedId());
			gomeGmpResDatasynDAO.deleteGomeGmpResDatasynBOByIds(gomeGmpResDatasynVO);
		}else {
			List<Long> ids = new ArrayList<Long>();
			ids.add(-1L);
			GomeGmpResDatasynVO gomeGmpResDatasynVO = new GomeGmpResDatasynVO();
			gomeGmpResDatasynVO.setDeleteIds(ids);
			gomeGmpResDatasynVO.setNeedId(needVO.getNeedId());
			gomeGmpResDatasynDAO.deleteGomeGmpResDatasynBOByIds(gomeGmpResDatasynVO);
		}

		if (null == needVO.getStates()) {
			needVO.setStates(4);
		}
		if(String.valueOf(needVO.getPayUserId()).equals(needVO.getCreateUser())){
			return 0;
		}
		
		return gomeGmpResNeedDAO.saveGomeGmpResNeedBO(needVO);		
		
	}

	/**
	 * 修改
	 **/
	@Override
	public int updateGomeGmpResNeedBO(GomeGmpResNeedVO gomeGmpResNeedVO) {
		// 获取当前用户保存
		Long loginUserId = BusinessUtil.getLoginUserId(CommandContext.getRequest());
		gomeGmpResNeedVO.setLoginUserId(loginUserId);
		Integer userId = NumberUtils.toInt(String.valueOf(loginUserId));
		// 删除上传附件
		if (gomeGmpResNeedVO.getOldUploadFileInfo() != null) {
			List<GomeGmpResDatasynVO> oldUploadFiles = gomeGmpResNeedVO.getOldUploadFileInfo();
			List<Long> ids = new ArrayList<Long>();
			for (GomeGmpResDatasynVO gomeGmpResDatasynVO : oldUploadFiles) {
				if (gomeGmpResDatasynVO.getId() != null) {
					ids.add(gomeGmpResDatasynVO.getId());
				}
			}
			GomeGmpResDatasynVO gomeGmpResDatasynVO = new GomeGmpResDatasynVO();
			gomeGmpResDatasynVO.setDeleteIds(ids);
			gomeGmpResDatasynVO.setNeedId(gomeGmpResNeedVO.getNeedId());
			gomeGmpResDatasynDAO.deleteGomeGmpResDatasynBOByIds(gomeGmpResDatasynVO);
		}
		else {
			List<Long> ids = new ArrayList<Long>();
			ids.add(-1L);
			GomeGmpResDatasynVO gomeGmpResDatasynVO = new GomeGmpResDatasynVO();
			gomeGmpResDatasynVO.setDeleteIds(ids);
			gomeGmpResDatasynVO.setNeedId(gomeGmpResNeedVO.getNeedId());
			gomeGmpResDatasynDAO.deleteGomeGmpResDatasynBOByIdsByNeedId(gomeGmpResDatasynVO);
		}

		// 资料保存
		List<GomeGmpResDatasynVO> uploadFiles = gomeGmpResNeedVO.getUploadFileInfo();
		if (uploadFiles != null) {
			for (GomeGmpResDatasynBO uploadFile : uploadFiles) {
				uploadFile.setNeedId(gomeGmpResNeedVO.getNeedId());			
				uploadFile.setUploadUserId(userId);
				uploadFile.setUploadTime(DateUtil.currDate());
				uploadFile.setProType(3);
				gomeGmpResDatasynDAO.saveGomeGmpResDatasynBO(uploadFile);
			}
		}
		if(String.valueOf(gomeGmpResNeedVO.getPayUserId()).equals(gomeGmpResNeedVO.getCreateUser())) {
			return 0;
		}
		// 更新用户
		gomeGmpResNeedVO.setUpdateUser(userId);
		return gomeGmpResNeedDAO.updateGomeGmpResNeedBO(gomeGmpResNeedVO);
	}

	/**
	 * 删除
	 */
	@Override
	public Integer deleteGomeGmpResNeedBOById(String needId) {
		gomeGmpResDatasynDAO.deleteGomeGmpResDatasynBOByNeedId(needId);
		return gomeGmpResNeedDAO.deleteGomeGmpResNeedBOById(needId);
	}

	/**
	 * 撤销转交
	 * @param gomeGmpResNeedVO
	 * @return
	 * @author wubin
	 */
	@Override
	public Integer revokeGomeGmpResNeedBOById(String needId) {
		
		Long loginUserId = BusinessUtil.getLoginUserId(CommandContext.getRequest());
		Integer userId = NumberUtils.toInt(String.valueOf(loginUserId));
		//撤回后交付人恢复成当前用户；
		GomeGmpResNeedVO gomeGmpResNeedVO = new GomeGmpResNeedVO();
		gomeGmpResNeedVO.setNeedId(needId);
		gomeGmpResNeedVO.setStates(4);
		gomeGmpResNeedVO.setPayUserId(userId);
		gomeGmpResNeedVO.setUpdateUser(userId);
		return gomeGmpResNeedDAO.updateGomeGmpResNeedBO(gomeGmpResNeedVO);
	}
	
	/**
	 * 
	 * 需求详情
	 * 
	 */
	@Override
	public GomeGmpResNeedVO findGomeGmpResNeedBOById(GomeGmpResNeedVO gomeGmpResNeedVO) {
		//当前用户
		Long loginUserId = BusinessUtil.getLoginUserId(CommandContext.getRequest());
		gomeGmpResNeedVO.setLoginUserId(loginUserId);
		Page<GomeGmpResNeedVO> needbo = gomeGmpResNeedDAO.findGomeGmpResNeedBO(gomeGmpResNeedVO);
		if (needbo != null && needbo.size() > 0) {
			gomeGmpResNeedVO = needbo.get(0);
		}
		//关联附件
		GomeGmpResDatasynVO gomeGmpResDatasynVO = new GomeGmpResDatasynVO();
		gomeGmpResDatasynVO.setNeedId(gomeGmpResNeedVO.getNeedId());
		List<GomeGmpResDatasynVO> uploadFileInfo = gomeGmpResDatasynDAO.findGomeGmpResDatasynFileBOByCondition(gomeGmpResDatasynVO);
		gomeGmpResNeedVO.setUploadFileInfo(uploadFileInfo);
		return gomeGmpResNeedVO;
	}

	/**
	 * 转交操作
	 **/
	@Override
	public int transfer(GomeGmpResNeedVO needVO) {

		// 当前用户
        Long loginUserId = BusinessUtil.getLoginUserId(CommandContext.getRequest());
        Integer userId = NumberUtils.toInt(String.valueOf(loginUserId));
        needVO.setLoginUserId(loginUserId);
        // 转交前交付用户id
        needVO.setPrePayUserId(NumberUtils.toInt(String.valueOf(loginUserId)));
        // 已转交
        needVO.setStates(5);
        if(String.valueOf(needVO.getPayUserId()).equals(needVO.getCreateUser())) {
			return 0;
		}
        needVO.setUpdateUser(userId);
		return gomeGmpResNeedDAO.updateGomeGmpResNeedBO(needVO);
	}

	/**
	 * 拒绝操作
	 **/
	@Override
	public int deny(GomeGmpResNeedVO needVO) {
		// 当前用户
        Long loginUserId = BusinessUtil.getLoginUserId(CommandContext.getRequest());
        Integer userId = NumberUtils.toInt(String.valueOf(loginUserId));
		needVO.setStates(2);
		needVO.setUpdateUser(userId);
		return gomeGmpResNeedDAO.updateGomeGmpResNeedBO(needVO);
	}

	/**
	 * 打开操作
	 **/
	@Override
	public int updateOpen(GomeGmpResNeedVO needVO) {
		// 当前用户
        Long loginUserId = BusinessUtil.getLoginUserId(CommandContext.getRequest());
        Integer userId = NumberUtils.toInt(String.valueOf(loginUserId));
        needVO.setUpdateUser(userId);
		return gomeGmpResNeedDAO.updateGomeGmpResNeedBO(needVO);
	}
	
	/**
	 * 获取需受理项目数量
	 * 
	 * @return
	 * @author wubin
	 */
	public int findAcceptCount() {
		// 当前用户
        Long loginUserId = BusinessUtil.getLoginUserId(CommandContext.getRequest());
		return gomeGmpResNeedDAO.findAcceptCount(loginUserId);
	}
}
