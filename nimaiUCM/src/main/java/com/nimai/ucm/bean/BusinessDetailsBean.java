package com.nimai.ucm.bean;

import java.util.Arrays;

public class BusinessDetailsBean {

	private String userId;

	private String comapanyName;
	private OwnerMasterBean[] ownerMasterBean;
    private String bankName;
	private String branchName;
	private String designation;
	private String swiftCode;
	private String telephone;
    private String registrationType;

	private String address1;
	private String address2;
	private String address3;
	private String provinceName;
	private String city;
	private String pincode;
	private String registeredCountry;
	
	//Change by Shubham patil		
	private Boolean isBDetailsFilled;
	//End
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getComapanyName() {
		return comapanyName;
	}

	public void setComapanyName(String comapanyName) {
		this.comapanyName = comapanyName;
	}

	public OwnerMasterBean[] getOwnerMasterBean() {
		return ownerMasterBean;
	}

	public void setOwnerMasterBean(OwnerMasterBean[] ownerMasterBean) {
		this.ownerMasterBean = ownerMasterBean;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getRegisteredCountry() {
		return registeredCountry;
	}

	public void setRegisteredCountry(String registeredCountry) {
		this.registeredCountry = registeredCountry;
	}

	public String getRegistrationType() {
		return registrationType;
	}

	public void setRegistrationType(String registrationType) {
		this.registrationType = registrationType;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public Boolean getIsBDetailsFilled() {
		return isBDetailsFilled;
	}

	public void setIsBDetailsFilled(Boolean isBDetailsFilled) {
		this.isBDetailsFilled = isBDetailsFilled;
	}

	@Override
	public String toString() {
		return "BusinessDetailsBean [userId=" + userId + ", comapanyName=" + comapanyName + ", ownerMasterBean="
				+ Arrays.toString(ownerMasterBean) + ", bankName=" + bankName + ", branchName=" + branchName
				+ ", designation=" + designation + ", swiftCode=" + swiftCode + ", telephone=" + telephone
				+ ", registrationType=" + registrationType + ", address1=" + address1 + ", address2=" + address2
				+ ", address3=" + address3 + ", provinceName=" + provinceName + ", city=" + city + ", pincode="
				+ pincode + ", registeredCountry=" + registeredCountry + ", isBDetailsFilled=" + isBDetailsFilled + "]";
	}

}
