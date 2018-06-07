/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.entities;

import java.io.Serializable;
import java.util.Collection;
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
import javax.validation.constraints.NotNull;

/**
 *
 * @author ymei
 */
@Entity
@Table(name = "WT_CHECKLIST")
@SequenceGenerator(name = "WtChecklistSeq",sequenceName = "WT_CHECKLIST_PKSEQ",allocationSize = 1)
public class WtChecklist extends AbstractEntity<WtChecklist> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(generator = "WtChecklistSeq", strategy = GenerationType.SEQUENCE)
    @Column(name = "WT_CHECKLIST_ID")
    private Integer wtChecklistId;
    @Column(name = "CHECKLIST_FIELD")
    private String checklistField;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Column(name = "CHECKLIST_TYPE")
    private String checklistType;
    @Column(name = "SORT_ORDER")
    private Integer sortOrder;
    @Column(name = "WELL_TRANSFER")
    private Integer wellTransfer;
    @Column(name = "WELL_MONITORING")
    private Integer wellMonitoring;
    @Column(name = "DETAIL")
    private String detail;
    @ManyToMany(mappedBy = "wtChecklistCollection")
    private Collection<WtAttachment> wtAttachmentCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "wtChecklist")
    private Collection<WtReviewComment> wtReviewCommentCollection;

    public WtChecklist() {
    }

    public WtChecklist(Integer wtChecklistId) {
        this.wtChecklistId = wtChecklistId;
    }

    public WtChecklist(Integer wtChecklistId, String name) {
        this.wtChecklistId = wtChecklistId;
        this.name = name;
    }

    public Integer getWtChecklistId() {
        return wtChecklistId;
    }

    public void setWtChecklistId(Integer wtChecklistId) {
        this.wtChecklistId = wtChecklistId;
    }

    public String getChecklistField() {
        return checklistField;
    }

    public void setChecklistField(String checklistField) {
        this.checklistField = checklistField;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChecklistType() {
        return checklistType;
    }

    public void setChecklistType(String checklistType) {
        this.checklistType = checklistType;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
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

    public String getDetail() {
      return detail;
    }

    public void setDetail(String detail) {
      this.detail = detail;
    }

    public Collection<WtAttachment> getWtAttachmentCollection() {
        return wtAttachmentCollection;
    }

    public void setWtAttachmentCollection(Collection<WtAttachment> wtAttachmentCollection) {
        this.wtAttachmentCollection = wtAttachmentCollection;
    }

    public Collection<WtReviewComment> getWtReviewCommentCollection() {
        return wtReviewCommentCollection;
    }

    public void setWtReviewCommentCollection(Collection<WtReviewComment> wtReviewCommentCollection) {
        this.wtReviewCommentCollection = wtReviewCommentCollection;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wtChecklistId != null ? wtChecklistId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WtChecklist)) {
            return false;
        }
        WtChecklist other = (WtChecklist) object;
        if ((this.wtChecklistId == null && other.wtChecklistId != null) || (this.wtChecklistId != null && !this.wtChecklistId.equals(other.wtChecklistId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gei.entities.WtChecklist[ wtChecklistId=" + wtChecklistId + " ]";
    }
    
}
