/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 *
 * @author ymei
 */
@Entity
@Table(name = "APP_USER")
@SequenceGenerator(name = "PKSEQ", sequenceName = "USER_PKSEQ", allocationSize = 1)
public class AppUser extends AbstractEntity<AppUser> implements Serializable {
  private static final long serialVersionUID = 1L;
  private static final String ROLE_APP_ACCOUNT = "APP_ACCT";
  private static final String ROLE_REVIEWER = "DWR_REVIEWER";
  private static final String ROLE_MANAGER = "DWR_MANAGER";
  private static final String ROLE_SYS_ADMIN = "SYS_ADMIN";
  private static final String ROLE_SUPER_ADMIN = "SUPER_ADMIN";
  private static final String ROLE_BUYER_REP = "BUYER_REP";
  private static final String ROLE_USBR = "USBR";

  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "USER_ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PKSEQ")
  private Integer userId;
  @Basic(optional = false)
  @NotNull
  @Column(name = "USERNAME")
  private String username;
  @Basic(optional = false)
  @NotNull
  @Column(name = "PASSWORD")
  private String password;
  @Column(name = "ACTIVE")
  private Integer active;
  @Column(name = "CREATED_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;
  @Column(name = "UPDATED_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date updatedDate;
  @Column(name = "NEED_PASSWORD_RESET")
  private Integer needPasswordReset;

  @JoinTable(name = "USER_GROUP", joinColumns = {
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")}, inverseJoinColumns = {
    @JoinColumn(name = "GROUP_ID", referencedColumnName = "GROUP_ID")})
  @ManyToMany(fetch = FetchType.EAGER)
  private Collection<AppGroup> appGroupCollection;
//  @JoinTable(name = "WT_REP_AGENCY", joinColumns = {
//    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")}, inverseJoinColumns = {
//    @JoinColumn(name = "WT_AGENCY_ID", referencedColumnName = "WT_AGENCY_ID")})
//  @ManyToMany(cascade = CascadeType.ALL)
//  private Collection<WtAgency> repAgencyCollection;

//  @ManyToMany(mappedBy = "repUserCollection", fetch = FetchType.EAGER)
//  @Fetch(value = FetchMode.SUBSELECT)
//  private Collection<WtAgency> repAgencyCollection;
  @OneToOne(cascade = CascadeType.ALL, mappedBy = "appUser")
  private UserProfile userProfile;
  @ManyToMany(mappedBy = "appUserCollection", fetch = FetchType.EAGER)
  @Fetch(value = FetchMode.SUBSELECT)
  private Collection<WtTrans> wtTransCollection;
  @JoinColumn(name = "WT_CONTACT_ID", referencedColumnName = "WT_CONTACT_ID")
  @OneToOne
  private WtContact wtContact;

  public AppUser() {
  }

  public AppUser(Integer userId) {
    this.userId = userId;
  }

  public AppUser(Integer userId, String username, String password) {
    this.userId = userId;
    this.username = username;
    this.password = password;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Date getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(Date updatedDate) {
    this.updatedDate = updatedDate;
  }

  public Integer getNeedPasswordReset() {
    return needPasswordReset;
  }

  public void setNeedPasswordReset(Integer needPasswordReset) {
    this.needPasswordReset = needPasswordReset;
  }

  public Collection<AppGroup> getAppGroupCollection() {
    return appGroupCollection;
  }

  public void setAppGroupCollection(Collection<AppGroup> appGroupCollection) {
    this.appGroupCollection = appGroupCollection;
  }

  public UserProfile getUserProfile() {
    return userProfile;
  }

  public void setUserProfile(UserProfile userProfile) {
    this.userProfile = userProfile;
  }

  public Collection<WtTrans> getWtTransCollection() {
    return wtTransCollection;
  }

  public void setWtTransCollection(Collection<WtTrans> wtTransCollection) {
    this.wtTransCollection = wtTransCollection;
  }

  public WtContact getWtContact() {
    return wtContact;
  }

  public void setWtContact(WtContact wtContact) {
    this.wtContact = wtContact;
  }

  public Integer getActive() {
    return active;
  }

  public boolean isActive() {
    if (active < 1) {
      return false;
    }
    return true;
  }

  public void setActive(Integer active) {
    this.active = active;
  }

//  public Collection<WtAgency> getRepAgencyCollection() {
//    return repAgencyCollection;
//  }
//
//  public void setRepAgencyCollection(Collection<WtAgency> agencyCollection) {
//    this.repAgencyCollection = agencyCollection;
//  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (userId != null ? userId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof AppUser)) {
      return false;
    }
    AppUser other = (AppUser) object;
    if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.gei.entities.AppUser[ userId=" + userId + " ]";
  }

  public boolean isAppAccount() {
    for (AppGroup group : appGroupCollection) {
      if (group.getCode().equalsIgnoreCase(ROLE_APP_ACCOUNT)) {
        return true;
      }
    }
    return false;
  }

  public boolean isReviewer() {
    for (AppGroup group : appGroupCollection) {
      if (group.getCode().equalsIgnoreCase(ROLE_REVIEWER)) {
        return true;
      }
    }
    return false;
  }

  public boolean isManager() {
    for (AppGroup group : appGroupCollection) {
      if (group.getCode().equalsIgnoreCase(ROLE_MANAGER)) {
        return true;
      }
    }
    return false;
  }

  public boolean isSysAdmin() {
    for (AppGroup group : appGroupCollection) {
      if (group.getCode().equalsIgnoreCase(ROLE_SYS_ADMIN)) {
        return true;
      }
    }
    return false;
  }

  public boolean isSuperAdmin() {
    for (AppGroup group : appGroupCollection) {
      if (group.getCode().equalsIgnoreCase(ROLE_SUPER_ADMIN)) {
        return true;
      }
    }
    return false;
  }

  public boolean isBuyersRepresentative() {
    for (AppGroup group : appGroupCollection) {
      if (group.getCode().equalsIgnoreCase(ROLE_BUYER_REP)) {
        return true;
      }
    }
    return false;
  }

  public boolean isUSBR() {
    for (AppGroup group : appGroupCollection) {
      if (group.getCode().equalsIgnoreCase(ROLE_USBR)) {
        return true;
      }
    }
    return false;
  }

  public boolean isCreater(WtTrans trans) {
    if (Objects.equals(userId, trans.getCreatedById())) {
      return true;
    }
    return false;
  }

  public boolean hasTransPermit(WtTrans trans) {
    for (WtTrans wtTrans : this.getWtTransCollection()) {
      if (trans.getWtTransId().equals(wtTrans.getWtTransId())) {
        return true;
      }
    }
    return false;
  }

  @Transient
  public String getName(){
    String result = null;

    if (this.wtContact == null) {
      result = this.username;
    }
    else {
      result = (this.wtContact.getFirstName() + " " + this.wtContact.getLastName()).trim();
    }

    return result;
  }
}
