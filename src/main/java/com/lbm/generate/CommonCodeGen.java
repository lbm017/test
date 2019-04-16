package com.lbm.generate;

import java.util.ArrayList;
import java.util.List;

public class CommonCodeGen {
	
	public static String databaseName = "vip_solr";
	public static String tableName = "vip_new_gender_query_category";
	
	public static List<Field> fieldList = new ArrayList<Field>();

	public static String className = "NewGenderCategory";
	
	public static String objectName = className.substring(0,1).toUpperCase()+className.substring(1);
}
