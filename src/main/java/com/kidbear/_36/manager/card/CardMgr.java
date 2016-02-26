package com.kidbear._36.manager.card;

import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kidbear._36.manager.bag.BagMgr;
import com.kidbear._36.manager.bag.ItemCode;
import com.kidbear._36.manager.building.BuildingInfo;
import com.kidbear._36.manager.building.BuildingMgr;
import com.kidbear._36.manager.event.ED;
import com.kidbear._36.manager.event.EventMgr;
import com.kidbear._36.manager.junzhu.JunZhu;
import com.kidbear._36.manager.task.TrainInfo;
import com.kidbear._36.manager.task.TrainMgr;
import com.kidbear._36.manager.task.TrainQueue;
import com.kidbear._36.net.ProtoMessage;
import com.kidbear._36.net.ResultCode;
import com.kidbear._36.net.http.HttpHandler;
import com.kidbear._36.template.Card;
import com.kidbear._36.template.CardExp;
import com.kidbear._36.util.csv.TempletService;
import com.kidbear._36.util.hibernate.HibernateUtil;
import com.kidbear._36.util.redis.Redis;

/**
 * @ClassName: CardMgr
 * @Description: TODO
 * @author 何金成
 * @date 2016年2月26日 上午11:06:03
 * 
 */
public class CardMgr {
	private static CardMgr cardMgr;
	private static final Logger logger = LoggerFactory.getLogger(CardMgr.class);
	public static int cardMaxFreeTime = 5;// 免费抽卡次数上限
	public static long cardFreeCD = 5 * 60 * 1000;// 免费抽卡CD
	public static int cardCostYuanbao = 10;// 抽卡消耗元宝
	public static int cardCostJianghun = 10;// 抽卡消耗将魂
	public static int card10CostYuanbao = 100;// 十连抽卡消耗元宝
	public static int bhVip = 5;// 普通强化vip保护等级
	public static final String CARD_FREE_CACHE = "card_free_cache_";// 免费抽卡次数
	public static final String CARD_DECOMPOSE_CACHE = "card_decompose_cache_";// 分解次数
	public Redis redis = Redis.getInstance();
	public Map<Integer, Card> cardMap = new HashMap<Integer, Card>();
	public Map<Integer, CardExp> cardLevelMap = new HashMap<Integer, CardExp>();
	public Map<Long, CardExp> cardExpMap = new HashMap<Long, CardExp>();
	public List<Integer> pickCardList = new ArrayList<Integer>();
	public List<Integer> pickEquipList = new ArrayList<Integer>();
	public Map<String, Integer> cardEquipMap = new HashMap<String, Integer>();
	public Map<Integer, Double> starPropertyMap = new HashMap<Integer, Double>();
	public Map<Integer, Double> qualityPropertyMap = new HashMap<Integer, Double>();
	public Map<Integer, String> upgradeStarMap = new HashMap<Integer, String>();

	private CardMgr() {
	}

	public static CardMgr getInstance() {
		if (null == cardMgr) {
			cardMgr = new CardMgr();
		}
		return cardMgr;
	}

