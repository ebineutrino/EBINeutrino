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
@Table(name = "COMPANYMEETINGCONTACTS", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Companymeetingcontacts.findAll", query = "SELECT c FROM Companymeetingcontacts c"),
    @NamedQuery(name = "Companymeetingcontacts.findByMeetingcontactid", query = "SELECT c FROM Companymeetingcontacts c WHERE c.meetingcontactid = :meetingcontactid"),
    @NamedQuery(name = "Companymeetingcontacts.findByPos", query = "SELECT c FROM Companymeetingcontacts c WHERE c.pos = :pos"),
    @NamedQuery(name = "Companymeetingcontacts.findByGender", query = "SELECT c FROM Companymeetingcontacts c WHERE c.gender = :gender"),
    @NamedQuery(name = "Companymeetingcontacts.findBySurname", query = "SELECT c FROM Companymeetingcontacts c WHERE c.surname = :surname"),
    @NamedQuery(name = "Companymeetingcontacts.findByName", query = "SELECT c FROM Companymeetingcontacts c WHERE c.name = :name"),
    @NamedQuery(name = "Companymeetingcontacts.findByMittelname", query = "SELECT c FROM Companymeetingcontacts c WHERE c.mittelname = :mittelname"),
    @NamedQuery(name = "Companymeetingcontacts.findByPosition", query = "SELECT c FROM Companymeetingcontacts c WHERE c.position = :position"),
    @NamedQuery(name = "Companymeetingcontacts.findByBirddate", query = "SELECT c FROM Companymeetingcontacts c WHERE c.birddate = :birddate"),
    @NamedQuery(name = "Companymeetingcontacts.findByPhone", query = "SELECT c FROM Companymeetingcontacts c WHERE c.phone = :phone"),
    @NamedQuery(name = "Companymeetingcontacts.findByFax", query = "SELECT c FROM Companymeetingcontacts c WHERE c.fax = :fax"),
    @NamedQuery(name = "Companymeetingcontacts.findByMobile", query = "SELECT c FROM Companymeetingcontacts c WHERE c.mobile = :mobile"),
    @NamedQuery(name = "Companymeetingcontacts.findByEmail", query = "SELECT c FROM Companymeetingcontacts c WHERE c.email = :email"),
    @NamedQuery(name = "Companymeetingcontacts.findByStreet", query = "SELECT c FROM Companymeetingcontacts c WHERE c.street = :street"),
    @NamedQuery(name = "Companymeetingcontacts.findByLocation", query = "SELECT c FROM Companymeetingcontacts c WHERE c.location = :location"),
    @NamedQuery(name = "Companymeetingcontacts.findByZip", query = "SELECT c FROM Companymeetingcontacts c WHERE c.zip = :zip"),
    @NamedQuery(name = "Companymeetingcontacts.findByCountry", query = "SELECT c FROM Companymeetingcontacts c WHERE c.country = :country"),
    @NamedQuery(name = "Companymeetingcontacts.findByPbox", query = "SELECT c FROM Companymeetingcontacts c WHERE c.pbox = :pbox"),
    @NamedQuery(name = "Companymeetingcontacts.findByCreatedfrom", query = "SELECT c FROM Companymeetingcontacts c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Companymeetingcontacts.findByCreateddate", query = "SELECT c FROM Companymeetingcontacts c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Companymeetingcontacts.findByChangedfrom", query = "SELECT c FROM Companymeetingcontacts c WHERE c.changedfrom = :changedfrom"),
    @NamedQuery(name = "Companymeetingcontacts.findByChangeddate", query = "SELECT c FROM Companymeetingcontacts c WHERE c.changeddate = :changeddate")})
public class Companymeetingcontacts implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "MEETINGCONTACTID")
    private Integer meetingcontactid;
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
    @Column(name = "STREET")
    private String street;
    @Column(name = "LOCATION")
    private String location;
    @Column(name = "ZIP")
    private String zip;
    @Column(name = "COUNTRY")
    private String country;
    @Column(name = "PBOX")
    private String pbox;
    @Lob
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "CREATEDFROM")
    private String createdfrom;
    @Basic(optional = false)
    @Column(name = "CREATEDDATE")
    @Temporal(TemporalType.DATE)
    private Date createddate;
    @Column(name = "CHANGEDFROM")
    private String changedfrom;
    @Column(name = "CHANGEDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date changeddate;
    @JoinColumn(name = "MEETINGID", referencedColumnName = "MEETINGPROTOCOLID")
    @ManyToOne
    private Companymeetingprotocol companymeetingprotocol;

    public Companymeetingcontacts() { }

    public Companymeetingcontacts(Integer meetingcontactid) {
        this.meetingcontactid = meetingcontactid;
    }

    public Companymeetingcontacts(Integer meetingcontactid, Date createddate) {
        this.meetingcontactid = meetingcontactid;
        this.createddate = createddate;
    }

    public Integer getMeetingcontactid() {
        return meetingcontactid;
    }

    public void setMeetingcontactid(Integer meetingcontactid) {
        this.meetingcontactid = meetingcontactid;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPbox() {
        return pbox;
    }

    public void setPbox(String pbox) {
        this.pbox = pbox;
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

    public Companymeetingprotocol getCompanymeetingprotocol() {
        return companymeetingprotocol;
    }

    public void setCompanymeetingprotocol(Companymeetingprotocol companymeetingprotocol) {
        this.companymeetingprotocol = companymeetingprotocol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (meetingcontactid != null ? meetingcontactid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Companymeetingcontacts)) {
            return false;
        }
        Companymeetingcontacts other = (Companymeetingcontacts) object;
        if ((this.meetingcontactid == null && other.meetingcontactid != null) || (this.meetingcontactid != null && !this.meetingcontactid.equals(other.meetingcontactid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Companymeetingcontacts[ meetingcontactid=" + meetingcontactid + " ]";
    }
    
}
