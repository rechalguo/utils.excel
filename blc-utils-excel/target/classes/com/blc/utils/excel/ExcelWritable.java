/**
 * 
 */
package com.blc.utils.excel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @description 
 * @author guoruichao
 * @version 1.0
 * @date 2018年5月15日上午8:13:25
 */
public abstract class ExcelWritable {

	Workbook book = null;
	boolean closed = false;
	Map<ExcelWriterProp,Object> properties = new HashMap<>();
	
	public ExcelWritable() {
		book = new XSSFWorkbook();
		properties.put(ExcelWriterProp.PROP_DATE_FORMAT, "yyyy/MM/dd");
	}
	public ExcelWritable(int window) {
		book = new SXSSFWorkbook(window);
		properties.put(ExcelWriterProp.PROP_DATE_FORMAT, "yyyy/MM/dd");
	}
	public final Workbook getWookBook(){
		return this.book;
	}
	
	public Sheet createOrGetSheet(String name){
		if(StringUtils.isBlank(name)) {
			name = "导出结果";
		}
		if(book.getSheet(name)!=null) return book.getSheet(name);
		return book.createSheet(name);
	}
	
	public abstract <T> void writeDataToSheet(Sheet sheet, List<T> objs, List<HeadDescr> header) throws Exception;
	
	/**
	 * 
	 * @param records  写入EXCEL的订单号和平台代码（匹配好的）
	 * @param fileName 新文件的文件名
	 */
	public void writeToFile(String fileName) {
		try {
			FileOutputStream f = new FileOutputStream(fileName);
			book.write(f);
			f.flush();
			this.closed = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(book!=null)
				book.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @param records  写入EXCEL的订单号和平台代码（匹配好的）
	 * @param fileName 新文件的文件名
	 */
	public void writeToOutputStream(OutputStream out) {
		try {
			book.write(out);
			out.flush();
			this.closed = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(book!=null)
				book.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean isClosed() {
		return this.closed;
	}
	
	public void setWriterProperty(ExcelWriterProp key, Object obj) {
		this.properties.put(key, obj);
	}
	public Object getWriterProperty(ExcelWriterProp key, String defalt) {
		if(this.properties.containsKey(key)) this.properties.get(key);
		return defalt;
	}
	
	public enum ExcelWriterProp {
		PROP_DATE_FORMAT
	}
}
