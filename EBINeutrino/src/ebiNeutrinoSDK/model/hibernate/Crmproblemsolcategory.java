package ebiNeutrinoSDK.model.hibernate;

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
@Table(name = "CRMPROBLEMSOLCATEGORY", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Crmproblemsolcategory.findAll", query = "SELECT c FROM Crmproblemsolcategory c"),
    @NamedQuery(name = "Crmproblemsolcategory.findById", query = "SELECT c FROM Crmproblemsolcategory c WHERE c.id = :id"),
    @NamedQuery(name = "Crmproblemsolcategory.findByName", query = "SELECT c FROM Crmproblemsolcategory c WHERE c.name = :name")})
public class Crmproblemsolcategory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME")
    private String name;

    public Crmproblemsolcategory() { }

    public Crmproblemsolcategory(Integer id) {
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
        if (!(object instanceof Crmproblemsolcategory)) {
            return false;
        }
        Crmproblemsolcategory other = (Crmproblemsolcategory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Crmproblemsolcategory[ id=" + id + " ]";
    }
    
}
