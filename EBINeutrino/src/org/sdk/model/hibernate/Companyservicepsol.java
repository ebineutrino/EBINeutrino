package org.sdk.model.hibernate;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "COMPANYSERVICEPSOL", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Companyservicepsol.findAll", query = "SELECT c FROM Companyservicepsol c"),
    @NamedQuery(name = "Companyservicepsol.findByProsolid", query = "SELECT c FROM Companyservicepsol c WHERE c.prosolid = :prosolid"),
    @NamedQuery(name = "Companyservicepsol.findBySolutionnr", query = "SELECT c FROM Companyservicepsol c WHERE c.solutionnr = :solutionnr"),
    @NamedQuery(name = "Companyservicepsol.findByName", query = "SELECT c FROM Companyservicepsol c WHERE c.name = :name"),
    @NamedQuery(name = "Companyservicepsol.findByClassification", query = "SELECT c FROM Companyservicepsol c WHERE c.classification = :classification"),
    @NamedQuery(name = "Companyservicepsol.findByCategory", query = "SELECT c FROM Companyservicepsol c WHERE c.category = :category"),
    @NamedQuery(name = "Companyservicepsol.findByType", query = "SELECT c FROM Companyservicepsol c WHERE c.type = :type"),
    @NamedQuery(name = "Companyservicepsol.findByStatus", query = "SELECT c FROM Companyservicepsol c WHERE c.status = :status"),
    @NamedQuery(name = "Companyservicepsol.findByCreatedfrom", query = "SELECT c FROM Companyservicepsol c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Companyservicepsol.findByCreateddate", query = "SELECT c FROM Companyservicepsol c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Companyservicepsol.findByChangedfrom", query = "SELECT c FROM Companyservicepsol c WHERE c.changedfrom = :changedfrom"),
    @NamedQuery(name = "Companyservicepsol.findByChangeddate", query = "SELECT c FROM Companyservicepsol c WHERE c.changeddate = :changeddate")})
public class Companyservicepsol implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PROSOLID")
    private Integer prosolid;
    @Column(name = "SOLUTIONNR")
    private String solutionnr;
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
    @JoinColumn(name = "SERVICEID", referencedColumnName = "SERVICEID")
    @ManyToOne
    private Companyservice companyservice;

    public Companyservicepsol() { }

    public Companyservicepsol(Integer prosolid) {
        this.prosolid = prosolid;
    }

    public Integer getProsolid() {
        return prosolid;
    }

    public void setProsolid(Integer prosolid) {
        this.prosolid = prosolid;
    }

    public String getSolutionnr() {
        return solutionnr;
    }

    public void setSolutionnr(String solutionnr) {
        this.solutionnr = solutionnr;
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

    public Companyservice getCompanyservice() {
        return companyservice;
    }

    public void setCompanyservice(Companyservice companyservice) {
        this.companyservice = companyservice;
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
        if (!(object instanceof Companyservicepsol)) {
            return false;
        }
        Companyservicepsol other = (Companyservicepsol) object;
        if ((this.prosolid == null && other.prosolid != null) || (this.prosolid != null && !this.prosolid.equals(other.prosolid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Companyservicepsol[ prosolid=" + prosolid + " ]";
    }
    
}