	public void initData() {
		logger.info("CardMgr initData");
		List<Card> cardList = TempletService
				.listAll(Card.class.getSimpleName());
		Map<Integer, Card> cardMap = new HashMap<Integer, Card>();
		for (Card card : cardList) {
			cardMap.put(card.getCardId(), card);
		}
		this.cardMap = cardMap;
		// 抽卡卡库
		List<Integer> pickCardList = new ArrayList<Integer>();
		for (int i = 1; i <= 5; i++) {
			int cardId = 200000 + i;
			pickCardList.add(cardId);
		}
		this.pickCardList = pickCardList;
		// 抽卡装备库
		List<Integer> pickEquipList = new ArrayList<Integer>();
		for (int i = 1; i <= 20; i++) {
			int equipId = i;
			pickEquipList.add(equipId);
		}
		this.pickEquipList = pickEquipList;
		// 十连抽卡牌与装备比例
		Map<String, Integer> cardEquipMap = new HashMap<String, Integer>();
		cardEquipMap.put("card", 30);
		cardEquipMap.put("equip", 70);
		this.cardEquipMap = cardEquipMap;
		// 星级成功率
		Map<Integer, Double> starPropertyMap = new HashMap<Integer, Double>();
		starPropertyMap.put(1, 5d);
		starPropertyMap.put(2, 2.42);
		starPropertyMap.put(3, 1.57);
		starPropertyMap.put(4, 1.14);
		starPropertyMap.put(5, 0.88);
		starPropertyMap.put(6, 0.71);
		starPropertyMap.put(7, 0.59);
		starPropertyMap.put(8, 0.5);
		starPropertyMap.put(9, 0.42);
		starPropertyMap.put(10, 0.37);
		starPropertyMap.put(11, 0.32);
		starPropertyMap.put(12, 0.28);
		starPropertyMap.put(13, 0.25);
		starPropertyMap.put(14, 0.22);
		starPropertyMap.put(15, 0.2);
		starPropertyMap.put(16, 0.17);
		starPropertyMap.put(17, 0.15);
		starPropertyMap.put(18, 0.14);
		starPropertyMap.put(19, 0.12);
		starPropertyMap.put(20, 0.11);
		this.starPropertyMap = starPropertyMap;
		// 品质成功率
		Map<Integer, Double> qualityPropertyMap = new HashMap<Integer, Double>();
		qualityPropertyMap.put(1, 1.5);
		qualityPropertyMap.put(2, 1.33);
		qualityPropertyMap.put(3, 1.16);
		qualityPropertyMap.put(4, 0.99);
		qualityPropertyMap.put(5, 0.82);
		qualityPropertyMap.put(6, 0.65);
		qualityPropertyMap.put(7, 0.48);
		qualityPropertyMap.put(8, 0.31);
		this.qualityPropertyMap = qualityPropertyMap;
		// 升星强化消耗
		Map<Integer, String> upgradeStarMap = new HashMap<Integer, String>();
		upgradeStarMap.put(1, "5000#20");
		upgradeStarMap.put(2, "10000#60");
		upgradeStarMap.put(3, "30000#100");
		upgradeStarMap.put(4, "30000#100");
		upgradeStarMap.put(5, "50000#200");
		upgradeStarMap.put(6, "50000#200");
		upgradeStarMap.put(7, "50000#200");
		upgradeStarMap.put(8, "50000#200");
		this.upgradeStarMap = upgradeStarMap;
		// 卡牌等级表 卡牌经验表
		Map<Integer, CardExp> cardLevelMap = new HashMap<Integer, CardExp>();
		Map<Long, CardExp> cardExpMap = new HashMap<Long, CardExp>();
		List<CardExp> cardExps = TempletService.listAll(CardExp.class
				.getSimpleName());
		for (CardExp cardExp : cardExps) {
			cardLevelMap.put(cardExp.getHeroLv(), cardExp);
			cardExpMap.put(cardExp.getSumExp(), cardExp);
		}
		this.cardLevelMap = cardLevelMap;
		this.cardExpMap = cardExpMap;
	}

	public void initCardInfo(long userid) {
		CardFreeInfo cardFreeInfo = new CardFreeInfo();
		cardFreeInfo.setFreeTimes(cardMaxFreeTime);
		cardFreeInfo.setPicktime(0);
		cardFreeInfo.setResettime(System.currentTimeMillis());
		redis.set(CARD_FREE_CACHE + userid, JSON.toJSONString(cardFreeInfo));
		// 分解次数领取礼包初始化
		DecomposeInfo decomposeInfo = new DecomposeInfo();
		decomposeInfo.setPickTimes(0);
		decomposeInfo.setDecomposeTimes(0);
		decomposeInfo.setResettime(0);
		redis.set(CARD_DECOMPOSE_CACHE + userid,
				JSON.toJSONString(decomposeInfo));
		logger.info("君主{}初始化卡牌信息", userid);
	}

