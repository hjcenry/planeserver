package com.kidbear.plane.manager.bag;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.kidbear.plane.util.cache.MCSupport;

@Entity
@Table(name = "Item", indexes = {
		@Index(name = "query_item", columnList = "userid"),
		@Index(name = "query_item_id", columnList = "userid,itemId") })
public class ItemInfo implements MCSupport{
	/** 
	* @Fields serialVersionUID : TODO
	*/ 
	private static final long serialVersionUID = 2708967183362709794L;
	@Id
	private long id;
	private long userid;
	private int itemId;
	private int count;
	private int type;// 物品类型，1-卡牌碎片，2-装备碎片，3-消耗品

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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	@Override
	public long getIdentifier() {
		return id;
	}
}
