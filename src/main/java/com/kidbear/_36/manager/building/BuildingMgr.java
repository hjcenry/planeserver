package com.kidbear._36.manager.building;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kidbear._36.manager.junzhu.JunZhu;
import com.kidbear._36.manager.task.WorkerMgr;
import com.kidbear._36.net.ProtoMessage;
import com.kidbear._36.net.http.HttpHandler;
import com.kidbear._36.template.Bingying;
import com.kidbear._36.template.Cangku;
import com.kidbear._36.template.Huangcheng;
import com.kidbear._36.template.Jiaochang;
import com.kidbear._36.template.JunjichuUpgrade;
import com.kidbear._36.template.Minju;
import com.kidbear._36.template.Nongtian;
import com.kidbear._36.template.Zhaoshangju;
import com.kidbear._36.util.cache.MC;
import com.kidbear._36.util.csv.TempletService;
import com.kidbear._36.util.hibernate.HibernateUtil;
import com.kidbear._36.util.redis.Redis;

public class BuildingMgr {
	private static BuildingMgr buildingMgr;
	private static final Logger logger = LoggerFactory
			.getLogger(BuildingMgr.class);
	public static final String PRODUCE_CACHE = "produce_";
	public static final String HCSC_CACHE = "hczs_";
	public static final String ZB_FREE_CACHE = "zb_free_";
	public Redis redis = Redis.getInstance();
	public static int hczsFreeTimes = 10;
	public static int zbFreeTimes = 10;
	public Map<Integer, JunjichuUpgrade> junjichuMap = new HashMap<Integer, JunjichuUpgrade>();
	public Map<Integer, Huangcheng> huangchengMap = new HashMap<Integer, Huangcheng>();
	public Map<Integer, Zhaoshangju> zhaoshangjuMap = new HashMap<Integer, Zhaoshangju>();
	public Map<Integer, Cangku> cangkuMap = new HashMap<Integer, Cangku>();
	public Map<Integer, Minju> minjuMap = new HashMap<Integer, Minju>();
	public Map<Integer, Nongtian> nongtianMap = new HashMap<Integer, Nongtian>();
	public Map<Integer, Jiaochang> jiaochangMap = new HashMap<Integer, Jiaochang>();
	public Map<Integer, Bingying> bingyingMap = new HashMap<Integer, Bingying>();
	public Map<Integer, String> hczsBjMap = new HashMap<Integer, String>();

	private BuildingMgr() {
	}

	public static BuildingMgr getInstance() {
		if (null == buildingMgr) {
			buildingMgr = new BuildingMgr();
		}
		return buildingMgr;
	}

	public void initData() {
		logger.info("BuildingMgr initData");
		/** 皇城征收暴击率 **/
		Map<Integer, String> hczsBjMap = new HashMap<Integer, String>();
		hczsBjMap.put(1, "50#20");
		hczsBjMap.put(2, "35#40");
		hczsBjMap.put(5, "10#33");
		hczsBjMap.put(10, "5#7");
		hczsBjMap.put(20, "0#0");
		this.hczsBjMap = hczsBjMap;
		/** 皇城征收次数 **/
		hczsFreeTimes = 10;
		/** 免费征兵次数 **/
		zbFreeTimes = 10;
		/** 军机处升级表 **/
		List<JunjichuUpgrade> junjichuUpgrades = TempletService
				.listAll(JunjichuUpgrade.class.getSimpleName());
		Map<Integer, JunjichuUpgrade> junjichuMap = new HashMap<Integer, JunjichuUpgrade>();
		for (JunjichuUpgrade junjichuUpgrade : junjichuUpgrades) {
			junjichuMap.put(junjichuUpgrade.getScienceLv(), junjichuUpgrade);
		}
		this.junjichuMap = junjichuMap;
		/** 皇城数据表 **/
		List<Huangcheng> huangchengs = TempletService.listAll(Huangcheng.class
				.getSimpleName());
		Map<Integer, Huangcheng> huangchengMap = new HashMap<Integer, Huangcheng>();
		for (Huangcheng huangcheng : huangchengs) {
			huangchengMap.put(huangcheng.getMainCityLv(), huangcheng);
		}
		this.huangchengMap = huangchengMap;
		/** 招商局数据表 **/
		List<Zhaoshangju> zhaoshangjus = TempletService
				.listAll(Zhaoshangju.class.getSimpleName());
		Map<Integer, Zhaoshangju> zhaoshangjuMap = new HashMap<Integer, Zhaoshangju>();
		for (Zhaoshangju zhaoshangju : zhaoshangjus) {
			zhaoshangjuMap.put(zhaoshangju.getZhaoshangLv(), zhaoshangju);
		}
		this.zhaoshangjuMap = zhaoshangjuMap;
		/** 仓库数据表 **/
		List<Cangku> cangkus = TempletService.listAll(Cangku.class
				.getSimpleName());
		Map<Integer, Cangku> cangkuMap = new HashMap<Integer, Cangku>();
		for (Cangku cangku : cangkus) {
			cangkuMap.put(cangku.getCangkuLv(), cangku);
		}
		this.cangkuMap = cangkuMap;
		/** 民居数据表 **/
		List<Minju> minjus = TempletService
				.listAll(Minju.class.getSimpleName());
		Map<Integer, Minju> minjuMap = new HashMap<Integer, Minju>();
		for (Minju minju : minjus) {
			minjuMap.put(minju.getMinjuLv(), minju);
		}
		this.minjuMap = minjuMap;
		/** 农田数据表 **/
		List<Nongtian> nongtians = TempletService.listAll(Nongtian.class
				.getSimpleName());
		Map<Integer, Nongtian> nongtianMap = new HashMap<Integer, Nongtian>();
		for (Nongtian nongtian : nongtians) {
			nongtianMap.put(nongtian.getNongtianLv(), nongtian);
		}
		this.nongtianMap = nongtianMap;
		/** 兵营数据表 **/
		List<Bingying> bingyings = TempletService.listAll(Bingying.class
				.getSimpleName());
		Map<Integer, Bingying> bingyingMap = new HashMap<Integer, Bingying>();
		for (Bingying bingying : bingyings) {
			bingyingMap.put(bingying.getBingYingLv(), bingying);
		}
		this.bingyingMap = bingyingMap;
		/** 校场数据表 **/
		List<Jiaochang> jiaochangs = TempletService.listAll(Jiaochang.class
				.getSimpleName());
		Map<Integer, Jiaochang> jiaochangMap = new HashMap<Integer, Jiaochang>();
		for (Jiaochang jiaochang : jiaochangs) {
			jiaochangMap.put(jiaochang.getJiaoChangLv(), jiaochang);
		}
		this.jiaochangMap = jiaochangMap;
	}

