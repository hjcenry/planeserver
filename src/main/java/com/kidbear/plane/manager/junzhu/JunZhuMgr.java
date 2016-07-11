package com.kidbear.plane.manager.junzhu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kidbear.plane.manager.event.Event;
import com.kidbear.plane.manager.event.EventProc;

public class JunZhuMgr extends EventProc {
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

	public void initCsvData() {
		logger.info("JunZhuMgr initCsvData");
	}

	@Override
	public void proc(Event param) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doReg() {
		// TODO Auto-generated method stub

	}

}
