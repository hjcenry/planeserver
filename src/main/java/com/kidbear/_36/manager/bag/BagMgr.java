package com.kidbear._36.manager.bag;

import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kidbear._36.manager.card.CardInfo;
import com.kidbear._36.manager.card.CardMgr;
import com.kidbear._36.manager.equip.EquipInfo;
import com.kidbear._36.manager.equip.EquipMgr;
import com.kidbear._36.manager.event.ED;
import com.kidbear._36.manager.event.Event;
import com.kidbear._36.manager.event.EventMgr;
import com.kidbear._36.manager.event.EventProc;
import com.kidbear._36.manager.junzhu.JunZhu;
import com.kidbear._36.net.http.HttpHandler;
import com.kidbear._36.template.Card;
import com.kidbear._36.template.Equip;
import com.kidbear._36.template.Prop;
import com.kidbear._36.util.csv.TempletService;
import com.kidbear._36.util.hibernate.HibernateUtil;
import com.kidbear._36.util.hibernate.TableIDCreator;

public class BagMgr extends EventProc {
	private static BagMgr bagMgr;
	private static final Logger logger = LoggerFactory.getLogger(BagMgr.class);
	Map<Integer, Prop> propMap = new HashMap<Integer, Prop>();

	private BagMgr() {
	}

	public static BagMgr getInstance() {
		if (null == bagMgr) {
			bagMgr = new BagMgr();
		}
		return bagMgr;
	}

	public void initData() {
		logger.info("BagMgr initData");
		List<Prop> propList = TempletService
				.listAll(Prop.class.getSimpleName());
		Map<Integer, Prop> propMap = new HashMap<Integer, Prop>();
		for (Prop prop : propList) {
			propMap.put(prop.getItemID(), prop);
		}
		this.propMap = propMap;
	}

	/**
	 * @Title: removeItem
	 * @Description: 移除物品
	 * @param type
	 * @param itemId
	 *            void
	 * @throws
	 */
	public void removeItem(final int type, final long itemId) {
		switch (type) {
		case ItemCode.CARD:// 卡牌
			CardInfo cardInfo = HibernateUtil.find(CardInfo.class, itemId);
			if (cardInfo == null) {
				logger.error("移除物品{}失败,物品不存在", itemId);
				return;
			}
			List<EquipInfo> equipInfos = HibernateUtil.list(EquipInfo.class,
					"where cardId=" + itemId + "");
			for (EquipInfo equipInfo : equipInfos) {
				equipInfo.setCardId(0);
				HibernateUtil.save(equipInfo);
			}
			HibernateUtil.delete(cardInfo);
			break;
		case ItemCode.EQUIP:// 装备
			EquipInfo equipInfo = HibernateUtil.find(EquipInfo.class, itemId);
			if (equipInfo == null) {
				logger.error("移除物品{}失败,物品不存在", itemId);
				return;
			}
			HibernateUtil.delete(equipInfo);
			break;
		case ItemCode.CARD_SP:// 卡牌碎片
		case ItemCode.EQUIP_SP:// 卡牌碎片
		case ItemCode.CONSUME:// 消耗品
			ItemInfo itemInfo = HibernateUtil.find(ItemInfo.class, itemId);
			if (itemInfo == null) {
				logger.error("移除物品{}失败,物品不存在", itemId);
				return;
			}
			HibernateUtil.delete(itemInfo);
			break;
		}
	}

