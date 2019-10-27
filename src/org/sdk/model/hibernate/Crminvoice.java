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
@Table(name = "CRMINVOICE", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Crminvoice.findAll", query = "SELECT c FROM Crminvoice c"),
    @NamedQuery(name = "Crminvoice.findByInvoiceid", query = "SELECT c FROM Crminvoice c WHERE c.invoiceid = :invoiceid"),
    @NamedQuery(name = "Crminvoice.findByAssosiation", query = "SELECT c FROM Crminvoice c WHERE c.assosiation = :assosiation"),
    @NamedQuery(name = "Crminvoice.findByInvoicenr", query = "SELECT c FROM Crminvoice c WHERE c.invoicenr = :invoicenr"),
    @NamedQuery(name = "Crminvoice.findByBeginchar", query = "SELECT c FROM Crminvoice c WHERE c.beginchar = :beginchar"),
    @NamedQuery(name = "Crminvoice.findByName", query = "SELECT c FROM Crminvoice c WHERE c.name = :name"),
    @NamedQuery(name = "Crminvoice.findByStatus", query = "SELECT c FROM Crminvoice c WHERE c.status = :status"),
    @NamedQuery(name = "Crminvoice.findByCategory", query = "SELECT c FROM Crminvoice c WHERE c.category = :category"),
    @NamedQuery(name = "Crminvoice.findByDate", query = "SELECT c FROM Crminvoice c WHERE c.date = :date"),
    @NamedQuery(name = "Crminvoice.findByTaxtype", query = "SELECT c FROM Crminvoice c WHERE c.taxtype = :taxtype"),
    @NamedQuery(name = "Crminvoice.findByGender", query = "SELECT c FROM Crminvoice c WHERE c.gender = :gender"),
    @NamedQuery(name = "Crminvoice.findByPosition", query = "SELECT c FROM Crminvoice c WHERE c.position = :position"),
    @NamedQuery(name = "Crminvoice.findByCompanyname", query = "SELECT c FROM Crminvoice c WHERE c.companyname = :companyname"),
    @NamedQuery(name = "Crminvoice.findByContactname", query = "SELECT c FROM Crminvoice c WHERE c.contactname = :contactname"),
    @NamedQuery(name = "Crminvoice.findByContactsurname", query = "SELECT c FROM Crminvoice c WHERE c.contactsurname = :contactsurname"),
    @NamedQuery(name = "Crminvoice.findByContactstreet", query = "SELECT c FROM Crminvoice c WHERE c.contactstreet = :contactstreet"),
    @NamedQuery(name = "Crminvoice.findByContactzip", query = "SELECT c FROM Crminvoice c WHERE c.contactzip = :contactzip"),
    @NamedQuery(name = "Crminvoice.findByContactlocation", query = "SELECT c FROM Crminvoice c WHERE c.contactlocation = :contactlocation"),
    @NamedQuery(name = "Crminvoice.findByContactpostcode", query = "SELECT c FROM Crminvoice c WHERE c.contactpostcode = :contactpostcode"),
    @NamedQuery(name = "Crminvoice.findByContactcountry", query = "SELECT c FROM Crminvoice c WHERE c.contactcountry = :contactcountry"),
    @NamedQuery(name = "Crminvoice.findByContacttelephone", query = "SELECT c FROM Crminvoice c WHERE c.contacttelephone = :contacttelephone"),
    @NamedQuery(name = "Crminvoice.findByContactfax", query = "SELECT c FROM Crminvoice c WHERE c.contactfax = :contactfax"),
    @NamedQuery(name = "Crminvoice.findByContactemail", query = "SELECT c FROM Crminvoice c WHERE c.contactemail = :contactemail"),
    @NamedQuery(name = "Crminvoice.findByContactweb", query = "SELECT c FROM Crminvoice c WHERE c.contactweb = :contactweb"),
    @NamedQuery(name = "Crminvoice.findByCreateddate", query = "SELECT c FROM Crminvoice c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Crminvoice.findByCreatedfrom", query = "SELECT c FROM Crminvoice c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Crminvoice.findByChangeddate", query = "SELECT c FROM Crminvoice c WHERE c.changeddate = :changeddate"),
    @NamedQuery(name = "Crminvoice.findByChangedfrom", query = "SELECT c FROM Crminvoice c WHERE c.changedfrom = :changedfrom")})
