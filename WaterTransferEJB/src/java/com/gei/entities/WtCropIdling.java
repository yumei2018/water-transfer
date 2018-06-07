
package com.gei.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ymei
 */
@Entity
@Table(name = "WT_CROP_IDLING")
@SequenceGenerator(name = "WtCropIdlingSeq", sequenceName = "WT_CROP_IDLING_PKSEQ", allocationSize = 1)
public class WtCropIdling extends AbstractEntity<WtCropIdling> implements Serializable {

  // <editor-fold defaultstate="collapsed" desc="Private Fields">
  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @NotNull
  @GeneratedValue(generator = "WtCropIdlingSeq", strategy = GenerationType.SEQUENCE)
  @Column(name = "WT_CROP_IDLING_ID")
  private Integer wtCropIdlingId;
  @Column(name = "WATER_TRANS_QUA")
  private Double waterTransQua;
  @Column(name = "TOTAL_TRANSFER_ACR")
  private Double totalTransferAcr;
  @Column(name = "PRO_TRANSFER_BY_CI")
  private Double proTransferByCI;
  @Column(name = "PRO_TRANSFER_BY_CS")
  private Double proTransferByCS;
  @Column(name = "IS_RES_RELEASE")
  private Integer isResRelease;
  @Column(name = "CURRENT_FS_AGENCY")
  private Double currentFsAgency;
  @Column(name = "MAY_TW")
  private Double mayTw;
  @Column(name = "JUNE_TW")
  private Double juneTw;
  @Column(name = "JULY_TW")
  private Double julyTw;
  @Column(name = "AUGUST_TW")
  private Double augustTw;
  @Column(name = "SEPTEMBER_TW")
  private Double septemberTw;
  @Column(name = "ACTUAL_AMOUNT")
  private Double actualAmount;
  @Column(name = "CONTRACT_AMOUNT")
  private Double contractAmount;
  @Column(name = "DELIVERED_AMOUNT")
  private Double deliveredAmount;
  @Column(name = "EXPORT_AMOUNT")
  private Double exportAmount;
  @Column(name = "EXPORTED_AMOUNT")
  private Double exportedAmount;

