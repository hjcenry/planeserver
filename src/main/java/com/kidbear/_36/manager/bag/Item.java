package com.kidbear._36.manager.bag;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="Item")
public class Item {
	private int type;// 物品类型，1-武将，2-装备，3-
}
