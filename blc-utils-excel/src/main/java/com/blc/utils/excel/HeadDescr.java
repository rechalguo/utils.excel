/**
 * 
 */
package com.blc.utils.excel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.blc.utils.excel.util.BeanMapUtil;

/**
 * @description 
 * @author guoruichao
 * @version 1.0
 * @date 2018年5月15日上午9:58:10
 */
public class HeadDescr {
	/**
	 * vo属性名
	 */
	private String fname; 
	/**
	 * 生成的excel表头
	 */
	private String headerName;
	/**
	 * 生成的excel表头所在的序列的序号
	 */
	private int headerOrder=0;
	/**
	 * vo属性的值
	 */
	private Object fvalue;
	public String getFname() {
		return fname;
	}
	public int getHeaderOrder() {
		return headerOrder;
	}
	public void setHeaderOrder(int headerOrder) {
		this.headerOrder = headerOrder;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getHeaderName() {
		return headerName;
	}
	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}
	public Object getFvalue() {
		return fvalue;
	}
	public void setFvalue(Object fvalue) {
		this.fvalue = fvalue;
	}
	
	
	public static List<HeadDescr> getHeaders(Object col) {
		List<HeadDescr> fdMap = new ArrayList<HeadDescr>();
		if(col instanceof Map) {
			Map h = (Map) col;
			for(Object k:h.keySet()) {
				HeadDescr fd = new HeadDescr();
				fd.setFname(k.toString());
				fd.setFvalue(k.toString());
				fd.setHeaderName(k.toString());
				fd.setHeaderOrder(0);
				fdMap.add(fd);
			}
		} else {
			try {
				fdMap = BeanMapUtil.getHeaderMap(col);
				Collections.sort(fdMap, new Comparator<HeadDescr>() {
					@Override
					public int compare(HeadDescr h1, HeadDescr h2) {
						if(h1==null) return 1;
						if(h2==null) return 1;
						if(h1.getHeaderOrder()>h2.getHeaderOrder()) return 1;
						if(h1.getHeaderOrder()<h2.getHeaderOrder()) return -1;
						return 0;
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return fdMap;
	}
}
