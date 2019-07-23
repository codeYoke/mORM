package com.fjh.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

import com.fjh.util.DbUtil;
/**
 * 
 * @ClassName: ServiceProxy
 * @Description: TODO(业务代理类对象处理数据库事务)
 * @author 冯佳豪
 * @param <T>
 * @date 2019年7月10日
 *
 */
public class ServiceProxy<T> implements InvocationHandler {
	//目标对象
	T target;
	/**
	 * 
	 * @Title: bind
	 * @Description: TODO(绑定目标对象)
	 * @param  target
	 * @return T 返回代理对象
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
		//获取数据库的连接对象
		Connection connection = DbUtil.getConnection();
		Object result = null;
		//开启事务
		connection.setAutoCommit(false);
		try {
			result = method.invoke(target, args);
		} catch (Exception e) {
			connection.rollback();
		}
		//提交事务
		connection.commit();
		
		return result;
	}

}
