
package com.gei.entities;

import com.gei.io.xml.JAXBHelper;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.namespace.QName;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import static org.springframework.test.util.AssertionErrors.fail;

/**
 *
 * @author ymei
 */
@Entity
@Table(name = "WT_TRANS")
@SequenceGenerator(name="WtTransSeq",sequenceName="WT_TRANS_PKSEQ",allocationSize = 1)
public class WtTrans extends AbstractEntity<WtTrans> implements Serializable {
  private static final long serialVersionUID = 1L;
  
  // <editor-fold defaultstate="collapsed" desc="Private Fields">
  @Id
  @Basic(optional = false)
  @NotNull
  @GeneratedValue(generator = "WtTransSeq", strategy = GenerationType.SEQUENCE)
  @Column(name = "WT_TRANS_ID")
  private Integer wtTransId;
  @Column(name = "WT_TRANS_NUM")
  private String wtTransNum;
  @Column(name = "TRANS_YEAR")
  private Integer transYear;
  @Column(name = "PRO_TRANS_QUA")
  private Double proTransQua;
  @Column(name = "ACT_TRANS_QUA")
  private Integer actTransQua;
  @Column(name = "DWR_PRO_APPR_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date dwrProApprDate;
  @Column(name = "TRANS_WIN_START")
  @Temporal(TemporalType.TIMESTAMP)
  private Date transWinStart;
  @Column(name = "TRANS_WIN_END")
  @Temporal(TemporalType.TIMESTAMP)
  private Date transWinEnd;
  @Column(name = "PRO_AGREE_PAID")
  private Double proAgreePaid;
  @Column(name = "PRO_AGREE_PAID_RANGE")
  private String proAgreePaidRange;
  @Column(name = "ACT_AMT_PAID")
  private Double actAmtPaid;
  @Column(name = "CAL_AMT_PAID")
  private Double calAmtPaid;
  @Column(name = "PRO_UNIT_COST")
  private Double proUnitCost;
  @Column(name = "CAL_UNIT_COST")
  private Double calUnitCost;
  @Column(name = "PRO_ACR_IDLE")
  private Integer proAcrIdle;
  @Column(name = "PRO_ACR_IDLE_IND")
  private Integer proAcrIdleInd;
  @Column(name = "ACT_FALL_ACR")
  private Integer actFallAcr;
  @Column(name = "ACT_FALL_ACR_IND")
  private Integer actFallAcrInd;
  @Column(name = "RES_RE_OP_IND")
  private Integer resReOpInd;
  @Column(name = "CONS_WATER_IND")
  private Integer consWaterInd;
  @Column(name = "WELL_USE_NUM")
  private Integer wellUseNum;
  @Column(name = "WELL_USE_NUM_IND")
  private Integer wellUseNumInd;
  @Column(name = "WT_COMM")
  private String wtComm;
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
  @Column(name = "REQ_EXP_WIN")
  private String reqExpWin;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "PRO_RECEIVED_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date proReceivedDate;
  @Column(name = "SWRCB_APP")
  private String swrcbApp;
  @Column(name = "SWRCB_APP_PROCESS")  
  private String swrcbAppProcess;
  @Column(name = "WATER_AVL_TIMING")
  private String waterAvlTiming;
  @Column(name = "DELTA_TRANSFER_IND")
  private Integer deltaTransferInd;
  @Column(name = "DELTA_TRANSFER_COMM")
  private String deltaTransferComm;
  @Column(name = "SUR_WATER_SOURCE")
  private String surWaterSource;
  @Column(name = "REQ_EXP_FROM")
  private String reqExpFrom;
  @Column(name = "REQ_EXP_TO")
  private String reqExpTo;
  @Column(name = "REQ_EXP_FROM_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date reqExpFromDate;
  @Column(name = "REQ_EXP_TO_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date reqExpToDate;
  @Column(name = "REQ_STORAGE_EXP")
  private String reqStorageExp;
  @Column(name = "IS_SHORT_LONG")
  private Integer isShortLong;
  @Column(name = "IS_STATE_CONTRACTOR")
  private Integer isStateContractor;
  @Column(name = "IS_FISHERIES_REVIEW")
  private Integer isFisheriesReview;
  @Column(name = "FISHERIES_APPROVAL_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date fisheriesApprocalDate;
  @Column(name = "ASSIGNED_REVIEWER")
  private String assignedReviewer;
  @Column(name = "DWR_COMMENTS")
  private String dwrComments;  
  @Column(name = "SWPAO_REVIEWER")
  private String swpaoReviewer;
  @Column(name = "REGION_REVIEWER")
  private String regionReviewer;
  @Column(name = "USBR_REVIEWER")
  private String usbrReviewer;
  @Column(name = "SWPAO_CONTRACT_NUM")
  private String swpaoContractNum;
  @Column(name = "MAJOR_RIVER_ATTRIBUTE")
  private String majorRiverAttribute;
  @Column(name = "TRANS_DESCRIPTION")
  private String transDescription;
  @Column(name = "REPORT_COMMENT")
  private String reportComment;
  @Column(name = "HAS_PRE_TRANS")
  private Integer hasPreTrans;
  @Column(name = "IS_ACTIVE")
  private Integer isActive;
  @Column(name = "CONTRACT_AMOUNT")
  private Double contractAmount;
  @Column(name = "CI_REVIEWER")
  private String ciReviewer;
  @Column(name = "GS_REVIEWER")
  private String gsReviewer;
  @Column(name = "RR_REVIEWER")
  private String rrReviewer;
  // </editor-fold>
    
  // <editor-fold defaultstate="collapsed" desc="Private Relationships">
  //    @JoinTable(name = "WT_TRANS_TYPE", joinColumns = {
//        @JoinColumn(name = "WT_TRANS_ID", referencedColumnName = "WT_TRANS_ID")}, inverseJoinColumns = {
//        @JoinColumn(name = "WT_FU_TYPE_ID", referencedColumnName = "WT_FU_TYPE_ID")})
//    @ManyToMany(cascade = CascadeType.PERSIST,fetch=FetchType.LAZY)
//    private Collection<WtFuType> wtFuTypeCollection;
  @JoinTable(name = "WT_SELLER", joinColumns = {
    @JoinColumn(name = "WT_TRANS_ID", referencedColumnName = "WT_TRANS_ID")}, inverseJoinColumns = {
    @JoinColumn(name = "WT_AGENCY_ID", referencedColumnName = "WT_AGENCY_ID")})
  @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  private Collection<WtAgency> wtSellerCollection;
  @JoinTable(name = "WT_BUYERS_CONTACT", joinColumns = {
    @JoinColumn(name = "WT_TRANS_ID", referencedColumnName = "WT_TRANS_ID")}, inverseJoinColumns = {
    @JoinColumn(name = "WT_CONTACT_ID", referencedColumnName = "WT_CONTACT_ID")})
//    @ManyToOne(cascade = CascadeType.PERSIST,fetch=FetchType.LAZY)
//    @ManyToOne
//    private WtContact wtBuyersContact;
  @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  private Collection<WtContact> buyersContactCollection;
  @JoinTable(name = "WT_APP_AGENCY", joinColumns = {
    @JoinColumn(name = "WT_TRANS_ID", referencedColumnName = "WT_TRANS_ID")}, inverseJoinColumns = {
    @JoinColumn(name = "WT_AGENCY_ID", referencedColumnName = "WT_AGENCY_ID")})
  @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  private Collection<WtAgency> wtApprovalCollection;
  @JoinColumn(name = "WT_STATUS_FLAG_ID", referencedColumnName = "WT_STATUS_FLAG_ID")
  @ManyToOne
  private WtStatusFlag wtStatusFlag;
  @JoinTable(name = "WT_TRANS_USER", joinColumns = {
    @JoinColumn(name = "WT_TRANS_ID", referencedColumnName = "WT_TRANS_ID")}, inverseJoinColumns = {
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")})
  @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
//    @ManyToMany(mappedBy = "wtTransCollection",fetch = FetchType.EAGER)
  @Fetch(value = FetchMode.SUBSELECT)
  private Collection<AppUser> appUserCollection;
  @ManyToMany(cascade = CascadeType.ALL, mappedBy = "wtTransCollection")
  private Collection<WtCounty> wtCountyCollection;
  
  @XmlTransient
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "wtTrans")
  private Collection<WtTransType> wtTransTypeCollection;  
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "wtTrans")
  private Collection<WtBuyer> wtBuyerCollection;    
  
  @XmlTransient
  @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "wtTrans", fetch = FetchType.LAZY)
  private Collection<WtAttachment> wtAttachmentCollection;
  @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "wtTransR", fetch = FetchType.LAZY)
  private Collection<WtAttachment> wtReportCollection;
  @OneToOne(cascade = CascadeType.ALL, mappedBy = "wtTrans")
//  @OneToOne(cascade = CascadeType.PERSIST, mappedBy = "wtTrans", fetch = FetchType.LAZY)
  private WtCropIdling wtCropIdling;
  @OneToOne(cascade = CascadeType.ALL, mappedBy = "wtTrans")
//  @OneToOne(cascade = CascadeType.PERSIST, mappedBy = "wtTrans", fetch = FetchType.LAZY)
  private WtReservoir wtReservoir;
  @OneToOne(cascade = CascadeType.ALL, mappedBy = "wtTrans")
//  @OneToOne(cascade = CascadeType.PERSIST, mappedBy = "wtTrans", fetch = FetchType.LAZY)
  private WtGroundwater wtGroundwater;
  @XmlTransient
  @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "wtTrans", fetch = FetchType.LAZY)
  private Collection<WtWaterRights> wtWaterRightsCollection;
  @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "wtTrans", fetch = FetchType.LAZY)
  private Collection<WtTrackFile> wtTrackFileCollection;
  @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "wtTrans", fetch = FetchType.LAZY)
  private Collection<WtReviewComment> wtReviewCommentCollection;
  @XmlTransient
  @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "wtTrans", fetch = FetchType.LAZY)
  private Collection<WtPreTransfer> wtPreTransferCollection;
  
  @OneToOne(cascade = CascadeType.ALL, mappedBy = "wtTrans")
  private WtAgencyApproval wtAgencyApproval;
  @XmlTransient
  @OneToOne(cascade = CascadeType.ALL, mappedBy = "wtTrans")
  private WtTransSwrcb wtTransSwrcb;
  @XmlTransient
  @OneToOne(cascade = CascadeType.ALL, mappedBy = "wtTrans")
  private WtTransCeqa wtTransCeqa;
  @XmlTransient
  @OneToOne(cascade = CascadeType.ALL, mappedBy = "wtTrans")
  private WtTransNepa wtTransNepa;
  @XmlTransient
  @OneToOne(cascade = CascadeType.ALL, mappedBy = "wtTrans")
  private WtTransReach wtTransReach;  
  @OneToOne(cascade = CascadeType.ALL, mappedBy = "wtTrans")
  private WtWaterLoss wtWaterLoss;
  // </editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Constructor">
  public WtTrans() {
  }
  
  public WtTrans(Integer wtTransId) {
    this.wtTransId = wtTransId;
  }
  
  public WtTrans(Integer wtTransId, String wtTransNum, Integer transYear, Integer proAcrIdleInd, Integer actFallAcrInd, Integer resReOpInd, Integer consWaterInd, Integer wellUseNumInd) {
    this.wtTransId = wtTransId;
    this.wtTransNum = wtTransNum;
    this.transYear = transYear;
    this.proAcrIdleInd = proAcrIdleInd;
    this.actFallAcrInd = actFallAcrInd;
    this.resReOpInd = resReOpInd;
    this.consWaterInd = consWaterInd;
    this.wellUseNumInd = wellUseNumInd;
  }
