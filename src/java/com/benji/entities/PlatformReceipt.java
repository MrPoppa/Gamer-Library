package com.benji.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Benjamin
 */
@Entity
@Table(name = "platform_receipt")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlatformReceipt.findAll", query = "SELECT p FROM PlatformReceipt p"),
    @NamedQuery(name = "PlatformReceipt.findById", query = "SELECT p FROM PlatformReceipt p WHERE p.id = :id"),
    @NamedQuery(name = "PlatformReceipt.findByPrice", query = "SELECT p FROM PlatformReceipt p WHERE p.price = :price"),
    @NamedQuery(name = "PlatformReceipt.findByBuyDate", query = "SELECT p FROM PlatformReceipt p WHERE p.buyDate = :buyDate")})
public class PlatformReceipt implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "price")
    private Integer price;
    @Column(name = "buyDate")
    @Temporal(TemporalType.DATE)
    private Date buyDate;
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    @ManyToOne
    private Owner owner;
    @JoinColumn(name = "platform_id", referencedColumnName = "id")
    @ManyToOne
    private Platform platform;

    public PlatformReceipt() {
    }

    public PlatformReceipt(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    @XmlTransient
    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
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
        if (!(object instanceof PlatformReceipt)) {
            return false;
        }
        PlatformReceipt other = (PlatformReceipt) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.benji.entities.PlatformReceipt[ id=" + id + " ]";
    }

}
