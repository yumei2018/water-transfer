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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "WT_CHANGE_LOG")
@NamedQueries({
    @NamedQuery(name = "WtChangeLog.findAll", query = "SELECT w FROM WtChangeLog w")})
@SequenceGenerator(name = "WtChangeLogSeq",sequenceName = "WT_CHANGE_LOG_PKSEQ",allocationSize = 1)
public class WtChangeLog extends AbstractEntity<WtChangeLog> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(generator = "WtChangeLogSeq", strategy = GenerationType.SEQUENCE)
    @Column(name = "WT_CHANGE_LOG_ID")
    private Integer wtChangeLogId;
    @Column(name = "WT_TRANS_ID")
    private Integer wtTransId;
    @Column(name = "CHANGE_USER")
    private String changeUser;
    @Lob
    @Column(name = "CHANGE_LOG")
    private String changeLog;
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "CREATED_BY_ID")
    private Integer createdById;
    @Column(name = "CHANGE_FIELD")
    private String changeField;

    public WtChangeLog() {
    }

    public WtChangeLog(Integer wtChangeLogId) {
        this.wtChangeLogId = wtChangeLogId;
    }

    public Integer getWtChangeLogId() {
        return wtChangeLogId;
    }

    public void setWtChangeLogId(Integer wtChangeLogId) {
        this.wtChangeLogId = wtChangeLogId;
    }

    public Integer getWtTransId() {
        return wtTransId;
    }

    public void setWtTransId(Integer wtTransId) {
        this.wtTransId = wtTransId;
    }

    public String getChangeUser() {
        return changeUser;
    }

    public void setChangeUser(String changeUser) {
        this.changeUser = changeUser;
    }

    public String getChangeLog() {
        return changeLog;
    }

    public void setChangeLog(String changeLog) {
        this.changeLog = changeLog;
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
    public void setChangeField(String changeField) {
        this.changeField = changeField;
    }

    public String getChangeField() {
        return changeField;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wtChangeLogId != null ? wtChangeLogId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WtChangeLog)) {
            return false;
        }
        WtChangeLog other = (WtChangeLog) object;
        if ((this.wtChangeLogId == null && other.wtChangeLogId != null) || (this.wtChangeLogId != null && !this.wtChangeLogId.equals(other.wtChangeLogId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gei.entities.WtChangeLog[ wtChangeLogId=" + wtChangeLogId + " ]";
    }

}
