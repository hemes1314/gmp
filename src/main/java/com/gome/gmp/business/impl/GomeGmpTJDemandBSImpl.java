package com.gome.gmp.business.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.gome.gmp.business.GomeGmpTJDemandBS;
import com.gome.gmp.common.BusinessUtil;
import com.gome.gmp.common.util.DateUtil;
import com.gome.gmp.dao.GomeGmpResNeedDAO;
import com.gome.gmp.dao.GomeGmpResProjectDAO;
import com.gome.gmp.dao.GomeGmpTJDemandDAO;
import com.gome.gmp.model.vo.GomeGmpResNeedVO;
import com.gome.gmp.model.vo.GomeGmpTJDemandVO;

@Service
public class GomeGmpTJDemandBSImpl implements GomeGmpTJDemandBS {

	@Resource(name = "gomeGmpResNeedDAO")
	private GomeGmpResNeedDAO gomeGmpResNeedDAO;

	@Resource(name = "gomeGmpTJDemandDAO")
	private GomeGmpTJDemandDAO gomeGmpTJDemandDAO;

	@Resource(name = "gomeGmpResProjectDAO")
	private GomeGmpResProjectDAO gomeGmpResProjectDAO;

	/**
	 * 提报需求统计
	 */
	@Override
	public List<Map<String, Object>> tjDemand(GomeGmpResNeedVO gomeGmpResNeedVO) {

		return gomeGmpTJDemandDAO.tjDemand(gomeGmpResNeedVO);
	}

	/**
	 * 提报人统计
	 */
	@Override
	public List<Map<String, Object>> tjPeopleInfo(GomeGmpResNeedVO gomeGmpResNeedVO) {
		return gomeGmpTJDemandDAO.tjPeopleInfo(gomeGmpResNeedVO);
	}

	/**
	 * 提报需求列表统计
	 */
	@Override
	public Page<GomeGmpResNeedVO> tjPeopleList(GomeGmpResNeedVO gomeGmpResNeedVO) {
		return gomeGmpTJDemandDAO.tjPeopleList(gomeGmpResNeedVO);
	}

	/**
	 * 总数据统计
	 **/
	// 年度项目统计
	@Override
	public List<Map<String, Object>> proTJ(GomeGmpTJDemandVO gomeGmpTJDemandVO) {
		return gomeGmpTJDemandDAO.proTJ(gomeGmpTJDemandVO);
	}

	// 年度项目统计
	@Override
	public Page<GomeGmpTJDemandVO> proTJNum(GomeGmpTJDemandVO gmpResProjectVO) {
		return gomeGmpTJDemandDAO.proTJNum(gmpResProjectVO);
	}

	// 年度进行中项目统计
	@Override
	public Page<GomeGmpTJDemandVO> jxzTJNum(GomeGmpTJDemandVO projectVO) {
		return gomeGmpTJDemandDAO.jxzTJNum(projectVO);
	}

	// 项目已上线，风险，延期，暂停，未启动
	@Override
	public Page<GomeGmpTJDemandVO> ztTJNum(GomeGmpTJDemandVO projectVO) {
		return gomeGmpTJDemandDAO.ztTJNum(projectVO);
	}

