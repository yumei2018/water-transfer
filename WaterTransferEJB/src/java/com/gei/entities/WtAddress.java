/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ymei
 */
@Entity
@Table(name = "WT_ADDRESS")
@SequenceGenerator(name = "WtAddressSeq",sequenceName = "WT_ADDRESS_PKSEQ",allocationSize = 1)
public class WtAddress extends AbstractEntity<WtAddress> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(generator = "WtAddressSeq", strategy = GenerationType.SEQUENCE)
    @Column(name = "WT_ADDRESS_ID")
    private Integer wtAddressId;
    @Basic(optional = false)
    @Column(name = "ADDRESS1")
    private String address1;
    @Column(name = "ADDRESS2")
    private String address2;
    @Column(name = "ZIPCODE")
    private Integer zipcode;
    @Column(name = "ZIP_EXT")
    private Integer zipExt;
    @JoinColumn(name = "WT_STATE_ID", referencedColumnName = "WT_STATE_ID")
    @ManyToOne
    private WtState wtState;
    @JoinColumn(name = "WT_COUNTY_ID", referencedColumnName = "WT_COUNTY_ID")
    @ManyToOne
    private WtCounty wtCounty;
    @JoinColumn(name = "WT_CITY_ID", referencedColumnName = "WT_CITY_ID")
    @ManyToOne
    private WtCity wtCity;

    public WtAddress() {
    }

    public WtAddress(Integer wtAddressId) {
        this.wtAddressId = wtAddressId;
    }

    public WtAddress(Integer wtAddressId, String address1) {
        this.wtAddressId = wtAddressId;
        this.address1 = address1;
    }

    public Integer getWtAddressId() {
        return wtAddressId;
    }

    public void setWtAddressId(Integer wtAddressId) {
        this.wtAddressId = wtAddressId;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public Integer getZipcode() {
        return zipcode;
    }

    public void setZipcode(Integer zipcode) {
        this.zipcode = zipcode;
    }

    public Integer getZipExt() {
        return zipExt;
    }

    public void setZipExt(Integer zipExt) {
        this.zipExt = zipExt;
    }

    public WtState getWtState() {
        return wtState;
    }

    public void setWtState(WtState wtState) {
        this.wtState = wtState;
    }

    public WtCounty getWtCounty() {
        return wtCounty;
    }

    public void setWtCounty(WtCounty wtCounty) {
        this.wtCounty = wtCounty;
    }

    public WtCity getWtCity() {
        return wtCity;
    }

    public void setWtCity(WtCity wtCity) {
        this.wtCity = wtCity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wtAddressId != null ? wtAddressId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WtAddress)) {
            return false;
        }
        WtAddress other = (WtAddress) object;
        if ((this.wtAddressId == null && other.wtAddressId != null) || (this.wtAddressId != null && !this.wtAddressId.equals(other.wtAddressId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gei.entities.WtAddress[ wtAddressId=" + wtAddressId + " ]";
    }
    
}
