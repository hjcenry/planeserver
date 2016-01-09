package com.kidbear._36.manager.building;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Building")
public class Building {
	@Id
	private long junZhuId;
	private int junjichu;// 军机处
	private int huangcheng;// 皇城
	private int zhaoshangju;// 招商局
	private int xiaochang;// 校场
	private int cangku;// 仓库
	private int bingying;// 兵营
	private int kuangchang;// 矿场

	public long getJunZhuId() {
		return junZhuId;
	}

	public void setJunZhuId(long junZhuId) {
		this.junZhuId = junZhuId;
	}

	public int getJunjichu() {
		return junjichu;
	}

	public void setJunjichu(int junjichu) {
		this.junjichu = junjichu;
	}

	public int getHuangcheng() {
		return huangcheng;
	}

	public void setHuangcheng(int huangcheng) {
		this.huangcheng = huangcheng;
	}

	public int getZhaoshangju() {
		return zhaoshangju;
	}

	public void setZhaoshangju(int zhaoshangju) {
		this.zhaoshangju = zhaoshangju;
	}

	public int getXiaochang() {
		return xiaochang;
	}

	public void setXiaochang(int xiaochang) {
		this.xiaochang = xiaochang;
	}

	public int getCangku() {
		return cangku;
	}

	public void setCangku(int cangku) {
		this.cangku = cangku;
	}

	public int getBingying() {
		return bingying;
	}

	public void setBingying(int bingying) {
		this.bingying = bingying;
	}

	public int getKuangchang() {
		return kuangchang;
	}

	public void setKuangchang(int kuangchang) {
		this.kuangchang = kuangchang;
	}

}
