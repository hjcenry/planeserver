package com.kidbear._36.manager.zhenxing;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.kidbear._36.util.cache.MCSupport;

/** --zzid表示阵型编号 zxishave 表示是否有武将 **/
@Entity
@Table(name = "ZhenXing")
public class ZhenXingInfo {
	@Id
	private long id;
	private long userid;
	@Column(columnDefinition = "INT default 0")
	private long zx1;
	@Column(columnDefinition = "INT default 0")
	private long zx2;
	@Column(columnDefinition = "INT default 0")
	private long zx3;
	@Column(columnDefinition = "INT default 0")
	private long zx4;
	@Column(columnDefinition = "INT default 0")
	private long zx5;
	@Column(columnDefinition = "INT default 0")
	private long zx6;
	@Column(columnDefinition = "INT default 0")
	private long zx7;
	@Column(columnDefinition = "INT default 0")
	private long zx8;
	@Column(columnDefinition = "INT default 0")
	private long zx9;
	private long zxId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getZx1() {
		return zx1;
	}

	public void setZx1(long zx1) {
		this.zx1 = zx1;
	}

	public long getZx2() {
		return zx2;
	}

	public void setZx2(long zx2) {
		this.zx2 = zx2;
	}

	public long getZx3() {
		return zx3;
	}

	public void setZx3(long zx3) {
		this.zx3 = zx3;
	}

	public long getZx4() {
		return zx4;
	}

	public void setZx4(long zx4) {
		this.zx4 = zx4;
	}

	public long getZx5() {
		return zx5;
	}

	public void setZx5(long zx5) {
		this.zx5 = zx5;
	}

	public long getZx6() {
		return zx6;
	}

	public void setZx6(long zx6) {
		this.zx6 = zx6;
	}

	public long getZx7() {
		return zx7;
	}

	public void setZx7(long zx7) {
		this.zx7 = zx7;
	}

	public long getZx8() {
		return zx8;
	}

	public void setZx8(long zx8) {
		this.zx8 = zx8;
	}

	public long getZx9() {
		return zx9;
	}

	public void setZx9(long zx9) {
		this.zx9 = zx9;
	}

	public long getZxId() {
		return zxId;
	}

	public void setZxId(long zxId) {
		this.zxId = zxId;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

}
