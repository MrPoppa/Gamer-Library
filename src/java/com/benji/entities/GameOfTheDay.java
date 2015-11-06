package com.benji.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Benjamin
 */
@Entity
@Table(name = "game_of_the_day")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GameOfTheDay.findAll", query = "SELECT g FROM GameOfTheDay g"),
    @NamedQuery(name = "GameOfTheDay.findById", query = "SELECT g FROM GameOfTheDay g WHERE g.id = :id"),
    @NamedQuery(name = "GameOfTheDay.findByLastUpdateDate", query = "SELECT g FROM GameOfTheDay g WHERE g.lastUpdateDate = :lastUpdateDate")})
public class GameOfTheDay implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Column(name = "lastUpdateDate")
    @Temporal(TemporalType.DATE)
    private Date lastUpdateDate;
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    @ManyToOne
    private Game game;

    public GameOfTheDay() {
    }

    public GameOfTheDay(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
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
        if (!(object instanceof GameOfTheDay)) {
            return false;
        }
        GameOfTheDay other = (GameOfTheDay) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.benji.entities.GameOfTheDay[ id=" + id + " ]";
    }

}
