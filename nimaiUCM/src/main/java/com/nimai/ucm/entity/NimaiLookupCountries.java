package com.nimai.ucm.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "nimai_lookup_countries")
@NamedQueries({ @NamedQuery(name = "NimaiLookupCountries.findAll", query = "SELECT n FROM NimaiLookupCountries n") })
public class NimaiLookupCountries implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "COUNTRY_ID")
	private Integer countryId;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "COUNTRY_NAME")
	private String countryName;
	
	@Column(name = "ISO3")
	private String iso3;
	
	@Column(name = "ISO2")
	private String iso2;
	
	@Column(name = "PHONE_CODE")
	private String phoneCode;
	
	@Column(name = "CAPITAL")
	private String capital;
	
	@Column(name = "CURRENCY")
	private String currency;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "CREATED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "COUNTRY_STATUS")
	private boolean countryStatus;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "countryId")
	private List<NimaiLookupStates> nimaiLookupStatesList;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "countryId")
	private List<NimaiLookupCities> nimaiLookupCitiesList;

	public NimaiLookupCountries() {
	}

	public NimaiLookupCountries(Integer countryId) {
		this.countryId = countryId;
	}

	public NimaiLookupCountries(Integer countryId, String countryName, Date createdDate, boolean countryStatus) {
		this.countryId = countryId;
		this.countryName = countryName;
		this.createdDate = createdDate;
		this.countryStatus = countryStatus;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getIso3() {
		return iso3;
	}

	public void setIso3(String iso3) {
		this.iso3 = iso3;
	}

	public String getIso2() {
		return iso2;
	}

	public void setIso2(String iso2) {
		this.iso2 = iso2;
	}

	public String getPhoneCode() {
		return phoneCode;
	}

	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}

	public String getCapital() {
		return capital;
	}

	public void setCapital(String capital) {
		this.capital = capital;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public boolean getCountryStatus() {
		return countryStatus;
	}

	public void setCountryStatus(boolean countryStatus) {
		this.countryStatus = countryStatus;
	}

	public List<NimaiLookupStates> getNimaiLookupStatesList() {
		return nimaiLookupStatesList;
	}

	public void setNimaiLookupStatesList(List<NimaiLookupStates> nimaiLookupStatesList) {
		this.nimaiLookupStatesList = nimaiLookupStatesList;
	}

	public List<NimaiLookupCities> getNimaiLookupCitiesList() {
		return nimaiLookupCitiesList;
	}

	public void setNimaiLookupCitiesList(List<NimaiLookupCities> nimaiLookupCitiesList) {
		this.nimaiLookupCitiesList = nimaiLookupCitiesList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (countryId != null ? countryId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof NimaiLookupCountries)) {
			return false;
		}
		NimaiLookupCountries other = (NimaiLookupCountries) object;
		if ((this.countryId == null && other.countryId != null)
				|| (this.countryId != null && !this.countryId.equals(other.countryId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.nimai.admin.model.NimaiLookupCountries[ countryId=" + countryId + " ]";
	}

}
