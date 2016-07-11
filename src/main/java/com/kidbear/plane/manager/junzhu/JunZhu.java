package com.kidbear.plane.manager.junzhu;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.kidbear.plane.util.cache.MCSupport;

@Entity
@Table(name = "JunZhu")
public class JunZhu implements MCSupport {
	// id，用户id，用户名字，用户所属国家，用户头像（用数字表示），用户等级，用户vip等级，用户军令数，用户金宝，用户银宝，用户粮食，用户精铁，用户木材，
	// 用户兵数量，用户军工数，用户将魂数
	private static final long serialVersionUID = -5385044598250102957L;
	@Id
	public long id;// —用户id
	public String name;// — 用户名
	@Column(columnDefinition = "INT default 1")
	public int headImg;// —用户头像
	public int role;// —用户角色
	public int country;// —用户所属国家 1表示蜀国 2表示魏国 3表示吴国
	@Column(columnDefinition = "INT default 0")
	public int exp;// —用户等级 —君主等级
	@Column(columnDefinition = "INT default 1")
	public int level;// —用户等级 —君主等级
	@Column(columnDefinition = "INT default 0")
	public int vip;// —用户vip等级
	@Column(columnDefinition = "INT default 0")
	public int vipExp;// —用户vip经验
	@Column(columnDefinition = "INT default 0")
	public int junling;// —用户军令数
	@Column(columnDefinition = "INT default 0")
	public int coin;// —用户金币
	@Column(columnDefinition = "INT default 0")
	public int yuanbao;// —用户元宝
	@Column(columnDefinition = "INT default 0")
	public int food;// —用户粮食
	@Column(columnDefinition = "INT default 0")
	public int iron;// —用户精铁
	@Column(columnDefinition = "INT default 0")
	public int wood;// —用户木材
	@Column(columnDefinition = "INT default 0")
	public int soldierNum;// —用户兵力
	@Column(columnDefinition = "INT default 0")
	public int jungongNum;// —用户军功数
	@Column(columnDefinition = "INT default 0")
	public int jianghunNum;// —将魂数量
	@Column(columnDefinition = "INT default 0")
	public int shengwang;// —声望
	@Column(columnDefinition = "INT default 0")
	public int zxId;// —阵型id

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

	public int getHeadImg() {
		return headImg;
	}

	public void setHeadImg(int headImg) {
		this.headImg = headImg;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
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

	public int getJunling() {
		return junling;
	}

	public void setJunling(int junling) {
		this.junling = junling;
	}

	public int getCoin() {
		return coin;
	}

	public void setCoin(int coin) {
		this.coin = coin;
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

	public int getSoldierNum() {
		return soldierNum;
	}

	public void setSoldierNum(int soldierNum) {
		this.soldierNum = soldierNum;
	}

	public int getJungongNum() {
		return jungongNum;
	}

	public void setJungongNum(int jungongNum) {
		this.jungongNum = jungongNum;
	}

	public int getJianghunNum() {
		return jianghunNum;
	}

	public void setJianghunNum(int jianghunNum) {
		this.jianghunNum = jianghunNum;
	}

	@Override
	public long getIdentifier() {
		return id;
	}

	public int getYuanbao() {
		return yuanbao;
	}

	public void setYuanbao(int yuanbao) {
		this.yuanbao = yuanbao;
	}

	public int getZxId() {
		return zxId;
	}

	public void setZxId(int zxId) {
		this.zxId = zxId;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getVipExp() {
		return vipExp;
	}

	public void setVipExp(int vipExp) {
		this.vipExp = vipExp;
	}

	public int getShengwang() {
		return shengwang;
	}

	public void setShengwang(int shengwang) {
		this.shengwang = shengwang;
	}

}
