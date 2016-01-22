package com.kidbear._36.net.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.CharsetUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kidbear._36.core.GameServer;
import com.kidbear._36.core.Router;
import com.kidbear._36.util.Constants;
import com.kidbear._36.util.JsonUtils;
import com.kidbear._36.util.encrypt.XXTeaCoder;

public class HttpHandlerImp {
	private static Logger log = LoggerFactory.getLogger(HttpHandlerImp.class);
	public static String DATA = "data";
	public static volatile boolean DECODE_SWITCH = false;// 开启解密
	public static volatile boolean ENCODE_SWITCH = false;// 开启加密

	public void channelRead(final ChannelHandlerContext ctx, final Object msg)
			throws Exception {
		HttpServer.handleTaskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				if (!GameServer.shutdown) {// 服务器开启的情况下
					DefaultFullHttpRequest req = (DefaultFullHttpRequest) msg;
					if (req.getMethod() == HttpMethod.GET) { // 处理get请求
						getHandle(ctx, req);
					}
					if (req.getMethod() == HttpMethod.POST) { // 处理POST请求
						postHandle(ctx, req);
					}
				} else {// 服务器已关闭
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("errMsg", "server closed");
					writeJSON(ctx, jsonObject);
				}
			}

		});
	}

	/**
	 * @Title: postHandle
	 * @Description: POST请求处理
	 * @param ctx
	 * @param req
	 *            void
	 * @throws
	 */
	private void postHandle(final ChannelHandlerContext ctx,
			DefaultFullHttpRequest req) {
		HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(
				new DefaultHttpDataFactory(false), req);
		InterfaceHttpData postGameData = decoder.getBodyHttpData(DATA);
		try {
			if (postGameData != null) {
				String val = ((Attribute) postGameData).getValue();
				val = decodeFilter(val);
				Router.getInstance().route(val, ctx);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	/**
	 * @Title: getHandle
	 * @Description: GET请求处理
	 * @param ctx
	 * @param req
	 *            void
	 * @throws
	 */
	private void getHandle(final ChannelHandlerContext ctx,
			DefaultFullHttpRequest req) {
		// QueryStringDecoder decoder = new QueryStringDecoder(req.getUri());
		// Map<String, List<String>> params = decoder.parameters();
		// if (params.containsKey(DATA)) {
		// List<String> list = params.get(DATA);
		// for (String val : list) {
		// val = codeFilter(val);
		// if (Constants.MSG_LOG_DEBUG) {
		// log.info("server received : {}", val);
		// }
		// // Router.getInstance().route(val, ctx);
		// }
		// }
		writeJSON(ctx, HttpResponseStatus.NOT_IMPLEMENTED, "not implement");
	}

	/**
	 * @Title: codeFilter
	 * @Description: 编解码过滤
	 * @param val
	 * @return String
	 * @throws
	 */
	private String decodeFilter(String val) {
		try {
			val = val.contains("%") ? URLDecoder.decode(val, "UTF-8") : val;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String valTmp = val;
		val = DECODE_SWITCH ? XXTeaCoder.decryptBase64StringToString(val,
				XXTeaCoder.key) : val;
		if (Constants.MSG_LOG_DEBUG) {
			if (val == null) {
				val = valTmp;
			}
			log.info("server received : {}", val);
		}
		return val;
	}

	private static String encodeFilter(String val) {
		if (ENCODE_SWITCH) {
			val = ENCODE_SWITCH ? XXTeaCoder.encryptToBase64String(val,
					XXTeaCoder.key) : val;
		}
		return val;
	}

	public static void writeJSON(ChannelHandlerContext ctx,
			HttpResponseStatus status, Object msg) {
		String sentMsg = JsonUtils.objectToJson(msg);
		if (Constants.MSG_LOG_DEBUG) {
			log.info("server sent : {}", sentMsg);
		}
		sentMsg = encodeFilter(sentMsg);
		writeJSON(ctx, status,
				Unpooled.copiedBuffer(sentMsg, CharsetUtil.UTF_8));
		ctx.flush();
	}

	public static void writeJSON(ChannelHandlerContext ctx, Object msg) {
		writeJSON(ctx, HttpResponseStatus.OK, msg);
	}

	private static void writeJSON(ChannelHandlerContext ctx,
			HttpResponseStatus status, ByteBuf content/* , boolean isKeepAlive */) {
		if (ctx.channel().isWritable()) {
			FullHttpResponse msg = null;
			if (content != null) {
				msg = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
						content);
				msg.headers().set(HttpHeaders.Names.CONTENT_TYPE,
						"application/json; charset=utf-8");
			} else {
				msg = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);
			}
			if (msg.content() != null) {
				msg.headers().set(HttpHeaders.Names.CONTENT_LENGTH,
						msg.content().readableBytes());
			}
			// not keep-alive
			ctx.write(msg).addListener(ChannelFutureListener.CLOSE);
			boolean f = content.release();
			System.out.println(f);
		}
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
	}

	public void messageReceived(ChannelHandlerContext ctx, FullHttpRequest msg)
			throws Exception {

	}
}
