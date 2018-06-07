
package com.gei.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ymei
 */
@Entity
@Table(name = "WT_CI_MONTHLY")
@SequenceGenerator(name="WtCiMonthlySeq",sequenceName="WT_CI_MONTHLY_PKSEQ",allocationSize = 1)
@NamedQueries({
  @NamedQuery(name = "WtCiMonthly.findAll", query = "SELECT w FROM WtCiMonthly w")})
public class WtCiMonthly extends AbstractEntity<WtCiMonthly> implements Serializable {
  
  // <editor-fold defaultstate="collapsed" desc="Private Fields">
  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "WT_CI_MONTHLY_ID")
  @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "WtCiMonthlySeq")
  private Integer wtCiMonthlyId;
  @Column(name = "MAY_ETAW")
  private Integer mayEtaw;
  @Column(name = "MAY_TW")
  private Double mayTw;
  @Column(name = "JUNE_ETAW")
  private Integer juneEtaw;
  @Column(name = "JUNE_TW")
  private Double juneTw;
  @Column(name = "JULY_ETAW")
  private Integer julyEtaw;
  @Column(name = "JULY_TW")
  private Double julyTw;
  @Column(name = "AUGUST_ETAW")
  private Integer augustEtaw;
  @Column(name = "AUGUST_TW")
  private Double augustTw;
  @Column(name = "SEPTEMBER_ETAW")
  private Integer septemberEtaw;
  @Column(name = "SEPTEMBER_TW")
  private Double septemberTw;
  
  @JoinColumn(name = "WT_CROP_IDLING_ID", referencedColumnName = "WT_CROP_IDLING_ID")
  @OneToOne
  private WtCropIdling wtCropIdling;
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Constructor">
  /**
   * Public Constructor  
   */
  public WtCiMonthly() {
    super();  
  }
  
  public WtCiMonthly(Integer wtCiMonthlyId) {
    this.wtCiMonthlyId = wtCiMonthlyId;
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Public Get and Set">
  public Integer getWtCiMonthlyId() {
    return wtCiMonthlyId;
  }

  public void setWtCiMonthlyId(Integer wtCiMonthlyId) {
    this.wtCiMonthlyId = wtCiMonthlyId;
  }

  public Integer getMayEtaw() {
    return mayEtaw;
  }

  public void setMayEtaw(Integer mayEtaw) {
    this.mayEtaw = mayEtaw;
  }

  public Double getMayTw() {
    return mayTw;
  }

  public void setMayTw(Double mayTw) {
    this.mayTw = mayTw;
  }

  public Integer getJuneEtaw() {
    return juneEtaw;
  }

  public void setJuneEtaw(Integer juneEtaw) {
    this.juneEtaw = juneEtaw;
  }

  public Double getJuneTw() {
    return juneTw;
  }

  public void setJuneTw(Double juneTw) {
    this.juneTw = juneTw;
  }

  public Integer getJulyEtaw() {
    return julyEtaw;
  }

  public void setJulyEtaw(Integer julyEtaw) {
    this.julyEtaw = julyEtaw;
  }

  public Double getJulyTw() {
    return julyTw;
  }

  public void setJulyTw(Double julyTw) {
    this.julyTw = julyTw;
  }

  public Integer getAugustEtaw() {
    return augustEtaw;
  }

  public void setAugustEtaw(Integer augustEtaw) {
    this.augustEtaw = augustEtaw;
  }

  public Double getAugustTw() {
    return augustTw;
  }

  public void setAugustTw(Double augustTw) {
    this.augustTw = augustTw;
  }

  public Integer getSeptemberEtaw() {
    return septemberEtaw;
  }

  public void setSeptemberEtaw(Integer septemberEtaw) {
    this.septemberEtaw = septemberEtaw;
  }

  public Double getSeptemberTw() {
    return septemberTw;
  }

  public void setSeptemberTw(Double septemberTw) {
    this.septemberTw = septemberTw;
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
    hash += (wtCiMonthlyId != null ? wtCiMonthlyId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof WtCiMonthly)) {
      return false;
    }
    WtCiMonthly other = (WtCiMonthly) object;
    if ((this.wtCiMonthlyId == null && other.wtCiMonthlyId != null) || (this.wtCiMonthlyId != null && !this.wtCiMonthlyId.equals(other.wtCiMonthlyId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.gei.entities.WtCiMonthly[ wtCiMonthlyId=" + wtCiMonthlyId + " ]";
  }
  // </editor-fold>
}
