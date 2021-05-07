package com.enableets.edu.pakage.framework.ppr.core.tablecopy;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 复制表数据组件
 * @author duffy_ding
 * @since 2018/05/30
 */
public class TableCopyComponent {
	
	/** 插入数据库语句模板 */
	private static final String INSERT_TABLE_TEMPLATE = "INSERT IGNORE INTO %s (%s) VALUES %s";

	/** 替换sql 参数 值形式 */
	private static final String SQL_PARAM_VALUE = "'%s'";

	/** 日志打印类 */
	private static final Logger logger = LoggerFactory.getLogger(TableCopyComponent.class);
	
	/** 日期格式化类 */
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/** 源数据库操作类 */
	private JdbcTemplate origin;
	
	/** 目标数据库操作类 */
	private JdbcTemplate target;
	
	/** 查询语句文件 资源 */
	private Resource resource;
	
	public TableCopyComponent(JdbcTemplate origin, JdbcTemplate target, String resourcePath) {
		this.origin = origin;
		this.target = target;
		this.resource = new ClassPathResource(resourcePath);
	}

	/**
	 * 复制数据库表数据
	 * @param param sql参数
	 * @throws IOException
	 */
	public void copyTable(Map<String, Object> param) throws IOException {
		copyTable(resource.getInputStream(), param);
	}
	
	/**
	 * 复制数据库表数据
	 * @param file xml文件
	 * @param param sql参数
	 * @throws IOException
	 */
	public void copyTable(File file, Map<String, Object> param) throws IOException {
		Assert.notNull(origin, "源jdbcTemplate操作类不能为空");
		Assert.notNull(target, "目标jdbcTemplate操作类不能为空");
		Assert.isTrue(file.exists(), "sql文件不存在");
		this.copyTable(new FileInputStream(file), param);
	}
	
	/**
	 * 复制数据库表数据
	 * @param inputStream xml文件流
	 * @param param sql参数
	 * @throws IOException
	 */
	public void copyTable(InputStream inputStream, Map<String, Object> param) throws IOException {
		Assert.notNull(origin, "源jdbcTemplate操作类不能为空");
		Assert.notNull(target, "目标jdbcTemplate操作类不能为空");
		// 1. 从xml文件中读取sql信息
		List<SqlResourceProperty> sqlResourceList = getSqlFromXMLResource(inputStream);
		
		if (isEmpty(sqlResourceList)) {
			return;
		}

		List<Statement> statementList = new ArrayList<Statement>();
		for (SqlResourceProperty sqlResource : sqlResourceList) {
			// 2. 查询sql参数替换
			String sql = sqlResource.getSql();
			for (Entry<String, Object> entry : param.entrySet()) {
				sql = sql.replaceAll(buildRegex(entry.getKey()), processValue(entry.getValue()));
			}
			logger.debug("[" + sqlResource.getId() + "] " + sql);
			// 3. 查询结果集
			List<Map<String, Object>> resultList = origin.queryForList(sql);
			// 4. 拼接保存sql
			statementList.addAll(buildBatchInsertStatement(sqlResource.getTableName(), resultList));
		}
		
		// 5. 执行保存
		save(statementList);
	}

	/**
	 * 保存数据到目标数据库
	 * @param statementList 语句list
	 */
	@Transactional
	public void save(List<Statement> statementList) {
		if (isEmpty(statementList)) {
			return;
		}
		int i = 0;
		for (Statement stat : statementList) {
			long s1 = System.currentTimeMillis();
			int count = target.update(stat.getSql());
			logger.debug(String.format("第%s条\t插入条数%s\t保存耗时%s\t%s", i++, count, System.currentTimeMillis() - s1, stat.getSql()));
		}
	}

	/**
	 * 获取插入sql语句
	 * @param tableName 目标表名称
	 * @param resultList 数据结果集
	 * @return 
	 */
	public List<Statement> buildBatchInsertStatement(String tableName, List<Map<String, Object>> resultList) {
		if (isEmpty(resultList)) {
			return Collections.emptyList();
		}
		List<Statement> statementList = new ArrayList<Statement>();
		// 拼接列
		String columns = joinInsertColumns(resultList.get(0).keySet());
		if (columns == null) {
			return Collections.emptyList();
		}
		String[] columnArr = columns.split(",");
		
		String valueItems = "";
		// 每10条生成一条保存sql
		for (int i = 0; i < resultList.size(); i++) {
			valueItems += String.format(",(%s)", joinInsertValue(columnArr,  resultList.get(i)));
			if (i % 10 == 9) {
				statementList.add(new Statement(String.format(INSERT_TABLE_TEMPLATE, tableName, columns, valueItems.substring(1))));
				valueItems = "";
			}
		}
		if (valueItems != "") {
			statementList.add(new Statement(String.format(INSERT_TABLE_TEMPLATE, tableName, columns, valueItems.substring(1))));
		}
		return statementList;
	}

