package com.gei.entities;

import com.gei.facades.AppUserFacade;
import gov.ca.water.transfer.util.WebUtil;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ymei
 */
@Entity
@Table(name = "WT_REVIEW_NOTE")
@NamedQueries({
  @NamedQuery(name = "WtReviewNote.findAll", query = "SELECT w FROM WtReviewNote w")})
@SequenceGenerator(name = "WtReviewNoteSeq",sequenceName = "WT_REVIEW_NOTE_PKSEQ",allocationSize = 1)
public class WtReviewNote extends AbstractEntity<WtReviewNote> implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @NotNull
  @GeneratedValue(generator = "WtReviewNoteSeq", strategy = GenerationType.SEQUENCE)
  @Column(name = "WT_REVIEW_NOTE_ID")
  private Integer wtReviewNoteId;
  @Column(name = "WT_TRANS_ID")
  private Integer wtTransId;
  @Column(name = "SECTION_KEY")
  private String sectionKey;
  @Column(name = "IS_COMPLETE")
  private Integer isComplete;
  @Column(name = "STATUS_KEY")
  private String statusKey;
  @Column(name = "STATUS_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date statusDate;
  @Column(name = "STATUS_UPDATED_BY")
  private Integer statusUpdatedBy;
  @Lob
  @Column(name = "NOTE")
  private String note;
  @Column(name = "NOTE_UPDATED_BY")
  private Integer noteUpdatedBy;
  @Column(name = "NOTE_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date noteDate;

  // <editor-fold defaultstate="collapsed" desc="Constructor">
  /**
   * Public Constructor  
   */
  public WtReviewNote() {
    super();  
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Public Methods">
    public WtReviewNote(Integer wtReviewNoteId) {
    this.wtReviewNoteId = wtReviewNoteId;
  }

  public Integer getWtReviewNoteId() {
    return wtReviewNoteId;
  }

  public void setWtReviewNoteId(Integer wtReviewNoteId) {
    this.wtReviewNoteId = wtReviewNoteId;
  }

  public Integer getWtTransId() {
    return wtTransId;
  }

  public void setWtTransId(Integer wtTransId) {
    this.wtTransId = wtTransId;
  }

  public String getSectionKey() {
    return sectionKey;
  }

  public void setSectionKey(String sectionKey) {
    this.sectionKey = sectionKey;
  }

  public Integer getIsComplete() {
    return isComplete;
  }

  public void setIsComplete(Integer isComplete) {
    this.isComplete = isComplete;
  }

  public String getStatusKey() {
    return statusKey;
  }

  public void setStatusKey(String statusKey) {
    this.statusKey = statusKey;
  }

  public Date getStatusDate() {
    return statusDate;
  }

  public void setStatusDate(Date statusDate) {
    this.statusDate = statusDate;
  }

  public Integer getStatusUpdatedBy() {
    return statusUpdatedBy;
  }

  public void setStatusUpdatedBy(Integer statusUpdatedBy) {
    this.statusUpdatedBy = statusUpdatedBy;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public Integer getNoteUpdatedBy() {
    return noteUpdatedBy;
  }

  public void setNoteUpdatedBy(Integer noteUpdatedBy) {
    this.noteUpdatedBy = noteUpdatedBy;
  }

  public Date getNoteDate() {
    return noteDate;
  }

  public void setNoteDate(Date noteDate) {
    this.noteDate = noteDate;
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Object Overrides">  
  @Override
  public int hashCode() {
    int hash = 0;
    hash += (wtReviewNoteId != null ? wtReviewNoteId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof WtReviewNote)) {
      return false;
    }
    WtReviewNote other = (WtReviewNote) object;
    if ((this.wtReviewNoteId == null && other.wtReviewNoteId != null) || (this.wtReviewNoteId != null && !this.wtReviewNoteId.equals(other.wtReviewNoteId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.gei.entities.WtReviewNote[ wtReviewNoteId=" + wtReviewNoteId + " ]";
  }
  // </editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Private Transient Properties">
  @Transient
  private AppUser updatedBy;
  
  @Transient
  public AppUser getUpdatedBy(){
    if ((this.updatedBy == null) && (this.noteUpdatedBy != null)) {
      AppUserFacade facade = WebUtil.getFacade(AppUserFacade.class);
      this.updatedBy = facade.find(this.noteUpdatedBy);
    }
    return this.updatedBy;
  }
  //</editor-fold>  
}
