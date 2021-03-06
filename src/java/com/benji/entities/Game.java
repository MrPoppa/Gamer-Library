package com.benji.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "game")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Game.findAll", query = "SELECT g FROM Game g"),
    @NamedQuery(name = "Game.findById", query = "SELECT g FROM Game g WHERE g.id = :id"),
    @NamedQuery(name = "Game.findByGameName", query = "SELECT g FROM Game g WHERE g.gameName = :gameName")})
public class Game implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 64)
    @Column(name = "gameName")
    private String gameName;
    @ManyToMany(mappedBy = "gameList")
    private List<Owner> ownerList;
    @ManyToMany(mappedBy = "gameList", cascade = CascadeType.PERSIST)
    private List<Genre> genreList;
    @OneToMany(mappedBy = "game")
    private List<GameOfTheDay> gameOfTheDayList;
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    @ManyToOne
    private GameBrand brand;
    @JoinColumn(name = "platform_id", referencedColumnName = "id")
    @ManyToOne
    private Platform platform;
    @JoinColumn(name = "rating_id", referencedColumnName = "id")
    @ManyToOne
    private GameRating rating;
    @OneToMany(mappedBy = "game")
    private List<GameReceipt> gameReceiptList;

    public Game() {
    }

    public Game(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    @XmlTransient
    public List<Owner> getOwnerList() {
        return ownerList;
    }

    public void setOwnerList(List<Owner> ownerList) {
        this.ownerList = ownerList;
    }

//    @XmlTransient
    public List<Genre> getGenreList() {
        return genreList;
    }

    public void setGenreList(List<Genre> genreList) {
        this.genreList = genreList;
    }

    @XmlTransient
    public List<GameOfTheDay> getGameOfTheDayList() {
        return gameOfTheDayList;
    }

    public void setGameOfTheDayList(List<GameOfTheDay> gameOfTheDayList) {
        this.gameOfTheDayList = gameOfTheDayList;
    }

    public GameBrand getBrand() {
        return brand;
    }

    public void setBrand(GameBrand brand) {
        this.brand = brand;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public GameRating getRating() {
        return rating;
    }

    public void setRating(GameRating rating) {
        this.rating = rating;
    }

    @XmlTransient
    public List<GameReceipt> getGameReceiptList() {
        return gameReceiptList;
    }

    public void setGameReceiptList(List<GameReceipt> gameReceiptList) {
        this.gameReceiptList = gameReceiptList;
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
        if (!(object instanceof Game)) {
            return false;
        }
        Game other = (Game) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.benji.entities.Game[ id=" + id + " ]";
    }

}
