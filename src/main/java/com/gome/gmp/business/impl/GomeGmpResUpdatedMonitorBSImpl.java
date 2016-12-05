package com.gome.gmp.business.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.gome.gmp.business.GomeGmpResUpdatedMonitorBS;
import com.gome.gmp.common.util.DateUtil;
import com.gome.gmp.dao.GomeGmpResUpdatedMonitorDAO;
import com.gome.gmp.model.vo.GomeGmpResProjectVO;

@Service
public class GomeGmpResUpdatedMonitorBSImpl implements GomeGmpResUpdatedMonitorBS {

	@Resource
	private GomeGmpResUpdatedMonitorDAO gomeGmpResUpdatedMonitorDAO;

	// 取得更新监控数据
	@Override
	public List<Map<String, Object>> getGomeGmpResUpdatedCountBO(GomeGmpResProjectVO gomeGmpResProjectVO) {
		gomeGmpResProjectVO.setStartTime(DateUtil.getDate(gomeGmpResProjectVO.getStartDate() + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		gomeGmpResProjectVO.setEndTime(DateUtil.getDate(gomeGmpResProjectVO.getEndDate() + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		return gomeGmpResUpdatedMonitorDAO.getGomeGmpResUpdatedCountBO(gomeGmpResProjectVO);
	}

	@Override
	public Page<Map<String, Object>> getUpdatedProjectsById(GomeGmpResProjectVO gomeGmpResProjectVO) {
		gomeGmpResProjectVO.setStartTime(DateUtil.getDate(gomeGmpResProjectVO.getStartDate() + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		gomeGmpResProjectVO.setEndTime(DateUtil.getDate(gomeGmpResProjectVO.getEndDate() + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		Page<Map<String, Object>> updatedList = gomeGmpResUpdatedMonitorDAO.getUpdatedProjectsById(gomeGmpResProjectVO);
		Page<Map<String, Object>> closeList = gomeGmpResUpdatedMonitorDAO.getCloseProjectsById(gomeGmpResProjectVO);
		updatedList.addAll(closeList);
		return updatedList;
	}

	@Override
	public Page<Map<String, Object>> getNoUpdatedProjectsById(GomeGmpResProjectVO gomeGmpResProjectVO) {
		gomeGmpResProjectVO.setStartTime(DateUtil.getDate(gomeGmpResProjectVO.getStartDate() + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		gomeGmpResProjectVO.setEndTime(DateUtil.getDate(gomeGmpResProjectVO.getEndDate() + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		return gomeGmpResUpdatedMonitorDAO.getNoUpdatedProjectsById(gomeGmpResProjectVO);
	}

	@Override
	public Page<Map<String, Object>> getPauseProjectsById(GomeGmpResProjectVO gomeGmpResProjectVO) {
		gomeGmpResProjectVO.setStartTime(DateUtil.getDate(gomeGmpResProjectVO.getStartDate() + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		gomeGmpResProjectVO.setEndTime(DateUtil.getDate(gomeGmpResProjectVO.getEndDate() + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		return gomeGmpResUpdatedMonitorDAO.getPauseProjectsById(gomeGmpResProjectVO);
	}

	@Override
	public Page<Map<String, Object>> getCloseProjectsById(GomeGmpResProjectVO gomeGmpResProjectVO) {
		gomeGmpResProjectVO.setStartTime(DateUtil.getDate(gomeGmpResProjectVO.getStartDate() + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		gomeGmpResProjectVO.setEndTime(DateUtil.getDate(gomeGmpResProjectVO.getEndDate() + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		return gomeGmpResUpdatedMonitorDAO.getCloseProjectsById(gomeGmpResProjectVO);
	}
}
