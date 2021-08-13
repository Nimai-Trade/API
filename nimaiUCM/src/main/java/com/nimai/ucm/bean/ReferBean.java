package com.nimai.ucm.bean;

import java.util.Date;

public class ReferBean {

	private int id;
	private String userId;
	private String referenceId;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String mobileNo;
	private String countryName;
	private String companyName;
	private String status;
	private Date insertedDate;
	private Date modifiedDate;
	private String branchUserId;
	private String insertedBy;
	private String modifiedBy;

	private String planPurchased;
	private String billingAmount;
	private String earnings;
	private String purchaseType;
	private String referrer_Email_Id;
	private float userWiseTotalEarning;
	
	
	
	
	
	
	
	
	
	




	public float getUserWiseTotalEarning() {
		return userWiseTotalEarning;
	}

	public void setUserWiseTotalEarning(float userWiseTotalEarning) {
		this.userWiseTotalEarning = userWiseTotalEarning;
	}

	public String getReferrer_Email_Id() {
		return referrer_Email_Id;
	}

	public void setReferrer_Email_Id(String referrer_Email_Id) {
		this.referrer_Email_Id = referrer_Email_Id;
	}

	public String getPlanPurchased() {
		return planPurchased;
	}

	public void setPlanPurchased(String planPurchased) {
		this.planPurchased = planPurchased;
	}

	public String getBillingAmount() {
		return billingAmount;
	}

	public void setBillingAmount(String billingAmount) {
		this.billingAmount = billingAmount;
	}

	public String getEarnings() {
		return earnings;
	}

	public void setEarnings(String earnings) {
		this.earnings = earnings;
	}

	public String getPurchaseType() {
		return purchaseType;
	}

	public void setPurchaseType(String purchaseType) {
		this.purchaseType = purchaseType;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getInsertedDate() {
		return insertedDate;
	}

	public void setInsertedDate(Date insertedDate) {
		this.insertedDate = insertedDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getBranchUserId() {
		return branchUserId;
	}

	public void setBranchUserId(String branchUserId) {
		this.branchUserId = branchUserId;
	}

	public String getInsertedBy() {
		return insertedBy;
	}

	public void setInsertedBy(String insertedBy) {
		this.insertedBy = insertedBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

}
