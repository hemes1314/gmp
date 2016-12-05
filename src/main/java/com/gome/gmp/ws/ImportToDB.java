package com.gome.gmp.ws;

import java.util.List;

/**
 * 基础数据同步接口
 * 
 * @param <T>
 *
 * @author wubin
 */
public interface ImportToDB<T> {

	/**
	 * 导入数据
	 * 
	 * @param datas
	 * @return
	 */
	int importData(List<T> datas);
}
