package com.gome.gmp.business.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.gome.framework.logging.Logger;
import com.gome.framework.logging.LoggerFactory;
import com.gome.gmp.business.GomeGmpResLogBS;
import com.gome.gmp.dao.GomeGmpResLogDAO;
import com.gome.gmp.model.vo.GomeGmpResLogVO;

/**
 * 
 *
 * @author wubin
 */
@Service("gomeGmpResLogBS")
public class GomeGmpResLogBSImpl implements GomeGmpResLogBS {

	protected Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Resource
	private GomeGmpResLogDAO gomeGmpResLogDAO;

	/**
	 * 查询变更记录
	 * 
	 * @see com.gome.gmp.business.GomeGmpResLogBS#findGomeGmpResLog(com.gome.gmp.
	 *      model.vo.GomeGmpResLogVO)
	 */
	@Override
	public Page<GomeGmpResLogVO> findGomeGmpResLog(GomeGmpResLogVO gomeGmpResLogVO) {
		return gomeGmpResLogDAO.findGomeGmpResLogBO(gomeGmpResLogVO);
	}
}
