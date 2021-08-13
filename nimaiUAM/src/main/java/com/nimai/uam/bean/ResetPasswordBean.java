package com.nimai.uam.bean;

public class ResetPasswordBean {

	private String emailId;
	private String userId;
	private String oldPassword;
	private String newPassword;
	private String retypePaasword;
	public String getToken;
	private String tcFlag;
	
	
	

	
	public String getGetToken() {
		return getToken;
	}

	public void setGetToken(String getToken) {
		this.getToken = getToken;
	}

	public String getTcFlag() {
		return tcFlag;
	}

	public void setTcFlag(String tcFlag) {
		this.tcFlag = tcFlag;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getRetypePaasword() {
		return retypePaasword;
	}

	public void setRetypePaasword(String retypePaasword) {
		this.retypePaasword = retypePaasword;
	}

}
