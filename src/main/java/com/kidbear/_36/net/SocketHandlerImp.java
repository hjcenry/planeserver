package com.kidbear._36.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kidbear._36.core.GameServer;
import com.kidbear._36.core.Router;
import com.kidbear._36.util.Constants;
import com.kidbear._36.util.JsonUtils;

public class SocketHandlerImp {
	private static Logger log = LoggerFactory.getLogger(SocketHandlerImp.class);

	public void channelRead(final ChannelHandlerContext ctx, final Object msg)
			throws Exception {
		SocketServer.handleTaskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				if (!GameServer.shutdown) {// 服务器开启的情况下
					ByteBuf buf = (ByteBuf) msg;
					byte[] req = new byte[buf.readableBytes()];
					buf.readBytes(req);
					String body = new String(req, CharsetUtil.UTF_8);
					if (Constants.MSG_LOG_DEBUG) {
						log.info("server received: " + body);
					}
					ProtoMessage protoMessage = null;
					try {
						protoMessage = (ProtoMessage) JsonUtils.jsonToBean(
								body, ProtoMessage.class);
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
		});

	}

	public static void writeJSON(ChannelHandlerContext ctx, Object msg) {
		String sentMsg = JsonUtils.objectToJson(msg);
		if (Constants.MSG_LOG_DEBUG) {
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
