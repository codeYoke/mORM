package com.fjh.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 * @ClassName: MyClounm
 * @Description: TODO(列注解)
 * @author 冯佳豪
 * @date 2019年7月10日
 *
 */

@Target(ElementType.FIELD)//作用于普通属性
@Retention(RetentionPolicy.RUNTIME)//运行时有效
public @interface MyColumn {
	public String columnName();
	public int length() default 50;
}
