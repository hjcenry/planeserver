package com.kidbear._36.util.mongo;

import java.util.ArrayList;
import java.util.List;

import com.kidbear._36.util.mongo.DBObjectUtil;
import com.kidbear._36.util.mongo.MongoUtil;
import com.mongodb.DBObject;

public class MainTest {
	public static void run() {
		TestBean bean = new TestBean();
		bean.setId(2);
		bean.setMsg("test msg");
		bean.setScore(100.2);

		SubBean subBean = new SubBean();
		subBean.setId(333);
		subBean.setStr("sub bean 中文");
		bean.setSubBean(subBean);

		List<SubBean> subs = new ArrayList<SubBean>();
		subs.add(subBean);
		SubBean subBean2 = new SubBean();
		subBean2.setId(555);
		subBean2.setStr("sub2");
		subs.add(subBean2);

		bean.setSubBeans(subs);

		DBObject obj = DBObjectUtil.bean2DBObject(bean);
		System.out.println("before insert");
		MongoUtil util = MongoUtil.getInstance();
		util.insert(TestBean.class.getSimpleName(), obj);
		System.out.println("after insert");

		System.out.println("before find");
		TestBean test = new TestBean();
		test.setId(2);
		List<DBObject> list = util.find(TestBean.class.getSimpleName(),
				DBObjectUtil.bean2DBObject(test));
		System.out.println("after find");
		for (DBObject dbObject : list) {
			System.out.println(dbObject.toString());
			TestBean t = new TestBean();
			t = DBObjectUtil.dbObject2Bean(dbObject, t);
			System.out.println(t.getId());
			System.out.println(t.getMsg());
			System.out.println(t.getScore());
		}
	}
}
