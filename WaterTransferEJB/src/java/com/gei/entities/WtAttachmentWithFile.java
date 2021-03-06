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
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
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
@Table(name = "WT_ATTACHMENT")
@SequenceGenerator(name = "WtAttachmentSeq",sequenceName = "WT_ATTACHMENT_PKSEQ",allocationSize = 1)
public class WtAttachmentWithFile extends AbstractEntity<WtAttachment> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(generator = "WtAttachmentSeq", strategy = GenerationType.SEQUENCE)
    @Column(name = "WT_ATTACHMENT_ID")
    private Integer wtAttachmentId;
    @Column(name = "FILENAME")
    private String filename;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "MODIFIED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    @Column(name = "MIME_TYPE")
    private String mimeType;
    @Column(name = "ATTACHMENT_TYPE")
    private String attachmentType;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "CREATED_BY_ID")
    private Integer createdById;
    @Column(name = "UPDATED_BY_ID")
    private Integer updatedById; 
    @Column(name = "CEQA_SUBMITTED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ceqaSubmittedDate;
    @Lob
    @Column(name = "FILE_LOB")
    private byte[] fileLob;
    @Column(name = "FILE_SIZE")
    private Long fileSize;
    @Column(name = "IS_ACTIVE")
    private Integer isActive;
    @JoinTable(name = "WT_TRANS_ATTACHMENTS", joinColumns = {
        @JoinColumn(name = "WT_ATTACHMENT_ID", referencedColumnName = "WT_ATTACHMENT_ID")}, inverseJoinColumns = {
        @JoinColumn(name = "WT_TRANS_ID", referencedColumnName = "WT_TRANS_ID")})
    @OneToOne
    private WtTrans wtTrans;
    @JoinTable(name = "WT_TRANS_REPORT", joinColumns = {
        @JoinColumn(name = "WT_ATTACHMENT_ID", referencedColumnName = "WT_ATTACHMENT_ID")}, inverseJoinColumns = {
        @JoinColumn(name = "WT_TRANS_ID", referencedColumnName = "WT_TRANS_ID")})
    @OneToOne
    private WtTrans wtTransR;
    @JoinTable(name = "WT_CI_ATTACHMENT", joinColumns = {
        @JoinColumn(name = "WT_ATTACHMENT_ID", referencedColumnName = "WT_ATTACHMENT_ID")}, inverseJoinColumns = {
        @JoinColumn(name = "WT_CROP_IDLING_ID", referencedColumnName = "WT_CROP_IDLING_ID")})
    @OneToOne
    private WtCropIdling wtCropIdling;
    @JoinTable(name = "WT_CI_MAP_ATTACHMENT", joinColumns = {
        @JoinColumn(name = "WT_ATTACHMENT_ID", referencedColumnName = "WT_ATTACHMENT_ID")}, inverseJoinColumns = {
        @JoinColumn(name = "WT_CROP_IDLING_ID", referencedColumnName = "WT_CROP_IDLING_ID")})
    @OneToOne
    private WtCropIdling wtCropIdling2;
    @JoinTable(name = "WT_RV_ATTACHMENT", joinColumns = {
        @JoinColumn(name = "WT_ATTACHMENT_ID", referencedColumnName = "WT_ATTACHMENT_ID")}, inverseJoinColumns = {
        @JoinColumn(name = "WT_RESERVOIR_ID", referencedColumnName = "WT_RESERVOIR_ID")})
    @OneToOne
    private WtReservoir wtReservoir;
    @JoinTable(name = "WT_GW_ATTACHMENT", joinColumns = {
        @JoinColumn(name = "WT_ATTACHMENT_ID", referencedColumnName = "WT_ATTACHMENT_ID")}, inverseJoinColumns = {
        @JoinColumn(name = "WT_GROUNDWATER_ID", referencedColumnName = "WT_GROUNDWATER_ID")})
    @OneToOne
    private WtGroundwater wtGroundwater; 
    @JoinTable(name = "WT_WELL_ATTACHMENT", joinColumns = {
        @JoinColumn(name = "WT_ATTACHMENT_ID", referencedColumnName = "WT_ATTACHMENT_ID")}, inverseJoinColumns = {
        @JoinColumn(name = "WT_WELL_ID", referencedColumnName = "WT_WELL_ID")})
    @OneToOne
    private WtWell wtWell;
    @JoinTable(name = "WT_ATT_CHECKLIST", joinColumns = {
        @JoinColumn(name = "WT_ATTACHMENT_ID", referencedColumnName = "WT_ATTACHMENT_ID")}, inverseJoinColumns = {
        @JoinColumn(name = "WT_CHECKLIST_ID", referencedColumnName = "WT_CHECKLIST_ID")})
    @ManyToMany
    private Collection<WtChecklist> wtChecklistCollection;

    public WtAttachmentWithFile() {
    }

    public WtAttachmentWithFile(Integer wtAttachmentId) {
        this.wtAttachmentId = wtAttachmentId;
    }

    public Integer getWtAttachmentId() {
        return wtAttachmentId;
    }

    public void setWtAttachmentId(Integer wtAttachmentId) {
        this.wtAttachmentId = wtAttachmentId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public byte[] getFileLob() {
        return fileLob;
    }

    public void setFileLob(byte[] fileLob) {
        this.fileLob = fileLob;
    }

    public Long getFileSize() {
      return fileSize;
    }

    public void setFileSize(Long fileSize) {
      this.fileSize = fileSize;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    
    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Integer createdById) {
        this.createdById = createdById;
    }

    public Integer getUpdatedById() {
        return updatedById;
    }

    public void setUpdatedById(Integer updatedById) {
        this.updatedById = updatedById;
    }    
    
    public Date getCeqaSubmittedDate() {
        return ceqaSubmittedDate;
    }    
    
    public void setCeqaSubmittedDate(Date ceqaSubmittedDate) {
        this.ceqaSubmittedDate = ceqaSubmittedDate;
    }

    public Integer getIsActive() {
      return isActive;
    }

    public void setIsActive(Integer isActive) {
      this.isActive = isActive;
    }
    
    public WtTrans getWtTrans() {
        return wtTrans;
    }
    
    public void setWtTrans(WtTrans wtTrans) {
        this.wtTrans = wtTrans;
    }    

    public WtTrans getWtTransR() {
      return wtTransR;
    }

    public void setWtTransR(WtTrans wtTransR) {
      this.wtTransR = wtTransR;
    }
    
    public WtCropIdling getWtCropIdling() {
        return wtCropIdling;
    }

    public void setWtCropIdling(WtCropIdling wtCropIdling) {
        this.wtCropIdling = wtCropIdling;
    }

    public WtCropIdling getWtCropIdling2() {
      return wtCropIdling2;
    }

    public void setWtCropIdling2(WtCropIdling wtCropIdling2) {
      this.wtCropIdling2 = wtCropIdling2;
    }

    public WtReservoir getWtReservoir() {
        return wtReservoir;
    }

    public void setWtReservoir(WtReservoir wtReservoir) {
        this.wtReservoir = wtReservoir;
    }

    public WtGroundwater getWtGroundwater() {
        return wtGroundwater;
    }

    public void setWtGroundwater(WtGroundwater wtGroundwater) {
        this.wtGroundwater = wtGroundwater;
    }    
    
    public WtWell getWtWell() {
        return wtWell;
    }

    public void setWtWell(WtWell wtWell) {
        this.wtWell = wtWell;
    }

    public Collection<WtChecklist> getWtChecklistCollection() {
        return wtChecklistCollection;
    }

    public void setWtChecklistCollection(Collection<WtChecklist> wtChecklistCollection) {
        this.wtChecklistCollection = wtChecklistCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wtAttachmentId != null ? wtAttachmentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WtAttachmentWithFile)) {
            return false;
        }
        WtAttachmentWithFile other = (WtAttachmentWithFile) object;
        if ((this.wtAttachmentId == null && other.wtAttachmentId != null) || (this.wtAttachmentId != null && !this.wtAttachmentId.equals(other.wtAttachmentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gei.entities.WtAttachment[ wtAttachmentId=" + wtAttachmentId + " ]";
    }

}
