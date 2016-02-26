package com.kidbear._36.manager.task;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.kidbear._36.manager.event.ED;
import com.kidbear._36.manager.event.Event;
import com.kidbear._36.manager.event.EventMgr;
import com.kidbear._36.manager.event.EventProc;
import com.kidbear._36.manager.junzhu.JunZhu;
import com.kidbear._36.util.hibernate.HibernateUtil;
import com.kidbear._36.util.redis.Redis;

public class WorkerMgr extends EventProc {
	private static WorkerMgr workerMgr;
	private static final Logger logger = LoggerFactory
			.getLogger(WorkerMgr.class);
	public static final String WORKER_CACHE = "worker_";// 建筑工人缓存key
														// worker_userid
	public Map<Integer, String> workerOpenMap = new HashMap<Integer, String>();
	public static long MAX_CD_TIME = 240 * 60 * 1000;// 建筑工人最大CD时间
	public Redis redis = Redis.getInstance();

	private WorkerMgr() {
	}

	public static WorkerMgr getInstance() {
		if (null == workerMgr) {
			workerMgr = new WorkerMgr();
		}
		return workerMgr;
	}

	public void initData() {
		logger.info("WorkerMgr initData");
		// 初始化建筑开放条件
		Map<Integer, String> workerOpenMap = new HashMap<Integer, String>();
		workerOpenMap.put(1, "0#0");
		workerOpenMap.put(2, "30#1");
		workerOpenMap.put(3, "40#3");
		workerOpenMap.put(4, "60#5");
		this.workerOpenMap = workerOpenMap;
	}

	/**
	 * @Title: initWorkers
	 * @Description: 初始化账号时调用，初始化工人信息
	 * @param userid
	 *            void
	 * @throws
	 */
	public void initWorkers(long userid) {
		WorkQueue workQueuen = new WorkQueue();
		Map<Integer, WorkerInfo> worker = new HashMap<Integer, WorkerInfo>();
		for (int i = 1; i <= workerOpenMap.keySet().size(); i++) {
			WorkerInfo info = new WorkerInfo();
			info.setIsopen(false);// 所有工人默认不开放
			worker.put(i, info);
		}
		workQueuen.setWorker(worker);
		redis.set(WORKER_CACHE + userid, JSON.toJSONString(workQueuen));
		EventMgr.addEvent(ED.CHECK_WORKER_OPEN, Long.valueOf(userid));
		logger.info("君主{}初始化工人队列", userid);
	}

	/**
	 * @Title: checkWorkerOpen
	 * @Description: 检查工人是否开放
	 * @param userid
	 *            void
	 * @throws
	 */
	public void checkWorkerOpen(final long userid) {
		JunZhu junZhu = HibernateUtil.find(JunZhu.class, userid);
		WorkQueue queue = (WorkQueue) JSON.parseObject(
				redis.get(WORKER_CACHE + userid), WorkQueue.class);
		for (Integer index : queue.getWorker().keySet()) {
			WorkerInfo worker = queue.getWorker().get(index);
			if (worker.isIsopen()) {
				continue;
			}
			String condition = workerOpenMap.get(index);
			int level = Integer.parseInt(condition.split("#")[0]);
			int vip = Integer.parseInt(condition.split("#")[1]);
			// 判断条件是否满足
			if (junZhu.level >= level || junZhu.vip >= vip) {
				// 开放建筑工人
				if (!worker.isIsopen()) {
					worker.setIsopen(true);
					queue.getWorker().put(index, worker);
					redis.set(WORKER_CACHE + userid, JSON.toJSONString(queue));
				}
			}
		}
	}

	/**
	 * @Title: addTaskToWorker
	 * @Description: 增加任务到工人
	 * @param userid
	 * @param worktime
	 *            CD时间，单位ms
	 * @return boolean
	 * @throws
	 */
	public boolean addTaskToWorker(long userid, long worktime) {
		WorkQueue queue = (WorkQueue) JSON.parseObject(
				redis.get(WORKER_CACHE + userid), WorkQueue.class);
		for (Integer index : queue.getWorker().keySet()) {
			WorkerInfo worker = queue.getWorker().get(index);
			if (!worker.isIsopen()) {// 工人未开放
				continue;
			}
			if (worker.getWorktime() + worktime >= MAX_CD_TIME) {// CD时间超过最大值
				continue;
			}
			if (worker.getStarttime() == 0) {
				worker.setStarttime(System.currentTimeMillis());
			}
			// 累加工作时间
			worker.setWorktime(worker.getWorktime() + worktime);
			queue.getWorker().put(index, worker);
			redis.set(WORKER_CACHE + userid, JSON.toJSONString(queue));
			return true;
		}
		return false;
	}

	/**
	 * @Title: getWorkerQueue
	 * @Description: 获取工人队列
	 * @param userid
	 * @return JSONArray
	 * @throws
	 */
	public JSONArray getWorkerQueue(long userid) {
		JSONArray workerArr = new JSONArray();
		WorkQueue queue = (WorkQueue) JSON.parseObject(
				redis.get(WORKER_CACHE + userid), WorkQueue.class);
		for (Integer key : queue.getWorker().keySet()) {
			WorkerInfo worker = queue.getWorker().get(key);
			long starttime = worker.getStarttime();
			long endtime = System.currentTimeMillis();
			if ((endtime - starttime) > worker.getWorktime()) {// 工作已经结束
				worker.setStarttime(0);
				worker.setWorktime(0);
			}
			JSONArray workerJson = new JSONArray();
			workerJson.add(worker.isIsopen());
			workerJson.add(worker.getStarttime());
			workerJson.add(worker.getWorktime());
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
		case ED.CHECK_WORKER_OPEN:
			checkWorkerOpen(userid);
			break;
		}
	}

	@Override
	protected void doReg() {
		EventMgr.regist(ED.CHECK_WORKER_OPEN, this);
	}
}
