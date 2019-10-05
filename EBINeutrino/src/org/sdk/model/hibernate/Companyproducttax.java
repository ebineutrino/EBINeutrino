package org.sdk.model.hibernate;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "COMPANYPRODUCTTAX", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Companyproducttax.findAll", query = "SELECT c FROM Companyproducttax c"),
    @NamedQuery(name = "Companyproducttax.findById", query = "SELECT c FROM Companyproducttax c WHERE c.id = :id"),
    @NamedQuery(name = "Companyproducttax.findByName", query = "SELECT c FROM Companyproducttax c WHERE c.name = :name"),
    @NamedQuery(name = "Companyproducttax.findByTaxvalue", query = "SELECT c FROM Companyproducttax c WHERE c.taxvalue = :taxvalue"),
    @NamedQuery(name = "Companyproducttax.findByCreateddate", query = "SELECT c FROM Companyproducttax c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Companyproducttax.findByCreatedfrom", query = "SELECT c FROM Companyproducttax c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Companyproducttax.findByChangeddate", query = "SELECT c FROM Companyproducttax c WHERE c.changeddate = :changeddate"),
    @NamedQuery(name = "Companyproducttax.findByChangedfrom", query = "SELECT c FROM Companyproducttax c WHERE c.changedfrom = :changedfrom")})
public class Companyproducttax implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME")
    private String name;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "TAXVALUE")
    private Double taxvalue;
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

    public Companyproducttax(){}

    public Companyproducttax(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTaxvalue() {
        return taxvalue;
    }

    public void setTaxvalue(Double taxvalue) {
        this.taxvalue = taxvalue;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Companyproducttax)) {
            return false;
        }
        Companyproducttax other = (Companyproducttax) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Companyproducttax[ id=" + id + " ]";
    }
    
}
