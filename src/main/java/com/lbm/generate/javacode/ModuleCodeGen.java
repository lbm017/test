package com.lbm.generate.javacode;

import com.generate.CommonCodeGen;
import com.generate.MysqlUtils;
import com.generate.utils.CodeGenUtils;

public class ModuleCodeGen extends CommonCodeGen {
	public static void main(String[] args) {
		MysqlUtils.getTableFields();
		fieldList.forEach(x -> {
			String item = "private ";
			if (x.getFiledType() == 1) {
				item += "Integer " + CodeGenUtils.genObjectName(x.getFieldName());
			} else if (x.getFiledType() == 2) {
				item += "String " + CodeGenUtils.genObjectName(x.getFieldName());
			} else if (x.getFiledType() == 3) {
				item += "Date " + CodeGenUtils.genObjectName(x.getFieldName());
			}else if (x.getFiledType() == 4) {
				item += "Long " + CodeGenUtils.genObjectName(x.getFieldName());
			}
			item += ";";
			System.out.println(item);
		});
	}
}
