
package com.gei.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ymei
 */
@Entity
@Table(name = "WT_WATER_LOSS")
@NamedQueries({
  @NamedQuery(name = "WtWaterLoss.findAll", query = "SELECT w FROM WtWaterLoss w")})
@SequenceGenerator(name = "WtWaterLossSeq", sequenceName = "WT_WATER_LOSS_PKSEQ", allocationSize = 1)
public class WtWaterLoss extends AbstractEntity<WtWaterLoss> implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "WT_WATER_LOSS_ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WtWaterLossSeq")
  private Integer wtWaterLossId;
  @Column(name = "BANKS_INITIAL_PERCENT")
  private Integer banksInitialPercent;
  @Column(name = "BANKS_FINAL_PERCENT")
  private Integer banksfinalPercent;
  @Column(name = "NBA_INITIAL_PERCENT")
  private Integer nbaInitialPercent;
  @Column(name = "NBA_FINAL_PERCENT")
  private Integer nbafinalPercent;
  @Column(name = "MERCED_INITIAL_PERCENT")
  private Integer mercedInitialPercent;
  @Column(name = "MERCED_FINAL_PERCENT")
  private Integer mercedfinalPercent;
  @Column(name = "REACH_LOSS_PERCENT")
  private Integer reachLossPercent;
  @JoinColumn(name = "WT_TRANS_ID", referencedColumnName = "WT_TRANS_ID")
  @OneToOne
  private WtTrans wtTrans;

  public WtWaterLoss() {
  }

  public WtWaterLoss(Integer wtWaterLossId) {
    this.wtWaterLossId = wtWaterLossId;
  }

  public Integer getWtWaterLossId() {
    return wtWaterLossId;
  }

  public void setWtWaterLossId(Integer wtWaterLossId) {
    this.wtWaterLossId = wtWaterLossId;
  }

  public Integer getBanksInitialPercent() {
    return banksInitialPercent;
  }

  public void setBanksInitialPercent(Integer banksInitialPercent) {
    this.banksInitialPercent = banksInitialPercent;
  }

  public Integer getBanksfinalPercent() {
    return banksfinalPercent;
  }

  public void setBanksfinalPercent(Integer banksfinalPercent) {
    this.banksfinalPercent = banksfinalPercent;
  }

  public Integer getNbaInitialPercent() {
    return nbaInitialPercent;
  }

  public void setNbaInitialPercent(Integer nbaInitialPercent) {
    this.nbaInitialPercent = nbaInitialPercent;
  }

  public Integer getNbafinalPercent() {
    return nbafinalPercent;
  }

  public void setNbafinalPercent(Integer nbafinalPercent) {
    this.nbafinalPercent = nbafinalPercent;
  }

  public Integer getMercedInitialPercent() {
    return mercedInitialPercent;
  }

  public void setMercedInitialPercent(Integer mercedInitialPercent) {
    this.mercedInitialPercent = mercedInitialPercent;
  }

  public Integer getMercedfinalPercent() {
    return mercedfinalPercent;
  }

  public void setMercedfinalPercent(Integer mercedfinalPercent) {
    this.mercedfinalPercent = mercedfinalPercent;
  }

  public Integer getReachLossPercent() {
    return reachLossPercent;
  }

  public void setReachLossPercent(Integer reachLossPercent) {
    this.reachLossPercent = reachLossPercent;
  }

  public WtTrans getWtTrans() {
    return wtTrans;
  }

  public void setWtTrans(WtTrans wtTrans) {
    this.wtTrans = wtTrans;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (wtWaterLossId != null ? wtWaterLossId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof WtWaterLoss)) {
      return false;
    }
    WtWaterLoss other = (WtWaterLoss) object;
    if ((this.wtWaterLossId == null && other.wtWaterLossId != null) || (this.wtWaterLossId != null && !this.wtWaterLossId.equals(other.wtWaterLossId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.gei.entities.WtWaterLoss[ wtWaterLossId=" + wtWaterLossId + " ]";
  }

}
