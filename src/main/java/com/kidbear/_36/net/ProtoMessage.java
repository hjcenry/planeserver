package com.kidbear._36.net;

import java.io.Serializable;

import net.sf.json.JSONObject;

public class ProtoMessage implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = -3460913241121151489L;
	private short protoId;
	public JSONObject msg;

	public ProtoMessage() {

	}

	public ProtoMessage(short protoId) {
		this.protoId = protoId;
	}

	public <T> ProtoMessage(short protoId, T msg) {
		this.protoId = protoId;
		this.setMsg(msg);
	}

	public short getProtoId() {
		return protoId;
	}

	public void setProtoId(short protoId) {
		this.protoId = protoId;
	}

	public <T> T getMsg(Class<T> t) {// 转换为对象传递
		return (T) JSONObject.toBean(msg, t);
	}

	public <T> void setMsg(T t) {
		this.msg = JSONObject.fromObject(t);
	}
}
