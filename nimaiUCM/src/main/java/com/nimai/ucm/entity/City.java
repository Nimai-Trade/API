package com.nimai.ucm.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "nimai_lookup_cities")
public class City implements Serializable {

	private static final long serialVersionUID = -1309343777690096968L;

	private int cityId;

	private States states;

	private String cityName;

	public City() {
	}

	public City(int cityId, String cityName) {
		super();
		this.cityId = cityId;
		this.cityName = cityName;
	}

	public City(States states, String cityName) {
		super();
		this.states = states;
		this.cityName = cityName;
	}

	@Id
	@Column(name = "CITY_ID", nullable = false)
	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "STATE_ID", nullable = false)
	public States getStates() {
		return states;
	}

	public void setStates(States states) {
		this.states = states;
	}

	@Column(name = "CITY_NAME", nullable = false)
	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

}
