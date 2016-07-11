package com.kidbear.plane.gm.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kidbear.plane.core.GameInit;
import com.kidbear.plane.manager.junzhu.JunZhu;
import com.kidbear.plane.manager.mail.MailInfo;
import com.kidbear.plane.manager.mail.MailMgr;
import com.kidbear.plane.util.hibernate.HibernateUtil;

@Controller
@RequestMapping(value = "/activity")
public class ActivityController {
	public Logger logger = LoggerFactory.getLogger(ActivityController.class);

	@RequestMapping(value = "/query")
	public void query() {

	}

	@RequestMapping(value = "/queryHandle")
	@ResponseBody
	public String queryHandle(HttpServletRequest request) {
		String userIDStr = request.getParameter("userID");
		if (userIDStr == null || userIDStr.length() == 0) {
			return "fail";
		}
		long junZhuId = Long.parseLong(userIDStr);
		JunZhu junZhu = HibernateUtil.find(JunZhu.class, junZhuId);
		if (junZhu == null) {
			return "fail";
		}
		return "success";
	}

	@RequestMapping(value = "/award")
	public void award(HttpServletRequest request) {
	}

	@RequestMapping(value = "/awardlist")
	public void awardlist(HttpServletRequest request, Model model) {
		String userIDStr = request.getParameter("userID");
		long userID = Long.parseLong(userIDStr);
		List<MailInfo> mails = HibernateUtil.list(MailInfo.class, userID,
				"where userid=" + userID + "");
		model.addAttribute("mails", mails);
		model.addAttribute("serverId", GameInit.serverId);
		model.addAttribute("userid", userID);
	}

	@RequestMapping(value = "/giveAward")
	@ResponseBody
	public String giveAward(HttpServletRequest request) {
		// String content = request.getParameter("content");
		String uids = request.getParameter("uids");
		String items = request.getParameter("awards");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String senderName = request.getParameter("senderName");
		try {
			List<String> uidList = new ArrayList<String>();
			if (uids != null && uids.length() > 0) {
				uidList = Arrays.asList(uids.split(","));
			}
			MailMgr.getInstance().gmSendMail(uidList, title, content, items,
					senderName);
		} catch (Exception e) {
			return "failed";
		}
		return "success";
	}

	@RequestMapping(value = "/delMail")
	@ResponseBody
	public String delMail(HttpServletRequest request) {
		String mailIdStr = request.getParameter("mailId");
		long mailId = Long.parseLong(mailIdStr);
		MailInfo mail = HibernateUtil.find(MailInfo.class, mailId);
		Throwable t = HibernateUtil.delete(mail, mail.getUserid());
		if (t == null) {
			return "success";
		}
		return "failed";
	}
}
