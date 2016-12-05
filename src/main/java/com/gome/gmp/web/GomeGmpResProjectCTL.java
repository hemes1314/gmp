package com.gome.gmp.web;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.Page;
import com.gome.framework.base.BaseRestController;
import com.gome.gmp.business.GomeGmpDictItemBS;
import com.gome.gmp.business.GomeGmpOrgManageBS;
import com.gome.gmp.business.GomeGmpResDemandBS;
import com.gome.gmp.business.GomeGmpResProjectBS;
import com.gome.gmp.business.GomeGmpResRoleBS;
import com.gome.gmp.common.BusinessUtil;
import com.gome.gmp.common.FileExportUtil;
import com.gome.gmp.common.base.Message;
import com.gome.gmp.common.constant.Constants;
import com.gome.gmp.common.filter.CommandContext;
import com.gome.gmp.common.mybaits.plugins.page.PageModel;
import com.gome.gmp.common.util.DateUtil;
import com.gome.gmp.model.bo.GomeGmpResOrgBO;
import com.gome.gmp.model.bo.GomeGmpResUserBO;
import com.gome.gmp.model.vo.GomeGmpResDatasynVO;
import com.gome.gmp.model.vo.GomeGmpResNeedVO;
import com.gome.gmp.model.vo.GomeGmpResProjectVO;
import com.gome.gmp.model.vo.GomeGmpResRelatedUserVO;
import com.gome.gmp.model.vo.GomeGmpResRoleVO;
import com.gome.gmp.model.vo.GomeGmpResTaskVO;

/**
 * 项目/敏捷需求
 * 
 * @author wubin
 */
@RestController
@RequestMapping("/project")
public class GomeGmpResProjectCTL extends BaseRestController {

	private static Logger logger = LoggerFactory.getLogger(GomeGmpResProjectCTL.class);

	@Resource
	private GomeGmpResProjectBS gomeGmpResProjectBS;

	@Resource
	private GomeGmpResDemandBS gomeGmpResDemandBS;

	@Resource
	private GomeGmpDictItemBS gomeGmpDictItemBS;

	@Resource
	private GomeGmpResRoleBS gomeGmpRoleBS;

