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
@Table(name = "CRMPRODUCTDIMENSION", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Crmproductdimension.findAll", query = "SELECT c FROM Crmproductdimension c"),
    @NamedQuery(name = "Crmproductdimension.findByDimensionid", query = "SELECT c FROM Crmproductdimension c WHERE c.dimensionid = :dimensionid"),
    @NamedQuery(name = "Crmproductdimension.findByCreateddate", query = "SELECT c FROM Crmproductdimension c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Crmproductdimension.findByCreatedfrom", query = "SELECT c FROM Crmproductdimension c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Crmproductdimension.findByChangeddate", query = "SELECT c FROM Crmproductdimension c WHERE c.changeddate = :changeddate"),
    @NamedQuery(name = "Crmproductdimension.findByChangedfrom", query = "SELECT c FROM Crmproductdimension c WHERE c.changedfrom = :changedfrom"),
    @NamedQuery(name = "Crmproductdimension.findByName", query = "SELECT c FROM Crmproductdimension c WHERE c.dimensionName = :dimensionName"),
    @NamedQuery(name = "Crmproductdimension.findByValue", query = "SELECT c FROM Crmproductdimension c WHERE c.dimensionValue = :dimensionValue")})
public class Crmproductdimension implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "DIMENSIONID")
    private Integer dimensionid;
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
    @Column(name = "DIMENSIONNAME")
    private String dimensionName;
    @Column(name = "DIMENSIONVALUE")
    private String dimensionValue;
    @JoinColumn(name = "PRODUCTID", referencedColumnName = "PRODUCTID")
    @ManyToOne
    private Crmproduct crmproduct;

    public Crmproductdimension() {}

    public Crmproductdimension(Integer dimensionid) {
        this.dimensionid = dimensionid;
    }

    public Integer getDimensionid() {
        return dimensionid;
    }

    public void setDimensionid(Integer dimensionid) {
        this.dimensionid = dimensionid;
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

    public String getDimensionName() {
        return this.dimensionName;
    }

    public void setDimensionName(String name) {
        this.dimensionName = name;
    }

    public String getDimensionValue() {
        return dimensionValue;
    }

    public void setDimensionValue(String value) {
        this.dimensionValue = value;
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
        hash += (dimensionid != null ? dimensionid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Crmproductdimension)) {
            return false;
        }
        Crmproductdimension other = (Crmproductdimension) object;
        if ((this.dimensionid == null && other.dimensionid != null) || (this.dimensionid != null && !this.dimensionid.equals(other.dimensionid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.sdk.model.hibernate.Crmproductdimension[ dimensionid=" + dimensionid + " ]";
    }
    
}
