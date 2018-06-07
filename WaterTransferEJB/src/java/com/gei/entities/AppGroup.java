/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gei.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author clay
 */
@Entity
@Table(name = "APP_GROUP")
@SequenceGenerator(name="PKSEQ",sequenceName="GROUP_PKSEQ",allocationSize = 1)
public class AppGroup extends AbstractEntity<AppGroup> implements Serializable {
  private static final Long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "GROUP_ID")
  @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "PKSEQ")
  private Integer groupId;
  @Column(name = "NAME")
  private String name;
  @Basic(optional = false)
  @NotNull
  @Column(name = "CODE")
  private String code;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "CREATED_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;
  @Column(name = "UPDATED_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date updatedDate;
  @ManyToMany(mappedBy = "appGroupCollection")
  private Collection<AppUser> appUserCollection;
  @JoinTable(name = "GROUP_PERMISSION", joinColumns = {
    @JoinColumn(name = "GROUP_ID", referencedColumnName = "GROUP_ID")}, inverseJoinColumns = {
    @JoinColumn(name = "PERMISSION_ID", referencedColumnName = "PERMISSION_ID")})
  @ManyToMany
  private Collection<AppPermission> appPermissionCollection;

  public AppGroup() {
  }

  public AppGroup(Integer groupId) {
    this.groupId = groupId;
  }

  public AppGroup(Integer groupId, String code) {
    this.groupId = groupId;
    this.code = code;
  }

  public Integer getGroupId() {
    return groupId;
  }

  public void setGroupId(Integer groupId) {
    this.groupId = groupId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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

  public Collection<AppUser> getAppUserCollection() {
    return appUserCollection;
  }

  public void setAppUserCollection(Collection<AppUser> appUserCollection) {
    this.appUserCollection = appUserCollection;
  }

  public Collection<AppPermission> getAppPermissionCollection() {
    return appPermissionCollection;
  }

  public void setAppPermissionCollection(Collection<AppPermission> appPermissionCollection) {
    this.appPermissionCollection = appPermissionCollection;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (groupId != null ? groupId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof AppGroup)) {
      return false;
    }
    AppGroup other = (AppGroup) object;
    if ((this.groupId == null && other.groupId != null) || (this.groupId != null && !this.groupId.equals(other.groupId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.gei.entities.AppGroup[ groupId=" + groupId + " ]";
  }

//  public static void main(String[] args)
//  {
//    AppGroup a = new AppGroup();
//    a.setGroupId(232);
//    System.out.print(a.getId());
//  }
}
