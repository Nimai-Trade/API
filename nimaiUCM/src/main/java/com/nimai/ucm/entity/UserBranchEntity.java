package com.nimai.ucm.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "NIMAI_M_BRANCH")
public class UserBranchEntity  implements Serializable {

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID")
	private int id;
	
	@Column(name ="USERID")
	private String user_id;

	@Column(name = "BATCH_ID")
	private String  batch_id;

	@Column(name = "EMPLOYEE_ID")
	private String employee_id;

	@Column(name = "EMAIL_ID")
	private String email_id;

	@Column(name = "EMPLOYEE_NAME")
	private String employee_name;

	@Column(name = "PASSCODE_VALUE")
	private String passcode_value;

	@Column(name = "TOKEN_EXPIRY_TIME")
	private Date expiry_time;

	@Column(name = "INSERTED_BY")
	private String insert_by;

	@Column(name="passcode_count")
	private Integer passcodeCount;
	
	@Column(name = "INSERTED_DATE")
	private Date insert_time;

	@Column(name = "MODIFIED_BY")
	private String modify_by;

	@Column(name = "MODIFIED_DATE")
	private Date modify_time;

	
	
	
	
	
	public Integer getPasscodeCount() {
		return passcodeCount;
	}

	public void setPasscodeCount(Integer passcodeCount) {
		this.passcodeCount = passcodeCount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public Date getExpiry_time() {
		return expiry_time;
	}

	public void setExpiry_time(Date expiry_time) {
		this.expiry_time = expiry_time;
	}

	public String getInsert_by() {
		return insert_by;
	}

	public void setInsert_by(String insert_by) {
		this.insert_by = insert_by;
	}

	public Date getInsert_time() {
		return insert_time;
	}

	public void setInsert_time(Date insert_time) {
		this.insert_time = insert_time;
	}

	public String getModify_by() {
		return modify_by;
	}

	public void setModify_by(String modify_by) {
		this.modify_by = modify_by;
	}

	public Date getModify_time() {
		return modify_time;
	}

	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}

}
