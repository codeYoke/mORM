package com.fjh.service.impl;

import java.sql.SQLException;

import com.fjh.dao.DbDao;
import com.fjh.dao.impl.DbDaoImpl;
import com.fjh.pojo.Student;
import com.fjh.service.StuService;
/**
 * 
 * @ClassName: StuServiceImpl
 * @Description: TODO(���ѧ��������ݿ�ҵ�������)
 * @author ��Ѻ�
 * @date 2019��7��10��
 *
 */
public class StuServiceImpl implements StuService {
	/*
	 * String sql = "INSERT into stu(stu_name,stu_no,stu_age) values(?,?,?)";
		Object[] params = new Object[] { "�˳�", "20190747", 25};
	 */
	DbDao db = new DbDaoImpl();
	@Override
	public void add(Student stu) throws SQLException {
		String sql = "INSERT into stu(stu_name,stu_no,stu_age,stu_birth) values(?,?,?,?)";
		Object[] params = new Object[] { stu.getStuName(), stu.getStuNo(), stu.getStuAge(),stu.getStuBirth()};
		Long key = null;
		key = db.excuteSql(sql, params, true);
		stu.setStuId(key);
		System.out.println("��ӵĶ�����Ϣ��"+stu.toString());
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
