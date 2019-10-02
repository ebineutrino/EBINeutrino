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
@Table(name = "CRMPROBLEMSOLDOCS", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Crmproblemsoldocs.findAll", query = "SELECT c FROM Crmproblemsoldocs c"),
    @NamedQuery(name = "Crmproblemsoldocs.findBySolutiondocid", query = "SELECT c FROM Crmproblemsoldocs c WHERE c.solutiondocid = :solutiondocid"),
    @NamedQuery(name = "Crmproblemsoldocs.findByName", query = "SELECT c FROM Crmproblemsoldocs c WHERE c.name = :name"),
    @NamedQuery(name = "Crmproblemsoldocs.findByCreatedfrom", query = "SELECT c FROM Crmproblemsoldocs c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Crmproblemsoldocs.findByCreateddate", query = "SELECT c FROM Crmproblemsoldocs c WHERE c.createddate = :createddate")})
public class Crmproblemsoldocs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "SOLUTIONDOCID")
    private Integer solutiondocid;
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
    @JoinColumn(name = "SOLUTIONID", referencedColumnName = "PROSOLID")
    @ManyToOne
    private Crmproblemsolutions crmproblemsolutions;

    public Crmproblemsoldocs() {}

    public Crmproblemsoldocs(Integer solutiondocid) {
        this.solutiondocid = solutiondocid;
    }

    public Integer getSolutiondocid() {
        return solutiondocid;
    }

    public void setSolutiondocid(Integer solutiondocid) {
        this.solutiondocid = solutiondocid;
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

    public Crmproblemsolutions getCrmproblemsolutions() {
        return crmproblemsolutions;
    }

    public void setCrmproblemsolutions(Crmproblemsolutions crmproblemsolutions) {
        this.crmproblemsolutions = crmproblemsolutions;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (solutiondocid != null ? solutiondocid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Crmproblemsoldocs)) {
            return false;
        }
        Crmproblemsoldocs other = (Crmproblemsoldocs) object;
        if ((this.solutiondocid == null && other.solutiondocid != null) || (this.solutiondocid != null && !this.solutiondocid.equals(other.solutiondocid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Crmproblemsoldocs[ solutiondocid=" + solutiondocid + " ]";
    }
    
}
