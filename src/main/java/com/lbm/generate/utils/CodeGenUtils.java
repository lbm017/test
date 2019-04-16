package com.lbm.generate.utils;

public class CodeGenUtils {
	
	public static void genFile(String url) {
		
	}
	
	public static String genObjectName(String columName) {
		String[] columItemArr = columName.split("_");
		
		String varName = "";//变量名称
		for (int j = 0; j < columItemArr.length; j++) {
			String fieldItem = columItemArr[j];
			if(j!=0) {
				String upWord = fieldItem.substring(0, 1).toUpperCase();
				String lowWord = fieldItem.substring(1);
				fieldItem = upWord+lowWord;
			}
			varName+=fieldItem;
		}
		return varName;
	}
}
