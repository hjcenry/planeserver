package com.kidbear._36.manager.mail;

import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailMgr {
	private static MailMgr mailMgr;
	private static final Logger logger = LoggerFactory.getLogger(MailMgr.class);

	private MailMgr() {
	}

	public static MailMgr getInstance() {
		if (null == mailMgr) {
			mailMgr = new MailMgr();
		}
		return mailMgr;
	}

	public void initData() {
		logger.info("MailMgr initData");
	}
	
	public void giveAwards(ChannelHandlerContext ctx){
		
	}
	
	public void read(ChannelHandlerContext ctx){
		
	}
	
	public void getMailList(ChannelHandlerContext ctx){
		
	}
	
	public void deleteMailList(ChannelHandlerContext ctx){
		
	}
}
