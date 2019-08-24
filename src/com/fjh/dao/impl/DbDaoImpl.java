package com.fjh.dao.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fjh.annotation.MyColumn;
import com.fjh.annotation.MyKey;
import com.fjh.annotation.MyTable;
import com.fjh.dao.DbDao;
import com.fjh.exception.MyException;
import com.fjh.util.DbUtil;
import com.google.gson.Gson;

/**
 * 
 * @ClassName: DbDaoImpl
 * @Description: TODO(数据库操作业务实现类)
 * @author 冯佳豪
 * @date 2019年7月9日
 *
 */
public class DbDaoImpl implements DbDao {
	
	@Override
	public Object excuteSql(String sql,Object obj) throws SQLException {
		// 查询学生表，查询操作不涉及事务回滚
		Connection connection = DbUtil.getConnection();
		PreparedStatement statement = null;
		ResultSet rs = null;
		Class clazz = obj.getClass();
		// 通过字节码对象获取其指定注解值
		// 获取所有声明属性
		Field[] declaredFields = clazz.getDeclaredFields();
		try {
			statement = connection.prepareStatement(sql);// 预编译sql语句
			rs = statement.executeQuery();// 执行sql语句
			// 获取元数据
			ResultSetMetaData metaData = rs.getMetaData();
			// 获取元数据多少列
			int columnCount = metaData.getColumnCount();
			// 获取查询的值
			while (rs.next()) {
				for (int i = 1; i <= columnCount; i++) {
					Object data = rs.getObject(i);
					Object object = data;
					String s = metaData.getColumnLabel(i) + ":" + object;
					try {
						Object param = rs.getObject(i);
						declaredFields[i - 1].setAccessible(true);
						declaredFields[i - 1].set(obj, param);	
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(s);
				}
				System.out.println("===============================");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.close(connection, statement, rs);
		}
		return obj;
	}

	@Override
	public Long excuteSql(String sql, Object[] param, boolean generateKey) throws SQLException {
		long result = -1;
		Connection connection = DbUtil.getConnection();
		PreparedStatement sts = null;

		sts = connection.prepareStatement(sql, sts.RETURN_GENERATED_KEYS);
		for (int i = 0; i < param.length; i++) {
			sts.setObject(i + 1, param[i]);
		}
		result = sts.executeUpdate();
		if (generateKey) {
			ResultSet rs = sts.getGeneratedKeys();
			while (rs.next()) {
				result = (long) rs.getObject(1);
			}
		}
		DbUtil.close(null, sts);
		return result;
	}

	@Override
	public Long excuteSql(List<Map<String, Object>> sqls) throws SQLException {
		// 默认参数值
		Long result = -1l;
		String sql = "";
		Object[] param = new Object[] {};
		for (Map<String, Object> map : sqls) {
			sql = (String) map.get("sql");
			param = (Object[]) map.get("param");
			result = excuteSql(sql, param, false);
		}
		// 注意：每个map里都存一个sql和对应的参数 执行放在循环外只能执行最后一个
		// Long result = excuteSql(sql, param, false, true);
		// 获取当前线程connection对象，并提交事务，最后关闭连接
		try {
			DbUtil.getConnection().commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DbUtil.closeAll();
		}
		return result;
	}

	@Override
	public Long excuteSql(String sql, Object[] param) throws SQLException {
		return excuteSql(sql, param, false);
	}
	@Override
	public List<Map<String, Object>> getDatas(String sql, Object... params) throws SQLException {
		//返回值
				List<Map<String, Object>> datas = new ArrayList<Map<String,Object>>();
				Connection conn = null;
				PreparedStatement stmt = null;
				ResultSet rs  = null;
				try {
					conn = DbUtil.getConnection();
					stmt = conn.prepareStatement(sql);
					//给sql中的参数赋值
					for(int i = 0 ; i < params.length; i ++) {
						stmt.setObject(i+1, params[i]);
					}
					//执行sql语句
					rs = stmt.executeQuery();
					//读取数据  用查询的列的别名做为key  列对应值作为value
					//获取元数据
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();//总共查询多少列
					while(rs.next()) {
						//while每循环一次，可以获取一行数据  但是一行数据中有很多列 select stu_id id ,stu_name from t_stu;
						//每一行数据封装陈给一个map
						Map<String, Object> data = new HashMap<String, Object>();
						for(int i = 1; i <= columnCount; i ++) {
							String columnLabel = metaData.getColumnLabel(i);//查询列的别名，如果没有别名，那么和列名是一致
							Object value = rs.getObject(columnLabel);
							data.put(columnLabel, value);
						}
						//将一行数据放入到list中
						datas.add(data);
					}
					
					
				} catch (SQLException e) {
					e.printStackTrace();
					throw e;
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					DbUtil.closeAll();
					DbUtil.close(null, stmt, rs);
				}
				
				return datas;
	}

	@Override
	public String getJsonDatas(String sql, Object... params) throws SQLException {
		
		return new Gson().toJson(getDatas(sql, params));
	}

	@Override
	public <T> List<T> getDatas(Class<T> clazz, String sql, Object... params) throws SQLException {
		List<T> datas = new ArrayList<T>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs  = null;
		try {
			conn = DbUtil.getConnection();
			stmt = conn.prepareStatement(sql);
			//给sql中的参数赋值
			for(int i = 0 ; i < params.length; i ++) {
				stmt.setObject(i+1, params[i]);
			}
			//执行sql语句
			rs = stmt.executeQuery();
			//读取数据  用查询的列的别名做为key  列对应值作为value
			//获取元数据
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();//总共查询多少列
			while(rs.next()) {
				//while每循环一次，可以获取一行数据  但是一行数据中有很多列 select stu_id id ,stu_name from t_stu;
				//每一行数据封装一个对象  Class<T> clazz
				T obj = clazz.newInstance();
				for(int i = 1; i <= columnCount; i ++) {
					String columnLabel = metaData.getColumnLabel(i);//查询列的别名，如果没有别名，那么和列名是一致
					Object value = rs.getObject(columnLabel);
					//根据列名查找字节码中是否有该属性，如果有该属性直接给属性赋值
					try {
						Field f = clazz.getDeclaredField(columnLabel);
						f.setAccessible(true);
						if(f.getType() == Integer.class || f.getType() == int.class) {
							if("java.lang.Long".equals(value.getClass().getTypeName())) {
								Long v = (Long)value;
								f.set(obj, v.intValue());
							}else{
								f.set(obj, value);
							}
							
						} else {
							f.set(obj, value);
						}
						
					} catch (NoSuchFieldException  | SecurityException e2) {
						e2.printStackTrace();
					}
				}
				//将一行数据放入到list中
				datas.add(obj);
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeAll();
			DbUtil.close(null, stmt, rs);
		}
		
		return datas;
	}

	@Override
	public void insert(Object obj) throws MyException {
		// "INSERT into stu(stu_name,stu_no,stu_age) values(?,?,?)";
		// 获取参数的字节码
		Class clazz = obj.getClass();

		// 是否自动增长的主键,如果是自动增长的主键则需要返回一个主键，默认为false
		boolean flagIsGenerator = false;

		// 主键保存位（属性）
		Field fieldKey = null;

		// 定义sql语句，逐渐完成拼接，语句前后都加空格，提高sql语句的容错率
		StringBuffer sqlFirst = new StringBuffer(" insert into ");
		StringBuffer sqlColumns = new StringBuffer(" ");
		StringBuffer sqlValues = new StringBuffer(" values( ");
		List<Object> params = new ArrayList<Object>();
		
		// 通过字节码对象获取其指定注解值
		// 首先获取用户自定的表名
		MyTable tableName = (MyTable) clazz.getAnnotation(MyTable.class);
		// System.out.println("==============:"+tableName);
		// 这里只能判断注解值是否为空，并不能判断你的数据库表是否存在，表不存在sql在执行时会抛出异常
		if (tableName != null) {
			String myTable = tableName.tableName();
			// 连接sql语句
			sqlFirst.append(myTable).append("(");

			// 获取所有声明属性
			Field[] declaredFields = clazz.getDeclaredFields();
			// 循环获取属性的注解
			for (Field f : declaredFields) {
				// 设置属性可访问
				f.setAccessible(true);
				// 逐个存放每个属性的注解
				Annotation[] annotations = f.getAnnotations();
				// 获取注解
				for (Annotation a : annotations) {
					Object value = null;
					try {
						value = f.get(obj);// 反射获得该属性在obj里对应的值
						System.out.println("获取属性" + f.getName() + "值：" + value);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}

					// 判断是否是主键列
					if (a instanceof MyKey && value != null) {
						// 如果属性对应的是主键
						fieldKey = f;
						MyKey myKey = (MyKey) a;
						String keyName = myKey.columnName();
						System.out.println("keyName:" + keyName);
						flagIsGenerator = myKey.isGenerator();// 是否是自动增长
						if (!flagIsGenerator) {
							// 如果不是自动增长，需要拼接
							sqlColumns.append(keyName).append(" , ");
							sqlValues.append(" ? ").append(" , ");
							// 将对应的占位符值放入集合中（有序）
							params.add(value);
						} 
					}else if (a instanceof MyColumn && value != null) {
							MyColumn myColumn = (MyColumn) a;
							// System.out.println("myColumn："+myColumn);
							String columnName = myColumn.columnName();
							System.out.println("columnName:" + columnName);
							sqlColumns.append(columnName).append(" , ");
							// 同时插入对应占位符
							sqlValues.append(" ? ").append(" , ");
							// 将对应的占位符值放入集合中（有序）
							params.add(value);
							System.out.println(sqlFirst + " " + sqlColumns + " " + sqlValues + " " + params);

						}

				}

			}
			
			//加上最后的括号
			sqlColumns.append(" ) ");
			sqlValues.append(" ) ");
			// 删除最后多余的逗号
			sqlColumns.deleteCharAt(sqlColumns.lastIndexOf(","));
			sqlValues.deleteCharAt(sqlValues.lastIndexOf(","));
			// 最后完成拼接
			StringBuffer sqlLast = sqlFirst.append(sqlColumns).append(sqlValues);
			System.out.println(sqlLast.toString());

			// 执行sql
			if (flagIsGenerator) {
				// 主键自动增长需要返回主键
				Long key = null;
				try {
					key = excuteSql(sqlLast.toString(), params.toArray(), true);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				// 给主键赋值（主键规 定好只有long和int）
				fieldKey.setAccessible(true);
				try {
					if (fieldKey.getType() == int.class || fieldKey.getType() == Integer.class) {
						fieldKey.set(obj, key.intValue());
					} else {
						fieldKey.set(obj, key);// 默认long类型
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			} else {
				try {
					// 非自动增长不需要返回主键
					Long key = excuteSql(sqlLast.toString().toString(), params.toArray());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			throw new MyException(-1, "未指定表名");
		}

	}

	@Override
	public void update(Object obj) throws MyException {
		// "UPDATE stu SET stu_no = ? where stu_id = ?";
				// 获取参数的字节码
				Class clazz = obj.getClass();

				// 是否自动增长的主键,如果是自动增长的主键则需要返回一个主键，默认为false
				boolean flagIsGenerator = false;

				// 主键保存位（属性）
				Field fieldKey = null;

				// 定义sql语句，逐渐完成拼接，语句前后都加空格，提高sql语句的容错率
				StringBuffer sqlFirst = new StringBuffer(" update ");
				StringBuffer sqlColumn = new StringBuffer(" set ");
				StringBuffer sqlLimit = new StringBuffer(" where ");
				Long keyTemplete = null;
				List<Object> params = new ArrayList<Object>();
				
				// 通过字节码对象获取其指定注解值
				// 首先获取用户自定的表名
				MyTable tableName = (MyTable) clazz.getAnnotation(MyTable.class);
				// System.out.println("==============:"+tableName);
				// 这里只能判断注解值是否为空，并不能判断你的数据库表是否存在，表不存在sql在执行时会抛出异常
				if (tableName != null) {
					String myTable = tableName.tableName();
					// 连接sql语句
					sqlFirst.append(myTable);
					// "UPDATE stu SET         stu_no = ? where stu_id = ?";
					// 获取参数的字节码
					// 获取所有声明属性
					Field[] declaredFields = clazz.getDeclaredFields();
					// 循环获取属性的注解
					for (Field f : declaredFields) {
						// 设置属性可访问
						f.setAccessible(true);
						// 逐个存放每个属性的注解
						Annotation[] annotations = f.getAnnotations();
						// 获取注解
						for (Annotation a : annotations) {
							
							Object value = null;
							try {
								value = f.get(obj);// 反射获得该属性在obj里对应的值
								System.out.println("获取属性" + f.getName() + "值：" + value);
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}

							// 判断是否是主键列
							if (a instanceof MyKey && value != null) {
								// 如果属性对应的是主键
								fieldKey = f;
								MyKey myKey = (MyKey) a;
								String keyName = myKey.columnName();
								System.out.println("keyName:" + keyName);
									sqlLimit.append(" "+keyName+" ").append(" = ").append(" ? ");
									// "UPDATE stu SET stu_no = ? where      stu_id = ?";
									 keyTemplete = (Long) value;
							}else if (a instanceof MyColumn && value != null) {
									MyColumn myColumn = (MyColumn) a;
									// System.out.println("myColumn："+myColumn);
									String columnName = myColumn.columnName();
									System.out.println("columnName:" + columnName);
									sqlColumn.append(" "+columnName+" ").append(" = ").append(" ? ").append(" , ");
							
									// 将对应的占位符值放入集合中（有序）
									params.add(value);

								}

							}
						}
					params.add(keyTemplete);
					sqlColumn.deleteCharAt(sqlColumn.lastIndexOf(","));
					// 最后完成拼接
					StringBuffer sqlLast = sqlFirst.append(sqlColumn).append(sqlLimit);
					System.out.println(sqlLast.toString());

					// 执行sql
					if (flagIsGenerator) {
						// 主键自动增长需要返回主键
						Long key = null;
						try {
							key = excuteSql(sqlLast.toString(), params.toArray(), true);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						// 给主键赋值（主键规 定好只有long和int）
						fieldKey.setAccessible(true);
						try {
							if (fieldKey.getType() == int.class || fieldKey.getType() == Integer.class) {
								fieldKey.set(obj, key.intValue());
							} else {
								fieldKey.set(obj, key);// 默认long类型
							}
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					} else {
						try {
							// 非自动增长不需要返回主键
							Long key = excuteSql(sqlLast.toString(), params.toArray());
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				} else {
					throw new MyException(-1, "未指定表名");
				}


	}

	@Override
	public void delete(Object obj) throws MyException {
	//	delete from  stu  where stu_id = ?
		// 获取参数的字节码
		Class clazz = obj.getClass();

		// 是否自动增长的主键,如果是自动增长的主键则需要返回一个主键，默认为false
		boolean flagIsGenerator = false;

		// 主键保存位（属性）
		Field fieldKey = null;

		// 定义sql语句，逐渐完成拼接，语句前后都加空格，提高sql语句的容错率
		StringBuffer sqlFirst = new StringBuffer(" delete from ");
		StringBuffer sqlLimit = new StringBuffer(" where ");
		Long keyTemplete = null;//临时主键存储为=位
		List<Object> params = new ArrayList<Object>();
		
		// 通过字节码对象获取其指定注解值
		// 首先获取用户自定的表名
		MyTable tableName = (MyTable) clazz.getAnnotation(MyTable.class);
		// System.out.println("==============:"+tableName);
		// 这里只能判断注解值是否为空，并不能判断你的数据库表是否存在，表不存在sql在执行时会抛出异常
		if (tableName != null) {
			String myTable = tableName.tableName();
			// 连接sql语句
			sqlFirst.append(myTable).append(" , ");
			// delete from  stu ,      where       stu_id = ?
			// 获取参数的字节码
			// 获取所有声明属性
			Field[] declaredFields = clazz.getDeclaredFields();
			// 循环获取属性的注解
			for (Field f : declaredFields) {
				// 设置属性可访问
				f.setAccessible(true);
				// 逐个存放每个属性的注解
				Annotation[] annotations = f.getAnnotations();
				// 获取注解
				for (Annotation a : annotations) {
					Object value = null;
					try {
						value = f.get(obj);// 反射获得该属性在obj里对应的值
						System.out.println("获取属性" + f.getName() + "值：" + value);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					// 判断是否是主键列
					if (a instanceof MyKey ) {
						if(value != null){
							// 如果属性对应的是主键
							fieldKey = f;
							MyKey myKey = (MyKey) a;
							String keyName = myKey.columnName();
							System.out.println("keyName:" + keyName);
							flagIsGenerator = myKey.isGenerator();// 是否是自动增长						
								// 如果不是自动增长，需要拼接
								sqlLimit.append(" "+keyName+" ").append(" = ").append(" ? ");
								//    where  stu_id = ?
								 keyTemplete = (Long) value;
						}else{
							throw new MyException(-1, "未找到主键...");
						}
						
						}
					}
				}
			params.add(keyTemplete);
			sqlFirst.deleteCharAt(sqlFirst.lastIndexOf(","));
			// 最后完成拼接
			StringBuffer sqlLast = sqlFirst.append(sqlLimit);
			System.out.println(sqlLast.toString());
			try {
					 Long result = excuteSql(sqlLast.toString(), params.toArray());
					 System.out.println(result);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			
		} else {
			throw new MyException(-1, "未指定表名");
		}
	}

	@Override
	public Object getObjectById(Object key, Object obj) throws MyException {
		Object o = null;//接收返回的新对象
		Class clazz = obj.getClass();
		// 查询操作
		// select * from stu where stu_id = key
		// 定义sql语句，逐渐完成拼接，语句前后都加空格，提高sql语句的容错率
		StringBuffer sqlFirst = new StringBuffer(" select * from ");
		StringBuffer sqlLimit = new StringBuffer(" where ");
		
		// 主键保存位（属性）
		Field fieldKey = null;
		// 通过字节码对象获取其指定注解值
		// 首先获取用户自定的表名
		MyTable tableName = (MyTable) clazz.getAnnotation(MyTable.class);
		//System.out.println("==============:" + tableName);
		// 这里只能判断注解值是否为空，并不能判断你的数据库表是否存在，表不存在sql在执行时会抛出异常
		if (tableName != null) {
			String myTable = tableName.tableName();
			// 连接sql语句
			sqlFirst.append(myTable);
			//
			Field[] declaredFields = clazz.getDeclaredFields();
			// 循环获取属性的注解
			for (Field f : declaredFields) {
				// 设置属性可访问
				f.setAccessible(true);
				// 逐个存放每个属性的注解
				Annotation[] annotations = f.getAnnotations();
				// 获取注解
				for (Annotation a : annotations) {
					Object value = null;
					try {
						value = f.get(obj);// 反射获得该属性在obj里对应的值
						System.out.println("获取属性" + f.getName() + "值：" + value);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					if (a instanceof MyKey && value != null) {
						// 如果属性对应的是主键
						fieldKey = f;
						MyKey myKey = (MyKey) a;
						String keyName = myKey.columnName();
						System.out.println("keyName:" + keyName);
						// select * from stu where stu_id = key
						sqlLimit.append(" " + keyName + " ").append(" = ").append(" " + key + " ");
						// where stu_id = ?
					} 
				}
			}
			//拼接
			StringBuffer sqlLast = sqlFirst.append(sqlLimit);
			System.out.println(sqlLast.toString());
			try {
				 o = excuteSql(sqlLast.toString(),obj);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			throw new MyException(-1, "未指定表名");
		}
		return o;
	}

	


}
