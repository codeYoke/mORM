package com.fjh.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)//声明周期，运行时有效
public @interface MyKey {
	public String columnName();
	public boolean isGenerator() default false;//默认不是自动增长
}