	/**
	 * @Title: initBuildingInfo
	 * @Description: 建筑信息初始化，创建账号时调用
	 * @param userid
	 *            void
	 * @throws
	 */
	public void initBuildingInfo(long userid) {
		BuildingInfo buildingInfo = new BuildingInfo();
		buildingInfo.setId(userid);
		buildingInfo.setBingying(1);
		buildingInfo.setHuangcheng(1);
		buildingInfo.setZhaoshangju(1);
		buildingInfo.setJiaochang(1);
		buildingInfo.setJunjichu(1);
		buildingInfo.setCangku(1);
		buildingInfo.setMinju1(1);
		buildingInfo.setMinju2(1);
		buildingInfo.setMinju3(1);
		buildingInfo.setMinju4(1);
		buildingInfo.setMinju5(1);
		buildingInfo.setMinju6(1);
		buildingInfo.setNongtian1(1);
		buildingInfo.setNongtian2(1);
		buildingInfo.setNongtian3(1);
		buildingInfo.setNongtian4(1);
		buildingInfo.setNongtian5(1);
		buildingInfo.setNongtian6(1);
		MC.add(buildingInfo, String.valueOf(userid));
		HibernateUtil.insert(buildingInfo);
		/** 记录产出开始时间 **/
		ProduceQueue produceQueue = new ProduceQueue();
		for (int i = 71; i <= 76; i++) {// 民居产出
			ProduceInfo produce = new ProduceInfo();
			produce.setStarttime(System.currentTimeMillis());
			produceQueue.getProduceMap().put(i, produce);
		}
		for (int i = 81; i <= 86; i++) {// 农田产出
			ProduceInfo produce = new ProduceInfo();
			produce.setStarttime(System.currentTimeMillis());
			produceQueue.getProduceMap().put(i, produce);
		}
		redis.set(PRODUCE_CACHE + userid, JSON.toJSONString(produceQueue));
		/** 开始记录皇城征收 **/
		HczsQueue hczsQueue = new HczsQueue();
		// Date nowDate = new Date();
		// nowDate.setHours(0);
		// nowDate.setMinutes(0);
		// nowDate.setSeconds(0);
		for (int i = 1; i <= 3; i++) {
			HczsInfo hczs = new HczsInfo();
			hczs.setResettime(System.currentTimeMillis());
			hczs.setYuanbaoTimes(0);
			hczs.setFreeTimes(hczsFreeTimes);
			hczsQueue.getHczsMap().put(i, hczs);
		}
		redis.set(HCSC_CACHE + userid, JSON.toJSONString(hczsQueue));
		/** 征兵数量 **/
		ZbInfo zbInfo = new ZbInfo();
		zbInfo.setFreeTimes(zbFreeTimes);
		zbInfo.setResettime(0);
		redis.set(ZB_FREE_CACHE + userid, JSON.toJSONString(zbInfo));
		logger.info("君主{}初始化建筑信息", userid);
	}

	/**
	 * @Title: canHczs
	 * @Description: 皇城是否可以征收
	 * @param userid
	 * @return boolean
	 * @throws
	 */
	public int canHczs(long userid) {
		JunZhu junZhu = HibernateUtil.find(JunZhu.class, userid);
		if (junZhu == null) {
			logger.error("君主{}不存在", userid);
			return 0;
		}
		HczsQueue queue = JSON.parseObject(redis.get(HCSC_CACHE + userid),
				HczsQueue.class);
		boolean isRefresh = false;
		for (Integer key : queue.getHczsMap().keySet()) {
			HczsInfo hczsInfo = queue.getHczsMap().get(key);
			if (new Date(hczsInfo.getResettime()).getDate() != new Date()
					.getDate()) {// 判断是否刷新征收日期
				hczsInfo.setFreeTimes(hczsFreeTimes);
				hczsInfo.setYuanbaoTimes(0);
				hczsInfo.setResettime(System.currentTimeMillis());
				queue.getHczsMap().put(key, hczsInfo);
				isRefresh = true;
			}
			if (hczsInfo.getFreeTimes() > 0) {
				return 1;
			}
		}
		if (isRefresh) {
			redis.set(HCSC_CACHE + userid, JSON.toJSONString(queue));
		}
		return 0;
	}

	/**
	 * @Title: queryHczs
	 * @Description: 查询皇城征收
	 * @param ctx
	 * @param userid
	 *            void
	 * @throws
	 */
	public void queryHczs(ChannelHandlerContext ctx, ProtoMessage msg,
			long userid) {
		BuildingInfo buildingInfo = HibernateUtil.find(BuildingInfo.class,
				userid);
		HczsQueue queue = JSON.parseObject(redis.get(HCSC_CACHE + userid),
				HczsQueue.class);
		JSONArray hczsArr = new JSONArray();
		boolean isRefresh = false;
		for (Integer key : queue.getHczsMap().keySet()) {
			HczsInfo hczsInfo = queue.getHczsMap().get(key);
			if (new Date(hczsInfo.getResettime()).getDate() != new Date()
					.getDate()) {// 判断是否刷新征收日期
				hczsInfo.setFreeTimes(hczsFreeTimes);
				hczsInfo.setYuanbaoTimes(0);
				hczsInfo.setResettime(System.currentTimeMillis());
				queue.getHczsMap().put(key, hczsInfo);
				isRefresh = true;
			}
			JSONArray hczsJson = new JSONArray();
			hczsJson.add(hczsInfo.getFreeTimes());
			hczsJson.add(hczsInfo.getYuanbaoTimes());
			if (key == 1) {
				hczsJson.add(huangchengMap.get(buildingInfo.getHuangcheng())
						.getGetCoin());
			} else if (key == 2) {
				hczsJson.add(huangchengMap.get(buildingInfo.getHuangcheng())
						.getGetFood());
			} else {
				hczsJson.add(huangchengMap.get(buildingInfo.getHuangcheng())
						.getGetIron());
			}
			hczsArr.add(hczsJson);
		}
		if (isRefresh) {
			redis.set(HCSC_CACHE + userid, JSON.toJSONString(queue));
		}
		HttpHandler.writeJSON(ctx, hczsArr);
	}

