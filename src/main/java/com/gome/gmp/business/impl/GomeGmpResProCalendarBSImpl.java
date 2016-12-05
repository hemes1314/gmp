package com.gome.gmp.business.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gome.framework.logging.Logger;
import com.gome.framework.logging.LoggerFactory;
import com.gome.gmp.business.GomeGmpOrgManageBS;
import com.gome.gmp.business.GomeGmpResProCalendarBS;
import com.gome.gmp.business.GomeGmpResProjectBS;
import com.gome.gmp.dao.GomeGmpResProCalendarDAO;
import com.gome.gmp.model.bo.GomeGmpResProjectBO;
import com.gome.gmp.model.bo.GomeGmpResUserBO;
import com.gome.gmp.model.vo.GomeGmpResProjectVO;
import com.gome.gmp.model.vo.GomeGmpResTaskVO;
import com.gome.gmp.model.vo.GomeGmpTJCalendarVO;

/**
 * 项目业务逻辑层
 * 
 * @author wubin
 */
@Service("gomeGmpResProCalendarBS")
public class GomeGmpResProCalendarBSImpl extends BaseBS implements GomeGmpResProCalendarBS {

	protected Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "gomeGmpResProCalendarDAO")
	private GomeGmpResProCalendarDAO gomeGmpResProCalendarDAO;
	
	@Resource(name = "gomeGmpResProjectBS")
	private GomeGmpResProjectBS gomeGmpResProjectBS;
	
	@Resource
	private GomeGmpOrgManageBS gomeGmpOrgManageBS;

	/**
	 * 查询项目列表
	 * 
	 * @author be_kangfengping
	 * @return list
	 */
	@Override
	public List<GomeGmpResProjectBO> findGomeGmpResProjectBOList() {
		return gomeGmpResProCalendarDAO.findGomeGmpResProjectBOList();
	}

	@Override
	public List<GomeGmpTJCalendarVO> findGomeGmpResProjectBOListByDate(GomeGmpTJCalendarVO calendarVo,GomeGmpResUserBO userBo) {
		String[] orgIds = getQueryOrgIds(calendarVo,userBo);
		calendarVo.setOrgIds(orgIds);
		return gomeGmpResProCalendarDAO.findGomeGmpResProjectBOListByDate(calendarVo);
	}

	@Override
	public List<GomeGmpTJCalendarVO> findGomeGmpResScheduleCountListByMonth(GomeGmpTJCalendarVO calendarVo,GomeGmpResUserBO userBo) {
		String[] orgIds = getQueryOrgIds(calendarVo,userBo);
		calendarVo.setOrgIds(orgIds);
		return gomeGmpResProCalendarDAO.findGomeGmpResScheduleCountListByMonth(calendarVo);
	}

	private String[] getQueryOrgIds(GomeGmpTJCalendarVO calendarVo,GomeGmpResUserBO userBo) {
		GomeGmpResProjectVO projectVo = new GomeGmpResProjectVO();
		projectVo.setOrgIds(calendarVo.getOrgIds());
		projectVo.setChildOrgIds(calendarVo.getChildOrgIds());
		projectVo.setGroupIds(calendarVo.getGroupIds());
		projectVo = gomeGmpResProjectBS.handleQueryOrgId(projectVo);
		if(projectVo.getOrgIds()==null){
			Map<String, Object> userOrgInfoMap = gomeGmpOrgManageBS.getUserOrgInfo(userBo);
			return (String[]) userOrgInfoMap.get("orgIds"); 
		}else{
			return projectVo.getOrgIds();
		}
	}

	@Override
	public List<GomeGmpResTaskVO> findGomeGmpResPlanScheduleBOListByDate(GomeGmpResTaskVO gomeGmpResTaskVO) {
		return gomeGmpResProCalendarDAO.findGomeGmpResPlanScheduleBOListByDate(gomeGmpResTaskVO);
	}

}
