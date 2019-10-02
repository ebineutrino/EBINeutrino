package ebiNeutrinoSDK.model.hibernate;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "EBIUSER", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ebiuser.findAll", query = "SELECT e FROM Ebiuser e"),
    @NamedQuery(name = "Ebiuser.findById", query = "SELECT e FROM Ebiuser e WHERE e.id = :id"),
    @NamedQuery(name = "Ebiuser.findByCreatedfrom", query = "SELECT e FROM Ebiuser e WHERE e.createdfrom = :createdfrom"),
    @NamedQuery(name = "Ebiuser.findByCreateddate", query = "SELECT e FROM Ebiuser e WHERE e.createddate = :createddate"),
    @NamedQuery(name = "Ebiuser.findByChangedfrom", query = "SELECT e FROM Ebiuser e WHERE e.changedfrom = :changedfrom"),
    @NamedQuery(name = "Ebiuser.findByChangeddate", query = "SELECT e FROM Ebiuser e WHERE e.changeddate = :changeddate"),
    @NamedQuery(name = "Ebiuser.findByEbiuser", query = "SELECT e FROM Ebiuser e WHERE e.ebiuser = :ebiuser"),
    @NamedQuery(name = "Ebiuser.findByPasswd", query = "SELECT e FROM Ebiuser e WHERE e.passwd = :passwd"),
    @NamedQuery(name = "Ebiuser.findByIsAdmin", query = "SELECT e FROM Ebiuser e WHERE e.isAdmin = :isAdmin"),
    @NamedQuery(name = "Ebiuser.findByCansave", query = "SELECT e FROM Ebiuser e WHERE e.cansave = :cansave"),
    @NamedQuery(name = "Ebiuser.findByCanprint", query = "SELECT e FROM Ebiuser e WHERE e.canprint = :canprint"),
    @NamedQuery(name = "Ebiuser.findByCandelete", query = "SELECT e FROM Ebiuser e WHERE e.candelete = :candelete")})
public class Ebiuser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
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
    @Column(name = "EBIUSER")
    private String ebiuser;
    @Column(name = "PASSWD")
    private String passwd;
    @Column(name = "IS_ADMIN")
    private Boolean isAdmin;
    @Lob
    @Column(name = "MODULEID")
    private String moduleid;
    @Column(name = "CANSAVE")
    private Boolean cansave;
    @Column(name = "CANPRINT")
    private Boolean canprint;
    @Column(name = "CANDELETE")
    private Boolean candelete;

    public Ebiuser() {}

    public Ebiuser(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getEbiuser() {
        return ebiuser;
    }

    public void setEbiuser(String ebiuser) {
        this.ebiuser = ebiuser;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getModuleid() {
        return moduleid;
    }

    public void setModuleid(String moduleid) {
        this.moduleid = moduleid;
    }

    public Boolean getCansave() {
        return cansave;
    }

    public void setCansave(Boolean cansave) {
        this.cansave = cansave;
    }

    public Boolean getCanprint() {
        return canprint;
    }

    public void setCanprint(Boolean canprint) {
        this.canprint = canprint;
    }

    public Boolean getCandelete() {
        return candelete;
    }

    public void setCandelete(Boolean candelete) {
        this.candelete = candelete;
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
        if (!(object instanceof Ebiuser)) {
            return false;
        }
        Ebiuser other = (Ebiuser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Ebiuser[ id=" + id + " ]";
    }
    
}
