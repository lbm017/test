package com.lbm.generate.javacode;

import java.io.File;
import java.io.FileInputStream;

import com.generate.CommonCodeGen;
import com.generate.Field;
import com.generate.MysqlUtils;
import com.generate.utils.CodeGenUtils;

public class XMLCodeGen extends CommonCodeGen{
	
	public static String object_str = "##Object##";
	public static String resultMap_str = "##resultMap##";
	public static String columns_str = "##columns_name##";
	public static String query_str = "##query_module##";
	public static String insert_str = "##insert_module##";
	public static String update_str = "##update_module##";
	public static String table_name_str = "##table_name##";
	
	public static void codeGen() throws Exception {
		String objectName = className;//##Object##
		String resultMap = "";//##resultMap##
		String columnsName = "";//##columns_name##
		String queryModule = "";//##query_module##
		String insertModule = "";//##insert_module##
		String updateModule = "";//##update_module##
		
		for (int i = 0; i < fieldList.size(); i++) {
			Field field = fieldList.get(i);
			String fieldName = field.getFieldName();//表中字段名称
			int fieldType = field.getFiledType();
			
			String varName = CodeGenUtils.genObjectName(fieldName);
			
			//resultMap
			if("id".equals(fieldName)) {
				resultMap += "		<id column=\"ID\" property=\"id\" jdbcType=\"BIGINT\" />\r\n";
			}else if(fieldType==1) {
				resultMap += "		<id column=\""+fieldName.toUpperCase()+"\" property=\""+varName+"\" jdbcType=\"INTEGER\" />\r\n";
			}else if(fieldType==2) {
				resultMap += "		<id column=\""+fieldName.toUpperCase()+"\" property=\""+varName+"\" jdbcType=\"VARCHAR\" />\r\n";
			}else if(fieldType==3) {
				resultMap += "		<id column=\""+fieldName.toUpperCase()+"\" property=\""+varName+"\" jdbcType=\"TIMESTAMP\" />\r\n";
			}else if(fieldType==4) {
				resultMap += "		<id column=\""+fieldName.toUpperCase()+"\" property=\""+varName+"\" jdbcType=\"BIGINT\" />\r\n";
			}
			
			//##columns_name##
			columnsName += fieldName.toUpperCase()+",";//所有字段

			//##query_module##
			if(fieldType==2) {
				//列表查询条件
				queryModule += "			<if test=\"t."+varName+"!=null and t."+varName+"!=''\">\r\n" + 
						"				AND "+fieldName.toUpperCase()+" like concat('%',#{t."+varName+"},'%')\r\n" + 
						"			</if>\r\n";
			}
			
			//##insert_module##
			if(fieldType==1 || fieldType==4) {
				insertModule += "			#{"+varName+",jdbcType=BIGINT},\r\n";
			}else if(fieldType==2) {
				insertModule += "			#{"+varName+",jdbcType=VARCHAR},\r\n";
			}else if(fieldType==3) {
				insertModule += "			#{"+varName+",jdbcType=TIMESTAMP},\r\n";
			}
			
			//##update_module##
			updateModule += "			<if test=\""+varName+"!=null\">\r\n";
			if(fieldType==1 || fieldType==4) {
				updateModule += "				"+fieldName.toUpperCase()+"=#{"+varName+",jdbcType=BIGINT},\r\n";
			}else if(fieldType==2) {
				updateModule += "				"+fieldName.toUpperCase()+"=#{"+varName+",jdbcType=VARCHAR},\r\n";
			}else if(fieldType==3) {
				updateModule += "				"+fieldName.toUpperCase()+"=#{"+varName+",jdbcType=TIMESTAMP},\r\n";
			}
			if(i == fieldList.size()-1) {
				updateModule = updateModule.substring(0, updateModule.length()-3)+"\r\n";
			}
			updateModule +="			</if>\r\n";
		}
		
		//获取模板文件
		File file = new File(Class.class.getClass().getResource("/").getPath()+"template/ObjectMapper.xml");
		int fileLength = (int)file.length();
		byte[] fileContent = new byte[fileLength];
		FileInputStream in = new FileInputStream(file);
		in.read(fileContent);
		in.close();
		
		String xmlStr = new String(fileContent);
		xmlStr = xmlStr.replaceAll(object_str, objectName);
		xmlStr = xmlStr.replaceAll(resultMap_str, resultMap);
		xmlStr = xmlStr.replaceAll(columns_str, columnsName.substring(0,columnsName.length()-1));
		xmlStr = xmlStr.replaceAll(query_str, queryModule);
		xmlStr = xmlStr.replaceAll(insert_str, insertModule.substring(0,insertModule.length()-3));
		xmlStr = xmlStr.replaceAll(update_str, updateModule);
		xmlStr = xmlStr.replaceAll(table_name_str, tableName);
		
		System.out.println(xmlStr);
	}
	
	public static void main(String[] args) throws Exception {
		MysqlUtils.getTableFields();
		codeGen();
	}
}
