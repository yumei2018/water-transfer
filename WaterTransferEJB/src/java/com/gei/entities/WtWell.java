
package com.gei.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ymei
 */
@Entity
@Table(name = "WT_WELL")
@SequenceGenerator(name = "WtWellSeq", sequenceName = "WT_WELL_PKSEQ", allocationSize = 1)
public class WtWell extends AbstractEntity<WtWell> implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "WT_WELL_ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WtWellSeq")
  private Integer wtWellId;
  @Column(name = "WT_WELL_NUM")
  private String wtWellNum;
  @Column(name = "STATE_WELL_NUM")
  private String stateWellNum;
  @Column(name = "LOCAL_WELL_ID")
  private String localWellId;
  @Column(name = "CASGEM_STATION_ID")
  private Integer casgenStationId;
  @Column(name = "WELL_TRANSFER")
  private Integer wellTransfer;
  @Column(name = "WELL_MONITORING")
  private Integer wellMonitoring;
  @Column(name = "LAST_CALIBRATE_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastCalibrateDate;
  @Column(name = "METER_MAKE")
  private String meterMake;
  @Column(name = "METER_MODEL")
  private String meterModel;
  @Column(name = "WELL_CAPACITY")
  private Integer wellCapacity;
  @Column(name = "METER_UNITS")
  private String meterUnits;
  @Column(name = "METER_LAST_INSTALL")
  @Temporal(TemporalType.TIMESTAMP)
  private Date meterLastInstall;
  @Column(name = "POWER_SOURCE")
  private String powerSource;
  @Column(name = "ATTRIBUTE_YEAR")
  private Integer attributeYear;
  @Column(name = "SERIAL_NUM")
  private String serialNum;
  @Column(name = "DATA_COLLECT_AGENCY")
  private String dataCollectAgency;

  @ManyToMany(mappedBy = "wtWellCollection")
  private Collection<WtGroundwater> wtGroundwaterCollection;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "wtWell")
  private Collection<WtAttachment> wtAttachmentCollection;

  public WtWell() {
  }

  public WtWell(String wtWellNum) {
    this.wtWellNum = wtWellNum;
  }

  public Integer getWtWellId() {
    return wtWellId;
  }

  public void setWtWellId(Integer wtWellId) {
    this.wtWellId = wtWellId;
  }

  public String getWtWellNum() {
    return wtWellNum;
  }

  public void setWtWellNum(String wtWellNum) {
    this.wtWellNum = wtWellNum;
  }

  public String getStateWellNum() {
    return stateWellNum;
  }

  public void setStateWellNum(String stateWellNum) {
    this.stateWellNum = stateWellNum;
  }

  public String getLocalWellId() {
    return localWellId;
  }

  public void setLocalWellId(String localWellId) {
    this.localWellId = localWellId;
  }

  public Integer getWellCapacity() {
    return wellCapacity;
  }

  public Integer getCasgenStationId() {
    return casgenStationId;
  }

  public void setCasgenStationId(Integer casgenStationId) {
    this.casgenStationId = casgenStationId;
  }

  public void setWellCapacity(Integer wellCapacity) {
    this.wellCapacity = wellCapacity;
  }

  public String getMeterMake() {
    return meterMake;
  }

  public void setMeterMake(String meterMake) {
    this.meterMake = meterMake;
  }

  public String getMeterModel() {
    return meterModel;
  }

  public void setMeterModel(String meterModel) {
    this.meterModel = meterModel;
  }

  public String getMeterUnits() {
    return meterUnits;
  }

  public void setMeterUnits(String meterUnits) {
    this.meterUnits = meterUnits;
  }

  public Date getLastCalibrateDate() {
    return lastCalibrateDate;
  }

  public void setLastCalibrateDate(Date lastCalibrateDate) {
    this.lastCalibrateDate = lastCalibrateDate;
  }

  public Integer getWellTransfer() {
    return wellTransfer;
  }

  public void setWellTransfer(Integer wellTransfer) {
    this.wellTransfer = wellTransfer;
  }

  public Integer getWellMonitoring() {
    return wellMonitoring;
  }

  public void setWellMonitoring(Integer wellMonitoring) {
    this.wellMonitoring = wellMonitoring;
  }

  public Date getMeterLastInstall() {
    return meterLastInstall;
  }

  public void setMeterLastInstall(Date meterLastInstall) {
    this.meterLastInstall = meterLastInstall;
  }

  public Integer getAttributeYear() {
    return attributeYear;
  }

  public void setAttributeYear(Integer attributeYear) {
    this.attributeYear = attributeYear;
  }

  public String getPowerSource() {
    return powerSource;
  }

  public void setPowerSource(String powerSource) {
    this.powerSource = powerSource;
  }

  public String getSerialNum() {
    return serialNum;
  }

  public void setSerialNum(String serialNum) {
    this.serialNum = serialNum;
  }

  public String getDataCollectAgency() {
    return dataCollectAgency;
  }

  public void setDataCollectAgency(String dataCollectAgency) {
    this.dataCollectAgency = dataCollectAgency;
  }

  public Collection<WtGroundwater> getWtGroundwaterCollection() {
    return wtGroundwaterCollection;
  }

  public void setWtGroundwaterCollection(Collection<WtGroundwater> wtGroundwaterCollection) {
    this.wtGroundwaterCollection = wtGroundwaterCollection;
  }

  public Collection<WtAttachment> getWtAttachmentCollection() {
    return wtAttachmentCollection;
  }

  public void setWtAttachmentCollection(Collection<WtAttachment> wtAttachmentCollection) {
    this.wtAttachmentCollection = wtAttachmentCollection;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (wtWellNum != null ? wtWellNum.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof WtWell)) {
      return false;
    }
    WtWell other = (WtWell) object;
    if ((this.wtWellNum == null && other.wtWellNum != null) 
          || (this.wtWellNum != null && !this.wtWellNum.equals(other.wtWellNum))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.gei.entities.WtWell[ wtWellNum=" + wtWellNum + " ]";
  }

}