//</editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Public Fields Get/Set Methods">
  public Integer getWtTransId() {
    return wtTransId;
  }
  
  public void setWtTransId(Integer wtTransId) {
    this.wtTransId = wtTransId;
  }
  
  public String getWtTransNum() {
    return wtTransNum;
  }
  
  public void setWtTransNum(String wtTransNum) {
    this.wtTransNum = wtTransNum;
  }
  
  public Integer getTransYear() {
    return transYear;
  }
  
  public void setTransYear(Integer transYear) {
    this.transYear = transYear;
  }
  
  public Double getProTransQua() {
    return proTransQua;
  }
  
  public void setProTransQua(Double proTransQua) {
    this.proTransQua = proTransQua;
  }
  
  public Integer getActTransQua() {
    return actTransQua;
  }
  
  public void setActTransQua(Integer actTransQua) {
    this.actTransQua = actTransQua;
  }
  
  public Date getDwrProApprDate() {
    return dwrProApprDate;
  }
  
  public void setDwrProApprDate(Date dwrProApprDate) {
    this.dwrProApprDate = dwrProApprDate;
  }
  
  public Date getTransWinStart() {
    return transWinStart;
  }
  
  public void setTransWinStart(Date transWinStart) {
    this.transWinStart = transWinStart;
  }
  
  public Date getTransWinEnd() {
    return transWinEnd;
  }
  
  public void setTransWinEnd(Date transWinEnd) {
    this.transWinEnd = transWinEnd;
  }
  
  public Double getProAgreePaid() {
    return proAgreePaid;
  }
  
  public void setProAgreePaid(Double proAgreePaid) {
    this.proAgreePaid = proAgreePaid;
  }
  
  public String getProAgreePaidRange() {
    return proAgreePaidRange;
  }
  
  public void setProAgreePaidRange(String proAgreePaidRange) {
    this.proAgreePaidRange = proAgreePaidRange;
  }
  
  public Double getActAmtPaid() {
    return actAmtPaid;
  }
  
  public void setActAmtPaid(Double actAmtPaid) {
    this.actAmtPaid = actAmtPaid;
  }
  
  public Double getCalAmtPaid() {
    return calAmtPaid;
  }
  
  public void setCalAmtPaid(Double calAmtPaid) {
    this.calAmtPaid = calAmtPaid;
  }
  
  public Double getProUnitCost() {
    return proUnitCost;
  }
  
  public void setProUnitCost(Double proUnitCost) {
    this.proUnitCost = proUnitCost;
  }
  
  public Double getCalUnitCost() {
    return calUnitCost;
  }
  
  public void setCalUnitCost(Double calUnitCost) {
    this.calUnitCost = calUnitCost;
  }
  
  @XmlTransient
  public Integer getProAcrIdle() {
    return proAcrIdle;
  }
  
  public void setProAcrIdle(Integer proAcrIdle) {
    this.proAcrIdle = proAcrIdle;
  }
  
  @XmlTransient
  public Integer getProAcrIdleInd() {
    return proAcrIdleInd;
  }
  
  public void setProAcrIdleInd(Integer proAcrIdleInd) {
    this.proAcrIdleInd = proAcrIdleInd;
  }
  
  @XmlTransient
  public Integer getActFallAcr() {
    return actFallAcr;
  }
  
  public void setActFallAcr(Integer actFallAcr) {
    this.actFallAcr = actFallAcr;
  }
  
  @XmlTransient
  public Integer getActFallAcrInd() {
    return actFallAcrInd;
  }
  
  public void setActFallAcrInd(Integer actFallAcrInd) {
    this.actFallAcrInd = actFallAcrInd;
  }
  
  @XmlTransient
  public Integer getResReOpInd() {
    return resReOpInd;
  }
  
  public void setResReOpInd(Integer resReOpInd) {
    this.resReOpInd = resReOpInd;
  }
  
  @XmlTransient
  public Integer getConsWaterInd() {
    return consWaterInd;
  }
  
  public void setConsWaterInd(Integer consWaterInd) {
    this.consWaterInd = consWaterInd;
  }
  
  @XmlTransient
  public Integer getWellUseNum() {
    return wellUseNum;
  }
  
  public void setWellUseNum(Integer wellUseNum) {
    this.wellUseNum = wellUseNum;
  }
  
  @XmlTransient
  public Integer getWellUseNumInd() {
    return wellUseNumInd;
  }
  
  public void setWellUseNumInd(Integer wellUseNumInd) {
    this.wellUseNumInd = wellUseNumInd;
  }
  
  public String getWtComm() {
    return wtComm;
  }
  
  public void setWtComm(String wtComm) {
    this.wtComm = wtComm;
  }
  
  @XmlTransient
  public Date getCreateDate() {
    return createDate;
  }
  
  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }
  
  @XmlTransient
  public Integer getCreatedById() {
    return createdById;
  }
  
  public void setCreatedById(Integer createdById) {
    this.createdById = createdById;
  }
  
  @XmlTransient
  public Date getModifyDate() {
    return modifyDate;
  }
  
  public void setModifyDate(Date modifyDate) {
    this.modifyDate = modifyDate;
  }
  
  @XmlTransient
  public Integer getUpdatedById() {
    return updatedById;
  }
  
  public void setUpdatedById(Integer updatedById) {
    this.updatedById = updatedById;
  }
  
  public String getReqExpWin() {
    return reqExpWin;
  }
  
  public void setReqExpWin(String reqExpWin) {
    this.reqExpWin = reqExpWin;
  }
  
  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public Date getProReceivedDate() {
    return proReceivedDate;
  }
  
  public void setProReceivedDate(Date proReceivedDate) {
    this.proReceivedDate = proReceivedDate;
  }
  
  public String getSwrcbApp() {
    return swrcbApp;
  }
  
  public void setSwrcbApp(String swrcbApp) {
    this.swrcbApp = swrcbApp;
  }
  
  public String getSwrcbAppProcess() {
    return swrcbAppProcess;
  }
  
  public void setSwrcbAppProcess(String swrcbAppProcess) {
    this.swrcbAppProcess = swrcbAppProcess;
  }  
  
  public String getWaterAvlTiming() {
    return waterAvlTiming;
  }
  
  public void setWaterAvlTiming(String waterAvlTiming) {
    this.waterAvlTiming = waterAvlTiming;
  }
  
  public Integer getDeltaTransferInd() {
    return deltaTransferInd;
  }
  
  public void setDeltaTransferInd(Integer deltaTransferInd) {
    this.deltaTransferInd = deltaTransferInd;
  }
  
  public String getDeltaTransferComm() {
    return deltaTransferComm;
  }
  
  public void setDeltaTransferComm(String deltaTransferComm) {
    this.deltaTransferComm = deltaTransferComm;
  }  
  
  @XmlTransient
  public String getAssignedReviewer() {
    return assignedReviewer;
  }
  
  public void setAssignedReviewer(String assignedReviewer) {
    this.assignedReviewer = assignedReviewer;
  }

  @XmlTransient
  public String getSwpaoReviewer() {
    return swpaoReviewer;
  }
  
  public void setSwpaoReviewer(String swpaoReviewer) {
    this.swpaoReviewer = swpaoReviewer;
  }
  
  @XmlTransient
  public String getRegionReviewer() {
    return regionReviewer;
  }
  
  public void setRegionReviewer(String regionReviewer) {
    this.regionReviewer = regionReviewer;
  }
  
  @XmlTransient
  public String getUsbrReviewer() {
    return usbrReviewer;
  }
  
  public void setUsbrReviewer(String usbrReviewer) {
    this.usbrReviewer = usbrReviewer;
  }
  
  @XmlTransient
  public String getSwpaoContractNum() {
    return swpaoContractNum;
  }
  
  public void setSwpaoContractNum(String swpaoContractNum) {
    this.swpaoContractNum = swpaoContractNum;
  }
  
  public String getMajorRiverAttribute() {
    return majorRiverAttribute;
  }
  
  public void setMajorRiverAttribute(String majorRiverAttribute) {
    this.majorRiverAttribute = majorRiverAttribute;
  }
  
  public String getTransDescription() {
    return transDescription;
  }
  
  public void setTransDescription(String transDescription) {
    this.transDescription = transDescription;
  } 

  @XmlTransient
  public String getReportComment() {
    return reportComment;
  }

  public void setReportComment(String reportComment) {
    this.reportComment = reportComment;
  }

  public Integer getHasPreTrans() {
    return hasPreTrans;
  }

  public void setHasPreTrans(Integer hasPreTrans) {
    this.hasPreTrans = hasPreTrans;
  }

  public Integer getIsActive() {
    return isActive;
  }

  public void setIsActive(Integer isActive) {
    this.isActive = isActive;
  } 

  public String getCiReviewer() {
    return ciReviewer;
  }

  public void setCiReviewer(String ciReviewer) {
    this.ciReviewer = ciReviewer;
  }

  public String getGsReviewer() {
    return gsReviewer;
  }

  public void setGsReviewer(String gsReviewer) {
    this.gsReviewer = gsReviewer;
  }

  public String getRrReviewer() {
    return rrReviewer;
  }

  public void setRrReviewer(String rrReviewer) {
    this.rrReviewer = rrReviewer;
  }
  // </editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Public Relationship Get/Set Methods">
  @XmlElement(name = "WtTransType", type = WtTransType.class)
  public Collection<WtTransType> getWtTransTypeCollection() {
    return wtTransTypeCollection;
  }
  
  public void setWtTransTypeCollection(Collection<WtTransType> wtTransTypeCollection) {
    this.wtTransTypeCollection = wtTransTypeCollection;
  }
  
