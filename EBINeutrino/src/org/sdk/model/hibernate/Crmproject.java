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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "CRMPROJECT", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Crmproject.findAll", query = "SELECT c FROM Crmproject c"),
    @NamedQuery(name = "Crmproject.findByProjectid", query = "SELECT c FROM Crmproject c WHERE c.projectid = :projectid"),
    @NamedQuery(name = "Crmproject.findByProjectnr", query = "SELECT c FROM Crmproject c WHERE c.projectnr = :projectnr"),
    @NamedQuery(name = "Crmproject.findByCreateddate", query = "SELECT c FROM Crmproject c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Crmproject.findByCreatedfrom", query = "SELECT c FROM Crmproject c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Crmproject.findByChangeddate", query = "SELECT c FROM Crmproject c WHERE c.changeddate = :changeddate"),
    @NamedQuery(name = "Crmproject.findByChangedfrom", query = "SELECT c FROM Crmproject c WHERE c.changedfrom = :changedfrom"),
    @NamedQuery(name = "Crmproject.findByName", query = "SELECT c FROM Crmproject c WHERE c.name = :name"),
    @NamedQuery(name = "Crmproject.findByManager", query = "SELECT c FROM Crmproject c WHERE c.manager = :manager"),
    @NamedQuery(name = "Crmproject.findByBudget", query = "SELECT c FROM Crmproject c WHERE c.budget = :budget"),
    @NamedQuery(name = "Crmproject.findByActualcost", query = "SELECT c FROM Crmproject c WHERE c.actualcost = :actualcost"),
    @NamedQuery(name = "Crmproject.findByRemaincost", query = "SELECT c FROM Crmproject c WHERE c.remaincost = :remaincost"),
    @NamedQuery(name = "Crmproject.findByStatus", query = "SELECT c FROM Crmproject c WHERE c.status = :status"),
    @NamedQuery(name = "Crmproject.findByValidfrom", query = "SELECT c FROM Crmproject c WHERE c.validfrom = :validfrom"),
    @NamedQuery(name = "Crmproject.findByValidto", query = "SELECT c FROM Crmproject c WHERE c.validto = :validto")})
public class Crmproject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PROJECTID")
    private Integer projectid;
    @Column(name = "PROJECTNR")
    private String projectnr;
    @Column(name = "CREATEDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createddate;
    @Column(name = "CREATEDFROM")
    private String createdfrom;
    @Column(name = "CHANGEDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date changeddate;
    @Column(name = "CHANGEDFROM")
    private String changedfrom;
    @Column(name = "NAME")
    private String name;
    @Column(name = "MANAGER")
    private String manager;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "BUDGET")
    private Double budget;
    @Column(name = "ACTUALCOST")
    private Double actualcost;
    @Column(name = "REMAINCOST")
    private Double remaincost;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "VALIDFROM")
    @Temporal(TemporalType.TIMESTAMP)
    private Date validfrom;
    @Column(name = "VALIDTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date validto;
    @OneToMany(mappedBy = "crmproject")
    private Set<Crmprojecttask> crmprojecttaskSet = new HashSet();

    public Crmproject() {}

    public Crmproject(Integer projectid) {
        this.projectid = projectid;
    }

    public Integer getProjectid() {
        return projectid;
    }

    public void setProjectid(Integer projectid) {
        this.projectid = projectid;
    }

    public String getProjectnr() {
        return projectnr;
    }

    public void setProjectnr(String projectnr) {
        this.projectnr = projectnr;
    }

    public Date getCreateddate() {
        return createddate;
    }

    public void setCreateddate(Date createddate) {
        this.createddate = createddate;
    }

    public String getCreatedfrom() {
        return createdfrom;
    }

    public void setCreatedfrom(String createdfrom) {
        this.createdfrom = createdfrom;
    }

    public Date getChangeddate() {
        return changeddate;
    }

    public void setChangeddate(Date changeddate) {
        this.changeddate = changeddate;
    }

    public String getChangedfrom() {
        return changedfrom;
    }

    public void setChangedfrom(String changedfrom) {
        this.changedfrom = changedfrom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Double getActualcost() {
        return actualcost;
    }

    public void setActualcost(Double actualcost) {
        this.actualcost = actualcost;
    }

    public Double getRemaincost() {
        return remaincost;
    }

    public void setRemaincost(Double remaincost) {
        this.remaincost = remaincost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getValidfrom() {
        return validfrom;
    }

    public void setValidfrom(Date validfrom) {
        this.validfrom = validfrom;
    }

    public Date getValidto() {
        return validto;
    }

    public void setValidto(Date validto) {
        this.validto = validto;
    }

    @XmlTransient
    public Set<Crmprojecttask> getCrmprojecttasks() {
        return crmprojecttaskSet;
    }

    public void setCrmprojecttasks(Set<Crmprojecttask> crmprojecttaskSet) {
        this.crmprojecttaskSet = crmprojecttaskSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (projectid != null ? projectid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Crmproject)) {
            return false;
        }
        Crmproject other = (Crmproject) object;
        if ((this.projectid == null && other.projectid != null) || (this.projectid != null && !this.projectid.equals(other.projectid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Crmproject[ projectid=" + projectid + " ]";
    }
    
}
