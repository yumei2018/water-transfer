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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ymei
 */
@Entity
@Table(name = "WT_AGENCY_APPROVAL")
@NamedQueries({
    @NamedQuery(name = "WtAgencyApproval.findAll", query = "SELECT w FROM WtAgencyApproval w")})
@SequenceGenerator(name="WtAgencyApprovalSeq",sequenceName="WT_WATER_RIGHTS_PKSEQ",allocationSize = 1)
public class WtAgencyApproval extends AbstractEntity<WtAgencyApproval> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "WT_AGENCY_APPROVAL_ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "WtAgencyApprovalSeq")
    private Integer wtAgencyApprovalId;
    @Column(name = "DWR")
    private Integer dwr;
    @Column(name = "SWRCB")
    private Integer swrcb;
    @Column(name = "USBR")
    private Integer usbr;
    @Column(name = "FISHERY")
    private Integer fishery;
    @Column(name = "OTHER")
    private Integer other;
    @Column(name = "OTHER_DETAIL")
    private String otherDetail;
    @JoinColumn(name = "WT_TRANS_ID", referencedColumnName = "WT_TRANS_ID")
    @OneToOne
    private WtTrans wtTrans;

    public WtAgencyApproval() {
    }

    public WtAgencyApproval(Integer wtAgencyApprovalId) {
        this.wtAgencyApprovalId = wtAgencyApprovalId;
    }

    public Integer getWtAgencyApprovalId() {
        return wtAgencyApprovalId;
    }

    public void setWtAgencyApprovalId(Integer wtAgencyApprovalId) {
        this.wtAgencyApprovalId = wtAgencyApprovalId;
    }

    public Integer getDwr() {
        return dwr;
    }

    public void setDwr(Integer dwr) {
        this.dwr = dwr;
    }

    public Integer getSwrcb() {
        return swrcb;
    }

    public void setSwrcb(Integer swrcb) {
        this.swrcb = swrcb;
    }

    public Integer getUsbr() {
        return usbr;
    }

    public void setUsbr(Integer usbr) {
        this.usbr = usbr;
    }

    public Integer getFishery() {
        return fishery;
    }

    public void setFishery(Integer fishery) {
        this.fishery = fishery;
    }

    public Integer getOther() {
        return other;
    }

    public void setOther(Integer other) {
        this.other = other;
    }

    public String getOtherDetail() {
        return otherDetail;
    }

    public void setOtherDetail(String otherDetail) {
        this.otherDetail = otherDetail;
    }


    public WtTrans getWtTrans() {
        return wtTrans;
    }

    public void setWtTrans(WtTrans wtTrans) {
        this.wtTrans = wtTrans;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wtAgencyApprovalId != null ? wtAgencyApprovalId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WtAgencyApproval)) {
            return false;
        }
        WtAgencyApproval other = (WtAgencyApproval) object;
        if ((this.wtAgencyApprovalId == null && other.wtAgencyApprovalId != null) || (this.wtAgencyApprovalId != null && !this.wtAgencyApprovalId.equals(other.wtAgencyApprovalId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gei.entities.WtAgencyApproval[ wtAgencyApprovalId=" + wtAgencyApprovalId + " ]";
    }
    
}
