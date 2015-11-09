package com.benji.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
@Table(name = "platform")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Platform.findAll", query = "SELECT p FROM Platform p"),
    @NamedQuery(name = "Platform.findById", query = "SELECT p FROM Platform p WHERE p.id = :id"),
    @NamedQuery(name = "Platform.findByPlatformName", query = "SELECT p FROM Platform p WHERE p.platformName = :platformName"),
    //SQL: SELECT p.* FROM platform AS p JOIN platform_owner AS po ON p.id = po.platform_id WHERE owner_id = 2;
    @NamedQuery(name = "Platform.findAllPlatformsByOwnerId", query = "SELECT p FROM Platform p JOIN p.ownerList o WHERE o.id = :ownerId")})
public class Platform implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 64)
    @Column(name = "platformName")
    private String platformName;
    @JoinTable(name = "platform_owner", joinColumns = {
        @JoinColumn(name = "platform_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "owner_id", referencedColumnName = "id")})
    @ManyToMany
    private List<Owner> ownerList;
    @OneToMany(mappedBy = "platform")
    private List<Game> gameList;
    @OneToMany(mappedBy = "platform")
    private List<PlatformReceipt> platformReceiptList;
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    @ManyToOne
    private PlatformBrand brand;

    public Platform() {
    }

    public Platform(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    @XmlTransient
    public List<Owner> getOwnerList() {
        return ownerList;
    }

    public void setOwnerList(List<Owner> ownerList) {
        this.ownerList = ownerList;
    }

    @XmlTransient
    public List<Game> getGameList() {
        return gameList;
    }

    public void setGameList(List<Game> gameList) {
        this.gameList = gameList;
    }

    @XmlTransient
    public List<PlatformReceipt> getPlatformReceiptList() {
        return platformReceiptList;
    }

    public void setPlatformReceiptList(List<PlatformReceipt> platformReceiptList) {
        this.platformReceiptList = platformReceiptList;
    }

    public PlatformBrand getBrand() {
        return brand;
    }

    public void setBrand(PlatformBrand brand) {
        this.brand = brand;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.id);
        hash = 83 * hash + Objects.hashCode(this.platformName);
        hash = 83 * hash + Objects.hashCode(this.ownerList);
        hash = 83 * hash + Objects.hashCode(this.gameList);
        hash = 83 * hash + Objects.hashCode(this.platformReceiptList);
        hash = 83 * hash + Objects.hashCode(this.brand);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Platform other = (Platform) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.platformName, other.platformName)) {
            return false;
        }
        if (!Objects.equals(this.ownerList, other.ownerList)) {
            return false;
        }
        if (!Objects.equals(this.gameList, other.gameList)) {
            return false;
        }
        if (!Objects.equals(this.platformReceiptList, other.platformReceiptList)) {
            return false;
        }
        if (!Objects.equals(this.brand, other.brand)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.benji.entities.Platform[ id=" + id + " ]";
    }

}
