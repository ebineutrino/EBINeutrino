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
@Table(name = "COMPANYCONTACTS", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Companycontacts.findAll", query = "SELECT c FROM Companycontacts c"),
    @NamedQuery(name = "Companycontacts.findByContactid", query = "SELECT c FROM Companycontacts c WHERE c.contactid = :contactid"),
    @NamedQuery(name = "Companycontacts.findByMaincontact", query = "SELECT c FROM Companycontacts c WHERE c.maincontact = :maincontact"),
    @NamedQuery(name = "Companycontacts.findByGender", query = "SELECT c FROM Companycontacts c WHERE c.gender = :gender"),
    @NamedQuery(name = "Companycontacts.findByTitle", query = "SELECT c FROM Companycontacts c WHERE c.title = :title"),
    @NamedQuery(name = "Companycontacts.findBySurname", query = "SELECT c FROM Companycontacts c WHERE c.surname = :surname"),
    @NamedQuery(name = "Companycontacts.findByName", query = "SELECT c FROM Companycontacts c WHERE c.name = :name"),
    @NamedQuery(name = "Companycontacts.findByMittelname", query = "SELECT c FROM Companycontacts c WHERE c.mittelname = :mittelname"),
    @NamedQuery(name = "Companycontacts.findByPosition", query = "SELECT c FROM Companycontacts c WHERE c.position = :position"),
    @NamedQuery(name = "Companycontacts.findByBirddate", query = "SELECT c FROM Companycontacts c WHERE c.birddate = :birddate"),
    @NamedQuery(name = "Companycontacts.findByPhone", query = "SELECT c FROM Companycontacts c WHERE c.phone = :phone"),
    @NamedQuery(name = "Companycontacts.findByFax", query = "SELECT c FROM Companycontacts c WHERE c.fax = :fax"),
    @NamedQuery(name = "Companycontacts.findByMobile", query = "SELECT c FROM Companycontacts c WHERE c.mobile = :mobile"),
    @NamedQuery(name = "Companycontacts.findByEmail", query = "SELECT c FROM Companycontacts c WHERE c.email = :email"),
    @NamedQuery(name = "Companycontacts.findByCreatedfrom", query = "SELECT c FROM Companycontacts c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Companycontacts.findByCreateddate", query = "SELECT c FROM Companycontacts c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Companycontacts.findByChangedfrom", query = "SELECT c FROM Companycontacts c WHERE c.changedfrom = :changedfrom"),
    @NamedQuery(name = "Companycontacts.findByChangeddate", query = "SELECT c FROM Companycontacts c WHERE c.changeddate = :changeddate")})
public class Companycontacts implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CONTACTID")
    private Integer contactid;
    @Column(name = "MAINCONTACT")
    private Boolean maincontact;
    @Column(name = "GENDER")
    private String gender;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "SURNAME")
    private String surname;
    @Column(name = "NAME")
    private String name;
    @Column(name = "MITTELNAME")
    private String mittelname;
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
    @OneToMany(mappedBy = "companycontacts")
    private Set<Companycontactaddress> companycontactaddressSet  = new HashSet();
    @JoinColumn(name = "COMPANYID", referencedColumnName = "COMPANYID")
    @ManyToOne(optional = false)
    private Company company;

    public Companycontacts() { }

    public Companycontacts(Integer contactid) {
        this.contactid = contactid;
    }

    public Integer getContactid() {
        return contactid;
    }

    public void setContactid(Integer contactid) {
        this.contactid = contactid;
    }

    public Boolean getMaincontact() {
        return maincontact;
    }

    public void setMaincontact(Boolean maincontact) {
        this.maincontact = maincontact;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    @XmlTransient
    public Set<Companycontactaddress> getCompanycontactaddresses() {
        return companycontactaddressSet;
    }

    public void setCompanycontactaddresses(Set<Companycontactaddress> companycontactaddressSet) {
        this.companycontactaddressSet = companycontactaddressSet;
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
        hash += (contactid != null ? contactid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Companycontacts)) {
            return false;
        }
        Companycontacts other = (Companycontacts) object;
        if ((this.contactid == null && other.contactid != null) || (this.contactid != null && !this.contactid.equals(other.contactid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Companycontacts[ contactid=" + contactid + " ]";
    }
    
}
