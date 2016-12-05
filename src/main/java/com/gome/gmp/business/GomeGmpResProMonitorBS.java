package com.gome.gmp.business;

import java.util.List;
import java.util.Map;

import com.gome.gmp.model.vo.GomeGmpResProjectVO;

public interface GomeGmpResProMonitorBS {

	// 查询项目统计
	public List<Map<String, Object>> findPro(GomeGmpResProjectVO gmpResProjectVO);

	// 项目全部，近期上线，即将上线，风险项目，延期项目，新增项目
	public List<Map<String, Object>> monitorData(GomeGmpResProjectVO gmpResProjectVO, String monitorType);

	// 有数据的年
	public List<String> toYear(GomeGmpResProjectVO gomeGmpResProjectVO);

	/**
	 * 加载查询项目监控条件
	 * @param gomeGmpResProjectVO
	 * @return
	 */
	public GomeGmpResProjectVO loadQueryMonitorDetailConditions(GomeGmpResProjectVO gomeGmpResProjectVO);
	
	/**
	 * 获取导出文件名称
	 * @param projectVO
	 * @param monitorType
	 * @return
	 */
	public String getExportFileName(GomeGmpResProjectVO projectVO, String monitorType);
}
