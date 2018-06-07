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
@Table(name = "APP_PERMISSION")
@SequenceGenerator(name="PKSEQ",sequenceName="PERMISSION_PKSEQ",allocationSize = 1)
public class AppPermission extends AbstractEntity<AppPermission> implements Serializable {
  private static final Long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "PERMISSION_ID")
  @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "PKSEQ")
  private Integer permissionId;
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
  @ManyToMany(mappedBy = "appPermissionCollection")
  private Collection<AppGroup> appGroupCollection;

  public AppPermission() {
  }

  public AppPermission(Integer permissionId) {
    this.permissionId = permissionId;
  }

  public AppPermission(Integer permissionId, String code) {
    this.permissionId = permissionId;
    this.code = code;
  }

  public Integer getPermissionId() {
    return permissionId;
  }

  public void setPermissionId(Integer permissionId) {
    this.permissionId = permissionId;
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

  public Collection<AppGroup> getAppGroupCollection() {
    return appGroupCollection;
  }

  public void setAppGroupCollection(Collection<AppGroup> appGroupCollection) {
    this.appGroupCollection = appGroupCollection;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (permissionId != null ? permissionId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof AppPermission)) {
      return false;
    }
    AppPermission other = (AppPermission) object;
    if ((this.permissionId == null && other.permissionId != null) || (this.permissionId != null && !this.permissionId.equals(other.permissionId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.gei.entities.AppPermission[ permissionId=" + permissionId + " ]";
  }
  
}
