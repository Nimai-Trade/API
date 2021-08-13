package com.nimai.ucm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Sahadeo
 */
@Entity
@Table(name = "nimai_lookup_cities")
@NamedQueries({ @NamedQuery(name = "NimaiLookupCities.findAll", query = "SELECT n FROM NimaiLookupCities n") })
public class NimaiLookupCities implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "CITY_ID")
	private Integer cityId;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "CITY_NAME")
	private String cityName;
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Basic(optional = false)
	@NotNull
	@Column(name = "LATITUDE")
	private BigDecimal latitude;
	@Basic(optional = false)
	@NotNull
	@Column(name = "LONGITUDE")
	private BigDecimal longitude;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 50)
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Basic(optional = false)
	@NotNull
	@Column(name = "CREATED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	@Basic(optional = false)
	@NotNull
	@Column(name = "CITY_STATUS")
	private boolean cityStatus;
	@JoinColumn(name = "STATE_ID", referencedColumnName = "STATE_ID")
	@ManyToOne(optional = false)
	private NimaiLookupStates stateId;
	@JoinColumn(name = "COUNTRY_ID", referencedColumnName = "COUNTRY_ID")
	@ManyToOne(optional = false)
	private NimaiLookupCountries countryId;

	public NimaiLookupCities() {
	}

	public NimaiLookupCities(Integer cityId) {
		this.cityId = cityId;
	}

	public NimaiLookupCities(Integer cityId, String cityName, BigDecimal latitude, BigDecimal longitude,
			String createdBy, Date createdDate, boolean cityStatus) {
		this.cityId = cityId;
		this.cityName = cityName;
		this.latitude = latitude;
		this.longitude = longitude;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.cityStatus = cityStatus;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
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

	public boolean getCityStatus() {
		return cityStatus;
	}

	public void setCityStatus(boolean cityStatus) {
		this.cityStatus = cityStatus;
	}

	public NimaiLookupStates getStateId() {
		return stateId;
	}

	public void setStateId(NimaiLookupStates stateId) {
		this.stateId = stateId;
	}

	public NimaiLookupCountries getCountryId() {
		return countryId;
	}

	public void setCountryId(NimaiLookupCountries countryId) {
		this.countryId = countryId;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (cityId != null ? cityId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof NimaiLookupCities)) {
			return false;
		}
		NimaiLookupCities other = (NimaiLookupCities) object;
		if ((this.cityId == null && other.cityId != null)
				|| (this.cityId != null && !this.cityId.equals(other.cityId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.nimai.admin.model.NimaiLookupCities[ cityId=" + cityId + " ]";
	}

}
