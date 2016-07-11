package com.kidbear.plane.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;

import com.kidbear.plane.manager.account.AccountMgr;
import com.kidbear.plane.manager.bag.BagMgr;
import com.kidbear.plane.manager.hotfix.HotFixMgr;
import com.kidbear.plane.manager.junzhu.JunZhuMgr;
import com.kidbear.plane.manager.mail.MailMgr;
import com.kidbear.plane.manager.shop.ShopMgr;
import com.kidbear.plane.net.ProtoMessage;

/**
 * @ClassName: Router
 * @Description: 消息路由分发
 * @author 何金成
 * @date 2015年12月14日 下午7:12:57
 * 
 */
public class Router {
	private static Router router = new Router();
	public Logger logger = LoggerFactory.getLogger(Router.class);
	public AccountMgr accountMgr;
	public MailMgr mailMgr;
	public BagMgr bagMgr;
	public JunZhuMgr junZhuMgr;
	public ShopMgr shopMgr;
	public HotFixMgr hotFixMgr;

	private Router() {

	}

	public void initMgr() {
		accountMgr = AccountMgr.getInstance();
		mailMgr = MailMgr.getInstance();
		bagMgr = BagMgr.getInstance();
		junZhuMgr = JunZhuMgr.getInstance();
		shopMgr = ShopMgr.getInstance();
		hotFixMgr = HotFixMgr.getInstance();
	}

	public static Router getInstance() {
		if (null == router) {
			router = new Router();
		}
		return router;
	}

	public void initCsvData() {// 初始化Csv
		bagMgr.initCsvData();
		junZhuMgr.initCsvData();
		shopMgr.initCsvData();
	}

	public void initData() {// 初始化数值
		accountMgr.initData();
		mailMgr.initData();
		bagMgr.initData();
		junZhuMgr.initData();
		shopMgr.initData();
	}

	/**
	 * @Title: route
	 * @Description: 消息路由分发
	 * @param val
	 * @param ctx
	 *            void
	 * @throws
	 */
	public void route(String val, ChannelHandlerContext ctx) {
	}

	public void test(ProtoMessage msg, ChannelHandlerContext ctx) {
		// logger.info("收到客户端的测试消息:");
		// logger.info("id:" + msg.getId());
		// logger.info("msg:" +
		// JsonUtils.objectToJson(msg.getData(TestReq.class)));
		// JSONObject object = new JSONObject();
		// object.put("msg", "服务器收到测试消息");
		// ProtoMessage message = new ProtoMessage(ProtoIds.TEST, object);
		// HttpHandler.writeJSON(ctx, message);
	}
}
