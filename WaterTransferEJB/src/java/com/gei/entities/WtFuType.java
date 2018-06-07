/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ymei
 */
@Entity
@Table(name = "WT_FU_TYPE")
@SequenceGenerator(name = "WtFuTypeSeq",sequenceName = "WT_FU_TYPE_PKSEQ",allocationSize = 1)
public class WtFuType extends AbstractEntity<WtFuType> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(generator = "WtFuTypeSeq", strategy = GenerationType.SEQUENCE)
    @Column(name = "WT_FU_TYPE_ID")
    private Integer wtFuTypeId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FU_TYPE")
    private String fuType;
    @Column(name = "FU_SUB_TYPE")
    private String fuSubType;
    @Column(name = "FU_TYPE_DESCRIPTION")
    private String fuTypeDescription;
//    @ManyToMany(mappedBy = "wtFuTypeCollection",cascade = CascadeType.PERSIST,fetch=FetchType.LAZY)
//    private Collection<WtTrans> wtTransCollection;
    @OneToMany(mappedBy = "wtFuType",cascade = CascadeType.PERSIST,fetch=FetchType.LAZY)
    private Collection<WtTransType> wtTransTypeCollection;

    public WtFuType() {
    }

    public WtFuType(Integer wtFuTypeId) {
        this.wtFuTypeId = wtFuTypeId;
    }

    public WtFuType(Integer wtFuTypeId, String fuType) {
        this.wtFuTypeId = wtFuTypeId;
        this.fuType = fuType;
    }

    public Integer getWtFuTypeId() {
        return wtFuTypeId;
    }

    public void setWtFuTypeId(Integer wtFuTypeId) {
        this.wtFuTypeId = wtFuTypeId;
    }

    public String getFuType() {
        return fuType;
    }

    public void setFuType(String fuType) {
        this.fuType = fuType;
    }
    
    public String getFuSubType() {
        return fuSubType;
    }
    
    public void setFuSubType(String fuSubType) {
        this.fuSubType = fuSubType;
    }

    public String getFuTypeDescription() {
        return fuTypeDescription;
    }

    public void setFuTypeDescription(String fuTypeDescription) {
        this.fuTypeDescription = fuTypeDescription;
    }

//    @XmlTransient
//    public Collection<WtTrans> getWtTransCollection() {
//        return wtTransCollection;
//    }
//
//    public void setWtTransCollection(Collection<WtTrans> wtTransCollection) {
//        this.wtTransCollection = wtTransCollection;
//    }

    @XmlTransient
    public Collection<WtTransType> getWtTransTypeCollection() {
        return wtTransTypeCollection;
    }

    public void setWtTransTypeCollection(Collection<WtTransType> wtTransTypeCollection) {
        this.wtTransTypeCollection = wtTransTypeCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wtFuTypeId != null ? wtFuTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WtFuType)) {
            return false;
        }
        WtFuType other = (WtFuType) object;
        if ((this.wtFuTypeId == null && other.wtFuTypeId != null) || (this.wtFuTypeId != null && !this.wtFuTypeId.equals(other.wtFuTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gei.entities.WtFuType[ wtFuTypeId=" + wtFuTypeId + " ]";
    }
    
}
