package com.nimai.ucm.bean;

import java.util.Date;

public class UserBranchBean {

	private String id;
	private String user_id;
	private String batch_id;
	private String employee_id;
	private String email_id;
	private String employee_name;
	private String passcode_value;
	private Date token_expiry_time;
	private String inserted_by;
	private Date inserted_date;
	private String modified_by;
	private Date modified_date;
	private int passcodeCount;
	
	
	


	public int getPasscodeCount() {
		return passcodeCount;
	}
	public void setPasscodeCount(int passcodeCount) {
		this.passcodeCount = passcodeCount;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getBatch_id() {
		return batch_id;
	}
	public void setBatch_id(String batch_id) {
		this.batch_id = batch_id;
	}
	public String getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}
	public String getEmail_id() {
		return email_id;
	}
	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}
	public String getEmployee_name() {
		return employee_name;
	}
	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}
	public String getPasscode_value() {
		return passcode_value;
	}
	public void setPasscode_value(String passcode_value) {
		this.passcode_value = passcode_value;
	}
	
	public Date getToken_expiry_time() {
		return token_expiry_time;
	}
	public void setToken_expiry_time(Date token_expiry_time) {
		this.token_expiry_time = token_expiry_time;
	}
	public String getInserted_by() {
		return inserted_by;
	}
	public void setInserted_by(String inserted_by) {
		this.inserted_by = inserted_by;
	}
	public Date getInserted_date() {
		return inserted_date;
	}
	public void setInserted_date(Date inserted_date) {
		this.inserted_date = inserted_date;
	}
	public String getModified_by() {
		return modified_by;
	}
	public void setModified_by(String modified_by) {
		this.modified_by = modified_by;
	}
	public Date getModified_date() {
		return modified_date;
	}
	public void setModified_date(Date modified_date) {
		this.modified_date = modified_date;
	}
	
	
}
