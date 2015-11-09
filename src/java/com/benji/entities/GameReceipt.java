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

/**
 *
 * @author Benjamin Bengtsson
 */
@Entity
@Table(name = "game_receipt")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GameReceipt.findAll", query = "SELECT g FROM GameReceipt g"),
    @NamedQuery(name = "GameReceipt.findById", query = "SELECT g FROM GameReceipt g WHERE g.id = :id"),
    @NamedQuery(name = "GameReceipt.findByPrice", query = "SELECT g FROM GameReceipt g WHERE g.price = :price"),
    @NamedQuery(name = "GameReceipt.findByBuyDate", query = "SELECT g FROM GameReceipt g WHERE g.buyDate = :buyDate")})
public class GameReceipt implements Serializable {
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
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    @ManyToOne
    private Game game;
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    @ManyToOne
    private Owner owner;

    public GameReceipt() {
    }

    public GameReceipt(Integer id) {
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

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
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
        if (!(object instanceof GameReceipt)) {
            return false;
        }
        GameReceipt other = (GameReceipt) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.benji.entities.GameReciept[ id=" + id + " ]";
    }

}
