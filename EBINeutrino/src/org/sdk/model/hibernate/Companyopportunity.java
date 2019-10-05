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
@Table(name = "COMPANYOPPORTUNITY", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Companyopportunity.findAll", query = "SELECT c FROM Companyopportunity c"),
    @NamedQuery(name = "Companyopportunity.findByOpportunityid", query = "SELECT c FROM Companyopportunity c WHERE c.opportunityid = :opportunityid"),
    @NamedQuery(name = "Companyopportunity.findByName", query = "SELECT c FROM Companyopportunity c WHERE c.name = :name"),
    @NamedQuery(name = "Companyopportunity.findBySalestage", query = "SELECT c FROM Companyopportunity c WHERE c.salestage = :salestage"),
    @NamedQuery(name = "Companyopportunity.findByProbability", query = "SELECT c FROM Companyopportunity c WHERE c.probability = :probability"),
    @NamedQuery(name = "Companyopportunity.findByOpportunityvalue", query = "SELECT c FROM Companyopportunity c WHERE c.opportunityvalue = :opportunityvalue"),
    @NamedQuery(name = "Companyopportunity.findByIsclose", query = "SELECT c FROM Companyopportunity c WHERE c.isclose = :isclose"),
    @NamedQuery(name = "Companyopportunity.findByClosedate", query = "SELECT c FROM Companyopportunity c WHERE c.closedate = :closedate"),
    @NamedQuery(name = "Companyopportunity.findByBusinesstype", query = "SELECT c FROM Companyopportunity c WHERE c.businesstype = :businesstype"),
    @NamedQuery(name = "Companyopportunity.findByEvaluationstatus", query = "SELECT c FROM Companyopportunity c WHERE c.evaluationstatus = :evaluationstatus"),
    @NamedQuery(name = "Companyopportunity.findByEvaluetiondate", query = "SELECT c FROM Companyopportunity c WHERE c.evaluetiondate = :evaluetiondate"),
    @NamedQuery(name = "Companyopportunity.findByBudgetstatus", query = "SELECT c FROM Companyopportunity c WHERE c.budgetstatus = :budgetstatus"),
    @NamedQuery(name = "Companyopportunity.findByBudgetdate", query = "SELECT c FROM Companyopportunity c WHERE c.budgetdate = :budgetdate"),
    @NamedQuery(name = "Companyopportunity.findBySalestagedate", query = "SELECT c FROM Companyopportunity c WHERE c.salestagedate = :salestagedate"),
    @NamedQuery(name = "Companyopportunity.findByOpportunitystatus", query = "SELECT c FROM Companyopportunity c WHERE c.opportunitystatus = :opportunitystatus"),
    @NamedQuery(name = "Companyopportunity.findByOpportunitystatusdate", query = "SELECT c FROM Companyopportunity c WHERE c.opportunitystatusdate = :opportunitystatusdate"),
    @NamedQuery(name = "Companyopportunity.findByCreatedfrom", query = "SELECT c FROM Companyopportunity c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Companyopportunity.findByCreateddate", query = "SELECT c FROM Companyopportunity c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Companyopportunity.findByChangedfrom", query = "SELECT c FROM Companyopportunity c WHERE c.changedfrom = :changedfrom"),
    @NamedQuery(name = "Companyopportunity.findByChangeddate", query = "SELECT c FROM Companyopportunity c WHERE c.changeddate = :changeddate")})
