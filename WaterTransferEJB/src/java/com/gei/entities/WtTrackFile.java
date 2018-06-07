/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.entities;

import com.gei.facades.AppUserFacade;
import gov.ca.water.transfer.util.WebUtil;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ymei
 */
@Entity
@Table(name = "WT_TRACK_FILE")
@SequenceGenerator(name = "WtTrackFileSeq",sequenceName = "WT_TRACK_FILE_PKSEQ",allocationSize = 1)
public class WtTrackFile extends AbstractEntity<WtTrackFile> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(generator = "WtTrackFileSeq", strategy = GenerationType.SEQUENCE)
    @Column(name = "WT_TRACK_FILE_ID")
    private Integer wtTrackFileId;
    @Column(name = "FILENAME")
    private String filename;
    @Column(name = "STATUS_NAME")
    private String statusName;
    @Lob
    @Column(name = "FILE_LOB")
    private byte[] fileLob;
    @Column(name = "MIME_TYPE")
    private String mimeType;
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "CREATED_BY_ID")
    private Integer createdById;
    @Column(name = "FILE_TYPE")
    private String fileType;
    @Column(name = "FILE_VERSION")
    private Integer fileVersion;
    @JoinColumn(name = "WT_TRANS_ID", referencedColumnName = "WT_TRANS_ID")
    @ManyToOne(optional = false)
    private WtTrans wtTrans;

    public WtTrackFile() {
    }

    public WtTrackFile(Integer wtTrackFileId) {
        this.wtTrackFileId = wtTrackFileId;
    }

    public Integer getWtTrackFileId() {
        return wtTrackFileId;
    }

    public void setWtTrackFileId(Integer wtTrackFileId) {
        this.wtTrackFileId = wtTrackFileId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public byte[] getFileLob() {
        return fileLob;
    }

    public void setFileLob(byte[] fileLob) {
        this.fileLob = fileLob;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
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

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Integer getFileVersion() {
        return fileVersion;
    }

    public void setFileVersion(Integer fileVersion) {
        this.fileVersion = fileVersion;
    }

    public WtTrans getWtTrans() {
        return wtTrans;
    }

    public void setWtTrans(WtTrans wtTrans) {
        this.wtTrans = wtTrans;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wtTrackFileId != null ? wtTrackFileId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WtTrackFile)) {
            return false;
        }
        WtTrackFile other = (WtTrackFile) object;
        if ((this.wtTrackFileId == null && other.wtTrackFileId != null) || (this.wtTrackFileId != null && !this.wtTrackFileId.equals(other.wtTrackFileId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gei.entities.WtTrackFile[ wtTrackFileId=" + wtTrackFileId + " ]";
    }

    //<editor-fold defaultstate="collapsed" desc="Private Transient Properties">
    @Transient
    private AppUser createdBy;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Get Created By User">
    @Transient
    public AppUser getCreatedBy(){
      if ((this.createdBy == null) && (this.createdById != null)) {
        AppUserFacade facade = WebUtil.getFacade(AppUserFacade.class);
        this.createdBy = facade.find(this.createdById);
      }
      return this.createdBy;
    }
    //</editor-fold>
}
