package com.gome.gmp.business;

import com.github.pagehelper.Page;
import com.gome.gmp.model.vo.GomeGmpResLogVO;

/**
 * 变更记录
 *
 * @author wubin
 */
public interface GomeGmpResLogBS {

	/**
	 * 查询变更记录
	 * 
	 * @param gomeGmpResLogVO
	 * @return
	 * @author wubin
	 */
	Page<GomeGmpResLogVO> findGomeGmpResLog(GomeGmpResLogVO gomeGmpResLogVO);
}
