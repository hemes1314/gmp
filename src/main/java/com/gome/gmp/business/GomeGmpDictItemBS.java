package com.gome.gmp.business;

import java.util.List;

import com.gome.gmp.model.bo.GomeGmpDictItemBO;
import com.gome.gmp.model.vo.GomeGmpDictItemVO;

/**
 * 字典数据
 *
 * @author wubin
 */
public interface GomeGmpDictItemBS {

	/**
	 * 初始化数据字典数据到缓存
	 */
	public void initDictItemEhcache();

	/**
	 * 获取字典缓存
	 * 
	 * @param groupType
	 * @return
	 */
	public List<GomeGmpDictItemBO> getDictItem(String groupType);

	/**
	 * 获取字典值
	 * 
	 * @param groupType
	 * @param itemId
	 * @return
	 */
	public String getItemName(String groupType, String itemId);

	/**
	 * 获取节假日
	 * 
	 * @return
	 */
	public List<String> holidayDates(GomeGmpDictItemVO gomeGmpDictItemVO);

	/**
	 * 获取日志类型
	 * 
	 * @return
	 */
	public List<GomeGmpDictItemBO> getDailyType();
}
