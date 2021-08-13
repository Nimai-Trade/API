package com.nimai.ucm.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "NIMAI_M_LOGIN")
//@SequenceGenerator(sequenceName = "LOGIN_SEQ", name = "LOGIN_SEQ", allocationSize = 1)
public class NimaiMLogin implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	@Column(name = "PASSWORD")
	private String password;
	
	
	@Column(name = "USER_TYPE")
	private String userType;
	
	
	@Column(name = "IS_ACT_PASSED")
	private String isActPassed;
	
	
	@Column(name = "FLAG")
	private String flag;
	
	@Column(name = "INSERTED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date insertedDate;
	
	@Column(name = "MODIFIED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;
	
	@Column(name = "LAST_LOGIN_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLoginTime;
	
	@Column(name = "LAST_ACTIVITY_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastActivityTime;
	
	
	// Added BY UDAY 
	
	
	@Column(name = "TOKEN")
	private String token;
	
	@Column(name = "TOKEN_EXPIRY_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date token_exp_Date;
	// Added BY UDAY
	
	
	
	@Column(name = "STATUS")
	private String status;
	@Id
	@Basic(optional = false)
	

//  Change by Uday Kumar 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
// End of change Uday Kumar
	@Column(name = "LOGIN_ID")
	private Integer loginId;
	
	@JoinColumn(name = "USERID", referencedColumnName = "USERID")
	@ManyToOne(optional = false)
	private NimaiCustomer userid;

	
	



	/**
	 * @return the password
	 */
	/*public String getPassword() {
		return password;
	}

	public NimaiMLogin() {
	}

	public NimaiMLogin(Integer loginId) {
		this.loginId = loginId;
	}

	public String getpassword() {
		return password;
	}
*/
	public String getToken() {
		return token;
	}

	public String getPassword() {
		return password;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getToken_exp_Date() {
		return token_exp_Date;
	}

	public void setToken_exp_Date(Date token_exp_Date) {
		this.token_exp_Date = token_exp_Date;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getIsActPassed() {
		return isActPassed;
	}

	public void setIsActPassed(String isActPassed) {
		this.isActPassed = isActPassed;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
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

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Date getLastActivityTime() {
		return lastActivityTime;
	}

	public void setLastActivityTime(Date lastActivityTime) {
		this.lastActivityTime = lastActivityTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getLoginId() {
		return loginId;
	}

	public void setLoginId(Integer loginId) {
		this.loginId = loginId;
	}

	public NimaiCustomer getUserid() {
		return userid;
	}

	public void setUserid(NimaiCustomer userid) {
		this.userid = userid;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (loginId != null ? loginId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof NimaiMLogin)) {
			return false;
		}
		NimaiMLogin other = (NimaiMLogin) object;
		if ((this.loginId == null && other.loginId != null)
				|| (this.loginId != null && !this.loginId.equals(other.loginId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "in.nimai.NimaiMLogin[ loginId=" + loginId + " ]";
	}

}