//    /**
//      * @return the wtSellerAgency
//      */
//     public WtAgency getWtSellerAgency() {
//       return wtSellerAgency;
//     }
//
//     /**
//      * @param wtSellerAgency the wtSellerAgency to set
//      */
//     public void setWtSellerAgency(WtAgency wtSellerAgency) {
//       this.wtSellerAgency = wtSellerAgency;
//     }
//    @XmlElement(name = "WtStatusFlag", type = WtStatusFlag.class)
  @XmlTransient
  public WtStatusFlag getWtStatusFlag() {
//    if ((this.wtStatusFlag == null) && (this.wtTransId != null)) {
//      WtStatusFlagFacade facade = WebUtil.getFacade(WtStatusFlagFacade.class);
//      String query = String.format("SELECT t1.* FROM WT_STATUS_FLAG t1\n" +
//                                    "  JOIN WT_TRANS t2\n" +
//                                    "  ON t1.WT_STATUS_FLAG_ID = t2.WT_STATUS_FLAG_ID\n" +
//                                    "WHERE t2.WT_TRANS_ID = %d",this.wtTransId);
//      List<WtStatusFlag> statuses = facade.select(query,WtStatusFlag.class);
//      if ((statuses != null) && (!statuses.isEmpty())) {
//        this.wtStatusFlag = statuses.get(0);
//      }
//    }
    return wtStatusFlag;
  }
  
  public void setWtStatusFlag(WtStatusFlag wtStatusFlag) {
    this.wtStatusFlag = wtStatusFlag;
  }
  
