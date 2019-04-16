package com.lbm.es;

import java.util.Date;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
import org.elasticsearch.action.admin.indices.alias.exists.AliasesExistResponse;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesResponse;
import org.elasticsearch.action.admin.indices.close.CloseIndexResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;


public class ESUtils {
	static ClientFactory clientFactory=new ClientFactory();
	static Client client=clientFactory.transportClient();
	/**
	 * 关闭索引
	 * @param client
	 * @param index
	 * @return
	 */
	public static boolean closeIndex(String index){
	    IndicesAdminClient indicesAdminClient = client.admin().indices();
	    CloseIndexResponse response = indicesAdminClient.prepareClose(index).get();
	    return response.isAcknowledged();
	}
	/**
	 * 打开索引
	 * @param client
	 * @param index
	 * @return
	 */
	public static boolean openIndex(String index){
	    IndicesAdminClient indicesAdminClient = client.admin().indices();
	    OpenIndexResponse response = indicesAdminClient.prepareOpen(index).get();
	    return response.isAcknowledged();
	}
	/**
	 * 为索引创建别名
	 * @param client
	 * @param index
	 * @param alias
	 * @return
	 */
	public static boolean addAliasIndex( String index , String alias){
	    IndicesAdminClient indicesAdminClient = client.admin().indices();
	    IndicesAliasesResponse response = indicesAdminClient.prepareAliases().addAlias(index, alias).get();
	    return response.isAcknowledged();
	}
	/**
	 * 判断别名是否存在
	 * @param client
	 * @param aliases
	 * @return
	 */
	public static boolean isAliasExist(String aliases){
	    IndicesAdminClient indicesAdminClient = client.admin().indices();
	    AliasesExistResponse response = indicesAdminClient.prepareAliasesExist(aliases).get();
	    return response.isExists();
	}
	/**
	 * 获取别名
	 * @param client
	 * @param aliases
	 */
	public static void getAliasIndex(String aliases){
	    IndicesAdminClient indicesAdminClient = client.admin().indices();
	    GetAliasesResponse response = indicesAdminClient.prepareGetAliases(aliases).get();
//	    ImmutableOpenMap<String, List<AliasMetaData>> aliasesMap = response.getAliases();
//	    UnmodifiableIterator<String> iterator = aliasesMap.keysIt();
//	    while(iterator.hasNext()){
//	        String key = iterator.next();
//	        List<AliasMetaData> aliasMetaDataList = aliasesMap.get(key);
//	        for(AliasMetaData aliasMetaData : aliasMetaDataList){
////	            logger.info("--------- getAliasIndex {}", aliasMetaData.getAlias());
//	        }
//	    }
	    System.out.println(response);
	}
	/**
	 * 删除别名
	 * @param client
	 * @param index
	 * @param aliases
	 * @return
	 */
	public static boolean deleteAliasIndex(String index, String aliases){
	    IndicesAdminClient indicesAdminClient = client.admin().indices();
	    IndicesAliasesResponse response = indicesAdminClient.prepareAliases().removeAlias(index, aliases).get();
	    return response.isAcknowledged();
	}
	/**
	 * 删除一条数据
	 * @param args
	 */
	public static void deleteOneData(String index,String type,String id){
		DeleteResponse dResponse=client.prepareDelete(index,type, id).get(); 
		System.out.println("删除一条数据");
		
	}
	
	/**
	 * 从老的es获取所有的mid和spu信息
	 * @param index
	 * @param type
	 * @return
	 */
	public static SearchResponse findIdsByPage(String index,String type,int count,int begin_count){
		System.out.println("from size 模式启动！");
		Date begin = new Date();
		String[] inSource={"m_id","merchandise_v_spu_id"};
		String[] excludes=new String[0];
		SearchResponse response = client.prepareSearch(index)
                 .setTypes(type).setQuery(QueryBuilders.matchAllQuery())
                 .setFetchSource(inSource, excludes)                
                 .setFrom(begin_count)
                 .setSize(count)
                 .execute().actionGet();
		Date end = new Date();
		System.out.println("耗时: "+(end.getTime()-begin.getTime()));
		return response;
		
	}
	/**
	 * 根据spu从es获取商品信息
	 * @param args
	 */
	public static SearchResponse getMerchandiseBySpu(String index,String type,String spuId) {
		MatchQueryBuilder query = QueryBuilders.matchQuery("merchandise_v_spu_id", spuId);
		SearchResponse response = client.prepareSearch(index)
                .setTypes(type)
                .setQuery(query)
                .execute().actionGet();
		return response;
	}
	
	/**
	* 往ES添加一条数据
	* 
	*/
	public void insertOneData(String index, String type, String json) {
		IndexResponse response = clientFactory.transportClient().prepareIndex(index, type)  
	            .setSource(json)  
	            .execute()  
	            .actionGet();  
	}

	/**
	* 删除索引
	* 
	* @param index
	*            要删除的索引名
	*/
	public static void deleteIndex(String index) {
		DeleteIndexResponse deleteIndexResponse = client.admin().indices().prepareDelete(index).get();
		System.out.println(deleteIndexResponse.isAcknowledged()); // true表示成功
	}
	/**
	* 创建一个索引
	* 
	* @param indexName
	*            索引名
	*/
	public static void createIndex(String indexName) {
		try {
		CreateIndexResponse indexResponse = client.admin().indices().prepareCreate(indexName).get();
		// 生产环境下，一律换成 logger.info()
		System.out.println(indexResponse.isAcknowledged()); // true表示创建成功
		} catch (ElasticsearchException e) {
		e.printStackTrace();
		}
	}
	/**
	* 新加一条数据
	*/
	public void createDoc(String index, String type, String json) {
	    IndexResponse response = client.prepareIndex(index, type)  
	            .setSource(json)  
	            .execute()  
	            .actionGet();  
	}

	
	public static void main(String[] args) {
//		getAliasIndex("msearch");
//		System.out.println(isAliasExist("msearch"));
//		System.out.println(deleteAliasIndex("vsearch01","msearch"));
//		deleteOneData("vsearch01","product","AWJhz6QXp3Estqm8F9JW");
		//给索引起别名
//		addAliasIndex("vsearch01","msearch");
//		createIndex("vsearch03");
		//关闭索引
//		closeIndex("vsearch01");
//		findIdsByPage("vsearch01","product");
//		getMerchandiseBySpu("vsearch01","product","7737823555318051008");
		//打开索引
//		openIndex("vsearch02");
		//删除索引别名
//		deleteAliasIndex("vsearch01","msearch");
		//添加索引别名
//		addAliasIndex("vsearch01","msearch");
		//
//		deleteIndex("vsearch01");
		createIndex("vsearch02");
	}

}
