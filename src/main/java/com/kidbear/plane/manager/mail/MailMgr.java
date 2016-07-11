package com.kidbear.plane.manager.mail;

import io.netty.channel.ChannelHandlerContext;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kidbear.plane.manager.bag.BagMgr;
import com.kidbear.plane.manager.junzhu.JunZhu;
import com.kidbear.plane.net.ProtoMessage;
import com.kidbear.plane.net.ResultCode;
import com.kidbear.plane.net.http.HttpInHandler;
import com.kidbear.plane.util.hibernate.HibernateUtil;
import com.kidbear.plane.util.hibernate.TableIDCreator;

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

	/**
	 * @Title: pickItems
	 * @Description: 领取邮件
	 * @param ctx
	 * @param msg
	 *            void
	 * @throws
	 */
	public void pickItems(ChannelHandlerContext ctx, ProtoMessage msg,
			Long userid) {
	}

	/**
	 * @Title: readMail
	 * @Description: 读取邮件
	 * @param ctx
	 * @param msg
	 *            void
	 * @throws
	 */
	public void readMail(ChannelHandlerContext ctx, ProtoMessage msg) {
	}

	/**
	 * @Title: queryMail
	 * @Description: 查询邮件
	 * @param ctx
	 * @param msg
	 *            void
	 * @throws
	 */
	public void queryMail(ChannelHandlerContext ctx, Long userid) {
	}

	/**
	 * @param userid
	 * @Title: deleteMail
	 * @Description: 批量删除邮件
	 * @param ctx
	 * @param msg
	 *            void
	 * @throws
	 */
	public void deleteMail(ChannelHandlerContext ctx, ProtoMessage msg) {
	}

	/**
	 * @Title: sendMail
	 * @Description: 发送邮件
	 * @param userid
	 * @param title
	 * @param content
	 * @param items
	 *            附件格式,参考http://123.59.110.201:8000/showdoc/index.php/home/item/
	 *            show?item_id=5
	 * @param senderName
	 *            void
	 * @throws
	 */
	public void sendMail(long userid, String title, String content,
			String items, String senderName) {
	}

	/**
	 * @Title: gmSendMail
	 * @Description: gm邮件发送
	 * @param uids
	 * @param title
	 * @param content
	 * @param items
	 * @param senderName
	 *            void
	 * @throws
	 */
	public void gmSendMail(final List<String> uids, final String title,
			final String content, final String items, final String senderName) {
	}

}
