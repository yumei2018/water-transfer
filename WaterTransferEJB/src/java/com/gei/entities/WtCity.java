/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ymei
 */
@Entity
@Table(name = "WT_CITY")
@NamedQueries({
    @NamedQuery(name = "WtCity.findAll", query = "SELECT w FROM WtCity w")})
public class WtCity implements Serializable {    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "WT_CITY_ID")
    private Integer wtCityId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NAME")
    private String name;
    @Column(name = "SHORT_NAME")
    private String shortName;
    @OneToMany(mappedBy = "wtCity")
    private Collection<WtAddress> wtAddressCollection;
    @OneToMany(mappedBy = "wtCity")
    private Collection<WtContact> wtContactCollection;

    public WtCity() {
    }

    public WtCity(Integer wtCityId) {
        this.wtCityId = wtCityId;
    }

    public WtCity(Integer wtCityId, String name) {
        this.wtCityId = wtCityId;
        this.name = name;
    }

    public Integer getWtCityId() {
        return wtCityId;
    }

    public void setWtCityId(Integer wtCityId) {
        this.wtCityId = wtCityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Collection<WtAddress> getWtAddressCollection() {
        return wtAddressCollection;
    }

    public void setWtAddressCollection(Collection<WtAddress> wtAddressCollection) {
        this.wtAddressCollection = wtAddressCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wtCityId != null ? wtCityId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WtCity)) {
            return false;
        }
        WtCity other = (WtCity) object;
        if ((this.wtCityId == null && other.wtCityId != null) || (this.wtCityId != null && !this.wtCityId.equals(other.wtCityId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gei.entities.WtCity[ wtCityId=" + wtCityId + " ]";
    }

    public Collection<WtContact> getWtContactCollection() {
        return wtContactCollection;
    }

    public void setWtContactCollection(Collection<WtContact> wtContactCollection) {
        this.wtContactCollection = wtContactCollection;
    }
    
}
