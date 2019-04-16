package com.lbm.es;

import java.net.InetAddress;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class ClientFactory {
	public Client transportClient() {
		//创建client
		TransportClient transportClient = null;
		try {
			//设置集群名称(配置信息)
			Settings settings = Settings.builder()
//			.put("cluster.name", "msearch-qa")
			.put("cluster.name", "pallas-staging-vm")
			.build();
			//创建client(添加连接地址)
			transportClient = new PreBuiltTransportClient(settings)
//			.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.199.174.166"), 9300));
			.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.199.173.106"), 9300));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return transportClient;
	}    
//	public static Client getClientForDelete() {
//		Settings settings = Settings.builder().put("cluster.name", "pallas-staging-vm").build();
//
//        TransportClient client = new PreBuiltTransportClient(settings)
//        		.a
//                .addPlugin(DeleteByQueryPlugin.class)
//
//                .build()
//
//                .addTransportAddress(new InetSocketTransportAddress(
//
//                        InetAddress.getByName("localhost"), 9300));
//	}    
}
