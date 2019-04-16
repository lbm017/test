package com.lbm.generate.javacode;

import java.io.Serializable;

import com.generate.CommonCodeGen;

public class DaoGen extends CommonCodeGen{
	public static void codeGen() {
		
		String code = "@Repository(\""+objectName+"Mapper\")\r\n" + 
				"public class PtpMapper extends BaseMapper<"+className+"Model, Serializable>  {\r\n" + 
				"\r\n" + 
				"}";
	}
}
