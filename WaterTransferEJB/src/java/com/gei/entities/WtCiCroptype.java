
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
@Table(name = "WT_CI_CROPTYPE")
@SequenceGenerator(name="WtCiCroptypeSeq",sequenceName="WT_CI_CROPTYPE_PKSEQ",allocationSize = 1)
@NamedQueries({
  @NamedQuery(name = "WtCiCroptype.findAll", query = "SELECT w FROM WtCiCroptype w")})
public class WtCiCroptype extends AbstractEntity<WtCiCroptype> implements Serializable {
  private static final long serialVersionUID = 1L;
  
  // <editor-fold defaultstate="collapsed" desc="Private Fields">
  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "WT_CI_CROPTYPE_ID")
  @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "WtCiCroptypeSeq")
  private Integer wtCiCroptypeId;
  @Column(name = "CROP_WITHOUT_TRANSFER")
  private String cropWithoutTransfer;
  @Column(name = "CROP_WITH_TRANSFER")
  private String cropWithTransfer;
  @Column(name = "PRO_TRANSFER_TOTAL")
  private Double proTransferTotal;
  @Column(name = "CROP_ETAW")
  private Double cropEtaw;
  @Column(name = "WATER_TRANSFER_AMOUNT")
  private Double waterTransferAmount;
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
  
  @JoinColumn(name = "WT_CROP_IDLING_ID", referencedColumnName = "WT_CROP_IDLING_ID")
  @ManyToOne
  private WtCropIdling wtCropIdling;
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Constructor">
  /**
   * Public Constructor  
   */
  public WtCiCroptype() {
    super();  
  }
  
  public WtCiCroptype(Integer wtCiCroptypeId) {
    this.wtCiCroptypeId = wtCiCroptypeId;
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Public Get and Set">
    public Integer getWtCiCroptypeId() {
    return wtCiCroptypeId;
  }

  public void setWtCiCroptypeId(Integer wtCiCroptypeId) {
    this.wtCiCroptypeId = wtCiCroptypeId;
  }

  public String getCropWithoutTransfer() {
    return cropWithoutTransfer;
  }

  public void setCropWithoutTransfer(String cropWithoutTransfer) {
    this.cropWithoutTransfer = cropWithoutTransfer;
  }

  public String getCropWithTransfer() {
    return cropWithTransfer;
  }

  public void setCropWithTransfer(String cropWithTransfer) {
    this.cropWithTransfer = cropWithTransfer;
  }

  
  public Double getProTransferTotal() {
    return proTransferTotal;
  }

  public void setProTransferTotal(Double proTransferTotal) {
    this.proTransferTotal = proTransferTotal;
  }

  public Double getCropEtaw() {
    return cropEtaw;
  }

  public void setCropEtaw(Double cropEtaw) {
    this.cropEtaw = cropEtaw;
  }

  public Double getWaterTransferAmount() {
    return waterTransferAmount;
  }

  public void setWaterTransferAmount(Double waterTransferAmount) {
    this.waterTransferAmount = waterTransferAmount;
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

  public WtCropIdling getWtCropIdling() {
    return wtCropIdling;
  }

  public void setWtCropIdling(WtCropIdling wtCropIdling) {
    this.wtCropIdling = wtCropIdling;
  }
  
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Object Overrides">
  @Override
  public int hashCode() {
    int hash = 0;
    hash += (wtCiCroptypeId != null ? wtCiCroptypeId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof WtCiCroptype)) {
      return false;
    }
    WtCiCroptype other = (WtCiCroptype) object;
    if ((this.wtCiCroptypeId == null && other.wtCiCroptypeId != null) || (this.wtCiCroptypeId != null && !this.wtCiCroptypeId.equals(other.wtCiCroptypeId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.gei.entities.WtCiCroptype[ wtCiCroptypeId=" + wtCiCroptypeId + " ]";
  }
  // </editor-fold>
}
