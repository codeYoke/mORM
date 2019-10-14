# mORM
一个简略版Object Relational Mapping(对象关系映射)框架，让crud变得如此简单：）:smiley:



### 功能描述

功能描述：

* 支持Update语句
* 支持Insert语句
* 支持Delete语句
* 支持查询分页
* 支持自定义sql语句(crud,排序,分页)
* 支持事务
* 支持单表查询 
* 支持直接返回Json格式查询结果
* 自定义异常

### 环境要求
jdk 1.7+

### 部分功能接口展示

```java
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
	 * @param sqls  <Map<String,Object>:String 必须为"sql" object表示需要执行的sql(占位符) 或者 "param" 表示sql语句中需要的参数
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
	 * @param sql 执行的sql语句
	 * @param params sql中的参数
	 * @return List对象集合
	 */
	public List<Map<String, Object>> getDatas(String sql,Object ... params) throws SQLException ;
	
	/**
	 * 
	 * @Title: getJsonDatas
	 * @Description: sql查询结果获取json对象
	 *  @param sql
	 *  @param params
	 *  @return String
	 *  @throws SQLException
	 */
	public String getJsonDatas(String sql,Object ... params) throws SQLException ;
	
	/**
	 * 
	 * @param <T>
	 * @param clazz
	 * @param sql
	 * @param params
	 * @return 对象的集合
	 * @throws SQLException
	 */
	public <T>  List<T> getDatas(Class<T> clazz,String sql,Object ... params) throws SQLException ;
	
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

```
### example

测试之前，记得修改成自己的数据库配置

注：MyException为自定义异常类

**测试 insert**

```java
		//注解测试 insert
		DbDao db = new DbDaoImpl();
		Student stu = new Student();
		stu.setStuName("王大大");
		stu.setStuNo("20190757");
		stu.setStuBirth(new Date());
		System.out.println(stu);
		try {
			db.insert(stu);
		} catch (MyException e) {
			int code = e.getCode();
			//根据code的值不同做不同处理
			System.out.println(e.getMessage()   + " " + code);
		}
		
		System.out.println(stu);
```
**测试 update**

```java
	//注解测试 update
		DbDao db = new DbDaoImpl();
		Student stu = new Student();
		stu.setStuNo("20190760");
		stu.setStuId(1001l);
		stu.setStuAge(63);
		
		System.out.println(stu);
		try {
			db.update(stu);
		} catch (MyException e) {
			int code = e.getCode();
			//根据code的值不同做不同处理
			System.out.println(e.getMessage()   + " " + code);
		}
		
		System.out.println(stu);
```
**测试getObjectById**

```java
		//注解测试getObjectById
		DbDao db = new DbDaoImpl();
		Student stu = new Student();
		stu.setStuId(1000l);
		try {
			db.getObjectById(stu.getStuId(),stu);
		} catch (MyException e) {
			int code = e.getCode();
			//根据code的值不同做不同处理
			System.out.println(e.getMessage()   + " " + code);
		}
		
		System.out.println(stu);
```

等等。。。



