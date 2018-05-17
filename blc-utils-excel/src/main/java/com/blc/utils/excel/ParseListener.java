/**
 * 
 */
package com.blc.utils.excel;

import org.apache.poi.ss.usermodel.Row;

/**
 * @author guoruichao
 * @version 1.0
 * @date 2017年1月4日下午1:21:23
 */
public interface ParseListener {
	
	public static final int PS_MSG_NONE = -1;
	public static final int PS_MSG_SUCCESS = 0;
	public static final int PS_MSG_ERROR = 1;
	
	int getParseErrorType();
	
	/**
	 * parse row and cells which rownum<titleRownum
	 * @param row
	 * @param rownum
	 */
	void parseRowPreHead(Row row, int rownum);
	/**
	 * @return
	 */
	public Object getParsedObject();
	/**
	 * parse head row
	 * @param obj
	 * @param idx
	 * @return
	 */
	String parseHead(Object obj, int idx);
	
	/**
	 * 
	 * @param cell
	 * @param v
	 * @return
	 */
	public CellParser getCellParser();
	
}
