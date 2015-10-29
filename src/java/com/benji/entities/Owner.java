package com.benji.entities;

import com.benji.models.Link;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Benjamin
 */
@Entity
@Table(name = "owner")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Owner.findAll", query = "SELECT o FROM Owner o"),
    @NamedQuery(name = "Owner.findById", query = "SELECT o FROM Owner o WHERE o.id = :id"),
    @NamedQuery(name = "Owner.findByFirstName", query = "SELECT o FROM Owner o WHERE o.firstName = :firstName"),
    @NamedQuery(name = "Owner.findByLastName", query = "SELECT o FROM Owner o WHERE o.lastName = :lastName"),
    @NamedQuery(name = "Owner.findBySsn", query = "SELECT o FROM Owner o WHERE o.ssn = :ssn"),
    @NamedQuery(name = "Owner.findByUserName", query = "SELECT o FROM Owner o WHERE o.userName = :userName"),
    @NamedQuery(name = "Owner.findByPassword", query = "SELECT o FROM Owner o WHERE o.password = :password")})
public class Owner implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 64)
    @Column(name = "firstName")
    private String firstName;
    @Size(max = 64)
    @Column(name = "lastName")
    private String lastName;
    @Size(max = 12)
    @Column(name = "ssn")
    private String ssn;
    @Size(max = 24)
    @Column(name = "userName")
    private String userName;
    @Size(max = 16)
    @Column(name = "password")
    private String password;
    @JoinTable(name = "game_owner", joinColumns = {
        @JoinColumn(name = "owner_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "game_id", referencedColumnName = "id")})
    @ManyToMany
    private List<Game> gameList;
    @ManyToMany(mappedBy = "ownerList")
    private List<Platform> platformList;
    @Transient
    private List<Link> links;

    public Owner() {
    }

    public Owner(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @XmlTransient
    public List<Game> getGameList() {
        return gameList;
    }

    public void setGameList(List<Game> gameList) {
        this.gameList = gameList;
    }
    
    @XmlTransient
    public List<Platform> getPlatformList() {
        return platformList;
    }

    public void setPlatformList(List<Platform> platformList) {
        this.platformList = platformList;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void addLink(String url, String rel) {
        Link link = new Link(url, rel);
        this.links.add(link);
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
        if (!(object instanceof Owner)) {
            return false;
        }
        Owner other = (Owner) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.benji.entities.Owner[ id=" + id + " ]";
    }

}
