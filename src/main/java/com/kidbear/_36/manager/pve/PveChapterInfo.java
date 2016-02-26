package com.kidbear._36.manager.pve;

import java.util.HashMap;
import java.util.Map;

public class PveChapterInfo {
	private Map<Integer, PveAwardInfo> chapter = new HashMap<Integer,PveAwardInfo>();

	public Map<Integer,PveAwardInfo> getChapter() {
		return chapter;
	}

	public void setChapter(Map<Integer,PveAwardInfo> chapter) {
		this.chapter = chapter;
	}

}
