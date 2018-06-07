
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
@Table(name = "WT_INTERNAL_NOTE")
@NamedQueries({
  @NamedQuery(name = "WtInternalNote.findAll", query = "SELECT w FROM WtInternalNote w")})
@SequenceGenerator(name = "WtInternalNoteSeq",sequenceName = "WT_INTERNAL_NOTE_PKSEQ",allocationSize = 1)
public class WtInternalNote extends AbstractEntity<WtInternalNote> implements Serializable {
  private static final long serialVersionUID = 1L;  

  // <editor-fold defaultstate="collapsed" desc="Private Fields">
  @Id
  @Basic(optional = false)
  @NotNull
  @GeneratedValue(generator = "WtInternalNoteSeq", strategy = GenerationType.SEQUENCE)
  @Column(name = "WT_INTERNAL_NOTE_ID")
  private Integer wtInternalNoteId;
  @Column(name = "WT_TRANS_ID")
  private Integer wtTransId;
  @Column(name = "SECTION_KEY")
  private String sectionKey;
  @Lob
  @Column(name = "NOTE")
  private String note;
  @Column(name = "NOTE_UPDATED_BY")
  private Integer noteUpdatedBy;
  @Column(name = "NOTE_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date noteDate;
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Constructor">
  /**
   * Public Constructor  
   */
  public WtInternalNote() {
    super();  
  }
  
  public WtInternalNote(Integer wtInternalNoteId) {
    this.wtInternalNoteId = wtInternalNoteId;
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Public Methods">
  public Integer getWtInternalNoteId() {
    return wtInternalNoteId;
  }

  public void setWtInternalNoteId(Integer wtInternalNoteId) {
    this.wtInternalNoteId = wtInternalNoteId;
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
    hash += (wtInternalNoteId != null ? wtInternalNoteId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof WtInternalNote)) {
      return false;
    }
    WtInternalNote other = (WtInternalNote) object;
    if ((this.wtInternalNoteId == null && other.wtInternalNoteId != null) || (this.wtInternalNoteId != null && !this.wtInternalNoteId.equals(other.wtInternalNoteId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.gei.entities.WtInternalNote[ wtInternalNoteId=" + wtInternalNoteId + " ]";
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
