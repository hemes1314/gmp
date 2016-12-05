package com.gome.gmp.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gome.gmp.model.vo.GomeGmpResProjectVO;

@Repository("gomeGmpResProMonitorDAO")
public interface GomeGmpResProMonitorDAO {

	// 项目统计
	public List<Map<String, Object>> findPro(GomeGmpResProjectVO gmpResProjectVO);

	// 项目近期上线项统计
	public List<Map<String, Object>> nearOnline(GomeGmpResProjectVO gmpResProjectVO);

	// 项目风险项统计
	public List<Map<String, Object>> riskPro(GomeGmpResProjectVO gmpResProjectVO);

	// 项目延期项统计
	public List<Map<String, Object>> delayPro(GomeGmpResProjectVO gmpResProjectVO);

	// 项目全部项统计
	public List<Map<String, Object>> allPro(GomeGmpResProjectVO gmpResProjectVO);

	// 项目即将上线项统计
	public List<Map<String, Object>> soonOnline(GomeGmpResProjectVO gmpResProjectVO);

	// 项目新增项统计
	public List<Map<String, Object>> newlyPro(GomeGmpResProjectVO gmpResProjectVO);

	// 有数据的年
	public List<String> toYear(GomeGmpResProjectVO gomeGmpResProjectVO);
}
