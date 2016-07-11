package com.kidbear.plane.manager.shop;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.kidbear.pay.client.pay.IPay;
import com.kidbear.pay.client.pay.Pay;
import com.kidbear.plane.manager.bag.BagMgr;
import com.kidbear.plane.manager.bag.ItemCode;
import com.kidbear.plane.manager.event.ED;
import com.kidbear.plane.manager.event.EventMgr;
import com.kidbear.plane.manager.junzhu.JunZhu;
import com.kidbear.plane.net.ProtoMessage;
import com.kidbear.plane.net.http.HttpInHandler;
import com.kidbear.plane.net.http.HttpServer;
import com.kidbear.plane.net.rpc.JsonRpcServers;
import com.kidbear.plane.net.rpc.MotanClient;
import com.kidbear.plane.util.hibernate.HibernateUtil;
import com.kidbear.plane.util.redis.Redis;

public class ShopMgr {
	private static ShopMgr shopMgr;
	private static final Logger logger = LoggerFactory.getLogger(ShopMgr.class);
	public Redis redis = Redis.getInstance();
	public static final String SHOP_CACHE = "shop_cache";

	private ShopMgr() {
	}

	public static ShopMgr getInstance() {
		if (null == shopMgr) {
			shopMgr = new ShopMgr();
		}
		return shopMgr;
	}

	public void initData() {
		logger.info("ShopMgr initData");
	}

	public void initCsvData() {
		logger.info("ShopMgr initCsvData");
	}

	public boolean initShopInfo(long userid) {
		return true;
	}

	/**
	 * @Title: queryPaySum
	 * @Description: 查询累计充值
	 * @param ctx
	 * @param userid
	 *            void
	 * @throws
	 */
	public void queryPaySum(ChannelHandlerContext ctx, long userid) {
		JSONArray ret = new JSONArray();
		int sum = MotanClient.getService(IPay.class).queryPaySum(userid);
		ret.add(sum);
		HttpInHandler.writeJSON(ctx, ret);
	}

	/**
	 * @Title: fetchOrder
	 * @Description: 获取支付订单
	 * @param ctx
	 * @param msg
	 * @param userid
	 *            void
	 * @throws
	 */
	public void fetchOrder(ChannelHandlerContext ctx, ProtoMessage msg,
			long userid) {
		String gamename = msg.getData().getString("game");
		String goods = msg.getData().getString("goods");
		Pay pay = new Pay();
		pay.setUserid(userid);
		pay.setGoodname(goods);
		pay.setGamename(gamename);
		// rpc调用支付服务器生成订单
		long billno = MotanClient.getService(IPay.class).generateOrder(pay);
		JSONArray ret = new JSONArray();
		ret.add(billno);
		HttpInHandler.writeJSON(ctx, ret);
	}

	/**
	 * @Title: fetchGoods
	 * @Description: 获取支付物品
	 * @param ctx
	 * @param msg
	 * @param userid
	 *            void
	 * @throws
	 */
	public void fetchGoods(ChannelHandlerContext ctx, ProtoMessage msg,
			long userid) {
		long billno = msg.getData().getLongValue("billno");
		// rpc调用支付服务器获取订单
		Pay pay = MotanClient.getService(IPay.class).queryOrder(billno);
		JSONArray ret = new JSONArray();
		switch (pay.getIsFinished()) {
		case 0:// 未验证
			ret.add(ProtoMessage.getErrorResp(pay.getIsFinished(), "订单验证中"));
			break;
		case 1:// 支付验证失败
			ret.add(ProtoMessage.getErrorResp(pay.getIsFinished(), "订单验证失败"));
			break;
		case 2:// 支付验证成功，未发货
			JSONArray goods = sendGoods(pay.getGoodname(), userid);
			if (goods != null) {
				MotanClient.getService(IPay.class).sendGoods(billno, true);
				ret.add(goods);
			} else {
				MotanClient.getService(IPay.class).sendGoods(billno, false);
				ret.add(ProtoMessage.getErrorResp(pay.getIsFinished(), "发货失败"));
			}
			break;
		case 3:// 已发货
			ret.add(ProtoMessage.getErrorResp(pay.getIsFinished(), "订单已发货"));
			break;
		default:
			break;
		}
		HttpInHandler.writeJSON(ctx, ret);
	}

	/**
	 * @Title: sendGoods
	 * @Description: 发货逻辑
	 * @param goods
	 * @param userid
	 * @return boolean
	 * @throws
	 */
	public JSONArray sendGoods(String goods, long userid) {
		JSONArray ret = new JSONArray();
		try {
			// BagMgr.getInstance().addItem(ItemCode.YUAN_BAO, 0, 100, userid);
			ret.add(ItemCode.YUAN_BAO);
			ret.add(0);
			ret.add(100);
			return ret;
		} catch (Exception e) {
			return ret;
		}
	}
}
