package com.nimai.ucm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="NIMAI_F_BRANCHUSER")
public class BranchUser {

	@Id
	@Column(name="TOKEN_ID")
	private String tokenId;
	
	@Column(name="EMAIL_ID")
	private String emailId;
	
	@Column(name="EMPLOYEE_ID")
	private String employeeId;
	
	@Column(name="BRANCH_ID")
	private String branchId;
	
	@Column(name="INSERTED_BY")
	private String insertedBy;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Column(name="INSERTED_DATE")
	private String insertedDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Column(name="MODIFIED_DATE")
	private String modifiedDate;

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getInsertedBy() {
		return insertedBy;
	}

	public void setInsertedBy(String insertedBy) {
		this.insertedBy = insertedBy;
	}

	public String getInsertedDate() {
		return insertedDate;
	}

	public void setInsertedDate(String insertedDate) {
		this.insertedDate = insertedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	
	
}
