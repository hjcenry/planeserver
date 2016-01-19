package com.kidbear._36.manager.bag;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Item")
public class Item {
	@Id
	private long id;
	private int type;// 物品类型，1-武将，2-装备，3-

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
