package com.kidbear.plane.manager.hotfix;

import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.kidbear.plane.net.ProtoMessage;
import com.kidbear.plane.net.http.HttpInHandler;
import com.kidbear.plane.net.http.HttpServer;
import com.kidbear.plane.util.redis.Redis;

public class HotFixMgr {
	private static HotFixMgr hotFixMgr;
	private static final Logger logger = LoggerFactory
			.getLogger(HotFixMgr.class);
	public Redis redis = Redis.getInstance();
	public static final String HOT_FIX = "hotfix";

	private HotFixMgr() {
	}

	public static HotFixMgr getInstance() {
		if (null == hotFixMgr) {
			hotFixMgr = new HotFixMgr();
		}
		return hotFixMgr;
	}

	public void initData() {
		logger.info("HotFixMgr initData");
	}

	public void queryDownUrl(ChannelHandlerContext ctx, ProtoMessage msg) {
		JSONArray ret = new JSONArray();
		String version = msg.getData().getString("fileversion");// 版本号
		String base = msg.getData().getString("base");// 版本号
		String channel = msg.getData().getString("channel");// 渠道号
		String val = Redis.getInstance().hget(Redis.GLOBAL_DB, HOT_FIX,
				channel + "#" + base);
		if (val != null && !val.split("#")[0].equals(version)) {
			ret.add("http://" + HttpServer.fileServer + ":"
					+ HttpServer.filePort);
			ret.add(val.split("#")[1]);
		}
		HttpInHandler.writeJSON(ctx, ret);
	}

	public void queryVersion(ChannelHandlerContext ctx, String channel,
			String base) {
		String val = Redis.getInstance().hget(Redis.GLOBAL_DB, HOT_FIX,
				channel + "#" + base);
		if (val == null) {
			HttpInHandler.writeJSON(ctx, "0.0.0");
		} else {
			HttpInHandler.writeJSON(ctx, val.split("#")[0]);
		}
	}
}
