package com.kidbear._36.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpResponseStatus;
import net.sf.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.kidbear._36.manager.account.AccountMgr;
import com.kidbear._36.manager.bag.BagMgr;
import com.kidbear._36.manager.building.BuildingInfo;
import com.kidbear._36.manager.building.BuildingMgr;
import com.kidbear._36.manager.card.CardMgr;
import com.kidbear._36.manager.equip.EquipMgr;
import com.kidbear._36.manager.junzhu.JunZhu;
import com.kidbear._36.manager.junzhu.JunZhuMgr;
import com.kidbear._36.manager.mail.MailMgr;
import com.kidbear._36.manager.pve.PveMgr;
import com.kidbear._36.manager.task.TrainMgr;
import com.kidbear._36.manager.task.WorkerMgr;
import com.kidbear._36.manager.tec.TecMgr;
import com.kidbear._36.manager.zhenxing.ZhenXingInfo;
import com.kidbear._36.manager.zhenxing.ZhenxingMgr;
import com.kidbear._36.net.ProtoIds;
import com.kidbear._36.net.ProtoMessage;
import com.kidbear._36.net.http.HttpHandler;
import com.kidbear._36.net.message.TestReq;
import com.kidbear._36.net.socket.SocketHandler;
import com.kidbear._36.util.JsonUtils;
import com.kidbear._36.util.hibernate.HibernateUtil;
import com.kidbear._36.util.redis.Redis;

/**
 * @ClassName: Router
 * @Description: 消息路由分发
 * @author 何金成
 * @date 2015年12月14日 下午7:12:57
 * 
 */
public class Router {
	private static Router router = new Router();
	public Logger logger = LoggerFactory.getLogger(Router.class);
	public Redis redis = Redis.getInstance();
	public AccountMgr accountMgr;
	public BuildingMgr buildingMgr;
	public MailMgr mailMgr;
	public CardMgr cardMgr;
	public EquipMgr equipMgr;
	public BagMgr bagMgr;
	public JunZhuMgr junZhuMgr;
	public TecMgr tecMgr;
	public PveMgr pveMgr;
	public WorkerMgr workerMgr;
	public TrainMgr trainMgr;
	public ZhenxingMgr zhenxingMgr;

	private Router() {
		accountMgr = AccountMgr.getInstance();
		buildingMgr = BuildingMgr.getInstance();
		mailMgr = MailMgr.getInstance();
		cardMgr = CardMgr.getInstance();
		equipMgr = EquipMgr.getInstance();
		bagMgr = BagMgr.getInstance();
		junZhuMgr = JunZhuMgr.getInstance();
		tecMgr = TecMgr.getInstance();
		pveMgr = PveMgr.getInstance();
		workerMgr = WorkerMgr.getInstance();
		trainMgr = TrainMgr.getInstance();
		zhenxingMgr = ZhenxingMgr.getInstance();
	}

	public static Router getInstance() {
		if (null == router) {
			router = new Router();
		}
		return router;
	}

	public void initData() {// 初始化模块
		accountMgr.initData();
		buildingMgr.initData();
		mailMgr.initData();
		cardMgr.initData();
		equipMgr.initData();
		bagMgr.initData();
		junZhuMgr.initData();
		tecMgr.initData();
		pveMgr.initData();
		workerMgr.initData();
		trainMgr.initData();
		zhenxingMgr.initData();
	}

