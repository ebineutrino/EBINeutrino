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
@Table(name = "COMPANYOPPORTUNITYCONTACT", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Companyopportunitycontact.findAll", query = "SELECT c FROM Companyopportunitycontact c"),
    @NamedQuery(name = "Companyopportunitycontact.findByOpportunitycontactid", query = "SELECT c FROM Companyopportunitycontact c WHERE c.opportunitycontactid = :opportunitycontactid"),
    @NamedQuery(name = "Companyopportunitycontact.findByPos", query = "SELECT c FROM Companyopportunitycontact c WHERE c.pos = :pos"),
    @NamedQuery(name = "Companyopportunitycontact.findByGender", query = "SELECT c FROM Companyopportunitycontact c WHERE c.gender = :gender"),
    @NamedQuery(name = "Companyopportunitycontact.findBySurname", query = "SELECT c FROM Companyopportunitycontact c WHERE c.surname = :surname"),
    @NamedQuery(name = "Companyopportunitycontact.findByName", query = "SELECT c FROM Companyopportunitycontact c WHERE c.name = :name"),
    @NamedQuery(name = "Companyopportunitycontact.findByMittelname", query = "SELECT c FROM Companyopportunitycontact c WHERE c.mittelname = :mittelname"),
    @NamedQuery(name = "Companyopportunitycontact.findByStreet", query = "SELECT c FROM Companyopportunitycontact c WHERE c.street = :street"),
    @NamedQuery(name = "Companyopportunitycontact.findByZip", query = "SELECT c FROM Companyopportunitycontact c WHERE c.zip = :zip"),
    @NamedQuery(name = "Companyopportunitycontact.findByLocation", query = "SELECT c FROM Companyopportunitycontact c WHERE c.location = :location"),
    @NamedQuery(name = "Companyopportunitycontact.findByPbox", query = "SELECT c FROM Companyopportunitycontact c WHERE c.pbox = :pbox"),
    @NamedQuery(name = "Companyopportunitycontact.findByCountry", query = "SELECT c FROM Companyopportunitycontact c WHERE c.country = :country"),
    @NamedQuery(name = "Companyopportunitycontact.findByPosition", query = "SELECT c FROM Companyopportunitycontact c WHERE c.position = :position"),
    @NamedQuery(name = "Companyopportunitycontact.findByBirddate", query = "SELECT c FROM Companyopportunitycontact c WHERE c.birddate = :birddate"),
    @NamedQuery(name = "Companyopportunitycontact.findByPhone", query = "SELECT c FROM Companyopportunitycontact c WHERE c.phone = :phone"),
    @NamedQuery(name = "Companyopportunitycontact.findByFax", query = "SELECT c FROM Companyopportunitycontact c WHERE c.fax = :fax"),
    @NamedQuery(name = "Companyopportunitycontact.findByMobile", query = "SELECT c FROM Companyopportunitycontact c WHERE c.mobile = :mobile"),
    @NamedQuery(name = "Companyopportunitycontact.findByEmail", query = "SELECT c FROM Companyopportunitycontact c WHERE c.email = :email"),
    @NamedQuery(name = "Companyopportunitycontact.findByCreatedfrom", query = "SELECT c FROM Companyopportunitycontact c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Companyopportunitycontact.findByCreateddate", query = "SELECT c FROM Companyopportunitycontact c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Companyopportunitycontact.findByChangedfrom", query = "SELECT c FROM Companyopportunitycontact c WHERE c.changedfrom = :changedfrom"),
    @NamedQuery(name = "Companyopportunitycontact.findByChangeddate", query = "SELECT c FROM Companyopportunitycontact c WHERE c.changeddate = :changeddate")})
public class Companyopportunitycontact implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "OPPORTUNITYCONTACTID")
    private Integer opportunitycontactid;
    @Column(name = "POS")
    private Integer pos;
    @Column(name = "GENDER")
    private String gender;
    @Column(name = "SURNAME")
    private String surname;
    @Column(name = "NAME")
    private String name;
    @Column(name = "MITTELNAME")
    private String mittelname;
    @Column(name = "STREET")
    private String street;
    @Column(name = "ZIP")
    private String zip;
    @Column(name = "LOCATION")
    private String location;
    @Column(name = "PBOX")
    private String pbox;
    @Column(name = "COUNTRY")
    private String country;
    @Column(name = "POSITION")
    private String position;
    @Column(name = "BIRDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date birddate;
    @Column(name = "PHONE")
    private String phone;
    @Column(name = "FAX")
    private String fax;
    @Column(name = "MOBILE")
    private String mobile;
    @Column(name = "EMAIL")
    private String email;
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
    @JoinColumn(name = "OPPORTUNITYID", referencedColumnName = "OPPORTUNITYID")
    @ManyToOne
    private Companyopportunity companyopportunity;

    public Companyopportunitycontact() { }

    public Companyopportunitycontact(Integer opportunitycontactid) {
        this.opportunitycontactid = opportunitycontactid;
    }

    public Integer getOpportunitycontactid() {
        return opportunitycontactid;
    }

    public void setOpportunitycontactid(Integer opportunitycontactid) {
        this.opportunitycontactid = opportunitycontactid;
    }

    public Integer getPos() {
        return pos;
    }

    public void setPos(Integer pos) {
        this.pos = pos;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMittelname() {
        return mittelname;
    }

    public void setMittelname(String mittelname) {
        this.mittelname = mittelname;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPbox() {
        return pbox;
    }

    public void setPbox(String pbox) {
        this.pbox = pbox;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Date getBirddate() {
        return birddate;
    }

    public void setBirddate(Date birddate) {
        this.birddate = birddate;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Companyopportunity getCompanyopportunity() {
        return companyopportunity;
    }

    public void setCompanyopportunity(Companyopportunity companyopportunity) {
        this.companyopportunity = companyopportunity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (opportunitycontactid != null ? opportunitycontactid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Companyopportunitycontact)) {
            return false;
        }
        Companyopportunitycontact other = (Companyopportunitycontact) object;
        if ((this.opportunitycontactid == null && other.opportunitycontactid != null) || (this.opportunitycontactid != null && !this.opportunitycontactid.equals(other.opportunitycontactid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Companyopportunitycontact[ opportunitycontactid=" + opportunitycontactid + " ]";
    }
    
}
