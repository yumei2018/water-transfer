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
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ymei
 */
@Entity
@Table(name = "WT_TRANS_REACH")
@NamedQueries({
    @NamedQuery(name = "WtTransReach.findAll", query = "SELECT w FROM WtTransReach w")})
@SequenceGenerator(name="WtTransReachSeq",sequenceName="WT_TRANS_REACH_PKSEQ",allocationSize = 1)
public class WtTransReach extends AbstractEntity<WtTransReach> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "WT_TRANS_REACH_ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "WtTransReachSeq")
    private Integer wtTransReachId;
    @Column(name = "POWER_PROVIDER")
    private String powerProvider;
    @Column(name = "IS_REACH_1")
    private Integer isReach1;
    @Column(name = "IS_REACH_2A")
    private Integer isReach2a;
    @Column(name = "IS_REACH_2B")
    private Integer isReach2b;
    @Column(name = "IS_REACH_3")
    private Integer isReach3;
    @Column(name = "IS_REACH_4")
    private Integer isReach4;
    @Column(name = "IS_REACH_5")
    private Integer isReach5;
    @Column(name = "IS_REACH_6")
    private Integer isReach6;
    @Column(name = "IS_REACH_7")
    private Integer isReach7;
    @JoinColumn(name = "WT_TRANS_ID", referencedColumnName = "WT_TRANS_ID")
    @OneToOne
    private WtTrans wtTrans;

    public WtTransReach() {
    }

    public WtTransReach(Integer wtTransReachId) {
        this.wtTransReachId = wtTransReachId;
    }

    public Integer getWtTransReachId() {
        return wtTransReachId;
    }

    public void setWtTransReachId(Integer wtTransReachId) {
        this.wtTransReachId = wtTransReachId;
    }

    public String getPowerProvider() {
        return powerProvider;
    }

    public void setPowerProvider(String powerProvider) {
        this.powerProvider = powerProvider;
    }

    public Integer getIsReach1() {
        return isReach1;
    }

    public void setIsReach1(Integer isReach1) {
        this.isReach1 = isReach1;
    }

    public Integer getIsReach2a() {
        return isReach2a;
    }

    public void setIsReach2a(Integer isReach2a) {
        this.isReach2a = isReach2a;
    }

    public Integer getIsReach2b() {
        return isReach2b;
    }

    public void setIsReach2b(Integer isReach2b) {
        this.isReach2b = isReach2b;
    }

    public Integer getIsReach3() {
        return isReach3;
    }

    public void setIsReach3(Integer isReach3) {
        this.isReach3 = isReach3;
    }

    public Integer getIsReach4() {
        return isReach4;
    }

    public void setIsReach4(Integer isReach4) {
        this.isReach4 = isReach4;
    }

    public Integer getIsReach5() {
        return isReach5;
    }

    public void setIsReach5(Integer isReach5) {
        this.isReach5 = isReach5;
    }

    public Integer getIsReach6() {
        return isReach6;
    }

    public void setIsReach6(Integer isReach6) {
        this.isReach6 = isReach6;
    }

    public Integer getIsReach7() {
        return isReach7;
    }

    public void setIsReach7(Integer isReach7) {
        this.isReach7 = isReach7;
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
        hash += (wtTransReachId != null ? wtTransReachId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WtTransReach)) {
            return false;
        }
        WtTransReach other = (WtTransReach) object;
        if ((this.wtTransReachId == null && other.wtTransReachId != null) 
                || (this.wtTransReachId != null && !this.wtTransReachId.equals(other.wtTransReachId))) {
            return false;
        }
        
        if ((this.powerProvider == null && other.powerProvider != null) 
                || (this.powerProvider != null && !this.powerProvider.equals(other.powerProvider))) {
            return false;
        }
        
        if ((this.isReach1 == null && other.isReach1 != null) 
                || (this.isReach1 != null && !this.isReach1.equals(other.isReach1))) {
            return false;
        }
        
        if ((this.isReach2a == null && other.isReach2a != null) 
                || (this.isReach2a != null && !this.isReach2a.equals(other.isReach2a))) {
            return false;
        }
        
        if ((this.isReach2b == null && other.isReach2b != null) 
                || (this.isReach2b != null && !this.isReach2b.equals(other.isReach2b))) {
            return false;
        }
        
        if ((this.isReach3 == null && other.isReach3 != null) 
                || (this.isReach3 != null && !this.isReach3.equals(other.isReach3))) {
            return false;
        }
        
        if ((this.isReach4 == null && other.isReach4 != null) 
                || (this.isReach4 != null && !this.isReach4.equals(other.isReach4))) {
            return false;
        }
        
        if ((this.isReach5 == null && other.isReach5 != null) 
                || (this.isReach5 != null && !this.isReach5.equals(other.isReach5))) {
            return false;
        }
        
        if ((this.isReach6 == null && other.isReach6 != null) 
                || (this.isReach6 != null && !this.isReach6.equals(other.isReach6))) {
            return false;
        }
        
        if ((this.isReach7 == null && other.isReach7 != null) 
                || (this.isReach7 != null && !this.isReach7.equals(other.isReach7))) {
            return false;
        }
        
        return true;
    }

    @Override
    public String toString() {
        return "com.gei.entities.WtTransReach[ wtTransReachId=" + wtTransReachId + " ]";
    }
    
}
