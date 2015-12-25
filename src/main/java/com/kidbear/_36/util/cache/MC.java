package com.kidbear._36.util.cache;

import java.util.HashSet;
import java.util.Set;

import com.kidbear._36.util.memcached.MemcachedCRUD;

public class MC {
	/**
	 * 控制哪些类进行memcached缓存。
	 * 被控制的类在进行创建时，需要注意调用MC的add和hibernate的insert。
	 */
	public static Set<Class<? extends MCSupport>> cachedClass = new HashSet<Class<? extends MCSupport>>();
	static{
//		cachedClass.add(JunZhu.class);
	}
	public static <T> T get(Class<T> t, long id){
		if(!cachedClass.contains(t)){
			return null;
		}
		String c = t.getSimpleName();
		String key = c+"#"+id;
		return MemcachedCRUD.getInstance().<T>get(key);
	}

	public static Object getValue(String key){
		Object o = MemcachedCRUD.getInstance().get(key);
		return o;
	} 
	
	public static boolean add(Object t, long id){
		if(!cachedClass.contains(t.getClass())){
			return false;
		}
		String c = t.getClass().getSimpleName();
		String key = c+"#"+id;
		return MemcachedCRUD.getInstance().safeSet(key, 0, t);
	}

	public static boolean addKeyValue(String key, Object value){
		return MemcachedCRUD.getInstance().safeSet(key, 0, value);
	}
	
	public static void update(Object t, long id){
		if(!cachedClass.contains(t.getClass())){
			return;
		}
		String c = t.getClass().getSimpleName();
		String key = c+"#"+id;
		MemcachedCRUD.getInstance().safeSet(key, 0, t);
	}
	
	/**
	 * 根据主键删除缓存
	 * @param obj	删除对象
	 * @param id	主键id
	 */
	public static void delete(Class clazz, long id) {
		if(!cachedClass.contains(clazz)) {
			return;
		}
		String prefix = clazz.getSimpleName();
		String key = prefix + "#" + id;
		MemcachedCRUD.getInstance().delete(key);
	}
}
