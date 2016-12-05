package com.gome.gmp.business.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.gome.gmp.business.GomeGmpDictItemBS;
import com.gome.gmp.business.GomeGmpOrgManageBS;
import com.gome.gmp.business.GomeGmpResProjectBS;
import com.gome.gmp.common.BusinessUtil;
import com.gome.gmp.common.FileExportUtil;
import com.gome.gmp.common.ReflectUtil;
import com.gome.gmp.common.constant.Constants;
import com.gome.gmp.common.filter.CommandContext;
import com.gome.gmp.common.util.DateUtil;
import com.gome.gmp.common.util.SendMailUtil;
import com.gome.gmp.dao.GomeGmpResDatasynDAO;
import com.gome.gmp.dao.GomeGmpResLogDAO;
import com.gome.gmp.dao.GomeGmpResNeedDAO;
import com.gome.gmp.dao.GomeGmpResProSysDAO;
import com.gome.gmp.dao.GomeGmpResProjectDAO;
import com.gome.gmp.dao.GomeGmpResRelatedUserDAO;
import com.gome.gmp.dao.GomeGmpResTaskDAO;
import com.gome.gmp.dao.GomeGmpResUserDAO;
import com.gome.gmp.dao.GomeGmpTJProjectDAO;
import com.gome.gmp.model.bo.GomeGmpResDatasynBO;
import com.gome.gmp.model.bo.GomeGmpResLogBO;
import com.gome.gmp.model.bo.GomeGmpResOrgBO;
import com.gome.gmp.model.bo.GomeGmpResProjectBO;
import com.gome.gmp.model.bo.GomeGmpResUserBO;
import com.gome.gmp.model.vo.GomeGmpResDailyVO;
import com.gome.gmp.model.vo.GomeGmpResDatasynVO;
import com.gome.gmp.model.vo.GomeGmpResLogVO;
import com.gome.gmp.model.vo.GomeGmpResNeedVO;
import com.gome.gmp.model.vo.GomeGmpResOrgVO;
import com.gome.gmp.model.vo.GomeGmpResProSysVO;
import com.gome.gmp.model.vo.GomeGmpResProjectVO;
import com.gome.gmp.model.vo.GomeGmpResRelatedUserVO;
import com.gome.gmp.model.vo.GomeGmpResTaskVO;
import com.gome.gmp.model.vo.GomeGmpTJHourVO;

/**
 * 项目业务逻辑层
 * 
 * @author wubin
 */
@Service("gomeGmpResProjectBS")
public class GomeGmpResProjectBSImpl extends BaseBS implements GomeGmpResProjectBS {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private GomeGmpDictItemBS gomeGmpDictItemBS;

	@Resource
	private GomeGmpOrgManageBS gomeGmpOrgManageBS;

	@Resource
	private GomeGmpResProjectDAO gomeGmpResProjectDAO;

	@Resource
	private GomeGmpResRelatedUserDAO gomeGmpResRelatedUserDAO;

	@Resource
	private GomeGmpResTaskDAO gomeGmpResTaskDAO;

	@Resource
	private GomeGmpResDatasynDAO gomeGmpResDatasynDAO;

	@Resource
	private GomeGmpResProSysDAO gomeGmpResProSysDAO;

	@Resource
	private GomeGmpResLogDAO gomeGmpResLogDAO;

	@Resource
	private GomeGmpResNeedDAO gomeGmpResNeedDAO;

	@Resource
	private GomeGmpResUserDAO gomeGmpResUserDao;

	@Resource
	private GomeGmpTJProjectDAO gomeGmpTJProjectDAO;

