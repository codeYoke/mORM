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
 * @Description: TODO(���ݿ����ҵ��ʵ����)
 * @author ��Ѻ�
 * @date 2019��7��9��
 *
 */
public class DbDaoImpl implements DbDao {
	
	@Override
	public Object excuteSql(String sql,Object obj) throws SQLException {
		// ��ѯѧ������ѯ�������漰����ع�
		Connection connection = DbUtil.getConnection();
		PreparedStatement statement = null;
		ResultSet rs = null;
		Class clazz = obj.getClass();
		// ͨ���ֽ�������ȡ��ָ��ע��ֵ
		// ��ȡ������������
		Field[] declaredFields = clazz.getDeclaredFields();
		try {
			statement = connection.prepareStatement(sql);// Ԥ����sql���
			rs = statement.executeQuery();// ִ��sql���
			// ��ȡԪ����
			ResultSetMetaData metaData = rs.getMetaData();
			// ��ȡԪ���ݶ�����
			int columnCount = metaData.getColumnCount();
			// ��ȡ��ѯ��ֵ
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
		// Ĭ�ϲ���ֵ
		Long result = -1l;
		String sql = "";
		Object[] param = new Object[] {};
		for (Map<String, Object> map : sqls) {
			sql = (String) map.get("sql");
			param = (Object[]) map.get("param");
			result = excuteSql(sql, param, false);
		}
		// ע�⣺ÿ��map�ﶼ��һ��sql�Ͷ�Ӧ�Ĳ��� ִ�з���ѭ����ֻ��ִ�����һ��
		// Long result = excuteSql(sql, param, false, true);
		// ��ȡ��ǰ�߳�connection���󣬲��ύ�������ر�����
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
		//����ֵ
				List<Map<String, Object>> datas = new ArrayList<Map<String,Object>>();
				Connection conn = null;
				PreparedStatement stmt = null;
				ResultSet rs  = null;
				try {
					conn = DbUtil.getConnection();
					stmt = conn.prepareStatement(sql);
					//��sql�еĲ�����ֵ
					for(int i = 0 ; i < params.length; i ++) {
						stmt.setObject(i+1, params[i]);
					}
					//ִ��sql���
					rs = stmt.executeQuery();
					//��ȡ����  �ò�ѯ���еı�����Ϊkey  �ж�Ӧֵ��Ϊvalue
					//��ȡԪ����
					ResultSetMetaData metaData = rs.getMetaData();
					int columnCount = metaData.getColumnCount();//�ܹ���ѯ������
					while(rs.next()) {
						//whileÿѭ��һ�Σ����Ի�ȡһ������  ����һ���������кܶ��� select stu_id id ,stu_name from t_stu;
						//ÿһ�����ݷ�װ�¸�һ��map
						Map<String, Object> data = new HashMap<String, Object>();
						for(int i = 1; i <= columnCount; i ++) {
							String columnLabel = metaData.getColumnLabel(i);//��ѯ�еı��������û�б�������ô��������һ��
							Object value = rs.getObject(columnLabel);
							data.put(columnLabel, value);
						}
						//��һ�����ݷ��뵽list��
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
			//��sql�еĲ�����ֵ
			for(int i = 0 ; i < params.length; i ++) {
				stmt.setObject(i+1, params[i]);
			}
			//ִ��sql���
			rs = stmt.executeQuery();
			//��ȡ����  �ò�ѯ���еı�����Ϊkey  �ж�Ӧֵ��Ϊvalue
			//��ȡԪ����
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();//�ܹ���ѯ������
			while(rs.next()) {
				//whileÿѭ��һ�Σ����Ի�ȡһ������  ����һ���������кܶ��� select stu_id id ,stu_name from t_stu;
				//ÿһ�����ݷ�װһ������  Class<T> clazz
				T obj = clazz.newInstance();
				for(int i = 1; i <= columnCount; i ++) {
					String columnLabel = metaData.getColumnLabel(i);//��ѯ�еı��������û�б�������ô��������һ��
					Object value = rs.getObject(columnLabel);
					//�������������ֽ������Ƿ��и����ԣ�����и�����ֱ�Ӹ����Ը�ֵ
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
				//��һ�����ݷ��뵽list��
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
		// ��ȡ�������ֽ���
		Class clazz = obj.getClass();

		// �Ƿ��Զ�����������,������Զ���������������Ҫ����һ��������Ĭ��Ϊfalse
		boolean flagIsGenerator = false;

		// ��������λ�����ԣ�
		Field fieldKey = null;

		// ����sql��䣬�����ƴ�ӣ����ǰ�󶼼ӿո����sql�����ݴ���
		StringBuffer sqlFirst = new StringBuffer(" insert into ");
		StringBuffer sqlColumns = new StringBuffer(" ");
		StringBuffer sqlValues = new StringBuffer(" values( ");
		List<Object> params = new ArrayList<Object>();
		
		// ͨ���ֽ�������ȡ��ָ��ע��ֵ
		// ���Ȼ�ȡ�û��Զ��ı���
		MyTable tableName = (MyTable) clazz.getAnnotation(MyTable.class);
		// System.out.println("==============:"+tableName);
		// ����ֻ���ж�ע��ֵ�Ƿ�Ϊ�գ��������ж�������ݿ���Ƿ���ڣ�������sql��ִ��ʱ���׳��쳣
		if (tableName != null) {
			String myTable = tableName.tableName();
			// ����sql���
			sqlFirst.append(myTable).append("(");

			// ��ȡ������������
			Field[] declaredFields = clazz.getDeclaredFields();
			// ѭ����ȡ���Ե�ע��
			for (Field f : declaredFields) {
				// �������Կɷ���
				f.setAccessible(true);
				// ������ÿ�����Ե�ע��
				Annotation[] annotations = f.getAnnotations();
				// ��ȡע��
				for (Annotation a : annotations) {
					Object value = null;
					try {
						value = f.get(obj);// �����ø�������obj���Ӧ��ֵ
						System.out.println("��ȡ����" + f.getName() + "ֵ��" + value);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}

					// �ж��Ƿ���������
					if (a instanceof MyKey && value != null) {
						// ������Զ�Ӧ��������
						fieldKey = f;
						MyKey myKey = (MyKey) a;
						String keyName = myKey.columnName();
						System.out.println("keyName:" + keyName);
						flagIsGenerator = myKey.isGenerator();// �Ƿ����Զ�����
						if (!flagIsGenerator) {
							// ��������Զ���������Ҫƴ��
							sqlColumns.append(keyName).append(" , ");
							sqlValues.append(" ? ").append(" , ");
							// ����Ӧ��ռλ��ֵ���뼯���У�����
							params.add(value);
						} 
					}else if (a instanceof MyColumn && value != null) {
							MyColumn myColumn = (MyColumn) a;
							// System.out.println("myColumn��"+myColumn);
							String columnName = myColumn.columnName();
							System.out.println("columnName:" + columnName);
							sqlColumns.append(columnName).append(" , ");
							// ͬʱ�����Ӧռλ��
							sqlValues.append(" ? ").append(" , ");
							// ����Ӧ��ռλ��ֵ���뼯���У�����
							params.add(value);
							System.out.println(sqlFirst + " " + sqlColumns + " " + sqlValues + " " + params);

						}

				}

			}
			
			//������������
			sqlColumns.append(" ) ");
			sqlValues.append(" ) ");
			// ɾ��������Ķ���
			sqlColumns.deleteCharAt(sqlColumns.lastIndexOf(","));
			sqlValues.deleteCharAt(sqlValues.lastIndexOf(","));
			// ������ƴ��
			StringBuffer sqlLast = sqlFirst.append(sqlColumns).append(sqlValues);
			System.out.println(sqlLast.toString());

			// ִ��sql
			if (flagIsGenerator) {
				// �����Զ�������Ҫ��������
				Long key = null;
				try {
					key = excuteSql(sqlLast.toString(), params.toArray(), true);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				// ��������ֵ�������� ����ֻ��long��int��
				fieldKey.setAccessible(true);
				try {
					if (fieldKey.getType() == int.class || fieldKey.getType() == Integer.class) {
						fieldKey.set(obj, key.intValue());
					} else {
						fieldKey.set(obj, key);// Ĭ��long����
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			} else {
				try {
					// ���Զ���������Ҫ��������
					Long key = excuteSql(sqlLast.toString().toString(), params.toArray());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			throw new MyException(-1, "δָ������");
		}

	}

	@Override
	public void update(Object obj) throws MyException {
		// "UPDATE stu SET stu_no = ? where stu_id = ?";
				// ��ȡ�������ֽ���
				Class clazz = obj.getClass();

				// �Ƿ��Զ�����������,������Զ���������������Ҫ����һ��������Ĭ��Ϊfalse
				boolean flagIsGenerator = false;

				// ��������λ�����ԣ�
				Field fieldKey = null;

				// ����sql��䣬�����ƴ�ӣ����ǰ�󶼼ӿո����sql�����ݴ���
				StringBuffer sqlFirst = new StringBuffer(" update ");
				StringBuffer sqlColumn = new StringBuffer(" set ");
				StringBuffer sqlLimit = new StringBuffer(" where ");
				Long keyTemplete = null;
				List<Object> params = new ArrayList<Object>();
				
				// ͨ���ֽ�������ȡ��ָ��ע��ֵ
				// ���Ȼ�ȡ�û��Զ��ı���
				MyTable tableName = (MyTable) clazz.getAnnotation(MyTable.class);
				// System.out.println("==============:"+tableName);
				// ����ֻ���ж�ע��ֵ�Ƿ�Ϊ�գ��������ж�������ݿ���Ƿ���ڣ�������sql��ִ��ʱ���׳��쳣
				if (tableName != null) {
					String myTable = tableName.tableName();
					// ����sql���
					sqlFirst.append(myTable);
					// "UPDATE stu SET         stu_no = ? where stu_id = ?";
					// ��ȡ�������ֽ���
					// ��ȡ������������
					Field[] declaredFields = clazz.getDeclaredFields();
					// ѭ����ȡ���Ե�ע��
					for (Field f : declaredFields) {
						// �������Կɷ���
						f.setAccessible(true);
						// ������ÿ�����Ե�ע��
						Annotation[] annotations = f.getAnnotations();
						// ��ȡע��
						for (Annotation a : annotations) {
							
							Object value = null;
							try {
								value = f.get(obj);// �����ø�������obj���Ӧ��ֵ
								System.out.println("��ȡ����" + f.getName() + "ֵ��" + value);
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}

							// �ж��Ƿ���������
							if (a instanceof MyKey && value != null) {
								// ������Զ�Ӧ��������
								fieldKey = f;
								MyKey myKey = (MyKey) a;
								String keyName = myKey.columnName();
								System.out.println("keyName:" + keyName);
									sqlLimit.append(" "+keyName+" ").append(" = ").append(" ? ");
									// "UPDATE stu SET stu_no = ? where      stu_id = ?";
									 keyTemplete = (Long) value;
							}else if (a instanceof MyColumn && value != null) {
									MyColumn myColumn = (MyColumn) a;
									// System.out.println("myColumn��"+myColumn);
									String columnName = myColumn.columnName();
									System.out.println("columnName:" + columnName);
									sqlColumn.append(" "+columnName+" ").append(" = ").append(" ? ").append(" , ");
							
									// ����Ӧ��ռλ��ֵ���뼯���У�����
									params.add(value);

								}

							}
						}
					params.add(keyTemplete);
					sqlColumn.deleteCharAt(sqlColumn.lastIndexOf(","));
					// ������ƴ��
					StringBuffer sqlLast = sqlFirst.append(sqlColumn).append(sqlLimit);
					System.out.println(sqlLast.toString());

					// ִ��sql
					if (flagIsGenerator) {
						// �����Զ�������Ҫ��������
						Long key = null;
						try {
							key = excuteSql(sqlLast.toString(), params.toArray(), true);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						// ��������ֵ�������� ����ֻ��long��int��
						fieldKey.setAccessible(true);
						try {
							if (fieldKey.getType() == int.class || fieldKey.getType() == Integer.class) {
								fieldKey.set(obj, key.intValue());
							} else {
								fieldKey.set(obj, key);// Ĭ��long����
							}
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					} else {
						try {
							// ���Զ���������Ҫ��������
							Long key = excuteSql(sqlLast.toString(), params.toArray());
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				} else {
					throw new MyException(-1, "δָ������");
				}


	}

	@Override
	public void delete(Object obj) throws MyException {
	//	delete from  stu  where stu_id = ?
		// ��ȡ�������ֽ���
		Class clazz = obj.getClass();

		// �Ƿ��Զ�����������,������Զ���������������Ҫ����һ��������Ĭ��Ϊfalse
		boolean flagIsGenerator = false;

		// ��������λ�����ԣ�
		Field fieldKey = null;

		// ����sql��䣬�����ƴ�ӣ����ǰ�󶼼ӿո����sql�����ݴ���
		StringBuffer sqlFirst = new StringBuffer(" delete from ");
		StringBuffer sqlLimit = new StringBuffer(" where ");
		Long keyTemplete = null;//��ʱ�����洢Ϊ=λ
		List<Object> params = new ArrayList<Object>();
		
		// ͨ���ֽ�������ȡ��ָ��ע��ֵ
		// ���Ȼ�ȡ�û��Զ��ı���
		MyTable tableName = (MyTable) clazz.getAnnotation(MyTable.class);
		// System.out.println("==============:"+tableName);
		// ����ֻ���ж�ע��ֵ�Ƿ�Ϊ�գ��������ж�������ݿ���Ƿ���ڣ�������sql��ִ��ʱ���׳��쳣
		if (tableName != null) {
			String myTable = tableName.tableName();
			// ����sql���
			sqlFirst.append(myTable).append(" , ");
			// delete from  stu ,      where       stu_id = ?
			// ��ȡ�������ֽ���
			// ��ȡ������������
			Field[] declaredFields = clazz.getDeclaredFields();
			// ѭ����ȡ���Ե�ע��
			for (Field f : declaredFields) {
				// �������Կɷ���
				f.setAccessible(true);
				// ������ÿ�����Ե�ע��
				Annotation[] annotations = f.getAnnotations();
				// ��ȡע��
				for (Annotation a : annotations) {
					Object value = null;
					try {
						value = f.get(obj);// �����ø�������obj���Ӧ��ֵ
						System.out.println("��ȡ����" + f.getName() + "ֵ��" + value);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					// �ж��Ƿ���������
					if (a instanceof MyKey ) {
						if(value != null){
							// ������Զ�Ӧ��������
							fieldKey = f;
							MyKey myKey = (MyKey) a;
							String keyName = myKey.columnName();
							System.out.println("keyName:" + keyName);
							flagIsGenerator = myKey.isGenerator();// �Ƿ����Զ�����						
								// ��������Զ���������Ҫƴ��
								sqlLimit.append(" "+keyName+" ").append(" = ").append(" ? ");
								//    where  stu_id = ?
								 keyTemplete = (Long) value;
						}else{
							throw new MyException(-1, "δ�ҵ�����...");
						}
						
						}
					}
				}
			params.add(keyTemplete);
			sqlFirst.deleteCharAt(sqlFirst.lastIndexOf(","));
			// ������ƴ��
			StringBuffer sqlLast = sqlFirst.append(sqlLimit);
			System.out.println(sqlLast.toString());
			try {
					 Long result = excuteSql(sqlLast.toString(), params.toArray());
					 System.out.println(result);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			
		} else {
			throw new MyException(-1, "δָ������");
		}
	}

	@Override
	public Object getObjectById(Object key, Object obj) throws MyException {
		Object o = null;//���շ��ص��¶���
		Class clazz = obj.getClass();
		// ��ѯ����
		// select * from stu where stu_id = key
		// ����sql��䣬�����ƴ�ӣ����ǰ�󶼼ӿո����sql�����ݴ���
		StringBuffer sqlFirst = new StringBuffer(" select * from ");
		StringBuffer sqlLimit = new StringBuffer(" where ");
		
		// ��������λ�����ԣ�
		Field fieldKey = null;
		// ͨ���ֽ�������ȡ��ָ��ע��ֵ
		// ���Ȼ�ȡ�û��Զ��ı���
		MyTable tableName = (MyTable) clazz.getAnnotation(MyTable.class);
		//System.out.println("==============:" + tableName);
		// ����ֻ���ж�ע��ֵ�Ƿ�Ϊ�գ��������ж�������ݿ���Ƿ���ڣ�������sql��ִ��ʱ���׳��쳣
		if (tableName != null) {
			String myTable = tableName.tableName();
			// ����sql���
			sqlFirst.append(myTable);
			//
			Field[] declaredFields = clazz.getDeclaredFields();
			// ѭ����ȡ���Ե�ע��
			for (Field f : declaredFields) {
				// �������Կɷ���
				f.setAccessible(true);
				// ������ÿ�����Ե�ע��
				Annotation[] annotations = f.getAnnotations();
				// ��ȡע��
				for (Annotation a : annotations) {
					Object value = null;
					try {
						value = f.get(obj);// �����ø�������obj���Ӧ��ֵ
						System.out.println("��ȡ����" + f.getName() + "ֵ��" + value);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					if (a instanceof MyKey && value != null) {
						// ������Զ�Ӧ��������
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
			//ƴ��
			StringBuffer sqlLast = sqlFirst.append(sqlLimit);
			System.out.println(sqlLast.toString());
			try {
				 o = excuteSql(sqlLast.toString(),obj);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			throw new MyException(-1, "δָ������");
		}
		return o;
	}

	


}
