package com.gome.gmp.business;


import java.util.List;

import com.gome.gmp.model.vo.GomeGmpResOrgVO;
import com.gome.gmp.model.vo.GomeGmpTJCountVO;

public interface GomeGmpResHoursCountBS {
	
	/**
	 * 部门工时统计
	 * 
	 * @param gomeGmpTJCountVO
	 * @return
	 * @author wubin
	 */
	List<GomeGmpResOrgVO> findGomeGmpResOrgHoursCountBOById(GomeGmpTJCountVO gomeGmpTJCountVO);
	
	/**
	 * 人员工时统计
	 * 
	 * @param gomeGmpTJCountVO
	 * @return
	 * @author wubin
	 */
	List<GomeGmpResOrgVO> findGomeGmpResUserHoursCountBOById(GomeGmpTJCountVO gomeGmpTJCountVO);
}
