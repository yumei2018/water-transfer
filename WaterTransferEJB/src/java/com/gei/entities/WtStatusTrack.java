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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ymei
 */
@Entity
@Table(name = "WT_STATUS_TRACK")
@SequenceGenerator(name = "WtStatusTrackSeq",sequenceName = "WT_STATUS_TRACK_PKSEQ",allocationSize = 1)
public class WtStatusTrack extends AbstractEntity<WtStatusTrack> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(generator = "WtStatusTrackSeq", strategy = GenerationType.SEQUENCE)
    @Column(name = "WT_STATUS_TRACK_ID")
    private Integer wtStatusTrackId;
    @Column(name = "WT_TRANS_ID")
    private Integer wtTransId;
    @Column(name = "STATUS_NAME")
    private String statusName;
    @Column(name = "STATUS_DESCRIPTION")
    private String statusDescription;
    @Column(name = "SUB_STATUS")
    private String subStatus;
    @Column(name = "USERNAME")
    private String username;    
    @Column(name = "STATUS_COMMENTS")
    private String statusComments;
    @Column(name = "STATUS_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date statusDate;
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

    public WtStatusTrack() {
    }

    public WtStatusTrack(Integer wtStatusTrackId) {
        this.wtStatusTrackId = wtStatusTrackId;
    }

    public Integer getWtStatusTrackId() {
        return wtStatusTrackId;
    }

    public void setWtStatusTrackId(Integer wtStatusTrackId) {
        this.wtStatusTrackId = wtStatusTrackId;
    }

    public Integer getWtTransId() {
        return wtTransId;
    }

    public void setWtTransId(Integer wtTransId) {
        this.wtTransId = wtTransId;
    }

    public String getStatusName() {
        return statusName;
    }
    
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }    

    public String getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(String subStatus) {
        this.subStatus = subStatus;
    }

    public String getStatusComments() {
        return statusComments;
    }

    public void setStatusComments(String statusComments) {
        this.statusComments = statusComments;
    }

    public Date getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
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
        hash += (wtStatusTrackId != null ? wtStatusTrackId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WtStatusTrack)) {
            return false;
        }
        WtStatusTrack other = (WtStatusTrack) object;
        if ((this.wtStatusTrackId == null && other.wtStatusTrackId != null) || (this.wtStatusTrackId != null && !this.wtStatusTrackId.equals(other.wtStatusTrackId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gei.entities.WtStatusTrack[ wtStatusTrackId=" + wtStatusTrackId + " ]";
    }
    
}
