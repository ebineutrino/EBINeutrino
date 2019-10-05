package org.sdk.model.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "COMPANYCATEGORY", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Companycategory.findAll", query = "SELECT c FROM Companycategory c"),
    @NamedQuery(name = "Companycategory.findById", query = "SELECT c FROM Companycategory c WHERE c.id = :id"),
    @NamedQuery(name = "Companycategory.findByName", query = "SELECT c FROM Companycategory c WHERE c.name = :name")})
public class Companycategory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME")
    
    private String name;
    @OneToMany(mappedBy = "categoryid")
    private List<Companynumber> companynumberList = new ArrayList();

    public Companycategory(){}

    public Companycategory(Integer id) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Companycategory)) {
            return false;
        }
        Companycategory other = (Companycategory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Companycategory[ id=" + id + " ]";
    }

    @XmlTransient
    public List<Companynumber> getCompanynumberList() {
        return companynumberList;
    }

    public void setCompanynumberList(List<Companynumber> companynumberList) {
        this.companynumberList = companynumberList;
    }
    
}
