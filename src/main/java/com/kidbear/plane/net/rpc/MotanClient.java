package com.kidbear.plane.net.rpc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kidbear.pay.client.pay.IPay;

public class MotanClient {
	private static ApplicationContext ctx;
	static {
		ctx = new ClassPathXmlApplicationContext(
				"classpath:spring-motan/motan_client.xml");
	}

	public static <T> T getService(Class<T> clz) {
		return ctx.getBean(clz);
	}

}