	/**
	 * @Title: addItem
	 * @Description: 添加物品
	 * @param type
	 * @param itemId
	 * @param count
	 *            1-卡牌，2-装备，3-卡牌碎片，4-装备碎片，5-消耗品
	 * @param userid
	 *            void
	 * @throws
	 */
	public void addItem(final int type, final int itemId, final int count,
			final long userid) {
		JunZhu junZhu = HibernateUtil.find(JunZhu.class, userid);
		switch (type) {
		case ItemCode.CARD:// 卡牌
			for (int i = 0; i < count; i++) {
				CardInfo cardInfo = new CardInfo();
				cardInfo.setId(TableIDCreator.getTableID(CardInfo.class, 1));
				cardInfo.setCardId(itemId);
				cardInfo.setUserid(userid);
				cardInfo.setExp(0);
				cardInfo.setLevel(CardMgr.getInstance().cardMap.get(itemId)
						.getCardLv());
				cardInfo.setStar(CardMgr.getInstance().cardMap.get(itemId)
						.getCardStar());
				HibernateUtil.save(cardInfo);
			}
			break;
		case ItemCode.EQUIP:// 装备
			for (int i = 0; i < count; i++) {
				EquipInfo equipInfo = new EquipInfo();
				equipInfo.setId(TableIDCreator.getTableID(EquipInfo.class, 1));
				equipInfo.setEquipId(itemId);
				equipInfo.setUserid(userid);
				equipInfo.setExp(0);
				equipInfo.setLevel(1);
				equipInfo.setStar(1);
				HibernateUtil.save(equipInfo);
			}
			break;
		case ItemCode.CARD_SP:// 卡牌碎片
			ItemInfo cardSpInfo = new ItemInfo();
			cardSpInfo.setId(TableIDCreator.getTableID(ItemInfo.class, 1));
			cardSpInfo.setCount(count);
			cardSpInfo.setItemId(itemId);
			cardSpInfo.setType(1);
			HibernateUtil.save(cardSpInfo);
			break;
		case ItemCode.EQUIP_SP:// 装备碎片
			ItemInfo equipSpInfo = new ItemInfo();
			equipSpInfo.setId(TableIDCreator.getTableID(ItemInfo.class, 1));
			equipSpInfo.setCount(count);
			equipSpInfo.setItemId(itemId);
			equipSpInfo.setType(2);
			HibernateUtil.save(equipSpInfo);
			break;
		case ItemCode.CONSUME:// 消耗品
			ItemInfo itemInfo = new ItemInfo();
			itemInfo.setId(TableIDCreator.getTableID(ItemInfo.class, 1));
			itemInfo.setCount(count);
			itemInfo.setItemId(itemId);
			itemInfo.setType(3);
			HibernateUtil.save(itemInfo);
			break;
		case ItemCode.COIN:// 金币
			junZhu.coin += count;
			HibernateUtil.save(junZhu);
			break;
		case ItemCode.FOOD:// 粮食
			junZhu.food += count;
			HibernateUtil.save(junZhu);
			break;
		case ItemCode.IRON:// 精铁
			junZhu.iron += count;
			HibernateUtil.save(junZhu);
			break;
		case ItemCode.YUAN_BAO:// 元宝
			junZhu.yuanbao += count;
			HibernateUtil.save(junZhu);
			break;
		case ItemCode.JIANG_HUN:// 将魂
			junZhu.jianghunNum += count;
			HibernateUtil.save(junZhu);
			break;
		case ItemCode.JUN_GONG:// 军功
			junZhu.jungongNum += count;
			HibernateUtil.save(junZhu);
			break;
		}
	}

	/**
	 * @Title: queryBag
	 * @Description: 查询背包
	 * @param ctx
	 * @param userid
	 *            void
	 * @throws
	 */
	public void queryBag(ChannelHandlerContext ctx, long userid) {
		JSONArray cardArray = CardMgr.getInstance().queryCard(userid);
		JSONArray equipArray = EquipMgr.getInstance().queryEquip(userid);
		JSONArray cardSpArray = new JSONArray();
		JSONArray equipSpArray = new JSONArray();
		JSONArray propArray = new JSONArray();
		List<ItemInfo> items = HibernateUtil.list(ItemInfo.class,
				"where userid=" + userid + "");
		for (ItemInfo item : items) {
			switch (item.getType()) {
			case 1:// 卡牌碎片
				JSONArray cardSpJson = new JSONArray();
				Card card = CardMgr.getInstance().cardMap.get(item.getItemId());
				cardSpJson.add(card.getCardId());
				cardSpJson.add(card.getCardName());
				cardSpJson.add(item.getCount());
				cardSpJson.add(card.getRMBID());
				cardSpArray.add(cardSpJson);
				break;
			case 2:// 装备碎片
				JSONArray equipSpJson = new JSONArray();
				Equip equip = EquipMgr.getInstance().equipMap.get(item
						.getItemId());
				equipSpJson.add(equip.getEquipID());
				equipSpJson.add(equip.getEquipName());
				equipSpJson.add(item.getCount());
				equipSpJson.add(equip.getEquipQuality());
				equipSpArray.add(equipSpJson);
				break;
			case 3:// 消耗品
				JSONArray propJson = new JSONArray();
				Prop prop = propMap.get(item.getItemId());
				propJson.add(prop.getItemID());
				propJson.add(prop.getItemName());
				propJson.add(item.getCount());
				propJson.add(prop.getItemDes());
				propJson.add(prop.getItemLv());
				propArray.add(propJson);
				break;
			default:
				break;
			}
		}
		JSONObject ret = new JSONObject();
		ret.put("card", cardArray);
		ret.put("equip", equipArray);
		ret.put("cardSp", cardSpArray);
		ret.put("equipSp", equipSpArray);
		ret.put("prop", propArray);
		HttpHandler.writeJSON(ctx, ret);
	}

	@Override
	public void proc(Event param) {
		if (param.param == null) {
			logger.error("事件{}错误，参数为空", param.id);
			return;
		}
		long userid = 0;
		int type = 0;
		int itemId = 0;
		int count = 0;
		long dbId = 0;
		Object[] params = null;
		switch (param.id) {
		case ED.BAG_ADD_ITEM:
			if (param.param instanceof Object[]) {
				params = (Object[]) param.param;
				type = (Integer) params[0];
				itemId = (Integer) params[1];
				count = (Integer) params[2];
				userid = (Long) params[3];
			}
			addItem(type, itemId, count, userid);
			break;
		case ED.BAG_REM_ITEM:
			if (param.param instanceof Object[]) {
				params = (Object[]) param.param;
				type = (Integer) params[0];
				dbId = (Long) params[1];
			}
			removeItem(type, dbId);
			break;
		}
	}

	@Override
	protected void doReg() {
		EventMgr.regist(ED.BAG_ADD_ITEM, this);
		EventMgr.regist(ED.BAG_REM_ITEM, this);
	}
}
