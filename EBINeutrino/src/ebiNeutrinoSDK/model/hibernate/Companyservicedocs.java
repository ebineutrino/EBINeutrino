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
@Table(name = "COMPANYSERVICEDOCS", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Companyservicedocs.findAll", query = "SELECT c FROM Companyservicedocs c"),
    @NamedQuery(name = "Companyservicedocs.findByServicedocid", query = "SELECT c FROM Companyservicedocs c WHERE c.servicedocid = :servicedocid"),
    @NamedQuery(name = "Companyservicedocs.findByName", query = "SELECT c FROM Companyservicedocs c WHERE c.name = :name"),
    @NamedQuery(name = "Companyservicedocs.findByCreatedfrom", query = "SELECT c FROM Companyservicedocs c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Companyservicedocs.findByCreateddate", query = "SELECT c FROM Companyservicedocs c WHERE c.createddate = :createddate")})
public class Companyservicedocs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "SERVICEDOCID")
    private Integer servicedocid;
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
    @JoinColumn(name = "SERVICEID", referencedColumnName = "SERVICEID")
    @ManyToOne
    private Companyservice companyservice;

    public Companyservicedocs() { }

    public Companyservicedocs(Integer servicedocid) {
        this.servicedocid = servicedocid;
    }

    public Integer getServicedocid() {
        return servicedocid;
    }

    public void setServicedocid(Integer servicedocid) {
        this.servicedocid = servicedocid;
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

    public Companyservice getCompanyservice() {
        return companyservice;
    }

    public void setCompanyservice(Companyservice companyservice) {
        this.companyservice = companyservice;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (servicedocid != null ? servicedocid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Companyservicedocs)) {
            return false;
        }
        Companyservicedocs other = (Companyservicedocs) object;
        if ((this.servicedocid == null && other.servicedocid != null) || (this.servicedocid != null && !this.servicedocid.equals(other.servicedocid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Companyservicedocs[ servicedocid=" + servicedocid + " ]";
    }
    
}
