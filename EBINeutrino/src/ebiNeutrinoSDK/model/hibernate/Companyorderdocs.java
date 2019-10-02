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
@Table(name = "COMPANYORDERDOCS", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Companyorderdocs.findAll", query = "SELECT c FROM Companyorderdocs c"),
    @NamedQuery(name = "Companyorderdocs.findByOrderdocid", query = "SELECT c FROM Companyorderdocs c WHERE c.orderdocid = :orderdocid"),
    @NamedQuery(name = "Companyorderdocs.findByName", query = "SELECT c FROM Companyorderdocs c WHERE c.name = :name"),
    @NamedQuery(name = "Companyorderdocs.findByCreatedfrom", query = "SELECT c FROM Companyorderdocs c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Companyorderdocs.findByCreateddate", query = "SELECT c FROM Companyorderdocs c WHERE c.createddate = :createddate")})
public class Companyorderdocs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ORDERDOCID")
    private Integer orderdocid;
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
    @JoinColumn(name = "ORDERID", referencedColumnName = "ORDERID")
    @ManyToOne
    private Companyorder companyorder;

    public Companyorderdocs() { }

    public Companyorderdocs(Integer orderdocid) {
        this.orderdocid = orderdocid;
    }

    public Integer getOrderdocid() {
        return orderdocid;
    }

    public void setOrderdocid(Integer orderdocid) {
        this.orderdocid = orderdocid;
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

    public Companyorder getCompanyorder() {
        return companyorder;
    }

    public void setCompanyorder(Companyorder companyorder) {
        this.companyorder = companyorder;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orderdocid != null ? orderdocid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Companyorderdocs)) {
            return false;
        }
        Companyorderdocs other = (Companyorderdocs) object;
        if ((this.orderdocid == null && other.orderdocid != null) || (this.orderdocid != null && !this.orderdocid.equals(other.orderdocid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Companyorderdocs[ orderdocid=" + orderdocid + " ]";
    }
    
}
