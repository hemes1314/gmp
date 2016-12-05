package com.gome.gmp.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.gome.gmp.model.vo.GomeGmpResNeedVO;
import com.gome.gmp.model.vo.GomeGmpTJDemandVO;

@Repository("gomeGmpTJDemandDAO")
public interface GomeGmpTJDemandDAO {

	/**
	 * 总数据统计
	 **/
	// 提报需求统计
	List<Map<String, Object>> tjDemand(GomeGmpResNeedVO gomeGmpResNeedVO);

	// 提报人统计
	List<Map<String, Object>> tjPeopleInfo(GomeGmpResNeedVO gomeGmpResNeedVO);

	// 提报需求列表
	Page<GomeGmpResNeedVO> tjPeopleList(GomeGmpResNeedVO gomeGmpResNeedVOr);

	// 年度项目统计
	List<Map<String, Object>> proTJ(GomeGmpTJDemandVO gomeGmpTJDemandVO);

	// 项目统计项
	Page<GomeGmpTJDemandVO> proTJNum(GomeGmpTJDemandVO gomeGmpTJDemandVO);

	// 项目进行中统计项
	Page<GomeGmpTJDemandVO> jxzTJNum(GomeGmpTJDemandVO gomeGmpTJDemandVO);

	// 项目已上线，风险，延期，暂停，未启动
	Page<GomeGmpTJDemandVO> ztTJNum(GomeGmpTJDemandVO gomeGmpTJDemandVO);

	// 本周项目统计
	List<Map<String, Object>> bzProTJ(GomeGmpTJDemandVO gomeGmpTJDemandVO);

	// 本周已上线，暂停项统计
	Page<GomeGmpTJDemandVO> bzProTJNum(GomeGmpTJDemandVO gomeGmpTJDemandVO);

	// 本周进行中项统计
	Page<GomeGmpTJDemandVO> bzjxzTJNum(GomeGmpTJDemandVO gomeGmpTJDemandVO);

	// 本周新增项统计
	Page<GomeGmpTJDemandVO> bzXzTJNum(GomeGmpTJDemandVO gomeGmpTJDemandVO);

	// 本周风险项统计
	Page<GomeGmpTJDemandVO> bzFxTJNum(GomeGmpTJDemandVO gomeGmpTJDemandVO);

	// 下周计划上线项统计
	Page<GomeGmpTJDemandVO> xzJhsxTJNum(GomeGmpTJDemandVO gomeGmpTJDemandVO);

	// 项目状态分布
	List<Map<String, Object>> ztProTJ(GomeGmpTJDemandVO gomeGmpTJDemandVO);

	// 未启动、正常、提前、延期风险、已延期、待上线、已取消、内部上线、暂停、暂缓、待排期、部分上线项统计
	Page<GomeGmpTJDemandVO> bzZtTJNum(GomeGmpTJDemandVO gomeGmpTJDemandVO);

	// 部门数据统计
	List<Map<String, Object>> unitProTJ(GomeGmpTJDemandVO gomeGmpTJDemandVO);

	// 部门进行中项
	Page<GomeGmpTJDemandVO> unitJxzTJNum(GomeGmpTJDemandVO gomeGmpTJDemandVO);

	// 部门已上线项
	Page<GomeGmpTJDemandVO> unitYsxTJNum(GomeGmpTJDemandVO gomeGmpTJDemandVO);

	// 部门风险，延期项
	Page<GomeGmpTJDemandVO> unitztTJNum(GomeGmpTJDemandVO gomeGmpTJDemandVO);

	// 上线分布统计
	List<Map<String, Object>> onlineTJ(GomeGmpTJDemandVO gomeGmpTJDemandVO);

	// 上线分布项统计
	Page<GomeGmpTJDemandVO> onlineTJNum(GomeGmpTJDemandVO gomeGmpTJDemandVO);

	// 各部门数据统
	List<Map<String, Object>> unitTJ(GomeGmpTJDemandVO gomeGmpTJDemandVO);

	// 部门统计项
	Page<GomeGmpTJDemandVO> unitTJNum(GomeGmpTJDemandVO gomeGmpTJDemandVO);

	// 上线分布有数据的年份
	List<String> onlineYear(GomeGmpTJDemandVO gomeGmpTJDemandVO);

	// 上线分布导出查询
	List<Map<String, Object>> onlineTJExport(GomeGmpTJDemandVO gomeGmpTJDemandVO);

	// 查询项目统计有数据的年
	List<String> toProTJ(GomeGmpTJDemandVO gomeGmpTJDemandVO);
}
