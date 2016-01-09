package com.kidbear._36.manager.junzhu;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "JunZhu")
public class JunZhu {
	private String name;// — 用户名
	@Id
	private int id;// —用户id
	private int country;// —用户所属国家 1表示蜀国 2表示魏国 3表示吴国
	private int headImg;// —用户头像
	private int level;// —用户等级 —君主等级
	private int vip;// —用户vip等级
	private int lingCount;// —用户军令数
	@Transient
	private int power;// —战力
	private int coin;// —用户金币
	private int acer;// —用户元宝
	private int food;// —用户粮食
	private int iron;// —用户精铁
	private int wood;// —用户木材
	private int forces;// —用户兵力
	private int jungong;// —用户军功数
	private int jianghunNum;// —将魂数量
	private int curentZx;// —用户的当前阵型

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public int getHeadImg() {
		return headImg;
	}

	public void setHeadImg(int headImg) {
		this.headImg = headImg;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

	public int getLingCount() {
		return lingCount;
	}

	public void setLingCount(int lingCount) {
		this.lingCount = lingCount;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public int getCoin() {
		return coin;
	}

	public void setCoin(int coin) {
		this.coin = coin;
	}

	public int getAcer() {
		return acer;
	}

	public void setAcer(int acer) {
		this.acer = acer;
	}

	public int getFood() {
		return food;
	}

	public void setFood(int food) {
		this.food = food;
	}

	public int getIron() {
		return iron;
	}

	public void setIron(int iron) {
		this.iron = iron;
	}

	public int getWood() {
		return wood;
	}

	public void setWood(int wood) {
		this.wood = wood;
	}

	public int getForces() {
		return forces;
	}

	public void setForces(int forces) {
		this.forces = forces;
	}

	public int getJungong() {
		return jungong;
	}

	public void setJungong(int jungong) {
		this.jungong = jungong;
	}

	public int getJianghunNum() {
		return jianghunNum;
	}

	public void setJianghunNum(int jianghunNum) {
		this.jianghunNum = jianghunNum;
	}

	public int getCurentZx() {
		return curentZx;
	}

	public void setCurentZx(int curentZx) {
		this.curentZx = curentZx;
	}

}
