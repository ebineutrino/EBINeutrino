package org.sdk.model.hibernate;

import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "COMPANYORDERPOSITIONS", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Companyorderpositions.findAll", query = "SELECT c FROM Companyorderpositions c"),
    @NamedQuery(name = "Companyorderpositions.findByPositionid", query = "SELECT c FROM Companyorderpositions c WHERE c.positionid = :positionid"),
    @NamedQuery(name = "Companyorderpositions.findByProductid", query = "SELECT c FROM Companyorderpositions c WHERE c.productid = :productid"),
    @NamedQuery(name = "Companyorderpositions.findByDeduction", query = "SELECT c FROM Companyorderpositions c WHERE c.deduction = :deduction"),
    @NamedQuery(name = "Companyorderpositions.findByProductnr", query = "SELECT c FROM Companyorderpositions c WHERE c.productnr = :productnr"),
    @NamedQuery(name = "Companyorderpositions.findByProductname", query = "SELECT c FROM Companyorderpositions c WHERE c.productname = :productname"),
    @NamedQuery(name = "Companyorderpositions.findByQuantity", query = "SELECT c FROM Companyorderpositions c WHERE c.quantity = :quantity"),
    @NamedQuery(name = "Companyorderpositions.findByNetamount", query = "SELECT c FROM Companyorderpositions c WHERE c.netamount = :netamount"),
    @NamedQuery(name = "Companyorderpositions.findByPretax", query = "SELECT c FROM Companyorderpositions c WHERE c.pretax = :pretax"),
    @NamedQuery(name = "Companyorderpositions.findByTaxtype", query = "SELECT c FROM Companyorderpositions c WHERE c.taxtype = :taxtype"),
    @NamedQuery(name = "Companyorderpositions.findByType", query = "SELECT c FROM Companyorderpositions c WHERE c.type = :type"),
    @NamedQuery(name = "Companyorderpositions.findByCategory", query = "SELECT c FROM Companyorderpositions c WHERE c.category = :category"),
    @NamedQuery(name = "Companyorderpositions.findByCreatedfrom", query = "SELECT c FROM Companyorderpositions c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Companyorderpositions.findByCreateddate", query = "SELECT c FROM Companyorderpositions c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Companyorderpositions.findByChangeddate", query = "SELECT c FROM Companyorderpositions c WHERE c.changeddate = :changeddate"),
    @NamedQuery(name = "Companyorderpositions.findByChangedfrom", query = "SELECT c FROM Companyorderpositions c WHERE c.changedfrom = :changedfrom")})
public class Companyorderpositions implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "POSITIONID")
    private Integer positionid;
    @Column(name = "PRODUCTID")
    private Integer productid;
    @Column(name = "DEDUCTION")
    private String deduction;
    @Column(name = "PRODUCTNR")
    private String productnr;
    @Column(name = "PRODUCTNAME")
    private String productname;
    @Column(name = "QUANTITY")
    private BigInteger quantity;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "NETAMOUNT")
    private Double netamount;
    @Column(name = "PRETAX")
    private Double pretax;
    @Column(name = "TAXTYPE")
    private String taxtype;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "CATEGORY")
    private String category;
    @Lob
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "CREATEDFROM")
    private String createdfrom;
    @Column(name = "CREATEDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createddate;
    @Column(name = "CHANGEDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date changeddate;
    @Column(name = "CHANGEDFROM")
    private String changedfrom;
    @JoinColumn(name = "ORDERID", referencedColumnName = "ORDERID")
    @ManyToOne
    private Companyorder companyorder;

    public Companyorderpositions() { }

    public Companyorderpositions(Integer positionid) {
        this.positionid = positionid;
    }

    public Integer getPositionid() {
        return positionid;
    }

    public void setPositionid(Integer positionid) {
        this.positionid = positionid;
    }

    public Integer getProductid() {
        return productid;
    }

    public void setProductid(Integer productid) {
        this.productid = productid;
    }

    public String getDeduction() {
        return deduction;
    }

    public void setDeduction(String deduction) {
        this.deduction = deduction;
    }

    public String getProductnr() {
        return productnr;
    }

    public void setProductnr(String productnr) {
        this.productnr = productnr;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public BigInteger getQuantity() {
        return quantity;
    }

    public void setQuantity(BigInteger quantity) {
        this.quantity = quantity;
    }

    public Double getNetamount() {
        return netamount;
    }

    public void setNetamount(Double netamount) {
        this.netamount = netamount;
    }

    public Double getPretax() {
        return pretax;
    }

    public void setPretax(Double pretax) {
        this.pretax = pretax;
    }

    public String getTaxtype() {
        return taxtype;
    }

    public void setTaxtype(String taxtype) {
        this.taxtype = taxtype;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public Companyorder getCompanyorder() {
        return companyorder;
    }

    public void setCompanyorder(Companyorder companyorder) {
        this.companyorder = companyorder;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (positionid != null ? positionid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Companyorderpositions)) {
            return false;
        }
        Companyorderpositions other = (Companyorderpositions) object;
        if ((this.positionid == null && other.positionid != null) || (this.positionid != null && !this.positionid.equals(other.positionid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Companyorderpositions[ positionid=" + positionid + " ]";
    }
    
}
