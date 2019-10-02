package ebiNeutrinoSDK.model.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "COMPANYSERVICE", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Companyservice.findAll", query = "SELECT c FROM Companyservice c"),
    @NamedQuery(name = "Companyservice.findByServiceid", query = "SELECT c FROM Companyservice c WHERE c.serviceid = :serviceid"),
    @NamedQuery(name = "Companyservice.findByServicenr", query = "SELECT c FROM Companyservice c WHERE c.servicenr = :servicenr"),
    @NamedQuery(name = "Companyservice.findByName", query = "SELECT c FROM Companyservice c WHERE c.name = :name"),
    @NamedQuery(name = "Companyservice.findByCategory", query = "SELECT c FROM Companyservice c WHERE c.category = :category"),
    @NamedQuery(name = "Companyservice.findByType", query = "SELECT c FROM Companyservice c WHERE c.type = :type"),
    @NamedQuery(name = "Companyservice.findByStatus", query = "SELECT c FROM Companyservice c WHERE c.status = :status"),
    @NamedQuery(name = "Companyservice.findByCreatedfrom", query = "SELECT c FROM Companyservice c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Companyservice.findByCreateddate", query = "SELECT c FROM Companyservice c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Companyservice.findByChangedfrom", query = "SELECT c FROM Companyservice c WHERE c.changedfrom = :changedfrom"),
    @NamedQuery(name = "Companyservice.findByChangeddate", query = "SELECT c FROM Companyservice c WHERE c.changeddate = :changeddate")})
public class Companyservice implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "SERVICEID")
    private Integer serviceid;
    @Column(name = "SERVICENR")
    private String servicenr;
    @Column(name = "NAME")
    private String name;
    @Column(name = "CATEGORY")
    private String category;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "STATUS")
    private String status;
    @Lob
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "CREATEDFROM")
    private String createdfrom;
    @Column(name = "CREATEDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createddate;
    @Column(name = "CHANGEDFROM")
    private String changedfrom;
    @Column(name = "CHANGEDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date changeddate;
    @OneToMany(mappedBy = "companyservice")
    private Set<Companyservicepositions> companyservicepositionsSet = new HashSet();
    @JoinColumn(name = "COMPANYID", referencedColumnName = "COMPANYID")
    @ManyToOne
    private Company company;
    @OneToMany(mappedBy = "companyservice")
    private Set<Companyservicedocs> companyservicedocsSet = new HashSet();
    @OneToMany(mappedBy = "companyservice")
    private Set<Companyservicepsol> companyservicepsolSet = new HashSet();

    public Companyservice() { }

    public Companyservice(Integer serviceid) {
        this.serviceid = serviceid;
    }

    public Integer getServiceid() {
        return serviceid;
    }

    public void setServiceid(Integer serviceid) {
        this.serviceid = serviceid;
    }

    public String getServicenr() {
        return servicenr;
    }

    public void setServicenr(String servicenr) {
        this.servicenr = servicenr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedfrom() {
        return createdfrom;
    }

    public void setCreatedfrom(String createdfrom) {
        this.createdfrom = createdfrom;
    }

    public Date getCreateddate() {
        return createddate;
    }

    public void setCreateddate(Date createddate) {
        this.createddate = createddate;
    }

    public String getChangedfrom() {
        return changedfrom;
    }

    public void setChangedfrom(String changedfrom) {
        this.changedfrom = changedfrom;
    }

    public Date getChangeddate() {
        return changeddate;
    }

    public void setChangeddate(Date changeddate) {
        this.changeddate = changeddate;
    }

    @XmlTransient
    public Set<Companyservicepositions> getCompanyservicepositionses() {
        return companyservicepositionsSet;
    }

    public void setCompanyservicepositionses(Set<Companyservicepositions> companyservicepositionsSet) {
        this.companyservicepositionsSet = companyservicepositionsSet;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @XmlTransient
    public Set<Companyservicedocs> getCompanyservicedocses() {
        return companyservicedocsSet;
    }

    public void setCompanyservicedocses(Set<Companyservicedocs> companyservicedocsSet) {
        this.companyservicedocsSet = companyservicedocsSet;
    }

    @XmlTransient
    public Set<Companyservicepsol> getCompanyservicepsols() {
        return companyservicepsolSet;
    }

    public void setCompanyservicepsols(Set<Companyservicepsol> companyservicepsolSet) {
        this.companyservicepsolSet = companyservicepsolSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (serviceid != null ? serviceid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Companyservice)) {
            return false;
        }
        Companyservice other = (Companyservice) object;
        if ((this.serviceid == null && other.serviceid != null) || (this.serviceid != null && !this.serviceid.equals(other.serviceid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Companyservice[ serviceid=" + serviceid + " ]";
    }
    
}