public class Crminvoice implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "INVOICEID")
    private Integer invoiceid;
    @Column(name = "ASSOSIATION")
    private String assosiation;
    @Column(name = "INVOICENR")
    private Integer invoicenr;
    @Column(name = "BEGINCHAR")
    private String beginchar;
    @Column(name = "NAME")
    private String name;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CATEGORY")
    private String category;
    @Column(name = "DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "TAXTYPE")
    private String taxtype;
    @Column(name = "GENDER")
    private String gender;
    @Column(name = "POSITION")
    private String position;
    @Column(name = "COMPANYNAME")
    private String companyname;
    @Column(name = "CONTACTNAME")
    private String contactname;
    @Column(name = "CONTACTSURNAME")
    private String contactsurname;
    @Column(name = "CONTACTSTREET")
    private String contactstreet;
    @Column(name = "CONTACTZIP")
    private String contactzip;
    @Column(name = "CONTACTLOCATION")
    private String contactlocation;
    @Column(name = "CONTACTPOSTCODE")
    private String contactpostcode;
    @Column(name = "CONTACTCOUNTRY")
    private String contactcountry;
    @Column(name = "CONTACTTELEPHONE")
    private String contacttelephone;
    @Column(name = "CONTACTFAX")
    private String contactfax;
    @Column(name = "CONTACTEMAIL")
    private String contactemail;
    @Column(name = "CONTACTWEB")
    private String contactweb;
    @Lob
    @Column(name = "CONTACTDESCRIPTION")
    private String contactdescription;
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
    @OneToMany(mappedBy = "crminvoice")
    private Set<Crminvoiceposition> crminvoicepositionSet = new HashSet();

    public Crminvoice() { }

    public Crminvoice(Integer invoiceid) {
        this.invoiceid = invoiceid;
    }

    public Integer getInvoiceid() {
        return invoiceid;
    }

    public void setInvoiceid(Integer invoiceid) {
        this.invoiceid = invoiceid;
    }

    public String getAssosiation() {
        return assosiation;
    }

    public void setAssosiation(String assosiation) {
        this.assosiation = assosiation;
    }

    public Integer getInvoicenr() {
        return invoicenr;
    }

    public void setInvoicenr(Integer invoicenr) {
        this.invoicenr = invoicenr;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTaxtype() {
        return taxtype;
    }

    public void setTaxtype(String taxtype) {
        this.taxtype = taxtype;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getContactname() {
        return contactname;
    }

    public void setContactname(String contactname) {
        this.contactname = contactname;
    }

    public String getContactsurname() {
        return contactsurname;
    }

    public void setContactsurname(String contactsurname) {
        this.contactsurname = contactsurname;
    }

    public String getContactstreet() {
        return contactstreet;
    }

    public void setContactstreet(String contactstreet) {
        this.contactstreet = contactstreet;
    }

    public String getContactzip() {
        return contactzip;
    }

    public void setContactzip(String contactzip) {
        this.contactzip = contactzip;
    }

    public String getContactlocation() {
        return contactlocation;
    }

    public void setContactlocation(String contactlocation) {
        this.contactlocation = contactlocation;
    }

    public String getContactpostcode() {
        return contactpostcode;
    }

    public void setContactpostcode(String contactpostcode) {
        this.contactpostcode = contactpostcode;
    }

    public String getContactcountry() {
        return contactcountry;
    }

    public void setContactcountry(String contactcountry) {
        this.contactcountry = contactcountry;
    }

    public String getContacttelephone() {
        return contacttelephone;
    }

    public void setContacttelephone(String contacttelephone) {
        this.contacttelephone = contacttelephone;
    }

    public String getContactfax() {
        return contactfax;
    }

    public void setContactfax(String contactfax) {
        this.contactfax = contactfax;
    }

    public String getContactemail() {
        return contactemail;
    }

    public void setContactemail(String contactemail) {
        this.contactemail = contactemail;
    }

    public String getContactweb() {
        return contactweb;
    }

    public void setContactweb(String contactweb) {
        this.contactweb = contactweb;
    }

    public String getContactdescription() {
        return contactdescription;
    }

    public void setContactdescription(String contactdescription) {
        this.contactdescription = contactdescription;
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

    @XmlTransient
    public Set<Crminvoiceposition> getCrminvoicepositions() {
        return crminvoicepositionSet;
    }

    public void setCrminvoicepositions(Set<Crminvoiceposition> crminvoicepositionSet) {
        this.crminvoicepositionSet = crminvoicepositionSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (invoiceid != null ? invoiceid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Crminvoice)) {
            return false;
        }
        Crminvoice other = (Crminvoice) object;
        if ((this.invoiceid == null && other.invoiceid != null) || (this.invoiceid != null && !this.invoiceid.equals(other.invoiceid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Crminvoice[ invoiceid=" + invoiceid + " ]";
    }
    
}
