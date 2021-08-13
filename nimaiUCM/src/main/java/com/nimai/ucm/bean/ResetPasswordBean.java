package com.nimai.ucm.bean;

public class ResetPasswordBean {

	private String username;
	private String oldPassword;
	private String newPassword;
	private String retypePaasword;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
