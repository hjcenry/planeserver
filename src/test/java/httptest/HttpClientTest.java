package httptest;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpVersion;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import com.kidbear._36.util.HttpClient;

public class HttpClientTest {

	public void connect(String host, int port) throws Exception {
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					// 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
					ch.pipeline().addLast(new HttpResponseDecoder());
					// 客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
					ch.pipeline().addLast(new HttpRequestEncoder());
					ch.pipeline().addLast(new HttpClientHandler());
				}
			});

			// Start the client.
			ChannelFuture f = b.connect(host, port).sync();

			URI uri = new URI("http://127.0.0.1:8844");
			String msg = "Are you ok?";
			DefaultFullHttpRequest request = new DefaultFullHttpRequest(
					HttpVersion.HTTP_1_1, HttpMethod.GET, uri.toASCIIString(),
					Unpooled.wrappedBuffer(msg.getBytes("UTF-8")));

			// 构建http请求
			request.headers().set(HttpHeaders.Names.HOST, host);
			request.headers().set(HttpHeaders.Names.CONNECTION,
					HttpHeaders.Values.KEEP_ALIVE);
			request.headers().set(HttpHeaders.Names.CONTENT_LENGTH,
					request.content().readableBytes());
			// 发送http请求
			f.channel().write(request);
			f.channel().flush();
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
		}

	}

	public static void main(String[] args) throws Exception {
		// URL url = new URL("http://127.0.0.1:81");
		// URLConnection conn = (HttpURLConnection)url.openConnection();
		// conn.setDoOutput(true);
		// conn.connect();
		// OutputStream out = conn.getOutputStream();
		// out.write(new String("hello test").getBytes());
		// out.flush();
		// out.close();
		// String result = HttpClient.get("http://127.0.0.1:81?a=1");
		// System.out.println(result);
		// HttpClientTest client = new HttpClientTest();
		// client.connect("127.0.0.1", 81);
		Map<String, String> map = new HashMap<String, String>();
		map.put("a", "111");
		map.put("b", "111");
		map.put("c", "111");
		map.put("d", "111");
		String result = HttpClient.get("http://localhost:8585/hero/core/test", map);
		System.out.println(result);
	}

}
