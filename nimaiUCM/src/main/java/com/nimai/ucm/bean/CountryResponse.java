package com.nimai.ucm.bean;

import java.util.List;

public class CountryResponse {

	private Integer countryId;
	private String countryName;
	private String phoneCode;
	public String getPhoneCode() {
		return phoneCode;
	}

	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}

	private String currency;
	private List<StateResponce> stateResponce;

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public List<StateResponce> getStateResponce() {
		return stateResponce;
	}

	public void setStateResponce(List<StateResponce> stateResponce) {
		this.stateResponce = stateResponce;
	}

}
