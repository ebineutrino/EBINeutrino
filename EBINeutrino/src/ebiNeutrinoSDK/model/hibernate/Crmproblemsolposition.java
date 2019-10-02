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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "CRMPROBLEMSOLPOSITION", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Crmproblemsolposition.findAll", query = "SELECT c FROM Crmproblemsolposition c"),
    @NamedQuery(name = "Crmproblemsolposition.findByPositionid", query = "SELECT c FROM Crmproblemsolposition c WHERE c.positionid = :positionid"),
    @NamedQuery(name = "Crmproblemsolposition.findByProductid", query = "SELECT c FROM Crmproblemsolposition c WHERE c.productid = :productid"),
    @NamedQuery(name = "Crmproblemsolposition.findByCreateddate", query = "SELECT c FROM Crmproblemsolposition c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Crmproblemsolposition.findByCreatedfrom", query = "SELECT c FROM Crmproblemsolposition c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Crmproblemsolposition.findByChangeddate", query = "SELECT c FROM Crmproblemsolposition c WHERE c.changeddate = :changeddate"),
    @NamedQuery(name = "Crmproblemsolposition.findByChangedfrom", query = "SELECT c FROM Crmproblemsolposition c WHERE c.changedfrom = :changedfrom"),
    @NamedQuery(name = "Crmproblemsolposition.findByProductnr", query = "SELECT c FROM Crmproblemsolposition c WHERE c.productnr = :productnr"),
    @NamedQuery(name = "Crmproblemsolposition.findByProductname", query = "SELECT c FROM Crmproblemsolposition c WHERE c.productname = :productname"),
    @NamedQuery(name = "Crmproblemsolposition.findByCategory", query = "SELECT c FROM Crmproblemsolposition c WHERE c.category = :category"),
    @NamedQuery(name = "Crmproblemsolposition.findByType", query = "SELECT c FROM Crmproblemsolposition c WHERE c.type = :type"),
    @NamedQuery(name = "Crmproblemsolposition.findByTaxtype", query = "SELECT c FROM Crmproblemsolposition c WHERE c.taxtype = :taxtype"),
    @NamedQuery(name = "Crmproblemsolposition.findByNetamount", query = "SELECT c FROM Crmproblemsolposition c WHERE c.netamount = :netamount"),
    @NamedQuery(name = "Crmproblemsolposition.findByPretax", query = "SELECT c FROM Crmproblemsolposition c WHERE c.pretax = :pretax"),
    @NamedQuery(name = "Crmproblemsolposition.findByDipendencyid", query = "SELECT c FROM Crmproblemsolposition c WHERE c.dipendencyid = :dipendencyid"),
    @NamedQuery(name = "Crmproblemsolposition.findByDimensionid", query = "SELECT c FROM Crmproblemsolposition c WHERE c.dimensionid = :dimensionid"),
    @NamedQuery(name = "Crmproblemsolposition.findByPicturename", query = "SELECT c FROM Crmproblemsolposition c WHERE c.picturename = :picturename")})
public class Crmproblemsolposition implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "POSITIONID")
    private Integer positionid;
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
    @Column(name = "DIPENDENCYID")
    private Integer dipendencyid;
    @Column(name = "DIMENSIONID")
    private Integer dimensionid;
    @Lob
    @Column(name = "PICTURE")
    private byte[] picture;
    @Column(name = "PICTURENAME")
    private String picturename;
    @Lob
    @Column(name = "DESCRIPTION")
    private String description;
    @JoinColumn(name = "SOLUTIONID", referencedColumnName = "PROSOLID")
    @ManyToOne
    private Crmproblemsolutions crmproblemsolutions;

    public Crmproblemsolposition() {}

    public Crmproblemsolposition(Integer positionid) {
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

    public Integer getDipendencyid() {
        return dipendencyid;
    }

    public void setDipendencyid(Integer dipendencyid) {
        this.dipendencyid = dipendencyid;
    }

    public Integer getDimensionid() {
        return dimensionid;
    }

    public void setDimensionid(Integer dimensionid) {
        this.dimensionid = dimensionid;
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

    public Crmproblemsolutions getCrmproblemsolutions() {
        return crmproblemsolutions;
    }

    public void setCrmproblemsolutions(Crmproblemsolutions crmproblemsolutions) {
        this.crmproblemsolutions = crmproblemsolutions;
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
        if (!(object instanceof Crmproblemsolposition)) {
            return false;
        }
        Crmproblemsolposition other = (Crmproblemsolposition) object;
        if ((this.positionid == null && other.positionid != null) || (this.positionid != null && !this.positionid.equals(other.positionid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Crmproblemsolposition[ positionid=" + positionid + " ]";
    }
    
}
