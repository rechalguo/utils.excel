package com.blc.utils.excel.util;


public class Utils {
	/**
	 * 将带小数点的数据格式化为整数字符串.小数部分都为0,否则原格式返回
	 * @param v
	 * @return
	 */
	public static String checkDoubleStr(String v) {
		if(v==null) return v;
		int idx = v.indexOf(".");
		if(idx<=-1) return v;
		boolean cut = true;
		for(int i=idx+1;i<v.length();i++) {
			if('0'!=v.charAt(i)) {
				cut = false;
				break;
			}
		}
		if(!cut) return v;
		return v.substring(0, idx);
	}

	public static Integer parseStringToInt(String obj) {
		if(obj==null) return null;
		if(obj.matches("^[0-9]*$")) {
			return Integer.parseInt(obj);
		}if(obj.matches("^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$")){
			return Integer.parseInt(obj.substring(0, obj.indexOf(".")));
		}
		return null;
	}
	public static Double parseStringToDouble(String obj) {
		if(obj==null) return null;
		//^-?([1-9]/d*/./d*|0/./d*[1-9]/d*|0?/.0+|0)$
		if(obj.matches("^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$")){
			return Double.parseDouble(obj);
		}else if(obj.matches("^-?[1-9]\\d*$")) {
			return Double.parseDouble(obj+".0");
		}
		return null;
	}
	public static Float parseStringToFloat(String obj) {
		if(obj==null) return null;
		if(obj.matches("^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$")){
			return Float.parseFloat(obj);
		}else if(obj.matches("^-?[1-9]\\d*$")) {
			return Float.parseFloat(obj+".0");
		}
		return null;
	}
	
	public static void main(String args[]) {
		System.out.println(checkDoubleStr("52.020"));
		
		java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
		double d = Double.parseDouble("4.101983436E9");
		System.out.println(Double.toString(d));
		System.out.println(nf.format(d));
	}
}
