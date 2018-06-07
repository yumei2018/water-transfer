/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ymei
 */
@Embeddable
public class WtBuyerPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "WT_TRANS_ID")
    private Integer wtTransId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "WT_AGENCY_ID")
    private Integer wtAgencyId;

    public WtBuyerPK() {
    }

    public WtBuyerPK(Integer wtTransId, Integer wtAgencyId) {
        this.wtTransId = wtTransId;
        this.wtAgencyId = wtAgencyId;
    }

    public Integer getWtTransId() {
        return wtTransId;
    }

    public void setWtTransId(Integer wtTransId) {
        this.wtTransId = wtTransId;
    }

    public Integer getWtAgencyId() {
        return wtAgencyId;
    }

    public void setWtAgencyId(Integer wtAgencyId) {
        this.wtAgencyId = wtAgencyId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) wtTransId;
        hash += (int) wtAgencyId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WtBuyerPK)) {
            return false;
        }
        WtBuyerPK other = (WtBuyerPK) object;
        if (this.wtTransId != other.wtTransId) {
            return false;
        }
        if (this.wtAgencyId != other.wtAgencyId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gei.entities.WtBuyerPK[ wtTransId=" + wtTransId + ", wtAgencyId=" + wtAgencyId + " ]";
    }
    
}
