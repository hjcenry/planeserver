package com.kidbear._36.manager.pve;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.kidbear._36.manager.bag.BagMgr;
import com.kidbear._36.manager.bag.ItemCode;
import com.kidbear._36.net.ProtoMessage;
import com.kidbear._36.net.http.HttpHandler;
import com.kidbear._36.util.hibernate.HibernateUtil;
import com.kidbear._36.util.redis.Redis;

public class PveMgr {
	private static PveMgr pveMgr;
	private static final Logger logger = LoggerFactory.getLogger(PveMgr.class);
	private Redis redis = Redis.getInstance();
	public static final String CHAPTER_CACHE = "chapter_cache_";

	private PveMgr() {
	}

	public static PveMgr getInstance() {
		if (null == pveMgr) {
			pveMgr = new PveMgr();
		}
		return pveMgr;
	}

	public void initData() {
		logger.info("PveMgr initData");
	}

	public void initPveInfo(long userid) {
		PveChapterInfo pveChapterInfo = new PveChapterInfo();
		redis.set(CHAPTER_CACHE + userid, JSON.toJSONString(pveChapterInfo));
		logger.info("君主{}初始化Pve信息", userid);
	}

	public void battleOver(ChannelHandlerContext ctx, ProtoMessage msg,
			long userid) {
		int levelId = msg.getData().getIntValue("levelid");
		// TODO
		PveInfo pveInfo = HibernateUtil.find(PveInfo.class, "where userid="
				+ userid + " and levelId=" + levelId + "");
		if (pveInfo == null) {
			pveInfo = new PveInfo();
		}
		pveInfo.setStar(3);
		pveInfo.setLevelId(1);
		pveInfo.setChapter(1);
		HibernateUtil.save(pveInfo);
		// 存储章节信息
		PveChapterInfo pveChapterInfo = JSON.parseObject(
				redis.get(CHAPTER_CACHE + userid), PveChapterInfo.class);
		PveAwardInfo pveAwardInfo = pveChapterInfo.getChapter().get(
				pveInfo.getChapter());
		if (pveAwardInfo == null) {
			pveAwardInfo = new PveAwardInfo();
		}
		pveAwardInfo.setBx1(0);
		pveAwardInfo.setBx2(0);
		pveAwardInfo.setBx3(0);
		pveAwardInfo.setStarSum(pveAwardInfo.getStarSum() + pveInfo.getStar());
		pveChapterInfo.getChapter().put(pveInfo.getChapter(), pveAwardInfo);
		redis.set(CHAPTER_CACHE + userid, JSON.toJSONString(pveChapterInfo));
	}

	/**
	 * @Title: queryPve
	 * @Description: 查询pve
	 * @param ctx
	 * @param userid
	 *            void
	 * @throws
	 */
	public void queryPve(ChannelHandlerContext ctx, long userid) {
		List<PveInfo> pveInfos = HibernateUtil.list(PveInfo.class,
				"where userid=" + userid + " order by levelId ASC");
		// 分章节查询
		Map<Integer, List<Integer>> chapterMap = new HashMap<Integer, List<Integer>>();
		for (PveInfo pveInfo : pveInfos) {
			List<Integer> pveList = chapterMap.get(pveInfo.getChapter());
			if (pveList == null) {
				pveList = new ArrayList<Integer>();
			}
			pveList.add(pveInfo.getStar());
			chapterMap.put(pveInfo.getChapter(), pveList);
		}
		JSONArray ret = new JSONArray();
		// 获取章节信息
		PveChapterInfo pveChapterInfo = JSON.parseObject(
				redis.get(CHAPTER_CACHE + userid), PveChapterInfo.class);
		for (Integer key : chapterMap.keySet()) {
			JSONArray chapterJson = new JSONArray();
			PveAwardInfo pveAwardInfo = pveChapterInfo.getChapter().get(key);
			// Integer[] pves = new Integer[chapterMap.keySet().size()];
			// pves = chapterMap.get(key).toArray(pves);
			chapterJson.add(pveAwardInfo.getBx1());
			chapterJson.add(pveAwardInfo.getBx2());
			chapterJson.add(pveAwardInfo.getBx3());
			chapterJson.add(chapterMap.get(key));
			ret.add(chapterJson);
		}
		HttpHandler.writeJSON(ctx, ret);
	}

	/**
	 * @Title: pickPveAward
	 * @Description: 领取PVE宝箱
	 * @param ctx
	 * @param msg
	 * @param userid
	 *            void
	 * @throws
	 */
	public void pickPveAward(ChannelHandlerContext ctx, ProtoMessage msg,
			long userid) {
		int chapter = msg.getData().getIntValue("chapter");
		int type = msg.getData().getIntValue("type");
		PveChapterInfo pveChapterInfo = JSON.parseObject(
				redis.get(CHAPTER_CACHE + userid), PveChapterInfo.class);
		PveAwardInfo pveAwardInfo = pveChapterInfo.getChapter().get(chapter);
		switch (type) {
		case 1:
			if (pveAwardInfo.getBx1() == 0) {
				logger.error("君主{}领取宝箱失败，宝箱已被领取", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("宝箱已被领取"));
				return;
			}
			pveAwardInfo.setBx1(1);
			break;
		case 2:
			if (pveAwardInfo.getBx1() == 0) {
				logger.error("君主{}领取宝箱失败，宝箱已被领取", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("宝箱已被领取"));
				return;
			}
			pveAwardInfo.setBx2(1);
			break;
		case 3:
			if (pveAwardInfo.getBx1() == 0) {
				logger.error("君主{}领取宝箱失败，宝箱已被领取", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("宝箱已被领取"));
				return;
			}
			pveAwardInfo.setBx3(1);
			break;
		}
		// 发放奖励
		BagMgr.getInstance().addItem(ItemCode.COIN, 0, 2000, userid);
		pveChapterInfo.getChapter().put(chapter, pveAwardInfo);
		redis.set(CHAPTER_CACHE + userid, JSON.toJSONString(pveChapterInfo));
		HttpHandler.writeJSON(ctx, ProtoMessage.getSuccessResp());
	}
}
