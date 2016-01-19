package com.kidbear._36.manager.building;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuildingMgr {
	private static BuildingMgr buildingMgr;
	private static final Logger logger = LoggerFactory
			.getLogger(BuildingMgr.class);

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
	}
}