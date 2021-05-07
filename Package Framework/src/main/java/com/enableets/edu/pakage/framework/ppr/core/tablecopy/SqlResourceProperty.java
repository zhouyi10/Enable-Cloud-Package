package com.enableets.edu.pakage.framework.ppr.core.tablecopy;

/**
 * 复制表数据sql属性对象
 * @author duffy_ding
 * @since 2018/05/30
 */
public class SqlResourceProperty {

	/** sql 标识 */
	private String id;
	
	/** 表明 */
	private String tableName;
	
	/** sql语句 */
	private String sql;
	
	public SqlResourceProperty() {
	}
	
	public SqlResourceProperty(String id, String tableName, String sql) {
		this.id = id;
		this.tableName = tableName;
		this.sql = sql;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
}