public class Companyopportunity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "OPPORTUNITYID")
    private Integer opportunityid;
    @Column(name = "NAME")
    private String name;
    @Column(name = "SALESTAGE")
    private String salestage;
    @Column(name = "PROBABILITY")
    private String probability;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "OPPORTUNITYVALUE")
    private Double opportunityvalue;
    @Column(name = "ISCLOSE")
    private Boolean isclose;
    @Column(name = "CLOSEDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date closedate;
    @Column(name = "BUSINESSTYPE")
    private String businesstype;
    @Column(name = "EVALUATIONSTATUS")
    private String evaluationstatus;
    @Column(name = "EVALUETIONDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date evaluetiondate;
    @Column(name = "BUDGETSTATUS")
    private String budgetstatus;
    @Column(name = "BUDGETDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date budgetdate;
    @Column(name = "SALESTAGEDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date salestagedate;
    @Column(name = "OPPORTUNITYSTATUS")
    private String opportunitystatus;
    @Column(name = "OPPORTUNITYSTATUSDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date opportunitystatusdate;
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
    @OneToMany(mappedBy = "companyopportunity")
    private Set<Companyopporunitydocs> companyopporunitydocsSet = new HashSet();
    @JoinColumn(name = "COMPANYID", referencedColumnName = "COMPANYID")
    @ManyToOne
    private Company company;
    @OneToMany(mappedBy = "companyopportunity")
    private Set<Companyopportunitycontact> companyopportunitycontactSet = new HashSet();

    public Companyopportunity() { }

    public Companyopportunity(Integer opportunityid) {
        this.opportunityid = opportunityid;
    }

    public Integer getOpportunityid() {
        return opportunityid;
    }

    public void setOpportunityid(Integer opportunityid) {
        this.opportunityid = opportunityid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalestage() {
        return salestage;
    }

    public void setSalestage(String salestage) {
        this.salestage = salestage;
    }

    public String getProbability() {
        return probability;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }

    public Double getOpportunityvalue() {
        return opportunityvalue;
    }

    public void setOpportunityvalue(Double opportunityvalue) {
        this.opportunityvalue = opportunityvalue;
    }

    public Boolean getIsclose() {
        return isclose;
    }

    public void setIsclose(Boolean isclose) {
        this.isclose = isclose;
    }

    public Date getClosedate() {
        return closedate;
    }

    public void setClosedate(Date closedate) {
        this.closedate = closedate;
    }

    public String getBusinesstype() {
        return businesstype;
    }

    public void setBusinesstype(String businesstype) {
        this.businesstype = businesstype;
    }

    public String getEvaluationstatus() {
        return evaluationstatus;
    }

    public void setEvaluationstatus(String evaluationstatus) {
        this.evaluationstatus = evaluationstatus;
    }

    public Date getEvaluetiondate() {
        return evaluetiondate;
    }

    public void setEvaluetiondate(Date evaluetiondate) {
        this.evaluetiondate = evaluetiondate;
    }

    public String getBudgetstatus() {
        return budgetstatus;
    }

    public void setBudgetstatus(String budgetstatus) {
        this.budgetstatus = budgetstatus;
    }

    public Date getBudgetdate() {
        return budgetdate;
    }

    public void setBudgetdate(Date budgetdate) {
        this.budgetdate = budgetdate;
    }

    public Date getSalestagedate() {
        return salestagedate;
    }

    public void setSalestagedate(Date salestagedate) {
        this.salestagedate = salestagedate;
    }

    public String getOpportunitystatus() {
        return opportunitystatus;
    }

    public void setOpportunitystatus(String opportunitystatus) {
        this.opportunitystatus = opportunitystatus;
    }

    public Date getOpportunitystatusdate() {
        return opportunitystatusdate;
    }

    public void setOpportunitystatusdate(Date opportunitystatusdate) {
        this.opportunitystatusdate = opportunitystatusdate;
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
    public Set<Companyopporunitydocs> getCompanyopporunitydocses() {
        return companyopporunitydocsSet;
    }

    public void setCompanyopporunitydocses(Set<Companyopporunitydocs> companyopporunitydocsSet) {
        this.companyopporunitydocsSet = companyopporunitydocsSet;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @XmlTransient
    public Set<Companyopportunitycontact> getCompanyopportunitycontacts() {
        return companyopportunitycontactSet;
    }

    public void setCompanyopportunitycontacts(Set<Companyopportunitycontact> companyopportunitycontactSet) {
        this.companyopportunitycontactSet = companyopportunitycontactSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (opportunityid != null ? opportunityid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Companyopportunity)) {
            return false;
        }
        Companyopportunity other = (Companyopportunity) object;
        if ((this.opportunityid == null && other.opportunityid != null) || (this.opportunityid != null && !this.opportunityid.equals(other.opportunityid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Companyopportunity[ opportunityid=" + opportunityid + " ]";
    }
    
}
