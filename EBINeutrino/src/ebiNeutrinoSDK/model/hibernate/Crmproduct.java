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
@Table(name = "CRMPRODUCT", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Crmproduct.findAll", query = "SELECT c FROM Crmproduct c"),
    @NamedQuery(name = "Crmproduct.findByProductid", query = "SELECT c FROM Crmproduct c WHERE c.productid = :productid"),
    @NamedQuery(name = "Crmproduct.findByCreateddate", query = "SELECT c FROM Crmproduct c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Crmproduct.findByCreatedfrom", query = "SELECT c FROM Crmproduct c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Crmproduct.findByChangeddate", query = "SELECT c FROM Crmproduct c WHERE c.changeddate = :changeddate"),
    @NamedQuery(name = "Crmproduct.findByChangedfrom", query = "SELECT c FROM Crmproduct c WHERE c.changedfrom = :changedfrom"),
    @NamedQuery(name = "Crmproduct.findByProductnr", query = "SELECT c FROM Crmproduct c WHERE c.productnr = :productnr"),
    @NamedQuery(name = "Crmproduct.findByProductname", query = "SELECT c FROM Crmproduct c WHERE c.productname = :productname"),
    @NamedQuery(name = "Crmproduct.findByCategory", query = "SELECT c FROM Crmproduct c WHERE c.category = :category"),
    @NamedQuery(name = "Crmproduct.findByType", query = "SELECT c FROM Crmproduct c WHERE c.type = :type"),
    @NamedQuery(name = "Crmproduct.findByTaxtype", query = "SELECT c FROM Crmproduct c WHERE c.taxtype = :taxtype"),
    @NamedQuery(name = "Crmproduct.findByNetamount", query = "SELECT c FROM Crmproduct c WHERE c.netamount = :netamount"),
    @NamedQuery(name = "Crmproduct.findByPretax", query = "SELECT c FROM Crmproduct c WHERE c.pretax = :pretax"),
    @NamedQuery(name = "Crmproduct.findBySaleprice", query = "SELECT c FROM Crmproduct c WHERE c.saleprice = :saleprice"),
    @NamedQuery(name = "Crmproduct.findByPicturename", query = "SELECT c FROM Crmproduct c WHERE c.picturename = :picturename")})
public class Crmproduct implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PRODUCTID")
    private Integer productid;
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
    @Column(name = "PRODUCTNR")
    private String productnr;
    @Column(name = "PRODUCTNAME")
    private String productname;
    @Column(name = "CATEGORY")
    private String category;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "TAXTYPE")
    private String taxtype;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "NETAMOUNT")
    private Double netamount;
    @Column(name = "PRETAX")
    private Double pretax;
    @Column(name = "SALEPRICE")
    private Double saleprice;
    @Lob
    @Column(name = "PICTURE")
    private byte[] picture;
    @Column(name = "PICTURENAME")
    private String picturename;
    @Lob
    @Column(name = "DESCRIPTION")
    private String description;
    @OneToMany(mappedBy = "crmproduct")
    private Set<Crmproductdimension> crmproductdimensionSet = new HashSet();
    @OneToMany(mappedBy = "crmproduct")
    private Set<Crmproductdocs> crmproductdocsSet = new HashSet();
    @OneToMany(mappedBy = "crmproduct")
    private Set<Crmproductdependency> crmproductdependencySet = new HashSet();

    public Crmproduct(){}

    public Crmproduct(Integer productid) {
        this.productid = productid;
    }

    public Integer getProductid() {
        return productid;
    }

    public void setProductid(Integer productid) {
        this.productid = productid;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTaxtype() {
        return taxtype;
    }

    public void setTaxtype(String taxtype) {
        this.taxtype = taxtype;
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

    public Double getSaleprice() {
        return saleprice;
    }

    public void setSaleprice(Double saleprice) {
        this.saleprice = saleprice;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPicturename() {
        return picturename;
    }

    public void setPicturename(String picturename) {
        this.picturename = picturename;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public Set<Crmproductdimension> getCrmproductdimensions() {
        return crmproductdimensionSet;
    }

    public void setCrmproductdimensions(Set<Crmproductdimension> crmproductdimensionSet) {
        this.crmproductdimensionSet = crmproductdimensionSet;
    }

    @XmlTransient
    public Set<Crmproductdocs> getCrmproductdocses() {
        return crmproductdocsSet;
    }

    public void setCrmproductdocsSet(Set<Crmproductdocs> crmproductdocsSet) {
        this.crmproductdocsSet = crmproductdocsSet;
    }

    @XmlTransient
    public Set<Crmproductdependency> getCrmproductdependencies() {
        return crmproductdependencySet;
    }

    public void setCrmproductdependencies(Set<Crmproductdependency> crmproductdependencySet) {
        this.crmproductdependencySet = crmproductdependencySet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productid != null ? productid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Crmproduct)) {
            return false;
        }
        Crmproduct other = (Crmproduct) object;
        if ((this.productid == null && other.productid != null) || (this.productid != null && !this.productid.equals(other.productid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Crmproduct[ productid=" + productid + " ]";
    }
    
}
