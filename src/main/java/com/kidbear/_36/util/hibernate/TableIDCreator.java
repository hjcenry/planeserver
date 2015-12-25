package com.kidbear._36.util.hibernate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kidbear._36.util.memcached.MemcachedCRUD;

public class TableIDCreator {

	private static Logger logger = LoggerFactory
			.getLogger(TableIDCreator.class);
	private static MemcachedCRUD memcached = MemcachedCRUD.getInstance();

	public static <T> long getTableID(Class<T> clazz, long startId) {
		String key = clazz.getName() + "#id";
		// 表的主键ID从1开始
		Long id = memcached.<Long> get(key);
		if (id == null) {
			// 从数据库里查询该表当前主键的最大值
			id = HibernateUtil.getTableIDMax(clazz);
			if (id == null) {
				boolean ret = memcached.safeSet(key, 0, startId);
				// logger.info("A开始为table:{}设置主键ID:{} ret {}", key, startId,
				// ret);
			} else {
				boolean ret = memcached.safeSet(key, 0, id);
				// logger.info("B开始为table:{}设置主键ID:{} ret {}", key, id,ret);
			}
		}
		id = id + 1l;
		memcached.set(key, 0, id);
		return id;
	}

	public static void main(String[] args) {
	}
}