	/**
	 * @Title: getBj
	 * @Description: 获取暴击
	 * @param type
	 *            0-免费，1-金币
	 * @return int
	 * @throws
	 */
	public int getBj(int type) {
		long num = Math.round(Math.random() * 100);
		int bj1 = Integer.parseInt(hczsBjMap.get(1).split("#")[type]);
		int bj2 = Integer.parseInt(hczsBjMap.get(2).split("#")[type]);
		int bj5 = Integer.parseInt(hczsBjMap.get(5).split("#")[type]);
		int bj10 = Integer.parseInt(hczsBjMap.get(10).split("#")[type]);
		int bj20 = Integer.parseInt(hczsBjMap.get(20).split("#")[type]);
		if (num <= bj1) {
			return 1;
		}
		if (num > bj1 && num <= bj1 + bj2) {
			return 2;
		}
		if (num > bj1 + bj2 && num <= bj1 + bj2 + bj5) {
			return 5;
		}
		if (num > bj1 + bj2 + bj5 && num <= bj1 + bj2 + bj5 + bj10) {
			return 10;
		}
		if (num > bj1 + bj2 + bj5 + bj10
				&& num <= bj1 + bj2 + bj5 + bj10 + bj20) {
			return 20;
		}
		return 1;
	}

	public void pickHczsResources(ChannelHandlerContext ctx, ProtoMessage msg,
			long userid) {
		JunZhu junZhu = HibernateUtil.find(JunZhu.class, userid);
		BuildingInfo buildingInfo = HibernateUtil.find(BuildingInfo.class,
				userid);
		int hczstype = msg.getData().getIntValue("hczstype");
		Huangcheng huangcheng = huangchengMap.get(buildingInfo.getHuangcheng());
		Cangku cangku = cangkuMap.get(buildingInfo.getCangku());
		HczsQueue queue = JSON.parseObject(redis.get(HCSC_CACHE + userid),
				HczsQueue.class);
		HczsInfo hczsInfo = queue.getHczsMap().get(hczstype);
		int type;
		if (hczsInfo.getFreeTimes() > 0) {// 免费征收
			type = 0;
		} else {// 元宝征收
			type = 1;
		}
		JSONObject ret = new JSONObject();
		switch (hczstype) {
		case 1:// coin
			int coin = huangcheng.getGetCoin() * getBj(type);
			junZhu.coin += coin;
			if (junZhu.coin > cangku.getCoinRongliang()) {
				ret.put("coin", -1);
				HttpHandler.writeJSON(ctx, ret);
				return;
			}
			ret.put("coin", coin);
			break;
		case 2:// food
			int food = huangcheng.getGetFood() * getBj(type);
			junZhu.food += food;
			if (junZhu.food > cangku.getFoodRongliang()) {
				ret.put("food", -1);
				HttpHandler.writeJSON(ctx, ret);
				return;
			}
			ret.put("food", food);
			break;
		case 3:// iron
			int iron = huangcheng.getGetIron() * getBj(type);
			junZhu.iron += iron;
			ret.put("iron", iron);
			break;
		}
		// 扣除次数
		if (type == 0) {// 扣除免费次数
			hczsInfo.setFreeTimes(hczsInfo.getFreeTimes() - 1);
		} else {// 增加金币次数并扣除金币
			hczsInfo.setYuanbaoTimes(hczsInfo.getYuanbaoTimes() + 1);
			int reduceYuanbao = hczsInfo.getYuanbaoTimes() * 2;
			if (junZhu.yuanbao < reduceYuanbao) {
				logger.error("君主{}皇城征收失败，金币不足", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("金币不足"));
				return;
			}
			junZhu.yuanbao -= reduceYuanbao;
		}
		queue.getHczsMap().put(hczstype, hczsInfo);
		redis.set(HCSC_CACHE + userid, JSON.toJSONString(queue));
		HibernateUtil.save(junZhu);
		HttpHandler.writeJSON(ctx, ret);
	}

	public void pickResources(ChannelHandlerContext ctx, ProtoMessage msg,
			long userid) {
		JunZhu junZhu = HibernateUtil.find(JunZhu.class, userid);
		BuildingInfo buildingInfo = HibernateUtil.find(BuildingInfo.class,
				userid);
		int jztype = msg.getData().getIntValue("jztype");
		ProduceQueue queue = JSON.parseObject(
				redis.get(PRODUCE_CACHE + userid), ProduceQueue.class);
		ProduceInfo produceInfo = queue.getProduceMap().get(jztype);
		int perhour = 0;
		int limit = 0;
		long starttime = produceInfo.getStarttime();
		produceInfo.setStarttime(System.currentTimeMillis());
		queue.getProduceMap().put(jztype, produceInfo);
		switch (jztype) {
		case 71:
			perhour = minjuMap.get(buildingInfo.getMinju1()).getGetCoinhour();
			limit = minjuMap.get(buildingInfo.getMinju1()).getMinjuUpLimit();
			break;
		case 72:
			perhour = minjuMap.get(buildingInfo.getMinju2()).getGetCoinhour();
			limit = minjuMap.get(buildingInfo.getMinju2()).getMinjuUpLimit();
			break;
		case 73:
			perhour = minjuMap.get(buildingInfo.getMinju3()).getGetCoinhour();
			limit = minjuMap.get(buildingInfo.getMinju3()).getMinjuUpLimit();
			break;
		case 74:
			perhour = minjuMap.get(buildingInfo.getMinju4()).getGetCoinhour();
			limit = minjuMap.get(buildingInfo.getMinju4()).getMinjuUpLimit();
			break;
		case 75:
			perhour = minjuMap.get(buildingInfo.getMinju5()).getGetCoinhour();
			limit = minjuMap.get(buildingInfo.getMinju5()).getMinjuUpLimit();
			break;
		case 76:
			perhour = minjuMap.get(buildingInfo.getMinju6()).getGetCoinhour();
			limit = minjuMap.get(buildingInfo.getMinju6()).getMinjuUpLimit();
			break;
		case 81:
			perhour = nongtianMap.get(buildingInfo.getNongtian1())
					.getGetFoodhour();
			limit = nongtianMap.get(buildingInfo.getNongtian1())
					.getNongtianUpLimit();
			break;
		case 82:
			perhour = nongtianMap.get(buildingInfo.getNongtian2())
					.getGetFoodhour();
			limit = nongtianMap.get(buildingInfo.getNongtian2())
					.getNongtianUpLimit();
			break;
		case 83:
			perhour = nongtianMap.get(buildingInfo.getNongtian3())
					.getGetFoodhour();
			limit = nongtianMap.get(buildingInfo.getNongtian3())
					.getNongtianUpLimit();
			break;
		case 84:
			perhour = nongtianMap.get(buildingInfo.getNongtian4())
					.getGetFoodhour();
			limit = nongtianMap.get(buildingInfo.getNongtian4())
					.getNongtianUpLimit();
			break;
		case 85:
			perhour = nongtianMap.get(buildingInfo.getNongtian5())
					.getGetFoodhour();
			limit = nongtianMap.get(buildingInfo.getNongtian5())
					.getNongtianUpLimit();
			break;
		case 86:
			perhour = nongtianMap.get(buildingInfo.getNongtian6())
					.getGetFoodhour();
			limit = nongtianMap.get(buildingInfo.getNongtian6())
					.getNongtianUpLimit();
			break;
		}
		int resource = (int) (((System.currentTimeMillis() - starttime) / 1000d / 3600d) * perhour);
		resource = (resource > limit) ? limit : resource;
		JSONObject ret = new JSONObject();
		if (jztype < 80) {// 民居
			junZhu.coin += resource;
			if (junZhu.coin > cangkuMap.get(buildingInfo.getCangku())
					.getCoinRongliang()) {
				ret.put("coin", -1);
				HttpHandler.writeJSON(ctx, ret);
				return;
			}
			ret.put("coin", resource);
		} else {// 农田
			junZhu.food += resource;
			if (junZhu.food > cangkuMap.get(buildingInfo.getCangku())
					.getFoodRongliang()) {
				ret.put("coin", -1);
				HttpHandler.writeJSON(ctx, ret);
				return;
			}
			ret.put("food", resource);
		}
		produceInfo.setStarttime(System.currentTimeMillis());
		redis.set(PRODUCE_CACHE + userid, JSON.toJSONString(queue));
		HibernateUtil.save(junZhu);
		HttpHandler.writeJSON(ctx, ret);
	}

