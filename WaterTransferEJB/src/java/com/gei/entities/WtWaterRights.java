/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ymei
 */
@Entity
@Table(name = "WT_WATER_RIGHTS")
@SequenceGenerator(name="WtWaterRightsSeq",sequenceName="WT_WATER_RIGHTS_PKSEQ",allocationSize = 1)
public class WtWaterRights extends AbstractEntity<WtWaterRights> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "WT_WATER_RIGHTS_ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "WtWaterRightsSeq")
    private Integer wtWaterRightsId;
    @Column(name = "WATER_RIGHTS_TYPE")
    private String waterRightsType;
    @Column(name = "WATER_RIGHTS_NUM")
    private String waterRightsNum;
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "CREATED_BY_ID")
    private Integer createdById;
    @Column(name = "UPDATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @Column(name = "UPDATED_BY_ID")
    private Integer updatedById;
    @Column(name = "PROPOSED_TRANS_VOL")
    private Double proposedTransVol;
    @JoinColumn(name = "WT_TRANS_ID", referencedColumnName = "WT_TRANS_ID")
    @ManyToOne(optional = false)
    private WtTrans wtTrans;

    public WtWaterRights() {
    }

    public WtWaterRights(Integer wtWaterRightsId) {
        this.wtWaterRightsId = wtWaterRightsId;
    }

    public Integer getWtWaterRightsId() {
        return wtWaterRightsId;
    }

    public void setWtWaterRightsId(Integer wtWaterRightsId) {
        this.wtWaterRightsId = wtWaterRightsId;
    }

    public String getWaterRightsType() {
        return waterRightsType;
    }

    public void setWaterRightsType(String waterRightsType) {
        this.waterRightsType = waterRightsType;
    }

    public String getWaterRightsNum() {
        return waterRightsNum;
    }

    public void setWaterRightsNum(String waterRightsNum) {
        this.waterRightsNum = waterRightsNum;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Integer createdById) {
        this.createdById = createdById;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getUpdatedById() {
        return updatedById;
    }

    public void setUpdatedById(Integer updatedById) {
        this.updatedById = updatedById;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wtWaterRightsId != null ? wtWaterRightsId.hashCode() : 0);
        return hash;
    }

    public Double getProposedTransVol() {
        return proposedTransVol;
    }

    public void setProposedTransVol(Double proposedTransVol) {
        this.proposedTransVol = proposedTransVol;
    }

//    @XmlTransient
    public WtTrans getWtTrans() {
        return wtTrans;
    }

    public void setWtTrans(WtTrans wtTrans) {
        this.wtTrans = wtTrans;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WtWaterRights)) {
            return false;
        }
        WtWaterRights other = (WtWaterRights) object;
        if ((this.wtWaterRightsId == null && other.wtWaterRightsId != null) || (this.wtWaterRightsId != null && !this.wtWaterRightsId.equals(other.wtWaterRightsId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gei.entities.WtWaterRights[ wtWaterRightsId=" + wtWaterRightsId + " ]";
    }

}
