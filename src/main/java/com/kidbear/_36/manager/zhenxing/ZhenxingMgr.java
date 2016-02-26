package com.kidbear._36.manager.zhenxing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.kidbear._36.manager.building.BuildingInfo;
import com.kidbear._36.manager.junzhu.JunZhu;
import com.kidbear._36.manager.tec.TecMgr;
import com.kidbear._36.net.ProtoMessage;
import com.kidbear._36.net.http.HttpHandler;
import com.kidbear._36.template.KejiOpen;
import com.kidbear._36.util.hibernate.HibernateUtil;
import com.kidbear._36.util.hibernate.TableIDCreator;

public class ZhenxingMgr {
	private static ZhenxingMgr zhenxingMgr;
	private static final Logger logger = LoggerFactory
			.getLogger(ZhenxingMgr.class);
	private Map<Integer, KejiOpen> zxMap = new HashMap<Integer, KejiOpen>();

	private ZhenxingMgr() {
	}

	public static ZhenxingMgr getInstance() {
		if (null == zhenxingMgr) {
			zhenxingMgr = new ZhenxingMgr();
		}
		return zhenxingMgr;
	}

	public void initData() {
		logger.info("ZhenxingMgr initData");
		Map<Integer, KejiOpen> zxMap = new HashMap<Integer, KejiOpen>();
		Map<Integer, KejiOpen> tecMap = TecMgr.getInstance().kejiOpenMap;
		for (Integer key : tecMap.keySet()) {
			KejiOpen tmp = tecMap.get(key);
			if (tmp.getFormation() == 1) {// 是阵型
				zxMap.put(key, tmp);
			}
		}
		this.zxMap = zxMap;
	}

	public void initZhenxingInfo(long userid) {
		JunZhu junZhu = HibernateUtil.find(JunZhu.class, userid);
		List<Integer> zxIds = new ArrayList<Integer>(zxMap.keySet());
		Collections.sort(zxIds, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1 - o2;
			}
		});
		for (Integer key : zxIds) {
			ZhenXingInfo zhenXing = new ZhenXingInfo();
			zhenXing.setId(TableIDCreator.getTableID(ZhenXingInfo.class, 1));
			zhenXing.setUserid(userid);
			zhenXing.setZx1(0);
			zhenXing.setZx2(0);
			zhenXing.setZx3(0);
			zhenXing.setZx4(0);
			zhenXing.setZx5(0);
			zhenXing.setZx6(0);
			zhenXing.setZx7(0);
			zhenXing.setZx8(0);
			zhenXing.setZx9(0);
			zhenXing.setZxId(key);
			HibernateUtil.save(zhenXing);
		}
		junZhu.zxId = zxIds.get(0);
		logger.info("君主{}初始化阵型信息", userid);
	}

	/**
	 * @Title: queryZhenxing
	 * @Description: 查询阵型
	 * @param ctx
	 * @param userid
	 *            void
	 * @throws
	 */
	public void queryZhenxing(ChannelHandlerContext ctx, long userid) {
		BuildingInfo buildingInfo = HibernateUtil.find(BuildingInfo.class,
				userid);
		int junjichuLv = buildingInfo.getJunjichu();
		List<ZhenXingInfo> zhenXingInfos = HibernateUtil.list(
				ZhenXingInfo.class, "where userid=" + userid + "");
		JSONArray ret = new JSONArray();
		for (ZhenXingInfo zhenXingInfo : zhenXingInfos) {
			JSONArray zxJson = new JSONArray();
			if (zxMap.get((int) zhenXingInfo.getZxId()).getDepartmentLV() <= junjichuLv) {
				zxJson.add(zhenXingInfo.getId());
				zxJson.add(zhenXingInfo.getZx1());
				zxJson.add(zhenXingInfo.getZx2());
				zxJson.add(zhenXingInfo.getZx3());
				zxJson.add(zhenXingInfo.getZx4());
				zxJson.add(zhenXingInfo.getZx5());
				zxJson.add(zhenXingInfo.getZx6());
				zxJson.add(zhenXingInfo.getZx7());
				zxJson.add(zhenXingInfo.getZx8());
				zxJson.add(zhenXingInfo.getZx9());
				ret.add(zxJson);
			}
		}
		HttpHandler.writeJSON(ctx, ret);
	}

	/**
	 * @Title: setZx
	 * @Description: 阵型设置
	 * @param ctx
	 * @param msg
	 * @param userid
	 *            void
	 * @throws
	 */
	public void setZx(ChannelHandlerContext ctx, ProtoMessage msg, long userid) {
		JunZhu junZhu = HibernateUtil.find(JunZhu.class, userid);
		int zxid = msg.getData().getIntValue("zxid");
		ZhenXingInfo zhenXingInfo = HibernateUtil
				.find(ZhenXingInfo.class, zxid);
		if (zhenXingInfo == null) {
			logger.error("阵型{}不存在", zxid);
			return;
		}
		JSONArray cardIds = msg.getData().getJSONArray("cardids");
		junZhu.zxId = zxid;
		zhenXingInfo.setZx1(cardIds.getIntValue(0));
		zhenXingInfo.setZx2(cardIds.getIntValue(1));
		zhenXingInfo.setZx3(cardIds.getIntValue(2));
		zhenXingInfo.setZx4(cardIds.getIntValue(3));
		zhenXingInfo.setZx5(cardIds.getIntValue(4));
		zhenXingInfo.setZx6(cardIds.getIntValue(5));
		zhenXingInfo.setZx7(cardIds.getIntValue(6));
		zhenXingInfo.setZx8(cardIds.getIntValue(7));
		zhenXingInfo.setZx9(cardIds.getIntValue(8));
		HibernateUtil.save(zhenXingInfo);
		HibernateUtil.save(junZhu);
		HttpHandler.writeJSON(ctx, ProtoMessage.getSuccessResp());
	}
}
