package com.gei.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ymei
 */
@Entity
@Table(name = "WT_COUNTY")
@NamedQueries({
    @NamedQuery(name = "WtCounty.findAll", query = "SELECT w FROM WtCounty w")})
public class WtCounty extends AbstractEntity<WtCounty> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "WT_COUNTY_ID")
    private Integer wtCountyId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NAME")
    private String name;
    @Column(name = "SHORT_NAME")
    private String shortName;
    @OneToMany(mappedBy = "wtCounty")
    private Collection<WtAddress> wtAddressCollection;
    @JoinTable(name = "WT_TRANS_COUNTY", joinColumns = {
        @JoinColumn(name = "WT_COUNTY_ID", referencedColumnName = "WT_COUNTY_ID")}, inverseJoinColumns = {
        @JoinColumn(name = "WT_TRANS_ID", referencedColumnName = "WT_TRANS_ID")})
    @ManyToMany
    private Collection<WtTrans> wtTransCollection;

    public WtCounty() {
    }

    public WtCounty(Integer wtCountyId) {
        this.wtCountyId = wtCountyId;
    }

    public WtCounty(Integer wtCountyId, String name) {
        this.wtCountyId = wtCountyId;
        this.name = name;
    }

    public Integer getWtCountyId() {
        return wtCountyId;
    }

    public void setWtCountyId(Integer wtCountyId) {
        this.wtCountyId = wtCountyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Collection<WtAddress> getWtAddressCollection() {
        return wtAddressCollection;
    }

    public void setWtAddressCollection(Collection<WtAddress> wtAddressCollection) {
        this.wtAddressCollection = wtAddressCollection;
    }

//    public WtTrans getWtTrans() {
//      return wtTrans;
//    }
//
//    public void setWtTrans(WtTrans wtTrans) {
//      this.wtTrans = wtTrans;
//    }

    public Collection<WtTrans> getWtTransCollection() {
      return wtTransCollection;
    }

    public void setWtTransCollection(Collection<WtTrans> wtTransCollection) {
      this.wtTransCollection = wtTransCollection;
    }    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wtCountyId != null ? wtCountyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WtCounty)) {
            return false;
        }
        WtCounty other = (WtCounty) object;
        if ((this.wtCountyId == null && other.wtCountyId != null) || (this.wtCountyId != null && !this.wtCountyId.equals(other.wtCountyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gei.entities.WtCounty[ wtCountyId=" + wtCountyId + " ]";
    }
    
}
