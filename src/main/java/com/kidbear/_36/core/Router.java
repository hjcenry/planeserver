package com.kidbear._36.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import net.sf.json.JSONObject;

import com.kidbear._36.manager.account.AccountMgr;
import com.kidbear._36.manager.building.JunjichuMgr;
import com.kidbear._36.net.ProtoIds;
import com.kidbear._36.net.ProtoMessage;
import com.kidbear._36.net.http.HttpHandler;
import com.kidbear._36.net.message.TestReq;
import com.kidbear._36.net.socket.SocketHandler;
import com.kidbear._36.util.JsonUtils;

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
	public JunjichuMgr junjichuMgr;

	private Router() {
		accountMgr = AccountMgr.getInstance();
		junjichuMgr = JunjichuMgr.getInstance();
	}

	public static Router getInstance() {
		if (null == router) {
			router = new Router();
		}
		return router;
	}

	public void initData() {// 初始化模块
		accountMgr.initData();
		junjichuMgr.initData();
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
		ProtoMessage msg = (ProtoMessage) JsonUtils.jsonToBean(val,
				ProtoMessage.class);
		int cmd = msg.getId();
		switch (cmd) {
		case ProtoIds.C_TEST:
			test(msg, ctx);
			break;
		case ProtoIds.C_LOGIN_REQ:
			// AccountMgr.getInstance().login(cmd, msg.getMsg(LoginReq.class),
			// session);
			break;

		default:
			break;
		}
	}

	public void test(ProtoMessage msg, ChannelHandlerContext ctx) {
		logger.info("收到客户端的测试消息:");
		logger.info("id:" + msg.getId());
		logger.info("msg:" + JsonUtils.objectToJson(msg.getData(TestReq.class)));
		JSONObject object = new JSONObject();
		object.put("msg", "服务器收到测试消息");
		ProtoMessage message = new ProtoMessage(ProtoIds.C_TEST, object);
		HttpHandler.writeJSON(ctx, message);
	}
}
