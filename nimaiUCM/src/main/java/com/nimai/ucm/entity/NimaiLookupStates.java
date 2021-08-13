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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "nimai_lookup_states")
@NamedQueries({
    @NamedQuery(name = "NimaiLookupStates.findAll", query = "SELECT n FROM NimaiLookupStates n")})
public class NimaiLookupStates implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "STATE_ID")
    private Integer stateId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "STATE_NAME")
    private String stateName;
    @Size(max = 255)
    @Column(name = "STATE_SHORT_NAME")
    private String stateShortName;
    @Size(max = 20)
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STATE_STATUS")
    private boolean stateStatus;
    @JoinColumn(name = "COUNTRY_ID", referencedColumnName = "COUNTRY_ID")
    @ManyToOne(optional = false)
    private NimaiLookupCountries countryId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "stateId")
    private List<NimaiLookupCities> nimaiLookupCitiesList;

    public NimaiLookupStates() {
    }

    public NimaiLookupStates(Integer stateId) {
        this.stateId = stateId;
    }

    public NimaiLookupStates(Integer stateId, String stateName, Date createdDate, boolean stateStatus) {
        this.stateId = stateId;
        this.stateName = stateName;
        this.createdDate = createdDate;
        this.stateStatus = stateStatus;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateShortName() {
        return stateShortName;
    }

    public void setStateShortName(String stateShortName) {
        this.stateShortName = stateShortName;
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

    public boolean getStateStatus() {
        return stateStatus;
    }

    public void setStateStatus(boolean stateStatus) {
        this.stateStatus = stateStatus;
    }

    public NimaiLookupCountries getCountryId() {
        return countryId;
    }

    public void setCountryId(NimaiLookupCountries countryId) {
        this.countryId = countryId;
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
        hash += (stateId != null ? stateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NimaiLookupStates)) {
            return false;
        }
        NimaiLookupStates other = (NimaiLookupStates) object;
        if ((this.stateId == null && other.stateId != null) || (this.stateId != null && !this.stateId.equals(other.stateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.nimai.admin.model.NimaiLookupStates[ stateId=" + stateId + " ]";
    }
    
}
