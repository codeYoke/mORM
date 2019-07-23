package com.fjh.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * 
 * @ClassName: DbUtil
 * @Description: TODO(���ݿ����ӹ�����)
 * @author ��Ѻ�
 * @date 2019��7��9��
 *
 */
public class DbUtil {
	// url �û��� ���� ���� ע��
	static String url = "jdbc:mysql://127.0.0.1:3306/student";
	static String user = "root";
	static String password = "123456";
	static String driver = "com.mysql.jdbc.Driver";
	static final ThreadLocal<Connection> connect = new ThreadLocal<Connection>();
	//ע������
	static{
		try {
			//JVM���Ҳ�����ָ�����࣬����������о�̬��ʼ�����Ļ���JVM��Ȼ��ִ�и���ľ�̬�����
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @Title: getConnection
	 * @Description: TODO(���ݿ�����Ӳ���)
	 * @return Connection ��������
	 */
	public static Connection getConnection(){
		try {
			Connection connection = connect.get();
			if(connection == null){
				connection = DriverManager.getConnection(url, user, password);
				//��connectionע�뵱ǰThreadLocal��,�Ӷ���֤һ���߳�ֻ��һ��connection����
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
	 * @Description: TODO(�ر�connection����)
	 * @return void ��������
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
	 * @Description: TODO(�ر����ݿ�����)
	 * @param  connection ���Ӷ���
	 * @param  statement Ԥ�����SQL���Ķ���
	 * @param  resultSet �����
	 * @return void ��������
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
