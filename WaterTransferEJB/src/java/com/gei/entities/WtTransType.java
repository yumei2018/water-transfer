/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ymei
 */
@Entity
@Table(name = "WT_TRANS_TYPE")
@NamedQueries({
    @NamedQuery(name = "WtTransType.findAll", query = "SELECT w FROM WtTransType w")})
public class WtTransType implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected WtTransTypePK wtTransTypePK;
    @Column(name = "TYPE_VOLUME")
    private Double typeVolume;
    @Column(name = "TYPE_DETAIL")
    private String typeDetail;
    @JoinColumn(name = "WT_TRANS_ID", referencedColumnName = "WT_TRANS_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private WtTrans wtTrans;
    @JoinColumn(name = "WT_FU_TYPE_ID", referencedColumnName = "WT_FU_TYPE_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private WtFuType wtFuType;    

    public WtTransType() {
    }

    public WtTransType(WtTransTypePK wtTransTypePK) {
        this.wtTransTypePK = wtTransTypePK;
    }

    public WtTransType(Integer wtTransId, Integer wtFuTypeId) {
        this.wtTransTypePK = new WtTransTypePK(wtTransId, wtFuTypeId);
    }

    public WtTransTypePK getWtTransTypePK() {
        return wtTransTypePK;
    }

    public void setWtTransTypePK(WtTransTypePK wtTransTypePK) {
        this.wtTransTypePK = wtTransTypePK;
    }

    public Double getTypeVolume() {
        return typeVolume;
    }

    public void setTypeVolume(Double typeVolume) {
        this.typeVolume = typeVolume;
    }

    public String getTypeDetail() {
        return typeDetail;
    }

    public void setTypeDetail(String typeDetail) {
        this.typeDetail = typeDetail;
    }

    @XmlTransient
    public WtTrans getWtTrans() {
        return wtTrans;
    }

    public void setWtTrans(WtTrans wtTrans) {
        this.wtTrans = wtTrans;
    }

    public WtFuType getWtFuType() {
        return wtFuType;
    }

    public void setWtFuType(WtFuType wtFuType) {
        this.wtFuType = wtFuType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wtTransTypePK != null ? wtTransTypePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WtTransType)) {
            return false;
        }
        WtTransType other = (WtTransType) object;
        if ((this.wtTransTypePK == null && other.wtTransTypePK != null) 
              || (this.wtTransTypePK != null && !this.wtTransTypePK.equals(other.wtTransTypePK))) {
            return false;
        }
        
        if ((this.typeVolume == null && other.typeVolume != null) 
              || (this.typeVolume != null && !this.typeVolume.equals(other.typeVolume))) {
            return false;
        }
        
        return true;
    }

    @Override
    public String toString() {
        return "com.gei.entities.WtTransType[ wtTransTypePK=" + wtTransTypePK + " ]";
    }
    
    public boolean isExist(Collection<WtTransType> typeCollection){
        for(WtTransType type : typeCollection){
            if(type.wtTransTypePK.equals(this.wtTransTypePK)){
                return true;
            }
        }
        return false;
    }
}
