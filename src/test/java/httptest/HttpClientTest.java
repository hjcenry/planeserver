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

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kidbear._36.manager.building.BuildingMgr;
import com.kidbear._36.manager.building.ZbInfo;
import com.kidbear._36.manager.card.CardMgr;
import com.kidbear._36.manager.pve.PveAwardInfo;
import com.kidbear._36.manager.pve.PveChapterInfo;
import com.kidbear._36.manager.pve.PveInfo;
import com.kidbear._36.manager.pve.PveMgr;
import com.kidbear._36.manager.task.TrainInfo;
import com.kidbear._36.manager.task.TrainMgr;
import com.kidbear._36.manager.task.WorkerMgr;
import com.kidbear._36.manager.zhenxing.ZhenxingMgr;
import com.kidbear._36.net.ProtoIds;
import com.kidbear._36.util.HttpClient;
import com.kidbear._36.util.hibernate.HibernateUtil;
import com.kidbear._36.util.redis.Redis;

public class HttpClientTest {

	public static void main(String[] args) throws Exception {
		// HibernateUtil.buildSessionFactory();
		// WorkerMgr.getInstance().initData();
		// WorkerMgr.getInstance().initWorkers(1);
		// BuildingMgr.getInstance().initBuildingInfo(1);
		// ZhenxingMgr.getInstance().initZhenxingInfo(1);
		// CardMgr.getInstance().initCardInfo(1);
		HibernateUtil.init();
//		 PveChapterInfo pveChapterInfo1 = new PveChapterInfo();
//		 Redis.getInstance().set(PveMgr.CHAPTER_CACHE + 1,
//		 JSON.toJSONString(pveChapterInfo1));
		for (int i = 16; i <= 30; i++) {
			PveInfo pveInfo = HibernateUtil.find(PveInfo.class,
					"where userid=1 and levelId=" + i + "");
			if (pveInfo == null) {
				pveInfo = new PveInfo();
				pveInfo.setId(i);
				pveInfo.setUserid(1);
			}
			pveInfo.setStar(3);
			pveInfo.setLevelId(i);
			pveInfo.setChapter(2);
			HibernateUtil.save(pveInfo);
			// 存储章节信息
			PveChapterInfo pveChapterInfo = JSON.parseObject(Redis
					.getInstance().get(PveMgr.CHAPTER_CACHE + 1),
					PveChapterInfo.class);
			PveAwardInfo pveAwardInfo = pveChapterInfo.getChapter().get(
					pveInfo.getChapter());
			if (pveAwardInfo == null) {
				pveAwardInfo = new PveAwardInfo();
			}
			pveAwardInfo.setBx1(0);
			pveAwardInfo.setBx2(0);
			pveAwardInfo.setBx3(0);
			pveAwardInfo.setStarSum(pveAwardInfo.getStarSum()
					+ pveInfo.getStar());
			pveChapterInfo.getChapter().put(pveInfo.getChapter(), pveAwardInfo);
			Redis.getInstance().set(PveMgr.CHAPTER_CACHE + 1,
					JSON.toJSONString(pveChapterInfo));
		}
		// /************************************/
		// JSONObject obj = new JSONObject();
		// obj.put("userid", 1);
		// obj.put("typeid", ProtoIds.MAIN_INFO_QUERY);
		// JSONObject dataJson = new JSONObject();
		// dataJson.put("cardtype", 1);
		// obj.put("data", dataJson);
		// String data = JSON.toJSONString(obj);
		// // String dataStr =
		// // "{\"userid\":1,\"data\":{\"jztype\":1,\"type\":0},\"typeid\":7}";
		// String ret = HttpClient.post("http://localhost:8586", "data=" + data
		// + "");
		// System.out.println(ret);
	}
}
