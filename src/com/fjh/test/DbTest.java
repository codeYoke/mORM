package com.fjh.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fjh.dao.DbDao;
import com.fjh.dao.impl.DbDaoImpl;
import com.fjh.exception.MyException;
import com.fjh.pojo.Student;
import com.fjh.service.ServiceProxy;
import com.fjh.service.StuService;
import com.fjh.service.impl.StuServiceImpl;

public class DbTest {

	public static void main(String[] args) throws SQLException {
		// 测试
//		// "INSERT into stu(stu_name,stu_no,stu_age,stu_birth) values(?,?,?,?)";
//		StuService stuService = new StuServiceImpl();
//		
//		
//		Student stu = new Student("成龙4","20190754",60,new Date());
//		System.out.println("需要修改的学生信息为："+stu.toString());
//		stu.setStuNo("20190755");
//		stuService.add(stu);
//		stuService.update(stu);
//		System.out.println("修改后学生信息为："+stu.toString());
//		
		
//		//代理类测试
//		ServiceProxy<StuServiceImpl> serviceProxy = new ServiceProxy<StuServiceImpl>();
//		StuService stuService = serviceProxy.bind(StuServiceImpl.class);
//		
//		Student stu = new Student("成龙7","20190757",60,new Date());
//		System.out.println("需要修改的学生信息为："+stu.toString());
//		stuService.add(stu);
//		System.out.println("修改后学生信息为："+stu.toString());
		
		
		//注解测试 insert
//		DbDao db = new DbDaoImpl();
//		Student stu = new Student();
//		stu.setStuName("王思聪");
//		stu.setStuNo("20190757");
//		stu.setStuBirth(new Date());
//		System.out.println(stu);
//		try {
//			db.insert(stu);
//		} catch (MyException e) {
//			int code = e.getCode();
//			//根据code的值不同做不同处理
//			System.out.println(e.getMessage()   + " " + code);
//		}
//		
//		System.out.println(stu);

//		
//		//注解测试 update
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
		
//		
//		
//		//注解测试 delete
//		DbDao db = new DbDaoImpl();
//		Student stu = new Student();
//		stu.setStuId(1036l);
//		stu.setStuAge(61);
//		System.out.println(stu);
//		try {
//			db.delete(stu);
//		} catch (MyException e) {
//			int code = e.getCode();
//			//根据code的值不同做不同处理
//			System.out.println(e.getMessage()   + " " + code);
//		}
//		
//		System.out.println(stu);
//		
//		
/*//
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
				*/
	}

}