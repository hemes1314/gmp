package com.gome.gmp.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.Page;
import com.gome.framework.base.BaseRestController;
import com.gome.gmp.business.GomeGmpDictItemBS;
import com.gome.gmp.business.GomeGmpOrgManageBS;
import com.gome.gmp.business.GomeGmpResDemandBS;
import com.gome.gmp.common.BusinessUtil;
import com.gome.gmp.common.constant.Constants;
import com.gome.gmp.common.filter.CommandContext;
import com.gome.gmp.common.mybaits.plugins.page.PageModel;
import com.gome.gmp.model.bo.GomeGmpResOrgBO;
import com.gome.gmp.model.bo.GomeGmpResUserBO;
import com.gome.gmp.model.vo.GomeGmpResDatasynVO;
import com.gome.gmp.model.vo.GomeGmpResNeedVO;
import com.gome.gmp.model.vo.GomeGmpResOrgVO;

@RestController
@RequestMapping("/demand")
public class GomeGmpResDemandCTL extends BaseRestController {

	private static Logger LOG = LoggerFactory.getLogger(GomeGmpResDemandCTL.class);
	
	@Resource
	private GomeGmpResDemandBS demandBS;

	@Resource
	private GomeGmpDictItemBS gomeGmpDictItemBS;

	@Resource
	private GomeGmpOrgManageBS gomeGmpOrgManageBS;
	
	/**
	 * 需求列表
	 * 
	 * @author tx_chenxin
	 **/
	@RequestMapping(value = "/list/{pageNum}", method = RequestMethod.POST)
	public PageModel GomegmpList(@RequestBody GomeGmpResNeedVO needBO, @PathVariable Integer pageNum) {
		BusinessUtil.setPageNum(pageNum);
		Page<GomeGmpResNeedVO> needBo = demandBS.findGomeGmpResNeedBO(needBO);
		PageModel model = new PageModel();
		model.setPageData(needBo);
		return model;
	}

	/**
	 * 列表视图
	 * 
	 * @author tx_chenxin
	 **/
	@RequestMapping(value = "/tolist")
	public ModelAndView tolist(@ModelAttribute GomeGmpResNeedVO gomeGmpResNeedVO) {
		
		loadProDefaultInfo(gomeGmpResNeedVO, CommandContext.getResponse());
		GomeGmpResUserBO loginUser = BusinessUtil.getLoginUser(CommandContext.getRequest());
		return new ModelAndView("/need/findneed").addObject("loginUser", loginUser);
	}

	/**
	 * 打开
	 **/
	@RequestMapping(value = "/open/{needId}", method = RequestMethod.POST)
	public String open(@PathVariable String needId) {
		GomeGmpResNeedVO vo = new GomeGmpResNeedVO();
		vo.setNeedId(needId);
		vo.setStates(4);
		int open = demandBS.updateOpen(vo);
		if (open > 0) {
			LOG.info("需求(id="+needId+")交付人(用户ID="+BusinessUtil.getLoginUserId(CommandContext.getRequest())+")重新打开。");
			return Constants.SUCCESS;
		}
		return Constants.FAILD;
	}

	/**
	 * 转交
	 **/
	@RequestMapping(value = "/transfer", method = RequestMethod.POST)
	public PageModel transfer(@ModelAttribute GomeGmpResNeedVO needBO) {
		int need = demandBS.transfer(needBO);
		boolean flag = false;
		if (need > 0) {
			flag = true;
			LOG.info("需求(id="+needBO.getNeedId()+")交付人(用户ID="+BusinessUtil.getLoginUserId(CommandContext.getRequest())+")转交->用户(ID="+needBO.getPayUserId()+")成功。");
		}
		PageModel model = new PageModel();
		model.setResultData(flag);
		model.setTargetUrl("/need/findneed");
		return model;
	}

	/**
	 * 拒绝
	 **/
	@RequestMapping(value = "/deny", method = RequestMethod.POST)
	public PageModel deny(@ModelAttribute GomeGmpResNeedVO needBO) {
		int need = demandBS.deny(needBO);
		boolean flag = false;
		if (need > 0) {
			flag = true;
			LOG.info("需求(id="+needBO.getNeedId()+")交付人(用户ID="+BusinessUtil.getLoginUserId(CommandContext.getRequest())+")已拒绝。");
		}
		PageModel model = new PageModel();
		model.setResultData(flag);
		model.setTargetUrl("/need/findneed");
		return model;
	}

