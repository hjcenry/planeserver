package com.kidbear._36.util.memcached;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.FailureMode;
import net.spy.memcached.ConnectionFactoryBuilder.Locator;
import net.spy.memcached.ConnectionFactoryBuilder.Protocol;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.HashAlgorithmRegistry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;

import com.kidbear._36.core.GameInit;

/**
 * 对SpyMemcached Client的二次封装,提供常用的Get/GetBulk/Set/Delete/Incr/Decr函数的同步与异步操作封装.
 * 
 * 未提供封装的函数可直接调用getClient()取出Spy的原版MemcachedClient来使用.
 * 
 * @author 何金成
 */
public class MemcachedCRUD implements DisposableBean {

	private static Logger logger = LoggerFactory.getLogger(MemcachedCRUD.class);
	private MemcachedClient memcachedClient;
	private long shutdownTimeout = 2500;
	private long updateTimeout = 2500;
	private static MemcachedCRUD inst;

	public static MemcachedCRUD getInstance() {
		if (inst == null) {
			inst = new MemcachedCRUD();
		}
		return inst;
	}

	private MemcachedCRUD() {
		String cacheServer = GameInit.cfg.get("cacheServer");
		if (cacheServer == null) {
			cacheServer = "localhost:11211";
		}
		// String cacheServer = "123.57.211.130:11211";
		String host = cacheServer.split(":")[0];
		int port = Integer.parseInt(cacheServer.split(":")[1]);
		List<InetSocketAddress> addrs = new ArrayList<InetSocketAddress>();
		addrs.add(new InetSocketAddress(host, port));
		try {
			ConnectionFactoryBuilder builder = new ConnectionFactoryBuilder();
			builder.setProtocol(Protocol.BINARY);
			builder.setOpTimeout(1000);
			builder.setDaemon(true);
			builder.setOpQueueMaxBlockTime(1000);
			builder.setMaxReconnectDelay(1000);
			builder.setTimeoutExceptionThreshold(1998);
			builder.setFailureMode(FailureMode.Retry);
			builder.setHashAlg(HashAlgorithmRegistry
					.lookupHashAlgorithm("KETAMA_HASH"));
			builder.setLocatorType(Locator.CONSISTENT);
			builder.setUseNagleAlgorithm(false);
			memcachedClient = new MemcachedClient(builder.build(), addrs);
			logger.info("Memcached at {}:{}", host, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get方法, 转换结果类型并屏蔽异常, 仅返回Null.
	 */
	public <T> T get(String key) {
		try {
			return (T) memcachedClient.get(key);
		} catch (RuntimeException e) {
			handleException(e, key);
			return null;
		}
	}

	/**
	 * GetBulk方法, 转换结果类型并屏蔽异常.
	 */
	public <T> Map<String, T> getBulk(Collection<String> keys) {
		try {
			return (Map<String, T>) memcachedClient.getBulk(keys);
		} catch (RuntimeException e) {
			handleException(e, StringUtils.join(keys, ","));
			return null;
		}
	}

	/**
	 * 异步Set方法, 不考虑执行结果.
	 * 
	 * @param expiredTime
	 *            以秒过期时间，0表示没有延迟，如果exptime大于30天，Memcached将使用它作为UNIX时间戳过期
	 */
	public void set(String key, int expiredTime, Object value) {
		memcachedClient.set(key, expiredTime, value);
	}

	/**
	 * 安全的Set方法, 保证在updateTimeout秒内返回执行结果, 否则返回false并取消操作.
	 * 
	 * @param expiredTime
	 *            以秒过期时间，0表示没有延迟，如果exptime大于30天，Memcached将使用它作为UNIX时间戳过期
	 */
	public boolean safeSet(String key, int expiration, Object value) {
		Future<Boolean> future = memcachedClient.set(key, expiration, value);
		try {
			return future.get(updateTimeout, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			future.cancel(false);
		}
		return false;
	}

	/**
	 * 异步 Delete方法, 不考虑执行结果.
	 */
	public void delete(String key) {
		memcachedClient.delete(key);
	}

	/**
	 * 安全的Delete方法, 保证在updateTimeout秒内返回执行结果, 否则返回false并取消操作.
	 */
	public boolean safeDelete(String key) {
		Future<Boolean> future = memcachedClient.delete(key);
		try {
			return future.get(updateTimeout, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			future.cancel(false);
		}
		return false;
	}

	/**
	 * Incr方法.
	 */
	public long incr(String key, int by, long defaultValue) {
		return memcachedClient.incr(key, by, defaultValue);
	}

	/**
	 * Decr方法.
	 */
	public long decr(String key, int by, long defaultValue) {
		return memcachedClient.decr(key, by, defaultValue);
	}

	/**
	 * 异步Incr方法, 不支持默认值, 若key不存在返回-1.
	 */
	public Future<Long> asyncIncr(String key, int by) {
		return memcachedClient.asyncIncr(key, by);
	}

	/**
	 * 异步Decr方法, 不支持默认值, 若key不存在返回-1.
	 */
	public Future<Long> asyncDecr(String key, int by) {
		return memcachedClient.asyncDecr(key, by);
	}

	private void handleException(Exception e, String key) {
		logger.warn("spymemcached client receive an exception with key:" + key,
				e);
	}

	@Override
	public void destroy() {
		if (memcachedClient != null) {
			memcachedClient.shutdown(shutdownTimeout, TimeUnit.MILLISECONDS);
		}
	}

	public MemcachedClient getMemcachedClient() {
		return memcachedClient;
	}

	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

	public void setUpdateTimeout(long updateTimeout) {
		this.updateTimeout = updateTimeout;
	}

	public void setShutdownTimeout(long shutdownTimeout) {
		this.shutdownTimeout = shutdownTimeout;
	}
}