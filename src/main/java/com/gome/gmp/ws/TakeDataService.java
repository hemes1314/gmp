package com.gome.gmp.ws;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据同步抽象父类
 */
public abstract class TakeDataService {

	private static Logger logger = LoggerFactory.getLogger(TakeDataService.class);
	
	Lock lock = new ReentrantLock();// 锁  
	
	public void takeDataForAgent() {
		logger.info("开始执行数据同步定时任务");
		lock.lock();
		try {
			takeData();
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			lock.unlock();
		}
	};

	public abstract void takeData();
}
