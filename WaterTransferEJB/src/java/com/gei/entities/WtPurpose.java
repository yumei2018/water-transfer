/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gei.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author ymei
 */
@Entity
@Table(name = "WT_PURPOSE")
@NamedQueries({
  @NamedQuery(name = "WtPurpose.findAll", query = "SELECT w FROM WtPurpose w")})
public class WtPurpose extends AbstractEntity<WtPurpose> implements Serializable {
  private static final long serialVersionUID = 1L;

  // <editor-fold defaultstate="collapsed" desc="Private Fields">
  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "WT_PURPOSE_ID")
  private Integer wtPurposeId;
  @Column(name = "PURPOSE")
  private String purpose;
  @JoinTable(name = "WT_RV_PURPOSE", joinColumns = {
      @JoinColumn(name = "WT_PURPOSE_ID", referencedColumnName = "WT_PURPOSE_ID")}, inverseJoinColumns = {
      @JoinColumn(name = "WT_RESERVOIR_ID", referencedColumnName = "WT_RESERVOIR_ID")})
  @ManyToMany
  private Collection<WtReservoir> wtReservoirCollection;
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Constructor">
  /**
   * Public Constructor
   */
  public WtPurpose() {
    super();
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Public Methods">

  public WtPurpose(Integer wtPurposeId) {
    this.wtPurposeId = wtPurposeId;
  }

  public Integer getWtPurposeId() {
    return wtPurposeId;
  }

  public void setWtPurposeId(Integer wtPurposeId) {
    this.wtPurposeId = wtPurposeId;
  }

  public String getPurpose() {
    return purpose;
  }

  public void setPurpose(String purpose) {
    this.purpose = purpose;
  }

//  public WtReservoir getWtReservoir() {
//    return wtReservoir;
//  }
//
//  public void setWtReservoir(WtReservoir wtReservoir) {
//    this.wtReservoir = wtReservoir;
//  }

  public Collection<WtReservoir> getWtReservoirCollection() {
    return wtReservoirCollection;
  }

  public void setWtReservoirCollection(Collection<WtReservoir> wtReservoirCollection) {
    this.wtReservoirCollection = wtReservoirCollection;
  }

  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Object Overrides">
  @Override
  public int hashCode() {
    int hash = 0;
    hash += (wtPurposeId != null ? wtPurposeId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof WtPurpose)) {
      return false;
    }
    WtPurpose other = (WtPurpose) object;
    if ((this.wtPurposeId == null && other.wtPurposeId != null) || (this.wtPurposeId != null && !this.wtPurposeId.equals(other.wtPurposeId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.gei.entities.WtPurpose[ wtPurposeId=" + wtPurposeId + " ]";
  }
  // </editor-fold>
}
