package org.sdk.model.hibernate;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "CRMPROJECTTASKTYPE", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Crmprojecttasktype.findAll", query = "SELECT c FROM Crmprojecttasktype c"),
    @NamedQuery(name = "Crmprojecttasktype.findById", query = "SELECT c FROM Crmprojecttasktype c WHERE c.id = :id"),
    @NamedQuery(name = "Crmprojecttasktype.findByName", query = "SELECT c FROM Crmprojecttasktype c WHERE c.name = :name")})
public class Crmprojecttasktype implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;

    public Crmprojecttasktype() {}

    public Crmprojecttasktype(Integer id) {
        this.id = id;
    }

    public Crmprojecttasktype(Integer id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Crmprojecttasktype)) {
            return false;
        }
        Crmprojecttasktype other = (Crmprojecttasktype) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Crmprojecttasktype[ id=" + id + " ]";
    }
    
}
