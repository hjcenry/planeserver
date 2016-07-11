package com.kidbear.plane.net.socket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author lilinfeng
 * @date 2014年2月14日
 * @version 1.0
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
	private WebSocketServerHandlerImp handler;

	@Override
	public void messageReceived(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		handler.messageReceived(ctx, msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		handler.channelReadComplete(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		handler.exceptionCaught(ctx, cause);
	}

	public static void writeJSON(ChannelHandlerContext ctx, Object msg) {
		WebSocketServerHandlerImp.writeJSON(ctx, msg);
	}
}
