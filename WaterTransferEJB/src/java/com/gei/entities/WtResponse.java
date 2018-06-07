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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
@Table(name = "WT_RESPONSE")
@SequenceGenerator(name="WtResponseSeq",sequenceName="WT_RESPONSE_PKSEQ",allocationSize = 1)
public class WtResponse extends AbstractEntity<WtResponse> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "WT_RESPONSE_ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "WtResponseSeq")
    private Integer wtResponseId;
    @Column(name = "RESPONSE_BY")
    private String responseBy;
    @Lob
    @Column(name = "RESPONSE_COMMENTS")
    private String responseComments;
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
    @JoinColumn(name = "WT_REVIEW_COMMENT_ID", referencedColumnName = "WT_REVIEW_COMMENT_ID")
    @ManyToOne
    private WtReviewComment wtReviewComment;

    public WtResponse() {
    }

    public WtResponse(Integer wtResponseId) {
        this.wtResponseId = wtResponseId;
    }

    public Integer getWtResponseId() {
        return wtResponseId;
    }

    public void setWtResponseId(Integer wtResponseId) {
        this.wtResponseId = wtResponseId;
    }

    public String getResponseBy() {
        return responseBy;
    }

    public void setResponseBy(String responseBy) {
        this.responseBy = responseBy;
    }

    public String getResponseComments() {
        return responseComments;
    }

    public void setResponseComments(String responseComments) {
        this.responseComments = responseComments;
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

    public WtReviewComment getWtReviewComment() {
        return wtReviewComment;
    }

    public void setWtReviewComment(WtReviewComment wtReviewComment) {
        this.wtReviewComment = wtReviewComment;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wtResponseId != null ? wtResponseId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WtResponse)) {
            return false;
        }
        WtResponse other = (WtResponse) object;
        if ((this.wtResponseId == null && other.wtResponseId != null) || (this.wtResponseId != null && !this.wtResponseId.equals(other.wtResponseId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gei.entities.WtResponse[ wtResponseId=" + wtResponseId + " ]";
    }
    
}
