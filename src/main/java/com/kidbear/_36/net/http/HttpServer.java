package com.kidbear._36.net.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class HttpServer {
	public static Logger log = LoggerFactory.getLogger(HttpServer.class);
	public static HttpServer inst;
	public static Properties p;
	public static int port;
	private NioEventLoopGroup bossGroup = new NioEventLoopGroup();
	private NioEventLoopGroup workGroup = new NioEventLoopGroup();
	public static ThreadPoolTaskExecutor handleTaskExecutor;// 处理消息线程池

	private HttpServer() {// 线程池初始化

	}

	/** 
	* @Title: initThreadPool 
	* @Description: 初始化线程池
	* void
	* @throws 
	*/
	public void initThreadPool() {
		handleTaskExecutor = new ThreadPoolTaskExecutor();
		// 线程池所使用的缓冲队列
		handleTaskExecutor.setQueueCapacity(Integer.parseInt(p
				.getProperty("handleTaskQueueCapacity")));
		// 线程池维护线程的最少数量
		handleTaskExecutor.setCorePoolSize(Integer.parseInt(p
				.getProperty("handleTaskCorePoolSize")));
		// 线程池维护线程的最大数量
		handleTaskExecutor.setMaxPoolSize(Integer.parseInt(p
				.getProperty("handleTaskMaxPoolSize")));
		// 线程池维护线程所允许的空闲时间
		handleTaskExecutor.setKeepAliveSeconds(Integer.parseInt(p
				.getProperty("handleTaskKeepAliveSeconds")));
		handleTaskExecutor.initialize();
	}

	public static HttpServer getInstance() {
		if (inst == null) {
			inst = new HttpServer();
			inst.initData();
			inst.initThreadPool();
		}
		return inst;
	}

	public void initData() {
		try {
			p = readProperties();
			port = Integer.parseInt(p.getProperty("port"));
		} catch (IOException e) {
			log.error("socket配置文件读取错误");
			e.printStackTrace();
		}
	}

	public void start() {
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workGroup);
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast("decoder", new HttpRequestDecoder());
				pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
				pipeline.addLast("encoder", new HttpResponseEncoder());
				pipeline.addLast("http-chunked", new ChunkedWriteHandler());
				pipeline.addLast("handler", new HttpHandler());
			}
		});
		log.info("端口{}已绑定", port);
		bootstrap.bind(port);
	}

	public void shut() {
		workGroup.shutdownGracefully();
		workGroup.shutdownGracefully();
		SessionMgr.getInstance().removeAll();
		log.info("端口{}已解绑", port);
	}

	/**
	 * 读配置socket文件
	 * 
	 * @return
	 * @throws IOException
	 */
	protected Properties readProperties() throws IOException {
		Properties p = new Properties();
		InputStream in = HttpServer.class
				.getResourceAsStream("/net.properties");
		Reader r = new InputStreamReader(in, Charset.forName("UTF-8"));
		p.load(r);
		in.close();
		return p;
	}
}
