/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ymei
 */
@Embeddable
public class WtTransTypePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "WT_TRANS_ID")
    private Integer wtTransId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "WT_FU_TYPE_ID")
    private Integer wtFuTypeId;

    public WtTransTypePK() {
    }

    public WtTransTypePK(Integer wtTransId, Integer wtFuTypeId) {
        this.wtTransId = wtTransId;
        this.wtFuTypeId = wtFuTypeId;
    }

    public Integer getWtTransId() {
        return wtTransId;
    }

    public void setWtTransId(Integer wtTransId) {
        this.wtTransId = wtTransId;
    }

    public Integer getWtFuTypeId() {
        return wtFuTypeId;
    }

    public void setWtFuTypeId(Integer wtFuTypeId) {
        this.wtFuTypeId = wtFuTypeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) wtTransId;
        hash += (int) wtFuTypeId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WtTransTypePK)) {
            return false;
        }
        WtTransTypePK other = (WtTransTypePK) object;
        if (!Objects.equals(this.wtTransId,other.wtTransId)) {
            return false;
        }
        if (!Objects.equals(this.wtFuTypeId, other.wtFuTypeId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gei.entities.WtTransTypePK[ wtTransId=" + wtTransId + ", wtFuTypeId=" + wtFuTypeId + " ]";
    }
    
}
