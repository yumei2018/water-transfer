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
@Table(name = "WT_TRANS_TRACK")
@NamedQueries({
  @NamedQuery(name = "WtTransTrack.findAll", query = "SELECT w FROM WtTransTrack w")})
@SequenceGenerator(name="WtTransTrackSeq",sequenceName="WT_TRANS_TRACK_PKSEQ",allocationSize = 1)
public class WtTransTrack extends AbstractEntity<WtTransTrack> implements Serializable {
  private static final long serialVersionUID = 1L;
  
  // <editor-fold defaultstate="collapsed" desc="Private Fields">
  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "WT_TRANS_TRACK_ID")
  @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "WtTransTrackSeq")
  private Integer wtTransTrackId;
  @Column(name = "WT_TRANS_ID")
  private Integer wtTransId;
  @Lob
  @Column(name = "TRANS_XML")
  private String transXml;
  @Column(name = "CREATED_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;
  @Column(name = "CREATED_BY")
  private String createdBy;
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Constructor">
  /**
   * Public Constructor  
   */
  public WtTransTrack() {
//    super();  
  }
  
  public WtTransTrack(Integer wtTransTrackId) {
    this.wtTransTrackId = wtTransTrackId;
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Private/Protected Methods">
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Public Methods">
  public Integer getWtTransTrackId() {
    return wtTransTrackId;
  }

  public void setWtTransTrackId(Integer wtTransTrackId) {
    this.wtTransTrackId = wtTransTrackId;
  }

  public Integer getWtTransId() {
    return wtTransId;
  }

  public void setWtTransId(Integer wtTransId) {
    this.wtTransId = wtTransId;
  }

  public String getTransXml() {
    return transXml;
  }

  public void setTransXml(String transXml) {
    this.transXml = transXml;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Object Overrides">
  /**
   * {@inheritDoc}
   * <p>OVERRIDE: </p>
   */
  @Override
  public int hashCode() {
    int hash = 0;
    hash += (wtTransTrackId != null ? wtTransTrackId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof WtTransTrack)) {
      return false;
    }
    WtTransTrack other = (WtTransTrack) object;
    if ((this.wtTransTrackId == null && other.wtTransTrackId != null) || (this.wtTransTrackId != null && !this.wtTransTrackId.equals(other.wtTransTrackId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.gei.entities.WtTransTrack[ wtTransTrackId=" + wtTransTrackId + " ]";
  }
  // </editor-fold>

}