	/**
	 * 需求创建
	 * 
	 * @author tx_chenxin
	 **/
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public PageModel save(@ModelAttribute GomeGmpResNeedVO needBO, @RequestParam(value = "file", required = false) CommonsMultipartFile[] files) {
		// 上传文件
		List<GomeGmpResDatasynVO> uploadFileInfo = BusinessUtil.uploadFiles("3", files, getRequest());
		// 保存基础信息
		needBO.setUploadFileInfo(uploadFileInfo);
		int need = demandBS.saveGomeGmpResNeedBO(needBO);
		boolean flag = false;
		if (need > 0) {
			flag = true;
		}
		PageModel model = new PageModel();
		model.setResultData(flag);
		model.setTargetUrl("/need/findneed");
		return model;
	}

	/**
	 * 需求创建视图
	 * 
	 * @author tx_chenxin
	 **/
	@RequestMapping(value = "/toSave", method = RequestMethod.GET)
	public ModelAndView toSave(@ModelAttribute GomeGmpResNeedVO needVO, HttpServletResponse response) {
		loadProDefaultInfo(needVO, response);
		GomeGmpResUserBO loginUser = BusinessUtil.getLoginUser(getRequest());
		needVO.setKeyUserId(NumberUtils.toInt(String.valueOf(loginUser.getId())));
		needVO.setKeyUser(loginUser.getUserId() + "(" + loginUser.getUserName() + ")");
		
		// 第一级别org
		List<GomeGmpResOrgVO> orgLevelFirst = gomeGmpOrgManageBS.getOrgListByLevel(Constants.ORGLEVEL_FIRST);
		needVO.setResUnitV(orgLevelFirst);
		return new ModelAndView("/need/saveDemand");
	}

	/**
	 * 上传
	 * 
	 */
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public Integer uploadFile(@RequestParam("file") CommonsMultipartFile[] files) {
		// 上传文件
		List<GomeGmpResDatasynVO> uploadFileInfo = BusinessUtil.uploadFiles("3", files, getRequest());
		return uploadFileInfo.size();
	}

	/**
	 * 新建编辑时加载默认信息
	 * 
	 * @param needVO
	 */
	private void loadProDefaultInfo(GomeGmpResNeedVO needVO, HttpServletResponse response) {
		GomeGmpResUserBO loginUser = BusinessUtil.getLoginUser(getRequest());
		// 获取当前用户
		needVO.setCreateUser(String.valueOf(loginUser.getId()));
		needVO.setCreateUserName(loginUser.getUserName());
		// 字典表的数据
		needVO.setStatusV(gomeGmpDictItemBS.getDictItem("status"));
		needVO.setPriorityIdsV(gomeGmpDictItemBS.getDictItem("priorityId"));
	}

	/**
	 * 详情视图
	 */
	@RequestMapping(value = "/toDetail/{needId}", method = RequestMethod.GET)
	public ModelAndView toDetail(@PathVariable String needId, @ModelAttribute GomeGmpResNeedVO needVO, HttpServletResponse response) {
		needVO = demandBS.findGomeGmpResNeedBOById(needVO);
		loadProDefaultInfo(needVO, response);
		
		// 将所有级别的orgId查询
		GomeGmpResOrgBO orgBo = gomeGmpOrgManageBS.findOrgBOByOrgId(needVO.getUnitBsId());
		if(needVO.getUnitBsId() != null && orgBo != null) {
			needVO.setUnitBsName(gomeGmpOrgManageBS.getAllLevelOrgName(orgBo, orgBo.getOrgName()));
		}
		if(needVO.getPayUnitId() != null && !needVO.getPayUnitId().equals(needVO.getUnitBsId())) {
			orgBo = gomeGmpOrgManageBS.findOrgBOByOrgId(needVO.getPayUnitId());
		}
		if(orgBo != null) {
			needVO.setPayUnitName(gomeGmpOrgManageBS.getAllLevelOrgName(orgBo, orgBo.getOrgName()));
		}
		return new ModelAndView("/need/demandDetail").addObject(needVO);
	}

