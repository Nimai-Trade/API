package com.nimai.ucm.bean;

import java.io.Serializable;
import java.util.List;

public class GenericResBean <E> implements Serializable {
	private String errMessage;
	private E data;
	private float totalEarning;
	private List list;
	private String errCode ;
	private String status;
	
	public String getErrMessage() {
		return errMessage;
	}
	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
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
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public float getTotalEarning() {
		return totalEarning;
	}
	public void setTotalEarning(float totalEarning) {
		this.totalEarning = totalEarning;
	}
	
	
	
}
