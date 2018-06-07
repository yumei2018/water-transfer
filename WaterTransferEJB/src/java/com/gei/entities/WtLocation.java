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
import javax.persistence.JoinTable;
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
@Table(name = "WT_LOCATION")
@SequenceGenerator(name="WtLocationSeq",sequenceName="WT_LOCATION_PKSEQ",allocationSize = 1)
public class WtLocation extends AbstractEntity<WtLocation> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(generator = "WtLocationSeq", strategy = GenerationType.SEQUENCE)
    @Column(name = "WT_LOCATION_ID")
    private Integer wtLocationId;
    @Column(name = "LOC_DESC")
    private String locDesc;
    @Column(name = "TOWNSHIP")
    private String township;
    @Column(name = "RANGE")
    private String range;
    @Column(name = "SECTION")
    private String section;
    @Column(name = "LAT_START")
    private Integer latStart;
    @Column(name = "LAT_END")
    private Integer latEnd;
    @Column(name = "LONG_START")
    private Integer longStart;
    @Column(name = "LONG_END")
    private Integer longEnd;
    @Column(name = "RIVER_MILE_START")
    private Integer riverMileStart;
    @Column(name = "RIVER_MILE_END")
    private Integer riverMileEnd;
    @Column(name = "LOCATION_TYPE")
    private String locationType;
    @Column(name = "SHAPE_TYPE")
    private String shapeType;
//    @Lob
//    @Column(name = "SHAPE")
//    private Object shape;
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "UPDATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @Column(name = "CREATED_BY_ID")
    private Integer createdById;
    @Column(name = "UPDATED_BY_ID")
    private Integer updatedById;
    @JoinTable(name = "WT_CI_LOCATION", joinColumns = {
        @JoinColumn(name = "WT_LOCATION_ID", referencedColumnName = "WT_LOCATION_ID")}, inverseJoinColumns = {
        @JoinColumn(name = "WT_CROP_IDLING_ID", referencedColumnName = "WT_CROP_IDLING_ID")})
    @OneToOne
    private WtCropIdling wtCropIdling;
    @JoinTable(name = "WT_RV_LOCATION", joinColumns = {
        @JoinColumn(name = "WT_LOCATION_ID", referencedColumnName = "WT_LOCATION_ID")}, inverseJoinColumns = {
        @JoinColumn(name = "WT_RESERVOIR_ID", referencedColumnName = "WT_RESERVOIR_ID")})
    @OneToOne
    private WtReservoir wtReservoir;
    @JoinTable(name = "WT_GW_LOCATION", joinColumns = {
        @JoinColumn(name = "WT_LOCATION_ID", referencedColumnName = "WT_LOCATION_ID")}, inverseJoinColumns = {
        @JoinColumn(name = "WT_GROUNDWATER_ID", referencedColumnName = "WT_GROUNDWATER_ID")})
    @OneToOne
    private WtGroundwater wtGroundwater;

    public WtLocation() {
    }

    public WtLocation(Integer wtLocationId) {
        this.wtLocationId = wtLocationId;
    }

    public Integer getWtLocationId() {
        return wtLocationId;
    }

    public void setWtLocationId(Integer wtLocationId) {
        this.wtLocationId = wtLocationId;
    }

    public String getLocDesc() {
        return locDesc;
    }

    public void setLocDesc(String locDesc) {
        this.locDesc = locDesc;
    }

    public String getTownship() {
        return township;
    }

    public void setTownship(String township) {
        this.township = township;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Integer getLatStart() {
        return latStart;
    }

    public void setLatStart(Integer latStart) {
        this.latStart = latStart;
    }

    public Integer getLatEnd() {
        return latEnd;
    }

    public void setLatEnd(Integer latEnd) {
        this.latEnd = latEnd;
    }

    public Integer getLongStart() {
        return longStart;
    }

    public void setLongStart(Integer longStart) {
        this.longStart = longStart;
    }

    public Integer getLongEnd() {
        return longEnd;
    }

    public void setLongEnd(Integer longEnd) {
        this.longEnd = longEnd;
    }

    public Integer getRiverMileStart() {
        return riverMileStart;
    }

    public void setRiverMileStart(Integer riverMileStart) {
        this.riverMileStart = riverMileStart;
    }

    public Integer getRiverMileEnd() {
        return riverMileEnd;
    }

    public void setRiverMileEnd(Integer riverMileEnd) {
        this.riverMileEnd = riverMileEnd;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getShapeType() {
        return shapeType;
    }

    public void setShapeType(String shapeType) {
        this.shapeType = shapeType;
    }

//    public Object getShape() {
//        return shape;
//    }
//
//    public void setShape(Object shape) {
//        this.shape = shape;
//    }

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

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getUpdatedById() {
        return updatedById;
    }

    public void setUpdatedById(Integer updatedById) {
        this.updatedById = updatedById;
    }

    public WtCropIdling getWtCropIdling() {
        return wtCropIdling;
    }

    public void setWtCropIdling(WtCropIdling wtCropIdling) {
        this.wtCropIdling = wtCropIdling;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wtLocationId != null ? wtLocationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WtLocation)) {
            return false;
        }
        WtLocation other = (WtLocation) object;
        if ((this.wtLocationId == null && other.wtLocationId != null) || (this.wtLocationId != null && !this.wtLocationId.equals(other.wtLocationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gei.entities.WtLocation[ wtLocationId=" + wtLocationId + " ]";
    }
    
}
