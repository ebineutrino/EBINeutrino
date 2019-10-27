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
@Table(name = "COMPANYHIRARCHIE", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Companyhirarchie.findAll", query = "SELECT c FROM Companyhirarchie c"),
    @NamedQuery(name = "Companyhirarchie.findByHierarchieid", query = "SELECT c FROM Companyhirarchie c WHERE c.hierarchieid = :hierarchieid"),
    @NamedQuery(name = "Companyhirarchie.findByName", query = "SELECT c FROM Companyhirarchie c WHERE c.name = :name"),
    @NamedQuery(name = "Companyhirarchie.findByRoot", query = "SELECT c FROM Companyhirarchie c WHERE c.root = :root"),
    @NamedQuery(name = "Companyhirarchie.findByParent", query = "SELECT c FROM Companyhirarchie c WHERE c.parent = :parent"),
    @NamedQuery(name = "Companyhirarchie.findByCreatedfrom", query = "SELECT c FROM Companyhirarchie c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Companyhirarchie.findByCreateddate", query = "SELECT c FROM Companyhirarchie c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Companyhirarchie.findByChangedfrom", query = "SELECT c FROM Companyhirarchie c WHERE c.changedfrom = :changedfrom"),
    @NamedQuery(name = "Companyhirarchie.findByChangeddate", query = "SELECT c FROM Companyhirarchie c WHERE c.changeddate = :changeddate")})
public class Companyhirarchie implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "HIERARCHIEID")
    private Integer hierarchieid;
    @Column(name = "NAME")
    private String name;
    @Column(name = "ROOT")
    private Integer root;
    @Column(name = "PARENT")
    private Integer parent;
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

    public Companyhirarchie() { }

    public Companyhirarchie(Integer hierarchieid) {
        this.hierarchieid = hierarchieid;
    }

    public Integer getHierarchieid() {
        return hierarchieid;
    }

    public void setHierarchieid(Integer hierarchieid) {
        this.hierarchieid = hierarchieid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRoot() {
        return root;
    }

    public void setRoot(Integer root) {
        this.root = root;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
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
        hash += (hierarchieid != null ? hierarchieid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Companyhirarchie)) {
            return false;
        }
        Companyhirarchie other = (Companyhirarchie) object;
        if ((this.hierarchieid == null && other.hierarchieid != null) || (this.hierarchieid != null && !this.hierarchieid.equals(other.hierarchieid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Companyhirarchie[ hierarchieid=" + hierarchieid + " ]";
    }
    
}
