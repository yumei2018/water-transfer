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
@Table(name = "WT_TRANS_CEQA")
@NamedQueries({
    @NamedQuery(name = "WtTransCeqa.findAll", query = "SELECT w FROM WtTransCeqa w")})
@SequenceGenerator(name="WtTransCeqaSeq",sequenceName="WT_TRANS_CEQA_PKSEQ",allocationSize = 1)
public class WtTransCeqa extends AbstractEntity<WtTransCeqa> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "WT_TRANS_CEQA_ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "WtTransCeqaSeq")
    private Integer wtTransCeqaId;
    @Column(name = "CEQA_SUBMITTED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ceqaSubmittedDate;
    @Column(name = "CEQA_ADOPTED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ceqaAdoptedDate;
    @Column(name = "SCH_NUM")
    private String schNum;
    @JoinColumn(name = "WT_TRANS_ID", referencedColumnName = "WT_TRANS_ID")
    @OneToOne
    private WtTrans wtTrans;

    public WtTransCeqa() {
    }

    public WtTransCeqa(Integer wtTransCeqaId) {
        this.wtTransCeqaId = wtTransCeqaId;
    }

    public Integer getWtTransCeqaId() {
        return wtTransCeqaId;
    }

    public void setWtTransCeqaId(Integer wtTransCeqaId) {
        this.wtTransCeqaId = wtTransCeqaId;
    }

    public Date getCeqaSubmittedDate() {
        return ceqaSubmittedDate;
    }

    public void setCeqaSubmittedDate(Date ceqaSubmittedDate) {
        this.ceqaSubmittedDate = ceqaSubmittedDate;
    }

    public Date getCeqaAdoptedDate() {
        return ceqaAdoptedDate;
    }

    public void setCeqaAdoptedDate(Date ceqaAdoptedDate) {
        this.ceqaAdoptedDate = ceqaAdoptedDate;
    }

    public String getSchNum() {
      return schNum;
    }

    public void setSchNum(String schNum) {
      this.schNum = schNum;
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
        hash += (wtTransCeqaId != null ? wtTransCeqaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WtTransCeqa)) {
            return false;
        }
        WtTransCeqa other = (WtTransCeqa) object;
        if ((this.wtTransCeqaId == null && other.wtTransCeqaId != null) 
            || (this.wtTransCeqaId != null && !this.wtTransCeqaId.equals(other.wtTransCeqaId))) {
            return false;
        }
        
        if ((this.ceqaSubmittedDate == null && other.ceqaSubmittedDate != null) 
            || (this.ceqaSubmittedDate != null && this.ceqaSubmittedDate.compareTo(other.ceqaSubmittedDate)!=0)) {
            return false;
        }
        
        if ((this.ceqaAdoptedDate == null && other.ceqaAdoptedDate != null) 
            || (this.ceqaAdoptedDate != null && this.ceqaAdoptedDate.compareTo(other.ceqaAdoptedDate)!=0)) {
            return false;
        }
        
        if ((this.schNum == null && other.schNum != null) 
            || (this.schNum != null && !this.schNum.equals(other.schNum))) {
            return false;
        }
        
        return true;
    }

    @Override
    public String toString() {
        return "com.gei.entities.WtTransCeqa[ wtTransCeqaId=" + wtTransCeqaId + " ]";
    }
    
}
