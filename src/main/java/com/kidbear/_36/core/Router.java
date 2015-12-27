package com.kidbear._36.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import net.sf.json.JSONObject;

import com.kidbear._36.manager.AccountMgr;
import com.kidbear._36.net.ProtoIds;
import com.kidbear._36.net.ProtoMessage;
import com.kidbear._36.net.SocketHandler;
import com.kidbear._36.net.message.TestReq;
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

	private Router() {
	}

	public static Router getInstance() {
		if (null == router) {
			router = new Router();
		}
		return router;
	}

	public void initData() {// 初始化模块
		AccountMgr.getInstance().initData();
	}

	/**
	 * @Title: route
	 * @Description: 消息路由分发
	 * @param cmd
	 *            协议号
	 * @param msg
	 *            消息体
	 * @param session
	 *            IoSession
	 * @return void
	 * @throws
	 */
	public void route(ProtoMessage msg, ChannelHandlerContext ctx) {
		int cmd = msg.getProtoId();
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
		logger.info("id:" + msg.getProtoId());
		logger.info("msg:" + JsonUtils.objectToJson(msg.getMsg(TestReq.class)));
		JSONObject object = new JSONObject();
		object.put("msg", "服务器收到测试消息");
		ProtoMessage message = new ProtoMessage(ProtoIds.C_TEST, object);
		SocketHandler.writeJSON(ctx, message);
	}
}