	/**
	 * @Title: trainCard
	 * @Description: 训练卡牌
	 * @param ctx
	 * @param msg
	 * @param userid
	 *            void
	 * @throws
	 */
	public void trainCard(ChannelHandlerContext ctx, ProtoMessage msg,
			long userid) {
		long cardId = msg.getData().getLong("cardid");
		int type = msg.getData().getIntValue("type");
		int trainPos = msg.getData().getIntValue("train");
		if (!TrainMgr.getInstance().addCardToTrain(
				userid,
				cardId,
				Integer.parseInt(TrainMgr.getInstance().trainConditionMap.get(
						type).split("#")[0]) * 60 * 60 * 1000, trainPos, type)) {
			logger.error("君主{}训练卡牌失败,无可用训练队列", userid);
			HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("无可用训练队列"));
			return;
		}
		HttpHandler.writeJSON(ctx, ProtoMessage.getSuccessResp());
	}

	/**
	 * @Title: pickTrainCard
	 * @Description: 领取训练完成的卡牌
	 * @param ctx
	 * @param msg
	 * @param userid
	 *            void
	 * @throws
	 */
	public void pickTrainCard(ChannelHandlerContext ctx, ProtoMessage msg,
			long userid) {
		int trainPos = msg.getData().getIntValue("train");
		TrainQueue queue = JSON.parseObject(
				redis.get(TrainMgr.TRAIN_CACHE + userid), TrainQueue.class);
		TrainInfo trainInfo = queue.getTrain().get(trainPos);
		if (!trainInfo.getIsopen()) {
			logger.error("君主{}领取训练卡牌失败,训练位置未开放", userid);
			HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("训练位置未开放"));
			return;
		}
		if (trainInfo.getCardDBId() == 0) {
			logger.error("君主{}领取训练卡牌失败,没有卡牌可领取", userid);
			HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("没有卡牌可领取"));
			return;
		}
		BuildingInfo buildingInfo = HibernateUtil.find(BuildingInfo.class,
				userid);
		long cardId = trainInfo.getCardDBId();
		int type = trainInfo.getType();
		CardInfo cardInfo = HibernateUtil.find(CardInfo.class, cardId);
		int hours = Integer.parseInt(TrainMgr.getInstance().trainConditionMap
				.get(type).split("#")[0]);
		int expMulti = Integer
				.parseInt(TrainMgr.getInstance().trainConditionMap.get(type)
						.split("#")[1]);
		int per4Hour = BuildingMgr.getInstance().jiaochangMap.get(
				buildingInfo.getJiaochang()).getFourHGetExp();
		int tufeiExp = trainInfo.getTufeiTimes()
				* BuildingMgr.getInstance().jiaochangMap.get(
						buildingInfo.getJiaochang()).getTuFeiGetExp();
		long exp = per4Hour * (hours / 4) * (expMulti / 100) + tufeiExp;
		cardInfo.setExp(cardInfo.getExp() + exp);
		while (true) {// 等级累加
			if (cardLevelMap.get(cardInfo.getLevel()).getSumExp() <= cardInfo
					.getExp()) {
				cardInfo.setLevel(cardInfo.getLevel() + 1);
			} else {
				break;
			}
		}
		JSONArray ret = new JSONArray();
		ret.add(cardId);
		ret.add(cardInfo.getExp());
		ret.add(exp);
		ret.add(cardInfo.getLevel());
		trainInfo.setCardDBId(0);
		trainInfo.setCardId(0);
		queue.getTrain().put(trainPos, trainInfo);
		HibernateUtil.save(cardInfo);
		redis.set(TrainMgr.TRAIN_CACHE + userid, JSON.toJSONString(queue));
		HttpHandler.writeJSON(ctx, ret);
	}

	/**
	 * @Title: tufeiTrain
	 * @Description: 突飞卡牌训练
	 * @param ctx
	 * @param msg
	 * @param userid
	 *            void
	 * @throws
	 */
	public void tufeiTrain(ChannelHandlerContext ctx, ProtoMessage msg,
			long userid) {
		JunZhu junZhu = HibernateUtil.find(JunZhu.class, userid);
		BuildingInfo buildingInfo = HibernateUtil.find(BuildingInfo.class,
				userid);
		int trainPos = msg.getData().getIntValue("train");
		TrainQueue queue = JSON.parseObject(
				redis.get(TrainMgr.TRAIN_CACHE + userid), TrainQueue.class);
		TrainInfo trainInfo = queue.getTrain().get(trainPos);
		if (!trainInfo.getIsopen()) {
			logger.error("君主{}突飞训练失败,训练位置未开放", userid);
			HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("训练位置未开放"));
			return;
		}
		if (trainInfo.getCardDBId() == 0) {
			logger.error("君主{}突飞训练失败,没有卡牌可领取", userid);
			HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("没有卡牌可突飞"));
			return;
		}
		int jungongNum = BuildingMgr.getInstance().jiaochangMap.get(
				buildingInfo.getJiaochang()).getTuFeiZhangongCost();
		if (junZhu.jungongNum < jungongNum) {
			logger.error("君主{}突飞训练失败,军功不足", userid);
			HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("军功不足"));
			return;
		}
		junZhu.jungongNum -= jungongNum;
		trainInfo.setTufeiTimes(trainInfo.getTufeiTimes() + 1);
		queue.getTrain().put(trainPos, trainInfo);
		redis.set(TrainMgr.TRAIN_CACHE + userid, JSON.toJSONString(queue));
		HibernateUtil.save(junZhu);
		HttpHandler.writeJSON(ctx, ProtoMessage.getSuccessResp());
	}

	/**
	 * @Title: queryTrain
	 * @Description: 查询训练队列
	 * @param ctx
	 * @param userid
	 *            void
	 * @throws
	 */
	public void queryTrain(ChannelHandlerContext ctx, long userid) {
		JSONArray ret = TrainMgr.getInstance().getTrainQueue(userid);
		ret.add(System.currentTimeMillis());
		HttpHandler.writeJSON(ctx, ret);
	}

	public void upgradeStar(ChannelHandlerContext ctx, ProtoMessage msg,
			long userid) {
		JunZhu junZhu = HibernateUtil.find(JunZhu.class, userid);
		long card1Id = msg.getData().getLong("card1");
		long card2Id = msg.getData().getLong("card2");
		int sxtype = msg.getData().getIntValue("sxtype");
		logger.info("卡牌1：{}和卡牌2：{}升星合成,君主金币{},需要金币{}", card1Id, card2Id,
				junZhu.coin);
		CardInfo card1Info = HibernateUtil.find(CardInfo.class, card1Id);
		CardInfo card2Info = HibernateUtil.find(CardInfo.class, card2Id);
		if (card1Info.getCardId() != card2Info.getCardId()) {
			logger.error("君主{}升星卡牌{}失败，卡牌种类不同", userid, card1Id);
			HttpHandler.writeJSON(ctx, ProtoMessage.getEmptyResp());
			return;
		}
		int card1Quality = cardMap.get(card1Info.getCardId()).getRMBID();
		int card2Quality = cardMap.get(card2Info.getCardId()).getRMBID();
		if (card1Quality != card2Quality) {
			logger.error("君主{}升星卡牌{}失败，卡牌品质不同", userid, card1Id);
			HttpHandler.writeJSON(ctx, ProtoMessage.getEmptyResp());
			return;
		}
		int newStar = card1Info.getStar() + card2Info.getStar();
		int newQuality = card1Quality;
		int resource = Integer.parseInt(upgradeStarMap.get(newQuality).split(
				"#")[sxtype]);
		logger.info("需要金币{}", resource);
		switch (sxtype) {
		case 0:// 普通升星
			if (junZhu.coin < resource) {
				logger.error("君主{}卡牌{}升星失败，金币不足", userid, card1Id);
				HttpHandler.writeJSON(ctx,
						ProtoMessage.getErrorResp(ResultCode.COIN_ERR));
				return;
			}
			junZhu.coin -= resource;
			break;
		case 1:// 保护升星
			if (junZhu.yuanbao < resource) {
				logger.error("君主{}卡牌{}升星失败，元宝不足", userid, card1Id);
				HttpHandler.writeJSON(ctx,
						ProtoMessage.getErrorResp(ResultCode.YUANBAO_ERR));
				return;
			}
			junZhu.yuanbao -= resource;
			break;
		}
		// 成功率 = （3/星级成功率系数）* 品质成功率系数 + 品质满星系数
		// 品质满星系数 = （材料星级之和 - 品质满星）*0.02，若<0，则取0
		logger.info("合成星级{}，品质{}", newStar, newQuality);
		int qualityMax = 0;
		if (newQuality == 1) {
			qualityMax = 5;
		} else if (newQuality == 2) {
			qualityMax = 10;
		} else if (newQuality == 3 || newQuality == 4) {
			qualityMax = 15;
		} else {
			qualityMax = 20;
		}
		double qualityProperty = (newStar - qualityMax) * 0.02;
		qualityProperty = (qualityProperty < 0) ? 0 : qualityProperty;
		double successProperty = (starPropertyMap.get(newStar))
				* qualityPropertyMap.get(newQuality) + qualityProperty;
		int randomNum = (int) Math.round(Math.random() * 100);
		logger.info("品质慢性系数{}，成功率{}", qualityProperty, successProperty);
		boolean success = (randomNum <= 100 * successProperty) ? true : false;
		JSONArray ret = new JSONArray();
		logger.info("sxtype:{},vip:{},needVip:{}", sxtype, junZhu.vip, bhVip);
		if (success) {// 强化成功
			long newExp = card1Info.getExp() + card2Info.getExp();
			card1Info.setExp(newExp);
			card1Info.setLevel(cardExpMap.get(newExp).getHeroLv());
			card1Info.setStar(newStar);
			ret.add(card1Info.getId());
			ret.add(card1Info.getCardId());
			ret.add(card1Info.getStar());
			HibernateUtil.save(card1Info);
			HibernateUtil.save(junZhu);
			EventMgr.addEvent(ED.BAG_REM_ITEM, new Object[] { 1, card2Id });
		} else if (sxtype == 0 && junZhu.vip < bhVip) {// 强化失败,
														// 普通强化并且vip等级也不够，消耗材料
			BagMgr.getInstance().removeItem(1, card1Id);
			BagMgr.getInstance().removeItem(1, card2Id);
		}
		HttpHandler.writeJSON(ctx, ret);
	}

	/**
	 * @Title: decompose
	 * @Description: 卡牌分解
	 * @param ctx
	 * @param msg
	 * @param userid
	 *            void
	 * @throws
	 */
	public void decompose(ChannelHandlerContext ctx, ProtoMessage msg,
			long userid) {
		JSONArray cardIds = msg.getData().getJSONArray("cardids");
		JSONArray ret = new JSONArray();
		for (Object obj : cardIds) {
			long cardId = -1;
			if (obj instanceof Integer) {
				cardId = (Integer) obj;
			} else if (obj instanceof Long) {
				cardId = (Long) obj;
			}
			if (cardId != -1) {
				JSONArray awardJson = new JSONArray();
				CardInfo cardInfo = HibernateUtil.find(CardInfo.class, cardId);
				if (cardInfo == null) {
					logger.error("卡牌分解失败，卡牌{}不存在", cardId);
					continue;
				}
				BagMgr.getInstance().removeItem(1, cardId);
				// TODO 分解奖励暂定一半几率2000将魂一半几率2000金币
				int randomNum = (int) Math.round(Math.random() * 100);
				if (randomNum <= 50) {
					BagMgr.getInstance()
							.addItem(ItemCode.COIN, 0, 2000, userid);
					awardJson.add(ItemCode.COIN);
					awardJson.add(2000);
				} else {
					BagMgr.getInstance().addItem(ItemCode.JIANG_HUN, 0, 2000,
							userid);
					awardJson.add(ItemCode.JIANG_HUN);
					awardJson.add(2000);
				}
				ret.add(awardJson);
			}
		}
		int count = cardIds.size();
		// 添加分解次数
		DecomposeInfo decomposeInfo = JSON.parseObject(
				redis.get(CARD_DECOMPOSE_CACHE + userid), DecomposeInfo.class);
		if (new Date().getDate() != new Date(decomposeInfo.getResettime())
				.getDate()) {// 第二天，重置
			decomposeInfo.setResettime(System.currentTimeMillis());
			decomposeInfo.setPickTimes(0);
			decomposeInfo.setDecomposeTimes(0);
		}
		decomposeInfo.setDecomposeTimes(decomposeInfo.getDecomposeTimes()
				+ count);
		redis.set(CARD_DECOMPOSE_CACHE + userid,
				JSON.toJSONString(decomposeInfo));
		HttpHandler.writeJSON(ctx, ret);
	}

	/**
	 * @Title: queryCardDecompose
	 * @Description: 查询分解次数领奖情况
	 * @param ctx
	 * @param userid
	 *            void
	 * @throws
	 */
	public void queryCardDecompose(ChannelHandlerContext ctx, long userid) {
		DecomposeInfo decomposeInfo = JSON.parseObject(
				redis.get(CARD_DECOMPOSE_CACHE + userid), DecomposeInfo.class);
		if (new Date().getDate() != new Date(decomposeInfo.getResettime())
				.getDate()) {// 第二天，重置
			decomposeInfo.setResettime(System.currentTimeMillis());
			decomposeInfo.setPickTimes(0);
			decomposeInfo.setDecomposeTimes(0);
		}
		JSONArray ret = new JSONArray();
		ret.add(decomposeInfo.getDecomposeTimes());
		ret.add(decomposeInfo.getPickTimes());
		HttpHandler.writeJSON(ctx, ret);
	}

	/**
	 * @Title: pickCardDecompose
	 * @Description: 领取分解奖励
	 * @param ctx
	 * @param userid
	 *            void
	 * @throws
	 */
	public void pickCardDecompose(ChannelHandlerContext ctx, long userid) {
		JunZhu junZhu = HibernateUtil.find(JunZhu.class, userid);
		DecomposeInfo decomposeInfo = JSON.parseObject(
				redis.get(CARD_DECOMPOSE_CACHE + userid), DecomposeInfo.class);
		// TODO 分解领奖，暂定2000金币
		decomposeInfo.setPickTimes(decomposeInfo.getPickTimes() + 1);
		junZhu.coin += 2000;
		redis.set(CARD_DECOMPOSE_CACHE + userid,
				JSON.toJSONString(decomposeInfo));
		HibernateUtil.save(junZhu);
		JSONArray ret = new JSONArray();
		ret.add(ItemCode.COIN);
		ret.add(2000);
		HttpHandler.writeJSON(ctx, ret);
	}

	/**
	 * @Title: queryCard
	 * @Description: 查询卡牌
	 * @param ctx
	 * @param userid
	 *            void
	 * @throws
	 */
	public void queryCard(ChannelHandlerContext ctx, long userid) {
		JSONArray cardArray = queryCard(userid);
		HttpHandler.writeJSON(ctx, cardArray);
	}

	/**
	 * @Title: queryCard
	 * @Description: 查询卡牌
	 * @param userid
	 * @return JSONArray
	 * @throws
	 */
	public JSONArray queryCard(long userid) {
		JSONArray cardArray = new JSONArray();
		List<CardInfo> cards = HibernateUtil.list(CardInfo.class,
				"where userid=" + userid + "");
		for (CardInfo cardInfo : cards) {
			Card card = cardMap.get(cardInfo.getCardId());
			JSONArray cardJson = new JSONArray();
			cardJson.add(cardInfo.getId());
			cardJson.add(card.getCardId());
			cardJson.add(card.getCardName());
			cardJson.add(cardInfo.getLevel());
			cardJson.add(cardInfo.getStar());
			cardJson.add(card.getRMBID());
			cardArray.add(cardJson);
		}
		return cardArray;
	}

	/**
	 * @Title: queryCardFree
	 * @Description: 查询免费抽卡
	 * @param ctx
	 * @param userid
	 *            void
	 * @throws
	 */
	public void queryCardFreePick(ChannelHandlerContext ctx, long userid) {
		CardFreeInfo cardFreeInfo = JSON.parseObject(
				redis.get(CARD_FREE_CACHE + userid), CardFreeInfo.class);
		JSONObject ret = new JSONObject();
		int free = cardFreeInfo.getFreeTimes();
		long during = System.currentTimeMillis() - cardFreeInfo.getPicktime();
		ret.put("free", free);
		if (during >= cardFreeCD) {
			ret.put("time", 0);
		} else {
			ret.put("time", cardFreeCD - during);
		}
		HttpHandler.writeJSON(ctx, ret);
	}

	/**
	 * @Title: pickCard
	 * @Description: 抽卡
	 * @param ctx
	 * @param userid
	 *            void
	 * @throws
	 */
	public void pickCard(ChannelHandlerContext ctx, ProtoMessage msg,
			long userid) {
		JunZhu junZhu = HibernateUtil.find(JunZhu.class, userid);
		int cardType = msg.getData().getIntValue("cardtype");
		CardFreeInfo cardFreeInfo = JSON.parseObject(
				redis.get(CARD_FREE_CACHE + userid), CardFreeInfo.class);
		int free = cardFreeInfo.getFreeTimes();
		JSONArray ret = new JSONArray();
		switch (cardType) {
		case 1:// 免费/元宝抽卡
			if (free > 0) {// 免费抽卡
				long during = System.currentTimeMillis()
						- cardFreeInfo.getPicktime();
				if (during < cardFreeCD) {
					// logger.error("君主{}免费抽卡失败，CD中", userid);
					// HttpHandler.writeJSON(ctx,
					// ProtoMessage.getErrorResp("免费抽卡冷却中"));
					// return;
					if (junZhu.yuanbao < cardCostYuanbao) {
						logger.error("君主{}元宝抽卡失败，元宝不足", userid);
						HttpHandler.writeJSON(ctx, ret);
						return;
					}
					junZhu.yuanbao -= cardCostYuanbao;
				} else {
					cardFreeInfo.setFreeTimes(cardFreeInfo.getFreeTimes() - 1);
					cardFreeInfo.setPicktime(System.currentTimeMillis());
				}
			} else {// 元宝抽卡
				if (junZhu.yuanbao < cardCostYuanbao) {
					logger.error("君主{}元宝抽卡失败，元宝不足", userid);
					HttpHandler.writeJSON(ctx, ret);
					return;
				}
				junZhu.yuanbao -= cardCostYuanbao;
			}
			break;
		case 2:// 将魂抽卡
			if (junZhu.jianghunNum < cardCostJianghun) {
				logger.error("君主{}元宝抽卡失败，将魂不足", userid);
				HttpHandler.writeJSON(ctx, ret);
				return;
			}
			junZhu.jianghunNum -= cardCostJianghun;
			break;
		case 3:// 元宝十连抽
			if (junZhu.yuanbao < card10CostYuanbao) {
				logger.error("君主{}元宝十连抽卡失败，元宝不足", userid);
				HttpHandler.writeJSON(ctx, ret);
				return;
			}
			junZhu.yuanbao -= card10CostYuanbao;
			break;
		}
		// 发卡牌
		int count = (cardType == 3) ? 10 : 1;
		for (int i = 0; i < count; i++) {
			JSONArray cardArr = new JSONArray();
			int num = (int) (Math.round(Math.random() * 100));
			if (num < cardEquipMap.get("card")) {// 卡牌
				int index = (int) (Math.round(Math.random()
						* (pickCardList.size() - 1)));
				EventMgr.addEvent(ED.BAG_ADD_ITEM, new Object[] { 1,
						pickCardList.get(index), 1, userid });
				cardArr.add(pickCardList.get(index));
				cardArr.add(1);
			} else {// 装备
				int index = (int) (Math.round(Math.random()
						* (pickEquipList.size() - 1)));
				EventMgr.addEvent(ED.BAG_ADD_ITEM, new Object[] { 2,
						pickEquipList.get(index), 1, userid });
				cardArr.add(pickEquipList.get(index));
				cardArr.add(0);
			}
			ret.add(cardArr);
		}
		// 存库
		redis.set(CARD_FREE_CACHE + userid, JSON.toJSONString(cardFreeInfo));
		HibernateUtil.save(junZhu);
		HttpHandler.writeJSON(ctx, ret);
	}
}