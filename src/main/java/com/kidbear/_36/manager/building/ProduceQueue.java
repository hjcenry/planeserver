package com.kidbear._36.manager.building;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class ProduceQueue {
	public Map<Integer, ProduceInfo> produceMap = new TreeMap<Integer, ProduceInfo>(// 比较器
			new Comparator<Integer>() {
				public int compare(Integer key1, Integer key2) {
					return key1 - key2;
				}
			});

	public Map<Integer, ProduceInfo> getProduceMap() {
		return produceMap;
	}

	public void setProduceMap(Map<Integer, ProduceInfo> produceMap) {
		this.produceMap = produceMap;
	}
}
