package com.lbm.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;


public class MainCateExport {
	
	public static Map<String,String> cate3IdShowRelation = new HashMap<String,String>();
	public static Map<String,String> cate3IdMap = new HashMap<String,String>();
	public static Map<String,String> show3IdMap = new HashMap<String,String>();
	public static ListMultimap<String, Integer> wordAndCateId3Relation = ArrayListMultimap.create();
	
	public static Map<String,List<Map<String,Object>>> result = new HashMap<String,List<Map<String,Object>>>();
	
	public static void main(String[] args) throws Exception {
		init();
		getCateId3Relation();
		getShowId3AndPropRelation();

		Workbook wb = new HSSFWorkbook();
		OutputStream out = new FileOutputStream("e:/aa/aa.xls");
	    Sheet sheet = wb.createSheet("result");
		
		Iterator wordSet = wordAndCateId3Relation.keySet().iterator();
		int line = 0;
		while(wordSet.hasNext()) {
			String word = (String) wordSet.next();
			List<Integer> cateId3List = wordAndCateId3Relation.get(word);
			//System.out.println(word+":"+cateId3List.toString());
			
			//循环查找展示分类ID
			for (int i = 0; i < cateId3List.size(); i++) {
				int cateId3 = cateId3List.get(i);
				String showIdStr = cate3IdShowRelation.get(cateId3+"");
				String[] showIdArr = showIdStr.split(",");
				for (int j = 0; j < showIdArr.length; j++) {
					//现在是展示分类
					String showCateId3 = showIdArr[j];
					for (int k = 0; k < 100; k++) {
						
						//写入excel
						Row row = sheet.createRow(line);
						row.createCell(0).setCellValue(word);
						row.createCell(1).setCellValue(cateId3);
						row.createCell(2).setCellValue((String)cate3IdMap.get(cateId3+""));
						row.createCell(3).setCellValue(showCateId3);
						row.createCell(4).setCellValue((String)show3IdMap.get(showCateId3));
						line++;
					}
				}
			}
		}
		
		wb.write(out);
		out.close();
	}

	
	public static void getCateId3Relation() throws Exception{
		InputStream is = new FileInputStream("e:/aa/展示导航分类表信息报表.xls");
		HSSFWorkbook book = new HSSFWorkbook(is);
		//获取男性数据
		HSSFSheet sheet = book.getSheetAt(0);
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			HSSFRow row = sheet.getRow(i);
			if(row.getCell(6)==null) {
				continue;
			}
			String cate3Id = row.getCell(6).getStringCellValue();
			if(cate3Id==null || "".equals(cate3Id.trim())) {
				continue;
			}
			String cate3Name = row.getCell(7).getStringCellValue();
			String cateShow3Id = row.getCell(4).getStringCellValue();
			String cateShow3Name = row.getCell(5).getStringCellValue();
			//System.out.println(cate3Id);
			cate3IdMap.put(cate3Id, cate3Name);
			show3IdMap.put(cateShow3Id, cateShow3Name);
			
			if(cate3IdShowRelation.get(cate3Id)!=null) {
				String show3Id = cate3IdShowRelation.get(cate3Id);
				cate3IdShowRelation.put(cate3Id, show3Id+","+cateShow3Id);
			}else {
				cate3IdShowRelation.put(cate3Id, cateShow3Id);
			}
		}
	}
	
	public static void getShowId3AndPropRelation() throws Exception{
		InputStream is = new FileInputStream("e:/aa/展示分类筛选属性.xls");
		HSSFWorkbook book = new HSSFWorkbook(is);
		//获取男性数据
		HSSFSheet sheet = book.getSheetAt(0);
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			HSSFRow row = sheet.getRow(i);
			int cate3Id = (int)row.getCell(0).getNumericCellValue();
			//System.out.println(cate3Id);
			int propId = (int)row.getCell(1).getNumericCellValue();
			String name = row.getCell(2).getStringCellValue();
			String selectStatus = row.getCell(3).getStringCellValue();
			String preStatus = row.getCell(4).getStringCellValue();
			String showStatus = row.getCell(5).getStringCellValue();
			int sortScore = (int)row.getCell(6).getNumericCellValue();
		}
	}
	
	public static void init() throws Exception{
		InputStream is = new FileInputStream("e:/aa/result.xls");
		HSSFWorkbook book = new HSSFWorkbook(is);
		//获取男性数据
		HSSFSheet sheet = book.getSheetAt(0);
		for (int i = 1; i < sheet.getLastRowNum(); i++) {
			HSSFRow row = sheet.getRow(i);
			String word = row.getCell(0).getStringCellValue();
			int cateId3 = (int)row.getCell(1).getNumericCellValue();
			wordAndCateId3Relation.put(word, cateId3);
		}
	}

}
