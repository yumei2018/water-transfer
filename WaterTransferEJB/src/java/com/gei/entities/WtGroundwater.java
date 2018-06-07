
package com.gei.entities;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ymei
 */
@Entity
@Table(name = "WT_GROUNDWATER")
@SequenceGenerator(name="WtGroundwaterSeq",sequenceName="WT_GROUNDWATER_PKSEQ",allocationSize = 1)
public class WtGroundwater extends AbstractEntity<WtGroundwater> implements Serializable {

  // <editor-fold defaultstate="collapsed" desc="Private Fields">
  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "WT_GROUNDWATER_ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WtGroundwaterSeq")
  private Integer wtGroundwaterId;
  @Column(name = "WELLS_NUMBER")
  private Integer wellsNumber;
  @Column(name = "TOTAL_PUMPING")
  private Double totalPumping;
  @Column(name = "BASE_PUMPING")
  private Double basePumping;
  @Column(name = "GROSS_TRANS_PUMPING")
  private Double grossTransPumping;
  @Column(name = "STREAM_DEPLETION")
  private Double streamDepletion;
  @Column(name = "NET_TRANS_WATER")
  private Double netTransWater;
  @Column(name = "PUMPING_WELLS_NUMBER")
  private Integer pumpingWellsNumber;
  @Column(name = "MONITORING_WELLS_NUMBER")
  private Integer monitoringWellsNumber;
  @Column(name = "DEPLETION_FACTOR")
  private Double depletionFactor;
  @Column(name = "IS_RES_RELEASE")
  private Integer isResRelease;
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
  
  @OneToMany(mappedBy = "wtGroundwater", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  private Collection<WtAttachment> wtAttachmentCollection;
  @JoinColumn(name = "WT_TRANS_ID", referencedColumnName = "WT_TRANS_ID")
  @OneToOne
  private WtTrans wtTrans;
  @JoinTable(name = "WT_GW_WELL", joinColumns = {
    @JoinColumn(name = "WT_GROUNDWATER_ID", referencedColumnName = "WT_GROUNDWATER_ID")}, inverseJoinColumns = {
    @JoinColumn(name = "WT_WELL_ID", referencedColumnName = "WT_WELL_ID")})
  @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  private Collection<WtWell> wtWellCollection;
  @OneToMany(mappedBy = "wtGroundwater", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  private Collection<WtGwMonthly> wtGwMonthlyCollection;
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Constructor">
  public WtGroundwater() {
  }

  public WtGroundwater(Integer wtGroundwaterId) {
    this.wtGroundwaterId = wtGroundwaterId;
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Public Get and Set">
  public Integer getWtGroundwaterId() {
    return wtGroundwaterId;
  }

  public void setWtGroundwaterId(Integer wtGroundwaterId) {
    this.wtGroundwaterId = wtGroundwaterId;
  }

  public Integer getWellsNumber() {
    return wellsNumber;
  }

  public void setWellsNumber(Integer wellsNumber) {
    this.wellsNumber = wellsNumber;
  }

  public Double getTotalPumping() {
    return totalPumping;
  }

  public void setTotalPumping(Double totalPumping) {
    this.totalPumping = totalPumping;
  }

  public Double getBasePumping() {
    return basePumping;
  }

  public void setBasePumping(Double basePumping) {
    this.basePumping = basePumping;
  }

  public Double getGrossTransPumping() {
    return grossTransPumping;
  }

  public void setGrossTransPumping(Double grossTransPumping) {
    this.grossTransPumping = grossTransPumping;
  }

  public Double getStreamDepletion() {
    return streamDepletion;
  }

  public void setStreamDepletion(Double streamDepletion) {
    this.streamDepletion = streamDepletion;
  }

  public Double getNetTransWater() {
    return netTransWater;
  }

  public void setNetTransWater(Double netTransWater) {
    this.netTransWater = netTransWater;
  }

  public Integer getPumpingWellsNumber() {
    return pumpingWellsNumber;
  }

  public void setPumpingWellsNumber(Integer pumpingWellsNumber) {
    this.pumpingWellsNumber = pumpingWellsNumber;
  }

  public Integer getMonitoringWellsNumber() {
    return monitoringWellsNumber;
  }

  public void setMonitoringWellsNumber(Integer monitoringWellsNumber) {
    this.monitoringWellsNumber = monitoringWellsNumber;
  }

  public Double getDepletionFactor() {
    return depletionFactor;
  }

  public void setDepletionFactor(Double depletionFactor) {
    this.depletionFactor = depletionFactor;
  }

  public Integer getIsResRelease() {
    return isResRelease;
  }

  public void setIsResRelease(Integer isResRelease) {
    this.isResRelease = isResRelease;
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
//      if ((this.wtAttachmentCollection == null) && (this.wtGroundwaterId != null)) {
//        WtAttachmentFacade facade = WebUtil.getFacade(WtAttachmentFacade.class);
//        String query = String.format("SELECT WT_ATTACHMENT_ID \n" +
//                                      "FROM WT_GW_ATTACHMENT \n" +
//                                      "WHERE WT_GROUNDWATER_ID = %d",this.wtGroundwaterId);
//        List<Map> results = facade.select(query);
//        if (results != null) {
//          this.wtAttachmentCollection = new ArrayList<>();
//          WtAttachment att = null;
//
//          for (Map m : results) {
//            att = facade.find(new WtAttachment(((java.math.BigDecimal) m.get("wtAttachmentId")).intValue()));
//            if (att == null) {
//              continue;
//            }
//            this.wtAttachmentCollection.add(att);
//          }
//        }
//      }
    return wtAttachmentCollection;
  }

  public void setWtAttachmentCollection(Collection<WtAttachment> wtAttachmentCollection) {
    this.wtAttachmentCollection = wtAttachmentCollection;
  }

  public WtTrans getWtTrans() {
    return wtTrans;
  }

  public void setWtTrans(WtTrans wtTrans) {
    this.wtTrans = wtTrans;
  }

  public Collection<WtWell> getWtWellCollection() {
    return wtWellCollection;
  }

  public void setWtWellCollection(Collection<WtWell> wtWellCollection) {
    this.wtWellCollection = wtWellCollection;
  }

  public Collection<WtGwMonthly> getWtGwMonthlyCollection() {
    return wtGwMonthlyCollection;
  }

  public void setWtGwMonthlyCollection(Collection<WtGwMonthly> wtGwMonthlyCollection) {
    this.wtGwMonthlyCollection = wtGwMonthlyCollection;
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Object Overrides">
  @Override
  public int hashCode() {
    int hash = 0;
    hash += (wtGroundwaterId != null ? wtGroundwaterId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof WtGroundwater)) {
      return false;
    }
    WtGroundwater other = (WtGroundwater) object;
    if ((this.wtGroundwaterId == null && other.wtGroundwaterId != null) || (this.wtGroundwaterId != null && !this.wtGroundwaterId.equals(other.wtGroundwaterId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.gei.entities.WtGroundwater[ wtGroundwaterId=" + wtGroundwaterId + " ]";
  }
  // </editor-fold>
}

