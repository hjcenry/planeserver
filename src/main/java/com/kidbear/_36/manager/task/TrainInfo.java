package com.kidbear._36.manager.task;

/*训练信息 json缓存*/
public class TrainInfo {
	private long cardDBId;
	private long cardId;
	private long starttime;
	private long worktime;
	private int tufeiTimes;
	private int type;
	private boolean isopen;

	public long getCardId() {
		return cardId;
	}

	public void setCardId(long cardId) {
		this.cardId = cardId;
	}

	public long getStarttime() {
		return starttime;
	}

	public void setStarttime(long starttime) {
		this.starttime = starttime;
	}

	public boolean getIsopen() {
		return isopen;
	}

	public void setIsopen(boolean isopen) {
		this.isopen = isopen;
	}

	public long getWorktime() {
		return worktime;
	}

	public void setWorktime(long worktime) {
		this.worktime = worktime;
	}

	public int getTufeiTimes() {
		return tufeiTimes;
	}

	public void setTufeiTimes(int tufeiTimes) {
		this.tufeiTimes = tufeiTimes;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getCardDBId() {
		return cardDBId;
	}

	public void setCardDBId(long cardDBId) {
		this.cardDBId = cardDBId;
	}

}
