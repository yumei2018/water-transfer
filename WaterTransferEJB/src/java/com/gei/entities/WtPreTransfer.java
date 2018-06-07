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
@Table(name = "WT_PRE_TRANSFER")
@NamedQueries({
  @NamedQuery(name = "WtPreTransfer.findAll", query = "SELECT w FROM WtPreTransfer w")})
@SequenceGenerator(name="WtPreTransferSeq",sequenceName="WT_PRE_TRANSFER_PKSEQ",allocationSize = 1)
public class WtPreTransfer extends AbstractEntity<WtPreTransfer> implements Serializable {
  private static final long serialVersionUID = 1L;
  
  // <editor-fold defaultstate="collapsed" desc="Private Fields">
  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "WT_PRE_TRANSFER_ID")
  @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "WtPreTransferSeq")
  private Integer wtPreTransferId;
  @Column(name = "SWPAO_CONTRACT_NUM")
  private String swpaoContractNum;
  @Column(name = "RECOM_NUM")
  private String recomNum;
  @Column(name = "IS_TYPE_CI")
  private Integer isTypeCi;
  @Column(name = "IS_TYPE_RV")
  private Integer isTypeRv;
  @Column(name = "IS_TYPE_GW")
  private Integer isTypeGw;
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
  @JoinColumn(name = "WT_TRANS_ID", referencedColumnName = "WT_TRANS_ID")
  @ManyToOne(optional = false)
  private WtTrans wtTrans;
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Public Methods">
  public WtPreTransfer() {
  }
  
  public WtPreTransfer(Integer wtPreTransferId) {
    this.wtPreTransferId = wtPreTransferId;
  }

  public Integer getWtPreTransferId() {
    return wtPreTransferId;
  }

  public void setWtPreTransferId(Integer wtPreTransferId) {
    this.wtPreTransferId = wtPreTransferId;
  }

  public String getSwpaoContractNum() {
    return swpaoContractNum;
  }

  public void setSwpaoContractNum(String swpaoContractNum) {
    this.swpaoContractNum = swpaoContractNum;
  }

  public String getRecomNum() {
    return recomNum;
  }

  public void setRecomNum(String recomNum) {
    this.recomNum = recomNum;
  }

  public Integer getIsTypeCi() {
    return isTypeCi;
  }

  public void setIsTypeCi(Integer isTypeCi) {
    this.isTypeCi = isTypeCi;
  }

  public Integer getIsTypeRv() {
    return isTypeRv;
  }

  public void setIsTypeRv(Integer isTypeRv) {
    this.isTypeRv = isTypeRv;
  }

  public Integer getIsTypeGw() {
    return isTypeGw;
  }

  public void setIsTypeGw(Integer isTypeGw) {
    this.isTypeGw = isTypeGw;
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
  
  public WtTrans getWtTrans() {
    return wtTrans;
  }

  public void setWtTrans(WtTrans wtTrans) {
    this.wtTrans = wtTrans;
  }
  // </editor-fold>

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (wtPreTransferId != null ? wtPreTransferId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof WtPreTransfer)) {
      return false;
    }
    WtPreTransfer other = (WtPreTransfer) object;
    if ((this.wtPreTransferId == null && other.wtPreTransferId != null) || (this.wtPreTransferId != null && !this.wtPreTransferId.equals(other.wtPreTransferId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.gei.entities.WtPreTransfer[ wtPreTransferId=" + wtPreTransferId + " ]";
  }
}
