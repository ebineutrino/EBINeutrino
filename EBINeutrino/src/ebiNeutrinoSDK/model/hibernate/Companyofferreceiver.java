package ebiNeutrinoSDK.model.hibernate;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "COMPANYOFFERRECEIVER", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Companyofferreceiver.findAll", query = "SELECT c FROM Companyofferreceiver c"),
    @NamedQuery(name = "Companyofferreceiver.findByReceiverid", query = "SELECT c FROM Companyofferreceiver c WHERE c.receiverid = :receiverid"),
    @NamedQuery(name = "Companyofferreceiver.findByCnum", query = "SELECT c FROM Companyofferreceiver c WHERE c.cnum = :cnum"),
    @NamedQuery(name = "Companyofferreceiver.findByReceivervia", query = "SELECT c FROM Companyofferreceiver c WHERE c.receivervia = :receivervia"),
    @NamedQuery(name = "Companyofferreceiver.findByGender", query = "SELECT c FROM Companyofferreceiver c WHERE c.gender = :gender"),
    @NamedQuery(name = "Companyofferreceiver.findBySurname", query = "SELECT c FROM Companyofferreceiver c WHERE c.surname = :surname"),
    @NamedQuery(name = "Companyofferreceiver.findByMittelname", query = "SELECT c FROM Companyofferreceiver c WHERE c.mittelname = :mittelname"),
    @NamedQuery(name = "Companyofferreceiver.findByPosition", query = "SELECT c FROM Companyofferreceiver c WHERE c.position = :position"),
    @NamedQuery(name = "Companyofferreceiver.findByEmail", query = "SELECT c FROM Companyofferreceiver c WHERE c.email = :email"),
    @NamedQuery(name = "Companyofferreceiver.findByPhone", query = "SELECT c FROM Companyofferreceiver c WHERE c.phone = :phone"),
    @NamedQuery(name = "Companyofferreceiver.findByFax", query = "SELECT c FROM Companyofferreceiver c WHERE c.fax = :fax"),
    @NamedQuery(name = "Companyofferreceiver.findByStreet", query = "SELECT c FROM Companyofferreceiver c WHERE c.street = :street"),
    @NamedQuery(name = "Companyofferreceiver.findByZip", query = "SELECT c FROM Companyofferreceiver c WHERE c.zip = :zip"),
    @NamedQuery(name = "Companyofferreceiver.findByLocation", query = "SELECT c FROM Companyofferreceiver c WHERE c.location = :location"),
    @NamedQuery(name = "Companyofferreceiver.findByPbox", query = "SELECT c FROM Companyofferreceiver c WHERE c.pbox = :pbox"),
    @NamedQuery(name = "Companyofferreceiver.findByCountry", query = "SELECT c FROM Companyofferreceiver c WHERE c.country = :country"),
    @NamedQuery(name = "Companyofferreceiver.findByCreatedfrom", query = "SELECT c FROM Companyofferreceiver c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Companyofferreceiver.findByCreateddate", query = "SELECT c FROM Companyofferreceiver c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Companyofferreceiver.findByName", query = "SELECT c FROM Companyofferreceiver c WHERE c.name = :name")})
public class Companyofferreceiver implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "RECEIVERID")
    private Integer receiverid;
    @Column(name = "CNUM")
    private Integer cnum;
    @Column(name = "RECEIVERVIA")
    private String receivervia;
    @Column(name = "GENDER")
    private String gender;
    @Column(name = "SURNAME")
    private String surname;
    @Column(name = "MITTELNAME")
    private String mittelname;
    @Column(name = "POSITION")
    private String position;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "PHONE")
    private String phone;
    @Column(name = "FAX")
    private String fax;
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
    @Column(name = "CREATEDFROM")
    private String createdfrom;
    @Column(name = "CREATEDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createddate;
    @Column(name = "NAME")
    private String name;
    @JoinColumn(name = "OFFERID", referencedColumnName = "OFFERID")
    @ManyToOne
    private Companyoffer companyoffer;

    public Companyofferreceiver(){ }

    public Companyofferreceiver(Integer receiverid) {
        this.receiverid = receiverid;
    }

    public Integer getReceiverid() {
        return receiverid;
    }

    public void setReceiverid(Integer receiverid) {
        this.receiverid = receiverid;
    }

    public Integer getCnum() {
        return cnum;
    }

    public void setCnum(Integer cnum) {
        this.cnum = cnum;
    }

    public String getReceivervia() {
        return receivervia;
    }

    public void setReceivervia(String receivervia) {
        this.receivervia = receivervia;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Companyoffer getCompanyoffer() {
        return companyoffer;
    }

    public void setCompanyoffer(Companyoffer companyoffer) {
        this.companyoffer = companyoffer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (receiverid != null ? receiverid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Companyofferreceiver)) {
            return false;
        }
        Companyofferreceiver other = (Companyofferreceiver) object;
        if ((this.receiverid == null && other.receiverid != null) || (this.receiverid != null && !this.receiverid.equals(other.receiverid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Companyofferreceiver[ receiverid=" + receiverid + " ]";
    }
    
}
