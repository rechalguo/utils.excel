package com.blc.utils.excel.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

import com.blc.utils.excel.ExcelWritable;

public class ExcelExportHttpHelper {

	public static void exportExcel(HttpServletResponse response, ExcelWritable book, String fileName) {
		try {
			fileName = URLEncoder.encode(fileName,"utf-8");
			response.setHeader("Set-Cookie", "fileDownload=true; path=/");
			response.setHeader("Content-disposition","attachment; filename=".concat(fileName));// 设定输出文件头				
			response.setContentType("application/octet-stream");// 定义输出类型
			ServletOutputStream out = null;
			out = response.getOutputStream();
			book.writeToOutputStream(out);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
//			if(book!=null) book = null;
		}
	}
	public static void exportExcel(HttpServletResponse response, Workbook book, String fileName) {
		try {
			fileName = URLEncoder.encode(fileName,"utf-8");
			response.setHeader("Set-Cookie", "fileDownload=true; path=/");
			response.setHeader("Content-disposition","attachment; filename=".concat(fileName));// 设定输出文件头				
			response.setContentType("application/octet-stream");// 定义输出类型
			ServletOutputStream out = response.getOutputStream();
			book.write(out);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(book!=null)
				try {
					book.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	/**
	 * 
	 * @param response
	 * @param books   文件名 -- workbook 对
	 * @throws UnsupportedEncodingException 
	 */
	public static void exportExcelZip(String zipName,HttpServletResponse response, Map<String, Workbook> books) throws UnsupportedEncodingException{
		String filename = URLEncoder.encode(zipName,"utf-8");
		response.setHeader("Set-Cookie", "fileDownload=true; path=/");
		response.addHeader("Content-Disposition","attachment; filename="+filename+"");//设置文件名
		response.setContentType("application/octect-stream");//设置文件类型
		try {
			ZipOutputStream zip = new ZipOutputStream (response.getOutputStream() );
			TarZipUtil.wrapZipOutputStreamsWithsWorkbooks(zip, books);
			zip.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
	}
	
	public static Map<String, ByteArrayOutputStream> convertToByteOutputStream(Map<String, Workbook> books) {
		if(books == null) return new  HashMap<String, ByteArrayOutputStream>();
		ByteArrayOutputStream bos = null;
		Map<String, ByteArrayOutputStream> bm = new HashMap<String, ByteArrayOutputStream>();
		try {
			for(String book : books.keySet()) {
				bos = new ByteArrayOutputStream();
				books.get(book).write(bos);
				bm.put(book, bos);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bm;
	}
}
