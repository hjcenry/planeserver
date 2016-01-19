package mongotest;

import java.util.List;

import com.kidbear._36.util.mongo.DBObjectUtil;
import com.kidbear._36.util.mongo.MongoUtil;
import com.mongodb.DBObject;

public class MainTest {
	public static void main(String[] args) {
		TestBean bean = new TestBean();
		bean.setId(2);
		bean.setMsg("test msg");
		bean.setScore(100.2);
		DBObject obj = DBObjectUtil.bean2DBObject(bean);
		System.out.println("before insert");
		MongoUtil.getInstance().insert(TestBean.class.getSimpleName(), obj);
		System.out.println("after insert");

		System.out.println("before find");
		TestBean test = new TestBean();
		test.setId(2);
		List<DBObject> list = MongoUtil.getInstance().find(
				TestBean.class.getSimpleName(),
				DBObjectUtil.bean2DBObject(test));
		System.out.println("after find");
		for (DBObject dbObject : list) {
			TestBean t = null;
			t = DBObjectUtil.dbObject2Bean(dbObject, t);
			System.out.println(t.getId());
			System.out.println(t.getMsg());
			System.out.println(t.getScore());
		}
	}
}
