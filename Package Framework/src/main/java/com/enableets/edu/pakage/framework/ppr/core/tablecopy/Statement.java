package com.enableets.edu.pakage.framework.ppr.core.tablecopy;

/**
 * sql 语句
 * @author duffy_ding
 * @since 2018/05/30
 */
public class Statement {
	
	/** sql 语句 */
	private String sql;
	
	public Statement() {
	}

	public Statement(String sql) {
		this.sql = sql;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	
}
