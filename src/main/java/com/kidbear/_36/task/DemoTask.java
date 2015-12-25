package com.kidbear._36.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;

public class DemoTask {
	@Autowired
	private TaskExecutor taskExecutor;

	private static final Logger logger = LoggerFactory
			.getLogger(DemoTask.class);

	private static DemoTask inst;

	private DemoTask() {
	}

	public static DemoTask getInstance() {
		if (null == inst) {
			inst = new DemoTask();
		}
		return inst;
	}

	public void refreshRank(final int articleId) {
		taskExecutor.execute(new Runnable() {

			@Override
			public void run() {
				//
			}
		});
	}
}
