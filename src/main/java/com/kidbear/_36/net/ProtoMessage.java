package com.kidbear._36.net;

import java.io.Serializable;

import net.sf.json.JSONObject;

public class ProtoMessage implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = -3460913241121151489L;
	private short protoId;
	public JSONObject data;

	public ProtoMessage() {

	}

	public ProtoMessage(short protoId) {
		this.protoId = protoId;
	}

	public <T> ProtoMessage(short protoId, T data) {
		this.protoId = protoId;
		this.setData(data);
	}

	public short getProtoId() {
		return protoId;
	}

	public void setProtoId(short protoId) {
		this.protoId = protoId;
	}

	@SuppressWarnings("unchecked")
	public <T> T getData(Class<T> t) {// 转换为对象传递
		return (T) JSONObject.toBean(data, t);
	}

	public <T> void setData(T t) {
		this.data = JSONObject.fromObject(t);
	}
}
