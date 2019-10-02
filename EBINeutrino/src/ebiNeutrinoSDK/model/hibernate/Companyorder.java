package ebiNeutrinoSDK.model.hibernate;

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
@Table(name = "COMPANYORDER", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Companyorder.findAll", query = "SELECT c FROM Companyorder c"),
    @NamedQuery(name = "Companyorder.findByOrderid", query = "SELECT c FROM Companyorder c WHERE c.orderid = :orderid"),
    @NamedQuery(name = "Companyorder.findByOrdernr", query = "SELECT c FROM Companyorder c WHERE c.ordernr = :ordernr"),
    @NamedQuery(name = "Companyorder.findByOfferid", query = "SELECT c FROM Companyorder c WHERE c.offerid = :offerid"),
    @NamedQuery(name = "Companyorder.findByName", query = "SELECT c FROM Companyorder c WHERE c.name = :name"),
    @NamedQuery(name = "Companyorder.findByInvoicecreated", query = "SELECT c FROM Companyorder c WHERE c.invoicecreated = :invoicecreated"),
    @NamedQuery(name = "Companyorder.findByStatus", query = "SELECT c FROM Companyorder c WHERE c.status = :status"),
    @NamedQuery(name = "Companyorder.findByOrderversion", query = "SELECT c FROM Companyorder c WHERE c.orderversion = :orderversion"),
    @NamedQuery(name = "Companyorder.findByOfferdate", query = "SELECT c FROM Companyorder c WHERE c.offerdate = :offerdate"),
    @NamedQuery(name = "Companyorder.findByValidto", query = "SELECT c FROM Companyorder c WHERE c.validto = :validto"),
    @NamedQuery(name = "Companyorder.findByIsrecieved", query = "SELECT c FROM Companyorder c WHERE c.isrecieved = :isrecieved"),
    @NamedQuery(name = "Companyorder.findByCreatedfrom", query = "SELECT c FROM Companyorder c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Companyorder.findByCreateddate", query = "SELECT c FROM Companyorder c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Companyorder.findByChangedfrom", query = "SELECT c FROM Companyorder c WHERE c.changedfrom = :changedfrom"),
    @NamedQuery(name = "Companyorder.findByChangeddate", query = "SELECT c FROM Companyorder c WHERE c.changeddate = :changeddate")})
public class Companyorder implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ORDERID")
    private Integer orderid;
    @Column(name = "ORDERNR")
    private String ordernr;
    @Column(name = "OFFERID")
    private Integer offerid;
    @Column(name = "NAME")
    private String name;
    @Column(name = "INVOICECREATED")
    private Boolean invoicecreated;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "ORDERVERSION")
    private Integer orderversion;
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
    @OneToMany(mappedBy = "companyorder")
    private Set<Companyorderdocs> companyorderdocsSet = new HashSet();
    @OneToMany(mappedBy = "companyorder")
    private Set<Companyorderpositions> companyorderpositionsSet = new HashSet();
    @OneToMany(mappedBy = "companyorder")
    private Set<Companyorderreceiver> companyorderreceiverSet = new HashSet();
    @JoinColumn(name = "COMPANYID", referencedColumnName = "COMPANYID")
    @ManyToOne
    private Company company;

    public Companyorder() { }

    public Companyorder(Integer orderid) {
        this.orderid = orderid;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public String getOrdernr() {
        return ordernr;
    }

    public void setOrdernr(String ordernr) {
        this.ordernr = ordernr;
    }

    public Integer getOfferid() {
        return offerid;
    }

    public void setOfferid(Integer offerid) {
        this.offerid = offerid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getInvoicecreated() {
        return invoicecreated;
    }

    public void setInvoicecreated(Boolean invoicecreated) {
        this.invoicecreated = invoicecreated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getOrderversion() {
        return orderversion;
    }

    public void setOrderversion(Integer orderversion) {
        this.orderversion = orderversion;
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
    public Set<Companyorderdocs> getCompanyorderdocses() {
        return companyorderdocsSet;
    }

    public void setCompanyorderdocsSet(Set<Companyorderdocs> companyorderdocses) {
        this.companyorderdocsSet = companyorderdocsSet;
    }

    @XmlTransient
    public Set<Companyorderpositions> getCompanyorderpositionses() {
        return companyorderpositionsSet;
    }

    public void setCompanyorderpositionses(Set<Companyorderpositions> companyorderpositionsSet) {
        this.companyorderpositionsSet = companyorderpositionsSet;
    }

    @XmlTransient
    public Set<Companyorderreceiver> getCompanyorderreceivers() {
        return companyorderreceiverSet;
    }

    public void setCompanyorderreceivers(Set<Companyorderreceiver> companyorderreceiverSet) {
        this.companyorderreceiverSet = companyorderreceiverSet;
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
        hash += (orderid != null ? orderid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Companyorder)) {
            return false;
        }
        Companyorder other = (Companyorder) object;
        if ((this.orderid == null && other.orderid != null) || (this.orderid != null && !this.orderid.equals(other.orderid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Companyorder[ orderid=" + orderid + " ]";
    }
    
}
