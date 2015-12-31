package com.kidbear._36.manager.account;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.kidbear._36.util.cache.MCSupport;

@Entity
public class Account implements MCSupport {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1821602184326315958L;
	@Id
	private long id;
	private String name;
	private String pwd;
	private long playerId;

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	@Override
	public long getIdentifier() {
		return id;
	}
}
