/**
 * 
 */
package com.blc.utils.blc_utils_excel;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.blc.utils.blc_utils_excel.vo.User;
import com.blc.utils.excel.ExcelWritable;
import com.blc.utils.excel.ExcelWriter;

/**
 * @description 
 * @author guoruichao
 * @version 1.0
 * @date 2018年5月15日上午10:26:16
 */
public class T {
	/**
	 * @description
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		testMap();
	}
	
	public static void testVo() {
		String dir = System.getProperty("user.dir");
		List<User> us = new ArrayList<User>();
		User u = new User();
		u.setAge(1961654954);
		u.setBirth(new Date());
		u.setDeath(new Date());
		u.setName("grc");
		us.add(u);
		
		List<Object> header = new ArrayList<>();
		header.add("n");
		header.add("b");
		
		ExcelWritable ew = new ExcelWriter(100);
		try {
			ew.writeDataToSheet(
					ew.createOrGetSheet("test"), us, null);
			ew.writeToFile(dir.concat(File.separator).concat("user.xlsx"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void testMap() {
		String dir = System.getProperty("user.dir");
		List<Map<Object,Object>> us = new ArrayList<>();
		Map<Object,Object> row = new HashMap<>();
		row.put("A", new Date());
		row.put("b", 12.03);
		row.put("c", "saweff");
		us.add(row);
		
		ExcelWritable ew = new ExcelWriter(100);
		try {
			ew.writeDataToSheet(
					ew.createOrGetSheet("test"), us, null);
			ew.writeToFile(dir.concat(File.separator).concat("user.xlsx"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
