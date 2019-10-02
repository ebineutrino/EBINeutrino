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
@Table(name = "ACCOUNTSTACKDOCS", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Accountstackdocs.findAll", query = "SELECT a FROM Accountstackdocs a"),
    @NamedQuery(name = "Accountstackdocs.findByAccountdocid", query = "SELECT a FROM Accountstackdocs a WHERE a.accountdocid = :accountdocid"),
    @NamedQuery(name = "Accountstackdocs.findByName", query = "SELECT a FROM Accountstackdocs a WHERE a.name = :name"),
    @NamedQuery(name = "Accountstackdocs.findByCreateddate", query = "SELECT a FROM Accountstackdocs a WHERE a.createddate = :createddate"),
    @NamedQuery(name = "Accountstackdocs.findByCreatedfrom", query = "SELECT a FROM Accountstackdocs a WHERE a.createdfrom = :createdfrom")})
public class Accountstackdocs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ACCOUNTDOCID")
    private Integer accountdocid;
    @Column(name = "NAME")
    private String name;
    @Lob
    @Column(name = "FILES")
    private byte[] files;
    @Column(name = "CREATEDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createddate;
    @Column(name = "CREATEDFROM")
    private String createdfrom;
    @JoinColumn(name = "ACCOUNTID", referencedColumnName = "ACSTACKID")
    @ManyToOne
    private Accountstack accountstack;

    public Accountstackdocs() { }

    public Accountstackdocs(Integer accountdocid) {
        this.accountdocid = accountdocid;
    }

    public Integer getAccountdocid() {
        return accountdocid;
    }

    public void setAccountdocid(Integer accountdocid) {
        this.accountdocid = accountdocid;
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

    public Accountstack getAccountstack() {
        return accountstack;
    }

    public void setAccountstack(Accountstack accountstack) {
        this.accountstack = accountstack;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accountdocid != null ? accountdocid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Accountstackdocs)) {
            return false;
        }
        Accountstackdocs other = (Accountstackdocs) object;
        if ((this.accountdocid == null && other.accountdocid != null) || (this.accountdocid != null && !this.accountdocid.equals(other.accountdocid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Accountstackdocs[ accountdocid=" + accountdocid + " ]";
    }
    
}
