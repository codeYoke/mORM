package com.fjh.service.impl;

import java.sql.SQLException;

import com.fjh.dao.DbDao;
import com.fjh.dao.impl.DbDaoImpl;
import com.fjh.pojo.Student;
import com.fjh.service.StuService;
/**
 * 
 * @ClassName: StuServiceImpl
 * @Description: TODO(针对学生表的数据库业务操作类)
 * @author 冯佳豪
 * @date 2019年7月10日
 *
 */
public class StuServiceImpl implements StuService {
	/*
	 * String sql = "INSERT into stu(stu_name,stu_no,stu_age) values(?,?,?)";
		Object[] params = new Object[] { "邓超", "20190747", 25};
	 */
	DbDao db = new DbDaoImpl();
	@Override
	public void add(Student stu) throws SQLException {
		String sql = "INSERT into stu(stu_name,stu_no,stu_age,stu_birth) values(?,?,?,?)";
		Object[] params = new Object[] { stu.getStuName(), stu.getStuNo(), stu.getStuAge(),stu.getStuBirth()};
		Long key = null;
		key = db.excuteSql(sql, params, true);
		stu.setStuId(key);
		System.out.println("添加的对象信息："+stu.toString());
		update(stu);
	}

	@Override
	public void update(Student stu) throws SQLException {
		stu.setStuNo("20190755");
		String sql = "UPDATE stu SET stu_no = ? where stu_id = ?";
			Object[] params = new Object[] { stu.getStuNo(),stu.getStuId()};
			db.excuteSql(sql, params);
	}

}
