package com.fjh.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)//�������ڣ�����ʱ��Ч
public @interface MyKey {
	public String columnName();
	public boolean isGenerator() default false;//Ĭ�ϲ����Զ�����
}
