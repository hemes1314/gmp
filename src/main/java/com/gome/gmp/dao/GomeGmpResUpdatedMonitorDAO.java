package com.gome.gmp.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.gome.gmp.model.vo.GomeGmpResProjectVO;

/**
 * @author Administrator
 */
@Repository("GomeGmpResUpdatedMonitorDAO")
public interface GomeGmpResUpdatedMonitorDAO {
	//取得更新监控数据
	List<Map<String, Object>> getGomeGmpResUpdatedCountBO(GomeGmpResProjectVO gomeGmpResProjectVO);
	Page<Map<String, Object>> getUpdatedProjectsById(GomeGmpResProjectVO gomeGmpResProjectVO);
	Page<Map<String, Object>> getNoUpdatedProjectsById(GomeGmpResProjectVO gomeGmpResProjectVO);
	Page<Map<String, Object>> getPauseProjectsById(GomeGmpResProjectVO gomeGmpResProjectVO);
	Page<Map<String, Object>> getCloseProjectsById(GomeGmpResProjectVO gomeGmpResProjectVO);
}