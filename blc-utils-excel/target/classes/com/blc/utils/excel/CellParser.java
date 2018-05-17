/**
 * 
 */
package com.blc.utils.excel;

import org.apache.poi.ss.usermodel.Cell;

/**
 * @author guoruichao
 * @version 1.0
 * @date 2017年1月22日下午5:08:33
 */
public interface CellParser {
	
	public String parseNumberCell(Cell cell, double v);
}
