package com.nimai.ucm.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PersonalDetailsBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;

	private String subscriberType;
	private String bankType;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String mobileNum;
	private String landLinenumber;
	private String countryName;
	private String tcFlag;
	// Changes By Shubham Patil
	private Boolean isRegister;
	private Boolean isBDetailsFilled;
	private Boolean isSPlanPurchased;
	private String modeOfPayment;
	private String paymentStatus;
	private Date paymentDate;
	private String kycStatus;
	private Date kycApprovalDate;
	private Boolean isRmAssigned;
	private String rmId;

	private String emailAddress1;
	private String emailAddress2;
	private String emailAddress3;
	// End

	private String companyName;

	private String designation;
	private String businessType;

	private String minLCValue;
//	private InterestedCountryBean[] interestedCountry;
//	private BlackListedGoodsBean[] blacklistedGoods;
	
	
	private List<InterestedCountryBean> interestedCountry;
	private List<BlackListedGoodsBean> blacklistedGoods;

	private String account_type;
	private String account_source;
	private String account_status;
	private Date account_created_date;
	private String regCurrency;
	private String recaptchaResponse;
	private int status;
    private String message;

    private String token;
    
 private String otherType;
 private String otherTypeBank;
 
 



	public String getTcFlag() {
	return tcFlag;
}

public void setTcFlag(String tcFlag) {
	this.tcFlag = tcFlag;
}

	public String getOtherTypeBank() {
	return otherTypeBank;
}

public void setOtherTypeBank(String otherTypeBank) {
	this.otherTypeBank = otherTypeBank;
}

	public String getOtherType() {
	return otherType;
}

