package ebiNeutrinoSDK.model.hibernate;

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
@Table(name = "ACCOUNTSTACKCD", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Accountstackcd.findAll", query = "SELECT a FROM Accountstackcd a"),
    @NamedQuery(name = "Accountstackcd.findByAccountstackcdid", query = "SELECT a FROM Accountstackcd a WHERE a.accountstackcdid = :accountstackcdid"),
    @NamedQuery(name = "Accountstackcd.findByAccountstackid", query = "SELECT a FROM Accountstackcd a WHERE a.accountstackid = :accountstackid"),
    @NamedQuery(name = "Accountstackcd.findByCreditdebitnumber", query = "SELECT a FROM Accountstackcd a WHERE a.creditdebitnumber = :creditdebitnumber"),
    @NamedQuery(name = "Accountstackcd.findByCreditdebitname", query = "SELECT a FROM Accountstackcd a WHERE a.creditdebitname = :creditdebitname"),
    @NamedQuery(name = "Accountstackcd.findByCreditdebitvalue", query = "SELECT a FROM Accountstackcd a WHERE a.creditdebitvalue = :creditdebitvalue"),
    @NamedQuery(name = "Accountstackcd.findByCreditdebittaxtname", query = "SELECT a FROM Accountstackcd a WHERE a.creditdebittaxtname = :creditdebittaxtname"),
    @NamedQuery(name = "Accountstackcd.findByCreditdebittype", query = "SELECT a FROM Accountstackcd a WHERE a.creditdebittype = :creditdebittype"),
    @NamedQuery(name = "Accountstackcd.findByCreatedfrom", query = "SELECT a FROM Accountstackcd a WHERE a.createdfrom = :createdfrom"),
    @NamedQuery(name = "Accountstackcd.findByCreateddate", query = "SELECT a FROM Accountstackcd a WHERE a.createddate = :createddate")})
public class Accountstackcd implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ACCOUNTSTACKCDID")
    private Integer accountstackcdid;
    @Column(name = "ACCOUNTSTACKID")
    private Integer accountstackid;
    @Column(name = "CREDITDEBITNUMBER")
    private String creditdebitnumber;
    @Column(name = "CREDITDEBITNAME")
    private String creditdebitname;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "CREDITDEBITVALUE")
    private Double creditdebitvalue;
    @Column(name = "CREDITDEBITTAXTNAME")
    private String creditdebittaxtname;
    @Column(name = "CREDITDEBITTYPE")
    private Integer creditdebittype;
    @Column(name = "CREATEDFROM")
    private String createdfrom;
    @Column(name = "CREATEDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createddate;

    public Accountstackcd() { }

    public Accountstackcd(Integer accountstackcdid) {
        this.accountstackcdid = accountstackcdid;
    }

    public Integer getAccountstackcdid() {
        return accountstackcdid;
    }

    public void setAccountstackcdid(Integer accountstackcdid) {
        this.accountstackcdid = accountstackcdid;
    }

    public Integer getAccountstackid() {
        return accountstackid;
    }

    public void setAccountstackid(Integer accountstackid) {
        this.accountstackid = accountstackid;
    }

    public String getCreditdebitnumber() {
        return creditdebitnumber;
    }

    public void setCreditdebitnumber(String creditdebitnumber) {
        this.creditdebitnumber = creditdebitnumber;
    }

    public String getCreditdebitname() {
        return creditdebitname;
    }

    public void setCreditdebitname(String creditdebitname) {
        this.creditdebitname = creditdebitname;
    }

    public Double getCreditdebitvalue() {
        return creditdebitvalue;
    }

    public void setCreditdebitvalue(Double creditdebitvalue) {
        this.creditdebitvalue = creditdebitvalue;
    }

    public String getCreditdebittaxtname() {
        return creditdebittaxtname;
    }

    public void setCreditdebittaxtname(String creditdebittaxtname) {
        this.creditdebittaxtname = creditdebittaxtname;
    }

    public Integer getCreditdebittype() {
        return creditdebittype;
    }

    public void setCreditdebittype(Integer creditdebittype) {
        this.creditdebittype = creditdebittype;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accountstackcdid != null ? accountstackcdid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Accountstackcd)) {
            return false;
        }
        Accountstackcd other = (Accountstackcd) object;
        if ((this.accountstackcdid == null && other.accountstackcdid != null) || (this.accountstackcdid != null && !this.accountstackcdid.equals(other.accountstackcdid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Accountstackcd[ accountstackcdid=" + accountstackcdid + " ]";
    }
    
}
