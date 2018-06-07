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
@Table(name = "WT_RESERVOIR")
@SequenceGenerator(name = "WtReservoirSeq", sequenceName = "WT_RESERVOIR_PKSEQ", allocationSize = 1)
public class WtReservoir extends AbstractEntity<WtReservoir> implements Serializable {

  // <editor-fold defaultstate="collapsed" desc="Private Fields">
  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "WT_RESERVOIR_ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WtReservoirSeq")
  private Integer wtReservoirId;
  @Column(name = "WATER_TRANS_QUA")
  private Double waterTransQua;
  @Column(name = "TOP_ALLOW_STORAGE")
  private Double topAllowStorage;
  @Column(name = "TARGET_STORAGE")
  private Double targetStorage;
  @Column(name = "LOCATION_LAT")
  private Double locationLat;
  @Column(name = "LOCATION_LONG")
  private Double locationLong;
  @Column(name = "IS_SELLER_AUTH")
  private Integer isSellerAuth;
  @Column(name = "AUTH_OPERATOR")
  private String authOperator;
  @Column(name = "APRIL_TW")
  private Double aprilTw;
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
  @Column(name = "OCTOBER_TW")
  private Double octoberTw;
  @Column(name = "NOVEMBER_TW")
  private Double novemberTw;
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

  @OneToMany(mappedBy = "wtReservoir", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  private Collection<WtAttachment> wtAttachmentCollection;
  @OneToMany(mappedBy = "wtReservoir", cascade = CascadeType.ALL)
  private Collection<WtRvTarstor> wtRvTarstorCollection;
  @ManyToMany(mappedBy = "wtReservoirCollection", cascade = CascadeType.ALL)
  private Collection<WtPurpose> wtPurposeCollection;
  @JoinColumn(name = "WT_TRANS_ID", referencedColumnName = "WT_TRANS_ID")
  @OneToOne
  private WtTrans wtTrans;
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Constructor">
  public WtReservoir() {
  }

  public WtReservoir(Integer wtReservoirId) {
    this.wtReservoirId = wtReservoirId;
  }
 // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Public Get and Set">
  public Integer getWtReservoirId() {
    return wtReservoirId;
  }

  public void setWtReservoirId(Integer wtReservoirId) {
    this.wtReservoirId = wtReservoirId;
  }

  public Double getWaterTransQua() {
    return waterTransQua;
  }

  public void setWaterTransQua(Double waterTransQua) {
    this.waterTransQua = waterTransQua;
  }

  public Double getTopAllowStorage() {
    return topAllowStorage;
  }

  public void setTopAllowStorage(Double topAllowStorage) {
    this.topAllowStorage = topAllowStorage;
  }

  public Double getTargetStorage() {
    return targetStorage;
  }

  public void setTargetStorage(Double targetStorage) {
    this.targetStorage = targetStorage;
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

  public Integer getIsSellerAuth() {
    return isSellerAuth;
  }

  public void setIsSellerAuth(Integer isSellerAuth) {
    this.isSellerAuth = isSellerAuth;
  }

  public String getAuthOperator() {
    return authOperator;
  }

  public void setAuthOperator(String authOperator) {
    this.authOperator = authOperator;
  }

  public Double getAprilTw() {
    return aprilTw;
  }

  public void setAprilTw(Double aprilTw) {
    this.aprilTw = aprilTw;
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

  public Double getOctoberTw() {
    return octoberTw;
  }

  public void setOctoberTw(Double octoberTw) {
    this.octoberTw = octoberTw;
  }

  public Double getNovemberTw() {
    return novemberTw;
  }

  public void setNovemberTw(Double novemberTw) {
    this.novemberTw = novemberTw;
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
//    if ((this.wtAttachmentCollection == null) && (this.wtReservoirId != null)) {
//      WtAttachmentFacade facade = WebUtil.getFacade(WtAttachmentFacade.class);
//      String query = String.format("SELECT WT_ATTACHMENT_ID \n"
//              + "FROM WT_RV_ATTACHMENT \n"
//              + "WHERE WT_RESERVOIR_ID = %d", this.wtReservoirId);
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

  public Collection<WtRvTarstor> getWtRvTarstorCollection() {
    return wtRvTarstorCollection;
  }

  public void setWtRvTarstorCollection(Collection<WtRvTarstor> wtRvTarstorCollection) {
    this.wtRvTarstorCollection = wtRvTarstorCollection;
  }

  public Collection<WtPurpose> getWtPurposeCollection() {
    return wtPurposeCollection;
  }

  public void setWtPurposeCollection(Collection<WtPurpose> wtPurposeCollection) {
    this.wtPurposeCollection = wtPurposeCollection;
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
    hash += (wtReservoirId != null ? wtReservoirId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof WtReservoir)) {
      return false;
    }
    WtReservoir other = (WtReservoir) object;
    if ((this.wtReservoirId == null && other.wtReservoirId != null) || (this.wtReservoirId != null && !this.wtReservoirId.equals(other.wtReservoirId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.gei.entities.WtReservoir[ wtReservoirId=" + wtReservoirId + " ]";
  }
  // </editor-fold>

}
