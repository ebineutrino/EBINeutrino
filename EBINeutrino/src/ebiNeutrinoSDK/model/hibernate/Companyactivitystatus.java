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
@Table(name = "COMPANYACTIVITYSTATUS", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Companyactivitystatus.findAll", query = "SELECT c FROM Companyactivitystatus c"),
    @NamedQuery(name = "Companyactivitystatus.findById", query = "SELECT c FROM Companyactivitystatus c WHERE c.id = :id"),
    @NamedQuery(name = "Companyactivitystatus.findByName", query = "SELECT c FROM Companyactivitystatus c WHERE c.name = :name")})
public class Companyactivitystatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME")
    private String name;

    public Companyactivitystatus() {}

    public Companyactivitystatus(Integer id) {
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
        if (!(object instanceof Companyactivitystatus)) {
            return false;
        }
        Companyactivitystatus other = (Companyactivitystatus) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Companyactivitystatus[ id=" + id + " ]";
    }
    
}
