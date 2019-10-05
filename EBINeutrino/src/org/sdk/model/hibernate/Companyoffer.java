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
@Table(name = "COMPANYOFFER", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Companyoffer.findAll", query = "SELECT c FROM Companyoffer c"),
    @NamedQuery(name = "Companyoffer.findByOfferid", query = "SELECT c FROM Companyoffer c WHERE c.offerid = :offerid"),
    @NamedQuery(name = "Companyoffer.findByOpportunityid", query = "SELECT c FROM Companyoffer c WHERE c.opportunityid = :opportunityid"),
    @NamedQuery(name = "Companyoffer.findByOffernr", query = "SELECT c FROM Companyoffer c WHERE c.offernr = :offernr"),
    @NamedQuery(name = "Companyoffer.findByName", query = "SELECT c FROM Companyoffer c WHERE c.name = :name"),
    @NamedQuery(name = "Companyoffer.findByStatus", query = "SELECT c FROM Companyoffer c WHERE c.status = :status"),
    @NamedQuery(name = "Companyoffer.findByOfferversion", query = "SELECT c FROM Companyoffer c WHERE c.offerversion = :offerversion"),
    @NamedQuery(name = "Companyoffer.findByOfferdate", query = "SELECT c FROM Companyoffer c WHERE c.offerdate = :offerdate"),
    @NamedQuery(name = "Companyoffer.findByValidto", query = "SELECT c FROM Companyoffer c WHERE c.validto = :validto"),
    @NamedQuery(name = "Companyoffer.findByIsrecieved", query = "SELECT c FROM Companyoffer c WHERE c.isrecieved = :isrecieved"),
    @NamedQuery(name = "Companyoffer.findByCreatedfrom", query = "SELECT c FROM Companyoffer c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Companyoffer.findByCreateddate", query = "SELECT c FROM Companyoffer c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Companyoffer.findByChangedfrom", query = "SELECT c FROM Companyoffer c WHERE c.changedfrom = :changedfrom"),
    @NamedQuery(name = "Companyoffer.findByChangeddate", query = "SELECT c FROM Companyoffer c WHERE c.changeddate = :changeddate")})
public class Companyoffer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "OFFERID")
    private Integer offerid;
    @Column(name = "OPPORTUNITYID")
    private Integer opportunityid;
    @Basic(optional = false)
    @Column(name = "OFFERNR")
    private String offernr;
    @Column(name = "NAME")
    private String name;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "OFFERVERSION")
    private Integer offerversion;
    @Column(name = "OFFERDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date offerdate;
    @Column(name = "VALIDTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date validto;
    @Column(name = "ISRECIEVED")
    private Boolean isrecieved;
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
    @OneToMany(mappedBy = "companyoffer")
    private Set<Companyofferdocs> companyofferdocsSet = new HashSet();
    @OneToMany(mappedBy = "companyoffer")
    private Set<Companyofferreceiver> companyofferreceiverSet = new HashSet();
    @OneToMany(mappedBy = "companyoffer")
    private Set<Companyofferpositions> companyofferpositionsSet = new HashSet();
    @JoinColumn(name = "COMPANYID", referencedColumnName = "COMPANYID")
    @ManyToOne
    private Company company;

    public Companyoffer(){}

    public Companyoffer(Integer offerid) {
        this.offerid = offerid;
    }

    public Companyoffer(Integer offerid, String offernr) {
        this.offerid = offerid;
        this.offernr = offernr;
    }

    public Integer getOfferid() {
        return offerid;
    }

    public void setOfferid(Integer offerid) {
        this.offerid = offerid;
    }

    public Integer getOpportunityid() {
        return opportunityid;
    }

    public void setOpportunityid(Integer opportunityid) {
        this.opportunityid = opportunityid;
    }

    public String getOffernr() {
        return offernr;
    }

    public void setOffernr(String offernr) {
        this.offernr = offernr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getOfferversion() {
        return offerversion;
    }

    public void setOfferversion(Integer offerversion) {
        this.offerversion = offerversion;
    }

    public Date getOfferdate() {
        return offerdate;
    }

    public void setOfferdate(Date offerdate) {
        this.offerdate = offerdate;
    }

    public Date getValidto() {
        return validto;
    }

    public void setValidto(Date validto) {
        this.validto = validto;
    }

    public Boolean getIsrecieved() {
        return isrecieved;
    }

    public void setIsrecieved(Boolean isrecieved) {
        this.isrecieved = isrecieved;
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
    public Set<Companyofferdocs> getCompanyofferdocses() {
        return companyofferdocsSet;
    }

    public void setCompanyofferdocses(Set<Companyofferdocs> companyofferdocsSet) {
        this.companyofferdocsSet = companyofferdocsSet;
    }

    @XmlTransient
    public Set<Companyofferreceiver> getCompanyofferreceivers() {
        return companyofferreceiverSet;
    }

    public void setCompanyofferreceivers(Set<Companyofferreceiver> companyofferreceiverSet) {
        this.companyofferreceiverSet = companyofferreceiverSet;
    }

    @XmlTransient
    public Set<Companyofferpositions> getCompanyofferpositionses() {
        return companyofferpositionsSet;
    }

    public void setCompanyofferpositionses(Set<Companyofferpositions> companyofferpositionsSet) {
        this.companyofferpositionsSet = companyofferpositionsSet;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (offerid != null ? offerid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Companyoffer)) {
            return false;
        }
        Companyoffer other = (Companyoffer) object;
        if ((this.offerid == null && other.offerid != null) || (this.offerid != null && !this.offerid.equals(other.offerid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Companyoffer[ offerid=" + offerid + " ]";
    }
    
}
