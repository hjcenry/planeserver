package com.kidbear._36.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
* @ClassName: AccountMgr 
* @Description: TODO
* @author 何金成 
* @date 2015年12月14日 下午5:34:21 
*  
*/
public class AccountMgr {
	private static AccountMgr accountMgr;
	private static final Logger logger = LoggerFactory.getLogger(AccountMgr.class);

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

//	/** 
//	 * @Title: login 
//	 * @Description: 登录后的处理
//	 * @param cmd
//	 * @param accountReq
//	 * @param session
//	 * @return void
//	 * @throws 
//	 */
//	public void login(int cmd, LoginReq accountReq, IoSession session) {
//		logger.info("login playerId:{}", accountReq.getPlayerId());
//		// 玩家登录到本服务器，Session进行记录
//		SessionMgr.getInstance().addSessionUser(session, accountReq.getPlayerId());
//		LoginResp response = new LoginResp();
//		response.setResult(0);
//		WarIoHandler.writeMsg(ProtoIds.S_LOGIN_RESP, response, session);
//	}
	
//	/** 
//	 * @Title: logout 
//	 * @Description: 下线后的处理
//	 * @param session
//	 * @return void
//	 * @throws 
//	 */
//	public void logout(IoSession session){
//		SessionMgr.getInstance().remSessionUser(session);
//	}
}
