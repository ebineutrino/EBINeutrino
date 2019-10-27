package org.sdk.model.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "COMPANY", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Company.findAll", query = "SELECT c FROM Company c"),
    @NamedQuery(name = "Company.findByCompanyid", query = "SELECT c FROM Company c WHERE c.companyid = :companyid"),
    @NamedQuery(name = "Company.findByCompanynumber", query = "SELECT c FROM Company c WHERE c.companynumber = :companynumber"),
    @NamedQuery(name = "Company.findByCustomernr", query = "SELECT c FROM Company c WHERE c.customernr = :customernr"),
    @NamedQuery(name = "Company.findByBeginchar", query = "SELECT c FROM Company c WHERE c.beginchar = :beginchar"),
    @NamedQuery(name = "Company.findByName", query = "SELECT c FROM Company c WHERE c.name = :name"),
    @NamedQuery(name = "Company.findByName2", query = "SELECT c FROM Company c WHERE c.name2 = :name2"),
    @NamedQuery(name = "Company.findByPhone", query = "SELECT c FROM Company c WHERE c.phone = :phone"),
    @NamedQuery(name = "Company.findByFax", query = "SELECT c FROM Company c WHERE c.fax = :fax"),
    @NamedQuery(name = "Company.findByEmail", query = "SELECT c FROM Company c WHERE c.email = :email"),
    @NamedQuery(name = "Company.findByEmployee", query = "SELECT c FROM Company c WHERE c.employee = :employee"),
    @NamedQuery(name = "Company.findByQualification", query = "SELECT c FROM Company c WHERE c.qualification = :qualification"),
    @NamedQuery(name = "Company.findByCategory", query = "SELECT c FROM Company c WHERE c.category = :category"),
    @NamedQuery(name = "Company.findByCooperation", query = "SELECT c FROM Company c WHERE c.cooperation = :cooperation"),
    @NamedQuery(name = "Company.findByIslock", query = "SELECT c FROM Company c WHERE c.islock = :islock"),
    @NamedQuery(name = "Company.findByWeb", query = "SELECT c FROM Company c WHERE c.web = :web"),
    @NamedQuery(name = "Company.findByTaxnumber", query = "SELECT c FROM Company c WHERE c.taxnumber = :taxnumber"),
    @NamedQuery(name = "Company.findByCreatedfrom", query = "SELECT c FROM Company c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Company.findByCreateddate", query = "SELECT c FROM Company c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Company.findByChangedfrom", query = "SELECT c FROM Company c WHERE c.changedfrom = :changedfrom"),
    @NamedQuery(name = "Company.findByChangeddate", query = "SELECT c FROM Company c WHERE c.changeddate = :changeddate"),
    @NamedQuery(name = "Company.findByIsactual", query = "SELECT c FROM Company c WHERE c.isactual = :isactual")})
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "COMPANYID")
    private Integer companyid;
    @Column(name = "COMPANYNUMBER")
    private Integer companynumber;
    @Column(name = "CUSTOMERNR")
    private String customernr;
    @Column(name = "BEGINCHAR")
    private String beginchar;
    @Column(name = "NAME")
    private String name;
    @Column(name = "NAME2")
    private String name2;
    @Column(name = "PHONE")
    private String phone;
    @Column(name = "FAX")
    private String fax;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "EMPLOYEE")
    private String employee;
    @Column(name = "QUALIFICATION")
    private String qualification;
    @Column(name = "CATEGORY")
    private String category;
    @Column(name = "COOPERATION")
    private String cooperation;
    @Column(name = "ISLOCK")
    private Boolean islock;
    @Column(name = "WEB")
    private String web;
    @Column(name = "TAXNUMBER")
    private String taxnumber;
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
    @Column(name = "ISACTUAL")
    private Boolean isactual;
    @OneToMany(mappedBy = "company")
    private Set<Companybank> companybankSet = new HashSet();
    @OneToMany(mappedBy = "company")
    private Set<Companyaddress> companyaddressSet = new HashSet();
    @OneToMany(mappedBy = "company")
    private Set<Companyopportunity> companyopportunitySet = new HashSet();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "company")
    private Set<Companycontacts> companycontactsSet = new HashSet();
    @OneToMany(mappedBy = "company")
    private Set<Companyhirarchie> companyhirarchieSet = new HashSet();
    @OneToMany(mappedBy = "company")
    private Set<Companyservice> companyserviceSet = new HashSet();
    @OneToMany(mappedBy = "company")
    private Set<Companyactivities> companyactivitiesSet = new HashSet();
    @OneToMany(mappedBy = "company")
    private Set<Companyoffer> companyofferSet = new HashSet();
    @OneToMany(mappedBy = "company")
    private Set<Companyorder> companyorderSet = new HashSet();
    @OneToMany(mappedBy = "company")
    private Set<MailAssigned> mailAssignedSet = new HashSet();
    @OneToMany(mappedBy = "company")
    private Set<Companymeetingprotocol> companymeetingprotocolSet = new HashSet();

    public Company() {}

    public Company(Integer companyid) {
        this.companyid = companyid;
    }

    public Integer getCompanyid() {
        return companyid;
    }

    public void setCompanyid(Integer companyid) {
        this.companyid = companyid;
    }

    public Integer getCompanynumber() {
        return companynumber;
    }

    public void setCompanynumber(Integer companynumber) {
        this.companynumber = companynumber;
    }

    public String getCustomernr() {
        return customernr;
    }

    public void setCustomernr(String customernr) {
        this.customernr = customernr;
    }

    public String getBeginchar() {
        return beginchar;
    }

    public void setBeginchar(String beginchar) {
        this.beginchar = beginchar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCooperation() {
        return cooperation;
    }

    public void setCooperation(String cooperation) {
        this.cooperation = cooperation;
    }

    public Boolean getIslock() {
        return islock;
    }

    public void setIslock(Boolean islock) {
        this.islock = islock;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getTaxnumber() {
        return taxnumber;
    }

    public void setTaxnumber(String taxnumber) {
        this.taxnumber = taxnumber;
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

    public Boolean getIsactual() {
        return isactual;
    }

    public void setIsactual(Boolean isactual) {
        this.isactual = isactual;
    }

    @XmlTransient
    public Set<Companybank> getCompanybanks() {
        return companybankSet;
    }

    public void setCompanybanks(Set<Companybank> companybankSet) {
        this.companybankSet = companybankSet;
    }

    @XmlTransient
    public Set<Companyaddress> getCompanyaddresses() {
        return companyaddressSet;
    }

    public void setCompanyaddresses(Set<Companyaddress> companyaddressSet) {
        this.companyaddressSet = companyaddressSet;
    }

    @XmlTransient
    public Set<Companyopportunity> getCompanyopportunities() {
        return companyopportunitySet;
    }

    public void getCompanyopportunities(Set<Companyopportunity> companyopportunitySet) {
        this.companyopportunitySet = companyopportunitySet;
    }

    @XmlTransient
    public Set<Companycontacts> getCompanycontactses() {
        return companycontactsSet;
    }

    public void setCompanycontactses(Set<Companycontacts> companycontactsSet) {
        this.companycontactsSet = companycontactsSet;
    }

    @XmlTransient
    public Set<Companyhirarchie> getCompanyhirarchies() {
        return companyhirarchieSet;
    }

    public void setCompanyhirarchies(Set<Companyhirarchie> companyhirarchieSet) {
        this.companyhirarchieSet = companyhirarchieSet;
    }

    @XmlTransient
    public Set<Companyservice> getCompanyservices() {
        return companyserviceSet;
    }

    public void setCompanyservices(Set<Companyservice> companyserviceSet) {
        this.companyserviceSet = companyserviceSet;
    }

    @XmlTransient
    public Set<Companyactivities> getCompanyactivitieses() {
        return companyactivitiesSet;
    }

    public void setCompanyactivitieses(Set<Companyactivities> companyactivitiesSet) {
        this.companyactivitiesSet = companyactivitiesSet;
    }

    @XmlTransient
    public Set<Companyoffer> getCompanyoffers() {
        return companyofferSet;
    }

    public void setCompanyoffers(Set<Companyoffer> companyofferSet) {
        this.companyofferSet = companyofferSet;
    }

    @XmlTransient
    public Set<Companyorder> getCompanyorders() {
        return companyorderSet;
    }

    public void setCompanyorders(Set<Companyorder> companyorderSet) {
        this.companyorderSet = companyorderSet;
    }

    @XmlTransient
    public Set<MailAssigned> getMailAssignedses() {
        return mailAssignedSet;
    }

    public void setMailAssignedses(Set<MailAssigned> mailAssignedSet) {
        this.mailAssignedSet = mailAssignedSet;
    }

    @XmlTransient
    public Set<Companymeetingprotocol> getCompanymeetingprotocols() {
        return companymeetingprotocolSet;
    }

    public void setCompanymeetingprotocols(Set<Companymeetingprotocol> companymeetingprotocolSet) {
        this.companymeetingprotocolSet = companymeetingprotocolSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (companyid != null ? companyid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Company)) {
            return false;
        }
        Company other = (Company) object;
        if ((this.companyid == null && other.companyid != null) || (this.companyid != null && !this.companyid.equals(other.companyid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Company[ companyid=" + companyid + " ]";
    }
}
