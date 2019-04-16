package com.lbm.es;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ESSpiders {
	public static void main(String[] args) throws Exception {
		
		//aa.txt文件里面是ES上面文档中各个页面的地址，从网上再下一份即可
		InputStream is = new FileInputStream("e:/aa/aa.txt");
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String data="";
		
		StringBuffer sb = new StringBuffer();
		while((data=br.readLine())!=null) {
			sb.append(data);
		}
		br.close();
		String html = sb.toString();
		
		Document doc = Jsoup.parse(html);
		
		Elements elements = doc.getElementsByTag("a");
		Map<String,String> map = new LinkedHashMap<String,String>();
		for(int i=0;i<elements.size();i++) {
			try {
				if(i%10==0) {
					Random ran = new Random();
					int num = ran.nextInt(10);
					Thread.currentThread().sleep(num*1000);
				}
				Random ran = new Random();
				int num = ran.nextInt(5);
				Thread.currentThread().sleep(num*1000);
				Element element = elements.get(i);
				String href = element.attr("href");
				String title = element.text();
				System.out.println(title+":"+href);
				//开始从网上爬数据
				Document doc1 = Jsoup.connect("https://www.elastic.co/guide/cn/elasticsearch/guide/current/"+href)
						  .data("query", "Java")
						  .userAgent("Mozilla")
						  .cookie("auth", "token")
						  .timeout(5000)
						  .get();
				Elements elements1 = doc1.getElementsByClass("titlepage");
				Element element1 = elements1.get(0).parent();
				String content = element1.outerHtml(); 
				String fileName = href;
				OutputStream os = new FileOutputStream("e:/es中文文档/"+fileName);
				BufferedOutputStream bos = new BufferedOutputStream(os);
				OutputStreamWriter osw = new OutputStreamWriter(bos,"gb2312");
				BufferedWriter bw = new BufferedWriter(osw);
				bw.write(content);
				bw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
