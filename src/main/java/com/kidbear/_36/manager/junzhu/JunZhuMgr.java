package com.kidbear._36.manager.junzhu;

import java.util.Date;

import javax.persistence.Column;

import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kidbear._36.manager.building.BuildingMgr;
import com.kidbear._36.manager.task.WorkerMgr;
import com.kidbear._36.net.http.HttpHandler;
import com.kidbear._36.util.hibernate.HibernateUtil;
import com.mongodb.util.StringParseUtil;

public class JunZhuMgr {
	private static JunZhuMgr junZhuMgr;
	private static final Logger logger = LoggerFactory
			.getLogger(JunZhuMgr.class);

	private JunZhuMgr() {
	}

	public static JunZhuMgr getInstance() {
		if (null == junZhuMgr) {
			junZhuMgr = new JunZhuMgr();
		}
		return junZhuMgr;
	}

	public void initData() {
		logger.info("JunZhuMgr initData");
	}

	/**
	 * @Title: getMainInfo
	 * @Description: 获取主界面信息
	 * @param ctx
	 * @param userid
	 *            void
	 * @throws
	 */
	public void getMainInfo(ChannelHandlerContext ctx, long userid) {
		JSONObject mainJson = new JSONObject();
		mainJson.put("junzhu", getJunZhuInfo(userid));
		mainJson.put("building", BuildingMgr.getInstance().getBuildings(userid));
		mainJson.put("worker", WorkerMgr.getInstance().getWorkerQueue(userid));
		mainJson.put("coin",
				BuildingMgr.getInstance().getProduceCoinInfo(userid));
		mainJson.put("food",
				BuildingMgr.getInstance().getProduceFoodInfo(userid));
		mainJson.put("time", System.currentTimeMillis());
		mainJson.put("hczs", BuildingMgr.getInstance().canHczs(userid));
		mainJson.put("zbcs", BuildingMgr.getInstance().getZbTimes(userid));
		HttpHandler.writeJSON(ctx, mainJson);
	}

	public JSONArray getJunZhuInfo(long userid) {
		JunZhu junZhu = HibernateUtil.find(JunZhu.class, userid);
		if (junZhu == null) {
			logger.error("君主{}不存在", userid);
			return null;
		}
		JSONArray jzArr = new JSONArray();
		jzArr.add(junZhu.id);
		jzArr.add(junZhu.name);// — 用户名
		jzArr.add(junZhu.headImg);// —用户头像
		jzArr.add(junZhu.country);// —用户所属国家 1表示蜀国 2表示魏国 3表示吴国
		jzArr.add(junZhu.level);// —用户等级 —君主等级
		jzArr.add(junZhu.vip);// —用户vip等级
		jzArr.add(junZhu.junling);// —用户军令数
		jzArr.add(junZhu.coin);// —用户金币
		jzArr.add(junZhu.yuanbao);// —用户元宝
		jzArr.add(junZhu.food);// —用户粮食
		jzArr.add(junZhu.iron);// —用户精铁
		jzArr.add(junZhu.wood);// —用户木材
		jzArr.add(junZhu.soldierNum);// —用户兵力
		jzArr.add(junZhu.jungongNum);// —用户军功数
		jzArr.add(junZhu.jianghunNum);// —将魂数量
		jzArr.add(junZhu.zxId);// -当前阵型id
		return jzArr;
	}

}
