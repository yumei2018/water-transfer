/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gei.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author ymei
 */
@Entity
@Table(name = "WT_CROP_TYPE")
@NamedQueries({
  @NamedQuery(name = "WtCropType.findAll", query = "SELECT w FROM WtCropType w")})
@SequenceGenerator(name = "WtCropTypeSeq",sequenceName = "WT_CROP_TYPE_PKSEQ",allocationSize = 1)
public class WtCropType extends AbstractEntity<WtCropType> implements Serializable {
  private static final long serialVersionUID = 1L;  

  // <editor-fold defaultstate="collapsed" desc="Private Fields">
  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "WT_CROP_TYPE_ID")
  @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "WtCropTypeSeq")
  private Integer wtCropTypeId;
  @Column(name = "CROP_TYPE")
  private String cropType;
  @Column(name = "CROP_ETAW")
  private Double cropEtaw;
  @Column(name = "CROP_DESCRIPTION")
  private String cropDescription;
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Constructor">
  /**
   * Public Constructor  
   */
  public WtCropType() {
    super();  
  }
  
  public WtCropType(Integer wtCropTypeId) {
    this.wtCropTypeId = wtCropTypeId;
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Public Get/Set Methods">
    public Integer getWtCropTypeId() {
    return wtCropTypeId;
  }

  public void setWtCropTypeId(Integer wtCropTypeId) {
    this.wtCropTypeId = wtCropTypeId;
  }

  public String getCropType() {
    return cropType;
  }

  public void setCropType(String cropType) {
    this.cropType = cropType;
  }

  public Double getCropEtaw() {
    return cropEtaw;
  }

  public void setCropEtaw(Double cropEtaw) {
    this.cropEtaw = cropEtaw;
  }

  public String getCropDescription() {
    return cropDescription;
  }

  public void setCropDescription(String cropDescription) {
    this.cropDescription = cropDescription;
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Object Overrides">
  @Override
  public int hashCode() {
    int hash = 0;
    hash += (wtCropTypeId != null ? wtCropTypeId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof WtCropType)) {
      return false;
    }
    WtCropType other = (WtCropType) object;
    if ((this.wtCropTypeId == null && other.wtCropTypeId != null) || (this.wtCropTypeId != null && !this.wtCropTypeId.equals(other.wtCropTypeId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.gei.entities.WtCropType[ wtCropTypeId=" + wtCropTypeId + " ]";
  }
  // </editor-fold>
}
