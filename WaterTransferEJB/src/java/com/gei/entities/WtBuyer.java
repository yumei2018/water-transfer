/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author ymei
 */
@Entity
@Table(name = "WT_BUYER")
@NamedQueries({
    @NamedQuery(name = "WtBuyer.findAll", query = "SELECT w FROM WtBuyer w")})
public class WtBuyer implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected WtBuyerPK wtBuyerPK;
    @Column(name = "SHARE_PERCENT")
    private Double sharePercent;
    @JoinColumn(name = "WT_TRANS_ID", referencedColumnName = "WT_TRANS_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private WtTrans wtTrans;
    @JoinColumn(name = "WT_AGENCY_ID", referencedColumnName = "WT_AGENCY_ID", insertable = false, updatable = false)
    @ManyToOne
    private WtAgency wtAgency;

    public WtBuyer() {
    }

    public WtBuyer(WtBuyerPK wtBuyerPK) {
        this.wtBuyerPK = wtBuyerPK;
    }

    public WtBuyer(Integer wtTransId, Integer wtAgencyId) {
        this.wtBuyerPK = new WtBuyerPK(wtTransId, wtAgencyId);
    }

    public WtBuyerPK getWtBuyerPK() {
        return wtBuyerPK;
    }

    public void setWtBuyerPK(WtBuyerPK wtBuyerPK) {
        this.wtBuyerPK = wtBuyerPK;
    }

    public Double getSharePercent() {
        return sharePercent;
    }

    public void setSharePercent(Double sharePercent) {
        this.sharePercent = sharePercent;
    }

    public WtTrans getWtTrans() {
        return wtTrans;
    }

    public void setWtTrans(WtTrans wtTrans) {
        this.wtTrans = wtTrans;
    }

    public WtAgency getWtAgency() {
        return wtAgency;
    }

    public void setWtAgency(WtAgency wtAgency) {
        this.wtAgency = wtAgency;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wtBuyerPK != null ? wtBuyerPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WtBuyer)) {
            return false;
        }
        WtBuyer other = (WtBuyer) object;
        if ((this.wtBuyerPK == null && other.wtBuyerPK != null) || (this.wtBuyerPK != null && !this.wtBuyerPK.equals(other.wtBuyerPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gei.entities.WtBuyer[ wtBuyerPK=" + wtBuyerPK + " ]";
    }
    
//    public boolean hasRepUser(int userId) {
//      if (wtAgency ==  null){
//        return false;
//      }
//      if (wtAgency.hasRepUser(userId)){
//        return true;
//      }
//      
//      return false;
//    }    
}
