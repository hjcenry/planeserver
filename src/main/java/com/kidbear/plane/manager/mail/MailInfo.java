package com.kidbear.plane.manager.mail;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.kidbear.plane.util.cache.MCSupport;

@Entity
@Table(name = "Mail", indexes = { @Index(name = "query_mail", columnList = "userid") })
public class MailInfo implements MCSupport {
	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = -4126389720545037835L;
	@Id
	private long id;
	private String title;
	private int type;
	@Column(name = "content", columnDefinition = "TEXT")
	private String content;
	private String items;// TODO 奖励，格式暂定
	private Date sendDate;
	private String sendName;// 发送人
	private long userid;// 接收人
	private int isRead;// 是否阅读
	private int isPick;// 是否领取附件

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

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}

	public int getIsPick() {
		return isPick;
	}

	public void setIsPick(int isPick) {
		this.isPick = isPick;
	}

	@Override
	public long getIdentifier() {
		return id;
	}

}
