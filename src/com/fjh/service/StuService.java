package com.fjh.service;

import java.sql.SQLException;

import com.fjh.pojo.Student;

/**
 * 
 * @ClassName: StuService
 * @Description: TODO(针对学生表的数据库业务操作接口)
 * @author 冯佳豪
 * @date 2019年7月10日
 *
 */
public interface StuService {
	public void add(Student stu) throws SQLException;
	public void update(Student stu) throws SQLException ;
}
