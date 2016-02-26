package com.kidbear._36.manager.mail;

import io.netty.channel.ChannelHandlerContext;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kidbear._36.manager.bag.BagMgr;
import com.kidbear._36.net.ProtoMessage;
import com.kidbear._36.net.ResultCode;
import com.kidbear._36.net.http.HttpHandler;
import com.kidbear._36.util.hibernate.HibernateUtil;

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
		JSONObject req = msg.getData();
		if (!req.containsKey("mailid")) {
			logger.error("没有mailid");
			HttpHandler.writeJSON(ctx,
					ProtoMessage.getErrorResp(ResultCode.PARAM_ERR));
			return;
		}
		JSONArray mailIds = req.getJSONArray("mailid");
		for (int i = 0; i < mailIds.size(); i++) {
			long mailId = mailIds.getLong(i);
			MailInfo mail = HibernateUtil.find(MailInfo.class, mailId);
			if (mail == null) {
				logger.error("领取邮件，mailid为{}的邮件不存在");
			}
			// 物品发放
			JSONArray items = JSON.parseArray(mail.getItems());
			for (int j = 0; j < items.size(); j++) {
				JSONArray item = items.getJSONArray(j);
				int itemId = item.getIntValue(0);
				int count = item.getIntValue(1);
				int type = item.getIntValue(2);
				BagMgr.getInstance().addItem(type, itemId, count, userid);
			}
			mail.setIsPick(1);
			mail.setIsRead(1);
			HibernateUtil.save(mail);
		}
		HttpHandler.writeJSON(ctx, ProtoMessage.getSuccessResp());

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
		JSONObject req = msg.getData();
		if (!req.containsKey("mailid")) {
			logger.error("没有mailid");
			HttpHandler.writeJSON(ctx,
					ProtoMessage.getErrorResp(ResultCode.PARAM_ERR));
			return;
		}
		JSONArray mailIds = req.getJSONArray("mailid");
		for (int i = 0; i < mailIds.size(); i++) {
			long mailId = mailIds.getLong(i);
			MailInfo mail = HibernateUtil.find(MailInfo.class, mailId);
			if (mail == null) {
				logger.error("读邮件，mailid为{}的邮件不存在");
			}
			mail.setIsRead(1);
			HibernateUtil.save(mail);
		}
		HttpHandler.writeJSON(ctx, ProtoMessage.getSuccessResp());
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
		List<MailInfo> mails = HibernateUtil.list(MailInfo.class,
				"where userid=" + userid + " order by sendDate DESC");
		JSONArray mailJsonArray = new JSONArray();
		for (MailInfo mail : mails) {
			JSONObject tmp = new JSONObject();
			tmp.put("mailId", mail.getId());
			tmp.put("title", mail.getTitle());
			tmp.put("content", mail.getContent());
			tmp.put("sendDate", mail.getSendDate().toLocaleString());
			tmp.put("sendeName", mail.getSendName());
			tmp.put("type", mail.getType());
			tmp.put("items", mail.getItems());
			tmp.put("isRead", mail.getIsRead());
			tmp.put("isPick", mail.getIsPick());
			mailJsonArray.add(tmp);
		}
		JSONObject mailJson = new JSONObject();
		mailJson.put("mails", mailJsonArray);
		HttpHandler.writeJSON(ctx, mailJson);
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
		JSONObject req = msg.getData();
		if (!req.containsKey("mailid")) {
			logger.error("没有mailid");
			HttpHandler.writeJSON(ctx,
					ProtoMessage.getErrorResp(ResultCode.PARAM_ERR));
			return;
		}
		JSONArray mailIds = req.getJSONArray("mailid");
		for (int i = 0; i < mailIds.size(); i++) {
			long mailId = mailIds.getLong(i);
			MailInfo mail = HibernateUtil.find(MailInfo.class, mailId);
			if (mail == null) {
				logger.error("删除邮件，mailid为{}的邮件不存在");
			}
			HibernateUtil.delete(mail);
		}
		HttpHandler.writeJSON(ctx, ProtoMessage.getSuccessResp());
	}
}
