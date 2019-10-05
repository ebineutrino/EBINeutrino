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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "CRMPRODUCTDEPENDENCY", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Crmproductdependency.findAll", query = "SELECT c FROM Crmproductdependency c"),
    @NamedQuery(name = "Crmproductdependency.findByDependencyid", query = "SELECT c FROM Crmproductdependency c WHERE c.dependencyid = :dependencyid"),
    @NamedQuery(name = "Crmproductdependency.findByProductidid", query = "SELECT c FROM Crmproductdependency c WHERE c.productidid = :productidid"),
    @NamedQuery(name = "Crmproductdependency.findByCreateddate", query = "SELECT c FROM Crmproductdependency c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Crmproductdependency.findByCreatedfrom", query = "SELECT c FROM Crmproductdependency c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Crmproductdependency.findByChangeddate", query = "SELECT c FROM Crmproductdependency c WHERE c.changeddate = :changeddate"),
    @NamedQuery(name = "Crmproductdependency.findByChangedfrom", query = "SELECT c FROM Crmproductdependency c WHERE c.changedfrom = :changedfrom"),
    @NamedQuery(name = "Crmproductdependency.findByProductnr", query = "SELECT c FROM Crmproductdependency c WHERE c.productnr = :productnr"),
    @NamedQuery(name = "Crmproductdependency.findByProductname", query = "SELECT c FROM Crmproductdependency c WHERE c.productname = :productname")})
public class Crmproductdependency implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "DEPENDENCYID")
    private Integer dependencyid;
    @Column(name = "PRODUCTIDID")
    private Integer productidid;
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
    @JoinColumn(name = "PRODUCTID", referencedColumnName = "PRODUCTID")
    @ManyToOne
    private Crmproduct crmproduct;

    public Crmproductdependency() {}

    public Crmproductdependency(Integer dependencyid) {
        this.dependencyid = dependencyid;
    }

    public Integer getDependencyid() {
        return dependencyid;
    }

    public void setDependencyid(Integer dependencyid) {
        this.dependencyid = dependencyid;
    }

    public Integer getProductidid() {
        return productidid;
    }

    public void setProductidid(Integer productidid) {
        this.productidid = productidid;
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

    public Crmproduct getCrmproduct() {
        return crmproduct;
    }

    public void setCrmproduct(Crmproduct crmproduct) {
        this.crmproduct = crmproduct;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dependencyid != null ? dependencyid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Crmproductdependency)) {
            return false;
        }
        Crmproductdependency other = (Crmproductdependency) object;
        if ((this.dependencyid == null && other.dependencyid != null) || (this.dependencyid != null && !this.dependencyid.equals(other.dependencyid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Crmproductdependency[ dependencyid=" + dependencyid + " ]";
    }
    
}