	/**
	 * @Title: route
	 * @Description: 消息路由分发
	 * @param val
	 * @param ctx
	 *            void
	 * @throws
	 */
	public void route(String val, ChannelHandlerContext ctx) {
		ProtoMessage msg = null;
		try {
			msg = (ProtoMessage) JSON.parseObject(val, ProtoMessage.class);
		} catch (Exception e) {
			logger.error("gameData的json格式转换错误");
			HttpHandler.writeJSON(ctx, HttpResponseStatus.NOT_ACCEPTABLE,
					"not acceptable");
			return;
		}
		Short typeid = msg.getTypeid();
		if (typeid == null) {
			logger.error("没有typeid");
			return;
		}
		Long userid = msg.getUserid();
		if (userid != null) {// 信息初始化
			JunZhu junZhu = HibernateUtil.find(JunZhu.class, userid);
			if (junZhu == null) {
				logger.error("君主{}不存在", userid);
				HttpHandler.writeJSON(ctx,
						ProtoMessage.getErrorResp("君主" + userid + "不存在"));
				return;
			}
			// trainMgr.initTrains(userid);
		}
		switch (typeid) {
		/** 邮件 **/
		case ProtoIds.MAIL_PICK:
			mailMgr.pickItems(ctx, msg, userid);
			break;
		case ProtoIds.MAIL_QUERY:
			mailMgr.queryMail(ctx, userid);
			break;
		case ProtoIds.MAIL_READ:
			mailMgr.readMail(ctx, msg);
			break;
		case ProtoIds.MAIL_DELETE:
			mailMgr.deleteMail(ctx, msg);
			break;
		/** 背包 **/
		case ProtoIds.BAG_QUERY:
			bagMgr.queryBag(ctx, userid);
			break;
		/** 主界面信息 **/
		case ProtoIds.MAIN_INFO_QUERY:
			junZhuMgr.getMainInfo(ctx, userid);
			break;
		/** 建筑 **/
		case ProtoIds.BUILDING_UPGRADE:
			buildingMgr.upgrade(ctx, msg, userid);
			break;
		case ProtoIds.RESOURCE_PICK:
			buildingMgr.pickResources(ctx, msg, userid);
			break;
		/** 皇城征收 **/
		case ProtoIds.TEC_QUERY:
			tecMgr.queryTec(ctx, userid);
			break;
		case ProtoIds.TEC_UPGRADE:
			tecMgr.upgrade(ctx, msg, userid);
			break;
		/** 皇城征收 **/
		case ProtoIds.HCZS_QUERY:
			buildingMgr.queryHczs(ctx, msg, userid);
			break;
		case ProtoIds.HCZS_PICK:
			buildingMgr.pickHczsResources(ctx, msg, userid);
			break;
		/*** 装备 */
		case ProtoIds.EQUIP_QUERY:
			equipMgr.queryEquip(ctx, userid);
			break;
		/** 卡牌 **/
		case ProtoIds.CARD_QUERY:
			cardMgr.queryCard(ctx, userid);
			break;
		case ProtoIds.CARD_FREE_QUERY:
			cardMgr.queryCardFreePick(ctx, userid);
			break;
		case ProtoIds.CARD_PICK:
			cardMgr.pickCard(ctx, msg, userid);
			break;
		case ProtoIds.CARD_UPGRADE_STAR:
			cardMgr.upgradeStar(ctx, msg, userid);
			break;
		case ProtoIds.CARD_DECOMPOSE:
			cardMgr.decompose(ctx, msg, userid);
			break;
		case ProtoIds.CARD_DECOMPOSE_PICK:
			cardMgr.pickCardDecompose(ctx, userid);
			break;
		case ProtoIds.CARD_DECOMPOSE_QUERY:
			cardMgr.queryCardDecompose(ctx, userid);
			break;
		/** 卡牌训练 **/
		case ProtoIds.TRAIN_CARD:
			cardMgr.trainCard(ctx, msg, userid);
			break;
		case ProtoIds.TRAIN_PICK_CARD:
			cardMgr.pickTrainCard(ctx, msg, userid);
			break;
		case ProtoIds.TRAIN_TUFEI:
			cardMgr.tufeiTrain(ctx, msg, userid);
			break;
		case ProtoIds.TRAIN_QUERY:
			cardMgr.queryTrain(ctx, userid);
			break;
		/** 阵型 **/
		case ProtoIds.ZX_QUERY:
			zhenxingMgr.queryZhenxing(ctx, userid);
			break;
		case ProtoIds.ZX_SET:
			zhenxingMgr.setZx(ctx, msg, userid);
			break;
		/** 征兵 **/
		case ProtoIds.ZHENG_BING:
			buildingMgr.zhengbing(ctx, msg, userid);
			break;
		/** Pve **/
		case ProtoIds.PVE_QUERY:
			pveMgr.queryPve(ctx, userid);
			break;
		case ProtoIds.PVE_PICK:
			pveMgr.pickPveAward(ctx, msg, userid);
			break;
		default:
			HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("协议号错误"));
			logger.error("未注册协议号{}", typeid);
			break;
		}
	}

	public void test(ProtoMessage msg, ChannelHandlerContext ctx) {
		// logger.info("收到客户端的测试消息:");
		// logger.info("id:" + msg.getId());
		// logger.info("msg:" +
		// JsonUtils.objectToJson(msg.getData(TestReq.class)));
		// JSONObject object = new JSONObject();
		// object.put("msg", "服务器收到测试消息");
		// ProtoMessage message = new ProtoMessage(ProtoIds.TEST, object);
		// HttpHandler.writeJSON(ctx, message);
	}
}
