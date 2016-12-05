package com.gome.gmp.business.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gome.gmp.business.GomeGmpTJProjectBS;
import com.gome.gmp.common.BusinessUtil;
import com.gome.gmp.common.util.DateUtil;
import com.gome.gmp.dao.GomeGmpTJProjectDAO;
import com.gome.gmp.model.vo.GomeGmpResDailyVO;
import com.gome.gmp.model.vo.GomeGmpResProjectVO;
import com.gome.gmp.model.vo.GomeGmpTJHourVO;

@Service
public class GomeGmpTJProjectBSImpl implements GomeGmpTJProjectBS {

	@Resource
	private GomeGmpTJProjectDAO gomeGmpTJProjectDAO;

	/**
	 * 状态分布
	 * 
	 * @see com.gome.gmp.business.GomeGmpTJProjectBS#stateDistribution(com.gome.gmp.
	 *      model.vo.GomeGmpResProjectVO)
	 */
	@Override
	public List<Map<String, Object>> stateDistribution(GomeGmpResProjectVO gomeGmpResProjectVO) {
		BusinessUtil.repeatOrg(gomeGmpResProjectVO);
		gomeGmpResProjectVO.setEndCreateTime(DateUtil.getDate(DateUtil.getDate(gomeGmpResProjectVO.getEndCreateTime()) + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		List<Map<String, Object>> result = gomeGmpTJProjectDAO.stateDistribution(gomeGmpResProjectVO);
		// 为每个进度设置固定颜色
		String[] scheduleArr = { "未启动", "正常", "提前", "延期风险", "已延期", "已上线", "待上线", "已取消", "内部上线", "暂停", "暂缓", "待排期", "部分上线" };
		String[] colorArr = { "#F4E001", "#B5C334", "#FCCE10", "#E87C25", "#27727B", "#FE8463", "#9BCA63", "#FAD860", "#F3A43B", "#60C0DD", "#D7504B", "#C6E579", "#C1232B", "#F0805A", "#26C0C0" };
		Map<String, String> name2Color = new HashMap<String, String>();
		for (int i = 0; i < scheduleArr.length; i++) {
			name2Color.put(scheduleArr[i], colorArr[i]);
		}
		// itemStyle:{normal:{color:'red'}}
		for (Map<String, Object> obj : result) {
			Map<String, String> colorMap = new HashMap<String, String>();
			colorMap.put("color", name2Color.get(obj.get("name")));
			Map<String, Object> normalMap = new HashMap<String, Object>();
			normalMap.put("normal", colorMap);
			obj.put("itemStyle", normalMap);
		}
		return result;
	}

	/**
	 * 项目工时统计
	 * 
	 * @see com.gome.gmp.business.GomeGmpTJProjectBS#workHour(com.gome.gmp.model.vo.
	 *      GomeGmpTJHourVO)
	 */
	@Override
	public GomeGmpTJHourVO workHour(GomeGmpTJHourVO gomeGmpTJHourVO) {
		GomeGmpTJHourVO hourVO = gomeGmpTJProjectDAO.workHour(gomeGmpTJHourVO);
		if (hourVO == null) {
			return gomeGmpTJHourVO;
		}
		List<GomeGmpResDailyVO> details = gomeGmpTJProjectDAO.workHourDetail(gomeGmpTJHourVO);
		if (details == null || details.size() == 0) {
			return hourVO;
		}
		String hourDataTmp = details.get(0).getHourDate();
		int rowspanNum = 0;
		GomeGmpResDailyVO dailyVOTmp = details.get(0);
		Double realHour = 0.0;
		for (int i = 0; i < details.size(); i++) {
			GomeGmpResDailyVO dailyVO = details.get(i);
			if (!hourDataTmp.equals(dailyVO.getHourDate())) {
				dailyVOTmp.setRowspanNum(rowspanNum);
				dailyVOTmp = dailyVO;
				hourDataTmp = details.get(i).getHourDate();
				rowspanNum = 0;
			}
			rowspanNum++;
			if (i == details.size() - 1) {
				dailyVOTmp.setRowspanNum(rowspanNum);
			}
			realHour += dailyVO.getHours();
		}
		hourVO.setDetails(details);
		hourVO.setRealHour(realHour);
		long finishPlan = Math.round(realHour/hourVO.getPlanHour()*100);
		hourVO.setPercentage(finishPlan);
		return hourVO;
	}
}
