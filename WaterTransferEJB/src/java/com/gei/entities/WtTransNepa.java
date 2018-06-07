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
@Table(name = "WT_TRANS_NEPA")
@NamedQueries({
    @NamedQuery(name = "WtTransNepa.findAll", query = "SELECT w FROM WtTransNepa w")})
@SequenceGenerator(name="WtTransNepaSeq",sequenceName="WT_TRANS_NEPA_PKSEQ",allocationSize = 1)
public class WtTransNepa extends AbstractEntity<WtTransNepa> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "WT_TRANS_NEPA_ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "WtTransNepaSeq")
    private Integer wtTransNepaId;
    @Column(name = "NEPA_ISSUED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date nepaIssuedDate;
    @Column(name = "NEPA_ADOPTED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date nepaAdoptedDate;
    @Column(name = "NEPA_NUM")
    private String nepaNum;
    @JoinColumn(name = "WT_TRANS_ID", referencedColumnName = "WT_TRANS_ID")
    @OneToOne
    private WtTrans wtTrans;

    public WtTransNepa() {
    }

    public WtTransNepa(Integer wtTransNepaId) {
        this.wtTransNepaId = wtTransNepaId;
    }

    public Integer getWtTransNepaId() {
        return wtTransNepaId;
    }

    public void setWtTransNepaId(Integer wtTransNepaId) {
        this.wtTransNepaId = wtTransNepaId;
    }

    public Date getNepaIssuedDate() {
        return nepaIssuedDate;
    }

    public void setNepaIssuedDate(Date nepaIssuedDate) {
        this.nepaIssuedDate = nepaIssuedDate;
    }

    public Date getNepaAdoptedDate() {
        return nepaAdoptedDate;
    }

    public void setNepaAdoptedDate(Date nepaAdoptedDate) {
        this.nepaAdoptedDate = nepaAdoptedDate;
    }

    public String getNepaNum() {
      return nepaNum;
    }

    public void setNepaNum(String nepaNum) {
      this.nepaNum = nepaNum;
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
        hash += (wtTransNepaId != null ? wtTransNepaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WtTransNepa)) {
            return false;
        }
        WtTransNepa other = (WtTransNepa) object;
        if ((this.wtTransNepaId == null && other.wtTransNepaId != null) 
            || (this.wtTransNepaId != null && !this.wtTransNepaId.equals(other.wtTransNepaId))) {
            return false;
        }
        
        if ((this.nepaIssuedDate == null && other.nepaIssuedDate != null) 
            || (this.nepaIssuedDate != null && this.nepaIssuedDate.compareTo(other.nepaIssuedDate)!=0)) {
            return false;
        }
        
        if ((this.nepaAdoptedDate == null && other.nepaAdoptedDate != null) 
            || (this.nepaAdoptedDate != null && this.nepaAdoptedDate.compareTo(other.nepaAdoptedDate)!=0)) {
            return false;
        }
        
        if ((this.nepaNum == null && other.nepaNum != null) 
            || (this.nepaNum != null && !this.nepaNum.equals(other.nepaNum))) {
            return false;
        }
        
        return true;
    }

    @Override
    public String toString() {
        return "com.gei.entities.WtTransNepa[ wtTransNepaId=" + wtTransNepaId + " ]";
    }
    
}
