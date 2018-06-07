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
@Table(name = "WT_CONTACT_PHONE")
@SequenceGenerator(name = "WtContactPhoneSeq",sequenceName = "WT_CONTACT_PHONE_PKSEQ",allocationSize = 1)
public class WtContactPhone extends AbstractEntity<WtContactPhone> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(generator = "WtContactPhoneSeq", strategy = GenerationType.SEQUENCE)
    @Column(name = "WT_CONTACT_PHONE_ID")
    private Integer wtContactPhoneId;
    @Column(name = "PHONE_TYPE")
    private String phoneType;
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @Column(name = "PHONE_EXTENSION")
    private String phoneExtension;
    @JoinColumn(name = "WT_CONTACT_ID", referencedColumnName = "WT_CONTACT_ID")
    @ManyToOne
    private WtContact wtContact;

    public WtContactPhone() {
    }

    public WtContactPhone(Integer wtContactPhoneId) {
        this.wtContactPhoneId = wtContactPhoneId;
    }

    public Integer getWtContactPhoneId() {
        return wtContactPhoneId;
    }

    public void setWtContactPhoneId(Integer wtContactPhoneId) {
        this.wtContactPhoneId = wtContactPhoneId;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneExtension() {
        return phoneExtension;
    }

    public void setPhoneExtension(String phoneExtension) {
        this.phoneExtension = phoneExtension;
    }

    public WtContact getWtContact() {
        return wtContact;
    }

    public void setWtContact(WtContact wtContact) {
        this.wtContact = wtContact;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wtContactPhoneId != null ? wtContactPhoneId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WtContactPhone)) {
            return false;
        }
        WtContactPhone other = (WtContactPhone) object;
        if ((this.wtContactPhoneId == null && other.wtContactPhoneId != null) || (this.wtContactPhoneId != null && !this.wtContactPhoneId.equals(other.wtContactPhoneId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gei.entities.WtContactPhone[ wtContactPhoneId=" + wtContactPhoneId + " ]";
    }
    
}
