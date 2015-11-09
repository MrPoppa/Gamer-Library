package com.benji.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Benjamin Bengtsson
 */
@Entity
@Table(name = "platform_brand")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlatformBrand.findAll", query = "SELECT p FROM PlatformBrand p"),
    @NamedQuery(name = "PlatformBrand.findById", query = "SELECT p FROM PlatformBrand p WHERE p.id = :id"),
    @NamedQuery(name = "PlatformBrand.findByBrandName", query = "SELECT p FROM PlatformBrand p WHERE p.brandName = :brandName")})
public class PlatformBrand implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 64)
    @Column(name = "brandName")
    private String brandName;
    @OneToMany(mappedBy = "brand")
    private List<Platform> platformList;

    public PlatformBrand() {
    }

    public PlatformBrand(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    @XmlTransient
    public List<Platform> getPlatformList() {
        return platformList;
    }

    public void setPlatformList(List<Platform> platformList) {
        this.platformList = platformList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlatformBrand)) {
            return false;
        }
        PlatformBrand other = (PlatformBrand) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.benji.entities.PlatformBrand[ id=" + id + " ]";
    }

}
