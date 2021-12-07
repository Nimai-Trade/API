package com.nimai.ucm.bean;

public class BeneficiaryInterestedCountryBean {

	private Long countryID;
	private String countriesIntrested;
	private String ccid;
	
	

	public Long getCountryID() {
		return countryID;
	}

	public void setCountryID(Long countryID) {
		this.countryID = countryID;
	}

//	public int getCcid() {
//		return ccid;
//	}
//
//	public void setCcid(int ccid) {
//		this.ccid = ccid;
//	}

	
	
	public String getCountriesIntrested() {
		return countriesIntrested;
	}

	public String getCcid() {
		return ccid;
	}

	public void setCcid(String ccid) {
		this.ccid = ccid;
	}

	public void setCountriesIntrested(String countriesIntrested) {
		this.countriesIntrested = countriesIntrested;
	}

}
