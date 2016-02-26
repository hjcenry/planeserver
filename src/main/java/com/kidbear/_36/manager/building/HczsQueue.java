package com.kidbear._36.manager.building;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class HczsQueue {
	private Map<Integer, HczsInfo> hczsMap = new TreeMap<Integer, HczsInfo>(// 比较器
			new Comparator<Integer>() {
				public int compare(Integer key1, Integer key2) {
					return key1 - key2;
				}
			});

	public Map<Integer, HczsInfo> getHczsMap() {
		return hczsMap;
	}

	public void setHczsMap(Map<Integer, HczsInfo> hczsMap) {
		this.hczsMap = hczsMap;
	}

}
