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
@Table(name = "CRMPRODUCTDOCS", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Crmproductdocs.findAll", query = "SELECT c FROM Crmproductdocs c"),
    @NamedQuery(name = "Crmproductdocs.findByProductdocid", query = "SELECT c FROM Crmproductdocs c WHERE c.productdocid = :productdocid"),
    @NamedQuery(name = "Crmproductdocs.findByName", query = "SELECT c FROM Crmproductdocs c WHERE c.name = :name"),
    @NamedQuery(name = "Crmproductdocs.findByCreatedfrom", query = "SELECT c FROM Crmproductdocs c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Crmproductdocs.findByCreateddate", query = "SELECT c FROM Crmproductdocs c WHERE c.createddate = :createddate")})
public class Crmproductdocs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PRODUCTDOCID")
    private Integer productdocid;
    @Column(name = "NAME")
    private String name;
    @Lob
    @Column(name = "FILES")
    private byte[] files;
    @Column(name = "CREATEDFROM")
    private String createdfrom;
    @Column(name = "CREATEDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createddate;
    @JoinColumn(name = "PRODUCTID", referencedColumnName = "PRODUCTID")
    @ManyToOne
    private Crmproduct crmproduct;

    public Crmproductdocs() {}

    public Crmproductdocs(Integer productdocid) {
        this.productdocid = productdocid;
    }

    public Integer getProductdocid() {
        return productdocid;
    }

    public void setProductdocid(Integer productdocid) {
        this.productdocid = productdocid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getFiles() {
        return files;
    }

    public void setFiles(byte[] files) {
        this.files = files;
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

    public Crmproduct getCrmproduct() {
        return crmproduct;
    }

    public void setCrmproduct(Crmproduct crmproduct) {
        this.crmproduct = crmproduct;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productdocid != null ? productdocid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Crmproductdocs)) {
            return false;
        }
        Crmproductdocs other = (Crmproductdocs) object;
        if ((this.productdocid == null && other.productdocid != null) || (this.productdocid != null && !this.productdocid.equals(other.productdocid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Crmproductdocs[ productdocid=" + productdocid + " ]";
    }
    
}
