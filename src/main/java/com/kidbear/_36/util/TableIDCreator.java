package com.kidbear._36.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kidbear._36.util.hibernate.HibernateUtil;
import com.kidbear._36.util.memcached.MemcachedCRUD;

public class TableIDCreator {

	private static Logger logger = LoggerFactory
			.getLogger(TableIDCreator.class);
	private static MemcachedCRUD memcachedCRUD = MemcachedCRUD.getInstance();

	public static <T> long getTableID(Class<T> clazz, long startId) {
		String key = clazz.getName() + "#id";
		// 表的主键ID从1开始
		Long id = null;
		if (memcachedCRUD.<Integer> get(key) == -1) {
			// 从数据库里查询该表当前主键的最大值
			id = HibernateUtil.getTableIDMax(clazz);
			if (id == null) {
				boolean ret = memcachedCRUD.safeSet(key, 0, startId);
				logger.info("A开始为table:{}设置主键ID:{} ret {}", key, startId, ret);
			} else {
				boolean ret = memcachedCRUD.safeSet(key, 0,
						Math.max(startId, id));// 即便数据库有记录，也比较该id是否满足参数startId的要求。
				logger.info("B开始为table:{}设置主键ID:{} ret {}", key, id, ret);
			}
		}
		id = memcachedCRUD.incr(key, 1, 1);
		if (id == -1) {
			logger.error("table:{}主键增加失败", key);
			return -1;
		} else {
			logger.info("table:{}的ID加1增长为{}", key, id);
		}
		return id;
	}

}
