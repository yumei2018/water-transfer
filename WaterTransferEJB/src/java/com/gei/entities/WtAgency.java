
package com.gei.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 *
 * @author ymei
 */
@Entity
@Table(name = "WT_AGENCY")
@SequenceGenerator(name = "WtAgencySeq", sequenceName = "WT_AGENCY_PKSEQ", allocationSize = 1)
public class WtAgency extends AbstractEntity<WtAgency> implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @NotNull
  @GeneratedValue(generator = "WtAgencySeq", strategy = GenerationType.SEQUENCE)
  @Column(name = "WT_AGENCY_ID")
  private Integer wtAgencyId;
  @Column(name = "AGENCY_CODE")
  private String agencyCode;
  @Column(name = "AGENCY_FULL_NAME")
  private String agencyFullName;
  @Column(name = "AGENCY_ACTIVE_IND")
  private Integer agencyActiveInd;
  @Column(name = "CREATE_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createDate;
  @Column(name = "CREATED_BY_ID")
  private Integer createdById;
  @Column(name = "MODIFY_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date modifyDate;
  @Column(name = "UPDATED_BY_ID")
  private Integer updatedById;
  @Column(name = "AGENCY_TYPE")
  private String agencyType;

  @ManyToMany(mappedBy = "wtSellerCollection", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  private Collection<WtTrans> wtTransCollection;
  @ManyToMany(mappedBy = "wtApprovalCollection", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  private Collection<WtTrans> wtTransAppCollection;
//  @ManyToMany(mappedBy = "repAgencyCollection", cascade = CascadeType.ALL)
//  private Collection<AppUser> appUserCollection;
  @OneToMany(mappedBy = "wtAgency", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  private Collection<WtContact> wtContactCollection;
  @OneToMany(mappedBy = "wtAgency", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  private Collection<WtBuyer> wtBuyerCollection;
  
//  @JoinTable(name = "WT_REP_AGENCY", joinColumns = {
//    @JoinColumn(name = "WT_AGENCY_ID", referencedColumnName = "WT_AGENCY_ID")}, inverseJoinColumns = {
//    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")})
//  @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
//  @Fetch(value = FetchMode.SUBSELECT)
//  private Collection<AppUser> repUserCollection;

  public WtAgency() {
  }

  public WtAgency(Integer wtAgencyId) {
    this.wtAgencyId = wtAgencyId;
  }

  public WtAgency(Integer wtAgencyId, String agencyCode, Integer agencyActiveInd) {
    this.wtAgencyId = wtAgencyId;
    this.agencyCode = agencyCode;
    this.agencyActiveInd = agencyActiveInd;
  }

  public Integer getWtAgencyId() {
    return wtAgencyId;
  }

  public void setWtAgencyId(Integer wtAgencyId) {
    this.wtAgencyId = wtAgencyId;
  }

  public String getAgencyCode() {
    return agencyCode;
  }

  public void setAgencyCode(String agencyCode) {
    this.agencyCode = agencyCode;
  }

  public String getAgencyFullName() {
    return agencyFullName;
  }

  public void setAgencyFullName(String agencyFullName) {
    this.agencyFullName = agencyFullName;
  }

  public Integer getAgencyActiveInd() {
    return agencyActiveInd;
  }

  public void setAgencyActiveInd(Integer agencyActiveInd) {
    this.agencyActiveInd = agencyActiveInd;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Integer getCreatedById() {
    return createdById;
  }

  public void setCreatedById(Integer createdById) {
    this.createdById = createdById;
  }

  public Date getModifyDate() {
    return modifyDate;
  }

  public void setModifyDate(Date modifyDate) {
    this.modifyDate = modifyDate;
  }

  public Integer getUpdatedById() {
    return updatedById;
  }

  public void setUpdatedById(Integer updatedById) {
    this.updatedById = updatedById;
  }

  public String getAgencyType() {
    return agencyType;
  }

  public void setAgencyType(String agencyType) {
    this.agencyType = agencyType;
  }

  public Collection<WtTrans> getWtTransCollection() {
    return wtTransCollection;
  }

  public void setWtTransCollection(Collection<WtTrans> wtTransCollection) {
    this.wtTransCollection = wtTransCollection;
  }

  public Collection<WtTrans> getWtTransAppCollection() {
    return wtTransAppCollection;
  }

  public void setWtTransAppCollection(Collection<WtTrans> wtTransAppCollection) {
    this.wtTransAppCollection = wtTransAppCollection;
  }

  public Collection<WtContact> getWtContactCollection() {
    return wtContactCollection;
  }

  public void setWtContactCollection(Collection<WtContact> wtContactCollection) {
    this.wtContactCollection = wtContactCollection;
  }

  public Collection<WtBuyer> getWtBuyerCollection() {
    return wtBuyerCollection;
  }

  public void setWtBuyerCollection(Collection<WtBuyer> wtBuyerCollection) {
    this.wtBuyerCollection = wtBuyerCollection;
  }

//  public Collection<AppUser> getRepUserCollection() {
//    return repUserCollection;
//  }
//
//  public void setRepUserCollection(Collection<AppUser> repUserCollection) {
//    this.repUserCollection = repUserCollection;
//  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (wtAgencyId != null ? wtAgencyId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof WtAgency)) {
      return false;
    }
    WtAgency other = (WtAgency) object;
    if ((this.wtAgencyId == null && other.wtAgencyId != null) || (this.wtAgencyId != null && !this.wtAgencyId.equals(other.wtAgencyId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.gei.entities.WtAgency[ wtAgencyId=" + wtAgencyId + " ]";
  }

  public boolean isMember(Collection<WtAgency> agencyCollection) {
    for (WtAgency agency : agencyCollection) {
      if (agency.equals(this)) {
        return true;
      }
    }
    return false;
  }

  public boolean isBuyer(Collection<WtBuyer> buyerCollection) {
    for (WtBuyer buyer : buyerCollection) {
      if (buyer.getWtAgency().equals(this)) {
        return true;
      }
    }
    return false;
  }
  
  public boolean isBuyer() {
    if (this.wtBuyerCollection != null && this.wtBuyerCollection.size()>0) {
      return true;
    }
    return false;
  }

//  public boolean hasRepUser(int userId) {
//    if (repUserCollection == null || repUserCollection.isEmpty()) {
//      return false;
//    }
//    for (AppUser user : repUserCollection) {
//      if (user.getUserId() == userId) {
//        return true;
//      }
//    }
//    return false;
//  }
}
