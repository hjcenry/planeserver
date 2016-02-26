package com.kidbear._36.manager.event;

/**
 * 事件处理器。
 * 
 * @author 何金成
 *
 */
public abstract class EventProc {
	public boolean disable;

	public EventProc() {
		doReg();
	}

	public abstract void proc(Event param);

	protected abstract void doReg();
}
