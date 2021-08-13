package com.nimai.uam.bean;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class GenericResponse<T> implements Serializable 
{
	private String message;
	private T data;
	private List list;
	private String errCode ;
	private int flag;
	
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
	
}
