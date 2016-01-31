package com.kidbear._36.core;

import mongotest.MainTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kidbear._36.net.socket.ChannelMgr;
import com.kidbear._36.util.Config;
import com.kidbear._36.util.ThreadViewer;
import com.kidbear._36.util.csv.CsvDataLoader;
import com.kidbear._36.util.hibernate.HibernateUtil;
import com.kidbear._36.util.memcached.MemcachedCRUD;
import com.kidbear._36.util.mongo.MongoUtil;
import com.kidbear._36.util.redis.Redis;
import com.kidbear._36.util.sensitive.SensitiveFilter;

/**
 * @ClassName: GameInit
 * @Description: 服务器初始化
 * @author 何金成
 * @date 2015年5月23日 下午4:21:19
 * 
 */
public class GameInit {
	public static int serverId = 1; // 服务器标示
	public static String confFileBasePath = "/csv/";// 配置文件根目录
	public static Config cfg;// 读取server.properties配置
	public static String templatePacket = "com.kidbear._36.template.";// xml模板类的位置
	public static String dataConfig = "/dataConfig.xml";// xml位置
	private static final Logger logger = LoggerFactory
			.getLogger(GameInit.class);

	public static boolean init() {
		try {
			logger.info("================开启逻辑服务器================");
			// 加载服务器配置文件
			logger.info("加载服务器配置文件");
			confInit();
			// 加载XML数据
			logger.info("加载数据配置");
			new CsvDataLoader(templatePacket, dataConfig).load();
			// 加载敏感词语
			logger.info("加载敏感词语");
			SensitiveFilter.getInstance();
			// 加载Redis
			logger.info("加载Redis");
			final Redis r = Redis.getInstance();
			new Thread(new Runnable() {// 测试Redis
						@Override
						public void run() {
							try {
								r.test();
							} catch (Exception e) {
								logger.info("Redis未启动，初始化异常");
							}
						}
					}).start();
			// memcached
			logger.info("加载Memcached");
			MemcachedCRUD.getInstance();
			// 加载hibernate
			logger.info("加载hibernate");
			HibernateUtil.init();
			// 加载mongo
			logger.info("加载mongo");
			if (MongoUtil.getInstance() == null) {
				logger.info("mongo登录失败，初始化异常");
			}
			// 初始化消息路由和系统模块
			logger.info("初始化消息路由和系统模块");
			Router.getInstance().initData();
			// 初始化消息协议
			logger.info("初始化消息协议");
			// ProtoIds.init();
			// 启动线程监控
			logger.info("启动线程监控");
			ThreadViewer.start();
			// 启动Channel管理
			logger.info("启动Channel管理");
			ChannelMgr.getInstance();
			// 启动服务器
			GameServer.getInstance().startServer();
			// ServerNotify.startServer();
			logger.info("================完成开启逻辑服务器================");
			return true;
		} catch (Throwable e) {
			CoreServlet.logger.error("初始化异常:{}", e);
			return false;
		}
	}

	public static void confInit() {
		cfg = new Config();
		cfg.loadConfig();
		serverId = cfg.get("serverId", serverId);
	}

	public static boolean shutdown() {
		logger.info("================关闭逻辑服务器================");
		// 通知关闭服务器，部署36router开启
		// ServerNotify.shutServer();
		logger.info("关闭redis连接");
		Redis.destroy();
		logger.info("关闭memcached连接");
		MemcachedCRUD.getInstance().destroy();
		// 关闭逻辑服务器
		logger.info("关闭逻辑服务器");
		GameServer.getInstance().shutServer();
		logger.info("================完成关闭服务器================");
		return true;
	}
}
