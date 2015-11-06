package com.benji.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Benjamin
 */
@Entity
@Table(name = "platform")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Platform.findAll", query = "SELECT p FROM Platform p"),
    @NamedQuery(name = "Platform.findById", query = "SELECT p FROM Platform p WHERE p.id = :id"),
    @NamedQuery(name = "Platform.findByPlatformName", query = "SELECT p FROM Platform p WHERE p.platformName = :platformName"),
    @NamedQuery(name = "Platform.findByPrice", query = "SELECT p FROM Platform p WHERE p.price = :price"),
    @NamedQuery(name = "Platform.findByBuyDate", query = "SELECT p FROM Platform p WHERE p.buyDate = :buyDate"),
    //SQL: SELECT p.* FROM platform AS p JOIN platform_owner AS po ON p.id = po.platform_id WHERE owner_id = 2;
    @NamedQuery(name = "Platform.findAllPlatformsByOwnerId", query = "SELECT p FROM Platform p JOIN p.ownerList o WHERE o.id = :ownerId")
})
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
    @Column(name = "price")
    private Integer price;
    @Column(name = "buyDate")
    @Temporal(TemporalType.DATE)
    private Date buyDate;
    @JoinTable(name = "platform_owner", joinColumns = {
        @JoinColumn(name = "platform_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "owner_id", referencedColumnName = "id")})
    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Owner> ownerList;
    @OneToMany(mappedBy = "platform")
    private List<Game> gameList;
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
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

    public PlatformBrand getBrand() {
        return brand;
    }

    public void setBrand(PlatformBrand brand) {
        this.brand = brand;
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
        if (!(object instanceof Platform)) {
            return false;
        }
        Platform other = (Platform) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.benji.entities.Platform[ id=" + id + " ]";
    }

}
