package com.kidbear._36.manager.tec;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netty.channel.ChannelHandlerContext;

import org.hibernate.sql.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.kidbear._36.manager.junzhu.JunZhu;
import com.kidbear._36.net.ProtoMessage;
import com.kidbear._36.net.http.HttpHandler;
import com.kidbear._36.template.KejiOpen;
import com.kidbear._36.template.KejiUpgrade;
import com.kidbear._36.util.csv.TempletService;
import com.kidbear._36.util.hibernate.HibernateUtil;
import com.kidbear._36.util.hibernate.TableIDCreator;

public class TecMgr {
	private static TecMgr tecMgr;
	private static final Logger logger = LoggerFactory.getLogger(TecMgr.class);
	public Map<Integer, KejiUpgrade> kejiUpgradeMap = new HashMap<Integer, KejiUpgrade>();
	public Map<Integer, KejiOpen> kejiOpenMap = new HashMap<Integer, KejiOpen>();

	private TecMgr() {
	}

	public static TecMgr getInstance() {
		if (null == tecMgr) {
			tecMgr = new TecMgr();
		}
		return tecMgr;
	}

	public void initData() {
		/** 科技升级表 **/
		Map<Integer, KejiUpgrade> kejiUpgradeMap = new HashMap<Integer, KejiUpgrade>();
		List<KejiUpgrade> kejiUpgrades = TempletService
				.listAll(KejiUpgrade.class.getSimpleName());
		for (KejiUpgrade kejiUpgrade : kejiUpgrades) {
			kejiUpgradeMap.put(kejiUpgrade.getScienceLv(), kejiUpgrade);
		}
		this.kejiUpgradeMap = kejiUpgradeMap;
		/** 科技开放表 **/
		Map<Integer, KejiOpen> kejiOpenMap = new HashMap<Integer, KejiOpen>();
		List<KejiOpen> kejiOpens = TempletService.listAll(KejiOpen.class
				.getSimpleName());
		for (KejiOpen kejiOpen : kejiOpens) {
			kejiOpenMap.put(kejiOpen.getScienceID(), kejiOpen);
		}
		this.kejiOpenMap = kejiOpenMap;
	}

	public void initTecInfo(long userid) {
		for (int i = 1; i <= 30; i++) {
			Tec tec = new Tec();
			tec.setId(TableIDCreator.getTableID(Tec.class, 1));
			tec.setTec(i);
			tec.setUserid(1);
			tec.setLevel(1);
			tec.setIsOpen(0);
			HibernateUtil.save(tec);
		}
		logger.info("君主{}初始化所有科技", userid);
	}

	public void queryTec(ChannelHandlerContext ctx, long userid) {
		List<Tec> tecList = HibernateUtil.list(Tec.class, "where userid="
				+ userid + "");
		if (tecList.size() == 0) {
			initTecInfo(userid);
		}
		JSONArray tecArr = new JSONArray();
		Collections.sort(tecList, new Comparator<Tec>() {// 升序排序
					@Override
					public int compare(Tec o1, Tec o2) {
						return o1.getTec() - o2.getTec();
					}
				});
		for (Tec tec : tecList) {
			// JSONArray tecJson = new JSONArray();
			// tecJson.add(tec.getLevel());
			tecArr.add(tec.getLevel());
		}
		HttpHandler.writeJSON(ctx, tecArr);
	}

	public void upgrade(ChannelHandlerContext ctx, ProtoMessage msg, long userid) {
		JunZhu junZhu = HibernateUtil.find(JunZhu.class, userid);
		int keji = msg.getData().getIntValue("keji");
		KejiUpgrade kejiUpgrade = kejiUpgradeMap.get(keji);
		KejiOpen kejiOpen = kejiOpenMap.get(keji);
		int jungong = Math.round((kejiUpgrade.getZhangongCostBase() * kejiOpen
				.getLvupCostNum()));
		if (junZhu.jungongNum < jungong) {
			logger.error("君主{}升级科技失败，战功{}不足{}", userid, junZhu.jungongNum,
					jungong);
			HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("战功不足"));
			return;
		}
		Tec tec = HibernateUtil.find(Tec.class, "where userid=" + userid
				+ " and tec=" + keji + "");
		tec.setLevel(tec.getLevel() + 1);
		junZhu.jungongNum -= jungong;
		HibernateUtil.save(tec);
		HibernateUtil.save(junZhu);
		HttpHandler.writeJSON(ctx, ProtoMessage.getSuccessResp());
	}
}
