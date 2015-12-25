package com.kidbear._36.util.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 添加一个模板表需要做以下几步 
 * 1.在包com.hjczb.war.main.template下创建对应的模板类 
 * 2.在resources/data/中添加模板数据文件
 * 3.在resources/dataConfig.xml加入刚才的模板数据文件
 * 
 * @author ranbo
 * 
 */
public class TempletService {
	public static Logger log = LoggerFactory.getLogger(TempletService.class);
	public static TempletService templetService = new TempletService();
	/**
	 * key:实体名 value:该实体下的所有模板数据
	 */
	public static Map<String, List<?>> templetMap = new HashMap<String, List<?>>();

	public TempletService() {

	}

	public static TempletService getInstance() {
		return templetService;
	}

	/**
	 * 获取该实体类下所有模板数据
	 * 
	 * @param beanName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List listAll(String beanName) {
		return templetMap.get(beanName);
	}

	/**
	 * @Title: registerObject 
	 * @Description: 注册对象到对应类的List中
	 * @param o
	 * @param dataMap
	 * @return void
	 * @throws
	 */
	public void registerObject(Object o, Map<String, List<?>> dataMap) {
		add(o.getClass().getSimpleName(), o, dataMap);
	}



	@SuppressWarnings("unchecked")
	private void add(String key, Object data, Map<String, List<?>> dataMap) {
		List list = dataMap.get(key);
		if (list == null) {
			list = new ArrayList();
			dataMap.put(key, list);
		}
		list.add(data);
	}
	
	public void afterLoad() {
		// 加载后处理
	}

	public void loadCanShu() {
		// 加载全局参数xml配置
	}
}