	public JSONArray getBuildings(long userid) {
		BuildingInfo buildingInfo = HibernateUtil.find(BuildingInfo.class,
				userid);
		JSONArray array = new JSONArray();
		array.add(buildingInfo.getHuangcheng());
		array.add(buildingInfo.getCangku());
		array.add(buildingInfo.getJiaochang());
		array.add(buildingInfo.getJunjichu());
		array.add(buildingInfo.getBingying());
		array.add(buildingInfo.getZhaoshangju());
		array.add(buildingInfo.getMinju1());
		array.add(buildingInfo.getMinju2());
		array.add(buildingInfo.getMinju3());
		array.add(buildingInfo.getMinju4());
		array.add(buildingInfo.getMinju5());
		array.add(buildingInfo.getMinju6());
		array.add(buildingInfo.getNongtian1());
		array.add(buildingInfo.getNongtian2());
		array.add(buildingInfo.getNongtian3());
		array.add(buildingInfo.getNongtian4());
		array.add(buildingInfo.getNongtian5());
		array.add(buildingInfo.getNongtian6());
		return array;
	}

	/**
	 * @Title: upgrade
	 * @Description: 建筑升级
	 * @param msg
	 * @param ctx
	 *            void
	 * @throws
	 */
	public void upgrade(ChannelHandlerContext ctx, ProtoMessage msg, long userid) {
		JunZhu junZhu = HibernateUtil.find(JunZhu.class, userid);
		int jztype = msg.getData().getIntValue("jztype");
		int type = msg.getData().getIntValue("type");// 0时间升级，1-元宝升级
		BuildingInfo buildingInfo = HibernateUtil.find(BuildingInfo.class,
				userid);
		switch (jztype) {
		case 1:// 皇城
			Huangcheng huangcheng = huangchengMap.get(buildingInfo
					.getHuangcheng() + 1);
			if (huangcheng.getScienceCostCoin() > junZhu.getCoin()) {
				logger.info("君主{}升级皇城失败，金币不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("金币不足"));
				return;
			}
			if (huangcheng.getScienceCostFood() > junZhu.getFood()) {
				logger.info("君主{}升级皇城失败，粮草不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("粮草不足"));
				return;
			}
			upgradeBuilding(ctx, userid, huangcheng.getCostTime(), type);
			buildingInfo.setHuangcheng(buildingInfo.getHuangcheng() + 1);
			junZhu.setCoin(junZhu.getCoin() - huangcheng.getScienceCostCoin());
			junZhu.setFood(junZhu.getFood() - huangcheng.getScienceCostFood());
			HibernateUtil.save(junZhu);
			HibernateUtil.save(buildingInfo);
			HttpHandler.writeJSON(ctx, ProtoMessage.getSuccessResp());
			return;
		case 2:// 招商局
			Zhaoshangju zhaoshangju = zhaoshangjuMap.get(buildingInfo
					.getZhaoshangju() + 1);
			if (zhaoshangju.getScienceCostCoin() > junZhu.getCoin()) {
				logger.info("君主{}升级招商局失败，金币不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("金币不足"));
				return;
			}
			if (zhaoshangju.getScienceCostFood() > junZhu.getFood()) {
				logger.info("君主{}升级招商局失败，粮草不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("粮草不足"));
				return;
			}
			upgradeBuilding(ctx, userid, zhaoshangju.getCostTime(), type);
			buildingInfo.setZhaoshangju(buildingInfo.getZhaoshangju() + 1);
			junZhu.setCoin(junZhu.getCoin() - zhaoshangju.getScienceCostCoin());
			junZhu.setFood(junZhu.getFood() - zhaoshangju.getScienceCostFood());
			HibernateUtil.save(junZhu);
			HibernateUtil.save(buildingInfo);
			HttpHandler.writeJSON(ctx, ProtoMessage.getSuccessResp());
			return;
		case 3:// 校场
			Jiaochang jiaochang = jiaochangMap
					.get(buildingInfo.getJiaochang() + 1);
			if (jiaochang.getScienceCostCoin() > junZhu.getCoin()) {
				logger.info("君主{}升级校场失败，金币不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("金币不足"));
				return;
			}
			if (jiaochang.getScienceCostFood() > junZhu.getFood()) {
				logger.info("君主{}升级校场失败，粮草不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("粮草不足"));
				return;
			}
			upgradeBuilding(ctx, userid, jiaochang.getCostTime(), type);
			buildingInfo.setJiaochang(buildingInfo.getJiaochang() + 1);
			junZhu.setCoin(junZhu.getCoin() - jiaochang.getScienceCostCoin());
			junZhu.setFood(junZhu.getFood() - jiaochang.getScienceCostFood());
			HibernateUtil.save(junZhu);
			HibernateUtil.save(buildingInfo);
			HttpHandler.writeJSON(ctx, ProtoMessage.getSuccessResp());
			return;
		case 4:// 仓库
			Cangku cangku = cangkuMap.get(buildingInfo.getCangku() + 1);
			if (cangku.getScienceCostCoin() > junZhu.getCoin()) {
				logger.info("君主{}升级仓库失败，金币不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("金币不足"));
				return;
			}
			if (cangku.getScienceCostFood() > junZhu.getFood()) {
				logger.info("君主{}升级仓库失败，粮草不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("粮草不足"));
				return;
			}
			upgradeBuilding(ctx, userid, cangku.getCostTime(), type);
			buildingInfo.setCangku(buildingInfo.getCangku() + 1);
			junZhu.setCoin(junZhu.getCoin() - cangku.getScienceCostCoin());
			junZhu.setFood(junZhu.getFood() - cangku.getScienceCostFood());
			HibernateUtil.save(junZhu);
			HibernateUtil.save(buildingInfo);
			HttpHandler.writeJSON(ctx, ProtoMessage.getSuccessResp());
			return;
		case 5:// 兵营
			Bingying bingying = bingyingMap.get(buildingInfo.getBingying() + 1);
			if (bingying.getScienceCostCoin() > junZhu.getCoin()) {
				logger.info("君主{}升级兵营失败，金币不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("金币不足"));
				return;
			}
			if (bingying.getScienceCostFood() > junZhu.getFood()) {
				logger.info("君主{}升级兵营失败，粮草不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("粮草不足"));
				return;
			}
			upgradeBuilding(ctx, userid, bingying.getCostTime(), type);
			buildingInfo.setBingying(buildingInfo.getBingying() + 1);
			junZhu.setCoin(junZhu.getCoin() - bingying.getScienceCostCoin());
			junZhu.setFood(junZhu.getFood() - bingying.getScienceCostFood());
			HibernateUtil.save(junZhu);
			HibernateUtil.save(buildingInfo);
			HttpHandler.writeJSON(ctx, ProtoMessage.getSuccessResp());
			return;
		case 6:// 军机处
			JunjichuUpgrade junjichuUpgrade = junjichuMap.get(buildingInfo
					.getJunjichu() + 1);
			if (junjichuUpgrade.getScienceCostCoin() > junZhu.getCoin()) {
				logger.info("君主{}升级军机处失败，金币不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("金币不足"));
				return;
			}
			if (junjichuUpgrade.getScienceCostFood() > junZhu.getFood()) {
				logger.info("君主{}升级军机处失败，粮草不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("粮草不足"));
				return;
			}
			upgradeBuilding(ctx, userid, junjichuUpgrade.getCostTime(), type);
			buildingInfo.setJunjichu(buildingInfo.getJunjichu() + 1);
			junZhu.setCoin(junZhu.getCoin()
					- junjichuUpgrade.getScienceCostCoin());
			junZhu.setFood(junZhu.getFood()
					- junjichuUpgrade.getScienceCostFood());
			HibernateUtil.save(junZhu);
			HibernateUtil.save(buildingInfo);
			HttpHandler.writeJSON(ctx, ProtoMessage.getSuccessResp());
			return;
		case 71:// 民居1
			Minju minju1 = minjuMap.get(buildingInfo.getMinju1() + 1);
			if (minju1.getScienceCostCoin() > junZhu.getCoin()) {
				logger.info("君主{}升级民居1失败，金币不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("金币不足"));
				return;
			}
			if (minju1.getScienceCostFood() > junZhu.getFood()) {
				logger.info("君主{}升级民居1失败，粮草不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("粮草不足"));
				return;
			}
			upgradeBuilding(ctx, userid, minju1.getCostTime(), type);
			buildingInfo.setMinju1(buildingInfo.getMinju1() + 1);
			junZhu.setCoin(junZhu.getCoin() - minju1.getScienceCostCoin());
			junZhu.setFood(junZhu.getFood() - minju1.getScienceCostFood());
			HibernateUtil.save(junZhu);
			HibernateUtil.save(buildingInfo);
			HttpHandler.writeJSON(ctx, ProtoMessage.getSuccessResp());
			return;
		case 72:// 民居2
			Minju minju2 = minjuMap.get(buildingInfo.getMinju2() + 1);
			if (minju2.getScienceCostCoin() > junZhu.getCoin()) {
				logger.info("君主{}升级民居2失败，金币不够");
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("金币不足"));
				return;
			}
			if (minju2.getScienceCostFood() > junZhu.getFood()) {
				logger.info("君主{}升级民居2失败，粮草不够");
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("粮草不足"));
				return;
			}
			upgradeBuilding(ctx, userid, minju2.getCostTime(), type);
			buildingInfo.setMinju2(buildingInfo.getMinju2() + 1);
			junZhu.setCoin(junZhu.getCoin() - minju2.getScienceCostCoin());
			junZhu.setFood(junZhu.getFood() - minju2.getScienceCostFood());
			HibernateUtil.save(junZhu);
			HibernateUtil.save(buildingInfo);
			HttpHandler.writeJSON(ctx, ProtoMessage.getSuccessResp());
			return;
		case 73:// 民居3
			Minju minju3 = minjuMap.get(buildingInfo.getMinju3() + 1);
			if (minju3.getScienceCostCoin() > junZhu.getCoin()) {
				logger.info("君主{}升级民居3失败，金币不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("金币不足"));
				return;
			}
			if (minju3.getScienceCostFood() > junZhu.getFood()) {
				logger.info("君主{}升级民居3失败，粮草不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("粮草不足"));
				return;
			}
			upgradeBuilding(ctx, userid, minju3.getCostTime(), type);
			buildingInfo.setMinju3(buildingInfo.getMinju3() + 1);
			junZhu.setCoin(junZhu.getCoin() - minju3.getScienceCostCoin());
			junZhu.setFood(junZhu.getFood() - minju3.getScienceCostFood());
			HibernateUtil.save(junZhu);
			HibernateUtil.save(buildingInfo);
			HttpHandler.writeJSON(ctx, ProtoMessage.getSuccessResp());
			return;
		case 74:// 民居4
			Minju minju4 = minjuMap.get(buildingInfo.getMinju4() + 1);
			if (minju4.getScienceCostCoin() > junZhu.getCoin()) {
				logger.info("君主{}升级民居4失败，金币不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("金币不足"));
				return;
			}
			if (minju4.getScienceCostFood() > junZhu.getFood()) {
				logger.info("君主{}升级民居4失败，粮草不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("粮草不足"));
				return;
			}
			upgradeBuilding(ctx, userid, minju4.getCostTime(), type);
			buildingInfo.setMinju4(buildingInfo.getMinju4() + 1);
			junZhu.setCoin(junZhu.getCoin() - minju4.getScienceCostCoin());
			junZhu.setFood(junZhu.getFood() - minju4.getScienceCostFood());
			HibernateUtil.save(junZhu);
			HibernateUtil.save(buildingInfo);
			HttpHandler.writeJSON(ctx, ProtoMessage.getSuccessResp());
			return;
		case 75:// 民居5
			Minju minju5 = minjuMap.get(buildingInfo.getMinju5() + 1);
			if (minju5.getScienceCostCoin() > junZhu.getCoin()) {
				logger.info("君主{}升级民居5失败，金币不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("金币不足"));
				return;
			}
			if (minju5.getScienceCostFood() > junZhu.getFood()) {
				logger.info("君主{}升级民居5失败，粮草不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("粮草不足"));
				return;
			}
			upgradeBuilding(ctx, userid, minju5.getCostTime(), type);
			buildingInfo.setMinju5(buildingInfo.getMinju5() + 1);
			junZhu.setCoin(junZhu.getCoin() - minju5.getScienceCostCoin());
			junZhu.setFood(junZhu.getFood() - minju5.getScienceCostFood());
			HibernateUtil.save(junZhu);
			HibernateUtil.save(buildingInfo);
			HttpHandler.writeJSON(ctx, ProtoMessage.getSuccessResp());
			return;
		case 76:// 民居6
			Minju minju6 = minjuMap.get(buildingInfo.getMinju6() + 1);
			if (minju6.getScienceCostCoin() > junZhu.getCoin()) {
				logger.info("君主{}升级民居6失败，金币不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("金币不足"));
				return;
			}
			if (minju6.getScienceCostFood() > junZhu.getFood()) {
				logger.info("君主{}升级民居6失败，粮草不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("粮草不足"));
				return;
			}
			upgradeBuilding(ctx, userid, minju6.getCostTime(), type);
			buildingInfo.setMinju6(buildingInfo.getMinju6() + 1);
			junZhu.setCoin(junZhu.getCoin() - minju6.getScienceCostCoin());
			junZhu.setFood(junZhu.getFood() - minju6.getScienceCostFood());
			HibernateUtil.save(junZhu);
			HibernateUtil.save(buildingInfo);
			HttpHandler.writeJSON(ctx, ProtoMessage.getSuccessResp());
			return;
		case 81:// 农田1
			Nongtian nongtian1 = nongtianMap
					.get(buildingInfo.getNongtian1() + 1);
			if (nongtian1.getScienceCostCoin() > junZhu.getCoin()) {
				logger.info("君主{}升级农田1失败，金币不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("金币不足"));
				return;
			}
			if (nongtian1.getScienceCostFood() > junZhu.getFood()) {
				logger.info("君主{}升级农田1失败，粮草不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("粮草不足"));
				return;
			}

			upgradeBuilding(ctx, userid, nongtian1.getCostTime(), type);
			buildingInfo.setNongtian1(buildingInfo.getNongtian1() + 1);
			junZhu.setCoin(junZhu.getCoin() - nongtian1.getScienceCostCoin());
			junZhu.setFood(junZhu.getFood() - nongtian1.getScienceCostFood());
			HibernateUtil.save(junZhu);
			HibernateUtil.save(buildingInfo);
			HttpHandler.writeJSON(ctx, ProtoMessage.getSuccessResp());
			return;
		case 82:// 农田2
			Nongtian nongtian2 = nongtianMap
					.get(buildingInfo.getNongtian2() + 1);
			if (nongtian2.getScienceCostCoin() > junZhu.getCoin()) {
				logger.info("君主{}升级农田2失败，金币不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("金币不足"));
				return;
			}
			if (nongtian2.getScienceCostFood() > junZhu.getFood()) {
				logger.info("君主{}升级农田2失败，粮草不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("粮草不足"));
				return;
			}

			upgradeBuilding(ctx, userid, nongtian2.getCostTime(), type);
			buildingInfo.setNongtian2(buildingInfo.getNongtian2() + 1);
			junZhu.setCoin(junZhu.getCoin() - nongtian2.getScienceCostCoin());
			junZhu.setFood(junZhu.getFood() - nongtian2.getScienceCostFood());
			HibernateUtil.save(junZhu);
			HibernateUtil.save(buildingInfo);
			HttpHandler.writeJSON(ctx, ProtoMessage.getSuccessResp());
			return;
		case 83:// 农田3
			Nongtian nongtian3 = nongtianMap
					.get(buildingInfo.getNongtian3() + 1);
			if (nongtian3.getScienceCostCoin() > junZhu.getCoin()) {
				logger.info("君主{}升级农田3失败，金币不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("金币不足"));
				return;
			}
			if (nongtian3.getScienceCostFood() > junZhu.getFood()) {
				logger.info("君主{}升级农田3失败，粮草不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("粮草不足"));
				return;
			}

			upgradeBuilding(ctx, userid, nongtian3.getCostTime(), type);
			buildingInfo.setNongtian3(buildingInfo.getNongtian3() + 1);
			junZhu.setCoin(junZhu.getCoin() - nongtian3.getScienceCostCoin());
			junZhu.setFood(junZhu.getFood() - nongtian3.getScienceCostFood());
			HibernateUtil.save(junZhu);
			HibernateUtil.save(buildingInfo);
			HttpHandler.writeJSON(ctx, ProtoMessage.getSuccessResp());
			return;
		case 84:// 农田4
			Nongtian nongtian4 = nongtianMap
					.get(buildingInfo.getNongtian4() + 1);
			if (nongtian4.getScienceCostCoin() > junZhu.getCoin()) {
				logger.info("君主{}升级农田4失败，金币不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("金币不足"));
				return;
			}
			if (nongtian4.getScienceCostFood() > junZhu.getFood()) {
				logger.info("君主{}升级农田4失败，粮草不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("粮草不足"));
				return;
			}

			upgradeBuilding(ctx, userid, nongtian4.getCostTime(), type);
			buildingInfo.setNongtian4(buildingInfo.getNongtian4() + 1);
			junZhu.setCoin(junZhu.getCoin() - nongtian4.getScienceCostCoin());
			junZhu.setFood(junZhu.getFood() - nongtian4.getScienceCostFood());
			HibernateUtil.save(junZhu);
			HibernateUtil.save(buildingInfo);
			HttpHandler.writeJSON(ctx, ProtoMessage.getSuccessResp());
			return;
		case 85:// 农田5
			Nongtian nongtian5 = nongtianMap
					.get(buildingInfo.getNongtian5() + 1);
			if (nongtian5.getScienceCostCoin() > junZhu.getCoin()) {
				logger.info("君主{}升级农田5失败，金币不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("金币不足"));
				return;
			}
			if (nongtian5.getScienceCostFood() > junZhu.getFood()) {
				logger.info("君主{}升级农田5失败，粮草不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("粮草不足"));
				return;
			}
			upgradeBuilding(ctx, userid, nongtian5.getCostTime(), type);
			buildingInfo.setNongtian5(buildingInfo.getNongtian5() + 1);
			junZhu.setCoin(junZhu.getCoin() - nongtian5.getScienceCostCoin());
			junZhu.setFood(junZhu.getFood() - nongtian5.getScienceCostFood());
			HibernateUtil.save(junZhu);
			HibernateUtil.save(buildingInfo);
			HttpHandler.writeJSON(ctx, ProtoMessage.getSuccessResp());
			return;
		case 86:// 农田6
			Nongtian nongtian6 = nongtianMap
					.get(buildingInfo.getNongtian6() + 1);
			if (nongtian6.getScienceCostCoin() > junZhu.getCoin()) {
				logger.info("君主{}升级农田6失败，金币不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("金币不足"));
				return;
			}
			if (nongtian6.getScienceCostFood() > junZhu.getFood()) {
				logger.info("君主{}升级农田6失败，粮草不够", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("粮草不足"));
				return;
			}
			upgradeBuilding(ctx, userid, nongtian6.getCostTime(), type);
			buildingInfo.setNongtian6(buildingInfo.getNongtian6() + 1);
			junZhu.setCoin(junZhu.getCoin() - nongtian6.getScienceCostCoin());
			junZhu.setFood(junZhu.getFood() - nongtian6.getScienceCostFood());
			HibernateUtil.save(junZhu);
			HibernateUtil.save(buildingInfo);
			HttpHandler.writeJSON(ctx, ProtoMessage.getSuccessResp());
			return;
		default:
			break;
		}
		HttpHandler.writeJSON(ctx, msg);
	}

	public void upgradeBuilding(ChannelHandlerContext ctx, long userid,
			long worktime, int type) {
		JunZhu junZhu = HibernateUtil.find(JunZhu.class, userid);
		if (type == 0) {// 时间升级
			if (!WorkerMgr.getInstance().addTaskToWorker(userid,
					worktime * 1000)) {
				logger.error("君主{}升级建筑失败，无可用建筑工人", userid);
				HttpHandler
						.writeJSON(ctx, ProtoMessage.getErrorResp("无可用建筑工人"));
				return;
			}
		} else if (type == 1) {// 元宝升级
			int yuanbao = Math.round(worktime / 360);
			if (yuanbao > junZhu.getYuanbao()) {
				logger.error("君主{}升级建筑失败，元宝不足", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("元宝不足"));
				return;
			}
			junZhu.setYuanbao(junZhu.getYuanbao() - yuanbao);
			HibernateUtil.save(junZhu);
		}
	}

	public JSONArray getProduceCoinInfo(long userid) {
		JSONArray array = new JSONArray();
		ProduceQueue queue = JSON.parseObject(
				redis.get(PRODUCE_CACHE + userid), ProduceQueue.class);
		for (Integer key : queue.getProduceMap().keySet()) {
			if (key >= 71 && key <= 76) {// 筛选民居
				ProduceInfo produce = queue.getProduceMap().get(key);
				JSONArray produceJson = new JSONArray();
				produceJson.add(produce.getStarttime());
			}
			ProduceInfo produce = queue.getProduceMap().get(key);
			BuildingInfo buildingInfo = HibernateUtil.find(BuildingInfo.class,
					userid);
			switch (key) {
			case 71:
				JSONArray minju1 = new JSONArray();
				minju1.add(produce.getStarttime());
				minju1.add(minjuMap.get(buildingInfo.getMinju1())
						.getGetCoinhour());
				minju1.add(minjuMap.get(buildingInfo.getMinju1())
						.getMinjuUpLimit());
				array.add(minju1);
				break;
			case 72:
				JSONArray minju2 = new JSONArray();
				minju2.add(produce.getStarttime());
				minju2.add(minjuMap.get(buildingInfo.getMinju2())
						.getGetCoinhour());
				minju2.add(minjuMap.get(buildingInfo.getMinju2())
						.getMinjuUpLimit());
				array.add(minju2);
				break;
			case 73:
				JSONArray minju3 = new JSONArray();
				minju3.add(produce.getStarttime());
				minju3.add(minjuMap.get(buildingInfo.getMinju3())
						.getGetCoinhour());
				minju3.add(minjuMap.get(buildingInfo.getMinju3())
						.getMinjuUpLimit());
				array.add(minju3);
				break;
			case 74:
				JSONArray minju4 = new JSONArray();
				minju4.add(produce.getStarttime());
				minju4.add(minjuMap.get(buildingInfo.getMinju4())
						.getGetCoinhour());
				minju4.add(minjuMap.get(buildingInfo.getMinju4())
						.getMinjuUpLimit());
				array.add(minju4);
				break;
			case 75:
				JSONArray minju5 = new JSONArray();
				minju5.add(produce.getStarttime());
				minju5.add(minjuMap.get(buildingInfo.getMinju5())
						.getGetCoinhour());
				minju5.add(minjuMap.get(buildingInfo.getMinju5())
						.getMinjuUpLimit());
				array.add(minju5);
				break;
			case 76:
				JSONArray minju6 = new JSONArray();
				minju6.add(produce.getStarttime());
				minju6.add(minjuMap.get(buildingInfo.getMinju6())
						.getGetCoinhour());
				minju6.add(minjuMap.get(buildingInfo.getMinju6())
						.getMinjuUpLimit());
				array.add(minju6);
				break;
			}
		}
		return array;
	}

	public JSONArray getProduceFoodInfo(long userid) {
		JSONArray array = new JSONArray();
		ProduceQueue queue = JSON.parseObject(
				redis.get(PRODUCE_CACHE + userid), ProduceQueue.class);
		for (Integer key : queue.getProduceMap().keySet()) {
			if (key >= 81 && key <= 86) {// 筛选农田
				ProduceInfo produce = queue.getProduceMap().get(key);
				JSONArray produceJson = new JSONArray();
				produceJson.add(produce.getStarttime());
			}
			ProduceInfo produce = queue.getProduceMap().get(key);
			BuildingInfo buildingInfo = HibernateUtil.find(BuildingInfo.class,
					userid);
			switch (key) {
			case 81:
				JSONArray nongtian1 = new JSONArray();
				nongtian1.add(produce.getStarttime());
				nongtian1.add(nongtianMap.get(buildingInfo.getNongtian1())
						.getGetFoodhour());
				nongtian1.add(nongtianMap.get(buildingInfo.getNongtian1())
						.getNongtianUpLimit());
				array.add(nongtian1);
				break;
			case 82:
				JSONArray nongtian2 = new JSONArray();
				nongtian2.add(produce.getStarttime());
				nongtian2.add(nongtianMap.get(buildingInfo.getNongtian2())
						.getGetFoodhour());
				nongtian2.add(nongtianMap.get(buildingInfo.getNongtian2())
						.getNongtianUpLimit());
				array.add(nongtian2);
				break;
			case 83:
				JSONArray nongtian3 = new JSONArray();
				nongtian3.add(produce.getStarttime());
				nongtian3.add(nongtianMap.get(buildingInfo.getNongtian3())
						.getGetFoodhour());
				nongtian3.add(nongtianMap.get(buildingInfo.getNongtian3())
						.getNongtianUpLimit());
				array.add(nongtian3);
				break;
			case 84:
				JSONArray nongtian4 = new JSONArray();
				nongtian4.add(produce.getStarttime());
				nongtian4.add(nongtianMap.get(buildingInfo.getNongtian4())
						.getGetFoodhour());
				nongtian4.add(nongtianMap.get(buildingInfo.getNongtian4())
						.getNongtianUpLimit());
				array.add(nongtian4);
				break;
			case 85:
				JSONArray nongtian5 = new JSONArray();
				nongtian5.add(produce.getStarttime());
				nongtian5.add(nongtianMap.get(buildingInfo.getNongtian5())
						.getGetFoodhour());
				nongtian5.add(nongtianMap.get(buildingInfo.getNongtian5())
						.getNongtianUpLimit());
				array.add(nongtian5);
				break;
			case 86:
				JSONArray nongtian6 = new JSONArray();
				nongtian6.add(produce.getStarttime());
				nongtian6.add(nongtianMap.get(buildingInfo.getNongtian6())
						.getGetFoodhour());
				nongtian6.add(nongtianMap.get(buildingInfo.getNongtian6())
						.getNongtianUpLimit());
				array.add(nongtian6);
				break;
			}
		}
		return array;
	}

	/**
	 * @Title: getZbTimes
	 * @Description: 获取征兵次数
	 * @param userid
	 * @return int
	 * @throws
	 */
	public int getZbTimes(long userid) {
		ZbInfo zbInfo = JSON.parseObject(redis.get(ZB_FREE_CACHE + userid),
				ZbInfo.class);
		if (new Date().getDate() != new Date(zbInfo.getResettime()).getDate()) {
			zbInfo.setFreeTimes(zbFreeTimes);
			zbInfo.setResettime(System.currentTimeMillis());
			redis.set(ZB_FREE_CACHE + userid, JSON.toJSONString(zbInfo));
		}
		return zbInfo.getFreeTimes();
	}

	/**
	 * @Title: zhengbing
	 * @Description: 征兵
	 * @param ctx
	 * @param msg
	 * @param userid
	 *            void
	 * @throws
	 */
	public void zhengbing(ChannelHandlerContext ctx, ProtoMessage msg,
			long userid) {
		int type = msg.getData().getIntValue("type");
		int soilderNum = msg.getData().getIntValue("num");
		JunZhu junZhu = HibernateUtil.find(JunZhu.class, userid);
		BuildingInfo buildingInfo = HibernateUtil.find(BuildingInfo.class,
				userid);
		Bingying bingying = bingyingMap.get(buildingInfo.getBingying());
		ZbInfo zbInfo = JSON.parseObject(redis.get(ZB_FREE_CACHE + userid),
				ZbInfo.class);
		switch (type) {
		case 1:// 免费
			if (zbInfo.getFreeTimes() == 0) {
				logger.error("君主{}免费征兵失败，免费次数已用完", userid);
				HttpHandler
						.writeJSON(ctx, ProtoMessage.getErrorResp("免费次数已用完"));
				return;
			}
			zbInfo.setFreeTimes(zbInfo.getFreeTimes() - 1);
			soilderNum = bingying.getSoldiersNum();
			break;
		case 2:// 粮草征兵
				// 消耗粮草 = 士兵数量 * （0.5+主城等级系数 – 科技23减免）
				// 主城等级系数 = 0.01+主城等级*0.005
			int food = soilderNum;
			if (junZhu.food < food) {
				logger.error("君主{}征兵失败，粮食不足", userid);
				HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("粮食不足"));
				return;
			}
			junZhu.food -= food;
			break;
		}
		junZhu.soldierNum += soilderNum;
		if (junZhu.soldierNum > bingying.getBingYingCapaCity()) {
			logger.error("君主{}征兵失败，兵数量超过最大容量", userid);
			HttpHandler.writeJSON(ctx, ProtoMessage.getErrorResp("兵数量超过最大容量"));
			return;
		}
		HibernateUtil.save(junZhu);
		redis.set(ZB_FREE_CACHE + userid, JSON.toJSONString(zbInfo));
		JSONArray ret = new JSONArray();
		ret.add(soilderNum);
		HttpHandler.writeJSON(ctx, ProtoMessage.getSuccessResp());
	}
}
