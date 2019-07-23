package com.fjh.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.fjh.exception.MyException;
/**
 * 
 * @ClassName: DbDao
 * @Description: TODO(数据库业务接口)
 * @author 冯佳豪
 * @date 2019年7月9日
 *
 */
public interface DbDao {
	/**
	 * @Description: insert delete update 的操作sql语句方法
	 * @param sql sql语句，sql中的参数用占位符代替
	 * @param param sql语句中需要的参数
	 * @return -1表示执行出错
	 * @eg:sql = "INSERT INTO TABLENAME(cloum1,cloum2,cloum3) VALUES(?,?,?);"
	 * 		param = new Object[] {cloum1_value, cloum2_value, cloum3_value};
	 */
	public Long excuteSql(String sql,Object [] param) throws SQLException;
	/**
	 * @Description:insert delete update 的操作sql语句方法
	 * @param sql sql语句，sql中的参数用占位符代替
	 * @param param sql语句中需要的参数
	 * @param generateKey true or false 是否返回主键
	 * @return -1表示执行出错
	 * @eg:sql = "INSERT INTO TABLENAME(CLOUM1,CLOUM2,COLUM3) VALUES(?,?,?);"
	 * 	param = new Object[] {CLOUM1_value, CLOUM2_value, COLUM3_value};
	 */
	public Long excuteSql(String sql,Object [] param,boolean generateKey) throws SQLException;
	/**
	 * @Description:insert delete update 的操作多条sql语句方法
	 * @param sqls  <Map<String,Object>:String 必须为"sql" 表示需要执行的sql(占位符) 或者 "param" 表示sql语句中需要的参数
	 * @return -1表示执行出错
	 */
	public Long excuteSql(List<Map<String,Object>> sqls) throws SQLException;
	
	/**
	 * 
	 * @Title: excuteSql
	 * @Description: TODO(执行sql查询操作,并返回查询的对象)
	 * @param  sql 执行查询操作的sql
	 * @param  object 传入的对象
	 * @throws SQLException 参数
	 * @return Object 返回类型
	 */
	public Object excuteSql(String sql,Object object) throws SQLException;
	/**
	 * 
	 * @Title: insert
	 * @Description: TODO(根据传入的对象执行sql的插入操作)
	 * @param obj 
	 * @throws MyException 参数
	 * @return void 返回类型
	 * @throws
	 */
	public void insert(Object obj)throws MyException ;
	
	/**
	 * 	
	 * @Title: update
	 * @Description: TODO(根据传入的对象执行sql的更新操作)
	 * @param  obj 传入对象
	 * @throws MyException 参数
	 * @return void 返回类型
	 */
	public void update(Object obj)throws MyException;
	
	
	/**
	 * 
	 * @Title: delete
	 * @Description: TODO(根据传入的对象执行sql的删除操作)
	 * @param obj
	 * @throws MyException 参数
	 * @return void 返回类型
	 */
	public void delete (Object obj)throws MyException; 
	
	
	/**
	 * @Title: getObjectById
	 * @Description: TODO(通过主键（限long和int）获取一条数据库记录)
	 * @param  key 主键
	 * @param  obj 传入的对象
	 * @return Object 返回一个与数据库记录数据对应的对象
	 * @throws MyException 
	 */
	public Object getObjectById(Object key,Object obj)throws MyException;
	
}
