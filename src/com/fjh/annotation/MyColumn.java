package com.fjh.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 * @ClassName: MyClounm
 * @Description: TODO(��ע��)
 * @author ��Ѻ�
 * @date 2019��7��10��
 *
 */

@Target(ElementType.FIELD)//��������ͨ����
@Retention(RetentionPolicy.RUNTIME)//����ʱ��Ч
public @interface MyColumn {
	public String columnName();
	public int length() default 50;
}
