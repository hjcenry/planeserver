package com.kidbear.plane.manager.rank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kidbear.plane.core.GameInit;

public class RankMgr {
	private static RankMgr rankMgr;
	private static final Logger logger = LoggerFactory.getLogger(RankMgr.class);
	public static final String PK_RANK = "pk_rank_" + GameInit.serverId;

	private RankMgr() {
	}

	public static RankMgr getInstance() {
		if (null == rankMgr) {
			rankMgr = new RankMgr();
		}
		return rankMgr;
	}

	public void initData() {
		logger.info("RankMgr initData");
	}

	public void refreshPkRank(long userid) {

	}

	public int queryRank() {
		return 0;
	}
}
