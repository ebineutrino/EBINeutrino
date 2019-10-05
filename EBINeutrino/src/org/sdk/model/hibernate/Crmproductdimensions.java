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
@Table(name = "CRMPRODUCTDIMENSIONS", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Crmproductdimensions.findAll", query = "SELECT c FROM Crmproductdimensions c"),
    @NamedQuery(name = "Crmproductdimensions.findById", query = "SELECT c FROM Crmproductdimensions c WHERE c.id = :id"),
    @NamedQuery(name = "Crmproductdimensions.findByName", query = "SELECT c FROM Crmproductdimensions c WHERE c.name = :name")})
public class Crmproductdimensions implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;

    public Crmproductdimensions() {}

    public Crmproductdimensions(Integer id) {
        this.id = id;
    }

    public Crmproductdimensions(Integer id, String name) {
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
        if (!(object instanceof Crmproductdimensions)) {
            return false;
        }
        Crmproductdimensions other = (Crmproductdimensions) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Crmproductdimensions[ id=" + id + " ]";
    }
    
}
