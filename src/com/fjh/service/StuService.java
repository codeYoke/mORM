package com.fjh.service;

import java.sql.SQLException;

import com.fjh.pojo.Student;

/**
 * 
 * @ClassName: StuService
 * @Description: TODO(���ѧ��������ݿ�ҵ������ӿ�)
 * @author ��Ѻ�
 * @date 2019��7��10��
 *
 */
public interface StuService {
	public void add(Student stu) throws SQLException;
	public void update(Student stu) throws SQLException ;
}
