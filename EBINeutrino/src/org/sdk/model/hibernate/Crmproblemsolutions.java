package org.sdk.model.hibernate;

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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "CRMPROBLEMSOLUTIONS", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Crmproblemsolutions.findAll", query = "SELECT c FROM Crmproblemsolutions c"),
    @NamedQuery(name = "Crmproblemsolutions.findByProsolid", query = "SELECT c FROM Crmproblemsolutions c WHERE c.prosolid = :prosolid"),
    @NamedQuery(name = "Crmproblemsolutions.findByServicenr", query = "SELECT c FROM Crmproblemsolutions c WHERE c.servicenr = :servicenr"),
    @NamedQuery(name = "Crmproblemsolutions.findByName", query = "SELECT c FROM Crmproblemsolutions c WHERE c.name = :name"),
    @NamedQuery(name = "Crmproblemsolutions.findByClassification", query = "SELECT c FROM Crmproblemsolutions c WHERE c.classification = :classification"),
    @NamedQuery(name = "Crmproblemsolutions.findByCategory", query = "SELECT c FROM Crmproblemsolutions c WHERE c.category = :category"),
    @NamedQuery(name = "Crmproblemsolutions.findByType", query = "SELECT c FROM Crmproblemsolutions c WHERE c.type = :type"),
    @NamedQuery(name = "Crmproblemsolutions.findByStatus", query = "SELECT c FROM Crmproblemsolutions c WHERE c.status = :status"),
    @NamedQuery(name = "Crmproblemsolutions.findByCreatedfrom", query = "SELECT c FROM Crmproblemsolutions c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Crmproblemsolutions.findByCreateddate", query = "SELECT c FROM Crmproblemsolutions c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Crmproblemsolutions.findByChangedfrom", query = "SELECT c FROM Crmproblemsolutions c WHERE c.changedfrom = :changedfrom"),
    @NamedQuery(name = "Crmproblemsolutions.findByChangeddate", query = "SELECT c FROM Crmproblemsolutions c WHERE c.changeddate = :changeddate")})
public class Crmproblemsolutions implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PROSOLID")
    private Integer prosolid;
    @Column(name = "SERVICENR")
    private String servicenr;
    @Column(name = "NAME")
    private String name;
    @Column(name = "CLASSIFICATION")
    private String classification;
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
    @OneToMany(mappedBy = "crmproblemsolutions")
    private Set<Crmproblemsolposition> crmproblemsolpositionSet = new HashSet();
    @OneToMany(mappedBy = "crmproblemsolutions")
    private Set<Crmproblemsoldocs> crmproblemsoldocsSet = new HashSet();

    public Crmproblemsolutions() { }

    public Crmproblemsolutions(Integer prosolid) {
        this.prosolid = prosolid;
    }

    public Integer getProsolid() {
        return prosolid;
    }

    public void setProsolid(Integer prosolid) {
        this.prosolid = prosolid;
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

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
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
    public Set<Crmproblemsolposition> getCrmproblemsolpositions() {
        return crmproblemsolpositionSet;
    }

    public void setCrmproblemsolpositions(Set<Crmproblemsolposition> crmproblemsolpositionSet) {
        this.crmproblemsolpositionSet = crmproblemsolpositionSet;
    }

    @XmlTransient
    public Set<Crmproblemsoldocs> getCrmproblemsoldocses() {
        return crmproblemsoldocsSet;
    }

    public void setCrmproblemsoldocses(Set<Crmproblemsoldocs> crmproblemsoldocsSet) {
        this.crmproblemsoldocsSet = crmproblemsoldocsSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (prosolid != null ? prosolid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Crmproblemsolutions)) {
            return false;
        }
        Crmproblemsolutions other = (Crmproblemsolutions) object;
        if ((this.prosolid == null && other.prosolid != null) || (this.prosolid != null && !this.prosolid.equals(other.prosolid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Crmproblemsolutions[ prosolid=" + prosolid + " ]";
    }
    
}
