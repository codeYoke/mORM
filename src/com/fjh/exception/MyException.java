package com.fjh.exception;

public class MyException extends Exception {
	int code;//��Ϣ��
	String message;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public MyException(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	
}
