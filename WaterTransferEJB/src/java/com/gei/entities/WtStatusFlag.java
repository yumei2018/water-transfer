/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "WT_STATUS_FLAG")
@SequenceGenerator(name = "WtStatusFlagSeq",sequenceName = "WT_STATUS_FLAG_PKSEQ",allocationSize = 1)
public class WtStatusFlag extends AbstractEntity<WtStatusFlag> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(generator = "WtStatusFlagSeq", strategy = GenerationType.SEQUENCE)
    @Column(name = "WT_STATUS_FLAG_ID")
    private Integer wtStatusFlagId;
    @Column(name = "STATUS_NAME")
    private String statusName;
    @Column(name = "STATUS_DESCRIPTION")
    private String statusDescription;
    @OneToMany(mappedBy = "wtStatusFlag")
    private Collection<WtTrans> wtTransCollection;

    public WtStatusFlag() {
    }

    public WtStatusFlag(Integer wtStatusFlagId) {
        this.wtStatusFlagId = wtStatusFlagId;
    }

    public WtStatusFlag(Integer wtStatusFlagId, String statusName) {
        this.wtStatusFlagId = wtStatusFlagId;
        this.statusName = statusName;
    }

    public Integer getWtStatusFlagId() {
        return wtStatusFlagId;
    }

    public void setWtStatusFlagId(Integer wtStatusFlagId) {
        this.wtStatusFlagId = wtStatusFlagId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    @XmlTransient
    public Collection<WtTrans> getWtTransCollection() {
        return wtTransCollection;
    }

    public void setWtTransCollection(Collection<WtTrans> wtTransCollection) {
        this.wtTransCollection = wtTransCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wtStatusFlagId != null ? wtStatusFlagId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WtStatusFlag)) {
            return false;
        }
        WtStatusFlag other = (WtStatusFlag) object;
        if ((this.wtStatusFlagId == null && other.wtStatusFlagId != null) || (this.wtStatusFlagId != null && !this.wtStatusFlagId.equals(other.wtStatusFlagId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gei.entities.WtStatusFlag[ wtStatusFlagId=" + wtStatusFlagId + " ]";
    }
    
}
