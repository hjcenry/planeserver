package com.kidbear._36.manager.building;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kidbear._36.manager.GameData;
import com.kidbear._36.net.ProtoMessage;
import com.kidbear._36.net.http.HttpHandler;
import com.kidbear._36.template.JunjichuUpgrade;
import com.kidbear._36.util.csv.TempletService;
import com.kidbear._36.util.mongo.DBObjectUtil;
import com.kidbear._36.util.mongo.MongoCollections;
import com.kidbear._36.util.mongo.MongoUtil;
import com.mongodb.BasicDBObject;

public class JunjichuMgr {
	private static JunjichuMgr junjichuMgr;
	private static final Logger logger = LoggerFactory
			.getLogger(BuildingMgr.class);
	public Map<Integer, JunjichuUpgrade> junjichuMap = new HashMap<Integer, JunjichuUpgrade>();

	private JunjichuMgr() {
	}

	public static JunjichuMgr getInstance() {
		if (null == junjichuMgr) {
			junjichuMgr = new JunjichuMgr();
		}
		return junjichuMgr;
	}

	public void initData() {
		logger.info("JunjichuMgr initData");
		List<JunjichuUpgrade> junjichuUpgrades = TempletService
				.listAll(JunjichuUpgrade.class.getSimpleName());
		Map<Integer, JunjichuUpgrade> junjichuMap = new HashMap<Integer, JunjichuUpgrade>();
		for (JunjichuUpgrade junjichuUpgrade : junjichuUpgrades) {
			junjichuMap.put(junjichuUpgrade.getScienceLv(), junjichuUpgrade);
		}
		this.junjichuMap = junjichuMap;
	}

	/**
	 * @Title: upgrade
	 * @Description: TODO 军机处升级
	 * @param msg
	 * @param ctx
	 *            void
	 * @throws
	 */
	public void upgrade(ProtoMessage msg, ChannelHandlerContext ctx) {
		// TODO
		long userid = 1;
		int junjiChuLv = 1;
		GameData data = new GameData();
		data.setJunjichu_level(data.getJunjichu_level() + 1);
		MongoUtil.getInstance().updateOne(MongoCollections.USER_DATA, userid,
				DBObjectUtil.bean2DBObject(data));
		HttpHandler.writeJSON(ctx, msg);
	}
}