	// 本周项目统计
	@Override
	public List<Map<String, Object>> bzProTJ(GomeGmpTJDemandVO gmpResProjectVO) {
		Date now = new Date();
		Date preWeekNow = DateUtil.addDays(now, -7);
		// 上周的今天
		gmpResProjectVO.setStartTime(preWeekNow);
		gmpResProjectVO.setStartTime(DateUtil.getDate(DateUtil.getDate(gmpResProjectVO.getStartTime()) + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		// 当天
		gmpResProjectVO.setEndTime(now);
		gmpResProjectVO.setEndTime(DateUtil.getDate(DateUtil.getDate(gmpResProjectVO.getEndTime()) + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		return gomeGmpTJDemandDAO.bzProTJ(gmpResProjectVO);
	}

	// 本周已上线，暂停项统计
	@Override
	public Page<GomeGmpTJDemandVO> bzProTJNum(GomeGmpTJDemandVO projectVO) {
		Date now = new Date();
		Date preWeekNow = DateUtil.addDays(now, -7);
		// 上周的今天
		projectVO.setStartTime(preWeekNow);
		projectVO.setStartTime(DateUtil.getDate(DateUtil.getDate(projectVO.getStartTime()) + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		// 当天
		projectVO.setEndTime(now);
		projectVO.setEndTime(DateUtil.getDate(DateUtil.getDate(projectVO.getEndTime()) + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		return gomeGmpTJDemandDAO.bzProTJNum(projectVO);
	}

	// 本周进行中项统计
	@Override
	public Page<GomeGmpTJDemandVO> bzjxzTJNum(GomeGmpTJDemandVO gomeGmpTJDemandVO) {
		Date now = new Date();
		Date preWeekNow = DateUtil.addDays(now, -7);
		// 上周的今天
		gomeGmpTJDemandVO.setStartTime(preWeekNow);
		gomeGmpTJDemandVO.setStartTime(DateUtil.getDate(DateUtil.getDate(gomeGmpTJDemandVO.getStartTime()) + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		
		// 当天
		gomeGmpTJDemandVO.setEndTime(now);
		gomeGmpTJDemandVO.setEndTime(DateUtil.getDate(DateUtil.getDate(gomeGmpTJDemandVO.getEndTime()) + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		
		return gomeGmpTJDemandDAO.bzjxzTJNum(gomeGmpTJDemandVO);
	}

	// 本周新增项统计
	@Override
	public Page<GomeGmpTJDemandVO> bzXzTJNum(GomeGmpTJDemandVO gomeGmpTJDemandVO) {
		Date now = new Date();
		Date preWeekNow = DateUtil.addDays(now, -7);
		// 上周的今天
		gomeGmpTJDemandVO.setStartTime(preWeekNow);
		gomeGmpTJDemandVO.setStartTime(DateUtil.getDate(DateUtil.getDate(gomeGmpTJDemandVO.getStartTime()) + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		
		// 当天
		gomeGmpTJDemandVO.setEndTime(now);
		gomeGmpTJDemandVO.setEndTime(DateUtil.getDate(DateUtil.getDate(gomeGmpTJDemandVO.getEndTime()) + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		
		return gomeGmpTJDemandDAO.bzXzTJNum(gomeGmpTJDemandVO);
	}

	// 本周风险项统计
	@Override
	public Page<GomeGmpTJDemandVO> bzFxTJNum(GomeGmpTJDemandVO gomeGmpTJDemandVO) {
		Date now = new Date();
		Date preWeekNow = DateUtil.addDays(now, -7);
		// 上周的今天
		gomeGmpTJDemandVO.setStartTime(preWeekNow);
		gomeGmpTJDemandVO.setStartTime(DateUtil.getDate(DateUtil.getDate(gomeGmpTJDemandVO.getStartTime()) + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		
		// 当天
		gomeGmpTJDemandVO.setEndTime(now);
		gomeGmpTJDemandVO.setEndTime(DateUtil.getDate(DateUtil.getDate(gomeGmpTJDemandVO.getEndTime()) + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		
		return gomeGmpTJDemandDAO.bzFxTJNum(gomeGmpTJDemandVO);
	}

	// 下周计划上线项统计
	@Override
	public Page<GomeGmpTJDemandVO> xzJhsxTJNum(GomeGmpTJDemandVO gomeGmpTJDemandVO) {
		Date dates = DateUtil.addDays(new Date(), 7);
		String sta = DateUtil.getDateTime(dates);
		Date[] weekFL = DateUtil.getWeekFirstAndLast(sta);
		//下周一
		gomeGmpTJDemandVO.setStartTime(weekFL[0]);
		gomeGmpTJDemandVO.setStartTime(DateUtil.getDate(DateUtil.getDate(gomeGmpTJDemandVO.getStartTime()) + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));	
		//下周日
		gomeGmpTJDemandVO.setEndTime(weekFL[1]);
		gomeGmpTJDemandVO.setEndTime(DateUtil.getDate(DateUtil.getDate(gomeGmpTJDemandVO.getEndTime()) + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		return gomeGmpTJDemandDAO.xzJhsxTJNum(gomeGmpTJDemandVO);
	}

	// 项目状态分布
	@Override
	public List<Map<String, Object>> ztProTJ(GomeGmpTJDemandVO gmpResProjectVO) {
		return gomeGmpTJDemandDAO.ztProTJ(gmpResProjectVO);
	}

	// 部门数据统计
	@Override
	public List<Map<String, Object>> unitProTJ(GomeGmpTJDemandVO gmpResProjectVO) {
		gmpResProjectVO.setStartTime(DateUtil.getDate(DateUtil.getDate(gmpResProjectVO.getStartTime()) + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		gmpResProjectVO.setEndTime(DateUtil.getDate(DateUtil.getDate(gmpResProjectVO.getEndTime()) + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		return gomeGmpTJDemandDAO.unitProTJ(gmpResProjectVO);
	}

	// 上线分布统计
	@Override
	public List<Map<String, Object>> onlineTJ(GomeGmpTJDemandVO gmpResProjectVO) {
		BusinessUtil.repeatOrg(gmpResProjectVO);
		gmpResProjectVO.setEndTime(DateUtil.getDate(DateUtil.getDate(gmpResProjectVO.getEndTime()) + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		return gomeGmpTJDemandDAO.onlineTJ(gmpResProjectVO);
	}

	// 各部门数据统
	@Override
	public List<Map<String, Object>> unitTJ(GomeGmpTJDemandVO gmpResProjectVO) {
		gmpResProjectVO.setStartTime(DateUtil.getDate(DateUtil.getDate(gmpResProjectVO.getStartTime()) + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		gmpResProjectVO.setEndTime(DateUtil.getDate(DateUtil.getDate(gmpResProjectVO.getEndTime()) + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		return gomeGmpTJDemandDAO.unitTJ(gmpResProjectVO);
	}

	// 未启动、正常、提前、延期风险、已延期、待上线、已取消、内部上线、暂停、暂缓、待排期、部分上线项统计
	@Override
	public Page<GomeGmpTJDemandVO> bzZtTJNum(GomeGmpTJDemandVO gomeGmpTJDemandVO) {
		return gomeGmpTJDemandDAO.bzZtTJNum(gomeGmpTJDemandVO);
	}

	// 部门进行中项
	@Override
	public Page<GomeGmpTJDemandVO> unitJxzTJNum(GomeGmpTJDemandVO gomeGmpTJDemandVO) {
		gomeGmpTJDemandVO.setStartTime(DateUtil.getDate(DateUtil.getDate(gomeGmpTJDemandVO.getStartTime()) + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		gomeGmpTJDemandVO.setEndTime(DateUtil.getDate(DateUtil.getDate(gomeGmpTJDemandVO.getEndTime()) + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		return gomeGmpTJDemandDAO.unitJxzTJNum(gomeGmpTJDemandVO);
	}

	// 部门已上线项
	@Override
	public Page<GomeGmpTJDemandVO> unitYsxTJNum(GomeGmpTJDemandVO gomeGmpTJDemandVO) {
		gomeGmpTJDemandVO.setStartTime(DateUtil.getDate(DateUtil.getDate(gomeGmpTJDemandVO.getStartTime()) + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		gomeGmpTJDemandVO.setEndTime(DateUtil.getDate(DateUtil.getDate(gomeGmpTJDemandVO.getEndTime()) + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		return gomeGmpTJDemandDAO.unitYsxTJNum(gomeGmpTJDemandVO);
	}

	// 上线分布项统计
	@Override
	public Page<GomeGmpTJDemandVO> onlineTJNum(GomeGmpTJDemandVO gomeGmpTJDemandVO) {
		BusinessUtil.repeatOrg(gomeGmpTJDemandVO);
		return gomeGmpTJDemandDAO.onlineTJNum(gomeGmpTJDemandVO);
	}

	// 部门统计项
	@Override
	public Page<GomeGmpTJDemandVO> unitTJNum(GomeGmpTJDemandVO gomeGmpTJDemandVO) {
		gomeGmpTJDemandVO.setStartTime(DateUtil.getDate(DateUtil.getDate(gomeGmpTJDemandVO.getStartTime()) + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		gomeGmpTJDemandVO.setEndTime(DateUtil.getDate(DateUtil.getDate(gomeGmpTJDemandVO.getEndTime()) + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		BusinessUtil.repeatOrg(gomeGmpTJDemandVO);
		return gomeGmpTJDemandDAO.unitTJNum(gomeGmpTJDemandVO);
	}

	// 上线分布有数据的年份
	@Override
	public List<String> onlineYear(GomeGmpTJDemandVO gomeGmpTJDemandVO) {
		return gomeGmpTJDemandDAO.onlineYear(gomeGmpTJDemandVO);
	}

	// 上线分布导出查询
	@Override
	public List<Map<String, Object>> onlineTJExport(GomeGmpTJDemandVO gomeGmpTJDemandVO) {
		return gomeGmpTJDemandDAO.onlineTJExport(gomeGmpTJDemandVO);
	}

	// 查询项目统计有数据的年
	@Override
	public List<String> toProTJ(GomeGmpTJDemandVO gomeGmpTJDemandVO) {
		return gomeGmpTJDemandDAO.toProTJ(gomeGmpTJDemandVO);
	}

	// 部门风险，延期项
	@Override
	public Page<GomeGmpTJDemandVO> unitztTJNum(GomeGmpTJDemandVO gomeGmpTJDemandVO) {
		gomeGmpTJDemandVO.setEndTime(DateUtil.getDate(DateUtil.getDate(gomeGmpTJDemandVO.getEndTime()) + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		return gomeGmpTJDemandDAO.unitztTJNum(gomeGmpTJDemandVO);
	}
}
