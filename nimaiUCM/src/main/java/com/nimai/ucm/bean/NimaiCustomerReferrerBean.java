package com.nimai.ucm.bean;

import java.util.Date;

import org.springframework.stereotype.Component;


public class NimaiCustomerReferrerBean {
	
	
	private String userid;
	private String countryName;
	private String companyName;
	private Date insertedDate;
    private String accountStatus;
	private String expiredIn;
	private String currency;
	private Float earning;
	private String beanchUserId;
	//private Float earnings;
	
	
	
	
	
	
	
	
	

	public String getBeanchUserId() {
		return beanchUserId;
	}
	public void setBeanchUserId(String beanchUserId) {
		this.beanchUserId = beanchUserId;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public Date getInsertedDate() {
		return insertedDate;
	}
	public void setInsertedDate(Date insertedDate) {
		this.insertedDate = insertedDate;
	}
	public String getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	public String getExpiredIn() {
		return expiredIn;
	}
	public void setExpiredIn(String expiredIn) {
		this.expiredIn = expiredIn;
	}
	public Float getEarning() {
		return earning;
	}
	public void setEarning(Float earning) {
		this.earning = earning;
	}

	
	

}



