package com.fjh.pojo;
import java.util.Date;

import com.fjh.annotation.MyColumn;
import com.fjh.annotation.MyKey;
import com.fjh.annotation.MyTable;
/**
 * 
 * @ClassName: Student
 * @Description: TODO(学生实体类，属性命名顺序必须与数据库一致)
 * @author 冯佳豪
 * @date 2019年7月10日
 *
 */
@MyTable (tableName="stu")//对应数据库中的表
public class Student {
	//SELECT stu_name stuName,stu_no stuNo,stu_age stuAge,stu_birth stuBirth FROM stu
	@MyKey(columnName = "stu_id",isGenerator = true)
	private Long stuId;
	@MyColumn(columnName ="stu_name" )
	private String stuName;
	@MyColumn(columnName = "stu_no" )
	private String stuNo;
	@MyColumn(columnName = "stu_age" )
	private Integer stuAge;
	@MyColumn(columnName = "stu_birth" )
	private Date stuBirth;
	public String getStuName() {
		return stuName;
	}
	
	public Long getStuId() {
		return stuId;
	}
	public void setStuId(Long stuId) {
		this.stuId = stuId;
	}
	public void setStuName(String stuName) {
		this.stuName = stuName;
	}
	public String getStuNo() {
		return stuNo;
	}
	public void setStuNo(String stuNo) {
		this.stuNo = stuNo;
	}
	public Integer getStuAge() {
		return stuAge;
	}
	public void setStuAge(Integer stuAge) {
		this.stuAge = stuAge;
	}
	public Date getStuBirth() {
		return stuBirth;
	}
	public void setStuBirth(Date stuBirth) {
		this.stuBirth = stuBirth;
	}
	public Student(String stuName, String stuNo, Integer stuAge, Date stuBirth) {
		super();
		this.stuName = stuName;
		this.stuNo = stuNo;
		this.stuAge = stuAge;
		this.stuBirth = stuBirth;
	}
	
	@Override
	public String toString() {
		return "Student [stuId=" + stuId + ", stuName=" + stuName + ", stuNo=" + stuNo + ", stuAge=" + stuAge
				+ ", stuBirth=" + stuBirth + "]";
	}

	public Student() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	
	
}
