package com.kidbear._36.net;

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
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kidbear._36.core.GameServer;
import com.kidbear._36.util.JsonUtils;

public class SocketHandlerImp {
	private static Logger log = LoggerFactory.getLogger(SocketHandlerImp.class);
	public static boolean DEBUG_LOG = true;

	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		if (!GameServer.shutdown) {// 服务器开启的情况下
			ByteBuf buf = (ByteBuf) msg;
			byte[] req = new byte[buf.readableBytes()];
			buf.readBytes(req);
			String body = new String(req, CharsetUtil.UTF_8);
			if (DEBUG_LOG) {
				log.info("server received: " + body);
			}
			ProtoMessage protoMessage = null;
			try {
				protoMessage = (ProtoMessage) JsonUtils.jsonToBean(body,
						ProtoMessage.class);
			} catch (Exception e) {
				log.error("json序列化错误");
			}
			Router.getInstance().route(protoMessage, ctx);
		} else {// 服务器已关闭
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("errMsg", "server closed");
			writeJSON(ctx, jsonObject);
		}

	}

	public static void writeJSON(ChannelHandlerContext ctx, Object msg) {
		String sentMsg = JsonUtils.objectToJson(msg);
		if (DEBUG_LOG) {
			log.info("server sent : {}", sentMsg);
		}
		if (ctx.channel().isWritable()) {
			ByteBuf resp = Unpooled.copiedBuffer(sentMsg
					.getBytes(CharsetUtil.UTF_8));
			ctx.write(resp);
			ctx.flush();
		}
	}

	public void messageReceived(ChannelHandlerContext ctx, String msg)
			throws Exception {
	}
}
