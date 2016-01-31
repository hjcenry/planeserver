package com.kidbear._36.util.mongo;

import java.util.ArrayList;
import java.util.List;

import com.kidbear._36.util.mongo.DBObjectUtil;
import com.kidbear._36.util.mongo.MongoUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class MainTest {
	public static void run() {
		TestBean bean = new TestBean();
		bean.setId(TableIDCreator.getTableID(TestBean.class, 1));
		bean.setMsg("test msg1");
		bean.setScore(100.2);

		SubBean subBean = new SubBean();
		subBean.setId(TableIDCreator.getTableID(SubBean.class, 1));
		subBean.setStr("sub bean 中文");
		bean.setSubBean(subBean);

		List<SubBean> subs = new ArrayList<SubBean>();
		subs.add(subBean);
		SubBean subBean2 = new SubBean();
		subBean2.setId(TableIDCreator.getTableID(SubBean.class, 1));
		subBean2.setStr("sub2");
		subs.add(subBean2);

		bean.setSubBeans(subs);

		DBObject obj = DBObjectUtil.bean2DBObject(bean);
		// MongoUtil.getInstance().insert(TestBean.class.getSimpleName(), obj);

		TestBean test = new TestBean();
		SubBean bean2 = new SubBean();
		bean2.setId(4l);
		test.setSubBean(bean2);

		List<DBObject> list = MongoUtil.getInstance().find(
				TestBean.class.getSimpleName(), new BasicDBObject("subBean.id", 4));

		for (DBObject dbObject : list) {
			System.out.println(dbObject.toString());
			TestBean t = new TestBean();
			t = DBObjectUtil.dbObject2Bean(dbObject, t);
			System.out.println(t.getId());
			System.out.println(t.getMsg());
			System.out.println(t.getScore());
			System.out.println("=================");
		}
	}
}
