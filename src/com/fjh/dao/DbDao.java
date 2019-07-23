package com.fjh.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.fjh.exception.MyException;
/**
 * 
 * @ClassName: DbDao
 * @Description: TODO(���ݿ�ҵ��ӿ�)
 * @author ��Ѻ�
 * @date 2019��7��9��
 *
 */
public interface DbDao {
	/**
	 * @Description: insert delete update �Ĳ���sql��䷽��
	 * @param sql sql��䣬sql�еĲ�����ռλ������
	 * @param param sql�������Ҫ�Ĳ���
	 * @return -1��ʾִ�г���
	 * @eg:sql = "INSERT INTO TABLENAME(cloum1,cloum2,cloum3) VALUES(?,?,?);"
	 * 		param = new Object[] {cloum1_value, cloum2_value, cloum3_value};
	 */
	public Long excuteSql(String sql,Object [] param) throws SQLException;
	/**
	 * @Description:insert delete update �Ĳ���sql��䷽��
	 * @param sql sql��䣬sql�еĲ�����ռλ������
	 * @param param sql�������Ҫ�Ĳ���
	 * @param generateKey true or false �Ƿ񷵻�����
	 * @return -1��ʾִ�г���
	 * @eg:sql = "INSERT INTO TABLENAME(CLOUM1,CLOUM2,COLUM3) VALUES(?,?,?);"
	 * 	param = new Object[] {CLOUM1_value, CLOUM2_value, COLUM3_value};
	 */
	public Long excuteSql(String sql,Object [] param,boolean generateKey) throws SQLException;
	/**
	 * @Description:insert delete update �Ĳ�������sql��䷽��
	 * @param sqls  <Map<String,Object>:String ����Ϊ"sql" ��ʾ��Ҫִ�е�sql(ռλ��) ���� "param" ��ʾsql�������Ҫ�Ĳ���
	 * @return -1��ʾִ�г���
	 */
	public Long excuteSql(List<Map<String,Object>> sqls) throws SQLException;
	
	/**
	 * 
	 * @Title: excuteSql
	 * @Description: TODO(ִ��sql��ѯ����,�����ز�ѯ�Ķ���)
	 * @param  sql ִ�в�ѯ������sql
	 * @param  object ����Ķ���
	 * @throws SQLException ����
	 * @return Object ��������
	 */
	public Object excuteSql(String sql,Object object) throws SQLException;
	/**
	 * 
	 * @Title: insert
	 * @Description: TODO(���ݴ���Ķ���ִ��sql�Ĳ������)
	 * @param obj 
	 * @throws MyException ����
	 * @return void ��������
	 * @throws
	 */
	public void insert(Object obj)throws MyException ;
	
	/**
	 * 	
	 * @Title: update
	 * @Description: TODO(���ݴ���Ķ���ִ��sql�ĸ��²���)
	 * @param  obj �������
	 * @throws MyException ����
	 * @return void ��������
	 */
	public void update(Object obj)throws MyException;
	
	
	/**
	 * 
	 * @Title: delete
	 * @Description: TODO(���ݴ���Ķ���ִ��sql��ɾ������)
	 * @param obj
	 * @throws MyException ����
	 * @return void ��������
	 */
	public void delete (Object obj)throws MyException; 
	
	
	/**
	 * @Title: getObjectById
	 * @Description: TODO(ͨ����������long��int����ȡһ�����ݿ��¼)
	 * @param  key ����
	 * @param  obj ����Ķ���
	 * @return Object ����һ�������ݿ��¼���ݶ�Ӧ�Ķ���
	 * @throws MyException 
	 */
	public Object getObjectById(Object key,Object obj)throws MyException;
	
}
