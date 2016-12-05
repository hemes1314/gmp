package com.gome.gmp.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gome.framework.base.BaseRestController;
import com.gome.gmp.business.GomeGmpDictItemBS;
import com.gome.gmp.model.bo.GomeGmpDictItemBO;
import com.gome.gmp.model.vo.GomeGmpDictItemVO;

/**
 * 字典缓存
 * 
 * @author wubin
 */
@RestController
@RequestMapping("dict")
public class GomeGmpDictItemCTL extends BaseRestController {

	@Resource
	private GomeGmpDictItemBS gomeGmpDictItemBS;

	/**
	 * 字典数据
	 * 
	 * @param pro
	 * @return
	 */
	@RequestMapping(value = "/{groupType}", method = RequestMethod.GET)
	public List<GomeGmpDictItemBO> find(@PathVariable String groupType) {
		return gomeGmpDictItemBS.getDictItem(groupType);
	}

	/**
	 * 获取节假日
	 * @param startDate yyyy-mm-dd
	 * @param endDate yyyy-mm-dd
	 * @return
	 */
	@RequestMapping(value = "/holiday", method = RequestMethod.GET)
	public List<String> holiday(String startDate, String endDate) {
		GomeGmpDictItemVO itemVO = new GomeGmpDictItemVO();
		itemVO.setStartDate(startDate);
		itemVO.setEndDate(endDate);
		return gomeGmpDictItemBS.holidayDates(itemVO);
	}
	
	/**
	 * 获取节假日天数
	 * @param startDate yyyy-mm-dd
	 * @param endDate yyyy-mm-dd
	 * @return
	 */
	@RequestMapping(value = "/holidayCount", method = RequestMethod.GET)
	public Integer holidayCount(String startDate, String endDate) {
		
		GomeGmpDictItemVO itemVO = new GomeGmpDictItemVO();
		itemVO.setStartDate(startDate);
		itemVO.setEndDate(endDate);
		List<String> rtnList = gomeGmpDictItemBS.holidayDates(itemVO);
		return rtnList.size();
	}
}
