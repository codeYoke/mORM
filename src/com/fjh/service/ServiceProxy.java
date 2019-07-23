package com.fjh.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

import com.fjh.util.DbUtil;
/**
 * 
 * @ClassName: ServiceProxy
 * @Description: TODO(ҵ���������������ݿ�����)
 * @author ��Ѻ�
 * @param <T>
 * @date 2019��7��10��
 *
 */
public class ServiceProxy<T> implements InvocationHandler {
	//Ŀ�����
	T target;
	/**
	 * 
	 * @Title: bind
	 * @Description: TODO(��Ŀ�����)
	 * @param  target
	 * @return T ���ش������
	 * @throws
	 */
	public T bind(Class<T> target){
		try {
			this.target = target.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (T) Proxy.newProxyInstance(target.getClassLoader(), target.getInterfaces(), this);
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		//��ȡ���ݿ�����Ӷ���
		Connection connection = DbUtil.getConnection();
		Object result = null;
		//��������
		connection.setAutoCommit(false);
		try {
			result = method.invoke(target, args);
		} catch (Exception e) {
			connection.rollback();
		}
		//�ύ����
		connection.commit();
		
		return result;
	}

}
