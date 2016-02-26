package com.kidbear._36.manager.equip;

import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.kidbear._36.manager.junzhu.JunZhu;
import com.kidbear._36.net.http.HttpHandler;
import com.kidbear._36.template.Equip;
import com.kidbear._36.util.csv.TempletService;
import com.kidbear._36.util.hibernate.HibernateUtil;

public class EquipMgr {
	private static EquipMgr equipMgr;
	private static final Logger logger = LoggerFactory
			.getLogger(EquipMgr.class);
	public Map<Integer, Equip> equipMap = new HashMap<Integer, Equip>();

	private EquipMgr() {
	}

	public static EquipMgr getInstance() {
		if (null == equipMgr) {
			equipMgr = new EquipMgr();
		}
		return equipMgr;
	}

	public void initData() {
		logger.info("EquipMgr initData");
		List<Equip> equipList = TempletService.listAll(Equip.class
				.getSimpleName());
		Map<Integer, Equip> equipMap = new HashMap<Integer, Equip>();
		for (Equip equip : equipList) {
			equipMap.put(equip.getEquipID(), equip);
		}
		this.equipMap = equipMap;
	}

	public void queryEquip(ChannelHandlerContext ctx, long userid) {
		JSONArray equipArray = queryEquip(userid);
		HttpHandler.writeJSON(ctx, equipArray);
	}

	public JSONArray queryEquip(long userid) {
		JSONArray equipArray = new JSONArray();
		List<EquipInfo> equips = HibernateUtil.list(EquipInfo.class,
				"where userid=" + userid + "");
		for (EquipInfo equipInfo : equips) {
			Equip equip = equipMap.get(equipInfo.getEquipId());
			JSONArray equipJson = new JSONArray();
			equipJson.add(equipInfo.getId());
			equipJson.add(equip.getEquipID());
			equipJson.add(equip.getEquipName());
			equipJson.add(equipInfo.getStar());
			equipJson.add(equip.getEquipQuality());
			equipArray.add(equipJson);
		}
		return equipArray;
	}

}
