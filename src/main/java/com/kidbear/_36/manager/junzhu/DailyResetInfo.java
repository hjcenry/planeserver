package com.kidbear._36.manager.junzhu;

/**
 * 每日重置信息json缓存 用户免费义兵次数，卡牌分解次数，领取礼包次数，免费抽卡次数，免费征收次数，金币征收次数
 * */
public class DailyResetInfo {
	private long userid;
	private long soilderTimes;
	private long decomposeTimes;
	private long pickAwardTimes;
	private long freeCardTimes;
	private long freeGetTimes;
	private long coinGetTimes;
	private long resettime;

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public long getSoilderTimes() {
		return soilderTimes;
	}

	public void setSoilderTimes(long soilderTimes) {
		this.soilderTimes = soilderTimes;
	}

	public long getDecomposeTimes() {
		return decomposeTimes;
	}

	public void setDecomposeTimes(long decomposeTimes) {
		this.decomposeTimes = decomposeTimes;
	}

	public long getPickAwardTimes() {
		return pickAwardTimes;
	}

	public void setPickAwardTimes(long pickAwardTimes) {
		this.pickAwardTimes = pickAwardTimes;
	}

	public long getFreeCardTimes() {
		return freeCardTimes;
	}

	public void setFreeCardTimes(long freeCardTimes) {
		this.freeCardTimes = freeCardTimes;
	}

	public long getFreeGetTimes() {
		return freeGetTimes;
	}

	public void setFreeGetTimes(long freeGetTimes) {
		this.freeGetTimes = freeGetTimes;
	}

	public long getCoinGetTimes() {
		return coinGetTimes;
	}

	public void setCoinGetTimes(long coinGetTimes) {
		this.coinGetTimes = coinGetTimes;
	}

	public long getResettime() {
		return resettime;
	}

	public void setResettime(long resettime) {
		this.resettime = resettime;
	}

}
