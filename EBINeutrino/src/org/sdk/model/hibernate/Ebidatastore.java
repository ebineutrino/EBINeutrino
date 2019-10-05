package org.sdk.model.hibernate;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "EBIDATASTORE", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ebidatastore.findAll", query = "SELECT e FROM Ebidatastore e"),
    @NamedQuery(name = "Ebidatastore.findById", query = "SELECT e FROM Ebidatastore e WHERE e.id = :id"),
    @NamedQuery(name = "Ebidatastore.findByName", query = "SELECT e FROM Ebidatastore e WHERE e.name = :name"),
    @NamedQuery(name = "Ebidatastore.findByExtra", query = "SELECT e FROM Ebidatastore e WHERE e.extra = :extra")})
public class Ebidatastore implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME")
    private String name;
    @Lob
    @Column(name = "TEXT")
    private String text;
    @Column(name = "EXTRA")
    private String extra;

    public Ebidatastore() {}

    public Ebidatastore(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
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
        if (!(object instanceof Ebidatastore)) {
            return false;
        }
        Ebidatastore other = (Ebidatastore) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Ebidatastore[ id=" + id + " ]";
    }
    
}
