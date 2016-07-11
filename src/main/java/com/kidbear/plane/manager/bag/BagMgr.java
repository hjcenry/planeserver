package com.kidbear.plane.manager.bag;

import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kidbear.plane.manager.event.ED;
import com.kidbear.plane.manager.event.Event;
import com.kidbear.plane.manager.event.EventMgr;
import com.kidbear.plane.manager.event.EventProc;
import com.kidbear.plane.manager.junzhu.JunZhu;
import com.kidbear.plane.manager.junzhu.JunZhuMgr;
import com.kidbear.plane.net.ProtoMessage;
import com.kidbear.plane.net.http.HttpInHandler;
import com.kidbear.plane.util.cache.MC;
import com.kidbear.plane.util.csv.TempletService;
import com.kidbear.plane.util.hibernate.HibernateUtil;
import com.kidbear.plane.util.hibernate.TableIDCreator;
import com.kidbear.plane.util.redis.Redis;

public class BagMgr extends EventProc {
	private static BagMgr bagMgr;
	private static final Logger logger = LoggerFactory.getLogger(BagMgr.class);
	public Redis redis = Redis.getInstance();

	private BagMgr() {
	}

	public static BagMgr getInstance() {
		if (null == bagMgr) {
			bagMgr = new BagMgr();
		}
		return bagMgr;
	}

	public void initCsvData() {
	}

	public void initData() {
	}

	@Override
	public void proc(Event param) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doReg() {
		// TODO Auto-generated method stub

	}

}
