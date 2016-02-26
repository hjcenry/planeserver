package sockettest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.kidbear._36.net.ProtoIds;
import com.kidbear._36.net.ProtoMessage;
import com.kidbear._36.net.message.TestReq;
import com.kidbear._36.util.JsonUtils;

public class SocketTest {

	public static void main(String[] args) {
		int N = 1;

		for (int i = 0; i < N; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						// Socket socket = new Socket("123.59.139.220", 8586);
						Socket socket = new Socket("localhost", 8586);
						OutputStream outputStream = socket.getOutputStream();
						ProtoMessage protoMessage = new ProtoMessage();
						protoMessage.setTypeid(ProtoIds.TEST);
						TestReq req = new TestReq();
						req.setErrMsg("客户端测试消息");
						req.setName("hjc");
						req.setAttack(214000136);
						req.setDefend(51200000);
						req.setHealth(100000);
						protoMessage.setData(req);

						long start = System.currentTimeMillis();
						outputStream.write(JsonUtils.objectToJson(protoMessage)
								.getBytes());
						outputStream.flush();
						// while (true) {
						InputStream is = socket.getInputStream();
						byte[] bytes = new byte[1024];
						int n = is.read(bytes);
						System.out.println(new String(bytes, 0, n) + ",during"
								+ (System.currentTimeMillis() - start));
						// is.close();
						// socket.close();
						// }
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
}
