/**
 * 
 */
package com.blc.utils.excel.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.sf.cglib.beans.BeanMap;

import com.blc.utils.excel.HeadDescr;
import com.blc.utils.excel.annotation.ExcelField;
import com.blc.utils.excel.annotation.Exportable;

/**
 * @description 
 * @author guoruichao
 * @version 1.0
 * @date 2018年5月15日上午8:58:50
 */
public class BeanMapUtil {
	
	private static ConcurrentMap<String, Object> CLASSES = new ConcurrentHashMap<>();

	/**
	 * 获取bean的表头相关属性，用来写EXCEL表头
	 * @description
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public static <T> List<HeadDescr> getHeaderMap(T bean) throws Exception {
		Exportable et = bean.getClass().getAnnotation(Exportable.class);
		if(et==null) throw new Exception("Not Found Annotation 'Exportable'");
		List<HeadDescr> mapObj = new ArrayList<>();
		BeanMap  m = BeanMap.create(bean);
		Field f = null;
		ExcelField ef = null;
		try {
			for(Object k:m.keySet()) {
				f = bean.getClass().getDeclaredField(k.toString());
				ef = f.getAnnotation(ExcelField.class);
				if(ef==null) continue;
				HeadDescr fd = new HeadDescr();
				fd.setFname(f.getName());
				fd.setFvalue(m.get(k));
				fd.setHeaderName(ef.name());
				fd.setHeaderOrder(ef.sort());
				mapObj.add(fd);
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return mapObj;
	}
	
	/**
	 * 将bean转换为以属性为key的map,用来写数据
	 * @description
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public static <T> Map<String,Object> getBeanMap(T bean) throws Exception{
		if(bean instanceof Map) {
			Map<String,Object> mapObj = new HashMap<>();
			Map m = (Map) bean;
			for(Object k:m.keySet()) {
				mapObj.put(k.toString(), m.get(k));
			}
			return mapObj;
		}
		Exportable et = bean.getClass().getAnnotation(Exportable.class);
		if(et==null) throw new Exception("Not Found Annotation 'Exportable'");
		Map<String,Object> mapObj = new HashMap<>();
		BeanMap  m = BeanMap.create(bean);
		Field f = null;
		ExcelField ef = null;
		try {
			for(Object k:m.keySet()) {
				f = bean.getClass().getDeclaredField(k.toString());
				ef = f.getAnnotation(ExcelField.class);
				if(ef==null) continue;
				mapObj.put(k.toString(), m.get(k));
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return mapObj;
	}
}
