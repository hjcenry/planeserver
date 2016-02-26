package com.kidbear._36.manager.task;

import java.util.Map;

public class WorkQueue {
	private Map<Integer, WorkerInfo> worker;

	public Map<Integer, WorkerInfo> getWorker() {
		return worker;
	}

	public void setWorker(Map<Integer, WorkerInfo> worker) {
		this.worker = worker;
	}

}
