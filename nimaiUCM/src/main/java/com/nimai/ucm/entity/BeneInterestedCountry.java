/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javax.validation.constraints.Size;

@Entity
@Table(name = "NIMAI_F_BENEINTCOUNTRY")
public class BeneInterestedCountry implements Serializable {

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "COUNTRY_ID")
	private Long countryID;

	private static final long serialVersionUID = 1L;
	@Column(name = "COUNTRY_NAME")
	private String countryName;

	@Column(name = "INSERTED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date insertedDate;

	@Column(name = "MODIFIED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	
//	@Column(name = "COUNTRY_CURID")
//	private Integer countryCurrencyId;
	
	@Column(name = "COUNTRY_CURID")
	private String countryCurrencyId;
	
	
//	public Integer getCountryCurrencyId() {
//		return countryCurrencyId;
//	}
//
//	public void setCountryCurrencyId(Integer countryCurrencyId) {
//		this.countryCurrencyId = countryCurrencyId;
//	}
	
	

	@JoinColumn(name = "USERID", referencedColumnName = "USERID")
	@ManyToOne(optional = false)
	private NimaiCustomer userId;

	public String getCountryCurrencyId() {
		return countryCurrencyId;
	}

	public void setCountryCurrencyId(String countryCurrencyId) {
		this.countryCurrencyId = countryCurrencyId;
	}

	public Long getCountryID() {
		return countryID;
	}

	public void setCountryID(Long countryID) {
		this.countryID = countryID;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
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

	public void setUserId(NimaiCustomer userId) {
		this.userId = userId;
	}
	
	public NimaiCustomer getUserId() {
		return userId;
	}
}
