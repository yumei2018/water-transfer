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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
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
@Table(name = "WT_TRANS_SWRCB")
@NamedQueries({
    @NamedQuery(name = "WtTransSwrcb.findAll", query = "SELECT w FROM WtTransSwrcb w")})
@SequenceGenerator(name="WtTransSwrcbSeq",sequenceName="WT_TRANS_SWRCB_PKSEQ",allocationSize = 1)
public class WtTransSwrcb extends AbstractEntity<WtTransSwrcb> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "WT_TRANS_SWRCB_ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "WtTransSwrcbSeq")
    private Integer wtTransSwrcbId;
    @Column(name = "SWRCB_PETITION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date swrcbPetitionDate;
    @Column(name = "SWRCB_ORDER_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date swrcbOrderDate;
    @JoinColumn(name = "WT_TRANS_ID", referencedColumnName = "WT_TRANS_ID")
    @OneToOne
    private WtTrans wtTrans;

    public WtTransSwrcb() {
    }

    public WtTransSwrcb(Integer wtTransSwrcbId) {
        this.wtTransSwrcbId = wtTransSwrcbId;
    }

    public Integer getWtTransSwrcbId() {
        return wtTransSwrcbId;
    }

    public void setWtTransSwrcbId(Integer wtTransSwrcbId) {
        this.wtTransSwrcbId = wtTransSwrcbId;
    }

    public Date getSwrcbPetitionDate() {
        return swrcbPetitionDate;
    }

    public void setSwrcbPetitionDate(Date swrcbPetitionDate) {
        this.swrcbPetitionDate = swrcbPetitionDate;
    }

    public Date getSwrcbOrderDate() {
        return swrcbOrderDate;
    }

    public void setSwrcbOrderDate(Date swrcbOrderDate) {
        this.swrcbOrderDate = swrcbOrderDate;
    }

    @XmlTransient
    public WtTrans getWtTrans() {
        return wtTrans;
    }

    public void setWtTrans(WtTrans wtTrans) {
        this.wtTrans = wtTrans;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wtTransSwrcbId != null ? wtTransSwrcbId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WtTransSwrcb)) {
            return false;
        }
        WtTransSwrcb other = (WtTransSwrcb) object;
        if ((this.wtTransSwrcbId == null && other.wtTransSwrcbId != null) || (this.wtTransSwrcbId != null && !this.wtTransSwrcbId.equals(other.wtTransSwrcbId))) {
            return false;
        }
        
        if ((this.swrcbPetitionDate == null && other.swrcbPetitionDate != null) 
            || (this.swrcbPetitionDate != null && this.swrcbPetitionDate.compareTo(other.swrcbPetitionDate)!=0)) {
            return false;
        }
        
        if ((this.swrcbOrderDate == null && other.swrcbOrderDate != null) 
            || (this.swrcbOrderDate != null && this.swrcbOrderDate.compareTo(other.swrcbOrderDate)!=0)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gei.entities.WtTransSwrcb[ wtTransSwrcbId=" + wtTransSwrcbId + " ]";
    }
    
}