public void setOtherType(String otherType) {
	this.otherType = otherType;
}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRecaptchaResponse() {
		return recaptchaResponse;
	}

	public void setRecaptchaResponse(String recaptchaResponse) {
		this.recaptchaResponse = recaptchaResponse;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSubscriberType() {
		return subscriberType;
	}

	public void setSubscriberType(String subscriberType) {
		this.subscriberType = subscriberType;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
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

	public String getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}

	public String getLandLinenumber() {
		return landLinenumber;
	}

	public void setLandLinenumber(String landLinenumber) {
		this.landLinenumber = landLinenumber;
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

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getMinLCValue() {
		return minLCValue;
	}

	public void setMinLCValue(String minLCValue) {
		this.minLCValue = minLCValue;
	}

//	public InterestedCountryBean[] getInterestedCountry() {
//		return interestedCountry;
//	}
//
//	public void setInterestedCountry(InterestedCountryBean[] interestedCountry) {
//		this.interestedCountry = interestedCountry;
//	}
//
//	public BlackListedGoodsBean[] getBlacklistedGoods() {
//		return blacklistedGoods;
//	}
//
//	public void setBlacklistedGoods(BlackListedGoodsBean[] blacklistedGoods) {
//		this.blacklistedGoods = blacklistedGoods;
//	}

	public Boolean getIsRegister() {
		return isRegister;
	}

	public void setIsRegister(Boolean isRegister) {
		this.isRegister = isRegister;
	}

	public Boolean getIsBDetailsFilled() {
		return isBDetailsFilled;
	}

	public void setIsBDetailsFilled(Boolean isBDetailsFilled) {
		this.isBDetailsFilled = isBDetailsFilled;
	}

	public Boolean getIsSPlanPurchased() {
		return isSPlanPurchased;
	}

	public void setIsSPlanPurchased(Boolean isSPlanPurchased) {
		this.isSPlanPurchased = isSPlanPurchased;
	}

	public String getModeOfPayment() {
		return modeOfPayment;
	}

	public void setModeOfPayment(String modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getKycStatus() {
		return kycStatus;
	}

	public void setKycStatus(String kycStatus) {
		this.kycStatus = kycStatus;
	}

	public Date getKycApprovalDate() {
		return kycApprovalDate;
	}

	public void setKycApprovalDate(Date kycApprovalDate) {
		this.kycApprovalDate = kycApprovalDate;
	}

	public Boolean getIsRmAssigned() {
		return isRmAssigned;
	}

	public void setIsRmAssigned(Boolean isRmAssigned) {
		this.isRmAssigned = isRmAssigned;
	}

	public String getRmId() {
		return rmId;
	}

	public void setRmId(String rmId) {
		this.rmId = rmId;
	}

	public String getAccount_type() {
		return account_type;
	}

	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}

	public String getAccount_source() {
		return account_source;
	}

	public void setAccount_source(String account_source) {
		this.account_source = account_source;
	}

	public String getAccount_status() {
		return account_status;
	}

	public void setAccount_status(String account_status) {
		this.account_status = account_status;
	}

	public Date getAccount_created_date() {
		return account_created_date;
	}

	public void setAccount_created_date(Date account_created_date) {
		this.account_created_date = account_created_date;
	}

	public String getRegCurrency() {
		return regCurrency;
	}

	public void setRegCurrency(String regCurrency) {
		this.regCurrency = regCurrency;
	}

	public String getEmailAddress1() {
		return emailAddress1;
	}

	public void setEmailAddress1(String emailAddress1) {
		this.emailAddress1 = emailAddress1;
	}

	public String getEmailAddress2() {
		return emailAddress2;
	}

	public void setEmailAddress2(String emailAddress2) {
		this.emailAddress2 = emailAddress2;
	}

	public String getEmailAddress3() {
		return emailAddress3;
	}

	public void setEmailAddress3(String emailAddress3) {
		this.emailAddress3 = emailAddress3;
	}
	
	

	public List<InterestedCountryBean> getInterestedCountry() {
		return interestedCountry;
	}

	public void setInterestedCountry(List<InterestedCountryBean> interestedCountry) {
		this.interestedCountry = interestedCountry;
	}

	public List<BlackListedGoodsBean> getBlacklistedGoods() {
		return blacklistedGoods;
	}

	public void setBlacklistedGoods(List<BlackListedGoodsBean> blacklistedGoods) {
		this.blacklistedGoods = blacklistedGoods;
	}

	@Override
	public String toString() {
		return "PersonalDetailsBean [userId=" + userId + ", subscriberType=" + subscriberType + ", bankType=" + bankType
				+ ", firstName=" + firstName + ", lastName=" + lastName + ", emailAddress=" + emailAddress
				+ ", mobileNum=" + mobileNum + ", landLinenumber=" + landLinenumber + ", countryName=" + countryName
				+ ", isRegister=" + isRegister + ", isBDetailsFilled=" + isBDetailsFilled + ", isSPlanPurchased="
				+ isSPlanPurchased + ", modeOfPayment=" + modeOfPayment + ", paymentStatus=" + paymentStatus
				+ ", paymentDate=" + paymentDate + ", kycStatus=" + kycStatus + ", kycApprovalDate=" + kycApprovalDate
				+ ", isRmAssigned=" + isRmAssigned + ", rmId=" + rmId + ", emailAddress1=" + emailAddress1
				+ ", emailAddress2=" + emailAddress2 + ", emailAddress3=" + emailAddress3 + ", companyName="
				+ companyName + ", designation=" + designation + ", businessType=" + businessType + ", minLCValue="
				+ minLCValue + ", interestedCountry=" + interestedCountry + ", blacklistedGoods=" + blacklistedGoods
				+ ", account_type=" + account_type + ", account_source=" + account_source + ", account_status="
				+ account_status + ", account_created_date=" + account_created_date + ", regCurrency=" + regCurrency
				+ ", recaptchaResponse=" + recaptchaResponse + ", status=" + status + ", message=" + message
				+ ", token=" + token + ", otherType=" + otherType + ", otherTypeBank=" + otherTypeBank + "]";
	}







}
