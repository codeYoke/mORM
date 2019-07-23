package com.fjh.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)//作用于类
@Retention(RetentionPolicy.RUNTIME)
public @interface MyTable {
	public String tableName() default "";
}
