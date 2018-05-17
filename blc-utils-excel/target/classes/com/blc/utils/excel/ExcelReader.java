package com.blc.utils.excel;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blc.utils.number.MathInt;
import com.blc.utils.number.NumberFormatter;

public class ExcelReader {
	
	private static Logger log = LoggerFactory.getLogger(ExcelReader.class);

	/**
	 * reading data row by row
	 * @param f
	 * @param heads
	 * @return
	 */
	public static <T> List<Map<String,String>> readExcelConfigFile(File f,int titleRowNum,ParseListener listener) {
		InputStream in = null;
		Workbook book = null;
		Sheet sheet = null;
		Row row = null;
		Cell cell = null;
		String head = null;
		int temp = -1;
		Map<Integer, String> header = new HashMap<Integer, String>();
		List<Map<String,String>> result = null;
		Map<String, String> map = null;
		Double d = null;
		try {
			in = new FileInputStream(f);
			if(f.getName().matches("^.*\\.xlsx$")) book = new XSSFWorkbook(in);
			else book = new HSSFWorkbook(in);
			sheet = book.getSheetAt(0);
			if(titleRowNum < 0 ) titleRowNum = 0;
			temp = sheet.getFirstRowNum() ;
			int max = MathInt.max(temp, titleRowNum);
			if(max<0) {
				log.error("# Excel File rownum Error !");
				return null;
			}
			if(max>0&&listener!=null) {
				titleRowNum = 0;
				for(int i=titleRowNum;i<max;i++) {
					row = sheet.getRow(i);
					listener.parseRowPreHead(row, i);
				}
				if(listener.getParseErrorType()==ParseListener.PS_MSG_ERROR) return null;
			}
			
			temp = max;
			result = new ArrayList<Map<String, String>>(sheet.getPhysicalNumberOfRows());
			
			row = sheet.getRow(temp);
			Iterator<Cell> cells = row.cellIterator();
			while(cells.hasNext()) {
				cell = cells.next();
				head = cell.getStringCellValue();
				if(listener!=null) head = listener.parseHead(head, cell.getColumnIndex());
				if(head!=null) {
					header.put(cell.getColumnIndex(), head.trim());
				}
			}
			int endRow = sheet.getLastRowNum();
			if(endRow==temp) {
				return null;
			} else if(endRow>temp) {
				for(int i=(temp+1);i<=(endRow);i++) {
					row = sheet.getRow(i);
					if(row==null) continue;
					cells = row.cellIterator();
					map = new HashMap<String, String>(row.getPhysicalNumberOfCells());
					while(cells.hasNext()) {
						cell = cells.next();
						if(header.containsKey(cell.getColumnIndex())) {
							if(cell==null||cell.getCellTypeEnum()==CellType.BLANK) continue;
							if(cell.getCellTypeEnum()==CellType.NUMERIC) {
								d = cell.getNumericCellValue();
								d = d*1.0;
							}
							if(d!=null) {
								if(listener!=null) {
									map.put(header.get(cell.getColumnIndex()),listener.getCellParser().parseNumberCell(cell, d));
								} else {
									map.put(header.get(cell.getColumnIndex()),NumberFormatter.formatBigNumber(d, 5));
								}
							} else map.put(header.get(cell.getColumnIndex()),cell.getStringCellValue());
							d = null;
						}
					}
					if(!map.isEmpty()) result.add(map);
				}
			}
			return result;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(book!=null) book.close();
				if(in!=null) in.close();
				in = null;
				book = null;
				sheet = null;
				cell = null;
				row = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