	/**
	 * 查询项目信息
	 * 
	 * @param gomeGmpResProjectVO
	 * @return
	 */
	@Override
	public Page<GomeGmpResProjectVO> findGomeGmpResProject(GomeGmpResProjectVO gomeGmpResProjectVO) {
		// 登录用户
		Long loginUserId = BusinessUtil.getLoginUserId(CommandContext.getRequest());
		gomeGmpResProjectVO.setLoginUserId(loginUserId);
		gomeGmpResProjectVO.setEndTime(DateUtil.getDate(DateUtil.getDate(gomeGmpResProjectVO.getEndTime()) + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		Page<GomeGmpResProjectVO> gomeGmpResProjectBOs = findProjects(gomeGmpResProjectVO);
		// 查询参数存储供导出使用
		CommandContext.getRequest().getSession().setAttribute("PROJECT_FIND_PARAM", gomeGmpResProjectVO);
		return gomeGmpResProjectBOs;
	}

	/**
	 * 保存项目信息
	 * 
	 * @param oldPro
	 * @param newPro
	 * @return
	 */
	@Override
	@Transactional
	public int saveGomeGmpResProject(GomeGmpResProjectVO gomeGmpResProjectVO, Map<String, Object> saveParms) {
		Integer userId = NumberUtils.toInt(String.valueOf(BusinessUtil.getLoginUserId(CommandContext.getRequest())));
		// 项目id根据bug号生成
		String proId = "00" + gomeGmpResProjectVO.getBugId();
		gomeGmpResProjectVO.setProId(proId);
		// 涉及系统保存
		String[] systemIds = gomeGmpResProjectVO.getSystemIds() == null ? null : gomeGmpResProjectVO.getSystemIds().split(",");
		// systemId转换为List
		if (systemIds != null) {
			for (String system : systemIds) {
				GomeGmpResProSysVO sys = new GomeGmpResProSysVO();
				if (StringUtils.isNotBlank(system)) {
					sys.setSysId(NumberUtils.toInt(system));
					sys.setProId(gomeGmpResProjectVO.getProId());
					gomeGmpResProSysDAO.saveGomeGmpResProSysBO(sys);
				}
			}
		}
		// 资料保存
		List<GomeGmpResDatasynVO> uploadFiles = gomeGmpResProjectVO.getUploadFileInfo();
		for (GomeGmpResDatasynBO uploadFile : uploadFiles) {
			uploadFile.setProId(proId);
			uploadFile.setProType(gomeGmpResProjectVO.getProType());
			uploadFile.setUploadUserId(userId);
			uploadFile.setUploadTime(new Date());
			gomeGmpResDatasynDAO.saveGomeGmpResDatasynBO(uploadFile);
		}
		// 默认相关人
		HashSet<Integer> defaultUserIds = new HashSet<Integer>();
		// 排期保存
		List<GomeGmpResTaskVO> tasks = gomeGmpResProjectVO.getRelatedTasks();
		if (tasks != null) {
			for (GomeGmpResTaskVO gomeGmpResTaskVO : tasks) {
				gomeGmpResTaskVO.setProId(gomeGmpResProjectVO.getProId());
				gomeGmpResTaskVO.setProType(gomeGmpResProjectVO.getProType());
				// 如果人员置空则将id置空
				if (StringUtils.isBlank(gomeGmpResTaskVO.getUserName())) {
					gomeGmpResTaskVO.setUserId(null);
				}
				if (gomeGmpResTaskVO.getUserId() != null) {
					defaultUserIds.add(gomeGmpResTaskVO.getUserId());
				}
				gomeGmpResTaskDAO.saveGomeGmpResTaskBO(gomeGmpResTaskVO);
			}
		}
		// 存关联用户
		this.addProRelatedUsers(gomeGmpResProjectVO,defaultUserIds,saveParms);
		// 如果是需求转项目则修改需求状态为已接受
		if (gomeGmpResProjectVO.getDemandId() != null) {
			updateNeedStates(gomeGmpResProjectVO, 3);
		}
		// 项目进度状态改为已上线的把需求状态改为已完成
		if (gomeGmpResProjectVO.getScheduleId() == 5) {
			updateNeedStates(gomeGmpResProjectVO, 1);
		}
		// 更新用户，创建用户
		gomeGmpResProjectVO.setUpdateUser(userId);
		gomeGmpResProjectVO.setCreateUser(userId);
		// 项目经理未填写则置空
		if (StringUtils.isBlank(gomeGmpResProjectVO.getBpName())) {
			gomeGmpResProjectVO.setBpId(null);
		}
		// 关键用户未填写则置空
		if (StringUtils.isBlank(gomeGmpResProjectVO.getKeyUserName())) {
			gomeGmpResProjectVO.setKeyUserId(null);
		}
		return gomeGmpResProjectDAO.saveGomeGmpResProjectBO(gomeGmpResProjectVO);
	}

	private void addProRelatedUsers(GomeGmpResProjectVO gomeGmpResProjectVO, HashSet<Integer> defaultUserIds, Map<String, Object> saveParms) {
		List<GomeGmpResRelatedUserVO> relatedUsers = gomeGmpResProjectVO.getRelatedUsers();
		if (relatedUsers != null) {
			for (GomeGmpResRelatedUserVO relatedUserVO : relatedUsers) {
				if (relatedUserVO.getUserId() != null && StringUtils.isNotBlank(relatedUserVO.getUserName())) {
					relatedUserVO.setProId(gomeGmpResProjectVO.getProId());
					relatedUserVO.setProType(gomeGmpResProjectVO.getProType());
					relatedUserVO.setRelatedUserType(0);
					defaultUserIds.add(relatedUserVO.getUserId());
					gomeGmpResRelatedUserDAO.saveGomeGmpResRelatedUserBO(relatedUserVO);
				}
			}
		}
		// 默认相关人
		if (gomeGmpResProjectVO.getBpId() != null) {
			defaultUserIds.add(gomeGmpResProjectVO.getBpId());
		}
		if (gomeGmpResProjectVO.getKeyUserId() != null) {
			defaultUserIds.add(gomeGmpResProjectVO.getKeyUserId());
		}
		// 项目任务人员的上级领导及领导的领导一直追溯到中心负责人默认为该项目相关人
		saveAllLevelLeaderId(gomeGmpResProjectVO,defaultUserIds,saveParms);
	}

	/**
	 * 获取上级及上级的上级领导
	 * 
	 * @param userId
	 * @return
	 * @author wubin
	 * @param saveParms 
	 */
	private int saveAllLevelLeaderId(GomeGmpResProjectVO gomeGmpResProjectVO,Set<Integer> defaultUserIds, Map<String, Object> saveParms) {
		Set<String> totalLeaderIds = new HashSet<String>();
		// 获取所有等级领导id
		for (Integer userId : defaultUserIds) {
			Set<String> leaderIds = gomeGmpOrgManageBS.getAllLevelLeaderId(userId);
			if (leaderIds != null) {
				for (String leaderId : leaderIds) {
					if (!defaultUserIds.contains(leaderId)) {
						totalLeaderIds.add(leaderId);
					}
				}
			}
		}
		// 保存所有上级领导
		int total = 0;
		if (totalLeaderIds != null) {
			for (String leaderId : totalLeaderIds) {
				GomeGmpResRelatedUserVO gomeGmpResRelatedUserVO = new GomeGmpResRelatedUserVO();
				gomeGmpResRelatedUserVO.setUserId(NumberUtils.toInt(leaderId));
				gomeGmpResRelatedUserVO.setProId(gomeGmpResProjectVO.getProId());
				gomeGmpResRelatedUserVO.setProType(gomeGmpResProjectVO.getProType());
				gomeGmpResRelatedUserVO.setRelatedUserType(1);
				int result = gomeGmpResRelatedUserDAO.saveGomeGmpResRelatedUserBO(gomeGmpResRelatedUserVO);
				if (result > 0) {
					total += result;
				}
			}
			saveParms.put("leaderIds", totalLeaderIds);
		}
		return total;
	}

	/**
	 * 更改需求状态
	 * 
	 * @param gomeGmpResProjectVO
	 * @return
	 * @author wubin
	 */
	private int updateNeedStates(GomeGmpResProjectVO gomeGmpResProjectVO, Integer status) {
		GomeGmpResNeedVO gomeGmpResNeedVO = new GomeGmpResNeedVO();
		gomeGmpResNeedVO.setNeedId(String.valueOf(gomeGmpResProjectVO.getDemandId()));
		gomeGmpResNeedVO.setType(gomeGmpResProjectVO.getProType());
		gomeGmpResNeedVO.setStates(status);
		return gomeGmpResNeedDAO.updateGomeGmpResNeedBO(gomeGmpResNeedVO);
	}

	/**
	 * 更新项目信息
	 * 
	 * @see com.gome.gmp.business.GomeGmpResProjectBS#updateGomeGmpResProjectBOById(com.gome.gmp.model.bo.GomeGmpResProjectBO)
	 */
	@Override
	@Transactional
	public int updateGomeGmpResProject(GomeGmpResProjectVO gomeGmpResProjectVO,Map<String, Object> saveParms) {
		// 当前登陆用户
		Integer userId = NumberUtils.toInt(String.valueOf(BusinessUtil.getLoginUserId(CommandContext.getRequest())));
		// 项目进度状态改为已上线的把需求状态改为已完成
		if (gomeGmpResProjectVO.getScheduleId() == 5) {
			updateNeedStates(gomeGmpResProjectVO, 1);
		} else {
			updateNeedStates(gomeGmpResProjectVO, 3);
		}
		// 项目经理未填写则置空
		if (StringUtils.isBlank(gomeGmpResProjectVO.getBpName())) {
			gomeGmpResProjectVO.setBpId(null);
		}
		// 关键用户未填写则置空
		if (StringUtils.isBlank(gomeGmpResProjectVO.getKeyUserName())) {
			gomeGmpResProjectVO.setKeyUserId(null);
		}
		// 默认相关人
		// 记录日志
		try{
			recordLog(gomeGmpResProjectVO);
		} catch(Exception e) {
			logger.warn("记录操作日志失败", e);
		}
		// 关联用户
		// 删除关联用户
		gomeGmpResRelatedUserDAO.deleteGomeGmpResRelatedUserBOByProId(gomeGmpResProjectVO.getProId());
		// 删除排期
		gomeGmpResTaskDAO.deleteGomeGmpResTaskBOByProId(gomeGmpResProjectVO.getProId());
		HashSet<Integer> defaultUserIds = new HashSet<Integer>();
		// 新增排期
		List<GomeGmpResTaskVO> tasks = gomeGmpResProjectVO.getRelatedTasks();
		if (tasks != null) {
			for (GomeGmpResTaskVO taskVO : tasks) {
				if (taskVO.getTaskNum() == null) {
					continue;
				}
				taskVO.setProId(gomeGmpResProjectVO.getProId());
				taskVO.setProType(gomeGmpResProjectVO.getProType());
				// 如果人员置空则将id置空
				if (StringUtils.isBlank(taskVO.getUserName())) {
					taskVO.setUserId(null);
				}
				if (taskVO.getUserId() != null) {
					defaultUserIds.add(taskVO.getUserId());
				}
				gomeGmpResTaskDAO.saveGomeGmpResTaskBO(taskVO);
			}
		}
		// 存关联用户
		this.addProRelatedUsers(gomeGmpResProjectVO,defaultUserIds,saveParms);
		// 删除涉及系统
		gomeGmpResProSysDAO.deleteGomeGmpResProSysBOByProId(gomeGmpResProjectVO.getProId());
		// 新增涉及系统
		// 涉及系统保存
		String[] systemIds = gomeGmpResProjectVO.getSystemIds() == null ? null : gomeGmpResProjectVO.getSystemIds().split(",");
		// systemId转换为List
		if (gomeGmpResProjectVO.getSystemIds() != null) {
			for (String system : systemIds) {
				GomeGmpResProSysVO sys = new GomeGmpResProSysVO();
				if (StringUtils.isNotBlank(system)) {
					sys.setSysId(NumberUtils.toInt(system));
					sys.setProId(gomeGmpResProjectVO.getProId());
					gomeGmpResProSysDAO.saveGomeGmpResProSysBO(sys);
				}
			}
		}
		// 删除上传附件
		if (gomeGmpResProjectVO.getOldUploadFileInfo() != null) {
			List<GomeGmpResDatasynVO> oldUploadFiles = gomeGmpResProjectVO.getOldUploadFileInfo();
			List<Long> ids = new ArrayList<Long>();
			for (GomeGmpResDatasynVO gomeGmpResDatasynVO : oldUploadFiles) {
				if (gomeGmpResDatasynVO.getId() != null) {
					ids.add(gomeGmpResDatasynVO.getId());
				}
			}
			GomeGmpResDatasynVO gomeGmpResDatasynVO = new GomeGmpResDatasynVO();
			gomeGmpResDatasynVO.setDeleteIds(ids);
			gomeGmpResDatasynVO.setProId(gomeGmpResProjectVO.getProId());
			gomeGmpResDatasynDAO.deleteGomeGmpResDatasynBOByIds(gomeGmpResDatasynVO);
		} else {
			List<Long> ids = new ArrayList<Long>();
			ids.add(-1L);
			GomeGmpResDatasynVO gomeGmpResDatasynVO = new GomeGmpResDatasynVO();
			gomeGmpResDatasynVO.setDeleteIds(ids);
			gomeGmpResDatasynVO.setProId(gomeGmpResProjectVO.getProId());
			gomeGmpResDatasynDAO.deleteGomeGmpResDatasynBOByIds(gomeGmpResDatasynVO);
		}
		// 资料保存
		List<GomeGmpResDatasynVO> uploadFiles = gomeGmpResProjectVO.getUploadFileInfo();
		if (uploadFiles != null) {
			for (GomeGmpResDatasynBO uploadFile : uploadFiles) {
				uploadFile.setProId(gomeGmpResProjectVO.getProId());
				uploadFile.setProType(gomeGmpResProjectVO.getProType());
				uploadFile.setUploadUserId(userId);
				uploadFile.setUploadTime(new Date());
				gomeGmpResDatasynDAO.saveGomeGmpResDatasynBO(uploadFile);
			}
		}
		// 更新用户
		gomeGmpResProjectVO.setUpdateUser(userId);
		logger.info("业务部门：");
		return gomeGmpResProjectDAO.updateGomeGmpResProjectBOById(gomeGmpResProjectVO);
	}

	/**
	 * 发送报警邮件
	 * 
	 * @param gomeGmpResProjectVO
	 * @author wubin
	 */
	@SuppressWarnings("unchecked")
	public void sendMails(GomeGmpResProjectVO gomeGmpResProjectVO, Map<String, Object> saveParms) {
		if(StringUtils.isNoneBlank(gomeGmpResProjectVO.getProId())){
			GomeGmpResRelatedUserVO queryRelatedUserVo = new GomeGmpResRelatedUserVO();
			queryRelatedUserVo.setProId(gomeGmpResProjectVO.getProId());
			HashSet<String> accounts = new HashSet<String>();
			// 项目相关人
			List<GomeGmpResTaskVO> relatedTasks = gomeGmpResProjectVO.getRelatedTasks();
			List<GomeGmpResRelatedUserVO> relatedUsers = gomeGmpResProjectVO.getRelatedUsers();
			for (GomeGmpResTaskVO gomeGmpResTaskVO : relatedTasks) {
				if (gomeGmpResTaskVO.getUserId() != null) {
					GomeGmpResUserBO userBo = gomeGmpResUserDao.findGomeGmpResUserBOById(gomeGmpResTaskVO.getUserId());
					if(userBo.getOrgId()!=null && userBo.getOrgId().length()!=2){
						String account = "";
						if(StringUtils.isNotBlank(userBo.getEmail())){
							account = userBo.getEmail();
						}else{
							account = userBo.getUserId()+"@yolo24.com";
						}
						accounts.add(account);
					}
				}
			}
			for (GomeGmpResRelatedUserVO gomeGmpResRelatedUserVO : relatedUsers) {
				if (gomeGmpResRelatedUserVO.getUserId() != null) {
					GomeGmpResUserBO userBo = gomeGmpResUserDao.findGomeGmpResUserBOById(gomeGmpResRelatedUserVO.getUserId());
					if(userBo.getOrgId()!=null && userBo.getOrgId().length()!=2){
						String account = "";
						if(StringUtils.isNotBlank(userBo.getEmail())){
							account = userBo.getEmail();
						}else{
							account = userBo.getUserId()+"@yolo24.com";
						}
						accounts.add(account);
					}
				}
			}
			if(saveParms.get("leaderIds")!=null){
				Set<String> leaderIds =  (Set<String>) saveParms.get("leaderIds");
				for (String leaderId : leaderIds) {
					GomeGmpResUserBO userBo = gomeGmpResUserDao.findGomeGmpResUserBOById(Long.valueOf(leaderId).longValue());
					if(userBo.getOrgId()!=null && userBo.getOrgId().length()!=2){
						String account = "";
						if(StringUtils.isNotBlank(userBo.getEmail())){
							account = userBo.getEmail();
						}else{
							account = userBo.getUserId()+"@yolo24.com";
						}
						accounts.add(account);
					}
				}
			}
			// PMO通讯组
	//		accounts.add("PMO2@yolo24.com");
			StringBuffer sb = new StringBuffer();
			sb.append("您关注的项目有延期风险，项目信息如下。<br>").append("&nbsp;&nbsp;项目名称：" + gomeGmpResProjectVO.getTitle() + "<br>").append("&nbsp;&nbsp;计划上线时间：" + DateUtil.getDate(gomeGmpResProjectVO.getPlanTime()) + "<br>").append("&nbsp;&nbsp;优先级：" + gomeGmpDictItemBS.getItemName("priorityId", String.valueOf(gomeGmpResProjectVO.getPriorityId())) + "<br>").append("&nbsp;&nbsp;进度状态：延期风险/已延期<br>").append("&nbsp;&nbsp;实施阶段：" + gomeGmpDictItemBS.getItemName("actualize", String.valueOf(gomeGmpResProjectVO.getActualize())) + "<br>");
			// 报警-延期风险报警邮件，发给PMO和项目相关人
			SendMailUtil.sendMails(accounts.toArray(new String[0]), "项目延期预警", sb.toString());
		}
	}

	/**
	 * 根据id删除项目信息
	 * 
	 * @see com.gome.gmp.business.GomeGmpResProjectBS#deleteGomeGmpResProjectBOById(java.lang.Long)
	 */
	@Override
	@Transactional
	public Integer deleteGomeGmpResProjectById(String proId) {
		gomeGmpResRelatedUserDAO.deleteGomeGmpResRelatedUserBOByProId(proId);
		gomeGmpResTaskDAO.deleteGomeGmpResTaskBOByProId(proId);
		gomeGmpResProSysDAO.deleteGomeGmpResProSysBOByProId(proId);
		gomeGmpResDatasynDAO.deleteGomeGmpResDatasynBOByProId(proId);
		// 另定时任务删除物理文件
		// ...
		return gomeGmpResProjectDAO.deleteGomeGmpResProjectBOById(proId);
	}

	/**
	 * 查询项目信息
	 * 
	 * @param gomeGmpResProjectVO
	 * @return
	 * @author wubin
	 */
	private Page<GomeGmpResProjectVO> findProjects(GomeGmpResProjectVO gomeGmpResProjectVO) {
		Page<GomeGmpResProjectVO> gomeGmpResProjectBOs = gomeGmpResProjectDAO.findGomeGmpResProjectBO(gomeGmpResProjectVO);
		for (GomeGmpResProjectVO pro : gomeGmpResProjectBOs) {
			pro.setStatusName(gomeGmpDictItemBS.getItemName("statusId", pro.getStatusId() + ""));
			pro.setPriorityName(gomeGmpDictItemBS.getItemName("priorityId", pro.getPriorityId() + ""));
			pro.setScheduleName(gomeGmpDictItemBS.getItemName("scheduleId", pro.getScheduleId() + ""));
			pro.setActualizeName(gomeGmpDictItemBS.getItemName("actualize", pro.getActualize() + ""));
			pro.setBpName(gomeGmpDictItemBS.getItemName("userId", pro.getBpId() + ""));
			pro.setUnitBsName(gomeGmpDictItemBS.getItemName("unitBsId", pro.getUnitBsId() + ""));
			pro.setKeyUserName(gomeGmpDictItemBS.getItemName("userId", pro.getKeyUserId() + ""));
			pro.setUpdateUserName(gomeGmpDictItemBS.getItemName("userId", pro.getUpdateUser() + ""));
			pro.setCreateUserName(gomeGmpDictItemBS.getItemName("userId", pro.getCreateUser() + ""));
		}
		return gomeGmpResProjectBOs;
	}

	/**
	 * 根据id查询项目信息
	 * 
	 * @see com.gome.gmp.business.GomeGmpResProjectBS#findGomeGmpResProjectBOById(java.lang.Long)
	 */
	@Override
	public GomeGmpResProjectVO findGomeGmpResProjectById(GomeGmpResProjectVO gomeGmpResProjectVO) {
		// 登录用户
		Long loginUserId = BusinessUtil.getLoginUserId(CommandContext.getRequest());
		gomeGmpResProjectVO.setLoginUserId(loginUserId);
		Page<GomeGmpResProjectVO> proVoPage = findProjects(gomeGmpResProjectVO);
		if (proVoPage != null && proVoPage.getResult()!=null && proVoPage.getResult().size() > 0) {
			for (GomeGmpResProjectVO proVo : proVoPage.getResult()) {
				if(proVo.getProId().equals(gomeGmpResProjectVO.getProId())){
					gomeGmpResProjectVO = proVo;
				}
			}
		} else {
			return null;
		}
		
		// 将所有级别的orgId, 用于查询及修改界面的业务部门数据回填(格式：centerOrgId-orgId-childOrgId)
		GomeGmpResOrgBO orgBo = gomeGmpOrgManageBS.findOrgBOByOrgId(gomeGmpResProjectVO.getUnitBsId());
		if(gomeGmpResProjectVO.getUnitBsId() != null && orgBo != null) {
			gomeGmpResProjectVO.setUnitBsIdAll(gomeGmpOrgManageBS.getAllLevelOrgId(orgBo, orgBo.getOrgId()));
		}
		// 将所有级别的orgName, 用于查询及修改界面的业务部门数据回填((格式：centerOrgName-orgName-childOrgName))
		if(gomeGmpResProjectVO.getUnitBsId() != null && orgBo != null) {
			gomeGmpResProjectVO.setUnitBsName(gomeGmpOrgManageBS.getAllLevelOrgName(orgBo, orgBo.getOrgName()));
		}
		
		// 涉及系统
		GomeGmpResProSysVO gomeGmpResProSysVO = new GomeGmpResProSysVO();
		gomeGmpResProSysVO.setProId(gomeGmpResProjectVO.getProId());
		List<GomeGmpResProSysVO> systems = gomeGmpResProSysDAO.findGomeGmpResProSysBO(gomeGmpResProSysVO);
		StringBuffer sb = new StringBuffer();
		StringBuffer nameSB = new StringBuffer();
		for (GomeGmpResProSysVO sysVO : systems) {
			sb.append(",").append(sysVO.getSysId());
			nameSB.append(",").append(sysVO.getSysName());
		}
		String systemId = sb.toString();
		String systemName = nameSB.toString();
		if (systemId.indexOf(",") > -1) {
			systemId = systemId.substring(1);
			systemName = systemName.substring(1);
		}
		gomeGmpResProjectVO.setSystemIds(systemId);
		gomeGmpResProjectVO.setSystemNames(systemName);
		gomeGmpResProjectVO.setSystems(systems);
		// 关联用户
		GomeGmpResRelatedUserVO gmpResRelatedUserVO = new GomeGmpResRelatedUserVO();
		gmpResRelatedUserVO.setProId(gomeGmpResProjectVO.getProId());
		List<GomeGmpResRelatedUserVO> relatedUsers = gomeGmpResRelatedUserDAO.findGomeGmpResRelatedUserBO(gmpResRelatedUserVO);
		gomeGmpResProjectVO.setRelatedUsers(relatedUsers);
		// 关联附件
		GomeGmpResDatasynVO gomeGmpResDatasynVO = new GomeGmpResDatasynVO();
		gomeGmpResDatasynVO.setProId(gomeGmpResProjectVO.getProId());
		List<GomeGmpResDatasynVO> uploadFileInfo = gomeGmpResDatasynDAO.findGomeGmpResDatasynFileBOByCondition(gomeGmpResDatasynVO);
		gomeGmpResProjectVO.setUploadFileInfo(uploadFileInfo);
		// 关联任务
		GomeGmpResTaskVO gomeGmpResTaskVO = new GomeGmpResTaskVO();
		gomeGmpResTaskVO.setProId(gomeGmpResProjectVO.getProId());
		List<GomeGmpResTaskVO> relatedTasks = gomeGmpResTaskDAO.findGomeGmpResTaskBO(gomeGmpResTaskVO);
		gomeGmpResProjectVO.setRelatedTasks(relatedTasks);
		// 查询计划工时，实际工时，百分比
		GomeGmpTJHourVO gomeGmpTJHourVO = new GomeGmpTJHourVO();
		gomeGmpTJHourVO.setProId(gomeGmpResProjectVO.getProId());
		List<GomeGmpResDailyVO> details = gomeGmpTJProjectDAO.workHourDetail(gomeGmpTJHourVO);
		Double realHour = 0.0;
		for (GomeGmpResDailyVO gomeGmpResDailyVO : details) {
			realHour += gomeGmpResDailyVO.getHours();
		}
		gomeGmpResProjectVO.setFinishHour(realHour);
		long finishPlan = Math.round(realHour / gomeGmpResProjectVO.getPlanHour() * 100);
		gomeGmpResProjectVO.setFinishPlan(finishPlan);
		return gomeGmpResProjectVO;
	}

	/**
	 * 导出项目详情
	 * 
	 * @param proId
	 * @author wubin
	 */
	@Override
	public void detailExport(String proId) {
		GomeGmpResProjectVO gomeGmpResProjectVO = new GomeGmpResProjectVO();
		gomeGmpResProjectVO.setProId(proId);
		gomeGmpResProjectVO = findGomeGmpResProjectById(gomeGmpResProjectVO);
		getExportExcel("项目详情导出", "项目详情导出文件", gomeGmpResProjectVO);
	}

	/**
	 * 导出逻辑抽取
	 * 
	 * @param fileName
	 * @param gomeGmpResProjectVO
	 * @author wubin
	 */
	private void getExportExcel(String title, String fileName, GomeGmpResProjectVO gomeGmpResProjectVO) {
		HttpServletResponse response = CommandContext.getResponse();
		response.setContentType("application/x-msdownload");
		try {
			fileName = new String(fileName.getBytes("gbk"), "ISO-8859-1");
		} catch (UnsupportedEncodingException e1) {
			logger.info("导出excel表格异常：fileName转码出错", e1);
		}
		// 创建一个工作薄
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sheet = workBook.createSheet(title);
		// style
		// title
		HSSFCellStyle titleStyle = workBook.createCellStyle();
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		// body style
		HSSFCellStyle bodyStyle = workBook.createCellStyle();
		bodyStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCellStyle bodyStyleLeft = workBook.createCellStyle();
		bodyStyleLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		// script
		int rowNum = 0;
		// title
		HSSFRow row = sheet.createRow(0);
		addCell(row, 0, " 基本内容", titleStyle);
		// 合并行
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 5));
		// body
		// 新增行
		rowNum++;
		row = sheet.createRow(rowNum);
		addCell(row, 0, "项目名称", bodyStyle);
		addCell(row, 1, gomeGmpResProjectVO.getTitle(), bodyStyle);
		addCell(row, 2, "项目ID", bodyStyle);
		addCell(row, 3, gomeGmpResProjectVO.getProId(), bodyStyle);
		addCell(row, 4, "涉及系统", bodyStyle);
		addCell(row, 5, gomeGmpResProjectVO.getSystemNames(), bodyStyle);
		// 新增行
		rowNum++;
		row = sheet.createRow(rowNum);
		addCell(row, 0, "项目经理", bodyStyle);
		addCell(row, 1, gomeGmpResProjectVO.getBpName(), bodyStyle);
		addCell(row, 2, "优先级", bodyStyle);
		addCell(row, 3, gomeGmpResProjectVO.getPriorityName(), bodyStyle);
		addCell(row, 4, "SVN路径", bodyStyle);
		addCell(row, 5, gomeGmpResProjectVO.getSvnAddr(), bodyStyle);
		// 新增行
		rowNum++;
		row = sheet.createRow(rowNum);
		addCell(row, 0, "业务部门", bodyStyle);
		addCell(row, 1, gomeGmpResProjectVO.getUnitBsName(), bodyStyle);
		addCell(row, 2, "关键用户", bodyStyle);
		addCell(row, 3, gomeGmpResProjectVO.getKeyUserName(), bodyStyle);
		addCell(row, 4, "相关需求", bodyStyle);
		addCell(row, 5, gomeGmpResProjectVO.getDemandName(), bodyStyle);
		// 新增行
		rowNum++;
		row = sheet.createRow(rowNum);
		addCell(row, 0, " 项目简述", bodyStyle);
		addCell(row, 1, gomeGmpResProjectVO.getRemark(), bodyStyleLeft);
		// 合并行
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 1, 5));
		// // 相关人
		List<GomeGmpResRelatedUserVO> relatedUsers = gomeGmpResProjectVO.getRelatedUsers();
		for (int i = 0; i < relatedUsers.size(); i++) {
			GomeGmpResRelatedUserVO relatedUser = relatedUsers.get(i);
			if (i % 3 == 0) {
				rowNum++;
				row = sheet.createRow(rowNum);
			}
			addCell(row, (i % 3) * 2, relatedUser.getRoleName(), bodyStyle);
			addCell(row, (i % 3) * 2 + 1, relatedUser.getUserName(), bodyStyle);
		}
		// 新增行
		rowNum++;
		row = sheet.createRow(rowNum);
		addCell(row, 0, "实际工时", bodyStyle);
		addCell(row, 1, gomeGmpResProjectVO.getFinishHour() + "h", bodyStyle);
		addCell(row, 2, "计划工时", bodyStyle);
		addCell(row, 3, gomeGmpResProjectVO.getPlanHour() + "h", bodyStyle);
		addCell(row, 4, "实际工时/计划工时", bodyStyle);
		addCell(row, 5, gomeGmpResProjectVO.getFinishPlan() + "%", bodyStyle);
		// 新增行
		rowNum++;
		row = sheet.createRow(rowNum);
		// 合并行
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 7));
		// 新增行
		rowNum++;
		row = sheet.createRow(rowNum);
		addCell(row, 0, " 项目排期", titleStyle);
		// 合并行
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 7));
		// 新增行
		rowNum++;
		row = sheet.createRow(rowNum);
		addCell(row, 0, "任务状态", bodyStyle);
		addCell(row, 1, gomeGmpResProjectVO.getStatusName(), bodyStyle);
		addCell(row, 2, "进度状态", bodyStyle);
		addCell(row, 3, gomeGmpResProjectVO.getScheduleName(), bodyStyle);
		addCell(row, 4, "实施阶段", bodyStyle);
		addCell(row, 5, gomeGmpResProjectVO.getActualizeName(), bodyStyle);
		addCell(row, 6, "完成百分比", bodyStyle);
		addCell(row, 7, gomeGmpResProjectVO.getPercentage(), bodyStyle);
		// 新增行
		rowNum++;
		row = sheet.createRow(rowNum);
		addCell(row, 0, "开始时间", bodyStyle);
		addCell(row, 1, DateUtil.getDate(gomeGmpResProjectVO.getStartTime()), bodyStyle);
		addCell(row, 2, "计划上线时间", bodyStyle);
		addCell(row, 3, DateUtil.getDate(gomeGmpResProjectVO.getPlanTime()), bodyStyle);
		addCell(row, 4, "完成时间", bodyStyle);
		addCell(row, 5, DateUtil.getDate(gomeGmpResProjectVO.getFinishTime()), bodyStyle);
		addCell(row, 6, "项目工期", bodyStyle);
		addCell(row, 7, (gomeGmpResProjectVO.getProPeriod() == null ? "" : gomeGmpResProjectVO.getProPeriod()) + "天", bodyStyle);
		// 新增行
		rowNum++;
		row = sheet.createRow(rowNum);
		// 合并行
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 10));
		// 新增行
		rowNum++;
		row = sheet.createRow(rowNum);
		addCell(row, 0, "任务号", bodyStyle);
		addCell(row, 1, "任务名称", bodyStyle);
		addCell(row, 2, "开始时间", bodyStyle);
		addCell(row, 3, "结束时间", bodyStyle);
		addCell(row, 4, "工期", bodyStyle);
		addCell(row, 5, "前置任务", bodyStyle);
		addCell(row, 6, "进度", bodyStyle);
		addCell(row, 7, "BUG号", bodyStyle);
		addCell(row, 8, "计划工时", bodyStyle);
		addCell(row, 9, "人员名称", bodyStyle);
		addCell(row, 10, "项目职责", bodyStyle);
		// 任务详情
		rowNum++;
		List<GomeGmpResTaskVO> tasks = gomeGmpResProjectVO.getRelatedTasks();
		for (int i = 0; i < tasks.size(); i++) {
			GomeGmpResTaskVO gomeGmpResTaskVO = tasks.get(i);
			row = sheet.createRow(rowNum);
			addCell(row, 0, i + 1 + "", bodyStyle);
			addCell(row, 1, gomeGmpResTaskVO.getTaskName(), bodyStyle);
			addCell(row, 2, DateUtil.getDate(gomeGmpResTaskVO.getStartTime()), bodyStyle);
			addCell(row, 3, DateUtil.getDate(gomeGmpResTaskVO.getEndTime()), bodyStyle);
			addCell(row, 4, gomeGmpResTaskVO.getWorkPeriod() == null ? "" : gomeGmpResTaskVO.getWorkPeriod() + "天", bodyStyle);
			addCell(row, 5, gomeGmpResTaskVO.getPreTaskName(), bodyStyle);
			addCell(row, 6, gomeGmpResTaskVO.getSchedule() == null ? "" : gomeGmpResTaskVO.getSchedule() + "%", bodyStyle);
			addCell(row, 7, gomeGmpResTaskVO.getBugId(), bodyStyle);
			addCell(row, 8, gomeGmpResTaskVO.getPlanTime() == null ? "" : gomeGmpResTaskVO.getPlanTime() + "h", bodyStyle);
			addCell(row, 9, gomeGmpResTaskVO.getUserName(), bodyStyle);
			addCell(row, 10, gomeGmpResTaskVO.getRebuke(), bodyStyle);
			rowNum++;
		}
		// 新增行
		row = sheet.createRow(rowNum);
		// 合并行
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 10));
		// 自适应宽度
		for (int i = 0; i < 10; i++) {
			sheet.autoSizeColumn(i, true);
		}
		// 新增行
		rowNum++;
		row = sheet.createRow(rowNum);
		addCell(row, 0, "近期完成工作", bodyStyle);
		addCell(row, 1, gomeGmpResProjectVO.getNearWorkDone(), bodyStyleLeft);
		addCell(row, 5, "近期计划工作", bodyStyle);
		addCell(row, 6, gomeGmpResProjectVO.getNearWorkPlan(), bodyStyleLeft);
		// 合并行
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 1, 4));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 6, 10));
		// 新增行
		rowNum++;
		row = sheet.createRow(rowNum);
		addCell(row, 0, "风险说明", bodyStyle);
		addCell(row, 1, gomeGmpResProjectVO.getRiskRemark(), bodyStyleLeft);
		// 合并行
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 1, 10));
		sheet = null;
		row = null;
		// 让用户下载excel
		FileExportUtil.downloadFile(CommandContext.getRequest(), CommandContext.getResponse(), workBook, fileName);
	}

	/**
	 * 增加列
	 * 
	 * @author wubin
	 */
	private void addCell(HSSFRow row, int cellIndex, String cellContent, HSSFCellStyle cellStyle) {
		HSSFCell bodyCell = row.createCell(cellIndex);
		bodyCell.setCellValue(cellContent);
		bodyCell.setCellStyle(cellStyle);
	}

	/**
	 * 根据id查询项目信息
	 * 
	 * @see com.gome.gmp.business.GomeGmpResProjectBS#findGomeGmpResProjectBOById(java.lang.Long)
	 */
	@Override
	public boolean validateBugId(GomeGmpResProjectVO gomeGmpResProjectVO) {
		Integer rstNum = gomeGmpResProjectDAO.validateBugId(gomeGmpResProjectVO);
		if (rstNum > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 基础信息对比
	 * 
	 * @param oldPro
	 * @param newPro
	 * @return
	 */
	private List<GomeGmpResLogVO> baseCompare(GomeGmpResProjectVO oldPro, GomeGmpResProjectVO newPro) {
		List<GomeGmpResLogVO> rtnList = new ArrayList<GomeGmpResLogVO>();
		List<String> diffList = new ArrayList<String>();
		try {
			diffList = ReflectUtil.compare(oldPro, newPro);
		} catch (Exception e) {
			logger.error("对比两个对象不同属性值异常", e);
		}
		for (String col : diffList) {
			// 获取是否需要记录日志
			boolean isLog = ReflectUtil.getColIsLog(newPro, col);
			if (!isLog) {
				continue;
			}
			// 获取值
			String val = ReflectUtil.getFieldValueByName(col, newPro);
			// 获取字段的中文名
			String cnName = ReflectUtil.getColCName(newPro, col);
			// 组织日志数据
			GomeGmpResLogVO log = new GomeGmpResLogVO();
			log.setProId(newPro.getProId());
			// 获取val的中文字典
			String valCN = "";
			if ("keyUserId".equals(col) || "bpId".equals(col)) {
				valCN = gomeGmpDictItemBS.getItemName("userId", val);
			} else {
				valCN = gomeGmpDictItemBS.getItemName(col, val);
			}
			// 状态修改记录
			if ("statusId".equals(col) || "scheduleId".equals(col) || "actualize".equals(col)) {
				log.setOperateType("更新状态");
				log.setContent("更新\"" + cnName + "\"为\"" + valCN + "\"");
				log.setOperateColumn(col);
				log.setColumnVal(val);
			} else if (StringUtils.isBlank(val) || "null".equalsIgnoreCase(val)) {
				log.setOperateType("删除了非必填项");
				log.setContent("删除了\"" + cnName + "\"");
				log.setOperateColumn(col);
			} else {
				if (valCN == null) {
					valCN = val;
				}
				log.setOperateType("编辑基础信息");
				log.setContent("编辑了\"" + cnName + "\"为\"" + valCN + "\"");
				log.setOperateColumn(col);
				log.setColumnVal(val);
			}
			rtnList.add(log);
		}
		return rtnList;
	}

	/**
	 * 资料的日志保存
	 * 
	 * @param oldPro
	 * @param gomeGmpResProjectVO
	 * @return
	 * @author wubin
	 */
	private List<GomeGmpResLogVO> dataSynCompare(GomeGmpResProjectVO oldPro, GomeGmpResProjectVO gomeGmpResProjectVO) {
		List<GomeGmpResDatasynVO> oldDBUploadInfo = oldPro.getUploadFileInfo();
		List<GomeGmpResLogVO> rtnList = new ArrayList<GomeGmpResLogVO>();
		// 库中文件转map
		Map<Long, GomeGmpResDatasynVO> oldDBUploadMap = new HashMap<Long, GomeGmpResDatasynVO>();
		for (GomeGmpResDatasynVO gomeGmpResDatasynVO : oldDBUploadInfo) {
			oldDBUploadMap.put(gomeGmpResDatasynVO.getId(), gomeGmpResDatasynVO);
		}
		// 未删除的上传附件
		if (gomeGmpResProjectVO.getOldUploadFileInfo() != null) {
			List<GomeGmpResDatasynVO> oldUploadFiles = gomeGmpResProjectVO.getOldUploadFileInfo();
			for (GomeGmpResDatasynVO gomeGmpResDatasynVO : oldUploadFiles) {
				if (gomeGmpResDatasynVO.getId() != null) {
					oldDBUploadMap.remove(gomeGmpResDatasynVO.getId());
				}
			}
		}
		// 删除资料
		for (Long dataSynId : oldDBUploadMap.keySet()) {
			GomeGmpResDatasynVO gomeGmpResDatasynVO = oldDBUploadMap.get(dataSynId);
			GomeGmpResLogVO dataSyn = new GomeGmpResLogVO();
			dataSyn.setProId(gomeGmpResProjectVO.getProId());
			dataSyn.setOperateType("删除");
			dataSyn.setContent("删除了资料\"" + gomeGmpResDatasynVO.getFileName() + "\"");
			rtnList.add(dataSyn);
		}
		// 资料保存
		List<GomeGmpResDatasynVO> uploadFiles = gomeGmpResProjectVO.getUploadFileInfo();
		if (uploadFiles != null) {
			for (GomeGmpResDatasynBO uploadFile : uploadFiles) {
				GomeGmpResLogVO dataSyn = new GomeGmpResLogVO();
				dataSyn.setProId(gomeGmpResProjectVO.getProId());
				dataSyn.setOperateType("添加");
				dataSyn.setContent("添加了资料\"" + uploadFile.getFileName() + "\"");
				rtnList.add(dataSyn);
			}
		}
		return rtnList;
	}

	/**
	 * 涉及系统信息对比
	 * 
	 * @param oldPro
	 * @param newPro
	 * @return
	 */
	private List<GomeGmpResLogVO> systemCompare(GomeGmpResProjectVO oldPro, GomeGmpResProjectVO newPro) {
		List<GomeGmpResLogVO> rtnList = new ArrayList<GomeGmpResLogVO>();
		List<GomeGmpResProSysVO> oldSystems = oldPro.getSystems();
		String[] newSystems = newPro.getSystemIds() == null ? null : newPro.getSystemIds().split(",");
		StringBuffer oldStr = new StringBuffer();
		StringBuffer newStr = new StringBuffer();
		StringBuffer newStrCN = new StringBuffer();
		if (oldSystems != null) {
			for (GomeGmpResProSysVO oldSys : oldSystems) {
				oldStr.append(oldSys.getSysId());
				if (oldSystems.indexOf(oldSys) < oldSystems.size() - 1) {
					oldStr.append(",");
				}
			}
		}
		// StringUtils.collectionToDelimitedString(missingProps, ", "));
		if (newSystems != null) {
			for (int i = 0; i < newSystems.length; i++) {
				String sysId = newSystems[i];
				newStr.append(sysId);
				// 获取val的中文字典
				String valCN = gomeGmpDictItemBS.getItemName("system", String.valueOf(sysId));
				newStrCN.append(valCN);
				if(i < newSystems.length-1) {
					newStr.append(",");
					newStrCN.append(",");
				}
			}
		}
		if (!oldStr.toString().equals(newStr.toString())) {
			GomeGmpResLogVO log = new GomeGmpResLogVO();
			String val = newStr.length() > 0 ? newStr.substring(0, newStr.length() - 1) : "";
			String valCN = newStrCN.length() > 0 ? newStrCN.substring(0, newStrCN.length() - 1) : "";
			log.setOperateType("编辑基础信息");
			log.setContent("编辑了\"涉及系统\"为\"" + valCN + "\"");
			log.setOperateColumn("system");
			log.setColumnVal(val);
			log.setProId(newPro.getProId());
			rtnList.add(log);
		}
		return rtnList;
	}

	/**
	 * 角色信息对比
	 * 
	 * @param oldPro
	 * @param newPro
	 * @return
	 */
	private List<GomeGmpResLogVO> rolesCompare(GomeGmpResProjectVO oldPro, GomeGmpResProjectVO newPro) {
		List<GomeGmpResLogVO> rtnList = new ArrayList<GomeGmpResLogVO>();
		List<GomeGmpResRelatedUserVO> oldUsers = oldPro.getRelatedUsers();
		List<GomeGmpResRelatedUserVO> newUsers = newPro.getRelatedUsers();
		Map<Integer, Integer> oldUserMap = new HashMap<Integer, Integer>();
		if (newUsers == null || newUsers.size() == 0) {
			return rtnList;
		}
		for (GomeGmpResRelatedUserVO oldUser : oldUsers) {
			oldUserMap.put(oldUser.getRoleId(), oldUser.getUserId());
		}
		for (GomeGmpResRelatedUserVO newUser : newUsers) {
			GomeGmpResLogVO log = null;
			if (!oldUserMap.containsKey(newUser.getRoleId()) && newUser.getUserId() != null) {
				log = new GomeGmpResLogVO();
				log.setProId(newPro.getProId());
				log.setOperateType("添加角色");
				// 注意：角色名称和用户名称前端传递
				log.setContent("添加角色:\"" + newUser.getRoleName() + "\" " + newUser.getUserName());
				rtnList.add(log);
			}
		}
		return rtnList;
	}

	/**
	 * 排期信息对比
	 * 
	 * @param oldPro
	 * @param newPro
	 * @return
	 */
	private List<GomeGmpResLogVO> tasksCompare(GomeGmpResProjectVO oldPro, GomeGmpResProjectVO newPro) {
		List<GomeGmpResLogVO> rtnList = new ArrayList<GomeGmpResLogVO>();
		List<GomeGmpResTaskVO> oldTasks = oldPro.getRelatedTasks();
		List<GomeGmpResTaskVO> newTasks = newPro.getRelatedTasks();
		Map<Long, GomeGmpResTaskVO> oldTaskMap = new HashMap<Long, GomeGmpResTaskVO>();
		Map<Long, GomeGmpResTaskVO> newTaskMap = new HashMap<Long, GomeGmpResTaskVO>();
		// 前置任务号对应任务名称
		Map<Integer, String> newPreNumTaskMap = new HashMap<Integer, String>();
		if (newTasks != null) {
			for (GomeGmpResTaskVO newTask : newTasks) {
				newTaskMap.put(newTask.getId(), newTask);
				newPreNumTaskMap.put(newTask.getTaskNum(), newTask.getTaskName());
			}
		}
		if (oldTasks != null) {
			for (GomeGmpResTaskVO oldTask : oldTasks) {
				oldTaskMap.put(oldTask.getId(), oldTask);
			}
		}
		Long userId = BusinessUtil.getLoginUserId(CommandContext.getRequest());
		if (newTasks != null) {
			for (GomeGmpResTaskVO newTask : newTasks) {
				if (StringUtils.isBlank(newTask.getUserName())) {
					newTask.setUserId(null);
				}
				// 任务名称为null的用任务号显示
				String taskName = newTask.getTaskName();
				if (StringUtils.isBlank(taskName)) {
					taskName = "任务号：" + newTask.getTaskNum();
				}
				if ((!oldTaskMap.containsKey(newTask.getId()))) {
					GomeGmpResLogVO log = new GomeGmpResLogVO();
					log.setProId(newPro.getProId());
					log.setOperateType("新增任务项");
					log.setContent("新增任务项:" + taskName);
					log.setCreateUser(NumberUtils.toInt(String.valueOf(userId)));
					rtnList.add(log);
				} else {
					// 查看哪些列有改动
					GomeGmpResTaskVO oldTask = oldTaskMap.get(newTask.getId());
					List<String> diffList = new ArrayList<String>();
					try {
						diffList = ReflectUtil.compare(oldTask, newTask);
					} catch (Exception e) {
						logger.error("对比两个对象不同属性值异常", e);
					}
					for (String col : diffList) {
						GomeGmpResLogVO log = new GomeGmpResLogVO();
						log.setProId(newPro.getProId());
						log.setOperateType("编辑任务字段");
						// 获取值
						String val = ReflectUtil.getFieldValueByName(col, newTask);
						// 获取val的中文字典
						String valCN = gomeGmpDictItemBS.getItemName(col, val);
						if (valCN == null) {
							valCN = val;
						}
						// 获取字段的中文名
						String CName = ReflectUtil.getColCName(newTask, col);
						if (valCN == null || "null".equals(valCN)) {
							log.setContent("删除了任务项\"" + taskName + "\"的" + CName);
							rtnList.add(log);
						} else {
							// 前置任务的编辑
							if (col == "preTaskNum") {
								String newPreTaskName = newPreNumTaskMap.get(newTask.getPreTaskNum());
								if ((oldTask.getPreTaskName() != null && newPreTaskName == null) || (oldTask.getPreTaskName() == null && newPreTaskName != null) || (oldTask.getPreTaskName() != null && newPreTaskName != null && !oldTask.getPreTaskName().equals(newPreTaskName))) {
									if (StringUtils.isBlank(newPreTaskName)) {
										newPreTaskName = "任务号：" + newTask.getPreTaskNum();
									} else {
										newPreTaskName = "任务:" + newPreTaskName;
									}
									log.setContent("设置任务项\"" + taskName + "\"的前置" + newPreTaskName);
									rtnList.add(log);
								}
							} else {
								if (col == "taskName") {
									taskName = "任务号：" + newTask.getTaskNum();
								}
								log.setContent("编辑任务项\"" + taskName + "\"的" + CName + "为" + valCN);
								rtnList.add(log);
							}
						}
					}
				}
			}
		}
		if (oldTasks != null) {
			for (GomeGmpResTaskVO oldTask : oldTasks) {
				GomeGmpResLogVO log = null;
				if (!newTaskMap.containsKey(oldTask.getId())) {
					log = new GomeGmpResLogVO();
					log.setProId(oldTask.getProId());
					log.setOperateType("删除任务项");
					log.setContent("删除任务项" + (StringUtils.isBlank(oldTask.getTaskName()) ? "任务号：" + oldTask.getTaskNum() : oldTask.getTaskName()));
					rtnList.add(log);
				}
			}
		}
		return rtnList;
	}

	/**
	 * 上移下移日志
	 * 
	 * @param newPro
	 * @return
	 * @author wubin
	 */
	private List<GomeGmpResLogVO> moveLogs(GomeGmpResProjectVO newPro) {
		String changeLogs = newPro.getChangeLogs();
		@SuppressWarnings("unchecked")
		List<Map<String, String>> changeLogObj = (List<Map<String, String>>) JSON.parse(changeLogs);
		List<GomeGmpResLogVO> rtnList = new ArrayList<GomeGmpResLogVO>();
		for (Map<String, String> map : changeLogObj) {
			GomeGmpResLogVO log = new GomeGmpResLogVO();
			String type = map.get("move");
			log.setProId(newPro.getProId());
			log.setOperateType("上下移任务项");
			String taskName = map.get("taskName");
			if (StringUtils.isBlank(taskName)) {
				taskName = "任务号:" + map.get("taskNum");
			}
			if ("up".equals(type)) {
				log.setContent("上移任务项\"" + taskName + "\"");
			} else {
				log.setContent("下移任务项\"" + taskName + "\"");
			}
			rtnList.add(log);
		}
		return rtnList;
	}

	/**
	 * 项目变更记录
	 * 
	 * @param gomeGmpResProjectVO
	 * @throws Exception
	 */
	private void recordLog(GomeGmpResProjectVO newPro) {
		// 查询修改前的对象
		GomeGmpResProjectVO qryObj = new GomeGmpResProjectVO();
		qryObj.setProId(newPro.getProId());
		GomeGmpResProjectVO oldPro = findGomeGmpResProjectById(qryObj);
		// 对比基础信息
		List<GomeGmpResLogVO> baseList = baseCompare(oldPro, newPro);
		// 对比资料
		List<GomeGmpResLogVO> dataSynList = dataSynCompare(oldPro, newPro);
		// 涉及系统
		List<GomeGmpResLogVO> systemList = systemCompare(oldPro, newPro);
		// 对比角色
		List<GomeGmpResLogVO> roleList = rolesCompare(oldPro, newPro);
		// 对比排期
		List<GomeGmpResLogVO> taskList = tasksCompare(oldPro, newPro);
		// 上移下移记录
		List<GomeGmpResLogVO> moveLogs = moveLogs(newPro);
		List<GomeGmpResLogVO> rtnList = new ArrayList<GomeGmpResLogVO>();
		rtnList.addAll(baseList);
		rtnList.addAll(dataSynList);
		rtnList.addAll(systemList);
		rtnList.addAll(roleList);
		rtnList.addAll(taskList);
		rtnList.addAll(moveLogs);
		// 保存变更记录
		for (GomeGmpResLogVO log : rtnList) {
			gomeGmpResLogDAO.saveGomeGmpResLogBO(log);
		}
	}

	/**
	 * 甘特图数据
	 * 
	 * @param proId
	 * @return
	 * @author wubin
	 */
	@Override
	public List<GomeGmpResTaskVO> findGanttData(GomeGmpResTaskVO gomeGmpResTaskVO) {
		return gomeGmpResTaskDAO.findGomeGmpResTaskBO(gomeGmpResTaskVO);
	}

	/**
	 * 获取变更记录对象
	 * 
	 * @param col
	 * @param val
	 * @param colCN
	 * @param isDictCol
	 * @return
	 * @author wubin
	 */
	private GomeGmpResLogBO getLogObj(String col, String val, String colCN, boolean isDictCol) {
		// 获取val的中文字典
		String valCN = val;
		if (isDictCol) {
			valCN = gomeGmpDictItemBS.getItemName(col, val);
		}
		GomeGmpResLogBO log = new GomeGmpResLogBO();
		log.setOperateType("更新状态");
		log.setContent("更新\"" + colCN + "\"为\"" + valCN + "\"");
		log.setOperateColumn(col);
		log.setColumnVal(val);
		Long userId = BusinessUtil.getLoginUserId(CommandContext.getRequest());
		log.setCreateUser(NumberUtils.toInt(String.valueOf(userId)));
		return log;
	}

	@Override
	public int closePro(String proId, Long userTid) {
		GomeGmpResProjectVO proVo = new GomeGmpResProjectVO();
		proVo.setProId(proId);
		proVo.setStatusId(3);
		proVo.setUpdateUser(NumberUtils.toInt(String.valueOf(userTid)));
		int result = gomeGmpResProjectDAO.updateProjectPropertyById(proVo);
		if (result > 0) {
			// 记录日志
			GomeGmpResLogBO logObj = getLogObj("statusId", String.valueOf(proVo.getStatusId()), "任务状态", true);
			logObj.setProId(proVo.getProId());
			gomeGmpResLogDAO.saveGomeGmpResLogBO(logObj);
		}
		return result;
	}

	/**
	 * 获取项目或需求列表
	 */
	@Override
	public Page<GomeGmpResProjectVO> queryGomeProjectPage(GomeGmpResProjectVO projectVo, Long loginUserId) {
		Page<GomeGmpResProjectVO> projectVoPage = findProjects(projectVo);
		// 查询参数存储供导出使用
		CommandContext.getRequest().getSession().setAttribute("PROJECT_FIND_PARAM", projectVo);
		return projectVoPage;
	}

	@Override
	public GomeGmpResProjectVO loadQueryConditionData(GomeGmpResProjectVO projectVo, String operateType, GomeGmpResUserBO userBo) {
		projectVo = userQueryCondition(projectVo, userBo);
		projectVo = viewQueryCondition(projectVo, operateType, userBo);
		projectVo = defaultQueryCondition(projectVo, operateType);
		return projectVo;
	}

	/**
	 * 页面的查询条件 （页面传值）
	 * 
	 * @param projectVo
	 * @param operateType
	 * @return
	 */
	private GomeGmpResProjectVO viewQueryCondition(GomeGmpResProjectVO projectVo, String operateType, GomeGmpResUserBO userBo) {
		projectVo.setQryType("projectManageList");
		projectVo.setLoginUserId(userBo.getId());
		projectVo.setEndTime(DateUtil.getDate(DateUtil.getDate(projectVo.getEndTime()) + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		if ("add".equals(operateType)) {
			projectVo.setBpId(NumberUtils.toInt(String.valueOf(userBo.getId())));
			// 添加项目时,当前登录用户默认为项目经理
			projectVo.setBpName(userBo.getUserName());
		}else if("query".equals(operateType)){
			if(projectVo.getOrgIds()!=null && projectVo.getOrgIds().length>0 && !"null".equals(projectVo.getOrgIds()[0])){
				if(StringUtils.isNotBlank(projectVo.getListType()) && "init".equals(projectVo.getListType())){
					projectVo.setProRelatedUsers(userBo.getId().toString().split(" "));
				}else{
					List<String> relatedList = new ArrayList<String>();
					for (String orgId : projectVo.getOrgIds()) {
						//获取组织下所有成员
						String[] orgUsers = gomeGmpOrgManageBS.getOrgAllMembers(orgId);
						if(orgUsers!=null && orgUsers.length>0){
							for (String userTid : orgUsers) {
								relatedList.add(userTid);
							}
						}
					}
					projectVo.setProRelatedUsers((String[])relatedList.toArray(new String[relatedList.size()]));
				}
			}
		}
		return projectVo;
	}

	public GomeGmpResProjectVO handleQueryOrgId(GomeGmpResProjectVO projectVo) {
		String orgStr = "";
		if (projectVo.getOrgIds() != null) {
			orgStr += StringUtils.join(projectVo.getOrgIds(), ",") + ",";
		}
		if (projectVo.getChildOrgIds() != null) {
			 if(StringUtils.isNotEmpty(orgStr)){
				 String tempStr = distinctOrgId(orgStr.split(","),projectVo.getChildOrgIds());
				 orgStr = tempStr;
			 }else{
				 orgStr += StringUtils.join(projectVo.getChildOrgIds(), ",") + ",";
			 }
		}
		if (projectVo.getGroupIds() != null) {
			 if(StringUtils.isNotEmpty(orgStr)){
				 String tempStr = distinctOrgId(orgStr.split(","),projectVo.getGroupIds());
				 orgStr = tempStr;
			 }else{
				 orgStr += StringUtils.join(projectVo.getGroupIds(), ",") + ",";
			 }
		}
		if (StringUtils.isNotEmpty(orgStr)) {
			projectVo.setOrgIds(orgStr.split(","));
		}
		return projectVo;
	}
	
	/**
	 * 数组中去除重复有包含的组织
	 * @param prevOrgIds
	 * @param thisOrgIds
	 * @return
	 */
	private String distinctOrgId(String[] prevOrgIds, String[] thisOrgIds) {
		String resultStr = "";
		Map<String, String> tempMap = new HashMap<String, String>();
		for (int i = 0; i < prevOrgIds.length; i++) {
			String tempStr = "";
			for (int j = 0; j < thisOrgIds.length; j++) {
				String prevOrgStr = prevOrgIds[i];
				String thisOrgStr = thisOrgIds[j];
				if(prevOrgStr.equals(StringUtils.left(thisOrgStr, prevOrgStr.length()))){
					if(StringUtils.isBlank(tempMap.get(prevOrgStr))){
						tempStr += thisOrgStr + ",";
					}else{
						if(tempMap.get(prevOrgStr).split(",")[0].equals(prevOrgStr) && thisOrgStr.indexOf(tempMap.get(prevOrgStr))==-1){
							tempMap.remove(prevOrgStr);
							tempStr += thisOrgStr + ",";
						}else{
							tempStr = tempMap.get(prevOrgStr) + thisOrgStr + ",";
						}
					}
					tempMap.put(prevOrgStr, tempStr);
				}else{
					if(StringUtils.isBlank(tempMap.get(prevOrgStr))){
						tempMap.put(prevOrgStr, prevOrgStr+",");
					}
				}
			}
		}
		for (Map.Entry<String, String> entry : tempMap.entrySet()) {  
			resultStr += entry.getValue();
		}
		return resultStr;
	}

	/**
	 * 根据当前登录用户的查询条件
	 * 
	 * @param projectVo
	 * @param userBo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public GomeGmpResProjectVO userQueryCondition(GomeGmpResProjectVO projectVo, GomeGmpResUserBO userBo) {
		Map<String, Object> userOrgInfoMap = gomeGmpOrgManageBS.getUserOrgInfo(userBo);
		Integer loginUserOrgLevel = (Integer) userOrgInfoMap.get("userOrgLevel");
		List<GomeGmpResOrgVO> userOrgList = (List<GomeGmpResOrgVO>) userOrgInfoMap.get("userOrgList");
		if (loginUserOrgLevel != null) {// 用户有组织
			projectVo.setLoginUserOrgLevel(loginUserOrgLevel);
			if (projectVo.getOrgIds() == null) {
				projectVo.setOrgIds((String[]) userOrgInfoMap.get("orgIds"));
			} else {
				projectVo = handleQueryOrgId(projectVo);
			}
			Boolean isMember = (Boolean) userOrgInfoMap.get("isMember");
			if (isMember != null && isMember ) {// 是组员
				if(projectVo.getIsMember()==null){//默认列表
					projectVo.setIsMember(isMember);
					projectVo.setBpId(NumberUtils.toInt(String.valueOf(userBo.getId())));
				}
			}
		} else {// 无组织
			if (userOrgList == null) {// 无组织用户
				projectVo.setOrgIds("null".split(" "));
			} else {// 管理员
				if(projectVo.getOrgIds() != null){
					projectVo = handleQueryOrgId(projectVo);
				}
			}
		}
		projectVo.setUserCenterOrgList(userOrgList);
		List<GomeGmpResOrgBO> orgBoList = (List<GomeGmpResOrgBO>) userOrgInfoMap.get("orgBoList");
		projectVo.setUserOrgBoList(orgBoList);
		return projectVo;
	}

	/**
	 * 默认的查询条件（下拉列表）
	 * 
	 * @param projectVo
	 * @return
	 */
	private GomeGmpResProjectVO defaultQueryCondition(GomeGmpResProjectVO projectVo, String operateType) {
		projectVo.setResUnitV(gomeGmpOrgManageBS.getOrgListByLevel(Constants.ORGLEVEL_FIRST));
		projectVo.setTaskStatusV(gomeGmpDictItemBS.getDictItem("statusId"));
		projectVo.setSystemsV(gomeGmpDictItemBS.getDictItem("system"));
		projectVo.setPriorityIdsV(gomeGmpDictItemBS.getDictItem("priorityId"));
		projectVo.setScheduleIdsV(gomeGmpDictItemBS.getDictItem("scheduleId"));
		projectVo.setActualizesV(gomeGmpDictItemBS.getDictItem("actualize"));
		// 查询时添加项目经理的筛选
		if("query".equals(operateType)) {
			List<GomeGmpResUserBO> bps = gomeGmpResProjectDAO.getProRelatedBps(projectVo);
			projectVo.setBpIdsV(bps);
		}
		return projectVo;
	}
	
	/**
	 * 根据当前用户获取关联项目
	 */
	@Override
	public List<GomeGmpResProjectBO> getDailyRelateProjectsByUser(Long userId) {
		return gomeGmpResProjectDAO.getDailyRelateProjectsByUser(userId);
	}
	
}
