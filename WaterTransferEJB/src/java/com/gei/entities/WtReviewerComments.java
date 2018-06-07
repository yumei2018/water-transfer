/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "WT_REVIEWER_COMMENTS")
@NamedQueries({
    @NamedQuery(name = "WtReviewerComments.findAll", query = "SELECT w FROM WtReviewerComments w")})
@SequenceGenerator(name = "WtReviewerCommentsSeq",sequenceName = "WT_REVIEWER_COMMENTS_PKSEQ",allocationSize = 1)
public class WtReviewerComments extends AbstractEntity<WtReviewerComments> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(generator = "WtReviewerCommentsSeq", strategy = GenerationType.SEQUENCE)
    @Column(name = "WT_REVIEWER_COMMENTS_ID")
    private Integer wtReviewerCommentsId;
    @Column(name = "WT_TRANS_ID")
    private Integer wtTransId;
    @Column(name = "REVIEWER_NAME")
    private String reviewerName;
    @Lob
    @Column(name = "REVIEWER_COMMENTS")
    private String reviewerComments;
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "CREATED_BY_ID")
    private Integer createdById;

    public WtReviewerComments() {
    }

    public WtReviewerComments(Integer wtReviewerCommentsId) {
        this.wtReviewerCommentsId = wtReviewerCommentsId;
    }

    public Integer getWtReviewerCommentsId() {
        return wtReviewerCommentsId;
    }

    public void setWtReviewerCommentsId(Integer wtReviewerCommentsId) {
        this.wtReviewerCommentsId = wtReviewerCommentsId;
    }

    public Integer getWtTransId() {
        return wtTransId;
    }

    public void setWtTransId(Integer wtTransId) {
        this.wtTransId = wtTransId;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getReviewerComments() {
        return reviewerComments;
    }

    public void setReviewerComments(String reviewerComments) {
        this.reviewerComments = reviewerComments;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wtReviewerCommentsId != null ? wtReviewerCommentsId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WtReviewerComments)) {
            return false;
        }
        WtReviewerComments other = (WtReviewerComments) object;
        if ((this.wtReviewerCommentsId == null && other.wtReviewerCommentsId != null) || (this.wtReviewerCommentsId != null && !this.wtReviewerCommentsId.equals(other.wtReviewerCommentsId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gei.entities.WtReviewerComments[ wtReviewerCommentsId=" + wtReviewerCommentsId + " ]";
    }
    
}
