package com.kidbear._36.manager.task;

/*
 * 工人信息（json格式缓存）
 * */
public class WorkerInfo {
	private long starttime;
	private long worktime;
	private boolean isopen;

	public long getStarttime() {
		return starttime;
	}

	public void setStarttime(long starttime) {
		this.starttime = starttime;
	}

	public long getWorktime() {
		return worktime;
	}

	public void setWorktime(long worktime) {
		this.worktime = worktime;
	}

	public boolean isIsopen() {
		return isopen;
	}

	public void setIsopen(boolean isopen) {
		this.isopen = isopen;
	}

}
