package com.kidbear._36.manager.task;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.kidbear._36.manager.card.CardInfo;
import com.kidbear._36.manager.event.ED;
import com.kidbear._36.manager.event.Event;
import com.kidbear._36.manager.event.EventMgr;
import com.kidbear._36.manager.event.EventProc;
import com.kidbear._36.manager.junzhu.JunZhu;
import com.kidbear._36.util.hibernate.HibernateUtil;
import com.kidbear._36.util.redis.Redis;

public class TrainMgr extends EventProc {
	private static TrainMgr trainMgr;
	private static final Logger logger = LoggerFactory
			.getLogger(TrainMgr.class);
	public static final String TRAIN_CACHE = "train_";
	public Map<Integer, String> trainOpenMap = new HashMap<Integer, String>();
	public Map<Integer, String> trainConditionMap = new HashMap<Integer, String>();
	public Redis redis = Redis.getInstance();

	private TrainMgr() {
	}

	public static TrainMgr getInstance() {
		if (null == trainMgr) {
			trainMgr = new TrainMgr();
		}
		return trainMgr;
	}

	public void initData() {
		logger.info("TrainMgr initData");
		// 初始化训练队列开放条件
		Map<Integer, String> trainOpenMap = new HashMap<Integer, String>();
		trainOpenMap.put(1, "0#0");
		trainOpenMap.put(2, "0#0");
		trainOpenMap.put(3, "30#1");
		trainOpenMap.put(4, "35#2");
		trainOpenMap.put(5, "40#3");
		trainOpenMap.put(6, "10000#4");
		this.trainOpenMap = trainOpenMap;
		Map<Integer, String> trainConditionMap = new HashMap<Integer, String>();
		trainConditionMap.put(1, "4#100");
		trainConditionMap.put(2, "8#150");
		trainConditionMap.put(3, "12#200");
		this.trainConditionMap = trainConditionMap;
	}

	/**
	 * @Title: initWorkers
	 * @Description: 初始化账号时调用，初始化训练信息
	 * @param userid
	 *            void
	 * @throws
	 */
	public void initTrains(long userid) {
		TrainQueue trainQueue = new TrainQueue();
		Map<Integer, TrainInfo> train = new HashMap<Integer, TrainInfo>();
		for (int i = 1; i <= trainOpenMap.keySet().size(); i++) {
			TrainInfo info = new TrainInfo();
			if (i == 1 || i == 2) {// 默认前两个开放
				info.setIsopen(true);
			} else {
				info.setIsopen(false);
			}
			info.setCardId(0);
			info.setCardDBId(0);
			info.setStarttime(0);
			info.setTufeiTimes(0);
			info.setWorktime(0);
			train.put(i, info);
		}
		trainQueue.setTrain(train);
		redis.set(TRAIN_CACHE + userid, JSON.toJSONString(trainQueue));
		EventMgr.addEvent(ED.CHECK_TRAIN_OPEN, userid);
		logger.info("君主{}初始化训练队列", userid);
	}

	/**
	 * @Title: checkTrainOpen
	 * @Description: 检查工人是否开放
	 * @param userid
	 *            void
	 * @throws
	 */
	public void checkTrainOpen(final long userid) {
		JunZhu junZhu = HibernateUtil.find(JunZhu.class, userid);
		TrainQueue queue = (TrainQueue) JSON.parseObject(
				redis.get(TRAIN_CACHE + userid), TrainQueue.class);
		for (Integer index : queue.getTrain().keySet()) {
			TrainInfo worker = queue.getTrain().get(index);
			if (worker.getIsopen()) {
				continue;
			}
			String condition = trainOpenMap.get(index);
			int level = Integer.parseInt(condition.split("#")[0]);
			int vip = Integer.parseInt(condition.split("#")[1]);
			// 判断条件是否满足
			if (junZhu.level >= level || junZhu.vip >= vip) {
				// 开放训练队列
				if (!worker.getIsopen()) {
					worker.setIsopen(true);
					queue.getTrain().put(index, worker);
					redis.set(TRAIN_CACHE + userid, JSON.toJSONString(queue));
				}
			}
		}
	}

	/**
	 * @Title: addCardToTrain
	 * @Description: 增加卡牌升级到训练队列
	 * @param userid
	 * @param cardId
	 * @param worktime
	 * @param trainPos
	 * @return boolean
	 * @throws
	 */
	public boolean addCardToTrain(long userid, long cardId, long worktime,
			int trainPos, int type) {
		TrainQueue queue = (TrainQueue) JSON.parseObject(
				redis.get(TRAIN_CACHE + userid), TrainQueue.class);
		TrainInfo train = queue.getTrain().get(trainPos);
		if (!train.getIsopen()) {
			return false;
		}
		if (train.getCardDBId() != 0) {// 有武将正在训练
			return false;
		}
		CardInfo cardInfo = HibernateUtil.find(CardInfo.class, cardId);
		train.setCardDBId(cardId);
		train.setCardId(cardInfo.getCardId());
		train.setStarttime(System.currentTimeMillis());
		train.setWorktime(worktime);
		train.setTufeiTimes(0);
		train.setType(type);
		queue.getTrain().put(trainPos, train);
		redis.set(TRAIN_CACHE + userid, JSON.toJSONString(queue));
		return true;
	}

	/**
	 * @Title: getTrainQueue
	 * @Description: 获取卡牌队列
	 * @param userid
	 * @return JSONArray
	 * @throws
	 */
	public JSONArray getTrainQueue(long userid) {
		JSONArray workerArr = new JSONArray();
		TrainQueue queue = (TrainQueue) JSON.parseObject(
				redis.get(TRAIN_CACHE + userid), TrainQueue.class);
		for (Integer key : queue.getTrain().keySet()) {
			TrainInfo train = queue.getTrain().get(key);
			long starttime = train.getStarttime();
			long endtime = System.currentTimeMillis();
			if ((endtime - starttime) > train.getWorktime()) {// 工作已经结束
				train.setStarttime(0);
				train.setWorktime(0);
			}
			JSONArray workerJson = new JSONArray();
			workerJson.add(train.getIsopen());
			workerJson.add(train.getStarttime());
			workerJson.add(train.getWorktime());
			workerJson.add(train.getTufeiTimes());
			workerJson.add(train.getCardDBId());
			workerJson.add(train.getCardId());
			workerArr.add(key - 1, workerJson);
		}
		return workerArr;
	}

	@Override
	public void proc(Event param) {
		if (param.param == null) {
			logger.error("事件{}错误，参数为空", param.id);
			return;
		}
		long userid = 0;
		if (param.param instanceof Long) {
			userid = (Long) param.param;
		}
		switch (param.id) {
		case ED.CHECK_TRAIN_OPEN:
			checkTrainOpen(userid);
			break;
		}
	}

	@Override
	protected void doReg() {
		EventMgr.regist(ED.CHECK_TRAIN_OPEN, this);
	}
}