	/**
	 * 拼接保存语句 值
	 * @param columnArr 数据列数组
	 * @param map 数据map
	 * @return
	 */
	private String joinInsertValue(String[] columnArr, Map<String, Object> map) {
		if (map == null) {
			return null;
		}
		String valueItem = "";
		for (String column : columnArr) {
			valueItem += "," + processValue(map.get(column));
		}
		return valueItem.substring(1);
	}

	/**
	 * 拼接保存语句 列 值
	 * @param columnSet 数据列集合
	 * @return
	 */
	private String joinInsertColumns(Set<String> columnSet) {
		if (columnSet == null || columnSet.size() == 0) {
			return null;
		}
		String columns = "";
		for (String column : columnSet) {
			columns += "," + column;
		}
		return columns.substring(1);
	}

	/**
	 * 获取插入sql语句,每条插入一条数据
	 * @param tableName 目标表名称
	 * @param resultList 数据结果集
	 * @return 
	 */
	public List<Statement> buildInsertStatement(String tableName, List<Map<String, Object>> resultList) {
		if (isEmpty(resultList)) {
			return Collections.emptyList();
		}
		List<Statement> statementList = new ArrayList<Statement>();
		for (Map<String, Object> result : resultList) {
			String insertSql = buildInsertSql(tableName, result);
			if (insertSql != null) {
				logger.debug("[" + tableName + "] " + insertSql);
				statementList.add(new Statement(insertSql));
			}
		}
		return statementList;
	}

	/**
	 * 根据数据结果生成插入sql语句
	 * @param tableName 目标数据库表名称
	 * @param param 参数
	 * @return
	 */
	private static String buildInsertSql(String tableName, Map<String, Object> param) {
		if (param == null) {
			return null;
		}
		String columns = "", values = "";
		for (Entry<String, Object> resultEntry : param.entrySet()) {
			if (resultEntry.getValue() != null) {
				columns += ", " + resultEntry.getKey();
				values += ", " + processValue(resultEntry.getValue());
			}
		}
		if (!StringUtils.isEmpty(columns)) {
			return String.format(INSERT_TABLE_TEMPLATE, tableName, columns.substring(1), String.format("(%s)", values.substring(1)));
		}
		return null;
	}
	
	/**
	 * 判断list是否为空
	 * @param list 集合数据
	 * @return true/false
	 */
	public static Boolean isEmpty(List<?> list) {
		return list == null || list.size() == 0;
	}
	
	/**
	 * 生成sql替换字符串
	 * @param key 传入 参数map key
	 * @return sql替换字符串
	 */
	private static String buildRegex(String key) {
		return "\\{" + key + "\\}";
	}
	
	/**
	 * 将值变成字符串 拼接sql
	 * @param value 值
	 * @return 字符串
	 */
	private static String processValue(Object value) {
		if (value == null) {
			return "null";
		}
		if (value instanceof Date) {
			return String.format(SQL_PARAM_VALUE, sdf.format(value));
		}
		if (value instanceof List){
			List list = (List) value;
			return list.stream().map(e -> String.format(SQL_PARAM_VALUE, e)).reduce((x, y) -> x + "," + y).get().toString();
		}
		String str = StringUtils.replace(value.toString(), "'", "''");
		str = StringUtils.replace(str, "\\", "\\\\");
		return String.format(SQL_PARAM_VALUE, str);
	}

	/**
	 * 从xml文件中读取所有sql信息
	 * @param inputStream xml文件
	 * @return sql 属性
	 * @throws IOException 文件读取失败
	 */
	private static List<SqlResourceProperty> getSqlFromXMLResource(InputStream inputStream) throws IOException {
		Document document = Jsoup.parse(inputStream, "utf-8", "");
		Elements elements = document.getElementsByTag("sql");
		
		List<SqlResourceProperty> sqlResourceList = new ArrayList<SqlResourceProperty>();
		for (Element ele : elements) {
			String id = ele.attr("id");
			sqlResourceList.add(new SqlResourceProperty(id, ele.attr("targetTableName"), ele.text()));
		}
		return sqlResourceList;
	}
}
