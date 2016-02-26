package com.kidbear._36.manager.task;

import java.util.HashMap;
import java.util.Map;

public class TrainQueue {
	private Map<Integer, TrainInfo> train = new HashMap<Integer, TrainInfo>();

	public Map<Integer, TrainInfo> getTrain() {
		return train;
	}

	public void setTrain(Map<Integer, TrainInfo> train) {
		this.train = train;
	}
}
