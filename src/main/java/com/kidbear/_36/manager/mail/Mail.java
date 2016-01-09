package com.kidbear._36.manager.mail;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Mail")
public class Mail {
	private long id;
	private String title;
	private int type;
	@Column(name = "content", columnDefinition = "TEXT")
	private String content;
	private String awards;// TODO 奖励，格式暂定
	private Date sendDate;
	private String sendName;// 发送人
	private long receiver;// 接收人
	private int isRead;// 是否阅读
	private int isGiveAward;// 是否领取附件

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAwards() {
		return awards;
	}

	public void setAwards(String awards) {
		this.awards = awards;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public String getSendName() {
		return sendName;
	}

	public void setSendName(String sendName) {
		this.sendName = sendName;
	}

	public long getReceiver() {
		return receiver;
	}

	public void setReceiver(long receiver) {
		this.receiver = receiver;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public int getIsGiveAward() {
		return isGiveAward;
	}

	public void setIsGiveAward(int isGiveAward) {
		this.isGiveAward = isGiveAward;
	}

}
