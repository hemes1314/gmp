package com.gome.gmp.business.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gome.framework.logging.Logger;
import com.gome.framework.logging.LoggerFactory;
import com.gome.gmp.business.GomeGmpDictItemBS;
import com.gome.gmp.business.GomeGmpOrgManageBS;
import com.gome.gmp.common.constant.Constants;
import com.gome.gmp.dao.GomeGmpDictItemDAO;
import com.gome.gmp.model.bo.GomeGmpDictItemBO;
import com.gome.gmp.model.vo.GomeGmpDictItemVO;

/**
 * 字典数据缓存
 * 
 * @author wubin
 */
@Service("gomeGmpDictItemBS")
public class GomeGmpDictItemBSImpl extends BaseBS implements GomeGmpDictItemBS {

	private static Logger logger = LoggerFactory.getLogger(GomeGmpDictItemBSImpl.class);

	@Resource
	private GomeGmpDictItemDAO gomeGmpDictItemDAO;
	
	@Resource
	private GomeGmpOrgManageBS gomeGmpOrgManageBS;

	/**
	 * 刷新数据字典缓存
	 */
	@Override
	@Transactional
	public void initDictItemEhcache() {
		List<GomeGmpDictItemBO> dictItems = gomeGmpDictItemDAO.findGomeGmpDictItemBO(new GomeGmpDictItemBO());
		if (dictItems == null || dictItems.size() == 0) {
			return;
		}
		Map<String, ArrayList<GomeGmpDictItemBO>> groupMap = new HashMap<String, ArrayList<GomeGmpDictItemBO>>();
		// 生成group
		for (GomeGmpDictItemBO item : dictItems) {
			if (item != null && StringUtils.isNotBlank(item.getGroupType())) {
				groupMap.put(item.getGroupType(), new ArrayList<GomeGmpDictItemBO>());
			}
		}
		// 填充item
		for (GomeGmpDictItemBO item : dictItems) {
			if (item != null && StringUtils.isNotBlank(item.getGroupType())) {
				groupMap.get(item.getGroupType()).add(item);
			}
		}
		// 放入缓存
		for (String key : groupMap.keySet()) {
			putEhcache(Constants.EHCACHE_TYPE_DICTITEM_LIST, key, groupMap.get(key));
			// 将bean转换为map存储
			Map<String, String> itemMap = new HashMap<String, String>();
			for (GomeGmpDictItemBO vo : groupMap.get(key)) {
				itemMap.put(vo.getItemId(), vo.getItemName());
			}
			putEhcache(Constants.EHCACHE_TYPE_DICTITEM_MAP, key, itemMap);
		}
	}

	/**
	 * 获取字典缓存
	 * 
	 * @param groupType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<GomeGmpDictItemBO> getDictItem(String groupType) {
		List<GomeGmpDictItemBO> dictItem = getFromEhCache(Constants.EHCACHE_TYPE_DICTITEM_LIST, groupType, List.class);
		if (dictItem == null) {
			logger.error("getDictItem 获取字典缓存数据失败,重新加载缓存数据:groupType:"+groupType);
			this.initDictItemEhcache();
			dictItem = getFromEhCache(Constants.EHCACHE_TYPE_DICTITEM_LIST, groupType, List.class);
		}
		return dictItem;
	}
	
	/**
	 * 获取字典值
	 * 
	 * @param groupType
	 * @param itemId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String getItemName(String groupType, String itemId) {
		Map<String, String> itemMap = getFromEhCache(Constants.EHCACHE_TYPE_DICTITEM_MAP, groupType, Map.class);
		if (itemMap == null) {
			this.initDictItemEhcache();
			itemMap = getFromEhCache(Constants.EHCACHE_TYPE_DICTITEM_MAP, groupType, Map.class);
		}
		if (itemMap == null || !itemMap.containsKey(itemId)) {
			return null;
		}
		return itemMap.get(itemId);
	}

	/**
	 * 获取节假日
	 * 
	 * @return
	 */
	@Override
	public List<String> holidayDates(GomeGmpDictItemVO gomeGmpDictItemVO) {
		return gomeGmpDictItemDAO.holidayDates(gomeGmpDictItemVO);
	}

	@Override
	public List<GomeGmpDictItemBO> getDailyType() {
		return gomeGmpDictItemDAO.getDailyType();
	}
}
