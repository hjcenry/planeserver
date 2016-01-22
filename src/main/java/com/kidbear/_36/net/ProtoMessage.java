package com.kidbear._36.net;

import java.io.Serializable;

import net.sf.json.JSONObject;

public class ProtoMessage implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = -3460913241121151489L;
	private short id;
	public JSONObject data;

	public ProtoMessage() {

	}

	public ProtoMessage(short id) {
		this.id = id;
	}

	public <T> ProtoMessage(short id, T data) {
		this.id = id;
		this.setData(data);
	}

	public short getId() {
		return id;
	}

	public void setId(short id) {
		this.id = id;
	}

	@SuppressWarnings("unchecked")
	public <T> T getData(Class<T> t) {// 转换为对象传递
		return (T) JSONObject.toBean(data, t);
	}

	public <T> void setData(T t) {
		this.data = JSONObject.fromObject(t);
	}
}
