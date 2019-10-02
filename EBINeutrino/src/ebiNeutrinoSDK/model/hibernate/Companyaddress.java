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
@Table(name = "COMPANYADDRESS", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Companyaddress.findAll", query = "SELECT c FROM Companyaddress c"),
    @NamedQuery(name = "Companyaddress.findByAddressid", query = "SELECT c FROM Companyaddress c WHERE c.addressid = :addressid"),
    @NamedQuery(name = "Companyaddress.findByAddresstype", query = "SELECT c FROM Companyaddress c WHERE c.addresstype = :addresstype"),
    @NamedQuery(name = "Companyaddress.findByStreet", query = "SELECT c FROM Companyaddress c WHERE c.street = :street"),
    @NamedQuery(name = "Companyaddress.findByZip", query = "SELECT c FROM Companyaddress c WHERE c.zip = :zip"),
    @NamedQuery(name = "Companyaddress.findByLocation", query = "SELECT c FROM Companyaddress c WHERE c.location = :location"),
    @NamedQuery(name = "Companyaddress.findByPbox", query = "SELECT c FROM Companyaddress c WHERE c.pbox = :pbox"),
    @NamedQuery(name = "Companyaddress.findByCountry", query = "SELECT c FROM Companyaddress c WHERE c.country = :country"),
    @NamedQuery(name = "Companyaddress.findByCreatedfrom", query = "SELECT c FROM Companyaddress c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Companyaddress.findByCreateddate", query = "SELECT c FROM Companyaddress c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Companyaddress.findByChangedfrom", query = "SELECT c FROM Companyaddress c WHERE c.changedfrom = :changedfrom"),
    @NamedQuery(name = "Companyaddress.findByChangeddate", query = "SELECT c FROM Companyaddress c WHERE c.changeddate = :changeddate")})
public class Companyaddress implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ADDRESSID")
    private Integer addressid;
    @Column(name = "ADDRESSTYPE")
    private String addresstype;
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
    @Column(name = "CHANGEDFROM")
    private String changedfrom;
    @Column(name = "CHANGEDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date changeddate;
    @JoinColumn(name = "COMPANYID", referencedColumnName = "COMPANYID")
    @ManyToOne
    private Company company;

    public Companyaddress() {}

    public Companyaddress(Integer addressid) {
        this.addressid = addressid;
    }

    public Integer getAddressid() {
        return addressid;
    }

    public void setAddressid(Integer addressid) {
        this.addressid = addressid;
    }

    public String getAddresstype() {
        return addresstype;
    }

    public void setAddresstype(String addresstype) {
        this.addresstype = addresstype;
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (addressid != null ? addressid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Companyaddress)) {
            return false;
        }
        Companyaddress other = (Companyaddress) object;
        if ((this.addressid == null && other.addressid != null) || (this.addressid != null && !this.addressid.equals(other.addressid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Companyaddress[ addressid=" + addressid + " ]";
    }
    
}
