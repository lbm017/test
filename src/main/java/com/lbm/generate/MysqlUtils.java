package com.lbm.generate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class MysqlUtils extends CommonCodeGen{

	private static String host = System.getenv("VIP_MSEARCH_SOLR_DB_SLAVE_HOST");
	private static String port = System.getenv("VIP_MSEARCH_SOLR_DB_SLAVE_PORT");
	private static String name = "com.mysql.jdbc.Driver";
	private static String user = System.getenv("VIP_MSEARCH_SOLR_DB_SLAVE_USERNAME");
	private static String password = System.getenv("VIP_MSEARCH_SOLR_DB_SLAVE_PASSWORD");

	public static void getTableFields() {
		Connection connetion = null;
		PreparedStatement prepareStatement = null;
		ResultSet rs = null;
		String jdbcUrl = "jdbc:mysql://%s:%s/" + databaseName + "?useUnicode=true&characterEncoding=UTF8";
		String url = String.format(jdbcUrl, host, port);
		
		String sql = "select * from " + tableName;

		try {
			Class.forName(name);// 指定连接类型
			connetion = DriverManager.getConnection(url, user, password);// 获取连接
			prepareStatement = connetion.prepareStatement(sql);
			rs = prepareStatement.executeQuery();

			ResultSetMetaData data = rs.getMetaData();
			for (int i = 1; i <= data.getColumnCount(); i++) {
				// 获得指定列的列名
				String columnName = data.getColumnName(i);
				int columnType = data.getColumnType(i);
				
				int type = 2;
				switch(columnType) {
				case -6:
					type = 1;
					break;
				case 12:
					type = 2;
					break;
				case 93:
					type = 3;
					break;
				case -5:
					type = 4;
					break;
				}
				
				Field field = new Field();
				field.setFieldName(columnName);
				field.setFiledType(type);
				fieldList.add(field);
			}


		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				prepareStatement.close();
				connetion.close();
			} catch (SQLException e) {
			}
		}
	}
}
