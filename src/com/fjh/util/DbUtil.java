package com.fjh.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * 
 * @ClassName: DbUtil
 * @Description: TODO(数据库连接工具类)
 * @author 冯佳豪
 * @date 2019年7月9日
 *
 */
public class DbUtil {
	// url 用户名 密码 驱动 注册
	static String url = "jdbc:mysql://127.0.0.1:3306/student";
	static String user = "root";
	static String password = "123456";
	static String driver = "com.mysql.jdbc.Driver";
	static final ThreadLocal<Connection> connect = new ThreadLocal<Connection>();
	//注册驱动
	static{
		try {
			//JVM查找并加载指定的类，如果在类中有静态初始化器的话，JVM必然会执行该类的静态代码段
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @Title: getConnection
	 * @Description: TODO(数据库的连接操作)
	 * @return Connection 返回类型
	 */
	public static Connection getConnection(){
		try {
			Connection connection = connect.get();
			if(connection == null){
				connection = DriverManager.getConnection(url, user, password);
				//将connection注入当前ThreadLocal中,从而保证一个线程只有一个connection对象
				connect.set(connection);
			}
			return connection;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 
	 * @Title: closeConnect
	 * @Description: TODO(关闭connection对象)
	 * @return void 返回类型
	 */
	public static void closeConnect(){
		Connection connection = connect.get();
		if(connection != null){
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		connect.remove();
	}
	
	/**
	 * @Title: close
	 * @Description: TODO(关闭数据库连接)
	 * @param  connection 连接对象
	 * @param  statement 预编译的SQL语句的对象
	 * @param  resultSet 结果集
	 * @return void 返回类型
	 */
	public static void close(Connection connection,Statement statement,ResultSet resultSet){
		try {
			if(null != connection) connection.close();
			if(null != statement) statement.close();
			if(null != resultSet) resultSet.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void close(Connection connection){
		close(connection,null,null);
	}
	public static void close(Connection connection,Statement statement){
		close(connection,statement,null);
	}
}
