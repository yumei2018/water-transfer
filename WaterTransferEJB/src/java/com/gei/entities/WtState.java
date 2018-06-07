/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ymei
 */
@Entity
@Table(name = "WT_STATE")
@NamedQueries({
    @NamedQuery(name = "WtState.findAll", query = "SELECT w FROM WtState w")})
public class WtState extends AbstractEntity<WtState> implements Serializable {    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "WT_STATE_ID")
    private Integer wtStateId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NAME")
    private String name;
    @Column(name = "SHORT_NAME")
    private String shortName;
    @OneToMany(mappedBy = "wtState")
    private Collection<WtAddress> wtAddressCollection;
    @OneToMany(mappedBy = "wtState")
    private Collection<WtContact> wtContactCollection;

    public WtState() {
    }

    public WtState(Integer wtStateId) {
        this.wtStateId = wtStateId;
    }

    public WtState(Integer wtStateId, String name) {
        this.wtStateId = wtStateId;
        this.name = name;
    }

    public Integer getWtStateId() {
        return wtStateId;
    }

    public void setWtStateId(Integer wtStateId) {
        this.wtStateId = wtStateId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wtStateId != null ? wtStateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WtState)) {
            return false;
        }
        WtState other = (WtState) object;
        if ((this.wtStateId == null && other.wtStateId != null) || (this.wtStateId != null && !this.wtStateId.equals(other.wtStateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gei.entities.WtState[ wtStateId=" + wtStateId + " ]";
    }

    public Collection<WtContact> getWtContactCollection() {
        return wtContactCollection;
    }

    public void setWtContactCollection(Collection<WtContact> wtContactCollection) {
        this.wtContactCollection = wtContactCollection;
    }
    
}
