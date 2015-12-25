package com.kidbear._36.notification;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kidbear._36.core.GameInit;
import com.kidbear._36.core.GameServer;

@Controller
@RequestMapping(value = "/notify")
public class NotifyController {
	public Logger logger = LoggerFactory.getLogger(NotifyController.class);

	@RequestMapping(value = "operateStart", method = RequestMethod.POST)
	@ResponseBody
	public String operateStart(HttpServletRequest request) {
		if (GameServer.shutdown) {
			logger.info("{} operate server to start", request.getRemoteHost());
			boolean flag = GameInit.init();
			if (flag) {
				return "success";
			}
			return "failed";
		}
		return "started";
	}

	@RequestMapping(value = "operateShut", method = RequestMethod.POST)
	@ResponseBody
	public String operateShut(HttpServletRequest request) {
		if (!GameServer.shutdown) {
			logger.info("{} operate server to shutdown",
					request.getRemoteHost());
			boolean flag = GameInit.shutdown();
			if (flag) {
				return "success";
			}
			return "failed";
		}
		return "shutted";
	}
}
