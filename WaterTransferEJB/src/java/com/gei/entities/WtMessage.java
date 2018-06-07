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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author pheng
 */
@Entity
@Table(name = "WT_MESSAGE")
@SequenceGenerator(name = "WtMessageSeq",sequenceName = "WT_MESSAGE_PKSEQ",allocationSize = 1)
@NamedQueries({
    @NamedQuery(name = "WtMessage.findAll", query = "SELECT w FROM WtMessage w")})
public class WtMessage extends AbstractEntity<WtMessage>implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(generator = "WtMessageSeq", strategy = GenerationType.SEQUENCE)
    @Column(name = "MSG_ID", nullable = false)
    private Integer msgId;
    @Column(name = "WT_TRANS_ID")
    private Integer wtTransId;
    @Column(name = "WT_CONTACT_ID")
    private Integer wtContactId;
    @Column(name = "CREATED_BY")
    private Integer createdBy;
    @Column(name = "WT_AGENCY_ID")
    private Integer wtAgencyId;
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "MESSAGE", length = 4000)
    private String message;
    @Column(name = "SUBJECT", length = 100)
    private String subject;
    public WtMessage() {
    }

    public WtMessage(Integer msgId) {
        this.msgId = msgId;
    }

    public Integer getMsgId() {
        return msgId;
    }

    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }

    public Integer getWtTransId() {
        return wtTransId;
    }

    public void setWtTransId(Integer wtTransId) {
        this.wtTransId = wtTransId;
    }
    public Integer getWtAgencyId() {
        return wtAgencyId;
    }

    public void setWtAgencyId(Integer wtAgencyId) {
        this.wtAgencyId = wtAgencyId;
    }

    public Integer getWtContactId() {
        return wtContactId;
    }

    public void setWtContactId(Integer wtContactId) {
        this.wtContactId = wtContactId;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (msgId != null ? msgId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WtMessage)) {
            return false;
        }
        WtMessage other = (WtMessage) object;
        if ((this.msgId == null && other.msgId != null) || (this.msgId != null && !this.msgId.equals(other.msgId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gei.entities.WtMessage[ msgId=" + msgId + " ]";
    }

}
