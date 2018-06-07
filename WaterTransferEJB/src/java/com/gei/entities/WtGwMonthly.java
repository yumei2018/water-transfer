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
@Table(name = "WT_GW_MONTHLY")
@SequenceGenerator(name="WtGwMonthlySeq",sequenceName="WT_GW_MONTHLY_PKSEQ",allocationSize = 1)
public class WtGwMonthly extends AbstractEntity<WtGwMonthly> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "WT_GW_MONTHLY_ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "WtGwMonthlySeq")
    private Integer wtGwMonthlyId;
    @Column(name = "GW_MONTH")
    private Integer gwMonth;
    @Column(name = "PROPOSED_PUMPING")
    private Double proposedPumping;
    @Column(name = "BASELINE_PUMPING")
    private Double baselinePumping;
    @Column(name = "GROSS_TRANS_PUMPING")
    private Double grossTransPumping;
    @Column(name = "STREAM_DEPLETION")
    private Double streamDepletion;
    @Column(name = "NET_TRANS_WATER")
    private Double netTransWater;
    @Column(name = "MEASUREMENT_DATE")
    private String measurementDate;
    @JoinColumn(name = "WT_GROUNDWATER_ID", referencedColumnName = "WT_GROUNDWATER_ID")
    @ManyToOne
    private WtGroundwater wtGroundwater;

    public WtGwMonthly() {
    }

    public WtGwMonthly(Integer wtGwMonthlyId) {
        this.wtGwMonthlyId = wtGwMonthlyId;
    }

    public Integer getWtGwMonthlyId() {
        return wtGwMonthlyId;
    }

    public void setWtGwMonthlyId(Integer wtGwMonthlyId) {
        this.wtGwMonthlyId = wtGwMonthlyId;
    }

    public Integer getGwMonth() {
        return gwMonth;
    }

    public void setGwMonth(Integer gwMonth) {
        this.gwMonth = gwMonth;
    }

    public Double getProposedPumping() {
        return proposedPumping;
    }

    public void setProposedPumping(Double proposedPumping) {
        this.proposedPumping = proposedPumping;
    }

    public Double getBaselinePumping() {
        return baselinePumping;
    }

    public void setBaselinePumping(Double baselinePumping) {
        this.baselinePumping = baselinePumping;
    }

    public Double getStreamDepletion() {
        return streamDepletion;
    }

    public void setStreamDepletion(Double streamDepletion) {
        this.streamDepletion = streamDepletion;
    }

    public Double getGrossTransPumping() {
        return grossTransPumping;
    }

    public void setGrossTransPumping(Double grossTransPumping) {
        this.grossTransPumping = grossTransPumping;
    }

    public Double getNetTransWater() {
        return netTransWater;
    }

    public void setNetTransWater(Double netTransWater) {
        this.netTransWater = netTransWater;
    }

    public String getMeasurementDate() {
        return measurementDate;
    }

    public void setMeasurementDate(String measurementDate) {
        this.measurementDate = measurementDate;
    }

    public WtGroundwater getWtGroundwater() {
        return wtGroundwater;
    }

    public void setWtGroundwater(WtGroundwater wtGroundwaterId) {
        this.wtGroundwater = wtGroundwaterId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wtGwMonthlyId != null ? wtGwMonthlyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WtGwMonthly)) {
            return false;
        }
        WtGwMonthly other = (WtGwMonthly) object;
        if ((this.wtGwMonthlyId == null && other.wtGwMonthlyId != null) || (this.wtGwMonthlyId != null && !this.wtGwMonthlyId.equals(other.wtGwMonthlyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gei.entities.WtGwMonthly[ wtGwMonthlyId=" + wtGwMonthlyId + " ]";
    }
    
}
