/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ymei
 */
@Entity
@Table(name = "WT_CONTACT")
@SequenceGenerator(name = "WtContactSeq",sequenceName = "WT_CONTACT_PKSEQ",allocationSize = 1)
public class WtContact extends AbstractEntity<WtContact> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(generator = "WtContactSeq", strategy = GenerationType.SEQUENCE)
    @Column(name = "WT_CONTACT_ID")
    private Integer wtContactId;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "MIDDLE_NAME")
    private String middleName;
    @Column(name = "FIRST_NAME")
    private String firstName;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "IS_ACTIVE")
    private Integer isActive;
    @Column(name = "PHONE_TYPE")
    private String phoneType;
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @Column(name = "ADDRESS1")
    private String address1;
    @Column(name = "ADDRESS2")
    private String address2;
    @Column(name = "ZIPCODE")
    private String zipcode;
    @Column(name = "CITY_NAME")
    private String cityName;
    @Column(name = "IS_BUYERS_CONTACT")
    private Integer isBuyersContact;
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
    @JoinColumn(name = "WT_STATE_ID", referencedColumnName = "WT_STATE_ID")
    @ManyToOne
    private WtState wtState;
    @JoinColumn(name = "WT_CITY_ID", referencedColumnName = "WT_CITY_ID")
    @ManyToOne
    private WtCity wtCity;
    @OneToOne(mappedBy = "wtContact")
    private AppUser appUser;
    @JoinColumn(name = "WT_AGENCY_ID", referencedColumnName = "WT_AGENCY_ID")
    @ManyToOne
    private WtAgency wtAgency;
//    @OneToMany(mappedBy = "wtBuyersContact")
//    private Collection<WtTrans> wtTransCollection;
    @ManyToMany(mappedBy = "buyersContactCollection")
    private Collection<WtTrans> wtTransCollection;
//    @OneToMany(mappedBy = "wtContact")
//    private Collection<WtContactPhone> wtContactPhoneCollection;

    public WtContact() {
    }

    public WtContact(Integer wtContactId) {
        this.wtContactId = wtContactId;
    }

    public Integer getWtContactId() {
        return wtContactId;
    }

    public void setWtContactId(Integer wtContactId) {
        this.wtContactId = wtContactId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
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

    public WtAgency getWtAgency() {
        return wtAgency;
    }

    public void setWtAgency(WtAgency wtAgency) {
        this.wtAgency = wtAgency;
    }

//    public Collection<WtContactPhone> getWtContactPhoneCollection() {
//        return wtContactPhoneCollection;
//    }
//
//    public void setWtContactPhoneCollection(Collection<WtContactPhone> wtContactPhoneCollection) {
//        this.wtContactPhoneCollection = wtContactPhoneCollection;
//    }

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

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public WtState getWtState() {
        return wtState;
    }

    public void setWtState(WtState wtState) {
        this.wtState = wtState;
    }

    public WtCity getWtCity() {
        return wtCity;
    }

    public void setWtCity(WtCity wtCity) {
        this.wtCity = wtCity;
    }

    public AppUser getUser() {
        return appUser;
    }

    public void setUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Integer getIsBuyersContact() {
      return isBuyersContact;
    }

    public void setIsBuyersContact(Integer isBuyersContact) {
      this.isBuyersContact = isBuyersContact;
    }

    public Collection<WtTrans> getWtTransCollection() {
      return wtTransCollection;
    }

    public void setWtTransCollection(Collection<WtTrans> wtTransCollection) {
      this.wtTransCollection = wtTransCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wtContactId != null ? wtContactId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WtContact)) {
            return false;
        }
        WtContact other = (WtContact) object;
        if ((this.wtContactId == null && other.wtContactId != null) || (this.wtContactId != null && !this.wtContactId.equals(other.wtContactId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gei.entities.WtContact[ wtContactId=" + wtContactId + " ]";
    }
    
    public boolean hasAccount(){
        if(getUser() != null){
            return true;
        }
        return false;
    }
}
