package com.nimai.ucm.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "NIMAI_LOOKUP_COUNTRIES")
public class Countries implements Serializable {

	private static final long serialVersionUID = 954880494790619388L;

	@Id
	@Column(name = "COUNTRY_ID")
	private int countryId;

	@Column(name = "COUNTRY_NAME")
	private String countryName;

	@Column(name = "PHONE_CODE")
	private String phoneCode;

	public Countries() {
	}

	public Countries(String countryName) {
		super();
		this.countryName = countryName;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getPhoneCode() {
		return phoneCode;
	}

	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}

}