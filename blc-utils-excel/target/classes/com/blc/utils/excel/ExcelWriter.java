package com.blc.utils.excel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blc.utils.date.DateTimeUtil;
import com.blc.utils.excel.util.BeanMapUtil;
import com.blc.utils.number.NumberFormatter;

/**
 * 写EXCEL文件的辅助类
 * 当数据量比较大、内在小时，用这个
 * @author rechel
 *
 */
public class ExcelWriter extends ExcelWritable {
	public static Logger log = LoggerFactory.getLogger(ExcelWriter.class);
	/**
	 * @param window
	 */
	public ExcelWriter(int window) {
		super(window);
	}

	@Override
	public <T> void writeDataToSheet(Sheet sheet, List<T> objs, List<HeadDescr> header) throws Exception {
		long begin = System.currentTimeMillis();
		if(objs==null||objs.isEmpty()) return ;
		
		int size = 0;
		int rownum = 0;
		Row row = sheet.createRow(rownum++);
		Cell cell = null;
		T col = objs.get(0);
		if(header==null)
			header = HeadDescr.getHeaders(col);
		
		HeadDescr hd = null;
		for(int i=0;i<header.size();i++) {
			hd = header.get(i);
			if(hd==null) continue;
			cell = row.createCell(i);
			cell.setCellValue(hd.getHeaderName());
			cell = null;
		}
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String,Object> rowMap = null;
		//创建表体
		Object val = null;
		CellStyle cellStyle = book.createCellStyle();
        DataFormat format= book.createDataFormat();
        BigDecimal key = null;
        Integer valInteger = null;
        Double valDouble = null;
        size = 0;
        int emptyCellsRow = 0;
        int emptyRow = 0;
		try {
			for(int i=0;i<objs.size();i++) {
				row = sheet.createRow(i+rownum-emptyRow);// 创建一个行对象
				rowMap = BeanMapUtil.getBeanMap(objs.get(i));  //得到每行数据，并转换成map形式
				size ++;
				emptyCellsRow = 0;
				for(int cidx=0;cidx<header.size();cidx++) {
					cell = row.createCell(cidx);
					val = rowMap.get(header.get(cidx).getFname());
					if(val==null) {
						val = "";
						emptyCellsRow ++;
					}
					if(val instanceof String) {
						cellStyle.setDataFormat(format.getFormat("@"));
						cell.setCellStyle(cellStyle);
						cell.setCellType(CellType.STRING);
						cell.setCellValue(val.toString());
					} else if(val instanceof Integer) {
						valInteger = (Integer) val;
						cellStyle.setDataFormat(format.getFormat("0"));
						cell.setCellType(CellType.NUMERIC);
						cell.setCellValue(valInteger);
					} else if(val instanceof Long) {
						cellStyle.setDataFormat(format.getFormat("0"));
						cell.setCellType(CellType.NUMERIC);
						cell.setCellValue((Long) val);
					} else if(val instanceof Double) {
						valDouble = (Double) val;
						cell.setCellType(CellType.STRING);
						cell.setCellValue(valDouble==null?"":NumberFormatter.formatBigDouble(valDouble));
					} else if(val instanceof BigDecimal) {
						key = (BigDecimal) val;
						cell.setCellType(CellType.STRING);
						cell.setCellValue(key==null?"":NumberFormatter.formatBigDouble(key.doubleValue()));
					} else if(val instanceof Date) {
						cell.setCellType(CellType.STRING);
						cell.setCellValue(DateTimeUtil.formatDate(val,this.getWriterProperty(ExcelWriterProp.PROP_DATE_FORMAT,"yyyy/MM/dd").toString()));
						cell.setCellStyle(cellStyle);
					}
					cell = null;
					val = null;
				}
				if(header.size()==emptyCellsRow) {
					sheet.removeRow(row);
					emptyRow ++;
				}
				row = null;
			}
		}catch(Exception ee) {
			throw new Exception(ee);
		}finally {
			log.debug("Export Excel Sheet  consumes "+(System.currentTimeMillis()-begin)+" ms, size: "+size);
			sheet = null;
			row = null;
			cell = null;
		}
	}
}
