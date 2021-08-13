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
@Table(name = "nimai_lookup_states")
public class States implements Serializable {

	private static final long serialVersionUID = -948667749181896671L;

	private int stateId;
	private String stateName;
	private Countries countries;

	public States(int stateId, String stateName) {
		this.stateId = stateId;
		this.stateName = stateName;
	}

	public States() {
		super();
	}

	@Id
	@Column(name = "STATE_ID", nullable = false)
	public int getStateId() {
		return stateId;
	}

	public void setStateId(int stateId) {
		this.stateId = stateId;
	}

	@Column(name = "STATE_NAME")
	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {

		this.stateName = stateName;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COUNTRY_ID")
	public Countries getCountries() {
		return countries;
	}

	public void setCountries(Countries countries) {
		this.countries = countries;
	}

}
