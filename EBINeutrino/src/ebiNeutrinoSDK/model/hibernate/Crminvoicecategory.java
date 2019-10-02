package ebiNeutrinoSDK.model.hibernate;

import java.io.Serializable;
import java.util.HashSet;
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
@Table(name = "CRMINVOICECATEGORY", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Crminvoicecategory.findAll", query = "SELECT c FROM Crminvoicecategory c"),
    @NamedQuery(name = "Crminvoicecategory.findById", query = "SELECT c FROM Crminvoicecategory c WHERE c.id = :id"),
    @NamedQuery(name = "Crminvoicecategory.findByName", query = "SELECT c FROM Crminvoicecategory c WHERE c.name = :name")})
public class Crminvoicecategory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME")
    private String name;
    @OneToMany(mappedBy = "crminvoicecategory")
    private Set<Crminvoicenumber> crminvoicenumberSet = new HashSet();

    public Crminvoicecategory(){}

    public Crminvoicecategory(Integer id) {
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

    @XmlTransient
    public Set<Crminvoicenumber> getCrminvoicenumberSet() {
        return crminvoicenumberSet;
    }

    public void setCrminvoicenumberSet(Set<Crminvoicenumber> crminvoicenumberSet) {
        this.crminvoicenumberSet = crminvoicenumberSet;
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
        if (!(object instanceof Crminvoicecategory)) {
            return false;
        }
        Crminvoicecategory other = (Crminvoicecategory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Crminvoicecategory[ id=" + id + " ]";
    }
    
}
