package com.kidbear.plane.manager.account;

import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kidbear.plane.core.GameInit;
import com.kidbear.plane.net.ProtoIds;
import com.kidbear.plane.net.ProtoMessage;
import com.kidbear.plane.net.http.HttpInHandler;
import com.kidbear.plane.net.http.SessionMgr;
import com.kidbear.plane.net.message.LoginReq;
import com.kidbear.plane.net.message.LoginResp;
import com.kidbear.plane.net.socket.ChannelMgr;
import com.kidbear.plane.net.socket.SocketHandler;

/**
 * @ClassName: AccountMgr
 * @Description: TODO
 * @author 何金成
 * @date 2015年12月14日 下午5:34:21
 * 
 */
public class AccountMgr {
	private static AccountMgr accountMgr;
	private static final Logger logger = LoggerFactory
			.getLogger(AccountMgr.class);

	private AccountMgr() {
	}

	public static AccountMgr getInstance() {
		if (null == accountMgr) {
			accountMgr = new AccountMgr();
		}
		return accountMgr;
	}

	public void initData() {
		logger.info("AccountMgr initData");
	}

	/**
	 * @Title: login
	 * @Description: 登录后的处理
	 * @param cmd
	 * @param accountReq
	 * @param session
	 * @return void
	 * @throws
	 */
	public void login(ChannelHandlerContext ctx, long userid) {
		// 与router服务器通信验证是否登录
		// boolean flag = ServerNotify.validateLogin(accId);
		// if (flag) {
		// resp.setCode(1);
		// resp.setErrMsg("登录服验证账号" + accId + " 登录验证失败");
		// logger.error("登录服验证账号 {} 登录验证失败", accId);
		// SocketHandler.writeJSON(ctx, resp);
		// return;
		// }
		logger.info("用户 {} 登录成功", userid);
		// 玩家登录到本服务器，Channel进行记录
		// ChannelMgr.getInstance().addChannelUser(ctx, userId);
		// SessionMgr.getInstance().addUser(userid);
		// response
		HttpInHandler.writeJSON(ctx, ProtoMessage.getSuccessResp());
	}

	/**
	 * @Title: logout
	 * @Description: 下线后的处理
	 * @param ctx
	 * @return void
	 * @throws
	 */
	public void logout(ChannelHandlerContext ctx) {
		// ChannelMgr.getInstance().removeChannel(ctx);
	}
}
