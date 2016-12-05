package com.gome.gmp.model.vo;

import org.apache.commons.lang3.math.NumberUtils;

import com.gome.gmp.common.BusinessUtil;
import com.gome.gmp.common.filter.CommandContext;
import com.gome.gmp.model.bo.GomeGmpResLogBO;

/**
 * @author Administrator
 */
public class GomeGmpResLogVO extends GomeGmpResLogBO {

	/**  */
	private static final long serialVersionUID = 6412805504310165860L;
	private String createUserName;

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public GomeGmpResLogVO() {

		Long loginUser = BusinessUtil.getLoginUserId(CommandContext.getRequest());
		this.setCreateUser(NumberUtils.toInt((String.valueOf(loginUser))));
	}

}