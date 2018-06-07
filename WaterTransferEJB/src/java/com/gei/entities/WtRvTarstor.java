/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gei.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ymei
 */
@Entity
@Table(name = "WT_RV_TARSTOR")
@NamedQueries({
  @NamedQuery(name = "WtRvTarstor.findAll", query = "SELECT w FROM WtRvTarstor w")})
@SequenceGenerator(name="WtRvTarstorSeq",sequenceName="WT_RV_TARSTOR_PKSEQ",allocationSize = 1)
public class WtRvTarstor extends AbstractEntity<WtRvTarstor> implements Serializable {
  private static final long serialVersionUID = 1L;

  // <editor-fold defaultstate="collapsed" desc="Private Fields">
  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "WT_RV_TARSTOR_ID")
  @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "WtRvTarstorSeq")
  private Integer wtRvTarstorId;
  @Column(name = "DAM_NAME")
  private String damName;
  @Column(name = "LOCATION_LAT")
  private Double locationLat;
  @Column(name = "LOCATION_LONG")
  private Double locationLong;
  @Column(name = "OPERATOR")
  private String operator;
  @Column(name = "PHONE_NUMBER")
  private String phoneNumber;
  @Column(name = "EMAIL")
  private String email;
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

  @JoinColumn(name = "WT_RESERVOIR_ID", referencedColumnName = "WT_RESERVOIR_ID")
  @ManyToOne
  private WtReservoir wtReservoir;
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Public Methods">
  public WtRvTarstor() {
  }

  public WtRvTarstor(Integer wtRvTarstorId) {
    this.wtRvTarstorId = wtRvTarstorId;
  }

  public Integer getWtRvTarstorId() {
    return wtRvTarstorId;
  }

  public void setWtRvTarstorId(Integer wtRvTarstorId) {
    this.wtRvTarstorId = wtRvTarstorId;
  }

  public String getDamName() {
    return damName;
  }

  public void setDamName(String damName) {
    this.damName = damName;
  }

  public Double getLocationLat() {
    return locationLat;
  }

  public void setLocationLat(Double locationLat) {
    this.locationLat = locationLat;
  }

  public Double getLocationLong() {
    return locationLong;
  }

  public void setLocationLong(Double locationLong) {
    this.locationLong = locationLong;
  }

  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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

  public WtReservoir getWtReservoir() {
    return wtReservoir;
  }

  public void setWtReservoir(WtReservoir wtReservoir) {
    this.wtReservoir = wtReservoir;
  }

  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Object Overrides">
    @Override
  public int hashCode() {
    int hash = 0;
    hash += (wtRvTarstorId != null ? wtRvTarstorId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof WtRvTarstor)) {
      return false;
    }
    WtRvTarstor other = (WtRvTarstor) object;
    if ((this.wtRvTarstorId == null && other.wtRvTarstorId != null) || (this.wtRvTarstorId != null && !this.wtRvTarstorId.equals(other.wtRvTarstorId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.gei.entities.WtRvTarstor[ wtRvTarstorId=" + wtRvTarstorId + " ]";
  }
  // </editor-fold>

}
