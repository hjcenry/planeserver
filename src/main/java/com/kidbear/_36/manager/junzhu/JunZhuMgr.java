package com.kidbear._36.manager.junzhu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

}
