package com.gome.gmp.common.mybaits.plugins.page;

/**
 * @author gongchangming
 * @date 2015-5-19
 * @project_name gome-gcc-common
 * @company 国美在线有限公司
 * @desc 分页工具类
 */
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.PropertyException;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.apache.log4j.Logger;


@SuppressWarnings("unchecked")
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class PagePlugin implements Interceptor {

	private static Dialect dialectObject = null; // 数据库方言

	private static String pageSqlId = ""; // mybaits的数据库xml映射文件中需要拦截的ID(正则匹配)

	public static String DBTYPE_MYSQL = "mysql";

	public static String DBTYPE_ORACLE = "oracle";

	Logger logger = Logger.getLogger(PagePlugin.class);

	// public Object intercept(Invocation ivk) throws Throwable {
	// if (!(ivk.getTarget() instanceof RoutingStatementHandler)) {
	// return ivk.proceed();
	// }
	// RoutingStatementHandler statementHandler = (RoutingStatementHandler)
	// ivk.getTarget();
	// BaseStatementHandler delegate = (BaseStatementHandler)
	// SystemUtil.getValueByFieldName(statementHandler, "delegate");
	// MappedStatement mappedStatement = (MappedStatement)
	// SystemUtil.getValueByFieldName(delegate, "mappedStatement");
	// // BoundSql封装了sql语句
	// BoundSql boundSql = delegate.getBoundSql();
	// // 获得查询对象
	// Object parameterObject = boundSql.getParameterObject();
	// // 根据参数类型判断是否是分页方法
	// if (!(parameterObject instanceof Query)) {
	// return ivk.proceed();
	// }
	// logger.debug(" beginning to intercept page SQL...");
	// Connection connection = (Connection) ivk.getArgs()[0];
	// String sql = boundSql.getSql();
	// Query query = (Query) parameterObject;
	// // 查询参数对象
	// Pager pager = null;
	// // 查询条件Map
	// Map<String, Object> conditions = query.getQueryParams();
	// pager = query.getPager();
	// // 拼装查询条件
	// if (conditions != null) {
	// Set<String> keys = conditions.keySet();
	// Object value = null;
	// StringBuffer sb = new StringBuffer();
	// boolean first = true;
	// for (String key : keys) {
	// value = conditions.get(key);
	// if (first) {
	// sb.append(" where ").append(key).append(value);
	// first = !first;
	// } else {
	// sb.append(" and ").append(key).append(value);
	// }
	// }
	// sql += sb.toString();
	// }
	// // 获取查询数来的总数目
	// String countSql = "SELECT COUNT(0) FROM (" + sql + ") AS tmp ";
	// PreparedStatement countStmt = connection.prepareStatement(countSql);
	// BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(),
	// countSql, boundSql.getParameterMappings(), parameterObject);
	// setParameters(countStmt, mappedStatement, countBS, parameterObject);
	// ResultSet rs = countStmt.executeQuery();
	// int count = 0;
	// if (rs.next()) {
	// count = rs.getInt(1);
	// }
	// rs.close();
	// countStmt.close();
	// // 设置总记录数
	// pager.setTotalResult(count);
	// // 设置总页数
	// pager.setTotalPage((count + pager.getShowCount() - 1) /
	// pager.getShowCount());
	// // 放到作用于
	// PageContext.getInstance().set(pager);
	// // 拼装查询参数
	// String pageSql = generatePageSql(sql, pager);
	// SystemUtil.setValueByFieldName(boundSql, "sql", pageSql);
	// logger.debug("generated pageSql is : " + pageSql);
	// return ivk.proceed();
	// }
	@SuppressWarnings("rawtypes")
	public Object intercept(Invocation ivk) throws Throwable {
		// TODO Auto-generated method stub
		if (ivk.getTarget() instanceof RoutingStatementHandler) {
			RoutingStatementHandler statementHandler = (RoutingStatementHandler) ivk.getTarget();
			BaseStatementHandler delegate = (BaseStatementHandler) ReflectHelper.getValueByFieldName(statementHandler, "delegate");
			MappedStatement mappedStatement = (MappedStatement) ReflectHelper.getValueByFieldName(delegate, "mappedStatement");
			if (mappedStatement.getId().matches(pageSqlId)) { // 拦截需要分页的SQL
				BoundSql boundSql = delegate.getBoundSql();
				Object parameterObject = boundSql.getParameterObject();// 分页SQL<select>中parameterType属性对应的实体参数，即Mapper接口中执行分页方法的参数,该参数不得为空
				if (parameterObject == null) {
//					throw new NullPointerException("parameterObject尚未实例化！");
					return ivk.proceed();
				} else {
					Connection connection = (Connection) ivk.getArgs()[0];
					String sql = boundSql.getSql();
					String countSql = "select count(0) from (" + sql + ") as tmp_count"; // 记录统计
					PreparedStatement countStmt = connection.prepareStatement(countSql);
					BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql, boundSql.getParameterMappings(), parameterObject);
					setParameters(countStmt, mappedStatement, countBS, parameterObject);
					ResultSet rs = countStmt.executeQuery();
					int count = 0;
					if (rs.next()) {
						count = rs.getInt(1);
					}
					rs.close();
					countStmt.close();
					// System.out.println(count);
					Pager page = null;
					if (parameterObject instanceof Pager) { // 参数就是Page实体
						page = (Pager) parameterObject;
						page.setEntityOrField(true); // 见com.flf.entity.Page.entityOrField
						page.setTotal(count);
					} else { // 参数为某个实体，该实体拥有Page属性
						Field pageField = ReflectHelper.getFieldByFieldName(parameterObject, "page");
						if (pageField != null) {
							page = (Pager) ReflectHelper.getValueByFieldName(parameterObject, "page");
							if (page == null){
								page = new Pager();
							}
							page.setEntityOrField(false); // 见com.flf.entity.Page.entityOrField
							page.setTotal(count);
							ReflectHelper.setValueByFieldName(parameterObject, "page", page); // 通过反射，对实体对象设置分页对象
						} else {
							throw new NoSuchFieldException(parameterObject.getClass().getName() + "不存在 page 属性！");
						}
					}
					String pageSql = generatePageSql(sql, page);
					ReflectHelper.setValueByFieldName(boundSql, "sql", pageSql); // 将分页sql语句反射回BoundSql.
				}
			}
		}
		return ivk.proceed();
	}




	private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject) throws SQLException {
		ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		if (parameterMappings != null) {
			Configuration configuration = mappedStatement.getConfiguration();
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);
				if (parameterMapping.getMode() != ParameterMode.OUT) {
					Object value;
					String propertyName = parameterMapping.getProperty();
					PropertyTokenizer prop = new PropertyTokenizer(propertyName);
					if (parameterObject == null) {
						value = null;
					} else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
						value = parameterObject;
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						value = boundSql.getAdditionalParameter(propertyName);
					} else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX) && boundSql.hasAdditionalParameter(prop.getName())) {
						value = boundSql.getAdditionalParameter(prop.getName());
						if (value != null) {
							value = configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));
						}
					} else {
						value = metaObject == null ? null : metaObject.getValue(propertyName);
					}
					TypeHandler typeHandler = parameterMapping.getTypeHandler();
					if (typeHandler == null) {
						throw new ExecutorException("There was no TypeHandler found for parameter " + propertyName + " of statement " + mappedStatement.getId());
					}
					typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
				}
			}
		}
	}

	/**
	 * 根据数据库方言，生成特定的分页sql
	 * 
	 * @param sql
	 * @param pager
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String generatePageSql(String sql, Pager pager) {
		if (pager != null) {
			String dbType = pager.getDbType();
			if (dbType == null || DBTYPE_MYSQL.equals(dbType)) {
				dialectObject = new MySQLDialect();
			} else if (DBTYPE_ORACLE.equals(dbType)) {
				dialectObject = new OracleDialect();
			}
			// pageNow默认是从1，而已数据库是从0开始计算的．所以(page.getPageNow()-1)
			// int pageNum = pager.getPageNo();
			return dialectObject.getLimitString(sql, pager.getStartRow(), pager.getPageSize(), pager.getSortOrder(), pager.getSortField());
		}
		return sql;
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties p) {
		String dialect = ""; // 数据库方言
		dialect = p.getProperty("dialect");
		if (StringUtils.isEmpty(dialect)) {
			try {
				throw new PropertyException("dialect property is not found!");
			} catch (PropertyException e) {
				e.printStackTrace();
			}
		} else {
			try {
				dialectObject = (Dialect) Class.forName(dialect).getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				throw new RuntimeException(dialect + ", init fail!\n" + e);
			}
		}
		pageSqlId = p.getProperty("pageSqlId");// 根据id来区分是否需要分页
		if (StringUtils.isEmpty(pageSqlId)) {
			try {
				throw new PropertyException("pageSqlId property is not found!");
			} catch (PropertyException e) {
				e.printStackTrace();
			}
		}
	}
}
