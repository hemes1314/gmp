package com.gome.gmp.common.mybaits.plugins;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gome.gmp.common.mybaits.plugins.page.Dialect;
import com.gome.gmp.common.mybaits.plugins.page.MySQLDialect;
import com.gome.gmp.common.mybaits.plugins.page.OracleDialect;
import com.gome.gmp.common.mybaits.plugins.page.Pager;
import com.gome.gmp.common.mybaits.plugins.page.ReflectHelper;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class PagePlugin implements Interceptor {

	private final Logger logger = LoggerFactory.getLogger(PagePlugin.class);

	private static Dialect dialectObject = null; // 数据库方言

	private static String pageSqlId = ""; // mybaits的数据库xml映射文件中需要拦截的ID(正则匹配)

	public static String DBTYPE_MYSQL = "mysql";

	public static String DBTYPE_ORACLE = "oracle";

	public Object intercept(Invocation ivk) throws Throwable {
		if (ivk.getTarget() instanceof RoutingStatementHandler) {
			RoutingStatementHandler statementHandler = (RoutingStatementHandler) ivk.getTarget();
			BaseStatementHandler delegate = (BaseStatementHandler) ReflectHelper.getValueByFieldName(statementHandler, "delegate");
			MappedStatement mappedStatement = (MappedStatement) ReflectHelper.getValueByFieldName(delegate, "mappedStatement");
			if (mappedStatement.getId().matches(pageSqlId)) { // 拦截需要分页的SQL
				BoundSql boundSql = delegate.getBoundSql();
				Object parameterObject = boundSql.getParameterObject();// 分页SQL<select>中parameterType属性对应的实体参数，即Mapper接口中执行分页方法的参数,该参数不得为空
				if (parameterObject == null) {
					return ivk.proceed();
				} else {
					Pager pager = null;
					if (parameterObject instanceof Pager) { // 参数就是Page实体
						pager = (Pager) parameterObject;
						pager.setEntityOrField(true); // 见com.flf.entity.Page.entityOrField
					} else if (parameterObject instanceof Map) {
						for (Entry entry : (Set<Entry>) ((Map) parameterObject).entrySet()) {
							if (entry.getValue() instanceof Pager) {
								pager = (Pager) entry.getValue();
								break;
							}
						}
					} else { // 参数为某个实体，该实体拥有Page属性
						Field pageField = ReflectHelper.getFieldByFieldName(parameterObject, "pager");
						if (pageField != null) {
							pager = (Pager) ReflectHelper.getValueByFieldName(parameterObject, "pager");
							if (pager == null) {
								pager = new Pager();
							}
							pager.setEntityOrField(false); // 见com.flf.entity.Page.entityOrField
							ReflectHelper.setValueByFieldName(parameterObject, "pager", pager); // 通过反射，对实体对象设置分页对象
						} else {
							pager = ReflectHelper.getValueByFieldType(parameterObject, Pager.class);
							if (pager == null) {
								return ivk.proceed();
							}
						}
					}
					String sql = boundSql.getSql();
					PreparedStatement countStmt = null;
					ResultSet rs = null;
					try {
						Connection connection = (Connection) ivk.getArgs()[0];
						String countSql = "select count(1) from (" + sql + ") as tmp_count"; // 记录统计
						countStmt = connection.prepareStatement(countSql);
						BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql, boundSql.getParameterMappings(), parameterObject);
						setParameters(countStmt, mappedStatement, countBS, parameterObject);
						int count = 0;
						rs = countStmt.executeQuery();
						if (rs.next()) {
							count = ((Number) rs.getObject(1)).intValue();
						}
						pager.setTotal(count);
					} finally {
						rs.close();
						countStmt.close();
					}
					String pageSql = generatePagesSql(sql, pager);
					ReflectHelper.setValueByFieldName(boundSql, "sql", pageSql); // 将分页sql语句反射回BoundSql.
					logger.debug("generated pageSql is : " + pageSql);
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
	private String generatePagesSql(String sql, Pager pager) {
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