	/**
	 * 编辑视图
	 * 
	 * @param gomeGmpResNeedVO
	 * @return
	 */
	@RequestMapping(value = "/toUpdate/{needId}", method = RequestMethod.GET)
	public ModelAndView toUpdate(@ModelAttribute GomeGmpResNeedVO gomeGmpResNeedVO, HttpServletResponse response) {
		gomeGmpResNeedVO = demandBS.findGomeGmpResNeedBOById(gomeGmpResNeedVO);
		loadProDefaultInfo(gomeGmpResNeedVO, response);
		
		// 将所有级别的orgId查询
		GomeGmpResOrgBO orgBo = gomeGmpOrgManageBS.findOrgBOByOrgId(gomeGmpResNeedVO.getUnitBsId());
		if(gomeGmpResNeedVO.getUnitBsId() != null && orgBo != null) {
			gomeGmpResNeedVO.setUnitBsId(gomeGmpOrgManageBS.getAllLevelOrgId(orgBo, orgBo.getOrgId()));
		}
		if(gomeGmpResNeedVO.getPayUnitId() != null && !gomeGmpResNeedVO.getUnitBsId().equals(gomeGmpResNeedVO.getPayUnitId())) {
			orgBo = gomeGmpOrgManageBS.findOrgBOByOrgId(gomeGmpResNeedVO.getPayUnitId());
		}
		if(orgBo != null) {
			gomeGmpResNeedVO.setPayUnitId(gomeGmpOrgManageBS.getAllLevelOrgId(orgBo, orgBo.getOrgId()));
		}
		
		// 第一级别org
		List<GomeGmpResOrgVO> orgLevelFirst = gomeGmpOrgManageBS.getOrgListByLevel(Constants.ORGLEVEL_FIRST);
		gomeGmpResNeedVO.setResUnitV(orgLevelFirst);
		return new ModelAndView("/need/updateDemand").addObject(gomeGmpResNeedVO);
	}

	/**
	 * 需求编辑
	 * 
	 * @author tx_chenxin
	 * 
	 **/
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public PageModel update(@ModelAttribute GomeGmpResNeedVO gomeGmpResNeedBO, @RequestParam(value = "file", required = false) CommonsMultipartFile[] files) {
		// 上传文件
		List<GomeGmpResDatasynVO> uploadFileInfo = BusinessUtil.uploadFiles("3", files, getRequest());
		// 保存基础信息
		gomeGmpResNeedBO.setUploadFileInfo(uploadFileInfo);
		if (null == gomeGmpResNeedBO.getStates()) {
			gomeGmpResNeedBO.setStates(4);
		}
		int need = demandBS.updateGomeGmpResNeedBO(gomeGmpResNeedBO);
		boolean flag = false;
		if (need > 0) {
			flag = true;
		}
		PageModel model = new PageModel();
		model.setResultData(flag);
		model.setTargetUrl("/need/findneed");
		return model;
	}

	/**
	 * 需求编辑视图
	 * 
	 * @author tx_chenxin
	 * 
	 **/
	@RequestMapping(value = "/toupload", method = RequestMethod.POST)
	public ModelAndView upload(@ModelAttribute GomeGmpResNeedVO need) {
		return new ModelAndView("/need/updateNeed");
	}

	/**
	 * 删除
	 * 
	 * @author tx_chenxin
	 */
	@RequestMapping(value = "/delete/{needId}", method = { RequestMethod.POST })
	public String delete(@PathVariable String needId) {
		Integer result = demandBS.deleteGomeGmpResNeedBOById(needId);
		if (result > 0) {
			return Constants.SUCCESS;
		}
		return Constants.FAILD;
	}
	
	/**
	 * 撤销
	 * 
	 * @author wubin
	 */
	@RequestMapping(value = "/revoke/{needId}", method = { RequestMethod.POST })
	public String revoke(@PathVariable String needId) {
		
		Integer result = demandBS.revokeGomeGmpResNeedBOById(needId);
		if (result > 0) {
			LOG.info("需求(id="+needId+")原交付人(用户ID="+BusinessUtil.getLoginUserId(CommandContext.getRequest())+")已撤销转交。");
			return Constants.SUCCESS;
		}
		return Constants.FAILD;
	}
	
	/**
	 * 查询是否有受理项目
	 */
	@RequestMapping(value = "/findAcceptCount", method = { RequestMethod.GET })
	public Integer findAcceptCount() {
		
		Integer result = demandBS.findAcceptCount();
		return result;
	}
}