//    @XmlElement(name = "WsSeller", type = WtAgency.class)
  @XmlTransient
  public void setWtSellerCollection(Collection<WtAgency> wtSellerCollection) {
    this.wtSellerCollection = wtSellerCollection;
  }
  
  public Collection<WtAgency> getWtSellerCollection() {
    return wtSellerCollection;
  }
  
  @XmlTransient
  public Collection<AppUser> getAppUserCollection() {
    return appUserCollection;
  }
  
  public void setAppUserCollection(Collection<AppUser> appUserCollection) {
    this.appUserCollection = appUserCollection;
  }
  
  @XmlTransient
  public Collection<WtAgency> getWtApprovalCollection() {
    return wtApprovalCollection;
  }
  
  public void setWtApprovalCollection(Collection<WtAgency> wtApprovalCollection) {
    this.wtApprovalCollection = wtApprovalCollection;
  }
  
//    @XmlElement(name = "WsBuyer", type = WtBuyer.class)
  @XmlTransient
  public Collection<WtBuyer> getWtBuyerCollection() {
    return wtBuyerCollection;
  }
  
  public void setWtBuyerCollection(Collection<WtBuyer> wtBuyerCollection) {
    this.wtBuyerCollection = wtBuyerCollection;
  }

  @XmlTransient
  public Collection<WtCounty> getWtCountyCollection() {
    return wtCountyCollection;
  }

  public void setWtCountyCollection(Collection<WtCounty> wtCountyCollection) {
    this.wtCountyCollection = wtCountyCollection;
  }
  
  @XmlTransient
  public Collection<WtAttachment> getWtAttachmentCollection() {
//    if ((this.wtAttachmentCollection == null) && (this.wtTransId != null)) {
//      WtAttachmentFacade facade = WebUtil.getFacade(WtAttachmentFacade.class);
//      String query = String.format("SELECT WT_ATTACHMENT_ID \n" +
//                                    "FROM WT_TRANS_ATTACHMENTS \n" +
//                                    "WHERE WT_TRANS_ID = %d",this.wtTransId);
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
  
  @XmlTransient
  public Collection<WtAttachment> getWtReportCollection() {
//    if ((this.wtReportCollection == null) && (this.wtTransId != null)) {
//      WtAttachmentFacade facade = WebUtil.getFacade(WtAttachmentFacade.class);
//      String query = String.format("SELECT WT_ATTACHMENT_ID \n" +
//                                    "FROM WT_TRANS_REPORT \n" +
//                                    "WHERE WT_TRANS_ID = %d",this.wtTransId);
//      List<Map> results = facade.select(query);
//      if (results != null) {
//        this.wtReportCollection = new ArrayList<>();
//        WtAttachment att = null;
//        
//        for (Map m : results) {
//          att = facade.find(new WtAttachment(((java.math.BigDecimal) m.get("wtAttachmentId")).intValue()));
//          if (att == null) {
//            continue;
//          }
//          this.wtReportCollection.add(att);
//        }
//      }
//    }
    
    return wtReportCollection;
  }
  
  public void setWtReportCollection(Collection<WtAttachment> wtReportCollection) {
    this.wtReportCollection = wtReportCollection;
  }
  
  @XmlTransient
  public WtCropIdling getWtCropIdling() {
//    if ((this.wtCropIdling == null) && (this.wtTransId != null)) {
//      WtTransFacade facade = WebUtil.getFacade(WtTransFacade.class);
//      String query = String.format("SELECT * FROM WT_CROP_IDLING\n" +
//                                    "WHERE WT_TRANS_ID = %d",this.wtTransId);
//      List<WtCropIdling> gws = facade.select(query,WtCropIdling.class);
//      if ((gws != null) && (!gws.isEmpty())) {
//        this.wtCropIdling = gws.get(0);
//      }
//    }
    return wtCropIdling;
  }
  
  public void setWtCropIdling(WtCropIdling wtCropIdling) {
    this.wtCropIdling = wtCropIdling;
  }
  
  @XmlTransient
  public WtGroundwater getWtGroundwater() {
//    if ((this.wtGroundwater == null) && (this.wtTransId != null)) {
//      WtTransFacade facade = WebUtil.getFacade(WtTransFacade.class);
//      String query = String.format("SELECT * FROM WT_GROUNDWATER\n" +
//                                    "WHERE WT_TRANS_ID = %d",this.wtTransId);
//      List<WtGroundwater> gws = facade.select(query,WtGroundwater.class);
//      if ((gws != null) && (!gws.isEmpty())) {
//        this.wtGroundwater = gws.get(0);
//      }
//    }
    return wtGroundwater;
  }
  
  public void setWtGroundwater(WtGroundwater wtGroundwater) {
    this.wtGroundwater = wtGroundwater;
  }
  
  @XmlTransient
  public WtReservoir getWtReservoir() {
//    if ((this.wtReservoir == null) && (this.wtTransId != null)) {
//      WtTransFacade facade = WebUtil.getFacade(WtTransFacade.class);
//      String query = String.format("SELECT * FROM WT_RESERVOIR\n" +
//                                    "WHERE WT_TRANS_ID = %d",this.wtTransId);
//      List<WtReservoir> gws = facade.select(query,WtReservoir.class);
//      if ((gws != null) && (!gws.isEmpty())) {
//        this.wtReservoir = gws.get(0);
//      }
//    }
    return wtReservoir;
  }
  
  public void setWtReservoir(WtReservoir wtReservoir) {
    this.wtReservoir = wtReservoir;
  }
  
//    @XmlElement(name = "WtWaterRights", type = WtWaterRights.class)
  @XmlTransient
  public Collection<WtWaterRights> getWtWaterRightsCollection() {
    return wtWaterRightsCollection;
  }
  
  public void setWtWaterRightsCollection(Collection<WtWaterRights> wtWaterRightsCollection) {
    this.wtWaterRightsCollection = wtWaterRightsCollection;
  }

  @XmlTransient
  public Collection<WtPreTransfer> getWtPreTransferCollection() {
    return wtPreTransferCollection;
  }

  public void setWtPreTransferCollection(Collection<WtPreTransfer> wtPreTransferCollection) {
    this.wtPreTransferCollection = wtPreTransferCollection;
  }
  
  @XmlTransient
  public Collection<WtTrackFile> getWtTrackFileCollection() {
    return wtTrackFileCollection;
  }
  
  public void setWtTrackFileCollection(Collection<WtTrackFile> wtTrackFileCollection) {
    this.wtTrackFileCollection = wtTrackFileCollection;
  }
  
  @XmlTransient
  public Collection<WtReviewComment> getWtReviewCommentCollection() {
    return wtReviewCommentCollection;
  }
  
  public void setWtReviewCommentCollection(Collection<WtReviewComment> wtReviewCommentCollection) {
    this.wtReviewCommentCollection = wtReviewCommentCollection;
  }
  
  public String getSurWaterSource() {
    return surWaterSource;
  }
  
  public void setSurWaterSource(String surWaterSource) {
    this.surWaterSource = surWaterSource;
  }
  
  public String getReqExpFrom() {
    return reqExpFrom;
  }
  
  public void setReqExpFrom(String reqExpFrom) {
    this.reqExpFrom = reqExpFrom;
  }
  
  public String getReqExpTo() {
    return reqExpTo;
  }
  
  public void setReqExpTo(String reqExpTo) {
    this.reqExpTo = reqExpTo;
  }

  public Date getReqExpFromDate() {
    return reqExpFromDate;
  }

  public void setReqExpFromDate(Date reqExpFromDate) {
    this.reqExpFromDate = reqExpFromDate;
  }

  public Date getReqExpToDate() {
    return reqExpToDate;
  }

  public void setReqExpToDate(Date reqExpToDate) {
    this.reqExpToDate = reqExpToDate;
  }
  
  public String getReqStorageExp() {
    return reqStorageExp;
  }
  
  public void setReqStorageExp(String reqStorageExp) {
    this.reqStorageExp = reqStorageExp;
  }
  
  public Integer getIsShortLong() {
    return isShortLong;
  }
  
  public void setIsShortLong(Integer isShortLong) {
    this.isShortLong = isShortLong;
  }
  
  public Integer getIsStateContractor() {
    return isStateContractor;
  }
  
  public void setIsStateContractor(Integer isStateContractor) {
    this.isStateContractor = isStateContractor;
  }
  
  @XmlTransient
  public Integer getIsFisheriesReview() {
    return isFisheriesReview;
  }
  
  public void setIsFisheriesReview(Integer isFisheriesReview) {
    this.isFisheriesReview = isFisheriesReview;
  }
  
  @XmlTransient
  public Date getFisheriesApprocalDate() {
    return fisheriesApprocalDate;
  }
  
  public void setFisheriesApprocalDate(Date fisheriesApprocalDate) {
    this.fisheriesApprocalDate = fisheriesApprocalDate;
  }
  
  public String getDwrComments() {
    return dwrComments;
  }
  
  public void setDwrComments(String dwrComments) {
    this.dwrComments = dwrComments;
  }

  public Double getContractAmount() {
    return contractAmount;
  }

  public void setContractAmount(Double contractAmount) {
    this.contractAmount = contractAmount;
  }
  
  @XmlTransient
  public WtAgencyApproval getWtAgencyApproval() {
    return wtAgencyApproval;
  }
  
  public void setWtAgencyApproval(WtAgencyApproval wtAgencyApproval) {
    this.wtAgencyApproval = wtAgencyApproval;
  }
  
//    @XmlTransient
  @XmlElement(name = "WtTransSwrcb", type = WtTransSwrcb.class)
  public WtTransSwrcb getWtTransSwrcb() {
    return wtTransSwrcb;
  }
  
  public void setWtTransSwrcb(WtTransSwrcb wtTransSwrcb) {
    this.wtTransSwrcb = wtTransSwrcb;
  }
  
//    @XmlTransient
  @XmlElement(name = "WtTransCeqa", type = WtTransCeqa.class)
  public WtTransCeqa getWtTransCeqa() {
    return wtTransCeqa;
  }
  
  public void setWtTransCeqa(WtTransCeqa wtTransCeqa) {
    this.wtTransCeqa = wtTransCeqa;
  }
  
//    @XmlTransient
  @XmlElement(name = "WtTransNepa", type = WtTransNepa.class)
  public WtTransNepa getWtTransNepa() {
    return wtTransNepa;
  }
  
  public void setWtTransNepa(WtTransNepa wtTransNepa) {
    this.wtTransNepa = wtTransNepa;
  }
  
//    @XmlTransient
  @XmlElement(name = "WtTransReach", type = WtTransReach.class)
  public WtTransReach getWtTransReach() {
    return wtTransReach;
  }
  
  public void setWtTransReach(WtTransReach wtTransReach) {
    this.wtTransReach = wtTransReach;
  }
  
  @XmlTransient
  public WtWaterLoss getWtWaterLoss() {
    return wtWaterLoss;
  }
  
  public void setWtWaterLoss(WtWaterLoss wtWaterLoss) {
    this.wtWaterLoss = wtWaterLoss;
  }
  
//    @XmlTransient
//    public WtContact getWtBuyersContact() {
//      return wtBuyersContact;
//    }
//
//    public void setWtBuyersContact(WtContact wtBuyersContact) {
//      this.wtBuyersContact = wtBuyersContact;
//    }
  @XmlTransient
  public Collection<WtContact> getBuyersContactCollection() {
    return buyersContactCollection;
  }
  
  public void setBuyersContactCollection(Collection<WtContact> buyersContactCollection) {
    this.buyersContactCollection = buyersContactCollection;
  }
//</editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Override Object">
  @Override
  public int hashCode() {
    int hash = 0;
    hash += (wtTransId != null ? wtTransId.hashCode() : 0);
    return hash;
  }
  
  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof WtTrans)) {
      return false;
    }
    WtTrans other = (WtTrans) object;
    if ((this.wtTransId == null && other.wtTransId != null) || (this.wtTransId != null && !this.wtTransId.equals(other.wtTransId))) {
      return false;
    }
    return true;
  }
  
  @Override
  public String toString() {
    return "com.gei.entities.WtTrans[ wtTransId=" + wtTransId + " ]";
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Public Customize Methods">
  public boolean hasUser(int userId) {
    if (appUserCollection == null || appUserCollection.isEmpty()) {
      return false;
    }
    for (AppUser user : appUserCollection) {
      if (user.getUserId() == userId) {
        return true;
      }
    }
    return false;
  }
  
  public String xmlSerialization() throws Exception {
    String xmlStr
            = JAXBHelper.getXMLContent(QName.valueOf("WtTrans"), this, true);
    
    return xmlStr;
  }
  
  public String dateFormat(Date date) throws ParseException {
    SimpleDateFormat dt = new SimpleDateFormat("MM/dd/yyyy");
    if (date != null) {
      return dt.format(date);
    } else {
      return "";
    }
  }
  
  public Map<String, String> getCompareMap(WtTrans other) {
    HashMap<String, String> outputMap = new HashMap<>();
    try {
      if ((this.transYear == null && other.transYear != null)
              || (this.transYear != null && !this.transYear.equals(other.transYear))) {
        outputMap.put("transYear", (this.transYear).toString());
      }
      
      if ((this.isShortLong == null && other.isShortLong != null)
              || (this.isShortLong != null && !this.isShortLong.equals(other.isShortLong))) {
        if (this.isShortLong == null) {
          outputMap.put("isShortLong", "");
        } else {
          outputMap.put("isShortLong", this.proTransQua == 0 ? "Long Term" : "One Year");
        }
      }
      
      if ((this.proTransQua == null && other.proTransQua != null)
              || (this.proTransQua != null && !this.proTransQua.equals(other.proTransQua))) {
        outputMap.put("proTransQua", (this.proTransQua).toString());
      }
      
      if ((this.proUnitCost == null && other.proUnitCost != null)
              || (this.proUnitCost != null && !this.proUnitCost.equals(other.proUnitCost))) {
        outputMap.put("proUnitCost", (this.proUnitCost).toString());
      }
      
      if ((this.proAgreePaidRange == null && other.proAgreePaidRange != null)
              || (this.proAgreePaidRange != null && !this.proAgreePaidRange.equals(other.proAgreePaidRange))) {
        outputMap.put("proAgreePaidRange", this.proAgreePaidRange);
      }
      
      if ((this.surWaterSource == null && other.surWaterSource != null)
              || (this.surWaterSource != null && !this.surWaterSource.equals(other.surWaterSource))) {
        outputMap.put("surWaterSource", this.surWaterSource);
      }
      
      if ((this.majorRiverAttribute == null && other.majorRiverAttribute != null)
              || (this.majorRiverAttribute != null && !this.majorRiverAttribute.equals(other.majorRiverAttribute))) {
        outputMap.put("surWaterSource", this.surWaterSource);
      }
      
      if ((this.transWinStart == null && other.transWinStart != null)
              || (this.transWinStart != null && !other.transWinStart.equals(this.transWinStart))) {
        outputMap.put("transWinStart", dateFormat(this.transWinStart));
      }
      
      if ((this.transWinEnd == null && other.transWinEnd != null)
              || (this.transWinEnd != null && !other.transWinEnd.equals(this.transWinEnd))) {
        outputMap.put("transWinEnd", dateFormat(this.transWinEnd));
      }
      
      if ((this.reqExpFrom == null && other.reqExpFrom != null)
              || (this.reqExpFrom != null && !this.reqExpFrom.equals(other.reqExpFrom))) {
        outputMap.put("reqExpFrom", this.reqExpFrom);
      }
      
      if ((this.reqExpTo == null && other.reqExpTo != null)
              || (this.reqExpTo != null && !this.reqExpTo.equals(other.reqExpTo))) {
        outputMap.put("reqExpTo", this.reqExpTo);
      }
      
      if ((this.reqStorageExp == null && other.reqStorageExp != null)
              || (this.reqStorageExp != null && !this.reqStorageExp.equals(other.reqStorageExp))) {
        outputMap.put("reqStorageExp", this.reqStorageExp);
      }
      
      if ((this.deltaTransferInd == null && other.deltaTransferInd != null)
              || (this.deltaTransferInd != null && !this.deltaTransferInd.equals(other.deltaTransferInd))) {
        outputMap.put("deltaTransferInd", this.deltaTransferInd == 1 ? "Yes" : "No");
      }
      
      if ((this.isStateContractor == null && other.isStateContractor != null)
              || (this.isStateContractor != null && !this.isStateContractor.equals(other.isStateContractor))) {
        outputMap.put("isStateContractor", this.isStateContractor == 1 ? "Yes" : "No");
      }
      
      if ((this.dwrComments == null && other.dwrComments != null)
              || (this.dwrComments != null && !this.dwrComments.equals(other.dwrComments))) {
        outputMap.put("dwrComments", this.dwrComments);
      }
      
      if ((this.wtTransTypeCollection == null && other.wtTransTypeCollection != null)
              || (this.wtTransTypeCollection != null && other.wtTransTypeCollection == null)
              || (this.wtTransTypeCollection != null && other.wtTransTypeCollection != null && this.wtTransTypeCollection.size() != other.wtTransTypeCollection.size())
              || (this.wtTransTypeCollection != null && other.wtTransTypeCollection != null && !this.wtTransTypeCollection.containsAll(other.wtTransTypeCollection))) {
        String comma, transTypes = "";
        Iterator<WtTransType> transTypeIt;
        
        if (this.wtTransTypeCollection != null) {
          WtTransType transType = null;
          transTypeIt = this.wtTransTypeCollection.iterator();
          if (transTypeIt != null) {
            comma = "";
            while (transTypeIt.hasNext()) {
              transType = transTypeIt.next();
              String fuType = transType.getWtFuType().getFuType();
              if (transType.getWtFuType().getFuSubType() != null) {
                fuType = fuType + "/" + transType.getWtFuType().getFuSubType();
              }              
              transTypes = transTypes + comma + fuType;              
              comma = ",";
            }
          }
        }
        outputMap.put("transTypes", transTypes);
      }
      
      if ((this.wtTransSwrcb == null && other.wtTransSwrcb != null)
              || (this.wtTransCeqa == null && other.wtTransCeqa != null)
              || (this.wtTransNepa == null && other.wtTransNepa != null)
              || (this.wtTransSwrcb != null && !this.wtTransSwrcb.equals(other.wtTransSwrcb))
              || (this.wtTransCeqa != null && !this.wtTransCeqa.equals(other.wtTransCeqa))
              || (this.wtTransNepa != null && !this.wtTransNepa.equals(other.wtTransNepa))) {
        String comma = "", envRegComp = "";
        if (this.wtTransSwrcb != null) {
          envRegComp = "SWRCB";
          comma = ",";
        }
        if (this.wtTransCeqa != null) {
          envRegComp = envRegComp + comma + "CEQA";
          comma = ",";
        }
        if (this.wtTransNepa != null) {
          envRegComp = envRegComp + comma + "NEPA";
        }        
        outputMap.put("envRegComp", envRegComp);
      }
      
      if ((this.wtTransReach == null && other.wtTransReach != null)
              || (this.wtTransReach != null && !this.wtTransReach.equals(other.wtTransReach))) {
        if (this.wtTransReach == null) {
          outputMap.put("transReach", "");
        } else {
          outputMap.put("transReach", this.wtTransReach.getPowerProvider());
        }
      }
      
    } catch (Exception ex) {
      fail("WtTrans.getCompareMap Failed.\n" + ex.getMessage());
    }
    
    return outputMap;
  }
  // </editor-fold>
}
