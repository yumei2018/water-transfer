/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
@Table(name = "WT_REVIEW_COMMENT")
@SequenceGenerator(name="WtReviewCommentSeq",sequenceName="WT_REVIEW_COMMENT_PKSEQ",allocationSize = 1)
public class WtReviewComment extends AbstractEntity<WtReviewComment> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "WT_REVIEW_COMMENT_ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "WtReviewCommentSeq")
    private Integer wtReviewCommentId;
    @Column(name = "WT_WELL_ID")
    private Integer wtWellId;
    @Column(name = "TRANS_TYPE")
    private String transType;
    @Column(name = "FIELD_NAME")
    private String fieldName;
    @Column(name = "ISSUE_STATUS")
    private String issueStatus;
    @Lob
    @Column(name = "REVIEWER_COMMENTS")
    private String reviewerComments;
    @Column(name = "CREATED_BY_ID")
    private Integer createdById;
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "UPDATED_BY_ID")
    private Integer updatedById;
    @Column(name = "UPDATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @OneToMany(mappedBy = "wtReviewComment")
    private Collection<WtResponse> wtResponseCollection;
    @JoinColumn(name = "WT_TRANS_ID", referencedColumnName = "WT_TRANS_ID")
    @ManyToOne(optional = false)
    private WtTrans wtTrans;
    @JoinColumn(name = "WT_CHECKLIST_ID", referencedColumnName = "WT_CHECKLIST_ID")
    @ManyToOne(optional = false)
    private WtChecklist wtChecklist;

    public WtReviewComment() {
    }

    public WtReviewComment(Integer wtReviewCommentId) {
        this.wtReviewCommentId = wtReviewCommentId;
    }

    public Integer getWtReviewCommentId() {
        return wtReviewCommentId;
    }

    public void setWtReviewCommentId(Integer wtReviewCommentId) {
        this.wtReviewCommentId = wtReviewCommentId;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    
    public String getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(String issueStatus) {
        this.issueStatus = issueStatus;
    }

    public String getReviewerComments() {
        return reviewerComments;
    }

    public void setReviewerComments(String reviewerComments) {
        this.reviewerComments = reviewerComments;
    }

    public Integer getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Integer createdById) {
        this.createdById = createdById;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getUpdatedById() {
        return updatedById;
    }

    public void setUpdatedById(Integer updatedById) {
        this.updatedById = updatedById;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Collection<WtResponse> getWtResponseCollection() {
        return wtResponseCollection;
    }

    public void setWtResponseCollection(Collection<WtResponse> wtResponseCollection) {
        this.wtResponseCollection = wtResponseCollection;
    }

    public Integer getWtWellId() {
        return wtWellId;
    }

    public void setWtWellId(Integer wtWellId) {
        this.wtWellId = wtWellId;
    }

    public WtTrans getWtTrans() {
        return wtTrans;
    }

    public void setWtTrans(WtTrans wtTrans) {
        this.wtTrans = wtTrans;
    }

    public WtChecklist getWtChecklist() {
        return wtChecklist;
    }

    public void setWtChecklist(WtChecklist wtChecklist) {
        this.wtChecklist = wtChecklist;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wtReviewCommentId != null ? wtReviewCommentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WtReviewComment)) {
            return false;
        }
        WtReviewComment other = (WtReviewComment) object;
        if ((this.wtReviewCommentId == null && other.wtReviewCommentId != null) || (this.wtReviewCommentId != null && !this.wtReviewCommentId.equals(other.wtReviewCommentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gei.entities.WtReviewComment[ wtReviewCommentId=" + wtReviewCommentId + " ]";
    }
    
}
