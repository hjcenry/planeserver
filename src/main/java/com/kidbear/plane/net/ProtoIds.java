package com.kidbear.plane.net;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @ClassName: ProtoIds
 * @Description: 存储协议号，添加协议号时，既要定义静态常量的协议号，也要在init方法中调用regist注册，方便协议号的管理
 * @author 何金成
 * @date 2015年5月23日 下午4:34:41
 * 
 */
public class ProtoIds {
	public static Map<Integer, String> protoClassMap = new HashMap<Integer, String>();// 协议号-类名map存储
	public static Map<String, Integer> classProtoMap = new HashMap<String, Integer>();// 类名-协议号map存储

	// 初始化
	public static void init() {
		// 协议初始化
		// regist(C_LOGIN_REQ, LoginReq.class.getName());
		// regist(S_LOGIN_RESP, LoginResp.class.getName());
	}

	/** 登录请求 **/
	public static final short TEST = 10000;
	public static final short LOGIN = 10001;
	public static final short MAIL_QUERY = 1;// 查询邮件
	public static final short MAIL_PICK = 2;// 领取邮件
	public static final short MAIL_DELETE = 3;// 删除邮件
	public static final short MAIL_READ = 4;// 读取邮件
	public static final short BAG_QUERY = 5;// 读取背包
	public static final short BAG_COMBINE = 6;// 背包合成
	public static final short BUILDING_UPGRADE = 7;// 升级建筑
	public static final short WORKER_QUERY = 8;// 工人查询
	public static final short MAIN_INFO_QUERY = 9;// 主信息查询
	public static final short RESOURCE_PICK = 10;// 领取资源
	public static final short TEC_QUERY = 11;// 查询科技
	public static final short TEC_UPGRADE = 12;// 升级科技
	public static final short HCZS_QUERY = 13;// 查询皇城征收
	public static final short HCZS_PICK = 14;// 领取皇城征收
	public static final short CARD_QUERY = 15;// 查询卡牌
	public static final short EQUIP_QUERY = 16;// 查询装备
	public static final short CARD_FREE_QUERY = 17;// 查询卡牌免费次数
	public static final short CARD_PICK = 18;// 抽取卡牌
	public static final short CARD_UPGRADE_STAR = 19;// 卡牌升星
	public static final short CARD_DECOMPOSE = 20;// 卡牌分解
	public static final short CARD_DECOMPOSE_PICK = 21;// 卡牌分解奖励领取
	public static final short CARD_DECOMPOSE_QUERY = 22;// 卡牌分解奖励查询
	public static final short ZX_QUERY = 23;// 阵型查询
	public static final short ZX_SET = 24;// 阵型放置
	public static final short TRAIN_QUERY = 25;// 卡牌训练查询
	public static final short TRAIN_CARD = 26;// 卡牌训练
	public static final short TRAIN_PICK_CARD = 27;// 卡牌训练领取
	public static final short TRAIN_TUFEI = 28;// 卡牌训练突飞
	public static final short ZHENG_BING = 29;// 征兵
	public static final short PVE_QUERY = 30;// 查询pve
	public static final short PVE_PICK = 31;// 领取pve宝箱
	public static final short EQUIP_CARD_QUERY = 32;// 查询卡牌和装备
	public static final short EQUIP_OPERATE = 33;// 操作装备
	public static final short EQUIP_POSITION_UNWEAR_QUERY = 34;// 查询某位置未穿戴装备
	public static final short EQUIP_UPGRADE_STAR = 35;// 装备升星
	public static final short EQUIP_UNWEAR_QUERY = 36;// 查询未穿戴装备
	public static final short CREATE_ROLE = 37;// 创建角色
	public static final short CHOOSE_COUNTRY = 38;// 选择国家
	public static final short BAG_USE = 39;// 使用道具
	public static final short PVE_CAL = 40;// 战斗结算
	public static final short INIT = 41;// 初始化
	public static final short ZX_BATTLE_CARD = 42;// 查询出战卡牌
	public static final short EQUIP_CARD_TEC_QUERY = 43;// 查询卡牌和装备和科技
	public static final short TASK_QUERY = 44;// 查询所有任务
	public static final short TASK_DAILY_PICK = 45;// 领取每日任务奖励
	public static final short TASK_DAILY_PICK_ACTIVE = 46;// 领取每日活跃度奖励
	public static final short TASK_MAIN_PICK = 47;// 领取主线任务奖励
	public static final short BAG_ITEMID_QUERY = 48;// 根据itemId查询
	public static final short BAG_ITEM_USE = 49;// 使用背包中的道具
	public static final short PK_QUERY = 50;// 竞技场战前查询
	public static final short PK_CAL = 51;// 竞技场战斗结算
	public static final short PK_QUERY_ZX = 52;// 获取他人上阵阵型
	public static final short PK_BUY_TIMES = 53;// 购买竞技场挑战次数
	public static final short PK_BUY_SHENGWANG = 54;// 购买竞技场声望商店
	public static final short PK_RANK = 55;// 查看竞技场排行榜
	public static final short PK_AWARD_QUERY = 56;// 查看竞技场奖励
	public static final short PK_AWARD_PICK = 57;// 领取竞技场奖励
	public static final short EQUIP_UPGRADE_UPLEVEL = 58;// 强化装备
	public static final short SHOP_BUY_JUNLING = 59;// 购买军令
	public static final short HOT_FIX = 60;// 热更新
	public static final short HOT_FIX_VERSION = 61;// 获取版本号
	public static final short ACCOUNT_LOGIN = 62;// 登录
	public static final short SHOP_FETCH_ORDER = 63;// 获取支付订单
	public static final short SHOP_FETCH_GOODS = 64;// 获取订单物品
	public static final short SHOP_QUERY_PAY_SUM = 65;// 获取支付累计

	public static void regist(int protoId, String className) {
		protoClassMap.put(protoId, className);
		classProtoMap.put(className, protoId);
	}
}
