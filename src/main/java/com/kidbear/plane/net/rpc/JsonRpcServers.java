package com.kidbear.plane.net.rpc;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Properties;

import com.googlecode.jsonrpc4j.JsonRpcServer;
import com.kidbear.plane.manager.shop.ShopMgr;
import com.kidbear.plane.net.http.HttpServer;

public class JsonRpcServers {
	public static String pvpIp;
	public static int pvpPort;
	public static String payServer;
	public static int payPort;
	public static Properties p;
	public static JsonRpcServer payRpcServer;

	public static void initData() {
		try {
			p = readProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}
		pvpPort = Integer.parseInt(p.getProperty("pvpPort"));
		pvpIp = p.getProperty("pvpIp");
		payServer = p.getProperty("payRpcIp");
		payPort = Integer.parseInt(p.getProperty("payRpcPort"));
		initRpcServers();
	}

	protected static void initRpcServers() {
		payRpcServer = new JsonRpcServer(ShopMgr.getInstance(), ShopMgr.class);
	}

	protected static Properties readProperties() throws IOException {
		Properties p = new Properties();
		InputStream in = HttpServer.class
				.getResourceAsStream("/net.properties");
		Reader r = new InputStreamReader(in, Charset.forName("UTF-8"));
		p.load(r);
		in.close();
		return p;
	}
}
