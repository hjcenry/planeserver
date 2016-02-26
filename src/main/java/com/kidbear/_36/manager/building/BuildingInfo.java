package com.kidbear._36.manager.building;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.kidbear._36.util.cache.MCSupport;

@Entity
@Table(name = "Building")
public class BuildingInfo implements MCSupport {
	private static final long serialVersionUID = 3541431553998148015L;
	@Id
	private long id;
	private int junjichu;
	private int huangcheng;
	private int zhaoshangju;
	private int jiaochang;
	private int bingying;
	private int cangku;
	private int minju1;
	private int minju2;
	private int minju3;
	private int minju4;
	private int minju5;
	private int minju6;
	private int nongtian1;
	private int nongtian2;
	private int nongtian3;
	private int nongtian4;
	private int nongtian5;
	private int nongtian6;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public int getJiaochang() {
		return jiaochang;
	}

	public void setJiaochang(int jiaochang) {
		this.jiaochang = jiaochang;
	}

	public int getBingying() {
		return bingying;
	}

	public void setBingying(int bingying) {
		this.bingying = bingying;
	}

	public int getMinju1() {
		return minju1;
	}

	public void setMinju1(int minju1) {
		this.minju1 = minju1;
	}

	public int getMinju2() {
		return minju2;
	}

	public void setMinju2(int minju2) {
		this.minju2 = minju2;
	}

	public int getMinju3() {
		return minju3;
	}

	public void setMinju3(int minju3) {
		this.minju3 = minju3;
	}

	public int getMinju4() {
		return minju4;
	}

	public void setMinju4(int minju4) {
		this.minju4 = minju4;
	}

	public int getMinju5() {
		return minju5;
	}

	public void setMinju5(int minju5) {
		this.minju5 = minju5;
	}

	public int getMinju6() {
		return minju6;
	}

	public void setMinju6(int minju6) {
		this.minju6 = minju6;
	}

	public int getNongtian1() {
		return nongtian1;
	}

	public void setNongtian1(int nongtian1) {
		this.nongtian1 = nongtian1;
	}

	public int getNongtian2() {
		return nongtian2;
	}

	public void setNongtian2(int nongtian2) {
		this.nongtian2 = nongtian2;
	}

	public int getNongtian3() {
		return nongtian3;
	}

	public void setNongtian3(int nongtian3) {
		this.nongtian3 = nongtian3;
	}

	public int getNongtian4() {
		return nongtian4;
	}

	public void setNongtian4(int nongtian4) {
		this.nongtian4 = nongtian4;
	}

	public int getNongtian5() {
		return nongtian5;
	}

	public void setNongtian5(int nongtian5) {
		this.nongtian5 = nongtian5;
	}

	public int getNongtian6() {
		return nongtian6;
	}

	public void setNongtian6(int nongtian6) {
		this.nongtian6 = nongtian6;
	}

	@Override
	public long getIdentifier() {
		return id;
	}

	public int getCangku() {
		return cangku;
	}

	public void setCangku(int cangku) {
		this.cangku = cangku;
	}

}
