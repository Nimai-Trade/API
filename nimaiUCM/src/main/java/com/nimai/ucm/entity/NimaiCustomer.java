package com.nimai.ucm.entity;

import java.io.Serializable;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "NIMAI_M_CUSTOMER")
public class NimaiCustomer implements Serializable {

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@NotNull
	@Column(name = "SUBSCRIBER_TYPE")
	private String subscriberType;

	@Id
	@Basic(optional = false)
	@Column(name = "USERID")
	private String userid;

	@Column(name = "BANK_TYPE")
	private String bankType;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "EMAIL_ADDRESS")
	private String emailAddress;

	@Column(name = "MOBILE_NUMBER")
	private String mobileNumber;

	@Column(name = "COUNTRY_NAME")
	private String countryName;

	@Column(name = "LANDLINE")
	private String landline;

	@Column(name = "DESIGNATION")
	private String designation;

	@Column(name = "COMPANY_NAME")
	private String companyName;

	@Column(name = "BUSINESS_TYPE")
	private String businessType;

	@Column(name = "INSERTED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date insertedDate;

	@Column(name = "MODIFIED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	@Column(name = "BANK_NAME")
	private String bankNbfcName;

	@Column(name = "BRANCH_NAME")
	private String branchName;

	@Column(name = "SWIFT_CODE")
	private String swiftCode;

	@Column(name = "REGISTERED_COUNTRY")
	private String registredCountry;

	@Column(name = "TELEPHONE")
	private String telephone;

	@Column(name = "MIN_VALUEOF_LC")
	private String minValueofLc;

	@Column(name = "REGISTRATION_TYPE")
	private String registrationType;

	@Column(name = "PROVINCENAME")
	private String provincename;

	@Column(name = "ADDRESS1")
	private String address1;

	@Column(name = "ADDRESS2")
	private String address2;

	@Column(name = "ADDRESS3")
	private String address3;

	@Column(name = "CITY")
	private String city;

	@Column(name = "PINCODE")
	private String pincode;

	// Changes By Shubham Patil
	@Column(name = "IS_REGISTER")
	private Boolean isRegister;

	@Column(name = "IS_BDETAILSFILLED")
	private Boolean isBDetailsFilled;

	@Column(name = "IS_SPLANPURCHASED")
	private Boolean isSPlanPurchased;

	@Column(name = "MODE_OF_PAYMENT")
	private String modeOfPayment;

	@Column(name = "PAYMENT_STATUS")
	private String paymentStatus;

	@Column(name = "PAYMENT_DATE")
	private Date paymentDate;

	@Column(name = "KYC_STATUS")
	private String kycStatus;

	@Column(name = "KYC_APPROVALDATE")
	private Date kycApprovalDate;

	@Column(name = "IS_RMASSIGNED")
	private Boolean isRmAssigned;

	@Column(name = "RM_ID")
	private String rmId;
	// End

	@Column(name = "ACCOUNT_TYPE")
	private String accountType;

	@Column(name = "ACCOUNT_SOURCE")
	private String accountSource;

	@Column(name = "ACCOUNT_CREATED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date accountCreatedDate;

	@Column(name = "ACCOUNT_STATUS")
	private String accountStatus;

	@Column(name = "CURRENCY_CODE")
	private String regCurrency;

	@Column(name = "EMAIL_ADDRESS1")
	private String emailAddress1;

	@Column(name = "EMAIL_ADDRESS2")
	private String emailAddress2;

	@Column(name = "EMAIL_ADDRESS3")
	private String emailAddress3;

	@Column(name = "OTHERS")
	private String others;
	
	@Column(name = "TC_INSERTED_DATE")
	private Date tCInsertedDate;
	
	@Column(name = "TC_FLAG")
	private String tcFlag;
	
	@Column(name = "CUSTOMER_IP_ADDRESS")
	private String customerIp;
	
	

	@OneToMany(mappedBy = "userId")
	private List<InterestedCountry> intrestedCountrList;

	@OneToMany(mappedBy = "userId")
	private List<BlackListedGoods> bgList;

	@OneToMany(mappedBy = "userid")
	private List<NimaiSubscriptionDetails> subscriptionDettails;
	
	@OneToMany(mappedBy = "userid")
	private List<Refer> referrList;

	
	
	
	public Date gettCInsertedDate() {
		return tCInsertedDate;
	}

	public void settCInsertedDate(Date tCInsertedDate) {
		this.tCInsertedDate = tCInsertedDate;
	}

	public String getTcFlag() {
		return tcFlag;
	}

	public void setTcFlag(String tcFlag) {
		this.tcFlag = tcFlag;
	}

	public String getCustomerIp() {
		return customerIp;
	}

	public void setCustomerIp(String customerIp) {
		this.customerIp = customerIp;
	}

	public List<Refer> getReferrList() {
		return referrList;
	}

	public void setReferrList(List<Refer> referrList) {
		this.referrList = referrList;
	}

	public List<NimaiSubscriptionDetails> getSubscriptionDettails() {
		return subscriptionDettails;
	}

	public void setSubscriptionDettails(List<NimaiSubscriptionDetails> subscriptionDettails) {
		this.subscriptionDettails = subscriptionDettails;
	}

	public List<BlackListedGoods> getBgList() {
		return bgList;
	}

	public void setBgList(List<BlackListedGoods> bgList) {
		this.bgList = bgList;
	}

	public List<InterestedCountry> getIntrestedCountrList() {
		return intrestedCountrList;
	}

	public void setIntrestedCountrList(List<InterestedCountry> intrestedCountrList) {
		this.intrestedCountrList = intrestedCountrList;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

	public String getRegCurrency() {
		return regCurrency;
	}

	public void setRegCurrency(String regCurrency) {
		this.regCurrency = regCurrency;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
	private List<NimaiMLogin> nimaiMLoginList;

	public NimaiCustomer() {
	}

	public NimaiCustomer(String userid) {
		this.userid = userid;
	}

	public NimaiCustomer(String userid, String subscriberType, String firstName, String lastName, String emailAddress,
			String mobileNumber) {
		this.userid = userid;
		this.subscriberType = subscriberType;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.mobileNumber = mobileNumber;
	}

	public String getSubscriberType() {
		return subscriberType;
	}

	public void setSubscriberType(String subscriberType) {
		this.subscriberType = subscriberType;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
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

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getLandline() {
		return landline;
	}

	public void setLandline(String landline) {
		this.landline = landline;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
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

	public String getBankNbfcName() {
		return bankNbfcName;
	}

	public void setBankNbfcName(String bankNbfcName) {
		this.bankNbfcName = bankNbfcName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	public String getRegistredCountry() {
		return registredCountry;
	}

	public void setRegistredCountry(String registredCountry) {
		this.registredCountry = registredCountry;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMinValueofLc() {
		return minValueofLc;
	}

	public void setMinValueofLc(String minValueofLc) {
		this.minValueofLc = minValueofLc;
	}

	public String getRegistrationType() {
		return registrationType;
	}

	public void setRegistrationType(String registrationType) {
		this.registrationType = registrationType;
	}

	public String getProvincename() {
		return provincename;
	}

	public void setProvincename(String provincename) {
		this.provincename = provincename;
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

	public List<NimaiMLogin> getNimaiMLoginList() {
		return nimaiMLoginList;
	}

	public void setNimaiMLoginList(List<NimaiMLogin> nimaiMLoginList) {
		this.nimaiMLoginList = nimaiMLoginList;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountSource() {
		return accountSource;
	}

	public void setAccountSource(String accountSource) {
		this.accountSource = accountSource;
	}

	public Date getAccountCreatedDate() {
		return accountCreatedDate;
	}

	public void setAccountCreatedDate(Date accountCreatedDate) {
		this.accountCreatedDate = accountCreatedDate;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
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

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (userid != null ? userid.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof NimaiCustomer)) {
			return false;
		}
		NimaiCustomer other = (NimaiCustomer) object;
		if ((this.userid == null && other.userid != null)
				|| (this.userid != null && !this.userid.equals(other.userid))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "in.nimai.NimaiMCustomer[ userid=" + userid + " ]";
	}

}
