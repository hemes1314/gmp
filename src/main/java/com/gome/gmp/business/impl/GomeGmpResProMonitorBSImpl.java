package com.gome.gmp.business.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.gome.gmp.business.GomeGmpDictItemBS;
import com.gome.gmp.business.GomeGmpOrgManageBS;
import com.gome.gmp.business.GomeGmpResProMonitorBS;
import com.gome.gmp.common.BusinessUtil;
import com.gome.gmp.common.constant.Constants;
import com.gome.gmp.dao.GomeGmpResProMonitorDAO;
import com.gome.gmp.dao.GomeGmpResProjectDAO;
import com.gome.gmp.model.vo.GomeGmpResOrgVO;
import com.gome.gmp.model.vo.GomeGmpResProjectVO;

@Service
public class GomeGmpResProMonitorBSImpl implements GomeGmpResProMonitorBS {

	@Resource
	private GomeGmpResProMonitorDAO gomeGmpResProMonitorDAO;

	@Resource
	private GomeGmpResProjectDAO gomeGmpResProjectDAO;

	@Resource
	private GomeGmpDictItemBS gomeGmpDictItemBS;
	
	@Resource
	private GomeGmpOrgManageBS gomeGmpOrgManageBS;

	/**
	 * 项目统计
	 ***/
	@Override
	public List<Map<String, Object>> findPro(GomeGmpResProjectVO gmpResProjectVO) {
		BusinessUtil.repeatOrg(gmpResProjectVO);
		return gomeGmpResProMonitorDAO.findPro(gmpResProjectVO);
	}

	/**
	 * 项目全部，近期上线，即将上线，风险项目，延期项目，新增项目
	 **/
	@Override
	public List<Map<String, Object>> monitorData(GomeGmpResProjectVO gmpResProjectVO, String monitorType) {
		List<Map<String, Object>> gomeGmpResProjectBOs = null;
		BusinessUtil.repeatOrg(gmpResProjectVO);
		if ("allPro".equals(monitorType)) {
			gomeGmpResProjectBOs = gomeGmpResProMonitorDAO.allPro(gmpResProjectVO);
		}
		if ("nearOnline".equals(monitorType)) {
			gomeGmpResProjectBOs = gomeGmpResProMonitorDAO.nearOnline(gmpResProjectVO);
		}
		if ("soonOnline".equals(monitorType)) {
			gomeGmpResProjectBOs = gomeGmpResProMonitorDAO.soonOnline(gmpResProjectVO);
		}
		if ("riskPro".equals(monitorType)) {
			gomeGmpResProjectBOs = gomeGmpResProMonitorDAO.riskPro(gmpResProjectVO);
		}
		if ("delayPro".equals(monitorType)) {
			gomeGmpResProjectBOs = gomeGmpResProMonitorDAO.delayPro(gmpResProjectVO);
		}
		if ("newlyPro".equals(monitorType)) {
			gomeGmpResProjectBOs = gomeGmpResProMonitorDAO.newlyPro(gmpResProjectVO);
		}
		return gomeGmpResProjectBOs;
	}

	/**
	 * 有数据的年
	 **/
	@Override
	public List<String> toYear(GomeGmpResProjectVO gomeGmpResProjectVO) {
		return gomeGmpResProMonitorDAO.toYear(gomeGmpResProjectVO);
	}
	
	/**
	 * 加载查询项目监控条件
	 */
	@Override
	public GomeGmpResProjectVO loadQueryMonitorDetailConditions(GomeGmpResProjectVO gomeGmpResProjectVO) {
		gomeGmpResProjectVO.setPriorityIdsV(gomeGmpDictItemBS.getDictItem("priorityId"));
		gomeGmpResProjectVO.setScheduleIdsV(gomeGmpDictItemBS.getDictItem("scheduleId"));
		gomeGmpResProjectVO.setActualizesV(gomeGmpDictItemBS.getDictItem("actualize"));
		gomeGmpResProjectVO.setTaskStatusV(gomeGmpDictItemBS.getDictItem("statusId"));
		return gomeGmpResProjectVO;
	}

	@Override
	public String getExportFileName(GomeGmpResProjectVO projectVO, String monitorType) {
		String resultStr = "";
		if(StringUtils.isNoneBlank(projectVO.getYear())){
			resultStr += projectVO.getYear()+"年度";
		}
		if(projectVO.getOrgIds()!=null && projectVO.getOrgIds().length>0){
			List<GomeGmpResOrgVO> orgVoList = gomeGmpOrgManageBS.getOrgListByLevel(Constants.ORGLEVEL_SECOND);
			for (GomeGmpResOrgVO gomeGmpResOrgVO : orgVoList) {
				for (String orgId : projectVO.getOrgIds()) {
					if(orgId.equals(gomeGmpResOrgVO.getOrgId()) && resultStr.indexOf(gomeGmpResOrgVO.getOrgName())==-1){
						resultStr += gomeGmpResOrgVO.getOrgName()+",";
					}
				}
			}
			resultStr = resultStr.substring(0, resultStr.lastIndexOf(","));
		}
		if(StringUtils.isNoneBlank(monitorType)){
			resultStr += "'";
			if("allPro".equals(monitorType)){
				resultStr += "全部";
			}else if("nearOnline".equals(monitorType)){
				resultStr += "近期上线";
			}else if("soonOnline".equals(monitorType)){
				resultStr += "即将上线";
			}else if("riskPro".equals(monitorType)){
				resultStr += "风险";
			}else if("delayPro".equals(monitorType)){
				resultStr += "已延期";
			}else if("newlyPro".equals(monitorType)){
				resultStr += "新增";
			}
			if(projectVO.getProType()!=null && projectVO.getProType()==1){
				resultStr += "项目";
			}else if(projectVO.getProType()!=null && projectVO.getProType()==2){
				resultStr += "敏捷需求";
			}
			resultStr += "'";
			resultStr += "监控统计明细表";
		}else{
			resultStr += "项目监控统计表";
		}
		return resultStr;
	}
}
