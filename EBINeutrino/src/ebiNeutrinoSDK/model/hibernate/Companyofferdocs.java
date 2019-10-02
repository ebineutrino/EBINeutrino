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
@Table(name = "COMPANYOFFERDOCS", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Companyofferdocs.findAll", query = "SELECT c FROM Companyofferdocs c"),
    @NamedQuery(name = "Companyofferdocs.findByOfferdocid", query = "SELECT c FROM Companyofferdocs c WHERE c.offerdocid = :offerdocid"),
    @NamedQuery(name = "Companyofferdocs.findByName", query = "SELECT c FROM Companyofferdocs c WHERE c.name = :name"),
    @NamedQuery(name = "Companyofferdocs.findByCreateddate", query = "SELECT c FROM Companyofferdocs c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Companyofferdocs.findByCreatedfrom", query = "SELECT c FROM Companyofferdocs c WHERE c.createdfrom = :createdfrom")})
public class Companyofferdocs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "OFFERDOCID")
    private Integer offerdocid;
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
    @JoinColumn(name = "OFFERID", referencedColumnName = "OFFERID")
    @ManyToOne
    private Companyoffer companyoffer;

    public Companyofferdocs(){}

    public Companyofferdocs(Integer offerdocid) {
        this.offerdocid = offerdocid;
    }

    public Integer getOfferdocid() {
        return offerdocid;
    }

    public void setOfferdocid(Integer offerdocid) {
        this.offerdocid = offerdocid;
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

    public Companyoffer getCompanyoffer() {
        return companyoffer;
    }

    public void setCompanyoffer(Companyoffer companyoffer) {
        this.companyoffer = companyoffer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (offerdocid != null ? offerdocid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Companyofferdocs)) {
            return false;
        }
        Companyofferdocs other = (Companyofferdocs) object;
        if ((this.offerdocid == null && other.offerdocid != null) || (this.offerdocid != null && !this.offerdocid.equals(other.offerdocid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Companyofferdocs[ offerdocid=" + offerdocid + " ]";
    }
    
}
