package com.nimai.ucm.bean;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class GenericResponse<E> implements Serializable 
{
	private String errMessage;
	private E data;
	private List list;
	private String errCode ;
	private String status;
	private float totalEarning;
	
	
	
	
	
	public float getTotalEarning() {
		return totalEarning;
	}
	public void setTotalEarning(float totalEarning) {
		this.totalEarning = totalEarning;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}


	public E getData() {
		return data;
	}
	public void setData(E data) {
		this.data = data;
	}
	public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
	public String getErrMessage() {
		return errMessage;
	}
	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