	@Resource
	private GomeGmpOrgManageBS gomeGmpOrgManageBS;

	
	/**
	 * 跳转至项目或需求页面
	 * @param model
	 * @param proType 1项目,2需求
	 * @return
	 */
	@RequestMapping(value = "/toProjectView", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView toProjectView(Model model,GomeGmpResProjectVO projectVo,Page<GomeGmpResProjectVO> proVoPage) {
		if(projectVo.getProType()!=null){
			GomeGmpResUserBO loginUser = BusinessUtil.getLoginUser(getRequest());
			Map<String,Object> parmsMap = new HashMap<String, Object>();
			parmsMap.put("orgIds", StringUtils.join(projectVo.getOrgIds(),","));
			parmsMap.put("childOrgIds", StringUtils.join(projectVo.getChildOrgIds(),","));
			parmsMap.put("groupIds", StringUtils.join(projectVo.getGroupIds(),","));
			projectVo = gomeGmpResProjectBS.loadQueryConditionData(projectVo, "query", loginUser);
			BusinessUtil.setPageNum(proVoPage.getPageNum()!=0?proVoPage.getPageNum():1);// 分页
			proVoPage = gomeGmpResProjectBS.queryGomeProjectPage(projectVo,BusinessUtil.getLoginUserId(getRequest()));
			PageModel pageModel = new PageModel();
			pageModel.setPageData(proVoPage);
			parmsMap.put("proType", projectVo.getProType());
			parmsMap.put("isMember", projectVo.getIsMember());
			parmsMap.put("startTime", projectVo.getStartTime()!=null?DateUtil.getDate(projectVo.getStartTime()):null);
			parmsMap.put("endTime", projectVo.getEndTime()!=null?DateUtil.getDate(projectVo.getEndTime()):null);
			parmsMap.put("title", projectVo.getTitle());
			parmsMap.put("unitBsId", projectVo.getUnitBsId());
			parmsMap.put("taskStatus",StringUtils.join( projectVo.getTaskStatus(),","));
			parmsMap.put("priorityIds", StringUtils.join( projectVo.getPriorityIds(),","));
			parmsMap.put("scheduleIds", StringUtils.join( projectVo.getScheduleIds(),","));
			parmsMap.put("actualizes", StringUtils.join( projectVo.getActualizes(),","));
			parmsMap.put("bpIds", StringUtils.join( projectVo.getBpIds(),","));
			// 将所有级别的orgId, 用于查询及修改界面的业务部门数据回填(格式：centerOrgId-orgId-childOrgId)
			if(StringUtils.isNotBlank(projectVo.getUnitBsId())) {
				GomeGmpResOrgBO orgBo = gomeGmpOrgManageBS.findOrgBOByOrgId(projectVo.getUnitBsId());
				if(projectVo.getUnitBsId() != null && orgBo != null) {
					parmsMap.put("unitBsIdAll", gomeGmpOrgManageBS.getAllLevelOrgId(orgBo, orgBo.getOrgId()));
				}
			}
			pageModel.setParams(parmsMap);
			model.addAttribute("gomeGmpResProjectVO", projectVo);
			model.addAttribute(Constants.PROJECT_VIEW_ATTRSTR,projectVo.getProType());
			model.addAttribute("userPermission",loginUser.getAuthority());
			model.addAttribute("pageModel", pageModel);
		}
		return new ModelAndView("/project/findProject");
	}

	/**
	 * 列表视图(只读)
	 * 
	 * @param pro
	 * @return
	 */
	@RequestMapping(value = "/readOnly/{scheduleId}", method = RequestMethod.GET)
	public ModelAndView toFindReadOnly(@PathVariable Integer scheduleId) {
		// 将进度id记录session下
		HttpSession session = getRequest().getSession();
		logger.debug("获取session数据，request="+getRequest()+",session="+session);
		GomeGmpResProjectVO gomeGmpResProjectVO = (GomeGmpResProjectVO) session.getAttribute(Constants.TJ_STATUS_FIND_PARAM);
		logger.debug("session-getGomeGmpResProjectVO="+gomeGmpResProjectVO);
		gomeGmpResProjectVO.setScheduleId(scheduleId);
		session.setAttribute(Constants.TJ_STATUS_FIND_PARAM, gomeGmpResProjectVO);
		return new ModelAndView("/project/findReadOnly");
	}

	/**
	 * 列表数据(只读)
	 * 
	 * @param pro
	 * @return
	 */
	@RequestMapping(value = "/find/readOnly/{pageNum}", method = RequestMethod.POST)
	public PageModel findReadOnly(@PathVariable Integer pageNum) {
		BusinessUtil.setPageNum(pageNum);
		GomeGmpResProjectVO gomeGmpResProjectVO = (GomeGmpResProjectVO) getRequest().getSession().getAttribute(Constants.TJ_STATUS_FIND_PARAM);
		logger.debug("session-getGomeGmpResProjectVO="+gomeGmpResProjectVO);
		Page<GomeGmpResProjectVO> pageData = gomeGmpResProjectBS.findGomeGmpResProject(gomeGmpResProjectVO);
		PageModel model = new PageModel();
		model.setPageData(pageData);
		return model;
	}

	/**
	 * 列表数据(只读)
	 * 
	 * @param pro
	 * @return
	 */
	@RequestMapping(value = "/exportFile", method = RequestMethod.GET)
	public void findExport(Integer pageNum) {
		// 设置分页
		BusinessUtil.setPageNum(pageNum==0 || pageNum==null?1:pageNum);
		// 查询参数
		GomeGmpResProjectVO gomeGmpResProjectVO = (GomeGmpResProjectVO) getRequest().getSession().getAttribute("PROJECT_FIND_PARAM");
		// 查询
		Page<GomeGmpResProjectVO> pageData = gomeGmpResProjectBS.findGomeGmpResProject(gomeGmpResProjectVO);
		// 导出文件
		String title = "项目列表导出";
		// 文件名
		String fileName = "项目列表导出文件";
		// 标题
		String colnums[] = new String[] { "项目ID", "项目名称", "任务状态", "优先级", "状态进度", "实施阶段", "完成度", "计划上线日期", "项目经理" };
		// 组织数据
		List<String[]> dataList = new ArrayList<String[]>();
		for (int i = 0; i < pageData.size(); i++) {
			GomeGmpResProjectVO proVO = pageData.get(i);
			String data[] = new String[colnums.length];
			data[0] = StringUtils.defaultString(proVO.getProId());
			data[1] = StringUtils.defaultString(proVO.getTitle());
			data[2] = StringUtils.defaultString(proVO.getStatusName());
			data[3] = StringUtils.defaultString(proVO.getPriorityName());
			data[4] = StringUtils.defaultString(proVO.getScheduleName());
			data[5] = StringUtils.defaultString(proVO.getActualizeName());
			data[6] = StringUtils.defaultString(proVO.getPercentage());
			data[7] = StringUtils.defaultString(proVO.getPlanTime() == null ? "" : DateUtil.getDate(proVO.getPlanTime()));
			data[8] = StringUtils.defaultString(proVO.getBpName());
			dataList.add(data);
		}
		// 导出
		String result = FileExportUtil.exportDataToExcelXLSX(title, colnums, dataList, CommandContext.getResponse(), fileName, null);
		if (!"success".equals(result)) {
			getRequest().setAttribute("validatorMsg", "导出错误数据操作异常,请稍后再试！");
		}
	}

	/**
	 * 详情导出
	 * 
	 * @param pro
	 * @return
	 */
	@RequestMapping(value = "/detail/export/{proId}", method = RequestMethod.GET)
	public void detailExport(@PathVariable String proId) {
		gomeGmpResProjectBS.detailExport(proId);
	}

	/**
	 * 新建视图
	 * 
	 * @return
	 */
	@RequestMapping(value = "/toSaveProjectView", method = RequestMethod.GET)
	public ModelAndView toSaveProjectView(Model model, GomeGmpResProjectVO projectVo) {
		GomeGmpResUserBO loginUser = BusinessUtil.getLoginUser(getRequest());
		projectVo = gomeGmpResProjectBS.loadQueryConditionData(projectVo, "add", loginUser);
		// 加载默认角色
		List<GomeGmpResRoleVO> gomeGmpResRoleVOs = gomeGmpRoleBS.defaultRoleInfo();
		List<GomeGmpResRelatedUserVO> relatedUsers = new ArrayList<GomeGmpResRelatedUserVO>();
		for (GomeGmpResRoleVO gomeGmpResRoleVO : gomeGmpResRoleVOs) {
			GomeGmpResRelatedUserVO gomeGmpResRelatedUserVO = new GomeGmpResRelatedUserVO();
			gomeGmpResRelatedUserVO.setRoleId(NumberUtils.toInt(String.valueOf(gomeGmpResRoleVO.getId())));
			relatedUsers.add(gomeGmpResRelatedUserVO);
		}
		projectVo.setRelatedUsers(relatedUsers);
		model.addAttribute(projectVo);
		model.addAttribute(Constants.PROJECT_VIEW_ATTRSTR,projectVo.getProType());
		return new ModelAndView("/project/saveProject");
	}

	/**
	 * 需求转项目
	 * 
	 * @return
	 */
	@RequestMapping(value = "/needToSave", method = RequestMethod.GET)
	public ModelAndView needToSave(Model model,@ModelAttribute GomeGmpResProjectVO gomeGmpResProjectVO) {
		if (gomeGmpResProjectVO.getDemandId() == null || StringUtils.isBlank(gomeGmpResProjectVO.getDemandName()) || gomeGmpResProjectVO.getProType() == null) {
			LOG.error("需求转项目参数不全，格式->/project/needToSave?proType=XX&demandId=XX");
		}
		// 设置需求名称，关键用户
		GomeGmpResNeedVO vo = new GomeGmpResNeedVO();
		vo.setNeedId(String.valueOf(gomeGmpResProjectVO.getDemandId()));
		vo = gomeGmpResDemandBS.findGomeGmpResNeedBOById(vo);
		gomeGmpResProjectVO.setDemandName(vo.getTitle());
		gomeGmpResProjectVO.setUnitBsId(vo.getUnitBsId());
		// 将所有级别的orgId, 用于查询及修改界面的业务部门数据回填(格式：centerOrgId-orgId-childOrgId)
		if(StringUtils.isNotBlank(vo.getUnitBsId())) {
			GomeGmpResOrgBO orgBo = gomeGmpOrgManageBS.findOrgBOByOrgId(gomeGmpResProjectVO.getUnitBsId());
			if(gomeGmpResProjectVO.getUnitBsId() != null && orgBo != null) {
				gomeGmpResProjectVO.setUnitBsIdAll(gomeGmpOrgManageBS.getAllLevelOrgId(orgBo, orgBo.getOrgId()));
			}
		}
		gomeGmpResProjectVO.setUnitBsName(vo.getUnitBsName());
		gomeGmpResProjectVO.setKeyUserId(vo.getKeyUserId());
		gomeGmpResProjectVO.setKeyUserName(vo.getKeyUser());
		loadProDefaultInfo(gomeGmpResProjectVO, "add");
		model.addAttribute(Constants.PROJECT_VIEW_ATTRSTR,gomeGmpResProjectVO.getProType());
		return new ModelAndView("/project/saveProject");
	}

	/**
	 * 新建保存
	 * 
	 * @param pro
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public PageModel save(@ModelAttribute GomeGmpResProjectVO gomeGmpResProjectVO, @RequestParam(value = "file", required = false) CommonsMultipartFile[] files) {
		// 上传文件
		List<GomeGmpResDatasynVO> uploadFileInfo = BusinessUtil.uploadFiles(gomeGmpResProjectVO.getProType() + "", files, getRequest());
		// 保存基础信息
		gomeGmpResProjectVO.setUploadFileInfo(uploadFileInfo);
		Map<String, Object> saveParms = new HashMap<String, Object>();
		int saveResult = gomeGmpResProjectBS.saveGomeGmpResProject(gomeGmpResProjectVO,saveParms);
		String flag = Constants.FAILD;
		if (saveResult > 0) {
			flag = Constants.SUCCESS;
			// 已提交项目，项目“进度状态”变更为“延期风险”、“已延期”时，触发报警邮件。
			if(gomeGmpResProjectVO.getIsCommit() == 1 && (gomeGmpResProjectVO.getScheduleId() == 3 || gomeGmpResProjectVO.getScheduleId() == 4) ) {
				gomeGmpResProjectBS.sendMails(gomeGmpResProjectVO,saveParms);
			}
		}
		Map<String, Object> resultData = new HashMap<String, Object>();
		resultData.put("flag", flag);
		PageModel model = new PageModel();
		model.setResultData(resultData);
		String targetUrl = "/project/toProjectView?listType=init&proType="+gomeGmpResProjectVO.getProType();
		model.setTargetUrl(targetUrl);
		return model;
	}

	/**
	 * 上传
	 * 
	 * @param pro
	 * @return
	 */
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public Integer uploadFile(@RequestParam("file") CommonsMultipartFile[] files) {
		// 上传文件
		List<GomeGmpResDatasynVO> uploadFileInfo = BusinessUtil.uploadFiles(files, getRequest());
		for (GomeGmpResDatasynVO dataSyn : uploadFileInfo) {
			importData(dataSyn.getPhysicalPath());
		}
		return uploadFileInfo.size();
	}

	/**
	 * 导入文件内容
	 * 
	 * @param filePath
	 * @author wubin
	 */
	private void importData(String filePath) {
		// office2007工作区
		XSSFWorkbook wb = null;
		try {
			wb = new XSSFWorkbook(new FileInputStream(filePath));
		} catch (IOException e) {
			LOG.error("文件读取失败", e);
		}
		// 获得该工作区的第一个sheet
		XSSFSheet sheet = wb.getSheetAt(0);
		// 总共有多少行,从0开始
		int totalRows = sheet.getLastRowNum();
		for (int i = 0; i <= totalRows; i++) {
			// 取得该行
			XSSFRow row = sheet.getRow(i);
			// 注释的代码，是为了防止excel文件有空行
			if (row == null) {
				continue;
			}
			for (int j = 0; j <= row.getLastCellNum(); j++) {
				System.out.println(row.getCell(j).toString());
			}
		}
	}

	/**
	 * 项目详情
	 * 
	 * @param proId
	 * @param gomeGmpResProjectVO
	 * @return
	 * @author wubin
	 */
	@RequestMapping(value = "/detail/{proId}", method = RequestMethod.GET)
	public ModelAndView detail(Model model,@PathVariable String proId, @ModelAttribute GomeGmpResProjectVO gomeGmpResProjectVO) {
		gomeGmpResProjectVO = gomeGmpResProjectBS.findGomeGmpResProjectById(gomeGmpResProjectVO);
		if (gomeGmpResProjectVO == null) {
			gomeGmpResProjectVO = new GomeGmpResProjectVO();
			gomeGmpResProjectVO.setReturnData(Constants.FAILD);
			LOG.error("查询详情失败，请检查是否正确调用接口！");
		}
		// 补充默认角色信息
		loadProDefaultRoles(gomeGmpResProjectVO);
		model.addAttribute(Constants.PROJECT_VIEW_ATTRSTR,gomeGmpResProjectVO.getProType());
		return new ModelAndView("/project/detailProject").addObject(gomeGmpResProjectVO);
	}

	/**
	 * 判断bugId唯一性
	 * 
	 * @param pro
	 * @return
	 */
	@RequestMapping(value = "/val/bugId", method = RequestMethod.POST)
	public String validateBugId(@RequestBody GomeGmpResProjectVO gomeGmpResProjectVO) {
		boolean isExists = gomeGmpResProjectBS.validateBugId(gomeGmpResProjectVO);
		if (isExists) {
			return Constants.SUCCESS;
		} else {
			return Constants.FAILD;
		}
	}

	/**
	 * 跳转到项目修改页面
	 * @param model
	 * @param gomeGmpResProjectVO
	 * @return
	 */
	@RequestMapping(value = "/toUpdate/{proId}", method = RequestMethod.GET)
	public ModelAndView toUpdate(Model model,@ModelAttribute GomeGmpResProjectVO gomeGmpResProjectVO) {
		gomeGmpResProjectVO = gomeGmpResProjectBS.findGomeGmpResProjectById(gomeGmpResProjectVO);
		// 补充默认角色信息
		loadProDefaultRoles(gomeGmpResProjectVO);
		// 如果到达开始时间则加标志
		if (gomeGmpResProjectVO.getStartTime() != null && gomeGmpResProjectVO.getStartTime().compareTo(new Date()) < 0 && gomeGmpResProjectVO.getIsCommit() == 1) {
			gomeGmpResProjectVO.setAfterStartTime(true);
		}
		loadProDefaultInfo(gomeGmpResProjectVO, "update");
		model.addAttribute(Constants.PROJECT_VIEW_ATTRSTR,gomeGmpResProjectVO.getProType());
		return new ModelAndView("/project/updateProject").addObject(gomeGmpResProjectVO);
	}

	/**
	 * 修改操作
	 * 
	 * @param pro
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public PageModel update(@ModelAttribute GomeGmpResProjectVO gomeGmpResProjectVO, @RequestParam(value = "file", required = false) CommonsMultipartFile[] files) {
		// 上传文件
		List<GomeGmpResDatasynVO> uploadFileInfo = BusinessUtil.uploadFiles(gomeGmpResProjectVO.getProType() + "", files, getRequest());
		// 保存基础信息
		gomeGmpResProjectVO.setUploadFileInfo(uploadFileInfo);
		Map<String, Object> saveParms = new HashMap<String, Object>();
		int saveResult = gomeGmpResProjectBS.updateGomeGmpResProject(gomeGmpResProjectVO,saveParms);
		String flag = Constants.FAILD;
		if (saveResult > 0) {
			flag = Constants.SUCCESS;
			// 已提交项目，项目“进度状态”变更为“延期风险”、“已延期”时，触发报警邮件。
			if(gomeGmpResProjectVO.getIsCommit() == 1 && (gomeGmpResProjectVO.getScheduleId() == 3 || gomeGmpResProjectVO.getScheduleId() == 4) ) {
				gomeGmpResProjectBS.sendMails(gomeGmpResProjectVO,saveParms);
			}
		}
		Map<String, Object> resultData = new HashMap<String, Object>();
		resultData.put("flag", flag);
		PageModel model = new PageModel();
		model.setResultData(resultData);
		String targetUrl = "/project/toProjectView?listType=init&proType="+gomeGmpResProjectVO.getProType();
		model.setTargetUrl(targetUrl);
		return model;
	}

	/**
	 * 关闭项目
	 * @param proId
	 * @return
	 */
	@RequestMapping(value = "/closePro", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Message closePro(String proId) {
		Message msg = null;
		if(StringUtils.isNotEmpty(proId)){
			int saveResult = gomeGmpResProjectBS.closePro(proId,BusinessUtil.getLoginUserId(getRequest()));
			if(saveResult>0){
				msg = Message.success();
			}
		}else{
			msg = Message.failure();
		}
		return msg;
	}
	

	/**
	 * 删除项目
	 * @param proId
	 * @return
	 */
	@RequestMapping(value = "/deletePro", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Message deletePro(String proId) {
		Message msg = null;
		if(StringUtils.isNotEmpty(proId)){
			Integer result = gomeGmpResProjectBS.deleteGomeGmpResProjectById(proId);
			if(result>0){
				msg = Message.success();
				msg.setData(proId);
			}
		}else{
			msg = Message.failure();
		}
		return msg;
	}

	/**
	 * 查询甘特图数据
	 * 
	 * @return
	 * @author wubin
	 */
	@RequestMapping(value = "gantt/{proType}/{proId}", method = RequestMethod.GET)
	public ModelAndView gantt(Model model,@PathVariable String proId, @PathVariable String proType) {
		if(StringUtils.isNotEmpty(proType)){
			model.addAttribute(Constants.PROJECT_VIEW_ATTRSTR,Integer.parseInt(proType));
		}
		return new ModelAndView("/project/gantt");
	}

	/**
	 * 查询甘特图数据
	 * 
	 * @return
	 * @author wubin
	 */
	@RequestMapping(value = "gantt/find/{proId}", method = RequestMethod.GET)
	public List<GomeGmpResTaskVO> findGanttData(@PathVariable String proId) {
		GomeGmpResTaskVO gomeGmpResTaskVO = new GomeGmpResTaskVO();
		gomeGmpResTaskVO.setProId(proId);
		List<GomeGmpResTaskVO> relatedTasks = gomeGmpResProjectBS.findGanttData(gomeGmpResTaskVO);
		for (int i = 0; i < relatedTasks.size(); i++) {
			GomeGmpResTaskVO taskVO = relatedTasks.get(i);
			if (taskVO.getStartTime() == null || taskVO.getEndTime() == null) {
				relatedTasks.remove(i);
				i--;
			}
		}
		return relatedTasks;
	}

	/**
	 * 新建编辑时加载默认信息
	 * 
	 * @param proVO
	 */
	private void loadProDefaultInfo(GomeGmpResProjectVO gomeGmpResProjectVO, String operateType) {
		if ("add".equals(operateType)) {
			// 项目经理默认为登录用户
			GomeGmpResUserBO loginUser = BusinessUtil.getLoginUser(getRequest());
			gomeGmpResProjectVO.setBpId(NumberUtils.toInt(String.valueOf(loginUser.getId())));
			gomeGmpResProjectVO.setBpName(loginUser.getUserName());
		}
		gomeGmpResProjectVO.setTaskStatusV(gomeGmpDictItemBS.getDictItem("statusId"));
		gomeGmpResProjectVO.setSystemsV(gomeGmpDictItemBS.getDictItem("system"));
		gomeGmpResProjectVO.setResUnitV(gomeGmpOrgManageBS.getOrgListByLevel(Constants.ORGLEVEL_FIRST));
		gomeGmpResProjectVO.setPriorityIdsV(gomeGmpDictItemBS.getDictItem("priorityId"));
		gomeGmpResProjectVO.setScheduleIdsV(gomeGmpDictItemBS.getDictItem("scheduleId"));
		gomeGmpResProjectVO.setActualizesV(gomeGmpDictItemBS.getDictItem("actualize"));
	}

	/**
	 * 补充默认角色信息
	 * 
	 * @param gomeGmpResProjectVO
	 * @author wubin
	 */
	private void loadProDefaultRoles(GomeGmpResProjectVO gomeGmpResProjectVO) {
		List<GomeGmpResRoleVO> defaultRoles = gomeGmpRoleBS.defaultRoleInfo();
		List<GomeGmpResRelatedUserVO> relatedUsers = gomeGmpResProjectVO.getRelatedUsers();
		Map<Integer, GomeGmpResRelatedUserVO> relatedMap = new HashMap<Integer, GomeGmpResRelatedUserVO>();
		Map<Integer, GomeGmpResRoleVO> defaultMap = new HashMap<Integer, GomeGmpResRoleVO>();
		for (GomeGmpResRelatedUserVO relatedUser : relatedUsers) {
			relatedMap.put(relatedUser.getRoleId(), relatedUser);
		}
		for (GomeGmpResRoleVO roleVO : defaultRoles) {
			defaultMap.put(NumberUtils.toInt(String.valueOf(roleVO.getId())), roleVO);
		}
		relatedUsers = new ArrayList<GomeGmpResRelatedUserVO>();
		for (GomeGmpResRoleVO defaultRole : defaultRoles) {
			if (!relatedMap.containsKey(NumberUtils.toInt(String.valueOf(defaultRole.getId())))) {
				GomeGmpResRelatedUserVO relatedUser = new GomeGmpResRelatedUserVO();
				relatedUser.setRoleId(NumberUtils.toInt(String.valueOf(defaultRole.getId())));
				relatedUser.setRoleName(defaultRole.getRoleName());
				relatedUsers.add(relatedUser);
			} else {
				relatedUsers.add(relatedMap.get(NumberUtils.toInt(String.valueOf(defaultRole.getId()))));
			}
		}
		for (Integer key : relatedMap.keySet()) {
			GomeGmpResRelatedUserVO relatedUser = relatedMap.get(key);
			if (!defaultMap.containsKey(key)) {
				relatedUser.setDelete(true);
				relatedUsers.add(relatedUser);
			}
		}
		gomeGmpResProjectVO.setRelatedUsers(relatedUsers);
	}
}