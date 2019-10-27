package org.sdk.model.hibernate;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "CRMINVOICENUMBER", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Crminvoicenumber.findAll", query = "SELECT c FROM Crminvoicenumber c"),
    @NamedQuery(name = "Crminvoicenumber.findById", query = "SELECT c FROM Crminvoicenumber c WHERE c.id = :id"),
    @NamedQuery(name = "Crminvoicenumber.findByCategory", query = "SELECT c FROM Crminvoicenumber c WHERE c.category = :category"),
    @NamedQuery(name = "Crminvoicenumber.findByBeginchar", query = "SELECT c FROM Crminvoicenumber c WHERE c.beginchar = :beginchar"),
    @NamedQuery(name = "Crminvoicenumber.findByNumberfrom", query = "SELECT c FROM Crminvoicenumber c WHERE c.numberfrom = :numberfrom"),
    @NamedQuery(name = "Crminvoicenumber.findByNumberto", query = "SELECT c FROM Crminvoicenumber c WHERE c.numberto = :numberto")})
public class Crminvoicenumber implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "CATEGORY")
    private String category;
    @Column(name = "BEGINCHAR")
    private String beginchar;
    @Column(name = "NUMBERFROM")
    private Integer numberfrom;
    @Column(name = "NUMBERTO")
    private Integer numberto;
    @JoinColumn(name = "CATEGORYID", referencedColumnName = "ID")
    @ManyToOne
    private Crminvoicecategory crminvoicecategory;

    public Crminvoicenumber() { }

    public Crminvoicenumber(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBeginchar() {
        return beginchar;
    }

    public void setBeginchar(String beginchar) {
        this.beginchar = beginchar;
    }

    public Integer getNumberfrom() {
        return numberfrom;
    }

    public void setNumberfrom(Integer numberfrom) {
        this.numberfrom = numberfrom;
    }

    public Integer getNumberto() {
        return numberto;
    }

    public void setNumberto(Integer numberto) {
        this.numberto = numberto;
    }

    public Crminvoicecategory getCrminvoicecategory() {
        return crminvoicecategory;
    }

    public void setCrminvoicecategory(Crminvoicecategory crminvoicecategory) {
        this.crminvoicecategory = crminvoicecategory;
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
        if (!(object instanceof Crminvoicenumber)) {
            return false;
        }
        Crminvoicenumber other = (Crminvoicenumber) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Crminvoicenumber[ id=" + id + " ]";
    }
    
}
