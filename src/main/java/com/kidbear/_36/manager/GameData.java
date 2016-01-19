package com.kidbear._36.manager;

import net.sf.json.JSONArray;

import com.kidbear._36.manager.zhenxing.ZhenXing;

public class GameData {
	private Long userid;
	private Integer country;// --用户所属国家 表示蜀国 表示魏国 表示吴国
	private Integer head_img;// --用户头像
	private Integer user_level;// --用户等级 --君主等级
	private Integer user_vip;// --用户vip等级
	private Long user_lingcount;// --用户军令数
	private Long user_power;// --战力
	private Long user_coin;// --用户金币
	private Long user_acer;// --用户元宝
	private Long user_food;// --用户粮食
	private Long user_iron;// --用户精铁
	private Long user_wood;// --用户木材
	private Long user_forces;// --用户兵力
	private Long user_jungong;// --用户军功数
	private Long user_jianghunnum;// --将魂数量
	private Integer user_mfck_num;// --免费抽卡次数
	private Integer user_curent_zxnum;// --用户的当前阵型
	private ZhenXing user_zxlist;
	private Integer junjichu_level;// --军机处等级
	private Integer huangcheng_level;// --皇城等级
	private Integer zhaoshangju_level;// --招商局等级
	private Integer jiaochang_level;// --校场等级
	private Integer cangku_level;// --仓库等级
	private Integer bingying_level;// --兵营等级
	private Integer kaugncang_level;// --矿场等级
	private JSONArray keji_levellist;// [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
										// --科技的等级数
	private JSONArray hc_zsNum;// {{12,1},{2,1},{2,1}} --皇城免费征收次数
								// 第一个数表示免费征收的次数,第二个表示金币征收的次数
	private JSONArray hammer_datalist;// {{0,0,0,1},{0,0,0,0},{0,0,0,0},{0,0,0,0}}
										// --第一个参数表示 建筑类型 ，第二个是开始时间 第三个是工作时间
										// 第四个表示是否开放 0表示未开放1表示开放
	// --卡牌列表 -- 第一个是参数是CardId 卡牌id 第二个参数是卡牌等级CardLv ，第三个参数是 卡牌星级 CardStar
	// ，第四个参数 是卡牌名字 CardName
	// --第五个参数是装备列表 EquipIDlist 第六个参数是 是否在阵型状态 第七个参数是武将经验 Experience
	private JSONArray kardlist;
	// --装备列表 --第一个是参数是装备id EquipID 第二个参数是装备星级 EquipStar 第三个参数是装备需要满足的等级 EquipLv
	// 第四个参数是装备名字EquipName
	// -- 第五个参数是 所属卡牌id CardId 第六个参数是装备所属位置 第七个参数是 countNum 装备的升星后的数量
	private JSONArray Equiplist;
	// --训练位置 第一个参数 是CardId,第二个参数是训练的开始时间beginTime ，第三个是状态表示是否开发 isOpen 1
	// 表示开放0表示未开放
	// -- 第四个参数 是训练状态 trainState 1,3,6三种状态 表示4，8，12个小时 ，第五个参数是突飞的次数 tufeinum
	private JSONArray Trainlist;// {{0,0,1,1,0},{0,0,1,1,0},{0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0},{0,0,0,1,0}}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Integer getCountry() {
		return country;
	}

	public void setCountry(Integer country) {
		this.country = country;
	}

	public Integer getHead_img() {
		return head_img;
	}

	public void setHead_img(Integer head_img) {
		this.head_img = head_img;
	}

	public Integer getUser_level() {
		return user_level;
	}

	public void setUser_level(Integer user_level) {
		this.user_level = user_level;
	}

	public Integer getUser_vip() {
		return user_vip;
	}

	public void setUser_vip(Integer user_vip) {
		this.user_vip = user_vip;
	}

	public Long getUser_lingcount() {
		return user_lingcount;
	}

	public void setUser_lingcount(Long user_lingcount) {
		this.user_lingcount = user_lingcount;
	}

	public Long getUser_power() {
		return user_power;
	}

	public void setUser_power(Long user_power) {
		this.user_power = user_power;
	}

	public Long getUser_coin() {
		return user_coin;
	}

	public void setUser_coin(Long user_coin) {
		this.user_coin = user_coin;
	}

	public Long getUser_acer() {
		return user_acer;
	}

	public void setUser_acer(Long user_acer) {
		this.user_acer = user_acer;
	}

	public Long getUser_food() {
		return user_food;
	}

	public void setUser_food(Long user_food) {
		this.user_food = user_food;
	}

	public Long getUser_iron() {
		return user_iron;
	}

	public void setUser_iron(Long user_iron) {
		this.user_iron = user_iron;
	}

	public Long getUser_wood() {
		return user_wood;
	}

	public void setUser_wood(Long user_wood) {
		this.user_wood = user_wood;
	}

	public Long getUser_forces() {
		return user_forces;
	}

	public void setUser_forces(Long user_forces) {
		this.user_forces = user_forces;
	}

	public Long getUser_jungong() {
		return user_jungong;
	}

	public void setUser_jungong(Long user_jungong) {
		this.user_jungong = user_jungong;
	}

	public Long getUser_jianghunnum() {
		return user_jianghunnum;
	}

	public void setUser_jianghunnum(Long user_jianghunnum) {
		this.user_jianghunnum = user_jianghunnum;
	}

	public Integer getUser_mfck_num() {
		return user_mfck_num;
	}

	public void setUser_mfck_num(Integer user_mfck_num) {
		this.user_mfck_num = user_mfck_num;
	}

	public Integer getUser_curent_zxnum() {
		return user_curent_zxnum;
	}

	public void setUser_curent_zxnum(Integer user_curent_zxnum) {
		this.user_curent_zxnum = user_curent_zxnum;
	}

	public ZhenXing getUser_zxlist() {
		return user_zxlist;
	}

	public void setUser_zxlist(ZhenXing user_zxlist) {
		this.user_zxlist = user_zxlist;
	}

	public Integer getJunjichu_level() {
		return junjichu_level;
	}

	public void setJunjichu_level(Integer junjichu_level) {
		this.junjichu_level = junjichu_level;
	}

	public Integer getHuangcheng_level() {
		return huangcheng_level;
	}

	public void setHuangcheng_level(Integer huangcheng_level) {
		this.huangcheng_level = huangcheng_level;
	}

	public Integer getZhaoshangju_level() {
		return zhaoshangju_level;
	}

	public void setZhaoshangju_level(Integer zhaoshangju_level) {
		this.zhaoshangju_level = zhaoshangju_level;
	}

	public Integer getJiaochang_level() {
		return jiaochang_level;
	}

	public void setJiaochang_level(Integer jiaochang_level) {
		this.jiaochang_level = jiaochang_level;
	}

	public Integer getCangku_level() {
		return cangku_level;
	}

	public void setCangku_level(Integer cangku_level) {
		this.cangku_level = cangku_level;
	}

	public Integer getBingying_level() {
		return bingying_level;
	}

	public void setBingying_level(Integer bingying_level) {
		this.bingying_level = bingying_level;
	}

	public Integer getKaugncang_level() {
		return kaugncang_level;
	}

	public void setKaugncang_level(Integer kaugncang_level) {
		this.kaugncang_level = kaugncang_level;
	}

	public JSONArray getKeji_levellist() {
		return keji_levellist;
	}

	public void setKeji_levellist(JSONArray keji_levellist) {
		this.keji_levellist = keji_levellist;
	}

	public JSONArray getHc_zsNum() {
		return hc_zsNum;
	}

	public void setHc_zsNum(JSONArray hc_zsNum) {
		this.hc_zsNum = hc_zsNum;
	}

	public JSONArray getHammer_datalist() {
		return hammer_datalist;
	}

	public void setHammer_datalist(JSONArray hammer_datalist) {
		this.hammer_datalist = hammer_datalist;
	}

	public JSONArray getKardlist() {
		return kardlist;
	}

	public void setKardlist(JSONArray kardlist) {
		this.kardlist = kardlist;
	}

	public JSONArray getEquiplist() {
		return Equiplist;
	}

	public void setEquiplist(JSONArray equiplist) {
		Equiplist = equiplist;
	}

	public JSONArray getTrainlist() {
		return Trainlist;
	}

	public void setTrainlist(JSONArray trainlist) {
		Trainlist = trainlist;
	}

}