  @OneToMany(mappedBy = "wtCropIdling", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  private Collection<WtAttachment> wtAttachmentCollection;
//  @OneToMany(mappedBy = "wtCropIdling2", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  @ManyToMany(cascade = CascadeType.ALL, mappedBy = "wtCropIdlingCollection")
  private Collection<WtAttachment> wtMapAttCollection;  
  @OneToMany(mappedBy = "wtCropIdling", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  private Collection<WtCiCroptype> croptypeCollection;
  
  @JoinColumn(name = "WT_TRANS_ID", referencedColumnName = "WT_TRANS_ID")
  @OneToOne
  private WtTrans wtTrans;
//  @OneToOne(mappedBy = "wtCropIdling", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  @OneToOne(mappedBy = "wtCropIdling", cascade = CascadeType.ALL)
  private WtCiMonthly wtCiMonthly;
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Constructor">
  public WtCropIdling() {
  }

  public WtCropIdling(Integer wtCropIdlingId) {
    this.wtCropIdlingId = wtCropIdlingId;
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Public Get and Set">
  public Integer getWtCropIdlingId() {
    return wtCropIdlingId;
  }

  public void setWtCropIdlingId(Integer wtCropIdlingId) {
    this.wtCropIdlingId = wtCropIdlingId;
  }

  public Double getWaterTransQua() {
    return waterTransQua;
  }

  public void setWaterTransQua(Double waterTransQua) {
    this.waterTransQua = waterTransQua;
  }

  public Double getTotalTransferAcr() {
    return totalTransferAcr;
  }

  public void setTotalTransferAcr(Double totalTransferAcr) {
    this.totalTransferAcr = totalTransferAcr;
  }

  public Double getProTransferByCI() {
    return proTransferByCI;
  }

  public void setProTransferByCI(Double proTransferByCI) {
    this.proTransferByCI = proTransferByCI;
  }

  public Double getProTransferByCS() {
    return proTransferByCS;
  }

  public void setProTransferByCS(Double proTransferByCS) {
    this.proTransferByCS = proTransferByCS;
  }

  public Integer getIsResRelease() {
    return isResRelease;
  }

  public void setIsResRelease(Integer isResRelease) {
    this.isResRelease = isResRelease;
  }

  public Double getCurrentFsAgency() {
    return currentFsAgency;
  }

  public void setCurrentFsAgency(Double currentFsAgency) {
    this.currentFsAgency = currentFsAgency;
  }

  public Double getMayTw() {
    return mayTw;
  }

  public void setMayTw(Double mayTw) {
    this.mayTw = mayTw;
  }

  public Double getJuneTw() {
    return juneTw;
  }

  public void setJuneTw(Double juneTw) {
    this.juneTw = juneTw;
  }

  public Double getJulyTw() {
    return julyTw;
  }

  public void setJulyTw(Double julyTw) {
    this.julyTw = julyTw;
  }

  public Double getAugustTw() {
    return augustTw;
  }

  public void setAugustTw(Double augustTw) {
    this.augustTw = augustTw;
  }

  public Double getSeptemberTw() {
    return septemberTw;
  }

  public void setSeptemberTw(Double septemberTw) {
    this.septemberTw = septemberTw;
  }

  public Double getActualAmount() {
    return actualAmount;
  }

  public void setActualAmount(Double actualAmount) {
    this.actualAmount = actualAmount;
  }

  public Double getContractAmount() {
    return contractAmount;
  }

  public void setContractAmount(Double contractAmount) {
    this.contractAmount = contractAmount;
  }

  public Double getDeliveredAmount() {
    return deliveredAmount;
  }

  public void setDeliveredAmount(Double deliveredAmount) {
    this.deliveredAmount = deliveredAmount;
  }

  public Double getExportAmount() {
    return exportAmount;
  }

  public void setExportAmount(Double exportAmount) {
    this.exportAmount = exportAmount;
  }

  public Double getExportedAmount() {
    return exportedAmount;
  }

  public void setExportedAmount(Double exportedAmount) {
    this.exportedAmount = exportedAmount;
  }

  public Collection<WtAttachment> getWtAttachmentCollection() {
//    if ((this.wtAttachmentCollection == null) && (this.wtCropIdlingId != null)) {
//      WtAttachmentFacade facade = WebUtil.getFacade(WtAttachmentFacade.class);
//      String query = String.format("SELECT WT_ATTACHMENT_ID \n"
//              + "FROM WT_CI_ATTACHMENT \n"
//              + "WHERE WT_CROP_IDLING_ID = %d", this.wtCropIdlingId);
//      List<Map> results = facade.select(query);
//      if (results != null) {
//        this.wtAttachmentCollection = new ArrayList<>();
//        WtAttachment att = null;
//
//        for (Map m : results) {
//          att = facade.find(new WtAttachment(((java.math.BigDecimal) m.get("wtAttachmentId")).intValue()));
//          if (att == null) {
//            continue;
//          }
//          this.wtAttachmentCollection.add(att);
//        }
//      }
//    }
    return wtAttachmentCollection;
  }

  public void setWtAttachmentCollection(Collection<WtAttachment> wtAttachmentCollection) {
    this.wtAttachmentCollection = wtAttachmentCollection;
  }

  public Collection<WtAttachment> getWtMapAttCollection() {
    return wtMapAttCollection;
  }

  public void setWtMapAttCollection(Collection<WtAttachment> wtMapAttCollection) {
    this.wtMapAttCollection = wtMapAttCollection;
  }

  public Collection<WtCiCroptype> getCroptypeCollection() {
    return croptypeCollection;
  }

  public void setCroptypeCollection(Collection<WtCiCroptype> croptypeCollection) {
    this.croptypeCollection = croptypeCollection;
  }

  public WtCiMonthly getWtCiMonthly() {
    return wtCiMonthly;
  }

  public void setWtCiMonthly(WtCiMonthly wtCiMonthly) {
    this.wtCiMonthly = wtCiMonthly;
  }

  public WtTrans getWtTrans() {
    return wtTrans;
  }

  public void setWtTrans(WtTrans wtTrans) {
    this.wtTrans = wtTrans;
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Object Overrides">
  @Override
  public int hashCode() {
    int hash = 0;
    hash += (wtCropIdlingId != null ? wtCropIdlingId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof WtCropIdling)) {
      return false;
    }
    WtCropIdling other = (WtCropIdling) object;
    if ((this.wtCropIdlingId == null && other.wtCropIdlingId != null) || (this.wtCropIdlingId != null && !this.wtCropIdlingId.equals(other.wtCropIdlingId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.gei.entities.WtCropIdling[ wtCropIdlingId=" + wtCropIdlingId + " ]";
  }
  // </editor-fold>
}
